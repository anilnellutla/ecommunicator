package com.ecommunicator.client.net;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * Routes incoming ECP messages to registered handlers by MessageType.
 * All handler callbacks are dispatched on a single-threaded executor so
 * UI controllers can marshal to the JavaFX thread themselves.
 */
public class MessageRouter {

    private static final Logger log = LoggerFactory.getLogger(MessageRouter.class);

    private final ObjectMapper mapper;
    private final Map<String, List<Consumer<JsonNode>>> handlers = new ConcurrentHashMap<>();
    private final ExecutorService dispatcher = Executors.newSingleThreadExecutor(
            r -> new Thread(r, "msg-router"));

    public MessageRouter(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Register a handler for a specific message type.
     * Multiple handlers can be registered for the same type.
     *
     * @param type     e.g. "WHITEBOARD_OP", "CHAT_MESSAGE"
     * @param handler  receives the full EcpMessage as a JsonNode
     */
    public void on(String type, Consumer<JsonNode> handler) {
        handlers.computeIfAbsent(type, k -> new CopyOnWriteArrayList<>()).add(handler);
    }

    /** Remove all handlers for a given type. */
    public void off(String type) {
        handlers.remove(type);
    }

    /**
     * Dispatch a raw STOMP MESSAGE body (JSON) to registered handlers.
     * Called from the StompClient receive thread.
     */
    public void dispatch(String json) {
        dispatcher.execute(() -> {
            try {
                JsonNode msg  = mapper.readTree(json);
                JsonNode hdr  = msg.get("header");
                if (hdr == null) return;
                String type   = hdr.path("type").asText();
                if (type.isBlank()) return;

                List<Consumer<JsonNode>> list = handlers.get(type);
                if (list != null) {
                    for (Consumer<JsonNode> h : list) {
                        try {
                            h.accept(msg);
                        } catch (Exception e) {
                            log.error("Handler error for {}: {}", type, e.getMessage(), e);
                        }
                    }
                } else {
                    log.debug("No handler registered for message type: {}", type);
                }
            } catch (Exception e) {
                log.error("Failed to parse incoming message: {}", e.getMessage());
            }
        });
    }

    public void shutdown() {
        dispatcher.shutdownNow();
    }
}
