<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="com.smartcampus.notification.model.Notification, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inbox &mdash; User ${userId}</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
        body { background: #f0f4ff; }
        .navbar { background: #0d1b6e !important; }
        .card-unread { border-left: 5px solid #dc3545; }
        .card-read { border-left: 5px solid #adb5bd; opacity: 0.85; }
    </style>
</head>
<body>
<nav class="navbar navbar-dark px-4 py-2 mb-4">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">&#128276; Notification Service</a
    >
    <div class="d-flex gap-2">
        <a href="${pageContext.request.contextPath}/notifications/" class="btn btn-outline-light btn-sm">A
            ll</a>
        <a href="${pageContext.request.contextPath}/notifications/new" class="btn btn-warning btn-sm">+ Ne
            w</a>
    </div>
</nav>
<div class="container">
    <!-- Header row -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4 class="mb-0">
            &#128100; User #${userId} &mdash; Notifications
            <%
                Integer unread = (Integer) request.getAttribute("unreadCount");
                if (unread != null && unread > 0) {
            %>
            <span class="badge bg-danger ms-2"><%= unread %> unread</span>
            <% } %>
        </h4>
        <%
            if (unread != null && unread > 0) {
        %>
        <form action="${pageContext.request.contextPath}/notifications/markAllRead" method="post">
            <input type="hidden" name="userId" value="${userId}">
            <button class="btn btn-sm btn-outline-success">&#10003; Mark All as Read</button>
        </form>
        <% } %>
    </div>
    <!-- Success message -->
    <% if ("created".equals(request.getParameter("success"))) { %>
    <div class="alert alert-success alert-dismissible fade show">
        &#10003; Notification created successfully!
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>
    <%
        @SuppressWarnings("unchecked")
        List<Notification> notes = (List<Notification>) request.getAttribute("notifications");
    %>
    <% if (notes == null || notes.isEmpty()) { %>
    <div class="alert alert-success">&#127881; No notifications found for this user.</div>
    <% } else { %>
    <% for (Notification n : notes) { %>
    <div class="card mb-3 <%= n.isRead() ? "card-read" : "card-unread" %>">
        <div class="card-body">
            <!-- Title row -->
            <div class="d-flex justify-content-between align-items-start">
                <h6 class="<%= !n.isRead() ? "fw-bold" : "text-muted" %> mb-1">
                    <%= n.getTitle() %>
                </h6>
                <div class="d-flex gap-2">
                    <%
                        String bc = "bg-secondary";
                        String tp = n.getType();
                        if ("PAYMENT".equals(tp)) bc = "bg-success";
                        else if ("ENROLLMENT".equals(tp)) bc = "bg-primary";
                        else if ("RESULT".equals(tp)) bc = "bg-warning text-dark";
                        else if ("ATTENDANCE".equals(tp)) bc = "bg-danger";
                        else if ("SYSTEM".equals(tp)) bc = "bg-dark";
                    %>
                    <span class="badge <%= bc %>"><%= tp %></span>
                    <% if (!n.isRead()) { %>
                    <span class="badge bg-danger">NEW</span>
                    <% } %>
                </div>
            </div>
            <!-- Message -->
            <p class="card-text mb-1"><%= n.getMessage() %></p>
            <small class="text-muted">
                Created: <%= n.getCreatedAt() %>
                <% if (n.getReadAt() != null) { %>
                &nbsp;| Read: <%= n.getReadAt() %>
                <% } %>
            </small>
            <!-- Action buttons -->
            <div class="mt-2 d-flex gap-2">
                <% if (!n.isRead()) { %>
                <form action="${pageContext.request.contextPath}/notifications/markRead" method="post">
                    <input type="hidden" name="id" value="<%= n.getId() %>">
                    <input type="hidden" name="userId" value="${userId}">
                    <button class="btn btn-sm btn-success">&#10003; Mark as Read</button>
                </form>
                <% } %>
                <form action="${pageContext.request.contextPath}/notifications/delete" method="post"
                      onsubmit="return confirm('Delete this notification?')">
                    <input type="hidden" name="id" value="<%= n.getId() %>">
                    <input type="hidden" name="userId" value="${userId}">
                    <button class="btn btn-sm btn-outline-danger">&#128465; Delete</button>
                </form>
            </div>
        </div>
    </div>
    <% } %>
    <% } %>
    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">&larr; Home</a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</htm