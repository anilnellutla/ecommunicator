package com.ecommunicator.client.ui;

import com.ecommunicator.client.net.EcpClient;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog shown to participants when a new poll is broadcast.
 */
public class PollResponseDialog {

    private final JsonNode poll;
    private final EcpClient ecpClient;

    public PollResponseDialog(JsonNode poll, EcpClient ecpClient) {
        this.poll = poll;
        this.ecpClient = ecpClient;
    }

    public void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Poll");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        String question    = poll.path("question").asText("Question?");
        boolean multiSelect = poll.path("multiSelect").asBoolean(false);
        String pollId      = poll.path("pollId").asText();

        Label questionLabel = new Label(question);
        questionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        root.getChildren().add(questionLabel);

        List<CheckBox> checkBoxes   = new ArrayList<>();
        List<RadioButton> radios    = new ArrayList<>();
        ToggleGroup group           = new ToggleGroup();

        for (JsonNode opt : poll.path("options")) {
            String text = opt.asText();
            if (multiSelect) {
                CheckBox cb = new CheckBox(text);
                checkBoxes.add(cb);
                root.getChildren().add(cb);
            } else {
                RadioButton rb = new RadioButton(text);
                rb.setToggleGroup(group);
                radios.add(rb);
                root.getChildren().add(rb);
            }
        }

        Button voteBtn = new Button("Submit Vote");
        voteBtn.getStyleClass().add("btn-primary");
        voteBtn.setOnAction(e -> {
            List<Integer> choices = new ArrayList<>();
            if (multiSelect) {
                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isSelected()) choices.add(i);
                }
            } else {
                for (int i = 0; i < radios.size(); i++) {
                    if (radios.get(i).isSelected()) { choices.add(i); break; }
                }
            }
            if (!choices.isEmpty()) {
                ecpClient.votePoll(pollId, choices);
                dialog.close();
            }
        });
        root.getChildren().add(voteBtn);

        dialog.setScene(new Scene(root, 350, 300));
        dialog.show();
    }
}
