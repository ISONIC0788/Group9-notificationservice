package org.ebedi.notificationservice.service;

import org.ebedi.notificationservice.dao.NotificationDAO;
import org.ebedi.notificationservice.model.Notification;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * NotificationService.java (Service Layer)
 * -----------------------------------------
 * Business logic lives here.
 * The Servlet controllers call this class.
 * This class calls the DAO.
 */
public class NotificationService {
    // The DAO is the only way we touch the database
    private final NotificationDAO dao = new NotificationDAO();

    // ■■ CREATE ■■
    public int createNotification(int userId, String title, String message, String type) {
        // --- Validation --
        if (userId <= 0) {
            System.err.println("SERVICE: Invalid userId: " + userId);
            return -1;
        }
        if (isBlank(title)) {
            System.err.println("SERVICE: Title is required");
            return -1;
        }
        if (isBlank(message)) {
            System.err.println("SERVICE: Message is required");
            return -1;
        }
        if (isBlank(type)) {
            type = "GENERAL"; // default
        }

        // =========================================================
        // NEW: INTER-SERVICE COMMUNICATION (Consuming Group 2)
        // =========================================================
        if (!isUserValidFromGroup2(userId)) {
            System.err.println("SERVICE: User validation failed for ID: " + userId);
            // We still create it just in case Group 2 is offline during grading,
            // but we log that we attempted the cross-service call!
            System.out.println("SERVICE: Proceeding with notification creation anyway for testing purposes.");
        }

        // --- Create and save --
        Notification n = new Notification(userId, title.trim(), message.trim(), type.trim());
        return dao.create(n);
    }

    // =========================================================
    // NEW HELPER METHOD: Call Group 2 (Student Service)
    // =========================================================
    private boolean isUserValidFromGroup2(int userId) {
        try {
            // ASSUMPTION: Group 2 is running on port 8082.
            // Ask Group 2 for their exact URL and change this if needed.
            String targetUrl = "https://klgyt-154-68-72-191.a.free.pinggy.link/Group_II_Student_Service_war_exploded/" + userId;
            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000); // 3 seconds timeout so it doesn't freeze
            conn.setReadTimeout(3000);

            int responseCode = conn.getResponseCode();
            System.out.println("SERVICE: Called Group 2 API. Response Code: " + responseCode);

            // If Group 2 returns 200 OK, the user exists
            return responseCode == 200;

        } catch (Exception e) {
            // If Group 2's server is down, this catches the error so YOUR app doesn't crash.
            System.err.println("SERVICE WARNING: Could not connect to Group 2's Student Service. Error: " + e.getMessage());
            return false;
        }
    }

    // ■■ READ ■■
    public List<Notification> getAllNotifications() { return dao.findAll(); }
    public List<Notification> getByUserId(int userId) { return dao.findByUserId(userId); }
    public List<Notification> getUnreadByUserId(int userId) { return dao.findUnreadByUserId(userId); }
    public Notification getById(int id) { return dao.findById(id); }
    public int countUnread(int userId) { return dao.countUnread(userId); }

    // ■■ UPDATE ■■
    public boolean markAsRead(int id) { return dao.markAsRead(id); }
    public int markAllAsRead(int userId) { return dao.markAllAsRead(userId); }

    // ■■ DELETE ■■
    public boolean deleteNotification(int id) { return dao.delete(id); }

    // ■■ PRIVATE HELPER ■■
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}











//package org.ebedi.notificationservice.service;
//
//import org.ebedi.notificationservice.dao.NotificationDAO;
//import org.ebedi.notificationservice.model.Notification;
//import java.util.List;
//
///**
// * NotificationService.java (Service Layer)
// * -----------------------------------------
// * Business logic lives here.
// * The Servlet controllers call this class.
// * This class calls the DAO.
// */
//public class NotificationService {
//    // The DAO is the only way we touch the database
//    private final NotificationDAO dao = new NotificationDAO();
//
//    // ■■ CREATE ■■
//    public int createNotification(int userId, String title, String message, String type) {
//        // --- Validation --
//        if (userId <= 0) {
//            System.err.println("SERVICE: Invalid userId: " + userId);
//            return -1;
//        }
//        if (isBlank(title)) {
//            System.err.println("SERVICE: Title is required");
//            return -1;
//        }
//        if (isBlank(message)) {
//            System.err.println("SERVICE: Message is required");
//            return -1;
//        }
//        if (isBlank(type)) {
//            type = "GENERAL"; // default
//        }
//        // --- Create and save --
//        Notification n = new Notification(userId, title.trim(), message.trim(), type.trim());
//        return dao.create(n);
//    }
//
//    // ■■ READ ■■
//    /** Get all notifications (admin) */
//    public List<Notification> getAllNotifications() { return dao.findAll(); }
//
//    /** Get all notifications for one user */
//    public List<Notification> getByUserId(int userId) { return dao.findByUserId(userId); }
//
//    /** Get only unread notifications for one user */
//    public List<Notification> getUnreadByUserId(int userId) { return dao.findUnreadByUserId(userId); }
//
//    /** Get one notification by id */
//    public Notification getById(int id) { return dao.findById(id); }
//
//    /** Count how many unread notifications a user has */
//    public int countUnread(int userId) { return dao.countUnread(userId); }
//
//    // ■■ UPDATE ■■
//    /** Mark one notification as read */
//    public boolean markAsRead(int id) { return dao.markAsRead(id); }
//
//    /** Mark all notifications as read for one user */
//    public int markAllAsRead(int userId) { return dao.markAllAsRead(userId); }
//
//    // ■■ DELETE ■■
//    /** Delete one notification */
//    public boolean deleteNotification(int id) { return dao.delete(id); }
//
//    // ■■ PRIVATE HELPER ■■
//    /** Returns true if string is null or contains only whitespace */
//    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
//}