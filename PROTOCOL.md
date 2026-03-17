# ECP — eCommunicator Protocol v2.0

A modern, extensible protocol for real-time collaboration: whiteboard, chat, audio/video conferencing, screen sharing, polling, and session management.

---

## Transport Layer

| Channel       | Protocol             | Port (default) | Purpose                                  |
|---------------|----------------------|----------------|------------------------------------------|
| Control       | WebSocket + STOMP    | 8080           | All signaling, whiteboard, chat, polls   |
| Media Binary  | WebSocket (binary)   | 8080 (`/media`)| Audio PCM frames, screen-share JPEG      |

TLS is used in production (`wss://`, `https://`). Plain `ws://` for local development.

---

## Control Channel — STOMP over WebSocket

The server exposes a STOMP broker at `/ws`. Clients connect and subscribe to topics.

### STOMP Destinations

| Direction      | Destination pattern                      | Description                         |
|----------------|------------------------------------------|-------------------------------------|
| Client → Server | `/app/session.join`                     | Join or create a session            |
| Client → Server | `/app/session.leave`                    | Leave gracefully                    |
| Client → Server | `/app/whiteboard.op`                    | Send a whiteboard operation         |
| Client → Server | `/app/chat.send`                        | Send a chat message                 |
| Client → Server | `/app/signaling.offer`                  | WebRTC/media offer                  |
| Client → Server | `/app/signaling.answer`                 | WebRTC/media answer                 |
| Client → Server | `/app/signaling.ice`                    | ICE candidate exchange              |
| Client → Server | `/app/poll.create`                      | Create a poll                       |
| Client → Server | `/app/poll.vote`                        | Submit a poll vote                  |
| Client → Server | `/app/reaction`                         | Send an emoji reaction              |
| Client → Server | `/app/hand.raise`                       | Raise/lower hand                    |
| Client → Server | `/app/permission.update`                | Grant/revoke permission (HOST only) |
| Client → Server | `/app/mute.request`                     | Request mute/unmute of participant  |
| Server → Client | `/topic/session/{sessionId}`            | Broadcast to all in session         |
| Server → Client | `/user/queue/private`                   | Private messages to one client      |
| Server → Client | `/user/queue/state`                     | Full session state sync (on join)   |
| Server → Client | `/user/queue/errors`                    | Error messages                      |

---

## Message Envelope

Every control message is a JSON object with a `header` and a `payload`:

```json
{
  "header": {
    "version":    2,
    "type":       "WHITEBOARD_OP",
    "messageId":  "550e8400-e29b-41d4-a716-446655440000",
    "sessionId":  "mtg-abc123",
    "senderId":   "user-uuid",
    "senderName": "Alice",
    "role":       "HOST",
    "timestamp":  "2026-03-16T10:30:00.123Z"
  },
  "payload": { ... }
}
```

### Header Fields

| Field        | Type   | Description                                       |
|--------------|--------|---------------------------------------------------|
| `version`    | int    | Protocol version (2)                              |
| `type`       | string | `MessageType` enum value                          |
| `messageId`  | UUID   | Unique message identifier (for dedup/ack)         |
| `sessionId`  | string | Session identifier                                |
| `senderId`   | UUID   | Sender's user identifier                          |
| `senderName` | string | Display name                                      |
| `role`       | enum   | `HOST` \| `CO_HOST` \| `ATTENDEE` \| `OBSERVER`  |
| `timestamp`  | ISO8601| Message creation time (UTC)                       |

---

## Message Types

### Session Management

#### `SESSION_JOIN` (client → server)
```json
{
  "sessionId":   "mtg-abc123",
  "displayName": "Alice",
  "password":    "optional-password",
  "avatar":      "base64-or-url"
}
```

#### `SESSION_JOINED` (server → client broadcast)
```json
{
  "participant": {
    "id":          "user-uuid",
    "displayName": "Alice",
    "role":        "ATTENDEE",
    "audioMuted":  true,
    "videoMuted":  true,
    "handRaised":  false,
    "joinedAt":    "2026-03-16T10:30:00Z"
  }
}
```

#### `SESSION_STATE` (server → joining client only, via `/user/queue/state`)
Full state sync for late joiners — includes participant list, whiteboard pages, chat history.
```json
{
  "session": {
    "id":        "mtg-abc123",
    "title":     "Team Standup",
    "createdAt": "2026-03-16T10:00:00Z",
    "recording": false
  },
  "participants": [ { ...participant objects... } ],
  "whiteboardPages": [
    {
      "pageId":     "page-1",
      "pageName":   "Slide 1",
      "operations": [ { ...draw ops... } ]
    }
  ],
  "chatHistory": [ { ...chat messages... } ],
  "activePolls": [ { ...poll objects... } ]
}
```

#### `SESSION_LEFT` (server broadcast)
```json
{ "participantId": "user-uuid" }
```

#### `SESSION_ENDED` (server broadcast, HOST only action)
```json
{ "reason": "HOST_LEFT" }
```

---

### Whiteboard

#### `WHITEBOARD_OP` (client → server, server broadcasts)
```json
{
  "pageId":    "page-uuid",
  "opId":      "op-uuid",
  "opType":    "DRAW_PATH",
  "data": { ... }
}
```

**Operation types and their `data` shapes:**

| `opType`         | Description                         | Key `data` fields                                        |
|------------------|-------------------------------------|----------------------------------------------------------|
| `DRAW_PATH`      | Freehand stroke                     | `points: [[x,y],...]`, `color`, `lineWidth`, `opacity`  |
| `DRAW_SHAPE`     | Rectangle, ellipse, line, arrow     | `shape`, `x`, `y`, `w`, `h`, `color`, `fill`, `lineWidth`|
| `DRAW_TEXT`      | Text annotation                     | `x`, `y`, `text`, `fontSize`, `fontFamily`, `color`     |
| `DRAW_IMAGE`     | Paste an image                      | `x`, `y`, `w`, `h`, `dataUrl` (base64 JPEG/PNG)         |
| `ERASE`          | Erase operation                     | `points: [[x,y],...]`, `radius`                         |
| `MOVE`           | Move an object                      | `opId` (target), `dx`, `dy`                             |
| `RESIZE`         | Resize an object                    | `opId` (target), `x`, `y`, `w`, `h`                    |
| `DELETE`         | Delete an object                    | `opId` (target)                                         |
| `CLEAR_PAGE`     | Clear entire page                   | *(no extra fields)*                                     |
| `ADD_PAGE`       | Add a new page                      | `pageId`, `pageName`                                    |
| `REMOVE_PAGE`    | Remove a page                       | `pageId`                                                |
| `RENAME_PAGE`    | Rename a page                       | `pageId`, `pageName`                                    |
| `SWITCH_PAGE`    | Navigate to a page                  | `pageId`                                                |
| `UNDO`           | Undo last op by this sender         | *(no extra fields)*                                     |
| `REDO`           | Redo last undone op                 | *(no extra fields)*                                     |
| `POINTER_MOVE`   | Show remote cursor                  | `x`, `y`                                               |
| `LASER_POINTER`  | Temporary highlight (fades)         | `x`, `y`                                               |

---

### Chat

#### `CHAT_MESSAGE` (client → server, server broadcasts)
```json
{
  "messageId": "msg-uuid",
  "text":      "Hello everyone!",
  "toId":      null,
  "attachment": null
}
```
Set `toId` to a participant UUID for a private message.

#### `CHAT_ATTACHMENT` (client → server, server broadcasts)
```json
{
  "messageId":  "msg-uuid",
  "fileName":   "diagram.png",
  "mimeType":   "image/png",
  "sizeBytes":  102400,
  "dataUrl":    "data:image/png;base64,..."
}
```

---

### Media Signaling

ECP uses a WebRTC-compatible signaling exchange for peer-to-peer audio/video. The server acts as a signaling relay. For deployments with >2 participants, a server-side SFU/MCU is used (the server's media WebSocket endpoint handles mixing).

#### `MEDIA_OFFER`
```json
{ "targetId": "user-uuid", "sdp": "v=0\r\no=..." }
```

#### `MEDIA_ANSWER`
```json
{ "targetId": "user-uuid", "sdp": "v=0\r\no=..." }
```

#### `ICE_CANDIDATE`
```json
{
  "targetId":  "user-uuid",
  "candidate": "candidate:...",
  "sdpMid":    "audio",
  "sdpMLineIndex": 0
}
```

#### `MEDIA_STATE_CHANGED` (broadcast)
```json
{
  "participantId": "user-uuid",
  "audioMuted":    false,
  "videoMuted":    true,
  "screenSharing": false
}
```

---

### Screen Sharing

Screen share frames are sent as **binary** WebSocket messages over the `/media` endpoint.

**Binary frame format:**

```
Bytes  0-3   : Magic number  0xEC 0x02 0x05 0x00
Bytes  4     : Frame type    0x01=SCREEN_SHARE, 0x02=AUDIO, 0x03=VIDEO
Bytes  5-20  : Sender UUID   (16 bytes, big-endian UUID)
Bytes 21-24  : Session hash  (4 bytes, CRC32 of sessionId)
Bytes 25-28  : Sequence no.  (4 bytes, big-endian uint32)
Bytes 29-32  : Timestamp ms  (4 bytes, big-endian uint32, ms since session start)
Bytes 33-34  : Payload len   (2 bytes, big-endian uint16)
Bytes 35+    : Payload       (JPEG for screen/video; raw PCM s16le 16kHz mono for audio)
```

---

### Audio

Audio frames follow the same binary format with frame type `0x02`.
Encoding: **16-bit signed PCM, little-endian, 16 kHz, mono** (can be upgraded to Opus).
Frame duration: **20 ms** → 640 bytes of PCM per frame.
The server receives audio from each participant and broadcasts to all others in the session.

---

### Polls

#### `POLL_CREATE` (HOST/CO_HOST → server, broadcast)
```json
{
  "pollId":    "poll-uuid",
  "question":  "Which feature is most important?",
  "options":   ["Whiteboard", "Video", "Recording", "Breakout Rooms"],
  "multiSelect": false,
  "anonymous": false,
  "durationSec": 60
}
```

#### `POLL_VOTE` (ATTENDEE → server)
```json
{
  "pollId":  "poll-uuid",
  "choices": [2]
}
```

#### `POLL_RESULTS` (server → all, on end or real-time)
```json
{
  "pollId":  "poll-uuid",
  "results": { "0": 3, "1": 7, "2": 1, "3": 2 },
  "totalVotes": 13,
  "closed": true
}
```

---

### Reactions & Hand Raise

#### `REACTION` (broadcast)
```json
{ "emoji": "👍" }
```

#### `HAND_RAISE` (broadcast)
```json
{ "raised": true }
```

---

### Permissions

#### `PERMISSION_UPDATE` (HOST → server, broadcast)
```json
{
  "targetId":  "user-uuid",
  "role":      "CO_HOST",
  "allowDraw": true,
  "allowChat": true,
  "allowShare": false
}
```

#### `MUTE_REQUEST` (HOST/CO_HOST → server)
```json
{
  "targetId":  "user-uuid",
  "muteAudio": true,
  "muteVideo": false
}
```

---

### Recording

#### `RECORDING_START` / `RECORDING_STOP` (HOST/CO_HOST → server, broadcast)
```json
{ "recordingId": "rec-uuid" }
```

---

### Keepalive

#### `PING` (client → server every 30s)
```json
{}
```

#### `PONG` (server → client)
```json
{ "serverTime": "2026-03-16T10:30:00.000Z" }
```

---

## Error Messages (server → `/user/queue/errors`)

```json
{
  "code":    "SESSION_NOT_FOUND",
  "message": "Session mtg-abc123 does not exist or has ended.",
  "details": {}
}
```

**Error codes:**

| Code                     | Meaning                                      |
|--------------------------|----------------------------------------------|
| `AUTH_REQUIRED`          | Not authenticated                            |
| `SESSION_NOT_FOUND`      | Session doesn't exist                        |
| `SESSION_FULL`           | Participant limit reached                    |
| `WRONG_PASSWORD`         | Session password mismatch                    |
| `PERMISSION_DENIED`      | Role insufficient for this action            |
| `RATE_LIMITED`           | Too many messages; back off                  |
| `INVALID_MESSAGE`        | Malformed message payload                    |
| `INTERNAL_ERROR`         | Server-side error                            |

---

## Roles & Permissions Matrix

| Permission                    | OBSERVER | ATTENDEE | CO_HOST | HOST |
|-------------------------------|:--------:|:--------:|:-------:|:----:|
| View whiteboard               | ✓        | ✓        | ✓       | ✓    |
| Draw on whiteboard            |          | ✓*       | ✓       | ✓    |
| Clear/delete whiteboard       |          |          | ✓       | ✓    |
| Send chat                     |          | ✓        | ✓       | ✓    |
| Send audio                    |          | ✓*       | ✓       | ✓    |
| Send video/screen             |          | ✓*       | ✓       | ✓    |
| Create polls                  |          |          | ✓       | ✓    |
| Mute others                   |          |          | ✓       | ✓    |
| Change others' roles          |          |          |         | ✓    |
| End session                   |          |          |         | ✓    |
| Start recording               |          |          | ✓       | ✓    |

`*` = Can be restricted per-participant by HOST/CO_HOST via `PERMISSION_UPDATE`.

---

## Session Lifecycle

```
HOST: SESSION_JOIN  ──►  SERVER: creates session, broadcasts SESSION_JOINED
ATTENDEE: SESSION_JOIN ──► SERVER: sends SESSION_STATE (history) to new client
                           SERVER: broadcasts SESSION_JOINED to all
...collaboration...
HOST: SESSION_ENDED ──► SERVER: broadcasts SESSION_ENDED, cleans up session
ATTENDEE: SESSION_LEFT ──► SERVER: broadcasts SESSION_LEFT
```
