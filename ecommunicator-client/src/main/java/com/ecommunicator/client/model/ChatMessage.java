package com.ecommunicator.client.model;

import java.time.Instant;

/**
 * Client-side chat message model.
 */
public record ChatMessage(
    String messageId,
    String senderId,
    String senderName,
    String text,
    String toId,        // null = broadcast; non-null = private
    String attachment,  // null or base64 data URL
    Instant timestamp
) {
    public boolean isPrivate() { return toId != null; }
}
