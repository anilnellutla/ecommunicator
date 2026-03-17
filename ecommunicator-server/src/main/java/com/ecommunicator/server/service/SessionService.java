package com.ecommunicator.server.service;

import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Role;
import com.ecommunicator.server.domain.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Core session lifecycle management.
 * All sessions are kept in memory; persistence layer can be added later.
 */
@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    /** sessionId → Session */
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    // ── Session creation ──────────────────────────────────────────────────────

    /**
     * Create a new session. The creating user becomes HOST.
     *
     * @param sessionId     caller-supplied ID (e.g. "mtg-abc123") or null → auto-generated
     * @param title         human-readable title
     * @param hostId        participant ID of the creating user
     * @param plainPassword optional password (null = open)
     * @return created Session
     */
    public Session createSession(String sessionId, String title, String hostId, String plainPassword) {
        String id = (sessionId != null && !sessionId.isBlank())
                ? sessionId
                : "mtg-" + UUID.randomUUID().toString().substring(0, 8);

        if (sessions.containsKey(id)) {
            throw new IllegalStateException("Session '%s' already exists".formatted(id));
        }

        String pwHash = plainPassword != null ? sha256(plainPassword) : null;
        Session session = new Session(id, title, hostId, pwHash);
        sessions.put(id, session);
        log.info("Created session {} title='{}'", id, title);
        return session;
    }

    // ── Session lookup ────────────────────────────────────────────────────────

    public Optional<Session> findSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Session requireSession(String sessionId) {
        return findSession(sessionId).orElseThrow(
            () -> new NoSuchElementException("Session not found: " + sessionId));
    }

    // ── Participant join / leave ───────────────────────────────────────────────

    /**
     * Validate password, create Participant, add to session.
     *
     * @return the created Participant
     */
    public Participant joinSession(String sessionId, String displayName,
                                   String plainPassword, String stompSessionId) {
        Session session = requireSession(sessionId);

        if (session.isEnded()) {
            throw new IllegalStateException("Session has ended");
        }
        if (session.getPasswordHash() != null) {
            String given = plainPassword != null ? sha256(plainPassword) : "";
            if (!given.equals(session.getPasswordHash())) {
                throw new SecurityException("Wrong session password");
            }
        }

        // First participant to join becomes HOST
        Role role = session.getParticipants().isEmpty() ? Role.HOST : Role.ATTENDEE;

        Participant p = new Participant(displayName, role, stompSessionId);
        session.addParticipant(p);
        log.info("Participant {} ({}) joined session {}", p.getId(), displayName, sessionId);
        return p;
    }

    /**
     * Remove participant from session. If session is now empty, it can be reaped by the scheduler.
     *
     * @return the removed Participant, or empty if not found
     */
    public Optional<Participant> leaveSession(String sessionId, String participantId) {
        return findSession(sessionId).flatMap(s -> {
            Optional<Participant> p = s.removeParticipant(participantId);
            p.ifPresent(pp -> log.info("Participant {} left session {}", pp.getId(), sessionId));
            return p;
        });
    }

    /**
     * Called when a STOMP session disconnects unexpectedly.
     * Scans all sessions for participants with that stompSessionId.
     */
    public record LeaveResult(String sessionId, Participant participant) {}

    public Optional<LeaveResult> handleDisconnect(String stompSessionId) {
        for (Session session : sessions.values()) {
            Optional<Participant> found = session.findParticipantByStompSession(stompSessionId);
            if (found.isPresent()) {
                Participant p = found.get();
                session.removeParticipant(p.getId());
                log.info("Cleaned up disconnected participant {} from session {}",
                         p.getId(), session.getId());
                return Optional.of(new LeaveResult(session.getId(), p));
            }
        }
        return Optional.empty();
    }

    // ── Session end ───────────────────────────────────────────────────────────

    public void endSession(String sessionId) {
        findSession(sessionId).ifPresent(s -> {
            s.end();
            sessions.remove(sessionId);
            log.info("Session {} ended", sessionId);
        });
    }

    // ── Scheduled cleanup ─────────────────────────────────────────────────────

    /** Remove sessions that have been empty for more than 10 minutes. */
    @Scheduled(fixedDelay = 5 * 60 * 1000)  // every 5 minutes
    public void cleanupEmptySessions() {
        sessions.entrySet().removeIf(e -> {
            Session s = e.getValue();
            if (s.isEmpty() || s.isEnded()) {
                log.info("Reaping empty/ended session {}", s.getId());
                return true;
            }
            return false;
        });
    }

    // ── Utility ───────────────────────────────────────────────────────────────

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append("%02x".formatted(b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Session> allSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }
}
