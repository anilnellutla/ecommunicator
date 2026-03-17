package com.ecommunicator.client.whiteboard.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Freehand pen tool — captures a sequence of (x,y) points.
 * Produces a DRAW_PATH operation.
 */
public class PenTool implements Tool {

    private Color  color     = Color.BLACK;
    private double lineWidth = 2.0;
    private double opacity   = 1.0;

    private final List<double[]> points = new ArrayList<>();
    private String completedOp = null;
    private String opId;

    public PenTool() {}

    public PenTool(Color color, double lineWidth) {
        this.color     = color;
        this.lineWidth = lineWidth;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext gc) {
        opId = UUID.randomUUID().toString();
        points.clear();
        completedOp = null;

        points.add(new double[]{e.getX(), e.getY()});

        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.setGlobalAlpha(opacity);
        gc.beginPath();
        gc.moveTo(e.getX(), e.getY());
    }

    @Override
    public void onMouseDragged(MouseEvent e, GraphicsContext gc) {
        double x = e.getX(), y = e.getY();
        points.add(new double[]{x, y});

        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.lineTo(x, y);
        gc.stroke();
        gc.moveTo(x, y);
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext gc) {
        gc.closePath();
        gc.setGlobalAlpha(1.0);

        if (points.size() < 2) { completedOp = null; return; }

        // Serialize to JSON
        StringBuilder sb = new StringBuilder();
        sb.append("{\"opId\":\"").append(opId).append("\"");
        sb.append(",\"opType\":\"DRAW_PATH\"");
        sb.append(",\"data\":{");
        sb.append("\"color\":\"").append(colorHex(color)).append("\"");
        sb.append(",\"lineWidth\":").append(lineWidth);
        sb.append(",\"opacity\":").append(opacity);
        sb.append(",\"points\":[");
        for (int i = 0; i < points.size(); i++) {
            sb.append("[").append(points.get(i)[0]).append(",").append(points.get(i)[1]).append("]");
            if (i < points.size() - 1) sb.append(",");
        }
        sb.append("]}}");
        completedOp = sb.toString();
    }

    @Override
    public String getCompletedOpJson() { return completedOp; }

    @Override
    public void reset() {
        points.clear();
        completedOp = null;
    }

    @Override
    public String getName() { return "Pen"; }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setColor(Color c)         { this.color = c; }
    public void setLineWidth(double w)    { this.lineWidth = w; }
    public void setOpacity(double o)      { this.opacity = o; }

    private String colorHex(Color c) {
        return "#%02X%02X%02X".formatted(
                (int)(c.getRed()   * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue()  * 255));
    }
}
