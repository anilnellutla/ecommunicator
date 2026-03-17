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

/**
 * WebRTC signaling relay: MEDIA_OFFER, MEDIA_ANSWER, ICE_CANDIDATE.
 *
 * The server acts as a signaling relay: it routes peer-to-peer signals between clients.
 * For audio/video mixing in conference mode, the MediaWebSocketHandler handles binary frames.
 */
@Controller
public class SignalingController {

    private static final Logger log = LoggerFactory.getLogger(SignalingController.class);

    private final SessionService sessionService;
    private final SimpMessagingTemplate messaging;
    private final ObjectMapper mapper;

    public SignalingController(SessionService sessionService,
                               SimpMessagingTemplate messaging,
                               ObjectMapper mapper) {
        this.sessionService = sessionService;
        this.messaging = messaging;
        this.mapper = mapper;
    }

    /** Client sends to /app/signaling.offer */
    @MessageMapping("/signaling.offer")
    public void offer(@Payload JsonNode msg) {
        relay(msg, MessageType.MEDIA_OFFER);
    }

    /** Client sends to /app/signaling.answer */
    @MessageMapping("/signaling.answer")
    public void answer(@Payload JsonNode msg) {
        relay(msg, MessageType.MEDIA_ANSWER);
    }

    /** Client sends to /app/signaling.ice */
    @MessageMapping("/signaling.ice")
    public void ice(@Payload JsonNode msg) {
        relay(msg, MessageType.ICE_CANDIDATE);
    }

    /**
     * Client sends to /app/signaling.media-state
     * Broadcasts mute/unmute and screen-share state changes.
     */
    @MessageMapping("/signaling.media-state")
    public void mediaState(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        // Update server state
        sessionService.findSession(sessionId).ifPresent(session ->
            session.findParticipant(senderId).ifPresent(p -> {
                if (payload.has("audioMuted"))   p.setAudioMuted(payload.path("audioMuted").asBoolean());
                if (payload.has("videoMuted"))   p.setVideoMuted(payload.path("videoMuted").asBoolean());
                if (payload.has("screenSharing")) p.setScreenSharing(payload.path("screenSharing").asBoolean());
            })
        );

        // Broadcast to everyone in session
        ObjectNode out = mapper.createObjectNode();
        out.put("participantId", senderId);
        if (payload.has("audioMuted"))   out.put("audioMuted",   payload.path("audioMuted").asBoolean());
        if (payload.has("videoMuted"))   out.put("videoMuted",   payload.path("videoMuted").asBoolean());
        if (payload.has("screenSharing")) out.put("screenSharing", payload.path("screenSharing").asBoolean());

        EcpMessage stateMsg = EcpMessage.of(
                MessageType.MEDIA_STATE_CHANGED, sessionId, senderId, senderName, role, out);
        messaging.convertAndSend("/topic/session/" + sessionId, stateMsg);
    }

    // ── Permission & mute controls ────────────────────────────────────────────

    /** HOST/CO_HOST sends to /app/permission.update */
    @MessageMapping("/permission.update")
    public void permissionUpdate(@Payload JsonNode msg) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        String targetId = payload.path("targetId").asText();
        sessionService.findSession(sessionId).ifPresent(session -> {
            session.findParticipant(senderId).ifPresent(actor -> {
                if (!actor.hasRole(com.ecommunicator.server.domain.Role.CO_HOST)) return;
                session.findParticipant(targetId).ifPresent(target -> {
                    if (payload.has("role")) target.setRole(
                            com.ecommunicator.server.domain.Role.valueOf(payload.path("role").asText()));
                    if (payload.has("allowDraw"))  target.setAllowDraw(payload.path("allowDraw").asBoolean());
                    if (payload.has("allowChat"))  target.setAllowChat(payload.path("allowChat").asBoolean());
                    if (payload.has("allowShare")) target.setAllowShare(payload.path("allowShare").asBoolean());
                });
            });
        });

        // Broadcast so all clients can update their UI
        EcpMessage permMsg = EcpMessage.of(
                MessageType.PERMISSION_UPDATE, sessionId, senderId, senderName, role,
                (ObjectNode) payload);
        messaging.convertAndSend("/topic/session/" + sessionId, permMsg);
    }

    /** HOST/CO_HOST sends to /app/mute.request  */
    @MessageMapping("/mute.request")
    public void muteRequest(@Payload JsonNode msg) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        // Update server state
        String targetId = payload.path("targetId").asText();
        sessionService.findSession(sessionId).ifPresent(session ->
            session.findParticipant(targetId).ifPresent(target -> {
                if (payload.has("muteAudio")) target.setAudioMuted(payload.path("muteAudio").asBoolean());
                if (payload.has("muteVideo")) target.setVideoMuted(payload.path("muteVideo").asBoolean());
            })
        );

        // Send mute request to the target (they can decline on client side)
        sessionService.findSession(sessionId).flatMap(s -> s.findParticipant(targetId)).ifPresent(target -> {
            EcpMessage muteMsg = EcpMessage.of(
                    MessageType.MUTE_REQUEST, sessionId, senderId, senderName, role,
                    (ObjectNode) payload);
            messaging.convertAndSendToUser(target.getStompSessionId(), "/queue/private", muteMsg);
        });
    }

    // ── Internal relay ────────────────────────────────────────────────────────

    private void relay(JsonNode msg, MessageType type) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");
        String targetId   = payload != null ? payload.path("targetId").asText(null) : null;

        EcpMessage relayMsg = EcpMessage.of(type, sessionId, senderId, senderName, role,
                (ObjectNode) payload);

        if (targetId != null) {
            // Point-to-point: route to specific participant
            sessionService.findSession(sessionId)
                    .flatMap(s -> s.findParticipant(targetId))
                    .ifPresent(target -> messaging.convertAndSendToUser(
                            target.getStompSessionId(), "/queue/private", relayMsg));
        } else {
            // Broadcast (rare: flood signaling)
            messaging.convertAndSend("/topic/session/" + sessionId, relayMsg);
        }
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
