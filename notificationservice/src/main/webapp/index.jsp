<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Notification Service - SmartCampus</title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <style>
        body { background: #f0f4ff; font-family: Arial, sans-serif; }
        .hero {
            background: linear-gradient(135deg, #0d1b6e, #2a52cc);
            color: white; padding: 60px 20px; text-align: center;
        }
        .card { border: none; border-radius: 14px; box-shadow: 0 4px 16px rgba(0,0,0,0.08); }
        .badge-method { font-size: 0.75rem; min-width: 60px; }
    </style>
</head>
<body>
<!-- Hero Banner -->
<div class="hero mb-5">
    <h1 class="display-5 fw-bold">&#128276; Notification Service</h1>
    <p class="lead mb-1">SmartCampus &mdash; Group 9 Microservice</p>
    <p class="opacity-75">Port: 8089 &nbsp;|&nbsp; Database: notification_db</p>
</div>
<div class="container">
    <!-- Quick Links -->
    <div class="row g-4 mb-5">
        <div class="col-md-4">
            <div class="card p-4 text-center h-100">
                <div style="font-size:2.5rem">&#128203;</div>
                <h5 class="mt-2">All Notifications</h5>
                <p class="text-muted small">Admin view &mdash; every notification in the system.</p>
                <a href="${pageContext.request.contextPath}/notifications/" class="btn btn-primary mt-auto">Vi
                    ew All</a>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-4 text-center h-100">
                <div style="font-size:2.5rem">&#128100;</div>
                <h5 class="mt-2">User Inbox</h5>
                <p class="text-muted small">Enter a User ID to see their notifications.</p>
                <form action="${pageContext.request.contextPath}/notifications/user" method="get"
                      class="d-flex gap-2 mt-auto">
                    <input type="number" name="id" class="form-control" placeholder="User ID" required min="1">
                    <button class="btn btn-success">Go</button>
                </form>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card p-4 text-center h-100">
                <div style="font-size:2.5rem">&#10133;</div>
                <h5 class="mt-2">Send Notification</h5>
                <p class="text-muted small">Manually create a new notification for any user.</p>
                <a href="${pageContext.request.contextPath}/notifications/new"
                   class="btn btn-warning mt-auto">Create New</a>
            </div>
        </div>
    </div>
    <!-- API Reference Table -->
    <div class="card p-4 mb-5">
        <h5 class="mb-3">&#128225; REST API Endpoints (for other microservices)</h5>
        <p class="text-muted small mb-3">
            Base URL: <code>http://localhost:8089/notification-service/api/notifications</code>
        </p>
        <table class="table table-bordered table-sm">
            <thead class="table-dark">
            <tr><th>Method</th><th>URL</th><th>Description</th><th>Body</th></tr>
            </thead>
            <tbody>
            <tr>
                <td><span class="badge bg-success badge-method">GET</span></td>
                <td><code>/api/notifications</code></td>
                <td>Get all notifications</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-success badge-method">GET</span></td>
                <td><code>/api/notifications?userId=1</code></td>
                <td>Get for user</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-success badge-method">GET</span></td>
                <td><code>/api/notifications?userId=1&amp;unread=true</code></td>
                <td>Get unread for user</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-success badge-method">GET</span></td>
                <td><code>/api/notifications/5</code></td>
                <td>Get by ID</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-success badge-method">GET</span></td>
                <td><code>/api/notifications/count?userId=1</code></td>
                <td>Count unread for user</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-primary badge-method">POST</span></td>
                <td><code>/api/notifications</code></td>
                <td>Create notification</td>
                <td><code>{"userId":1,"title":"...","message":"...","type":"PAYMENT"}</code></td>
            </tr>
            <tr>
                <td><span class="badge bg-warning text-dark badge-method">PUT</span></td>
                <td><code>/api/notifications/5/read</code></td>
                <td>Mark as read</td><td>None</td>
            </tr>
            <tr>
                <td><span class="badge bg-danger badge-method">DELETE</span></td>
                <td><code>/api/notifications/5</code></td>
                <td>Delete notification</td><td>None</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<footer class="text-center text-muted py-4">
    SmartCampus &copy; 2025 &mdash; Group 9: Notification Service
</footer>
</body>
</html>