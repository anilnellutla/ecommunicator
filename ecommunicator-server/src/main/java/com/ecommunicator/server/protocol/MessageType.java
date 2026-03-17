package com.ecommunicator.server.protocol;

/**
 * All ECP v2 message type identifiers.
 * These appear in the {@code header.type} field of every control message.
 */
public enum MessageType {

    // ── Session ─────────────────────────────────────────────────────────────
    SESSION_JOIN,
    SESSION_JOINED,
    SESSION_LEFT,
    SESSION_ENDED,
    SESSION_STATE,        // Full state sync sent to a newly joined client
    SESSION_UPDATED,      // Session metadata changed (title, recording flag, etc.)

    // ── Participant ──────────────────────────────────────────────────────────
    PARTICIPANT_UPDATED,  // Role, mute state, hand-raise changed

    // ── Whiteboard ───────────────────────────────────────────────────────────
    WHITEBOARD_OP,        // Draw, erase, page management, undo/redo

    // ── Chat ─────────────────────────────────────────────────────────────────
    CHAT_MESSAGE,
    CHAT_ATTACHMENT,

    // ── Media Signaling (WebRTC-compatible) ──────────────────────────────────
    MEDIA_OFFER,
    MEDIA_ANSWER,
    ICE_CANDIDATE,
    MEDIA_STATE_CHANGED,

    // ── Polls ────────────────────────────────────────────────────────────────
    POLL_CREATE,
    POLL_VOTE,
    POLL_END,
    POLL_RESULTS,

    // ── Reactions & Engagement ───────────────────────────────────────────────
    REACTION,
    HAND_RAISE,

    // ── Permissions ──────────────────────────────────────────────────────────
    PERMISSION_UPDATE,
    MUTE_REQUEST,

    // ── Recording ────────────────────────────────────────────────────────────
    RECORDING_START,
    RECORDING_STOP,

    // ── Keepalive ────────────────────────────────────────────────────────────
    PING,
    PONG,

    // ── Errors ───────────────────────────────────────────────────────────────
    ERROR
}
