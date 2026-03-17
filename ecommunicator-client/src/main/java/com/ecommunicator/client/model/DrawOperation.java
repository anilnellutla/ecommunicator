package com.ecommunicator.client.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A single whiteboard draw operation as received from the server.
 * Stored per page for undo/redo and late-joiner replay.
 */
public record DrawOperation(
    String opId,
    String opType,
    String pageId,
    String senderId,
    JsonNode data       // Raw JsonNode; interpreted by the appropriate renderer
) {}
