package com.ecommunicator.server.controller;

import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.protocol.EcpMessage;
import com.ecommunicator.server.protocol.MessageType;
import com.ecommunicator.server.service.SessionService;
import com.ecommunicator.server.service.WhiteboardService;
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

/**
 * Handles WHITEBOARD_OP messages: apply to server state, then broadcast to all session participants.
 */
@Controller
public class WhiteboardController {

    private static final Logger log = LoggerFactory.getLogger(WhiteboardController.class);

    private final SessionService     sessionService;
    private final WhiteboardService  whiteboardService;
    private final SimpMessagingTemplate messaging;
    private final ObjectMapper       mapper;

    public WhiteboardController(SessionService sessionService,
                                WhiteboardService whiteboardService,
                                SimpMessagingTemplate messaging,
                                ObjectMapper mapper) {
        this.sessionService    = sessionService;
        this.whiteboardService = whiteboardService;
        this.messaging         = messaging;
        this.mapper            = mapper;
    }

    /**
     * Client sends to /app/whiteboard.op
     * Full EcpMessage envelope:
     * { header: { sessionId, senderId, role, ... }, payload: { pageId, opId, opType, data: {...} } }
     */
    @MessageMapping("/whiteboard.op")
    public void whiteboardOp(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId     = getStr(msg, "header", "sessionId");
        String senderId      = getStr(msg, "header", "senderId");
        String senderName    = getStr(msg, "header", "senderName");
        String role          = getStr(msg, "header", "role");
        JsonNode payload     = msg.get("payload");

        try {
            Session session = sessionService.requireSession(sessionId);
            Participant sender = session.findParticipant(senderId)
                    .orElseThrow(() -> new SecurityException("Participant not in session"));

            whiteboardService.applyOperation(session, sender, payload);

            // Broadcast to all participants (including sender, so they get server-confirmed op)
            EcpMessage broadcast = EcpMessage.of(
                    MessageType.WHITEBOARD_OP, sessionId,
                    senderId, senderName, role, (ObjectNode) payload);
            messaging.convertAndSend("/topic/session/" + sessionId, broadcast);

        } catch (SecurityException e) {
            log.warn("Whiteboard permission denied: {}", e.getMessage());
            sendError(headerAccessor.getSessionId(), "PERMISSION_DENIED", e.getMessage());
        } catch (Exception e) {
            log.error("Whiteboard op error", e);
            sendError(headerAccessor.getSessionId(), "INTERNAL_ERROR", e.getMessage());
        }
    }

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
