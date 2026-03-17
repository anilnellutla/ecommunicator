package com.ecommunicator.server.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Stores all whiteboard pages and their accumulated draw operations.
 * Thread-safe: multiple threads (one per STOMP message) may call these concurrently.
 */
public class WhiteboardState {

    /** Ordered list of page IDs (determines tab order). */
    private final List<String> pageOrder = new CopyOnWriteArrayList<>();

    /** Page metadata: pageId → page name */
    private final Map<String, String> pageNames = new ConcurrentHashMap<>();

    /**
     * Accumulated draw operations per page.
     * Each entry is a raw JsonNode (the full WHITEBOARD_OP payload) so we can replay exactly.
     */
    private final Map<String, List<JsonNode>> pageOps = new ConcurrentHashMap<>();

    /** Currently active page (used as default if client does not specify). */
    private volatile String activePageId;

    public WhiteboardState() {
        // Create the first page
        String firstId = UUID.randomUUID().toString();
        addPage(firstId, "Page 1");
        activePageId = firstId;
    }

    public void addPage(String pageId, String pageName) {
        pageOrder.add(pageId);
        pageNames.put(pageId, pageName);
        pageOps.put(pageId, new CopyOnWriteArrayList<>());
    }

    public void removePage(String pageId) {
        pageOrder.remove(pageId);
        pageNames.remove(pageId);
        pageOps.remove(pageId);
        if (pageId.equals(activePageId) && !pageOrder.isEmpty()) {
            activePageId = pageOrder.get(0);
        }
    }

    public void renamePage(String pageId, String newName) {
        pageNames.put(pageId, newName);
    }

    public void switchPage(String pageId) {
        if (pageOps.containsKey(pageId)) {
            activePageId = pageId;
        }
    }

    public void addOperation(String pageId, JsonNode op) {
        List<JsonNode> ops = pageOps.get(pageId);
        if (ops != null) {
            ops.add(op);
        }
    }

    public void clearPage(String pageId) {
        List<JsonNode> ops = pageOps.get(pageId);
        if (ops != null) {
            ops.clear();
        }
    }

    // ── Snapshot for late-join sync ──────────────────────────────────────────

    public List<PageSnapshot> snapshot() {
        List<PageSnapshot> result = new ArrayList<>();
        for (String pageId : pageOrder) {
            result.add(new PageSnapshot(
                pageId,
                pageNames.getOrDefault(pageId, "Page"),
                new ArrayList<>(pageOps.getOrDefault(pageId, List.of()))
            ));
        }
        return result;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public List<String> getPageOrder()    { return Collections.unmodifiableList(pageOrder); }
    public String getActivePageId()       { return activePageId; }
    public String getPageName(String id)  { return pageNames.getOrDefault(id, "Page"); }

    public record PageSnapshot(String pageId, String pageName, List<JsonNode> operations) {}
}
