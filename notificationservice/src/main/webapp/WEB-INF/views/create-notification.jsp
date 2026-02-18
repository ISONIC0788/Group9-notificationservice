<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Notification</title>
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
<div class="container" style="max-width: 580px;">
    <div class="card shadow-sm p-4">
        <h4 class="mb-4">&#10133; Create New Notification</h4>

        <% String error = (String) request.getAttribute("error"); %>
        <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/notifications/create" method="post">
            <div class="mb-3">
                <label class="form-label fw-semibold">User ID <span class="text-danger">*</span></label>
                <input type="number" name="userId" class="form-control"
                       placeholder="Enter the user's ID (e.g. 1)"
                       value="<%= request.getAttribute("userId") != null ? request.getAttribute("userId") : "" %>"
                       required min="1">
                <div class="form-text">The notification will be sent to this user.</div>
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Title <span class="text-danger">*</span></label>
                <input type="text" name="title" class="form-control"
                       placeholder="Short notification title (e.g. Payment Confirmed)"
                       value="<%= request.getAttribute("title") != null ? request.getAttribute("title") : "" %>"
                       required>
            </div>

            <div class="mb-3">
                <label class="form-label fw-semibold">Message <span class="text-danger">*</span></label>
                <textarea name="message" class="form-control" rows="4"
                          placeholder="Full notification message..." required><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></textarea>
            </div>

            <div class="mb-4">
                <label class="form-label fw-semibold">Type</label>
                <select name="type" class="form-select">
                    <option value="GENERAL">GENERAL &mdash; General announcement</option>
                    <option value="PAYMENT">PAYMENT &mdash; Payment related</option>
                    <option value="ENROLLMENT">ENROLLMENT &mdash; Course enrollment</option>
                    <option value="RESULT">RESULT &mdash; Academic results</option>
                    <option value="ATTENDANCE">ATTENDANCE &mdash; Attendance alert</option>
                    <option value="SYSTEM">SYSTEM &mdash; System message</option>
                </select>
            </div>

            <div class="d-grid gap-2">
                <button type="submit" class="btn btn-primary">&#10003; Send Notification</button>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>