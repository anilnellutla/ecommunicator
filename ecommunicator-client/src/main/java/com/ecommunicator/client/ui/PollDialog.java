package com.ecommunicator.client.ui;

import com.ecommunicator.client.net.EcpClient;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog for creating a new poll.
 */
public class PollDialog {

    private final EcpClient ecpClient;

    public PollDialog(EcpClient ecpClient) {
        this.ecpClient = ecpClient;
    }

    public void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Create Poll");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("dialog-root");

        Label questionLabel = new Label("Question:");
        TextField questionField = new TextField();
        questionField.setPromptText("e.g. What is the most important feature?");

        Label optionsLabel = new Label("Options (one per line):");
        TextArea optionsArea = new TextArea();
        optionsArea.setPromptText("Option A\nOption B\nOption C");
        optionsArea.setPrefRowCount(4);

        CheckBox multiSelect = new CheckBox("Allow multiple selections");
        CheckBox anonymous   = new CheckBox("Anonymous voting");

        Label durationLabel = new Label("Duration (seconds, 0 = no limit):");
        TextField durationField = new TextField("60");

        Button createBtn = new Button("Create Poll");
        createBtn.getStyleClass().add("btn-primary");
        createBtn.setOnAction(e -> {
            String question = questionField.getText().trim();
            List<String> options = Arrays.stream(optionsArea.getText().split("\n"))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (question.isEmpty() || options.size() < 2) {
                new Alert(Alert.AlertType.WARNING,
                        "Please provide a question and at least 2 options.").show();
                return;
            }
            int duration = 0;
            try { duration = Integer.parseInt(durationField.getText().trim()); } catch (Exception ex) { duration = 0; }
            ecpClient.createPoll(question, options, multiSelect.isSelected(),
                                 anonymous.isSelected(), duration > 0 ? duration : null);
            dialog.close();
        });

        root.getChildren().addAll(questionLabel, questionField, optionsLabel, optionsArea,
                multiSelect, anonymous, durationLabel, durationField, createBtn);

        Scene scene = new Scene(root, 400, 380);
        dialog.setScene(scene);
        dialog.show();
    }
}
