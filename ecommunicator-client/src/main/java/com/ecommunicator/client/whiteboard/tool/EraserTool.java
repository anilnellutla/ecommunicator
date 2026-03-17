package com.ecommunicator.client.whiteboard.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Eraser tool — uses destination-out compositing to erase pixels.
 * Produces an ERASE operation.
 */
public class EraserTool implements Tool {

    private double radius = 15.0;
    private final List<double[]> points = new ArrayList<>();
    private String completedOp = null;
    private String opId;

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext gc) {
        opId = UUID.randomUUID().toString();
        points.clear();
        completedOp = null;
        erase(gc, e.getX(), e.getY());
        points.add(new double[]{e.getX(), e.getY()});
    }

    @Override
    public void onMouseDragged(MouseEvent e, GraphicsContext gc) {
        erase(gc, e.getX(), e.getY());
        points.add(new double[]{e.getX(), e.getY()});
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext gc) {
        if (points.isEmpty()) { completedOp = null; return; }

        StringBuilder sb = new StringBuilder();
        sb.append("{\"opId\":\"").append(opId).append("\"");
        sb.append(",\"opType\":\"ERASE\"");
        sb.append(",\"data\":{\"radius\":").append(radius).append(",\"points\":[");
        for (int i = 0; i < points.size(); i++) {
            sb.append("[").append(points.get(i)[0]).append(",").append(points.get(i)[1]).append("]");
            if (i < points.size() - 1) sb.append(",");
        }
        sb.append("]}}");
        completedOp = sb.toString();
    }

    private void erase(GraphicsContext gc, double x, double y) {
        gc.clearRect(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override public String getCompletedOpJson() { return completedOp; }
    @Override public void reset() { points.clear(); completedOp = null; }
    @Override public String getName() { return "Eraser"; }

    public void setRadius(double r) { this.radius = r; }
}
