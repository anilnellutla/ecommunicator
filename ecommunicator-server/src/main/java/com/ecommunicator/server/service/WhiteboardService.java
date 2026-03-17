package com.ecommunicator.server.service;

import com.ecommunicator.server.domain.Session;
import com.ecommunicator.server.domain.WhiteboardState;
import com.ecommunicator.server.domain.Participant;
import com.ecommunicator.server.domain.Role;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Processes whiteboard operations and updates the server-side whiteboard state.
 * Operations are also forwarded to all participants by the controller.
 */
@Service
public class WhiteboardService {

    /**
     * Apply a whiteboard operation to the session's whiteboard state.
     *
     * @param session    the target session
     * @param sender     the participant who sent the operation
     * @param payload    the WHITEBOARD_OP payload JsonNode
     * @throws SecurityException if the sender lacks draw permission
     */
    public void applyOperation(Session session, Participant sender, JsonNode payload) {
        WhiteboardState wb = session.getWhiteboard();
        String opType = getStr(payload, "opType");

        // Permission check: HOST and CO_HOST always allowed; ATTENDEEs need allowDraw
        if (!sender.hasRole(Role.CO_HOST) && !sender.isAllowDraw()) {
            throw new SecurityException("Draw permission denied for participant " + sender.getId());
        }

        switch (opType) {
            case "ADD_PAGE" -> {
                String pageId = getStr(payload, "data", "pageId");
                String pageName = getStr(payload, "data", "pageName");
                if (pageId == null) pageId = UUID.randomUUID().toString();
                if (pageName == null) pageName = "Page";
                wb.addPage(pageId, pageName);
            }
            case "REMOVE_PAGE" -> {
                requireRole(sender, Role.CO_HOST, "remove page");
                wb.removePage(getStr(payload, "data", "pageId"));
            }
            case "RENAME_PAGE" -> {
                String pageId   = getStr(payload, "data", "pageId");
                String pageName = getStr(payload, "data", "pageName");
                wb.renamePage(pageId, pageName);
            }
            case "SWITCH_PAGE" -> {
                wb.switchPage(getStr(payload, "data", "pageId"));
                // SWITCH_PAGE is a navigation op; we persist so late joiners know active page
                wb.addOperation(wb.getActivePageId(), payload);
            }
            case "CLEAR_PAGE" -> {
                requireRole(sender, Role.CO_HOST, "clear page");
                String pageId = getStr(payload, "pageId");
                wb.clearPage(pageId);
                wb.addOperation(pageId, payload);
            }
            case "UNDO", "REDO" -> {
                // UNDO/REDO are client-side for now; server just persists as-is for replay
                String pageId = getStr(payload, "pageId");
                if (pageId != null) wb.addOperation(pageId, payload);
            }
            case "POINTER_MOVE", "LASER_POINTER" -> {
                // Transient: do not persist pointer/laser ops
            }
            default -> {
                // DRAW_PATH, DRAW_SHAPE, DRAW_TEXT, DRAW_IMAGE, ERASE, MOVE, RESIZE, DELETE
                String pageId = getStr(payload, "pageId");
                if (pageId == null) pageId = wb.getActivePageId();
                wb.addOperation(pageId, payload);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void requireRole(Participant sender, Role minimum, String action) {
        if (!sender.hasRole(minimum)) {
            throw new SecurityException("Role %s required to %s".formatted(minimum, action));
        }
    }

    private String getStr(JsonNode node, String... path) {
        JsonNode cur = node;
        for (String key : path) {
            if (cur == null || cur.isNull() || !cur.isObject()) return null;
            cur = cur.get(key);
        }
        return (cur != null && cur.isTextual()) ? cur.asText() : null;
    }
}
