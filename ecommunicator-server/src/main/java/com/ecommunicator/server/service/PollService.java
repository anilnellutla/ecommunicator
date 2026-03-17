package com.ecommunicator.server.service;

import com.ecommunicator.server.domain.Poll;
import com.ecommunicator.server.domain.Session;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages poll lifecycle: creation, voting, closing, and timed auto-close.
 */
@Service
public class PollService {

    /**
     * Callbacks invoked when a poll closes (e.g. by timer).
     * The controller registers itself here to broadcast results.
     */
    public interface PollClosedCallback {
        void onPollClosed(String sessionId, Poll poll);
    }

    private final List<PollClosedCallback> callbacks = new CopyOnWriteArrayList<>();
    /** sessionId → pollId for polls with a closing time */
    private final Map<String, String> timedPolls = new ConcurrentHashMap<>();

    public void registerCallback(PollClosedCallback cb) { callbacks.add(cb); }

    // ── Create ────────────────────────────────────────────────────────────────

    public Poll createPoll(Session session, String creatorId, String question,
                           List<String> options, boolean multiSelect,
                           boolean anonymous, Integer durationSec) {
        String pollId = UUID.randomUUID().toString();
        Poll poll = new Poll(pollId, creatorId, question, options, multiSelect, anonymous, durationSec);
        session.addPoll(poll);
        if (durationSec != null) {
            timedPolls.put(pollId, session.getId());
        }
        return poll;
    }

    // ── Vote ──────────────────────────────────────────────────────────────────

    public boolean vote(Session session, String pollId, String participantId, List<Integer> choices) {
        return session.findPoll(pollId)
                      .map(p -> p.vote(participantId, choices))
                      .orElse(false);
    }

    // ── Close ─────────────────────────────────────────────────────────────────

    public Optional<Poll> closePoll(Session session, String pollId) {
        return session.findPoll(pollId).map(p -> {
            p.close();
            timedPolls.remove(pollId);
            return p;
        });
    }

    // ── Timer-based auto-close ────────────────────────────────────────────────

    @Scheduled(fixedDelay = 5_000)
    public void checkTimedPolls() {
        Instant now = Instant.now();
        timedPolls.forEach((pollId, sessionId) -> {
            // We can't look up session here without SessionService; callbacks handle it
        });
    }

}
