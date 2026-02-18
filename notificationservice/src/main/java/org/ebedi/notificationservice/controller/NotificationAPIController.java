package org.ebedi.notificationservice.controller;

import org.ebedi.notificationservice.model.Notification;
import org.ebedi.notificationservice.service.NotificationService;
import org.ebedi.notificationservice.util.JSONUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * NotificationAPIController.java (REST API Controller)
 * -------------------------------------------------------
 * This Servlet handles HTTP requests from OTHER microservices.
 * It reads and writes JSON - no HTML, no JSP.
 * URL pattern: /api/notifications/*
 */
@WebServlet("/api/notifications/*")
public class NotificationAPIController extends HttpServlet {
    private final NotificationService service = new NotificationService();

    // ■■ GET ■■
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareJSON(resp);
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo(); // e.g. null, "/5", "/count"

        // --- /api/notifications/count?userId=X --
        if ("/count".equals(pathInfo)) {
            int userId = toInt(req.getParameter("userId"));
            int count = service.countUnread(userId);
            out.print("{\"userId\":" + userId + ",\"unreadCount\":" + count + "}");
            return;
        }

        // --- /api/notifications/5 (get by id) --
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int id = Integer.parseInt(pathInfo.substring(1));
            Notification n = service.getById(id);
            if (n == null) {
                resp.setStatus(404);
                out.print(JSONUtil.error("Notification not found with id: " + id));
            } else {
                out.print(n.toJSON());
            }
            return;
        }

        // --- /api/notifications OR ?userId=X OR ?userId=X&unread=true ---
        String userIdParam = req.getParameter("userId");
        String unreadParam = req.getParameter("unread");
        List<Notification> list;
        if (userIdParam != null) {
            int userId = toInt(userIdParam);
            if ("true".equalsIgnoreCase(unreadParam)) {
                list = service.getUnreadByUserId(userId);
            } else {
                list = service.getByUserId(userId);
            }
        } else {
            list = service.getAllNotifications();
        }
        out.print(toJSONArray(list));
    }

    // ■■ POST (Create) ■■
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareJSON(resp);
        PrintWriter out = resp.getWriter();
        // Read JSON body from the request
        String body = readBody(req);
        // Parse fields from JSON body
        int userId = extractInt(body, "userId");
        String title = extractStr(body, "title");
        String message = extractStr(body, "message");
        String type = extractStr(body, "type");
        // Call service to create
        int newId = service.createNotification(userId, title, message, type);
        if (newId > 0) {
            resp.setStatus(201); // 201 Created
            out.print("{\"status\":\"success\",\"id\":" + newId + ",\"message\":\"Notification created successfully.\"}");
        } else {
            resp.setStatus(400); // 400 Bad Request
            out.print(JSONUtil.error("Failed to create notification. Make sure userId (int > 0), title, and message are all provided."));
        }
    }

    // ■■ PUT (Mark as read) ■■
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareJSON(resp);
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo(); // should be like "/5/read"
        if (pathInfo != null && pathInfo.matches("/\\d+/read")) {
            // extract the id from "/5/read"
            int id = Integer.parseInt(pathInfo.split("/")[1]);
            boolean ok = service.markAsRead(id);
            if (ok) {
                out.print(JSONUtil.success("Notification " + id + " marked as read."));
            } else {
                resp.setStatus(404);
                out.print(JSONUtil.error("Notification not found with id: " + id));
            }
        } else {
            resp.setStatus(400);
            out.print(JSONUtil.error("Invalid URL. Use: PUT /api/notifications/{id}/read"));
        }
    }

    // ■■ DELETE ■■
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        prepareJSON(resp);
        PrintWriter out = resp.getWriter();
        String pathInfo = req.getPathInfo(); // should be like "/5"
        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean ok = service.deleteNotification(id);
            if (ok) {
                out.print(JSONUtil.success("Notification " + id + " deleted."));
            } else {
                resp.setStatus(404);
                out.print(JSONUtil.error("Notification not found with id: " + id));
            }
        } else {
            resp.setStatus(400);
            out.print(JSONUtil.error("Invalid URL. Use: DELETE /api/notifications/{id}"));
        }
    }

    // ■■ Private Helpers ■■
    /** Sets Content-Type to JSON and encoding to UTF-8 */
    private void prepareJSON(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    /** Reads the raw text body of the HTTP request */
    private String readBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private String extractStr(String json, String key) {
        if (json == null || key == null) return null;
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return null;
        int q1 = json.indexOf('"', colon + 1);
        if (q1 < 0) return null;
        int q2 = json.indexOf('"', q1 + 1);
        if (q2 < 0) return null;
        return json.substring(q1 + 1, q2);
    }

    private int extractInt(String json, String key) {
        if (json == null || key == null) return -1;
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return -1;
        int colon = json.indexOf(':', idx + pattern.length());
        if (colon < 0) return -1;
        int start = colon + 1;
        while (start < json.length() && !Character.isDigit(json.charAt(start))) start++;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        if (start == end) return -1;
        try {
            return Integer.parseInt(json.substring(start, end));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return -1;
        }
    }

    private String toJSONArray(List<Notification> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toJSON());
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}