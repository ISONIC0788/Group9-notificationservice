package org.ebedi.notificationservice.service;

package com.smartcampus.notification.service;
import com.smartcampus.notification.dao.NotificationDAO; import com.smartcampus.notification.model.Notification;
import java.util.List;
/**
 *	NotificationService.java  (Service Layer) * ----------------------------------------- * Business logic lives here.
 *	The Servlet controllers call this class.
 *	This class calls the DAO.
 *	 * Why have a Service layer?
 *	- Keeps validation out of controllers
 *	- Keeps SQL out of controllers *   - Makes code easier to test and maintain
 *	 * Flow:  *   Controller  ->  Service (validates)  ->  DAO (database)  */
public class NotificationService {
    // The DAO is the only way we touch the database     private final NotificationDAO dao = new NotificationDAO();
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    // CREATE
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    /**      * Creates a new notification after validating the input.
     *	     * Rules:
     *	- userId must be a positive number
     *	- title must not be empty
     *	- message must not be empty     *   - type defaults to GENERAL if not provided
     *	     * @return the generated id, or -1 if validation fails or insert fails      */
    public int createNotification(int userId, String title, String message, String type) {
        // --- Validation --        if (userId <= 0) {             System.err.println("SERVICE: Invalid userId: " + userId);             return -1;         }
        if (isBlank(title)) {             System.err.println("SERVICE: Title is required");             return -1;
        }

        if (isBlank(message)) {             System.err.println("SERVICE: Message is required");             return -1;         }
        if (isBlank(type)) {
            type = "GENERAL"; // default         }
            // --- Create and save --        Notification n = new Notification(userId, title.trim(), message.trim(), type.trim());         return dao.create(n);     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /** Get all notifications (admin) */
            public List<Notification> getAllNotifications() {         return dao.findAll();     }
            /** Get all notifications for one user */     public List<Notification> getByUserId(int userId) {         return dao.findByUserId(userId);     }
            /** Get only unread notifications for one user */     public List<Notification> getUnreadByUserId(int userId) {         return dao.findUnreadByUserId(userId);     }
            /** Get one notification by id */     public Notification getById(int id) {         return dao.findById(id);     }
            /** Count how many unread notifications a user has */     public int countUnread(int userId) {         return dao.countUnread(userId);     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // UPDATE
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /** Mark one notification as read */     public boolean markAsRead(int id) {         return dao.markAsRead(id);     }
            /** Mark all notifications as read for one user */     public int markAllAsRead(int userId) {         return dao.markAllAsRead(userId);     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // DELETE
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /** Delete one notification */
            public boolean deleteNotification(int id) {

                return dao.delete(id);     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // PRIVATE HELPER
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /** Returns true if string is null or contains only whitespace */     private boolean isBlank(String s) {         return s == null || s.trim().isEmpty();     }
        }
