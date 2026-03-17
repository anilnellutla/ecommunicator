package com.ecommunicator.client.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Parsed STOMP frame (server → client direction).
 */
public record StompFrame(String command, Map<String, String> headers, String body) {

    /** Parse a raw STOMP frame string. Handles both LF and CRLF line endings. */
    public static StompFrame parse(String raw) {
        // Strip trailing null byte(s)
        String text = raw;
        while (text.endsWith("\u0000")) text = text.substring(0, text.length() - 1);

        // Normalize CRLF → LF so all downstream logic only deals with \n
        text = text.replace("\r\n", "\n").replace("\r", "\n");
        text = text.trim();

        int headerEnd = text.indexOf("\n\n");
        if (headerEnd == -1) {
            // No body — single-line frame (e.g. heartbeat)
            String cmd = text.split("\n")[0].trim();
            return new StompFrame(cmd, Map.of(), "");
        }

        String headerPart = text.substring(0, headerEnd);
        String body       = text.substring(headerEnd + 2);

        String[] lines   = headerPart.split("\n");
        String command   = lines[0].trim();
        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            int colon = lines[i].indexOf(':');
            if (colon > 0) {
                headers.put(lines[i].substring(0, colon).trim(),
                            lines[i].substring(colon + 1).trim());
            }
        }
        return new StompFrame(command, headers, body);
    }

    public String header(String name) {
        return headers.getOrDefault(name, null);
    }

    public String destination() {
        return header("destination");
    }
}
