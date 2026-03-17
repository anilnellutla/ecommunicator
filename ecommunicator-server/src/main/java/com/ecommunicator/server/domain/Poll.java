package com.ecommunicator.server.domain;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents an active or closed poll within a session.
 */
public class Poll {

    private final String pollId;
    private final String creatorId;
    private final String question;
    private final List<String> options;
    private final boolean multiSelect;
    private final boolean anonymous;
    private final Instant createdAt;
    private final Instant closesAt;   // null = no timer

    /** participantId → chosen option indices */
    private final Map<String, List<Integer>> votes = new ConcurrentHashMap<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public Poll(String pollId, String creatorId, String question,
                List<String> options, boolean multiSelect, boolean anonymous,
                Integer durationSec) {
        this.pollId = pollId;
        this.creatorId = creatorId;
        this.question = question;
        this.options = List.copyOf(options);
        this.multiSelect = multiSelect;
        this.anonymous = anonymous;
        this.createdAt = Instant.now();
        this.closesAt = durationSec != null
                ? createdAt.plusSeconds(durationSec)
                : null;
    }

    public boolean vote(String participantId, List<Integer> choices) {
        if (closed.get()) return false;
        if (!multiSelect && choices.size() > 1) return false;
        votes.put(participantId, List.copyOf(choices));
        return true;
    }

    public void close() { closed.set(true); }
    public boolean isClosed() { return closed.get(); }

    /** Returns option index → vote count map. */
    public Map<Integer, Long> results() {
        Map<Integer, Long> counts = new LinkedHashMap<>();
        for (int i = 0; i < options.size(); i++) counts.put(i, 0L);
        for (List<Integer> v : votes.values()) {
            for (int idx : v) counts.merge(idx, 1L, Long::sum);
        }
        return counts;
    }

    public int totalVotes()    { return votes.size(); }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getPollId()       { return pollId; }
    public String getCreatorId()    { return creatorId; }
    public String getQuestion()     { return question; }
    public List<String> getOptions(){ return options; }
    public boolean isMultiSelect()  { return multiSelect; }
    public boolean isAnonymous()    { return anonymous; }
    public Instant getCreatedAt()   { return createdAt; }
    public Instant getClosesAt()    { return closesAt; }
}
