package com.ecommunicator.client.ui;

import com.ecommunicator.client.net.EcpClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML controller for login.fxml.
 * Handles both "Create Session" and "Join Session" flows.
 */
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField     serverUrlField;
    @FXML private TextField     displayNameField;
    @FXML private TextField     sessionIdField;
    @FXML private PasswordField sessionPasswordField;
    @FXML private Button        createSessionBtn;
    @FXML private Button        joinSessionBtn;
    @FXML private Label         statusLabel;
    @FXML private ProgressIndicator progressIndicator;

    private Stage primaryStage;
    private EcpClient ecpClient;

    public void init(Stage stage, EcpClient client) {
        this.primaryStage = stage;
        this.ecpClient    = client;
        progressIndicator.setVisible(false);
        statusLabel.setText("");
    }

    @FXML
    private void onCreateSession(ActionEvent event) {
        String sessionId = sessionIdField.getText().trim();
        if (sessionId.isBlank()) {
            sessionId = "mtg-" + java.util.UUID.randomUUID().toString().substring(0, 8);
            sessionIdField.setText(sessionId);
        }
        connect(sessionId, true);
    }

    @FXML
    private void onJoinSession(ActionEvent event) {
        String sessionId = sessionIdField.getText().trim();
        if (sessionId.isBlank()) {
            showError("Please enter a Session ID to join.");
            return;
        }
        connect(sessionId, false);
    }

    private void connect(String sessionId, boolean isCreating) {
        String serverUrl  = serverUrlField.getText().trim();
        String name       = displayNameField.getText().trim();
        String password   = sessionPasswordField.getText().isEmpty()
                            ? null : sessionPasswordField.getText();

        if (serverUrl.isBlank()) { showError("Server URL is required."); return; }
        if (name.isBlank())      { showError("Display name is required."); return; }

        setLoading(true, isCreating ? "Creating session..." : "Joining session...");

        String finalSessionId = sessionId;
        new Thread(() -> {
            try {
                // Register handler BEFORE connecting so we never miss SESSION_STATE
                ecpClient.router().on("SESSION_STATE", msg -> {
                    com.fasterxml.jackson.databind.JsonNode payload = msg.get("payload");
                    if (payload != null && payload.has("myId")) {
                        ecpClient.setMyId(payload.get("myId").asText());
                    }
                    Platform.runLater(() -> openMainWindow(finalSessionId));
                });

                ecpClient.connect(serverUrl, () -> {
                    log.info("{} session {}", isCreating ? "Creating" : "Joining", finalSessionId);
                    ecpClient.joinSession(finalSessionId, name, password);
                });
            } catch (Exception e) {
                log.error("Connection failed", e);
                Platform.runLater(() -> {
                    setLoading(false, "");
                    showError("Connection failed: " + e.getMessage());
                });
            }
        }, "connect-thread").start();
    }

    private void openMainWindow(String sessionId) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/ecommunicator/client/main.fxml"));
            Scene mainScene = new Scene(loader.load(), 1280, 800);
            mainScene.getStylesheets().add(
                    getClass().getResource("/com/ecommunicator/client/styles/dark.css").toExternalForm());

            MainController mainController = loader.getController();
            mainController.init(primaryStage, ecpClient, sessionId);

            primaryStage.setScene(mainScene);
            primaryStage.setResizable(true);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("eCommunicator — " + sessionId);
            log.info("Main window opened for session {}", sessionId);
        } catch (Exception e) {
            log.error("Failed to open main window", e);
            showError("Failed to open session window: " + e.getMessage());
        }
    }

    private void setLoading(boolean loading, String message) {
        Platform.runLater(() -> {
            progressIndicator.setVisible(loading);
            createSessionBtn.setDisable(loading);
            joinSessionBtn.setDisable(loading);
            statusLabel.setText(message);
            statusLabel.setStyle(loading ? "-fx-text-fill: #aaa;" : "");
        });
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: #ff6b6b;");
        progressIndicator.setVisible(false);
        createSessionBtn.setDisable(false);
        joinSessionBtn.setDisable(false);
    }
}
