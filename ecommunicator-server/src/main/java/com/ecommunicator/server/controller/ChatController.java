package com.ecommunicator.server.controller;

import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.protocol.EcpMessage;
import com.ecommunicator.server.protocol.MessageType;
import com.ecommunicator.server.service.SessionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.UUID;

/**
 * Handles CHAT_MESSAGE and CHAT_ATTACHMENT.
 * Supports both broadcast and private (direct) messages.
 */
@Controller
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final SessionService sessionService;
    private final SimpMessagingTemplate messaging;
    private final ObjectMapper mapper;

    public ChatController(SessionService sessionService,
                          SimpMessagingTemplate messaging,
                          ObjectMapper mapper) {
        this.sessionService = sessionService;
        this.messaging = messaging;
        this.mapper = mapper;
    }

    /** Client sends to /app/chat.send */
    @MessageMapping("/chat.send")
    public void chatSend(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        try {
            Session session = sessionService.requireSession(sessionId);
            Participant sender = session.findParticipant(senderId)
                    .orElseThrow(() -> new SecurityException("Participant not in session"));

            if (!sender.isAllowChat()) {
                throw new SecurityException("Chat is disabled for this participant");
            }

            String messageId  = UUID.randomUUID().toString();
            String text       = payload != null ? payload.path("text").asText("") : "";
            String toId       = payload != null && payload.has("toId") && !payload.get("toId").isNull()
                                ? payload.path("toId").asText(null) : null;
            String attachment = payload != null && payload.has("attachment")
                                ? payload.path("attachment").asText(null) : null;
            Instant timestamp = Instant.now();

            // Persist to session history
            session.addChatMessage(new Session.ChatMessage(
                    messageId, senderId, senderName, text, toId, attachment, timestamp));

            // Build outgoing payload
            ObjectNode out = mapper.createObjectNode();
            out.put("messageId",  messageId);
            out.put("senderId",   senderId);
            out.put("senderName", senderName);
            out.put("text",       text);
            out.put("timestamp",  timestamp.toString());
            if (toId != null) out.put("toId", toId);
            if (attachment != null) out.put("attachment", attachment);

            EcpMessage chatMsg = EcpMessage.of(
                    MessageType.CHAT_MESSAGE, sessionId, senderId, senderName, role, out);

            if (toId != null) {
                // Private message: deliver to target and sender only
                session.findParticipant(toId).ifPresent(target -> {
                    messaging.convertAndSendToUser(target.getStompSessionId(), "/queue/private", chatMsg);
                });
                messaging.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/private", chatMsg);
            } else {
                // Broadcast to all
                messaging.convertAndSend("/topic/session/" + sessionId, chatMsg);
            }

        } catch (SecurityException e) {
            log.warn("Chat denied: {}", e.getMessage());
            sendError(headerAccessor.getSessionId(), "PERMISSION_DENIED", e.getMessage());
        } catch (Exception e) {
            log.error("Chat error", e);
            sendError(headerAccessor.getSessionId(), "INTERNAL_ERROR", e.getMessage());
        }
    }

    /** Client sends to /app/reaction */
    @MessageMapping("/reaction")
    public void reaction(@Payload JsonNode msg) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        ObjectNode out = mapper.createObjectNode();
        out.put("emoji", payload != null ? payload.path("emoji").asText("👍") : "👍");
        EcpMessage reactionMsg = EcpMessage.of(
                MessageType.REACTION, sessionId, senderId, senderName, role, out);
        messaging.convertAndSend("/topic/session/" + sessionId, reactionMsg);
    }

    /** Client sends to /app/hand.raise */
    @MessageMapping("/hand.raise")
    public void handRaise(@Payload JsonNode msg) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        boolean raised = payload != null && payload.path("raised").asBoolean(true);

        sessionService.findSession(sessionId).ifPresent(session ->
            session.findParticipant(senderId).ifPresent(p -> p.setHandRaised(raised)));

        ObjectNode out = mapper.createObjectNode();
        out.put("participantId", senderId);
        out.put("raised", raised);
        EcpMessage handMsg = EcpMessage.of(
                MessageType.HAND_RAISE, sessionId, senderId, senderName, role, out);
        messaging.convertAndSend("/topic/session/" + sessionId, handMsg);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void sendError(String stompSessionId, String code, String message) {
        ObjectNode err = mapper.createObjectNode();
        err.put("code", code);
        err.put("message", message);
        EcpMessage errMsg = EcpMessage.of(MessageType.ERROR, null, "server", "server", "SERVER", err);
        messaging.convertAndSendToUser(stompSessionId, "/queue/errors", errMsg);
    }

    private String getStr(JsonNode root, String... path) {
        JsonNode cur = root;
        for (String k : path) {
            if (cur == null || !cur.isObject()) return null;
            cur = cur.get(k);
        }
        return cur != null && cur.isTextual() ? cur.asText() : null;
    }
}
