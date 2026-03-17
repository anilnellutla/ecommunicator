package com.ecommunicator.client.whiteboard;

import com.ecommunicator.client.model.DrawOperation;
import com.ecommunicator.client.whiteboard.tool.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.function.Consumer;

/**
 * JavaFX Canvas that handles local drawing and remote operation replay.
 *
 * - Local interactions are processed by the active {@link Tool}.
 * - Remote operations received from the server are replayed via {@link #applyRemoteOp(DrawOperation)}.
 * - When a local gesture completes, {@code onLocalOpComplete} is called with the op JSON
 *   so the controller can send it to the server.
 */
public class WhiteboardCanvas extends Canvas {

    @Override public boolean isResizable() { return true; }
    @Override public double prefWidth(double height)  { return getWidth(); }
    @Override public double prefHeight(double width)  { return getHeight(); }

    private static final Logger log = LoggerFactory.getLogger(WhiteboardCanvas.class);

    private final GraphicsContext gc;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DrawingModel model;
    private final Pane textOverlay;   // Transparent pane for TextTool input fields

    private Tool activeTool;
    private Consumer<String> onLocalOpComplete;  // Callback: serialized op JSON → EcpClient

    // Cursor-sharing: last known positions of remote participants
    // participantId → [x, y]
    private final java.util.Map<String, double[]> remoteCursors = new java.util.concurrent.ConcurrentHashMap<>();

    public WhiteboardCanvas(double width, double height, DrawingModel model, Pane textOverlay) {
        super(width, height);
        this.gc          = getGraphicsContext2D();
        this.model        = model;
        this.textOverlay  = textOverlay;

        // Default tool
        this.activeTool  = new PenTool(Color.BLACK, 2.0);

        // White background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        setupMouseHandlers();
    }

    // ── Mouse handlers ────────────────────────────────────────────────────────

    private void setupMouseHandlers() {
        setOnMousePressed(e -> {
            if (activeTool != null) activeTool.onMousePressed(e, gc);
        });
        setOnMouseDragged(e -> {
            if (activeTool != null) activeTool.onMouseDragged(e, gc);
            // Broadcast cursor position (throttle: every drag event)
            if (onLocalOpComplete != null) {
                String pointerOp = "{\"opType\":\"POINTER_MOVE\",\"data\":{\"x\":" +
                        e.getX() + ",\"y\":" + e.getY() + "}}";
                // Note: pointer ops are transient and not stored; send directly
                onLocalOpComplete.accept(pointerOp);
            }
        });
        setOnMouseReleased(e -> {
            if (activeTool != null) {
                activeTool.onMouseReleased(e, gc);
                String opJson = activeTool.getCompletedOpJson();
                if (opJson != null && onLocalOpComplete != null) {
                    onLocalOpComplete.accept(opJson);
                }
            }
        });
    }

    // ── Remote operation replay ───────────────────────────────────────────────

    /**
     * Replay a draw operation received from the server.
     * Must be called on the JavaFX Application Thread (or via Platform.runLater).
     */
    public void applyRemoteOp(DrawOperation op) {
        Platform.runLater(() -> {
            try {
                JsonNode data = op.data();
                switch (op.opType()) {
                    case "DRAW_PATH"  -> replayPath(data);
                    case "DRAW_SHAPE" -> replayShape(data);
                    case "DRAW_TEXT"  -> replayText(data);
                    case "DRAW_IMAGE" -> replayImage(data);
                    case "ERASE"      -> replayErase(data);
                    case "POINTER_MOVE" -> updateRemoteCursor(op.senderId(), data);
                    case "CLEAR_PAGE"   -> clearCanvas();
                    default -> log.debug("Unhandled op type: {}", op.opType());
                }
            } catch (Exception e) {
                log.warn("Failed to apply remote op {}: {}", op.opType(), e.getMessage());
            }
        });
    }

    // ── Replayers ─────────────────────────────────────────────────────────────

    private void replayPath(JsonNode d) {
        gc.setStroke(parseColor(d.path("color").asText("#000000")));
        gc.setLineWidth(d.path("lineWidth").asDouble(2.0));
        gc.setGlobalAlpha(d.path("opacity").asDouble(1.0));
        JsonNode pts = d.get("points");
        if (pts == null || pts.isEmpty()) return;

        gc.beginPath();
        gc.moveTo(pts.get(0).get(0).asDouble(), pts.get(0).get(1).asDouble());
        for (int i = 1; i < pts.size(); i++) {
            gc.lineTo(pts.get(i).get(0).asDouble(), pts.get(i).get(1).asDouble());
        }
        gc.stroke();
        gc.setGlobalAlpha(1.0);
    }

    private void replayShape(JsonNode d) {
        String shape    = d.path("shape").asText("RECT");
        double x        = d.path("x").asDouble();
        double y        = d.path("y").asDouble();
        double w        = d.path("w").asDouble();
        double h        = d.path("h").asDouble();
        double lw       = d.path("lineWidth").asDouble(2.0);
        Color stroke    = parseColor(d.path("color").asText("#000000"));
        String fillStr  = d.path("fillColor").asText("none");
        Color fill      = "none".equals(fillStr) ? null : parseColor(fillStr);

        gc.setStroke(stroke);
        gc.setLineWidth(lw);

        switch (shape) {
            case "RECT" -> {
                if (fill != null) { gc.setFill(fill); gc.fillRect(x, y, w, h); }
                gc.strokeRect(x, y, w, h);
            }
            case "ELLIPSE" -> {
                if (fill != null) { gc.setFill(fill); gc.fillOval(x, y, w, h); }
                gc.strokeOval(x, y, w, h);
            }
            case "LINE" -> {
                double sx = d.path("startX").asDouble(x);
                double sy = d.path("startY").asDouble(y);
                double ex = d.path("endX").asDouble(x + w);
                double ey = d.path("endY").asDouble(y + h);
                gc.strokeLine(sx, sy, ex, ey);
            }
            case "ARROW" -> {
                double sx = d.path("startX").asDouble(x);
                double sy = d.path("startY").asDouble(y);
                double ex = d.path("endX").asDouble(x + w);
                double ey = d.path("endY").asDouble(y + h);
                gc.strokeLine(sx, sy, ex, ey);
                double angle = Math.atan2(ey - sy, ex - sx);
                double al = 15.0, aa = Math.toRadians(25);
                gc.strokeLine(ex, ey, ex - al*Math.cos(angle-aa), ey - al*Math.sin(angle-aa));
                gc.strokeLine(ex, ey, ex - al*Math.cos(angle+aa), ey - al*Math.sin(angle+aa));
            }
        }
    }

    private void replayText(JsonNode d) {
        double x     = d.path("x").asDouble();
        double y     = d.path("y").asDouble();
        String text  = d.path("text").asText();
        double size  = d.path("fontSize").asDouble(16.0);
        String font  = d.path("fontFamily").asText("Arial");
        Color color  = parseColor(d.path("color").asText("#000000"));

        gc.setFill(color);
        gc.setFont(javafx.scene.text.Font.font(font, size));
        gc.fillText(text, x, y);
    }

    private void replayImage(JsonNode d) {
        double x      = d.path("x").asDouble();
        double y      = d.path("y").asDouble();
        double w      = d.path("w").asDouble(200);
        double h      = d.path("h").asDouble(150);
        String dataUrl = d.path("dataUrl").asText();
        if (dataUrl.contains(",")) {
            String base64 = dataUrl.substring(dataUrl.indexOf(',') + 1);
            byte[] bytes  = Base64.getDecoder().decode(base64);
            Image img     = new Image(new ByteArrayInputStream(bytes));
            gc.drawImage(img, x, y, w, h);
        }
    }

    private void replayErase(JsonNode d) {
        double radius  = d.path("radius").asDouble(15.0);
        JsonNode pts   = d.get("points");
        if (pts == null) return;
        for (JsonNode pt : pts) {
            gc.clearRect(pt.get(0).asDouble() - radius, pt.get(1).asDouble() - radius,
                         radius * 2, radius * 2);
        }
    }

    private void updateRemoteCursor(String senderId, JsonNode d) {
        if (senderId == null) return;
        remoteCursors.put(senderId, new double[]{d.path("x").asDouble(), d.path("y").asDouble()});
        // Cursor overlay is rendered separately; just store position
    }

    // ── Public controls ───────────────────────────────────────────────────────

    public void clearCanvas() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    /** Replay all ops from the model's active page (e.g. after page switch or late-join sync). */
    public void replayPage(DrawingModel.Page page) {
        clearCanvas();
        for (DrawOperation op : page.operations) {
            applyRemoteOp(op);
        }
    }

    public void setActiveTool(Tool tool) {
        if (activeTool != null) activeTool.reset();
        this.activeTool = tool;
    }

    public void setOnLocalOpComplete(Consumer<String> cb) {
        this.onLocalOpComplete = cb;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Color parseColor(String hex) {
        try {
            return Color.web(hex);
        } catch (Exception e) {
            return Color.BLACK;
        }
    }
}
