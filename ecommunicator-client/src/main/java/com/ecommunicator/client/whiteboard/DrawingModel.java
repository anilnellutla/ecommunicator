package com.ecommunicator.client.whiteboard;

import com.ecommunicator.client.model.DrawOperation;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Client-side whiteboard model.
 * Maintains pages, operations per page, and undo/redo stacks.
 */
public class DrawingModel {

    private final List<Page> pages = new CopyOnWriteArrayList<>();
    private volatile String activePageId;

    public DrawingModel() {
        Page first = new Page(UUID.randomUUID().toString(), "Page 1");
        pages.add(first);
        activePageId = first.pageId;
    }

    // ── Page management ───────────────────────────────────────────────────────

    public Page addPage(String pageId, String pageName) {
        Page p = new Page(pageId, pageName);
        pages.add(p);
        return p;
    }

    public void removePage(String pageId) {
        pages.removeIf(p -> p.pageId.equals(pageId));
        if (pageId.equals(activePageId) && !pages.isEmpty()) {
            activePageId = pages.get(0).pageId;
        }
    }

    public void renamePage(String pageId, String newName) {
        findPage(pageId).ifPresent(p -> p.name = newName);
    }

    public void setActivePage(String pageId) {
        activePageId = pageId;
    }

    public Optional<Page> findPage(String pageId) {
        return pages.stream().filter(p -> p.pageId.equals(pageId)).findFirst();
    }

    public Page getActivePage() {
        return findPage(activePageId).orElseGet(() -> pages.isEmpty() ? null : pages.get(0));
    }

    public List<Page> getPages() { return Collections.unmodifiableList(pages); }
    public String getActivePageId() { return activePageId; }

    // ── Operations ────────────────────────────────────────────────────────────

    public void addOperation(String pageId, DrawOperation op) {
        findPage(pageId).ifPresent(p -> p.operations.add(op));
    }

    public void clearPage(String pageId) {
        findPage(pageId).ifPresent(p -> {
            p.operations.clear();
            p.undoStack.clear();
        });
    }

    // ── Undo / Redo ───────────────────────────────────────────────────────────

    public Optional<DrawOperation> undoOnActivePage() {
        Page p = getActivePage();
        if (p == null || p.operations.isEmpty()) return Optional.empty();
        DrawOperation op = p.operations.remove(p.operations.size() - 1);
        p.undoStack.add(op);
        return Optional.of(op);
    }

    public Optional<DrawOperation> redoOnActivePage() {
        Page p = getActivePage();
        if (p == null || p.undoStack.isEmpty()) return Optional.empty();
        DrawOperation op = p.undoStack.remove(p.undoStack.size() - 1);
        p.operations.add(op);
        return Optional.of(op);
    }

    // ── Inner types ───────────────────────────────────────────────────────────

    public static class Page {
        public String pageId;
        public String name;
        public final List<DrawOperation> operations = new CopyOnWriteArrayList<>();
        public final List<DrawOperation> undoStack  = new ArrayList<>();

        public Page(String pageId, String name) {
            this.pageId = pageId;
            this.name   = name;
        }
    }
}
