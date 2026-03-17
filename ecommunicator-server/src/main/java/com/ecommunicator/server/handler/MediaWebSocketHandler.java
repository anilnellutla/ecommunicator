package com.ecommunicator.server.handler;

import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

/**
 * Raw binary WebSocket handler for real-time media frames.
 *
 * Binary frame format (see PROTOCOL.md):
 *   [4]  Magic:     0xEC 0x02 0x05 0x00
 *   [1]  FrameType: 0x01=SCREEN_SHARE, 0x02=AUDIO, 0x03=VIDEO
 *   [16] SenderId:  UUID bytes (big-endian)
 *   [4]  SessionHash: CRC32 of sessionId string
 *   [4]  Sequence:  uint32 big-endian
 *   [4]  Timestamp: ms since session start
 *   [2]  PayloadLen: uint16 big-endian
 *   [N]  Payload:   JPEG (screen/video) | PCM s16le 16kHz mono (audio)
 *
 * The server receives frames from one sender and broadcasts to all other
 * participants in the same session.
 */
@Component
public class MediaWebSocketHandler extends BinaryWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MediaWebSocketHandler.class);

    private static final byte[] MAGIC = {(byte)0xEC, 0x02, 0x05, 0x00};
    private static final int HEADER_LEN = 4 + 1 + 16 + 4 + 4 + 4 + 2; // 35 bytes

    private static final byte FRAME_SCREEN = 0x01;
    private static final byte FRAME_AUDIO  = 0x02;
    private static final byte FRAME_VIDEO  = 0x03;

    private final SessionService sessionService;
    private final Executor mediaExecutor;

    /** WebSocket session ID → WebSocket session */
    private final Map<String, WebSocketSession> wsSessions = new ConcurrentHashMap<>();

    /** WebSocket session ID → { participantId, sessionId } */
    private final Map<String, MediaSession> mediaSessions = new ConcurrentHashMap<>();

    public MediaWebSocketHandler(SessionService sessionService,
                                 @Qualifier("mediaExecutor") Executor mediaExecutor) {
        this.sessionService = sessionService;
        this.mediaExecutor  = mediaExecutor;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession ws) {
        wsSessions.put(ws.getId(), ws);
        log.debug("Media WS connected: {}", ws.getId());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession ws, BinaryMessage message) {
        ByteBuffer buf = message.getPayload();

        // Validate magic
        if (buf.remaining() < HEADER_LEN) return;
        byte[] magic = new byte[4];
        buf.get(magic);
        if (!Arrays.equals(magic, MAGIC)) {
            log.warn("Bad magic from {}", ws.getId());
            return;
        }

        byte frameType = buf.get();

        // Read sender UUID (16 bytes big-endian)
        long msb = buf.getLong();
        long lsb = buf.getLong();
        String senderId = new UUID(msb, lsb).toString();

        int sessionHash = buf.getInt();
        int sequence    = buf.getInt();
        int timestamp   = buf.getInt();
        int payloadLen  = buf.getShort() & 0xFFFF;

        if (buf.remaining() < payloadLen) return;
        byte[] payload = new byte[payloadLen];
        buf.get(payload);

        // Register this WS session for the sender if not already done
        mediaSessions.computeIfAbsent(ws.getId(), k -> {
            // Find which session this participant belongs to (by participant ID)
            for (Session s : sessionService.allSessions()) {
                if (s.findParticipant(senderId).isPresent()) {
                    return new MediaSession(senderId, s.getId());
                }
            }
            return new MediaSession(senderId, null);
        });

        MediaSession ms = mediaSessions.get(ws.getId());
        if (ms == null || ms.sessionId == null) return;

        // Rebuild the frame bytes verbatim and broadcast to all others in the session
        final byte[] frameBytes = message.getPayload().array().clone();

        mediaExecutor.execute(() -> broadcastToSession(ms.sessionId, ws.getId(), frameBytes));
    }

    private void broadcastToSession(String sessionId, String senderWsId, byte[] frameBytes) {
        BinaryMessage outMsg = new BinaryMessage(frameBytes);
        for (Map.Entry<String, MediaSession> entry : mediaSessions.entrySet()) {
            if (senderWsId.equals(entry.getKey())) continue;         // Don't echo back
            if (!sessionId.equals(entry.getValue().sessionId)) continue; // Different session

            WebSocketSession peer = wsSessions.get(entry.getKey());
            if (peer != null && peer.isOpen()) {
                try {
                    synchronized (peer) {
                        peer.sendMessage(outMsg);
                    }
                } catch (IOException e) {
                    log.warn("Failed to send media frame to {}: {}", entry.getKey(), e.getMessage());
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession ws, CloseStatus status) {
        wsSessions.remove(ws.getId());
        mediaSessions.remove(ws.getId());
        log.debug("Media WS disconnected: {}", ws.getId());
    }

    @Override
    public boolean supportsPartialMessages() { return false; }

    private record MediaSession(String participantId, String sessionId) {}
}
