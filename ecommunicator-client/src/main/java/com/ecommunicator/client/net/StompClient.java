package com.ecommunicator.client.net;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Lightweight STOMP 1.2 client over WebSocket.
 *
 * Supports: CONNECT, SUBSCRIBE, SEND, DISCONNECT, PING.
 * Handles: CONNECTED, MESSAGE, ERROR server frames.
 *
 * Usage:
 * <pre>
 *   StompClient stomp = new StompClient(URI.create("ws://host:8080/ws-native"));
 *   stomp.connect("guest", "guest", () -> {
 *       stomp.subscribe("/topic/session/mtg-123", frame -> handleMsg(frame));
 *       stomp.send("/app/session.join", headers, body);
 *   });
 * </pre>
 */
public class StompClient {

    private static final Logger log = LoggerFactory.getLogger(StompClient.class);

    private static final String NULL_BYTE = "\u0000";
    private static final byte   NULL      = 0x00;

    private final URI serverUri;
    private WebSocketClient ws;

    /** Subscription id → message callback */
    private final Map<String, Consumer<StompFrame>> subscriptions = new ConcurrentHashMap<>();
    private int subCounter = 0;

    private Runnable onConnected;
    private Consumer<String> onError;
    private Consumer<String> onDisconnect;

    private volatile boolean connected = false;
    private final CountDownLatch connectLatch = new CountDownLatch(1);

    public StompClient(URI serverUri) {
        this.serverUri = serverUri;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void connect(String login, String passcode, Runnable onConnected) {
        this.onConnected = onConnected;
        buildWebSocketClient();
        try {
            ws.connectBlocking(10, TimeUnit.SECONDS);
            // Send STOMP CONNECT frame
            sendRawFrame(buildConnectFrame(login, passcode));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("WebSocket connect interrupted", e);
        }
    }

    /**
     * Subscribe to a STOMP destination.
     *
     * @param destination e.g. "/topic/session/mtg-abc"
     * @param callback    called on every received MESSAGE
     * @return subscriptionId (use to unsubscribe)
     */
    public String subscribe(String destination, Consumer<StompFrame> callback) {
        String subId = "sub-" + (++subCounter);
        subscriptions.put(subId, callback);
        sendRawFrame(buildSubscribeFrame(destination, subId));
        log.debug("Subscribed to {} as {}", destination, subId);
        return subId;
    }

    /**
     * Subscribe to a user-private queue.
     * STOMP destination becomes /user/queue/xxx → /user/queue/xxx
     */
    public String subscribePrivate(String queue, Consumer<StompFrame> callback) {
        return subscribe("/user" + queue, callback);
    }

    /**
     * Send a STOMP message.
     *
     * @param destination e.g. "/app/whiteboard.op"
     * @param body        JSON string payload
     */
    public void send(String destination, String body) {
        send(destination, Map.of("content-type", "application/json"), body);
    }

    public void send(String destination, Map<String, String> extraHeaders, String body) {
        if (!connected) {
            log.warn("Not connected; dropping message to {}", destination);
            return;
        }
        sendRawFrame(buildSendFrame(destination, extraHeaders, body));
    }

    public void unsubscribe(String subId) {
        subscriptions.remove(subId);
        sendRawFrame("UNSUBSCRIBE\nid:" + subId + "\n\n" + NULL_BYTE);
    }

    public void disconnect() {
        if (connected) {
            sendRawFrame("DISCONNECT\n\n" + NULL_BYTE);
            connected = false;
        }
        if (ws != null) {
            ws.close();
        }
    }

    public boolean isConnected() { return connected; }

    public void setOnError(Consumer<String> cb)      { this.onError = cb; }
    public void setOnDisconnect(Consumer<String> cb) { this.onDisconnect = cb; }

    // ── WebSocket setup ───────────────────────────────────────────────────────

    private void buildWebSocketClient() {
        ws = new WebSocketClient(serverUri) {
            @Override public void onOpen(ServerHandshake hs) {
                log.debug("WebSocket opened to {}", serverUri);
            }
            @Override public void onMessage(String text) {
                handleIncoming(text);
            }
            @Override public void onMessage(ByteBuffer bytes) {
                // Binary frames are handled by MediaClient, not here
            }
            @Override public void onClose(int code, String reason, boolean remote) {
                connected = false;
                log.info("WebSocket closed: {} / {}", code, reason);
                if (onDisconnect != null) onDisconnect.accept(reason);
            }
            @Override public void onError(Exception ex) {
                log.error("WebSocket error: {}", ex.getMessage(), ex);
                if (onError != null) onError.accept(ex.getMessage());
            }
        };
    }

    // ── STOMP frame parsing ───────────────────────────────────────────────────

    private void handleIncoming(String raw) {
        if (raw.isBlank() || raw.equals("\n") || raw.equals("\r\n")) return; // heartbeat

        StompFrame frame = StompFrame.parse(raw);
        log.debug("← STOMP cmd={} dest={} sub={}", frame.command(), frame.destination(), frame.header("subscription"));

        switch (frame.command()) {
            case "CONNECTED" -> {
                connected = true;
                connectLatch.countDown();
                log.info("STOMP connected (session={})", frame.header("session"));
                if (onConnected != null) onConnected.run();
            }
            case "MESSAGE" -> {
                String subId = frame.header("subscription");
                Consumer<StompFrame> cb = subscriptions.get(subId);
                if (cb != null) {
                    cb.accept(frame);
                } else {
                    log.warn("No subscriber for subscription {}", subId);
                }
            }
            case "ERROR" -> {
                log.error("STOMP ERROR: {}", frame.body());
                if (onError != null) onError.accept(frame.body());
            }
            case "RECEIPT" -> log.debug("STOMP RECEIPT: {}", frame.header("receipt-id"));
            default -> log.debug("Unhandled STOMP command: {}", frame.command());
        }
    }

    // ── Frame builders ────────────────────────────────────────────────────────

    private String buildConnectFrame(String login, String passcode) {
        return "CONNECT\n" +
               "accept-version:1.2\n" +
               "heart-beat:10000,10000\n" +
               (login    != null ? "login:"    + login    + "\n" : "") +
               (passcode != null ? "passcode:" + passcode + "\n" : "") +
               "\n" + NULL_BYTE;
    }

    private String buildSubscribeFrame(String destination, String subId) {
        return "SUBSCRIBE\n" +
               "id:" + subId + "\n" +
               "destination:" + destination + "\n" +
               "ack:auto\n" +
               "\n" + NULL_BYTE;
    }

    private String buildSendFrame(String destination, Map<String, String> extra, String body) {
        StringBuilder sb = new StringBuilder("SEND\n");
        sb.append("destination:").append(destination).append("\n");
        sb.append("content-length:").append(body.getBytes().length).append("\n");
        extra.forEach((k, v) -> sb.append(k).append(":").append(v).append("\n"));
        sb.append("\n").append(body).append(NULL_BYTE);
        return sb.toString();
    }

    private void sendRawFrame(String frame) {
        if (ws != null && ws.isOpen()) {
            ws.send(frame);
        }
    }
}
