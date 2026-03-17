package com.ecommunicator.server.controller;

import com.ecommunicator.server.domain.Poll;
import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.protocol.EcpMessage;
import com.ecommunicator.server.protocol.MessageType;
import com.ecommunicator.server.service.PollService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles poll creation, voting, ending, and result broadcasting.
 */
@Controller
public class PollController {

    private static final Logger log = LoggerFactory.getLogger(PollController.class);

    private final SessionService sessionService;
    private final PollService    pollService;
    private final SimpMessagingTemplate messaging;
    private final ObjectMapper   mapper;

    public PollController(SessionService sessionService,
                          PollService pollService,
                          SimpMessagingTemplate messaging,
                          ObjectMapper mapper) {
        this.sessionService = sessionService;
        this.pollService    = pollService;
        this.messaging      = messaging;
        this.mapper         = mapper;

        // Register callback so timed polls get results broadcast
        pollService.registerCallback(this::broadcastResults);
    }

    /** HOST/CO_HOST sends to /app/poll.create */
    @MessageMapping("/poll.create")
    public void createPoll(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        String senderName = getStr(msg, "header", "senderName");
        String role       = getStr(msg, "header", "role");
        JsonNode payload  = msg.get("payload");

        try {
            Session session = sessionService.requireSession(sessionId);
            session.findParticipant(senderId).ifPresent(actor -> {
                if (!actor.hasRole(com.ecommunicator.server.domain.Role.CO_HOST)) {
                    throw new SecurityException("Only HOST or CO_HOST can create polls");
                }
            });

            String question  = payload.path("question").asText();
            boolean multi    = payload.path("multiSelect").asBoolean(false);
            boolean anon     = payload.path("anonymous").asBoolean(false);
            Integer duration = payload.has("durationSec") ? payload.path("durationSec").asInt() : null;

            List<String> options = new ArrayList<>();
            payload.path("options").forEach(n -> options.add(n.asText()));

            Poll poll = pollService.createPoll(session, senderId, question, options, multi, anon, duration);

            ObjectNode out = pollJson(poll);
            EcpMessage createMsg = EcpMessage.of(
                    MessageType.POLL_CREATE, sessionId, senderId, senderName, role, out);
            messaging.convertAndSend("/topic/session/" + sessionId, createMsg);

        } catch (Exception e) {
            log.error("Poll create error", e);
            sendError(headerAccessor.getSessionId(), "INTERNAL_ERROR", e.getMessage());
        }
    }

    /** Any participant sends to /app/poll.vote */
    @MessageMapping("/poll.vote")
    public void vote(@Payload JsonNode msg, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = getStr(msg, "header", "sessionId");
        String senderId  = getStr(msg, "header", "senderId");
        JsonNode payload = msg.get("payload");

        try {
            Session session = sessionService.requireSession(sessionId);
            String pollId   = payload.path("pollId").asText();

            List<Integer> choices = new ArrayList<>();
            payload.path("choices").forEach(n -> choices.add(n.asInt()));

            boolean accepted = pollService.vote(session, pollId, senderId, choices);
            if (!accepted) {
                sendError(headerAccessor.getSessionId(), "POLL_CLOSED", "Poll is closed");
                return;
            }

            // Optionally broadcast running results (real-time)
            session.findPoll(pollId).ifPresent(poll -> {
                ObjectNode resultsPayload = buildResultsPayload(poll, false);
                EcpMessage resultsMsg = EcpMessage.of(
                        MessageType.POLL_RESULTS, sessionId, "server", "server", "SERVER", resultsPayload);
                messaging.convertAndSend("/topic/session/" + sessionId, resultsMsg);
            });

        } catch (Exception e) {
            log.error("Poll vote error", e);
        }
    }

    /** HOST/CO_HOST sends to /app/poll.end */
    @MessageMapping("/poll.end")
    public void endPoll(@Payload JsonNode msg) {
        String sessionId  = getStr(msg, "header", "sessionId");
        String senderId   = getStr(msg, "header", "senderId");
        JsonNode payload  = msg.get("payload");
        String pollId     = payload.path("pollId").asText();

        sessionService.findSession(sessionId).ifPresent(session ->
            pollService.closePoll(session, pollId).ifPresent(poll ->
                broadcastResults(sessionId, poll)));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void broadcastResults(String sessionId, Poll poll) {
        ObjectNode resultsPayload = buildResultsPayload(poll, true);
        EcpMessage resultsMsg = EcpMessage.of(
                MessageType.POLL_RESULTS, sessionId, "server", "server", "SERVER", resultsPayload);
        messaging.convertAndSend("/topic/session/" + sessionId, resultsMsg);
    }

    private ObjectNode buildResultsPayload(Poll poll, boolean closed) {
        ObjectNode out = mapper.createObjectNode();
        out.put("pollId", poll.getPollId());
        out.put("totalVotes", poll.totalVotes());
        out.put("closed", closed);
        ObjectNode results = mapper.createObjectNode();
        for (Map.Entry<Integer, Long> entry : poll.results().entrySet()) {
            results.put(entry.getKey().toString(), entry.getValue());
        }
        out.set("results", results);
        return out;
    }

    private ObjectNode pollJson(Poll poll) {
        ObjectNode node = mapper.createObjectNode();
        node.put("pollId", poll.getPollId());
        node.put("question", poll.getQuestion());
        node.put("multiSelect", poll.isMultiSelect());
        node.put("anonymous", poll.isAnonymous());
        var opts = mapper.createArrayNode();
        poll.getOptions().forEach(opts::add);
        node.set("options", opts);
        if (poll.getClosesAt() != null) node.put("closesAt", poll.getClosesAt().toString());
        return node;
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
