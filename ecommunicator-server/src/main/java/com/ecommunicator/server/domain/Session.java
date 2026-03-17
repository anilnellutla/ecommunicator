package com.ecommunicator.server.domain;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents an active collaboration session.
 * Thread-safe: designed for concurrent access from multiple STOMP message handlers.
 */
public class Session {

    private final String id;
    private volatile String title;
    private final String hostId;
    private final String passwordHash;   // null = open session
    private final Instant createdAt;

    /** All current participants, keyed by participantId */
    private final Map<String, Participant> participants = new ConcurrentHashMap<>();

    /** Chat history (bounded to last 500 messages) */
    private final List<ChatMessage> chatHistory = new CopyOnWriteArrayList<>();
    private static final int MAX_CHAT_HISTORY = 500;

    /** Whiteboard state (pages + operations) */
    private final WhiteboardState whiteboard = new WhiteboardState();

    /** Active polls */
    private final Map<String, Poll> polls = new ConcurrentHashMap<>();

    private final AtomicBoolean recording = new AtomicBoolean(false);
    private volatile String recordingId;
    private volatile boolean ended = false;

    public Session(String id, String title, String hostId, String passwordHash) {
        this.id = id;
        this.title = title;
        this.hostId = hostId;
        this.passwordHash = passwordHash;
        this.createdAt = Instant.now();
    }

    // ── Participant management ────────────────────────────────────────────────

    public void addParticipant(Participant p) {
        participants.put(p.getId(), p);
    }

    public Optional<Participant> removeParticipant(String participantId) {
        return Optional.ofNullable(participants.remove(participantId));
    }

    public Optional<Participant> findParticipant(String participantId) {
        return Optional.ofNullable(participants.get(participantId));
    }

    public Optional<Participant> findParticipantByStompSession(String stompSessionId) {
        return participants.values().stream()
                .filter(p -> stompSessionId.equals(p.getStompSessionId()))
                .findFirst();
    }

    public Collection<Participant> getParticipants() {
        return Collections.unmodifiableCollection(participants.values());
    }

    public boolean isEmpty() { return participants.isEmpty(); }

    // ── Chat ─────────────────────────────────────────────────────────────────

    public void addChatMessage(ChatMessage msg) {
        chatHistory.add(msg);
        if (chatHistory.size() > MAX_CHAT_HISTORY) {
            chatHistory.remove(0);
        }
    }

    public List<ChatMessage> getChatHistory() {
        return Collections.unmodifiableList(chatHistory);
    }

    // ── Whiteboard ───────────────────────────────────────────────────────────

    public WhiteboardState getWhiteboard() { return whiteboard; }

    // ── Polls ────────────────────────────────────────────────────────────────

    public void addPoll(Poll poll) { polls.put(poll.getPollId(), poll); }

    public Optional<Poll> findPoll(String pollId) {
        return Optional.ofNullable(polls.get(pollId));
    }

    public Collection<Poll> getActivePolls() {
        return polls.values().stream().filter(p -> !p.isClosed()).toList();
    }

    // ── Recording ────────────────────────────────────────────────────────────

    public boolean startRecording(String recId) {
        if (recording.compareAndSet(false, true)) {
            this.recordingId = recId;
            return true;
        }
        return false;
    }

    public boolean stopRecording() {
        if (recording.compareAndSet(true, false)) {
            this.recordingId = null;
            return true;
        }
        return false;
    }

    public boolean isRecording() { return recording.get(); }

    // ── Lifecycle ────────────────────────────────────────────────────────────

    public void end() { this.ended = true; }
    public boolean isEnded() { return ended; }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getId()           { return id; }
    public String getTitle()        { return title; }
    public void   setTitle(String t){ this.title = t; }
    public String getHostId()       { return hostId; }
    public String getPasswordHash() { return passwordHash; }
    public Instant getCreatedAt()   { return createdAt; }
    public String getRecordingId()  { return recordingId; }

    // ── Chat message record ───────────────────────────────────────────────────

    public record ChatMessage(
        String messageId,
        String senderId,
        String senderName,
        String text,
        String toId,        // null = broadcast
        String attachment,  // null or base64 data URL
        Instant timestamp
    ) {}
}
