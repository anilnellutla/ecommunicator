package com.ecommunicator.client.net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * High-level ECP client.
 *
 * Wraps:
 *   - {@link StompClient} for control messages (whiteboard, chat, signaling, polls)
 *   - A raw binary WebSocketClient for media frames (audio, screen share)
 *
 * Usage:
 * <pre>
 *   EcpClient client = new EcpClient();
 *   client.connect("ws://localhost:8080/ws-native", () -> {
 *       client.joinSession("mtg-abc", "Alice", null);
 *   });
 *   client.router().on("CHAT_MESSAGE", msg -> updateChatUI(msg));
 * </pre>
 */
public class EcpClient {

    private static final Logger log = LoggerFactory.getLogger(EcpClient.class);

    private final ObjectMapper mapper;
    private final MessageRouter router;

    private StompClient stomp;
    private WebSocketClient mediaWs;

    // Identity (set on join)
    private String myId;
    private String myName;
    private String myRole;
    private String sessionId;

    public EcpClient() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.router = new MessageRouter(mapper);
    }

    // ── Connect ───────────────────────────────────────────────────────────────

    /**
     * Connect to the server and invoke {@code onConnected} once the STOMP handshake succeeds.
     *
     * @param serverWsUrl  e.g. "ws://localhost:8080/ws-native"
     * @param onConnected  callback on successful STOMP CONNECTED frame
     */
    public void connect(String serverWsUrl, Runnable onConnected) {
        try {
            URI uri = URI.create(serverWsUrl);
            stomp = new StompClient(uri);
            stomp.setOnError(err -> log.error("STOMP error: {}", err));
            stomp.setOnDisconnect(reason -> log.info("Disconnected: {}", reason));

            stomp.connect(null, null, () -> {
                log.info("Control channel connected");
                onConnected.run();
            });

            // Also connect the media WebSocket
            connectMedia(serverWsUrl.replace("/ws-native", "/media"));

        } catch (Exception e) {
            log.error("Failed to connect", e);
        }
    }

    private void connectMedia(String mediaUrl) {
        try {
            mediaWs = new WebSocketClient(URI.create(mediaUrl)) {
                @Override public void onOpen(ServerHandshake hs) {
                    log.debug("Media channel connected");
                }
                @Override public void onMessage(String text) {}
                @Override public void onMessage(ByteBuffer bytes) {
                    // Dispatch binary media frame to registered binary handler
                    router.dispatch("{\"header\":{\"type\":\"MEDIA_FRAME\"}}");
                    // TODO: route actual binary frame to audio/video renderers
                }
                @Override public void onClose(int code, String reason, boolean remote) {
                    log.debug("Media WS closed: {}", reason);
                }
                @Override public void onError(Exception ex) {
                    log.warn("Media WS error: {}", ex.getMessage());
                }
            };
            mediaWs.connect();
        } catch (Exception e) {
            log.warn("Could not connect media channel: {}", e.getMessage());
        }
    }

    // ── Session operations ────────────────────────────────────────────────────

    /**
     * Join (or create) a session.
     * Subscribes to session topic and private queue, then sends SESSION_JOIN.
     *
     * @param sessionId   target session ID
     * @param displayName local user's display name
     * @param password    optional session password (null = open)
     */
    public void joinSession(String sessionId, String displayName, String password) {
        this.sessionId = sessionId;
        this.myName    = displayName;
        this.myId      = UUID.randomUUID().toString();

        // Generate a one-time token so the server can route SESSION_STATE back to us
        // without relying on Spring's user-destination mechanism (which requires auth).
        String initToken = UUID.randomUUID().toString();

        // Subscribe to session broadcast topic
        stomp.subscribe("/topic/session/" + sessionId,
                frame -> router.dispatch(frame.body()));

        // Subscribe to our private init topic (server sends SESSION_STATE here)
        stomp.subscribe("/topic/session/" + sessionId + "/init/" + initToken,
                frame -> router.dispatch(frame.body()));

        // Subscribe to private queues (private chat, mute requests, errors)
        stomp.subscribe("/user/queue/private",
                frame -> router.dispatch(frame.body()));
        stomp.subscribe("/user/queue/errors",
                frame -> router.dispatch(frame.body()));

        // Send join message (include initToken so server knows where to reply)
        ObjectNode payload = mapper.createObjectNode();
        payload.put("displayName", displayName);
        payload.put("initToken", initToken);
        if (password != null) payload.put("password", password);

        stomp.send("/app/session.join", buildMessage("SESSION_JOIN", payload));
        log.info("Sent SESSION_JOIN for session {}", sessionId);
    }

    public void leaveSession() {
        if (sessionId == null) return;
        stomp.send("/app/session.leave", buildMessage("SESSION_LEAVE", mapper.createObjectNode()));
        sessionId = null;
    }

    // ── Whiteboard ────────────────────────────────────────────────────────────

    public void sendWhiteboardOp(ObjectNode opPayload) {
        stomp.send("/app/whiteboard.op", buildMessage("WHITEBOARD_OP", opPayload));
    }

    // ── Chat ──────────────────────────────────────────────────────────────────

    public void sendChat(String text, String toId) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("text", text);
        if (toId != null) payload.put("toId", toId);
        stomp.send("/app/chat.send", buildMessage("CHAT_MESSAGE", payload));
    }

    public void sendReaction(String emoji) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("emoji", emoji);
        stomp.send("/app/reaction", buildMessage("REACTION", payload));
    }

    public void raiseHand(boolean raised) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("raised", raised);
        stomp.send("/app/hand.raise", buildMessage("HAND_RAISE", payload));
    }

    // ── Signaling ─────────────────────────────────────────────────────────────

    public void sendMediaOffer(String targetId, String sdp) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("targetId", targetId);
        payload.put("sdp", sdp);
        stomp.send("/app/signaling.offer", buildMessage("MEDIA_OFFER", payload));
    }

    public void sendMediaAnswer(String targetId, String sdp) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("targetId", targetId);
        payload.put("sdp", sdp);
        stomp.send("/app/signaling.answer", buildMessage("MEDIA_ANSWER", payload));
    }

    public void sendIceCandidate(String targetId, String candidate, String sdpMid, int sdpMLineIndex) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("targetId", targetId);
        payload.put("candidate", candidate);
        payload.put("sdpMid", sdpMid);
        payload.put("sdpMLineIndex", sdpMLineIndex);
        stomp.send("/app/signaling.ice", buildMessage("ICE_CANDIDATE", payload));
    }

    public void sendMediaState(boolean audioMuted, boolean videoMuted, boolean screenSharing) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("audioMuted", audioMuted);
        payload.put("videoMuted", videoMuted);
        payload.put("screenSharing", screenSharing);
        stomp.send("/app/signaling.media-state", buildMessage("MEDIA_STATE_CHANGED", payload));
    }

    // ── Polls ─────────────────────────────────────────────────────────────────

    public void createPoll(String question, java.util.List<String> options,
                           boolean multiSelect, boolean anonymous, Integer durationSec) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("question", question);
        payload.put("multiSelect", multiSelect);
        payload.put("anonymous", anonymous);
        if (durationSec != null) payload.put("durationSec", durationSec);
        var opts = mapper.createArrayNode();
        options.forEach(opts::add);
        payload.set("options", opts);
        stomp.send("/app/poll.create", buildMessage("POLL_CREATE", payload));
    }

    public void votePoll(String pollId, java.util.List<Integer> choices) {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("pollId", pollId);
        var choicesNode = mapper.createArrayNode();
        choices.forEach(choicesNode::add);
        payload.set("choices", choicesNode);
        stomp.send("/app/poll.vote", buildMessage("POLL_VOTE", payload));
    }

    // ── Media (binary) ────────────────────────────────────────────────────────

    /**
     * Send a binary media frame over the media WebSocket.
     * See PROTOCOL.md for the binary frame format.
     */
    public void sendMediaFrame(byte frameType, byte[] mediaPayload) {
        if (mediaWs == null || !mediaWs.isOpen() || myId == null) return;
        try {
            UUID uuid   = UUID.fromString(myId);
            byte[] buf  = new byte[35 + mediaPayload.length];
            // Magic
            buf[0] = (byte)0xEC; buf[1] = 0x02; buf[2] = 0x05; buf[3] = 0x00;
            buf[4] = frameType;
            // UUID big-endian
            long msb = uuid.getMostSignificantBits();
            long lsb = uuid.getLeastSignificantBits();
            for (int i = 0; i < 8; i++) {
                buf[5  + i] = (byte)(msb >>> (56 - 8*i));
                buf[13 + i] = (byte)(lsb >>> (56 - 8*i));
            }
            // Session hash (simple CRC32)
            java.util.zip.CRC32 crc = new java.util.zip.CRC32();
            if (sessionId != null) crc.update(sessionId.getBytes());
            int hash = (int) crc.getValue();
            buf[21] = (byte)(hash >>> 24);
            buf[22] = (byte)(hash >>> 16);
            buf[23] = (byte)(hash >>> 8);
            buf[24] = (byte)hash;
            // Sequence (incremented per call, simplified: just use currentTimeMillis low bits)
            int seq = (int)(System.currentTimeMillis() & 0xFFFFFFFFL);
            buf[25] = (byte)(seq >>> 24);
            buf[26] = (byte)(seq >>> 16);
            buf[27] = (byte)(seq >>> 8);
            buf[28] = (byte)seq;
            // Timestamp (ms, truncated to 4 bytes)
            buf[29] = buf[25]; buf[30] = buf[26]; buf[31] = buf[27]; buf[32] = buf[28];
            // Payload length
            int len = mediaPayload.length;
            buf[33] = (byte)(len >>> 8);
            buf[34] = (byte)len;
            System.arraycopy(mediaPayload, 0, buf, 35, len);
            mediaWs.send(ByteBuffer.wrap(buf));
        } catch (Exception e) {
            log.warn("Failed to send media frame: {}", e.getMessage());
        }
    }

    // ── Disconnect ────────────────────────────────────────────────────────────

    public void disconnect() {
        if (stomp != null) stomp.disconnect();
        if (mediaWs != null) mediaWs.close();
        router.shutdown();
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public MessageRouter router()  { return router; }
    public ObjectMapper  mapper()  { return mapper; }
    public String        getMyId() { return myId; }
    public void          setMyId(String id) { this.myId = id; }
    public String        getMyName()  { return myName; }
    public String        getMyRole()  { return myRole; }
    public void          setMyRole(String r) { this.myRole = r; }
    public String        getSessionId(){ return sessionId; }
    public boolean       isConnected(){ return stomp != null && stomp.isConnected(); }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Build a complete ECP message envelope JSON string. */
    public String buildMessage(String type, ObjectNode payload) {
        ObjectNode msg    = mapper.createObjectNode();
        ObjectNode header = mapper.createObjectNode();
        header.put("version",    2);
        header.put("type",       type);
        header.put("messageId",  UUID.randomUUID().toString());
        header.put("sessionId",  sessionId != null ? sessionId : "");
        header.put("senderId",   myId != null ? myId : "");
        header.put("senderName", myName != null ? myName : "");
        header.put("role",       myRole != null ? myRole : "ATTENDEE");
        header.put("timestamp",  Instant.now().toString());
        msg.set("header", header);
        msg.set("payload", payload);
        try {
            return mapper.writeValueAsString(msg);
        } catch (Exception e) {
            log.error("Failed to serialize message", e);
            return "{}";
        }
    }
}
