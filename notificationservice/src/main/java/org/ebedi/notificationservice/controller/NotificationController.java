package org.ebedi.notificationservice.controller;

import org.ebedi.notificationservice.model.Notification;
import org.ebedi.notificationservice.service.NotificationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * NotificationController.java (MVC Controller for browser requests)
 * -------------------------------------------------------------------
 * This Servlet handles all requests that come from a web browser.
 * It uses JSP pages to display the response (HTML).
 * URL pattern: /notifications/*
 */
@WebServlet("/notifications/*")
public class NotificationController extends HttpServlet {
    private final NotificationService service = new NotificationService();

    // ■■ GET requests (display pages) ■■
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/";

        switch (path) {
            case "/user": {
                // Show all notifications for a specific user
                int userId = toInt(req.getParameter("id"));
                List<Notification> list = service.getByUserId(userId);
                int unread = service.countUnread(userId);
                req.setAttribute("notifications", list);
                req.setAttribute("unreadCount", unread);
                req.setAttribute("userId", userId);
                forward(req, resp, "/WEB-INF/views/user-notifications.jsp");
                break;
            }
            case "/unread": {
                // Show only unread notifications for a user
                int userId = toInt(req.getParameter("id"));
                List<Notification> list = service.getUnreadByUserId(userId);
                int unread = service.countUnread(userId);
                req.setAttribute("notifications", list);
                req.setAttribute("unreadCount", unread);
                req.setAttribute("userId", userId);
                forward(req, resp, "/WEB-INF/views/user-notifications.jsp");
                break;
            }
            case "/view": {
                // Show one notification detail
                int id = toInt(req.getParameter("id"));
                Notification n = service.getById(id);
                req.setAttribute("notification", n);
                forward(req, resp, "/WEB-INF/views/view-notification.jsp");
                break;
            }
            case "/new": {
                // Show the create-notification form
                forward(req, resp, "/WEB-INF/views/create-notification.jsp");
                break;
            }
            default: {
                // Show all notifications (admin view)
                List<Notification> all = service.getAllNotifications();
                req.setAttribute("notifications", all);
                forward(req, resp, "/WEB-INF/views/all-notifications.jsp");
                break;
            }
        }
    }

    // ■■ POST requests (form submissions) ■■
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/";

        switch (path) {
            case "/create": {
                // Read form fields
                int userId = toInt(req.getParameter("userId"));
                String title = req.getParameter("title");
                String message = req.getParameter("message");
                String type = req.getParameter("type");

                // Call service to create
                int newId = service.createNotification(userId, title, message, type);

                if (newId > 0) {
                    // Success: redirect to user's notification list
                    resp.sendRedirect(req.getContextPath() + "/notifications/user?id=" + userId + "&success=created");
                } else {
                    // Failed: show form again with error
                    req.setAttribute("error", "Failed to create. Check all fields and try again.");
                    req.setAttribute("userId", userId);
                    req.setAttribute("title", title);
                    req.setAttribute("message", message);
                    req.setAttribute("type", type);
                    forward(req, resp, "/WEB-INF/views/create-notification.jsp");
                }
                break;
            }
            case "/markRead": {
                int id = toInt(req.getParameter("id"));
                int userId = toInt(req.getParameter("userId"));
                service.markAsRead(id);
                resp.sendRedirect(req.getContextPath() + "/notifications/user?id=" + userId);
                break;
            }
            case "/markAllRead": {
                int userId = toInt(req.getParameter("userId"));
                service.markAllAsRead(userId);
                resp.sendRedirect(req.getContextPath() + "/notifications/user?id=" + userId);
                break;
            }
            case "/delete": {
                int id = toInt(req.getParameter("id"));
                int userId = toInt(req.getParameter("userId"));
                service.deleteNotification(id);
                resp.sendRedirect(req.getContextPath() + "/notifications/user?id=" + userId);
                break;
            }
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown path: " + path);
        }
    }

    // ■■ Helpers ■■
    private int toInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String jsp) throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, resp);
    }
}