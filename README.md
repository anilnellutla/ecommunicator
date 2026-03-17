# eCommunicator v2.0

A modern, real-time collaboration platform — whiteboard, video conferencing, chat, screen sharing, polls, and more. Think Zoom + Miro in a self-hosted Java application.

*Originally created in 2001 by Anil K Nellutla & Dhiraj Peechara. Fully rewritten in 2026 with a modern architecture, ECP v2 protocol, and Zoom/Teams-level capabilities.*

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                       eCommunicator Platform                        │
│                                                                     │
│  ┌───────────────────────────────┐  ┌──────────────────────────┐   │
│  │   ecommunicator-server        │  │  ecommunicator-client     │   │
│  │   Spring Boot 3.2 + Java 21   │  │  JavaFX 21 + Java 21     │   │
│  │                               │  │                           │   │
│  │  STOMP/WebSocket broker       │◄─┼─► StompClient             │   │
│  │  Binary WebSocket (media)     │◄─┼─► MediaClient (PCM+JPEG) │   │
│  │  SessionService               │  │  WhiteboardCanvas         │   │
│  │  WhiteboardService            │  │  AudioCapture/Playback    │   │
│  │  SignalingService (WebRTC)    │  │  ScreenCapture            │   │
│  │  PollService / ChatService    │  │  JavaFX UI (dark theme)   │   │
│  └───────────────────────────────┘  └──────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Features

| Category          | Features                                                                 |
|-------------------|--------------------------------------------------------------------------|
| **Whiteboard**    | Multi-page, pen/shape/text/eraser, undo/redo, cursor sharing, image paste|
| **Audio**         | 16kHz PCM capture/playback, server-relayed, mute/unmute                  |
| **Screen Share**  | 15fps JPEG capture, server-broadcast to all participants                  |
| **Video**         | WebRTC-compatible signaling relay; peer-to-peer video (browser-ready)    |
| **Chat**          | Broadcast + private DMs, chat history for late joiners                   |
| **Participants**  | Role system (HOST/CO_HOST/ATTENDEE/OBSERVER), hand raise, real-time list |
| **Polls**         | Create/vote/results broadcast, multi-select, anonymous, timed            |
| **Reactions**     | Emoji reactions                                                           |
| **Permissions**   | HOST can grant/revoke draw/chat/share per participant                    |
| **Sessions**      | Password-protected, full state sync for late joiners, auto-cleanup       |

---

## Protocol

See [PROTOCOL.md](PROTOCOL.md) for the full **ECP v2.0** specification.

- **Control channel**: WebSocket + STOMP, JSON envelopes
- **Media channel**: Raw binary WebSocket, 35-byte header + payload
- **Audio**: 16kHz / 16-bit PCM, 20ms frames
- **Screen share**: JPEG frames, ~15 fps

---

## Project Structure

```
ecommunicator/
├── PROTOCOL.md                   ← Full protocol specification
├── pom.xml                       ← Parent Maven multi-module POM
├── ecommunicator-server/         ← Spring Boot 3.2 server
│   └── src/main/java/com/ecommunicator/server/
│       ├── config/               ← WebSocket, Security, App config
│       ├── domain/               ← Session, Participant, Poll, WhiteboardState
│       ├── service/              ← Business logic services
│       ├── controller/           ← STOMP @MessageMapping controllers
│       ├── handler/              ← Binary media WebSocket handler
│       └── protocol/             ← EcpMessage, MessageType
└── ecommunicator-client/         ← JavaFX 21 desktop client
    └── src/main/java/com/ecommunicator/client/
        ├── net/                  ← EcpClient, StompClient, MessageRouter
        ├── whiteboard/           ← Canvas, DrawingModel, Tools
        ├── audio/                ← AudioCapture, AudioPlayback
        ├── video/                ← ScreenCapture
        └── ui/                   ← JavaFX FXML controllers + dark.css
```

---

## Building & Running

**Prerequisites:** Java 21+, Maven 3.8+

```bash
# Build everything
mvn clean install -DskipTests

# Start the server (default port 8080)
cd ecommunicator-server
mvn spring-boot:run

# Start the client
cd ecommunicator-client
mvn javafx:run
```

**Quick start:**
1. Start the server
2. Open two or more client instances
3. First client: enter Session ID (e.g. `demo-123`) → **Create Session**
4. Others: same Session ID → **Join Session**
5. Collaborate!

---

## Technology Stack

| Component       | Technology                      |
|-----------------|---------------------------------|
| Server runtime  | Spring Boot 3.2 + Java 21       |
| WebSocket       | Spring WebSocket + STOMP broker |
| JSON            | Jackson 2.16 with JSR-310       |
| Client UI       | JavaFX 21                       |
| Client WebSocket| Java-WebSocket 1.5.6            |
| Audio           | javax.sound.sampled (JDK)       |
| Screen capture  | java.awt.Robot (JDK)            |
| Build           | Maven 3.8+ (multi-module)       |
