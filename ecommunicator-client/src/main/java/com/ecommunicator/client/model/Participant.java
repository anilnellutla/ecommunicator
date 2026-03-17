package com.ecommunicator.client.model;

import javafx.beans.property.*;

/**
 * Client-side model for a session participant, backed by JavaFX properties
 * so UI controls can bind directly.
 */
public class Participant {

    private final StringProperty  id          = new SimpleStringProperty();
    private final StringProperty  displayName = new SimpleStringProperty();
    private final StringProperty  role        = new SimpleStringProperty();
    private final BooleanProperty audioMuted  = new SimpleBooleanProperty(true);
    private final BooleanProperty videoMuted  = new SimpleBooleanProperty(true);
    private final BooleanProperty screenSharing = new SimpleBooleanProperty(false);
    private final BooleanProperty handRaised  = new SimpleBooleanProperty(false);
    private final BooleanProperty allowDraw   = new SimpleBooleanProperty(true);
    private final BooleanProperty allowChat   = new SimpleBooleanProperty(true);
    private final StringProperty  joinedAt    = new SimpleStringProperty();

    public Participant() {}

    public Participant(String id, String displayName, String role) {
        this.id.set(id);
        this.displayName.set(displayName);
        this.role.set(role);
    }

    // ── Properties ────────────────────────────────────────────────────────────

    public StringProperty  idProperty()           { return id; }
    public StringProperty  displayNameProperty()  { return displayName; }
    public StringProperty  roleProperty()         { return role; }
    public BooleanProperty audioMutedProperty()   { return audioMuted; }
    public BooleanProperty videoMutedProperty()   { return videoMuted; }
    public BooleanProperty screenSharingProperty(){ return screenSharing; }
    public BooleanProperty handRaisedProperty()   { return handRaised; }
    public BooleanProperty allowDrawProperty()    { return allowDraw; }
    public BooleanProperty allowChatProperty()    { return allowChat; }
    public StringProperty  joinedAtProperty()     { return joinedAt; }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public String  getId()              { return id.get(); }
    public void    setId(String v)      { id.set(v); }
    public String  getDisplayName()     { return displayName.get(); }
    public void    setDisplayName(String v){ displayName.set(v); }
    public String  getRole()            { return role.get(); }
    public void    setRole(String v)    { role.set(v); }
    public boolean isAudioMuted()       { return audioMuted.get(); }
    public void    setAudioMuted(boolean v){ audioMuted.set(v); }
    public boolean isVideoMuted()       { return videoMuted.get(); }
    public void    setVideoMuted(boolean v){ videoMuted.set(v); }
    public boolean isScreenSharing()    { return screenSharing.get(); }
    public void    setScreenSharing(boolean v){ screenSharing.set(v); }
    public boolean isHandRaised()       { return handRaised.get(); }
    public void    setHandRaised(boolean v){ handRaised.set(v); }
    public boolean isAllowDraw()        { return allowDraw.get(); }
    public void    setAllowDraw(boolean v){ allowDraw.set(v); }
    public boolean isAllowChat()        { return allowChat.get(); }
    public void    setAllowChat(boolean v){ allowChat.set(v); }
    public String  getJoinedAt()        { return joinedAt.get(); }
    public void    setJoinedAt(String v){ joinedAt.set(v); }

    public boolean isHost()    { return "HOST".equals(role.get()); }
    public boolean isCoHost()  { return "CO_HOST".equals(role.get()); }

    @Override
    public String toString() { return displayName.get() + " [" + role.get() + "]"; }
}
