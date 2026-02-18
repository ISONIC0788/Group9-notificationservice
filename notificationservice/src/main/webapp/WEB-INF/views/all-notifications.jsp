<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="com.smartcampus.notification.model.Notification, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Notifications &mdash; Admin</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
        body { background: #f0f4ff; }
        .navbar { background: #0d1b6e !important; }
        .unread-row { font-weight: bold; background: #fff9e6; }
    </style>
</head>
<body>
<nav class="navbar navbar-dark px-4 py-2 mb-4">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">&#128276; Notification Service</a
    >
    <a href="${pageContext.request.contextPath}/notifications/new" class="btn btn-warning btn-sm">
        + New Notification
    </a>
</nav>
<div class="container">
    <h4 class="mb-3">
        All Notifications
        <span class="badge bg-secondary">
<%= ((List) request.getAttribute("notifications")).size() %> total
</span>
    </h4>
    <%
        @SuppressWarnings("unchecked")
        List<Notification> notes = (List<Notification>) request.getAttribute("notifications");
    %>
    <% if (notes == null || notes.isEmpty()) { %>
    <div class="alert alert-info">No notifications found in the database.</div>
    <% } else { %>
    <div class="card shadow-sm">
        <table class="table table-hover mb-0">
            <thead class="table-dark">
            <tr>
                <th>#ID</th>
                <th>User ID</th>
                <th>Title</th>
                <th>Type</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <% for (Notification n : notes) { %>
            <tr class="<%= !n.isRead() ? "unread-row" : "" %>">
                <td><%= n.getId() %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/notifications/user?id=<%= n.getUserId() %>">
                        User #<%= n.getUserId() %>
                    </a>
                </td>
                <td><%= n.getTitle() %></td>
                <td>
                    <%
                        String badgeClass = "bg-secondary";
                        String t = n.getType();
                        if ("PAYMENT".equals(t)) badgeClass = "bg-success";
                        else if ("ENROLLMENT".equals(t)) badgeClass = "bg-primary";
                        else if ("RESULT".equals(t)) badgeClass = "bg-warning text-dark";
                        else if ("ATTENDANCE".equals(t)) badgeClass = "bg-danger";
                        else if ("SYSTEM".equals(t)) badgeClass = "bg-dark";
                    %>
                    <span class="badge <%= badgeClass %>"><%= t %></span>
                </td>
                <td>
<span class="badge <%= n.isRead() ? "bg-light text-dark border" : "bg-danger" %>">
<%= n.isRead() ? "Read" : "Unread" %>
</span>
                </td>
                <td><small><%= n.getCreatedAt() %></small></td>
                <td>
                    <% if (!n.isRead()) { %>
                    <form action="${pageContext.request.contextPath}/notifications/markRead"
                          method="post" class="d-inline">
                        <input type="hidden" name="id" value="<%= n.getId() %>">
                        <input type="hidden" name="userId" value="<%= n.getUserId() %>">
                        <button class="btn btn-sm btn-outline-success py-0">&#10003; Read</button>
                    </form>
                    <% } %>
                    <form action="${pageContext.request.contextPath}/notifications/delete"
                          method="post" class="d-inline"
                          onsubmit="return confirm('Delete this notification?')">
                        <input type="hidden" name="id" value="<%= n.getId() %>">
                        <input type="hidden" name="userId" value="<%= n.getUserId() %>">
                        <button class="btn btn-sm btn-outline-danger py-0">&#128465;</button>
                    </form>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
    <% } %>
</div>
</body>
</html>