package com.ecommunicator.server.controller;

import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.domain.WhiteboardState;
import com.ecommunicator.server.protocol.EcpMessage;
import com.ecommunicator.server.protocol.MessageType;
import com.ecommunicator.server.service.SessionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.UUID;

/**
 * Handles SESSION_JOIN, SESSION_LEAVE, and STOMP disconnect events.
 */
@Controller
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);

    private final SessionService sessionService;
    private final SimpMessagingTemplate messaging;
    private final ObjectMapper mapper;

    public SessionController(SessionService sessionService,
                             SimpMessagingTemplate messaging,
                             ObjectMapper mapper) {
        this.sessionService = sessionService;
        this.messaging = messaging;
        this.mapper = mapper;
    }

    // ── Join ──────────────────────────────────────────────────────────────────

    /**
     * Client sends to /app/session.join
     * Payload: { sessionId, displayName, password?, title? }
     */
    @MessageMapping("/session.join")
    public void join(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String stompSessionId = headerAccessor.getSessionId();
        String sessionId      = getString(msg, "header", "sessionId");
        JsonNode payload      = msg.get("payload");

        String displayName = payload != null ? payload.path("displayName").asText("Anonymous") : "Anonymous";
        String password    = payload != null ? payload.path("password").asText(null) : null;
        String title       = payload != null ? payload.path("title").asText("New Session") : "New Session";
        String initToken   = payload != null ? payload.path("initToken").asText(null) : null;

        try {
            // Create session if it doesn't exist
            if (sessionService.findSession(sessionId).isEmpty()) {
                sessionService.createSession(sessionId, title, null, password);
            }

            Participant participant = sessionService.joinSession(
                    sessionId, displayName, password, stompSessionId);

            // Store participant ID in WebSocket session attributes for disconnect handling
            java.util.Map<String, Object> attrs = headerAccessor.getSessionAttributes();
            if (attrs != null) {
                attrs.put("participantId", participant.getId());
                attrs.put("sessionId", sessionId);
            }

            // Send full state to the joining participant via their private init topic
            sendStateToParticipant(sessionId, initToken, participant);

            // Broadcast SESSION_JOINED to everyone in the session
            ObjectNode joinedPayload = mapper.createObjectNode();
            joinedPayload.set("participant", participantJson(participant));
            broadcast(sessionId, MessageType.SESSION_JOINED,
                      participant.getId(), participant.getDisplayName(),
                      participant.getRole().name(), joinedPayload);

        } catch (SecurityException e) {
            sendError(stompSessionId, "WRONG_PASSWORD", e.getMessage());
        } catch (Exception e) {
            log.error("Join error", e);
            sendError(stompSessionId, "INTERNAL_ERROR", e.getMessage());
        }
    }

    // ── Leave ─────────────────────────────────────────────────────────────────

    /** Client sends to /app/session.leave */
    @MessageMapping("/session.leave")
    public void leave(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId      = getString(msg, "header", "sessionId");
        String participantId  = getString(msg, "header", "senderId");
        handleLeave(sessionId, participantId);
    }

    // ── Disconnect (connection dropped) ──────────────────────────────────────

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String stompSessionId = accessor.getSessionId();

        sessionService.handleDisconnect(stompSessionId).ifPresent(result -> {
            handleLeave(result.sessionId(), result.participant().getId());
        });
    }

    // ── Ping / Pong ───────────────────────────────────────────────────────────

    @MessageMapping("/ping")
    public void ping(SimpMessageHeaderAccessor headerAccessor) {
        String stompSessionId = headerAccessor.getSessionId();
        ObjectNode pong = mapper.createObjectNode();
        pong.put("serverTime", java.time.Instant.now().toString());
        EcpMessage response = EcpMessage.of(MessageType.PONG, null, "server", "server", "SERVER", pong);
        messaging.convertAndSendToUser(stompSessionId, "/queue/private", response);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void handleLeave(String sessionId, String participantId) {
        sessionService.leaveSession(sessionId, participantId).ifPresent(p -> {
            ObjectNode payload = mapper.createObjectNode();
            payload.put("participantId", p.getId());
            broadcast(sessionId, MessageType.SESSION_LEFT,
                      p.getId(), p.getDisplayName(), p.getRole().name(), payload);
        });
    }

    private void sendStateToParticipant(String sessionId, String initToken, Participant me) {
        Session session = sessionService.requireSession(sessionId);
        ObjectNode statePayload = mapper.createObjectNode();

        // Session info
        ObjectNode sessionNode = mapper.createObjectNode();
        sessionNode.put("id", session.getId());
        sessionNode.put("title", session.getTitle());
        sessionNode.put("recording", session.isRecording());
        statePayload.set("session", sessionNode);

        // My own participant ID
        statePayload.put("myId", me.getId());

        // All participants
        var partArray = mapper.createArrayNode();
        session.getParticipants().forEach(p -> partArray.add(participantJson(p)));
        statePayload.set("participants", partArray);

        // Whiteboard pages
        var pagesArray = mapper.createArrayNode();
        for (WhiteboardState.PageSnapshot snap : session.getWhiteboard().snapshot()) {
            ObjectNode pageNode = mapper.createObjectNode();
            pageNode.put("pageId", snap.pageId());
            pageNode.put("pageName", snap.pageName());
            var opsArray = mapper.createArrayNode();
            snap.operations().forEach(opsArray::add);
            pageNode.set("operations", opsArray);
            pagesArray.add(pageNode);
        }
        statePayload.set("whiteboardPages", pagesArray);

        // Chat history
        var chatArray = mapper.createArrayNode();
        session.getChatHistory().forEach(cm -> {
            ObjectNode chatNode = mapper.createObjectNode();
            chatNode.put("messageId", cm.messageId());
            chatNode.put("senderId", cm.senderId());
            chatNode.put("senderName", cm.senderName());
            chatNode.put("text", cm.text());
            chatNode.put("timestamp", cm.timestamp().toString());
            chatArray.add(chatNode);
        });
        statePayload.set("chatHistory", chatArray);

        EcpMessage stateMsg = EcpMessage.of(MessageType.SESSION_STATE,
                sessionId, "server", "server", "SERVER", statePayload);
        // Send on the client's private init topic (reliable without Spring Security principal)
        String destination = (initToken != null)
                ? "/topic/session/" + sessionId + "/init/" + initToken
                : "/topic/session/" + sessionId;
        log.info("Sending SESSION_STATE for {} to {}", me.getId(), destination);
        messaging.convertAndSend(destination, stateMsg);
    }

    void broadcast(String sessionId, MessageType type,
                   String senderId, String senderName, String role, ObjectNode payload) {
        EcpMessage msg = EcpMessage.of(type, sessionId, senderId, senderName, role, payload);
        messaging.convertAndSend("/topic/session/" + sessionId, msg);
    }

    private void sendError(String stompSessionId, String code, String message) {
        ObjectNode err = mapper.createObjectNode();
        err.put("code", code);
        err.put("message", message);
        EcpMessage errMsg = EcpMessage.of(MessageType.ERROR, null, "server", "server", "SERVER", err);
        messaging.convertAndSendToUser(stompSessionId, "/queue/errors", errMsg);
    }

    ObjectNode participantJson(Participant p) {
        ObjectNode node = mapper.createObjectNode();
        node.put("id", p.getId());
        node.put("displayName", p.getDisplayName());
        node.put("role", p.getRole().name());
        node.put("audioMuted", p.isAudioMuted());
        node.put("videoMuted", p.isVideoMuted());
        node.put("screenSharing", p.isScreenSharing());
        node.put("handRaised", p.isHandRaised());
        node.put("allowDraw", p.isAllowDraw());
        node.put("allowChat", p.isAllowChat());
        node.put("joinedAt", p.getJoinedAt().toString());
        return node;
    }

    private String getString(JsonNode root, String... path) {
        JsonNode cur = root;
        for (String key : path) {
            if (cur == null || !cur.isObject()) return null;
            cur = cur.get(key);
        }
        return cur != null && cur.isTextual() ? cur.asText() : null;
    }
}
