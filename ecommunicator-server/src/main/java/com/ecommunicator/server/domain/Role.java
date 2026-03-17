package com.ecommunicator.server.domain;

/**
 * Participant roles in ascending privilege order.
 */
public enum Role {
    OBSERVER,   // View-only: can see whiteboard and chat but not interact
    ATTENDEE,   // Normal participant: draw (if allowed), chat, audio/video
    CO_HOST,    // Elevated: can mute others, manage polls, grant attendee permissions
    HOST        // Full control: can end session, assign CO_HOST, remove participants
}
