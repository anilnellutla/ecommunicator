package com.ecommunicator.client.whiteboard.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.UUID;

/**
 * Shape tool: draws rectangles, ellipses, lines, or arrows.
 * Produces a DRAW_SHAPE operation.
 */
public class ShapeTool implements Tool {

    public enum ShapeType { RECT, ELLIPSE, LINE, ARROW }

    private ShapeType shapeType  = ShapeType.RECT;
    private Color     stroke     = Color.BLACK;
    private Color     fill       = null;     // null = no fill
    private double    lineWidth  = 2.0;

    private double startX, startY, endX, endY;
    private String completedOp = null;
    private String opId;

    // For live preview: store last known canvas snapshot
    private javafx.scene.image.WritableImage snapshot;

    public ShapeTool() {}
    public ShapeTool(ShapeType type, Color stroke, Color fill, double lineWidth) {
        this.shapeType = type;
        this.stroke    = stroke;
        this.fill      = fill;
        this.lineWidth = lineWidth;
    }

    @Override
    public void onMousePressed(MouseEvent e, GraphicsContext gc) {
        opId = UUID.randomUUID().toString();
        completedOp = null;
        startX = e.getX();
        startY = e.getY();
        endX   = startX;
        endY   = startY;
        // Snapshot the canvas so we can restore it during drag preview
        double w = gc.getCanvas().getWidth();
        double h = gc.getCanvas().getHeight();
        snapshot = gc.getCanvas().snapshot(null, null);
    }

    @Override
    public void onMouseDragged(MouseEvent e, GraphicsContext gc) {
        endX = e.getX();
        endY = e.getY();
        // Restore snapshot and draw preview
        if (snapshot != null) {
            gc.drawImage(snapshot, 0, 0);
        }
        drawShape(gc, startX, startY, endX, endY, false);
    }

    @Override
    public void onMouseReleased(MouseEvent e, GraphicsContext gc) {
        endX = e.getX();
        endY = e.getY();
        if (snapshot != null) gc.drawImage(snapshot, 0, 0);
        drawShape(gc, startX, startY, endX, endY, false);

        // Normalize coordinates
        double x = Math.min(startX, endX);
        double y = Math.min(startY, endY);
        double w = Math.abs(endX - startX);
        double h = Math.abs(endY - startY);
        if (w < 2 && h < 2) { completedOp = null; return; }

        completedOp = """
            {"opId":"%s","opType":"DRAW_SHAPE","data":{"shape":"%s",\
            "x":%f,"y":%f,"w":%f,"h":%f,\
            "color":"%s","fillColor":"%s","lineWidth":%f,\
            "startX":%f,"startY":%f,"endX":%f,"endY":%f}}""".formatted(
                opId, shapeType.name(), x, y, w, h,
                colorHex(stroke), fill != null ? colorHex(fill) : "none",
                lineWidth, startX, startY, endX, endY);
    }

    private void drawShape(GraphicsContext gc, double x1, double y1, double x2, double y2, boolean fill) {
        double x = Math.min(x1, x2);
        double y = Math.min(y1, y2);
        double w = Math.abs(x2 - x1);
        double h = Math.abs(y2 - y1);

        gc.setStroke(stroke);
        gc.setLineWidth(lineWidth);

        if (this.fill != null) {
            gc.setFill(this.fill);
        }

        switch (shapeType) {
            case RECT    -> {
                if (this.fill != null) gc.fillRect(x, y, w, h);
                gc.strokeRect(x, y, w, h);
            }
            case ELLIPSE -> {
                if (this.fill != null) gc.fillOval(x, y, w, h);
                gc.strokeOval(x, y, w, h);
            }
            case LINE    -> gc.strokeLine(x1, y1, x2, y2);
            case ARROW   -> drawArrow(gc, x1, y1, x2, y2);
        }
    }

    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        gc.strokeLine(x1, y1, x2, y2);
        // Arrowhead
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLen = 15.0;
        double arrowAngle = Math.toRadians(25);
        gc.strokeLine(x2, y2,
            x2 - arrowLen * Math.cos(angle - arrowAngle),
            y2 - arrowLen * Math.sin(angle - arrowAngle));
        gc.strokeLine(x2, y2,
            x2 - arrowLen * Math.cos(angle + arrowAngle),
            y2 - arrowLen * Math.sin(angle + arrowAngle));
    }

    @Override public String getCompletedOpJson() { return completedOp; }
    @Override public void reset() { completedOp = null; snapshot = null; }
    @Override public String getName() { return "Shape (" + shapeType + ")"; }

    public void setShapeType(ShapeType t) { this.shapeType = t; }
    public void setStroke(Color c)        { this.stroke = c; }
    public void setFill(Color c)          { this.fill = c; }
    public void setLineWidth(double w)    { this.lineWidth = w; }

    private String colorHex(Color c) {
        return "#%02X%02X%02X".formatted(
                (int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255));
    }
}
