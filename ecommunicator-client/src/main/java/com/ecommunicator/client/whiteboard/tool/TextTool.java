package com.ecommunicator.client.whiteboard.tool;

import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.UUID;

/**
 * Text annotation tool.
 * On click: places a TextField overlay; on Enter/blur: commits the text.
 * Produces a DRAW_TEXT operation.
 */
public class TextTool implements Tool {

    private Color  color    = Color.BLACK;
    private double fontSize = 16.0;
    private String fontFamily = "Arial";

    private String completedOp = null;
    private Pane overlay;   // parent pane for the TextField input

    private double clickX, clickY;
    private String opId;

    public TextTool(Pane overlayPane) {
        this.overlay = overlayPane;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext gc) {
        opId = UUID.randomUUID().toString();
        completedOp = null;
        clickX = e.getX();
        clickY = e.getY();

        // Place a TextField at the click position
        TextField tf = new TextField();
        tf.setLayoutX(clickX);
        tf.setLayoutY(clickY - 18);
        tf.setStyle("-fx-background-color: transparent; -fx-border-color: #888; " +
                    "-fx-text-fill: " + colorHex(color) + "; -fx-font-size: " + (int)fontSize + "px;");
        tf.setPrefWidth(200);
        overlay.getChildren().add(tf);
        Platform.runLater(tf::requestFocus);

        tf.setOnAction(ev -> commitText(tf, gc));
        tf.focusedProperty().addListener((obs, wasF, isF) -> {
            if (!isF) commitText(tf, gc);
        });
    }

    private void commitText(TextField tf, GraphicsContext gc) {
        String text = tf.getText().trim();
        overlay.getChildren().remove(tf);
        if (text.isEmpty()) return;

        gc.setFill(color);
        gc.setFont(Font.font(fontFamily, fontSize));
        gc.fillText(text, clickX, clickY);

        completedOp = """
            {"opId":"%s","opType":"DRAW_TEXT","data":{"x":%f,"y":%f,"text":"%s",\
            "fontSize":%f,"fontFamily":"%s","color":"%s"}}""".formatted(
                opId, clickX, clickY,
                text.replace("\"", "\\\""),
                fontSize, fontFamily, colorHex(color));
    }

    @Override public void onMouseDragged(MouseEvent e, GraphicsContext gc) {}
    @Override public void onMouseReleased(MouseEvent e, GraphicsContext gc) {}
    @Override public String getCompletedOpJson() { return completedOp; }
    @Override public void reset() { completedOp = null; }
    @Override public String getName() { return "Text"; }

    public void setColor(Color c)       { this.color = c; }
    public void setFontSize(double s)   { this.fontSize = s; }
    public void setFontFamily(String f) { this.fontFamily = f; }

    private String colorHex(Color c) {
        return "#%02X%02X%02X".formatted(
                (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
    }
}
