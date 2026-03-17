package com.ecommunicator.server.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

/**
 * Universal ECP v2 message envelope.
 *
 * <pre>
 * {
 *   "header": { "version":2, "type":"WHITEBOARD_OP", "sessionId":"...", ... },
 *   "payload": { ... }
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcpMessage {

    private Header header;
    private JsonNode payload;   // Flexible: typed per MessageType at deserialization time

    // ── Constructors ─────────────────────────────────────────────────────────

    public EcpMessage() {}

    public EcpMessage(Header header, JsonNode payload) {
        this.header = header;
        this.payload = payload;
    }

    // ── Factory helpers ──────────────────────────────────────────────────────

    public static EcpMessage of(MessageType type, String sessionId, String senderId,
                                 String senderName, String role, JsonNode payload) {
        Header h = new Header();
        h.version = 2;
        h.type = type;
        h.messageId = UUID.randomUUID().toString();
        h.sessionId = sessionId;
        h.senderId = senderId;
        h.senderName = senderName;
        h.role = role;
        h.timestamp = Instant.now();
        return new EcpMessage(h, payload);
    }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public Header getHeader()                { return header; }
    public void setHeader(Header h)          { this.header = h; }
    public JsonNode getPayload()             { return payload; }
    public void setPayload(JsonNode p)       { this.payload = p; }

    // ── Inner header record ──────────────────────────────────────────────────

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Header {
        public int version = 2;
        public MessageType type;
        public String messageId;
        public String sessionId;
        public String senderId;
        public String senderName;
        public String role;
        public Instant timestamp;
    }
}
