package com.ecommunicator.client.whiteboard.tool;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * Contract for all whiteboard drawing tools.
 */
public interface Tool {

    /** Called once when the user first presses the mouse button. */
    void onMousePressed(MouseEvent e, GraphicsContext gc);

    /** Called on every drag event while the mouse button is held. */
    void onMouseDragged(MouseEvent e, GraphicsContext gc);

    /** Called when the mouse button is released. */
    void onMouseReleased(MouseEvent e, GraphicsContext gc);

    /**
     * Returns a JSON string representing the completed draw operation payload,
     * suitable for sending as a WHITEBOARD_OP.
     * Returns null if no op was completed (e.g. cancelled gesture).
     */
    String getCompletedOpJson();

    /** Reset any in-progress state (e.g. on tool switch). */
    void reset();

    /** Human-readable tool name. */
    String getName();
}
