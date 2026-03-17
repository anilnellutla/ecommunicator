package com.ecommunicator.server.domain;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Represents a connected participant within a session.
 */
public class Participant {

    private final String id;
    private String displayName;
    private Role role;
    private String stompSessionId;   // Spring STOMP session ID (for targeted messages)

    // Media state
    private final AtomicBoolean audioMuted   = new AtomicBoolean(true);
    private final AtomicBoolean videoMuted   = new AtomicBoolean(true);
    private final AtomicBoolean screenSharing = new AtomicBoolean(false);
    private final AtomicBoolean handRaised   = new AtomicBoolean(false);

    // Permissions (can be overridden by HOST)
    private volatile boolean allowDraw  = true;
    private volatile boolean allowChat  = true;
    private volatile boolean allowShare = true;

    private final Instant joinedAt;

    public Participant(String displayName, Role role, String stompSessionId) {
        this.id = UUID.randomUUID().toString();
        this.displayName = displayName;
        this.role = role;
        this.stompSessionId = stompSessionId;
        this.joinedAt = Instant.now();
    }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public String getId()                           { return id; }
    public String getDisplayName()                  { return displayName; }
    public void   setDisplayName(String n)          { this.displayName = n; }
    public Role   getRole()                         { return role; }
    public void   setRole(Role r)                   { this.role = r; }
    public String getStompSessionId()               { return stompSessionId; }
    public void   setStompSessionId(String s)       { this.stompSessionId = s; }

    public boolean isAudioMuted()                   { return audioMuted.get(); }
    public void    setAudioMuted(boolean m)         { audioMuted.set(m); }
    public boolean isVideoMuted()                   { return videoMuted.get(); }
    public void    setVideoMuted(boolean m)         { videoMuted.set(m); }
    public boolean isScreenSharing()                { return screenSharing.get(); }
    public void    setScreenSharing(boolean s)      { screenSharing.set(s); }
    public boolean isHandRaised()                   { return handRaised.get(); }
    public void    setHandRaised(boolean h)         { handRaised.set(h); }

    public boolean isAllowDraw()                    { return allowDraw; }
    public void    setAllowDraw(boolean a)          { this.allowDraw = a; }
    public boolean isAllowChat()                    { return allowChat; }
    public void    setAllowChat(boolean a)          { this.allowChat = a; }
    public boolean isAllowShare()                   { return allowShare; }
    public void    setAllowShare(boolean a)         { this.allowShare = a; }

    public Instant getJoinedAt()                    { return joinedAt; }

    public boolean hasRole(Role minimum) {
        return role.ordinal() >= minimum.ordinal();
    }

    @Override
    public String toString() {
        return "Participant{id='%s', name='%s', role=%s}".formatted(id, displayName, role);
    }
}
