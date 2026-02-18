<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="org.ebedi.notificationservice.model.Notification" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Notification</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
        body { background: #f0f4ff; }
        .navbar { background: #0d1b6e !important; }
    </style>
</head>
<body>
<nav class="navbar navbar-dark px-4 py-2 mb-4">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">&#128276; Notification Service</a>
</nav>
<div class="container" style="max-width: 640px;">
    <%
        Notification n = (Notification) request.getAttribute("notification");
    %>
    <% if (n == null) { %>
    <div class="alert alert-danger">Notification not found.</div>
    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">&larr; Home</a>
    <% } else { %>
    <div class="card shadow-sm p-4">
        <h4 class="mb-1"><%= n.getTitle() %></h4>
        <div class="mb-3 d-flex gap-2 flex-wrap">
            <span class="badge bg-secondary">ID: <%= n.getId() %></span>
            <span class="badge bg-info text-dark">User #<%= n.getUserId() %></span>
            <%
                String bc = "bg-secondary";
                if ("PAYMENT".equals(n.getType())) bc = "bg-success";
                else if ("ENROLLMENT".equals(n.getType())) bc = "bg-primary";
                else if ("RESULT".equals(n.getType())) bc = "bg-warning text-dark";
                else if ("ATTENDANCE".equals(n.getType())) bc = "bg-danger";
                else if ("SYSTEM".equals(n.getType())) bc = "bg-dark";
            %>
            <span class="badge <%= bc %>"><%= n.getType() %></span>
            <span class="badge <%= n.isRead() ? "bg-light text-dark border" : "bg-danger" %>">
                <%= n.isRead() ? "Read" : "Unread" %>
            </span>
        </div>
        <hr>
        <p class="fs-6"><%= n.getMessage() %></p>
        <hr>
        <div class="text-muted small">
            <div>Created: <%= n.getCreatedAt() %></div>
            <% if (n.getReadAt() != null) { %>
            <div>Read at: <%= n.getReadAt() %></div>
            <% } %>
        </div>
        <div class="mt-3 d-flex gap-2">
            <a href="${pageContext.request.contextPath}/notifications/user?id=<%= n.getUserId() %>"
               class="btn btn-secondary">&larr; Back to Inbox</a>
            <% if (!n.isRead()) { %>
            <form action="${pageContext.request.contextPath}/notifications/markRead" method="post">
                <input type="hidden" name="id" value="<%= n.getId() %>">
                <input type="hidden" name="userId" value="<%= n.getUserId() %>">
                <button class="btn btn-success">&#10003; Mark as Read</button>
            </form>
            <% } %>
        </div>
    </div>
    <% } %>
</div>
</body>
</html>