package org.ebedi.notificationservice.dao;


import org.ebedi.notificationservice.model.Notification;
import org.ebedi.notificationservice.util.DBConnection;
import java.sql.*; import java.util.ArrayList; import java.util.List;
/**
 *	NotificationDAO.java  (Data Access Object)
 *	------------------------------------------- * ALL database operations live here.  * No SQL should appear anywhere else in the project.
 *	 * DAO pattern:  *   Controller  ->  Service  ->  DAO  ->  MySQL
 *	 * Each method:  *   1. Opens a connection (auto-closed with try-with-resources)  *   2. Prepares a SQL statement
 *	3. Executes it *   4. Returns result  */
public class NotificationDAO {
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    // CREATE
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    /**      * Inserts a new notification into the database.
     *
     *	@param n  the Notification object to save     * @return   the generated id (primary key), or -1 if insert failed      */
    public int create(Notification n) {         String sql = "INSERT INTO notifications (user_id, title, message, type, is_read, created_at) "                    + "VALUES (?, ?, ?, ?, 0, NOW())";
        try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getUserId());             ps.setString(2, n.getTitle());             ps.setString(3, n.getMessage());
            ps.setString(4, n.getType() != null ? n.getType() : "GENERAL");
            int rows = ps.executeUpdate();
            if (rows > 0) {                 // Get the auto-generated ID
                try (ResultSet keys = ps.getGeneratedKeys()) {

                    if (keys.next()) {
                        return keys.getInt(1); // return the new id                     }
                    }
                }
            } catch (SQLException e) {             System.err.println("DAO ERROR - create(): " + e.getMessage());             e.printStackTrace();         }
            return -1; // failed     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ  - findAll
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Returns every notification in the database, newest first.
             * Used by the admin view.      */
            public List<Notification> findAll() {         List<Notification> list = new ArrayList<>();         String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
                try (Connection conn = DBConnection.getConnection();              Statement  st   = conn.createStatement();              ResultSet  rs   = st.executeQuery(sql)) {
                    while (rs.next()) {                 list.add(mapRow(rs));             }
                } catch (SQLException e) {             System.err.println("DAO ERROR - findAll(): " + e.getMessage());         }
                return list;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ  - findById
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Returns one notification by its primary key id.
             * Returns null if not found.      */
            public Notification findById(int id) {         String sql = "SELECT * FROM notifications WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery()) {                 if (rs.next()) {                     return mapRow(rs);                 }
                    }
                } catch (SQLException e) {


                    System.err.println("DAO ERROR - findById(): " + e.getMessage());         }
                return null;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ  - findByUserId
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Returns all notifications for a specific user, newest first.      */
            public List<Notification> findByUserId(int userId) {         List<Notification> list = new ArrayList<>();         String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {                 while (rs.next()) {                     list.add(mapRow(rs));                 }
                    }
                } catch (SQLException e) {             System.err.println("DAO ERROR - findByUserId(): " + e.getMessage());         }
                return list;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ  - findUnreadByUserId
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Returns only UNREAD notifications for a user.      */
            public List<Notification> findUnreadByUserId(int userId) {         List<Notification> list = new ArrayList<>();         String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = 0 "                    + "ORDER BY created_at DESC";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {                 while (rs.next()) {                     list.add(mapRow(rs));                 }
                    }
                } catch (SQLException e) {             System.err.println("DAO ERROR - findUnreadByUserId(): " + e.getMessage());         }
                return list;
            }

            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // READ  - countUnread
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Returns the number of unread notifications for a user.
             * Used for notification badge counters.      */
            public int countUnread(int userId) {         String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = 0";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);
                    try (ResultSet rs = ps.executeQuery()) {                 if (rs.next()) {                     return rs.getInt(1);                 }
                    }
                } catch (SQLException e) {             System.err.println("DAO ERROR - countUnread(): " + e.getMessage());         }         return 0;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // UPDATE  - markAsRead (one)
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Marks ONE notification as read and saves the read timestamp.
             *      * @return true if the update succeeded, false if id not found      */
            public boolean markAsRead(int id) {         String sql = "UPDATE notifications SET is_read = 1, read_at = NOW() WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int rows = ps.executeUpdate();             return rows > 0;
                } catch (SQLException e) {             System.err.println("DAO ERROR - markAsRead(): " + e.getMessage());         }
                return false;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // UPDATE  - markAllAsRead (all for a user)
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Marks ALL unread notifications for a user as read.
             *      * @return the number of rows updated
             */

            public int markAllAsRead(int userId) {         String sql = "UPDATE notifications SET is_read = 1, read_at = NOW() "                    + "WHERE user_id = ? AND is_read = 0";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, userId);             return ps.executeUpdate();
                } catch (SQLException e) {             System.err.println("DAO ERROR - markAllAsRead(): " + e.getMessage());         }         return 0;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // DELETE
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Deletes a notification by id.
             *      * @return true if deleted, false if id not found      */
            public boolean delete(int id) {         String sql = "DELETE FROM notifications WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();              PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    return ps.executeUpdate() > 0;
                } catch (SQLException e) {             System.err.println("DAO ERROR - delete(): " + e.getMessage());         }
                return false;     }
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            // PRIVATE HELPER  - mapRow
            // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            /**      * Converts one row from a ResultSet into a Notification object.
             * Called after every SELECT query.      */
            private Notification mapRow(ResultSet rs) throws SQLException {         Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setUserId(rs.getInt("user_id"));
                n.setTitle(rs.getString("title"));
                n.setMessage(rs.getString("message"));
                n.setType(rs.getString("type"));
                n.setRead(rs.getBoolean("is_read"));
                Timestamp created = rs.getTimestamp("created_at");
                if (created != null) n.setCreatedAt(created.toLocalDateTime());
                Timestamp readAt = rs.getTimestamp("read_at");
                if (readAt != null) n.setReadAt(readAt.toLocalDateTime());

                return n;     }
        }
