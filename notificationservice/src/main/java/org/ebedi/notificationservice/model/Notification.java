
package org.ebedi.notificationservice.model

import java.time.LocalDateTime;
/**
 * Notification.java (Model)
 * --------------------------
 * This class represents ONE row in the notifications table.
 * It is a plain Java object (POJO) - no logic, just data + getters/setters.
 *
 * It also has a toJSON() method so we can easily convert it to JSON
 * for our REST API without needing any external library.
 */
public class Notification {
    // These fields match the columns in the notifications table
    private int id;
    private int userId;
    private String title;
    private String message;
    private String type; // GENERAL | PAYMENT | ENROLLMENT | RESULT | ATTENDANCE | SYSTE
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime readAt; // null until the notification is read
// ■■ Constructors ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    /** Empty constructor - used when reading from database */
    public Notification() {}
    /** Constructor used when creating a NEW notification */
    public Notification(int userId, String title, String message, String type) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = (type != null && !type.isEmpty()) ? type : "GENERAL";
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    // ■■ Getters and Setters ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { this.isRead = read; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }
// ■■ toJSON() ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    /**
     * Converts this notification to a JSON string.
     * Called by the API controller when responding to other services.
     *
     * Example output:
     * {
     * "id": 1,
     * "userId": 1,
     * "title": "Payment Confirmed",
     * "message": "Your payment was received.",
     * "type": "PAYMENT",
     * "isRead": false,
     * "createdAt": "2025-01-15T10:30:00",
     * "readAt": ""
     * }
     */
    public String toJSON() {
        return "{"
                + "\"id\":" + id + ","
                + "\"userId\":" + userId + ","
                + "\"title\":\"" + esc(title) + "\"" + ","
                + "\"message\":\"" + esc(message) + "\"" + ","
                + "\"type\":\"" + esc(type) + "\"" + ","
                + "\"isRead\":" + isRead + ","
                + "\"createdAt\":\"" + (createdAt != null ? createdAt.toString() : "") + "\"" + ","
                + "\"readAt\":\"" + (readAt != null ? readAt.toString() : "") + "\""
                + "}";
    }
    // escapes special characters for JSON safety
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}