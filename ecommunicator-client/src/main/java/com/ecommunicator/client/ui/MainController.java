package com.ecommunicator.client.ui;

import com.ecommunicator.client.audio.AudioCapture;
import com.ecommunicator.client.audio.AudioPlayback;
import com.ecommunicator.client.model.ChatMessage;
import com.ecommunicator.client.model.DrawOperation;
import com.ecommunicator.client.model.Participant;
import com.ecommunicator.client.net.EcpClient;
import com.ecommunicator.client.video.ScreenCapture;
import com.ecommunicator.client.whiteboard.DrawingModel;
import com.ecommunicator.client.whiteboard.WhiteboardCanvas;
import com.ecommunicator.client.whiteboard.tool.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

/**
 * Main session window controller.
 * Manages: whiteboard, participants panel, chat panel, toolbar, audio/video.
 */
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    // ── FXML injections ───────────────────────────────────────────────────────

    @FXML private BorderPane rootPane;

    // Toolbar controls
    @FXML private Button     micBtn;
    @FXML private Button     cameraBtn;
    @FXML private Button     screenShareBtn;
    @FXML private Button     recordBtn;
    @FXML private Button     handBtn;
    @FXML private Button     pollBtn;
    @FXML private Button     leaveBtn;
    @FXML private Label      sessionIdLabel;

    // Whiteboard
    @FXML private StackPane  whiteboardArea;
    @FXML private Pane       textOverlayPane;
    @FXML private HBox       toolPalette;
    @FXML private HBox       pageTabBar;
    @FXML private ColorPicker colorPicker;
    @FXML private Slider     brushSizeSlider;

    // Screen share viewer
    @FXML private ImageView  screenShareViewer;
    @FXML private StackPane  mediaArea;

    // Chat panel
    @FXML private VBox       chatPanel;
    @FXML private ListView<ChatMessage> chatListView;
    @FXML private TextField  chatInput;
    @FXML private Button     chatSendBtn;
    @FXML private ToggleButton showChatBtn;

    // Participants panel
    @FXML private VBox       participantsPanel;
    @FXML private ListView<Participant> participantsListView;
    @FXML private ToggleButton showParticipantsBtn;

    // Video grid (for conference mode)
    @FXML private GridPane   videoGrid;
    @FXML private TabPane    mainTabPane;

    // ── Non-FXML fields ───────────────────────────────────────────────────────

    private Stage primaryStage;
    private EcpClient ecpClient;
    private String sessionId;

    private DrawingModel drawingModel;
    private WhiteboardCanvas whiteboardCanvas;

    private AudioCapture audioCapture;
    private AudioPlayback audioPlayback;
    private ScreenCapture screenCapture;

    private boolean micMuted   = true;
    private boolean cameraOff  = true;
    private boolean sharingScreen = false;
    private boolean handRaised = false;
    private boolean recording  = false;

    private final ObservableList<Participant> participants = FXCollections.observableArrayList();
    private final ObservableList<ChatMessage> chatMessages = FXCollections.observableArrayList();

    // ── Init ──────────────────────────────────────────────────────────────────

    public void init(Stage stage, EcpClient client, String sessionId) {
        this.primaryStage = stage;
        this.ecpClient    = client;
        this.sessionId    = sessionId;

        sessionIdLabel.setText("Session: " + sessionId);

        initWhiteboard();
        initParticipantsPanel();
        initChatPanel();
        initAudio();
        registerMessageHandlers();

        log.info("Main controller initialized for session {}", sessionId);
    }

    // ── Whiteboard ────────────────────────────────────────────────────────────

    private void initWhiteboard() {
        drawingModel = new DrawingModel();
        double w = whiteboardArea.getPrefWidth() > 0 ? whiteboardArea.getPrefWidth() : 1024;
        double h = whiteboardArea.getPrefHeight() > 0 ? whiteboardArea.getPrefHeight() : 700;

        whiteboardCanvas = new WhiteboardCanvas(w, h, drawingModel, textOverlayPane);
        whiteboardCanvas.setOnLocalOpComplete(this::onLocalWhiteboardOp);
        whiteboardArea.getChildren().add(0, whiteboardCanvas);

        // Tool palette buttons
        setupToolPalette();

        // First page tab
        refreshPageTabs();
    }

    private void setupToolPalette() {
        // Tool buttons are defined in FXML; wire them to tool selection here
        // Color and brush size
        colorPicker.setValue(Color.BLACK);
        colorPicker.setOnAction(e -> updateToolColor(colorPicker.getValue()));

        if (brushSizeSlider != null) {
            brushSizeSlider.setMin(1);
            brushSizeSlider.setMax(30);
            brushSizeSlider.setValue(2);
            brushSizeSlider.valueProperty().addListener((obs, ov, nv) -> updateToolSize(nv.doubleValue()));
        }
    }

    @FXML private void onSelectPen()    { whiteboardCanvas.setActiveTool(new PenTool(colorPicker.getValue(), getBrushSize())); }
    @FXML private void onSelectEraser() { whiteboardCanvas.setActiveTool(new EraserTool()); }
    @FXML private void onSelectRect()   { whiteboardCanvas.setActiveTool(new ShapeTool(ShapeTool.ShapeType.RECT, colorPicker.getValue(), null, getBrushSize())); }
    @FXML private void onSelectEllipse(){ whiteboardCanvas.setActiveTool(new ShapeTool(ShapeTool.ShapeType.ELLIPSE, colorPicker.getValue(), null, getBrushSize())); }
    @FXML private void onSelectLine()   { whiteboardCanvas.setActiveTool(new ShapeTool(ShapeTool.ShapeType.LINE, colorPicker.getValue(), null, getBrushSize())); }
    @FXML private void onSelectArrow()  { whiteboardCanvas.setActiveTool(new ShapeTool(ShapeTool.ShapeType.ARROW, colorPicker.getValue(), null, getBrushSize())); }
    @FXML private void onSelectText()   { whiteboardCanvas.setActiveTool(new TextTool(textOverlayPane)); }

    @FXML private void onUndoClicked() {
        drawingModel.undoOnActivePage().ifPresent(op -> {
            whiteboardCanvas.replayPage(drawingModel.getActivePage());
            sendWhiteboardOp("{\"opType\":\"UNDO\"}", null);
        });
    }

    @FXML private void onRedoClicked() {
        drawingModel.redoOnActivePage().ifPresent(op -> {
            whiteboardCanvas.replayPage(drawingModel.getActivePage());
            sendWhiteboardOp("{\"opType\":\"REDO\"}", null);
        });
    }

    @FXML private void onClearPage() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Clear this page?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> {
            whiteboardCanvas.clearCanvas();
            drawingModel.clearPage(drawingModel.getActivePageId());
            sendWhiteboardOp("{\"opType\":\"CLEAR_PAGE\"}", null);
        });
    }

    @FXML private void onAddPage() {
        String pageId   = UUID.randomUUID().toString();
        String pageName = "Page " + (drawingModel.getPages().size() + 1);
        drawingModel.addPage(pageId, pageName);
        refreshPageTabs();
        switchToPage(pageId);
        sendWhiteboardOp("{\"opType\":\"ADD_PAGE\",\"data\":{\"pageId\":\"" + pageId +
                "\",\"pageName\":\"" + pageName + "\"}}", null);
    }

    private void switchToPage(String pageId) {
        drawingModel.setActivePage(pageId);
        drawingModel.findPage(pageId).ifPresent(p -> whiteboardCanvas.replayPage(p));
        sendWhiteboardOp("{\"opType\":\"SWITCH_PAGE\",\"data\":{\"pageId\":\"" + pageId + "\"}}", null);
    }

    private void refreshPageTabs() {
        pageTabBar.getChildren().clear();
        for (DrawingModel.Page page : drawingModel.getPages()) {
            Button tab = new Button(page.name);
            tab.getStyleClass().add("page-tab");
            if (page.pageId.equals(drawingModel.getActivePageId())) {
                tab.getStyleClass().add("page-tab-active");
            }
            tab.setOnAction(e -> switchToPage(page.pageId));
            pageTabBar.getChildren().add(tab);
        }
    }

    private void onLocalWhiteboardOp(String opJson) {
        // POINTER_MOVE is transient — send but don't persist locally
        if (opJson.contains("\"POINTER_MOVE\"")) {
            sendWhiteboardOp(opJson, null);
            return;
        }
        // Store locally and send to server
        try {
            JsonNode opNode = ecpClient.mapper().readTree(opJson);
            String opId   = opNode.path("opId").asText(UUID.randomUUID().toString());
            String opType = opNode.path("opType").asText();
            String pageId = drawingModel.getActivePageId();
            DrawOperation op = new DrawOperation(opId, opType, pageId, ecpClient.getMyId(), opNode.get("data"));
            drawingModel.addOperation(pageId, op);
        } catch (Exception e) {
            log.warn("Could not parse local op for model storage: {}", e.getMessage());
        }
        sendWhiteboardOp(opJson, drawingModel.getActivePageId());
    }

    private void sendWhiteboardOp(String dataJson, String pageId) {
        try {
            ObjectNode payload = ecpClient.mapper().createObjectNode();
            payload.put("opId",   UUID.randomUUID().toString());
            if (pageId != null) payload.put("pageId", pageId);
            // Merge the data fields from dataJson
            JsonNode data = ecpClient.mapper().readTree(dataJson);
            data.fields().forEachRemaining(entry -> payload.set(entry.getKey(), entry.getValue()));
            ecpClient.sendWhiteboardOp(payload);
        } catch (Exception e) {
            log.error("Failed to send whiteboard op", e);
        }
    }

    // ── Participants ──────────────────────────────────────────────────────────

    private void initParticipantsPanel() {
        participantsListView.setItems(participants);
        participantsListView.setCellFactory(lv -> new ParticipantCell());
    }

    // ── Chat ──────────────────────────────────────────────────────────────────

    private void initChatPanel() {
        chatListView.setItems(chatMessages);
        chatListView.setCellFactory(lv -> new ChatMessageCell(ecpClient.getMyId()));
    }

    @FXML private void onChatSend() {
        String text = chatInput.getText().trim();
        if (text.isBlank()) return;
        ecpClient.sendChat(text, null);
        chatInput.clear();
    }

    @FXML private void onChatInputKeyPressed(javafx.scene.input.KeyEvent e) {
        if (e.getCode() == javafx.scene.input.KeyCode.ENTER) onChatSend();
    }

    // ── Toolbar controls ──────────────────────────────────────────────────────

    @FXML private void onToggleMic() {
        micMuted = !micMuted;
        micBtn.setText(micMuted ? "🎙️ Unmute" : "🎙️ Mute");
        micBtn.getStyleClass().removeAll("btn-muted", "btn-active");
        micBtn.getStyleClass().add(micMuted ? "btn-muted" : "btn-active");
        if (audioCapture != null) audioCapture.setMuted(micMuted);
        ecpClient.sendMediaState(micMuted, cameraOff, sharingScreen);
    }

    @FXML private void onToggleCamera() {
        cameraOff = !cameraOff;
        cameraBtn.setText(cameraOff ? "📷 Start Video" : "📷 Stop Video");
        ecpClient.sendMediaState(micMuted, cameraOff, sharingScreen);
    }

    @FXML private void onToggleScreenShare() {
        if (!sharingScreen) {
            startScreenShare();
        } else {
            stopScreenShare();
        }
    }

    private void startScreenShare() {
        try {
            screenCapture = new ScreenCapture(15, 0.5f, null);
            screenCapture.start(jpegBytes -> {
                // Send via media WebSocket
                ecpClient.sendMediaFrame((byte)0x01, jpegBytes);
            });
            sharingScreen = true;
            screenShareBtn.setText("🖥️ Stop Share");
            ecpClient.sendMediaState(micMuted, cameraOff, true);
            log.info("Screen sharing started");
        } catch (Exception e) {
            log.error("Failed to start screen share", e);
            showAlert("Screen Share Error", e.getMessage());
        }
    }

    private void stopScreenShare() {
        if (screenCapture != null) { screenCapture.stop(); screenCapture = null; }
        sharingScreen = false;
        screenShareBtn.setText("🖥️ Share Screen");
        ecpClient.sendMediaState(micMuted, cameraOff, false);
        log.info("Screen sharing stopped");
    }

    @FXML private void onRaiseHand() {
        handRaised = !handRaised;
        handBtn.setText(handRaised ? "✋ Lower Hand" : "✋ Raise Hand");
        ecpClient.raiseHand(handRaised);
    }

    @FXML private void onCreatePoll() {
        // Show poll creation dialog
        new PollDialog(ecpClient).show();
    }

    @FXML private void onToggleRecord() {
        recording = !recording;
        recordBtn.setText(recording ? "⏹️ Stop Record" : "⏺️ Record");
        recordBtn.getStyleClass().removeAll("btn-recording", "btn-default");
        recordBtn.getStyleClass().add(recording ? "btn-recording" : "btn-default");
        // TODO: send RECORDING_START / RECORDING_STOP
    }

    @FXML private void onLeave() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Leave this session?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().filter(b -> b == ButtonType.YES).ifPresent(b -> {
            ecpClient.leaveSession();
            cleanup();
            Platform.runLater(() -> {
                primaryStage.close();
                javafx.application.Platform.exit();
            });
        });
    }

    @FXML private void onToggleChat() {
        chatPanel.setVisible(!chatPanel.isVisible());
        chatPanel.setManaged(chatPanel.isVisible());
    }

    @FXML private void onToggleParticipants() {
        participantsPanel.setVisible(!participantsPanel.isVisible());
        participantsPanel.setManaged(participantsPanel.isVisible());
    }

    // ── Message handlers ──────────────────────────────────────────────────────

    private void registerMessageHandlers() {
        ecpClient.router().on("SESSION_JOINED",     this::handleParticipantJoined);
        ecpClient.router().on("SESSION_LEFT",       this::handleParticipantLeft);
        ecpClient.router().on("PARTICIPANT_UPDATED", this::handleParticipantUpdated);
        ecpClient.router().on("WHITEBOARD_OP",      this::handleWhiteboardOp);
        ecpClient.router().on("CHAT_MESSAGE",       this::handleChatMessage);
        ecpClient.router().on("MEDIA_STATE_CHANGED", this::handleMediaState);
        ecpClient.router().on("POLL_CREATE",        this::handlePollCreate);
        ecpClient.router().on("POLL_RESULTS",       this::handlePollResults);
        ecpClient.router().on("HAND_RAISE",         this::handleHandRaise);
        ecpClient.router().on("MUTE_REQUEST",       this::handleMuteRequest);
        ecpClient.router().on("ERROR",              this::handleError);
        ecpClient.router().on("MEDIA_FRAME",        this::handleMediaFrame);
    }

    private void handleParticipantJoined(JsonNode msg) {
        JsonNode p = msg.path("payload").path("participant");
        Participant participant = parseParticipant(p);
        Platform.runLater(() -> {
            // Don't add duplicate
            participants.removeIf(ex -> ex.getId().equals(participant.getId()));
            participants.add(participant);
        });
    }

    private void handleParticipantLeft(JsonNode msg) {
        String pid = msg.path("payload").path("participantId").asText();
        Platform.runLater(() -> participants.removeIf(p -> p.getId().equals(pid)));
    }

    private void handleParticipantUpdated(JsonNode msg) {
        JsonNode payload = msg.path("payload");
        String pid = payload.path("id").asText();
        Platform.runLater(() ->
            participants.stream().filter(p -> p.getId().equals(pid)).findFirst().ifPresent(p -> {
                if (payload.has("role"))         p.setRole(payload.path("role").asText());
                if (payload.has("audioMuted"))   p.setAudioMuted(payload.path("audioMuted").asBoolean());
                if (payload.has("videoMuted"))   p.setVideoMuted(payload.path("videoMuted").asBoolean());
                if (payload.has("handRaised"))   p.setHandRaised(payload.path("handRaised").asBoolean());
                participantsListView.refresh();
            })
        );
    }

    private void handleWhiteboardOp(JsonNode msg) {
        String senderId = msg.path("header").path("senderId").asText();
        // Don't replay our own ops (already drawn locally)
        if (ecpClient.getMyId() != null && ecpClient.getMyId().equals(senderId)) return;

        JsonNode payload = msg.get("payload");
        if (payload == null) return;

        String opId   = payload.path("opId").asText(UUID.randomUUID().toString());
        String opType = payload.path("opType").asText();
        String pageId = payload.path("pageId").asText(drawingModel.getActivePageId());

        DrawOperation op = new DrawOperation(opId, opType, pageId, senderId, payload.get("data"));
        drawingModel.addOperation(pageId, op);
        whiteboardCanvas.applyRemoteOp(op);
    }

    private void handleChatMessage(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        if (payload == null) return;
        ChatMessage cm = new ChatMessage(
            payload.path("messageId").asText(),
            payload.path("senderId").asText(),
            payload.path("senderName").asText(),
            payload.path("text").asText(),
            payload.has("toId") ? payload.path("toId").asText(null) : null,
            payload.has("attachment") ? payload.path("attachment").asText(null) : null,
            Instant.now()
        );
        Platform.runLater(() -> {
            chatMessages.add(cm);
            chatListView.scrollTo(chatMessages.size() - 1);
        });
    }

    private void handleMediaState(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        if (payload == null) return;
        String pid = payload.path("participantId").asText();
        Platform.runLater(() ->
            participants.stream().filter(p -> p.getId().equals(pid)).findFirst().ifPresent(p -> {
                if (payload.has("audioMuted"))   p.setAudioMuted(payload.path("audioMuted").asBoolean());
                if (payload.has("videoMuted"))   p.setVideoMuted(payload.path("videoMuted").asBoolean());
                if (payload.has("screenSharing")) p.setScreenSharing(payload.path("screenSharing").asBoolean());
                participantsListView.refresh();
            })
        );
    }

    private void handlePollCreate(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        if (payload == null) return;
        Platform.runLater(() -> new PollResponseDialog(payload, ecpClient).show());
    }

    private void handlePollResults(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        Platform.runLater(() -> showAlert("Poll Results",
            "Poll ID: " + payload.path("pollId").asText() +
            "\nTotal votes: " + payload.path("totalVotes").asText() +
            "\nResults: " + payload.path("results").toString()));
    }

    private void handleHandRaise(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        String pid = payload.path("participantId").asText();
        boolean raised = payload.path("raised").asBoolean();
        Platform.runLater(() ->
            participants.stream().filter(p -> p.getId().equals(pid)).findFirst()
                        .ifPresent(p -> { p.setHandRaised(raised); participantsListView.refresh(); })
        );
    }

    private void handleMuteRequest(JsonNode msg) {
        JsonNode payload = msg.get("payload");
        if (payload.path("muteAudio").asBoolean(false) && !micMuted) {
            Platform.runLater(this::onToggleMic);
        }
    }

    private void handleError(JsonNode msg) {
        String code    = msg.path("payload").path("code").asText();
        String message = msg.path("payload").path("message").asText();
        log.error("Server error {}: {}", code, message);
        Platform.runLater(() -> showAlert("Error [" + code + "]", message));
    }

    private void handleMediaFrame(JsonNode msg) {
        // Binary media frames (audio PCM, screen JPEG) are decoded here
        // The actual bytes are delivered via a separate callback registered in EcpClient
        // Placeholder: screen share JPEG display handled by MediaClient
    }

    // ── Audio init ────────────────────────────────────────────────────────────

    private void initAudio() {
        try {
            audioPlayback = new AudioPlayback();
            audioPlayback.start();
            audioCapture = new AudioCapture();
            audioCapture.setMuted(true); // Start muted
            audioCapture.start(pcm -> ecpClient.sendMediaFrame((byte)0x02, pcm));
            log.info("Audio subsystem initialized");
        } catch (Exception e) {
            log.warn("Audio init failed (no microphone?): {}", e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void cleanup() {
        if (audioCapture  != null) audioCapture.stop();
        if (audioPlayback != null) audioPlayback.stop();
        if (screenCapture != null) screenCapture.stop();
    }

    private Participant parseParticipant(JsonNode p) {
        Participant pt = new Participant();
        pt.setId(p.path("id").asText());
        pt.setDisplayName(p.path("displayName").asText());
        pt.setRole(p.path("role").asText("ATTENDEE"));
        pt.setAudioMuted(p.path("audioMuted").asBoolean(true));
        pt.setVideoMuted(p.path("videoMuted").asBoolean(true));
        pt.setHandRaised(p.path("handRaised").asBoolean(false));
        return pt;
    }

    private void updateToolColor(Color c) {
        Tool t = whiteboardCanvas != null ? null : null; // Tool access not exposed; reinitialize
        // Simple approach: next tool selection picks up the new color
    }

    private void updateToolSize(double size) {
        // Same as color — picked up on next tool selection
    }

    private double getBrushSize() {
        return brushSizeSlider != null ? brushSizeSlider.getValue() : 2.0;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    // ── Inner list cell classes ───────────────────────────────────────────────

    private static class ParticipantCell extends ListCell<Participant> {
        @Override protected void updateItem(Participant p, boolean empty) {
            super.updateItem(p, empty);
            if (empty || p == null) {
                setText(null);
                setGraphic(null);
            } else {
                String indicators = "";
                if (p.isAudioMuted())   indicators += " 🔇";
                if (p.isVideoMuted())   indicators += " 📷✗";
                if (p.isHandRaised())   indicators += " ✋";
                if (p.isScreenSharing()) indicators += " 🖥️";
                setText(p.getDisplayName() + " [" + p.getRole() + "]" + indicators);
            }
        }
    }

    private static class ChatMessageCell extends ListCell<ChatMessage> {
        private final String myId;
        ChatMessageCell(String myId) { this.myId = myId; }

        @Override protected void updateItem(ChatMessage m, boolean empty) {
            super.updateItem(m, empty);
            if (empty || m == null) {
                setText(null);
                setGraphic(null);
            } else {
                boolean isMine = myId != null && myId.equals(m.senderId());
                String prefix = isMine ? "You" : m.senderName();
                String dm     = m.isPrivate() ? " (private)" : "";
                setText("[" + prefix + dm + "]: " + m.text());
                setStyle(isMine ? "-fx-text-fill: #80cbc4;" : "-fx-text-fill: #e0e0e0;");
            }
        }
    }
}
