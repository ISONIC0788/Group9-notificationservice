package org.ebedi.notificationservice.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * DBConnection.java
 * Opens a JDBC connection to the notification_db MySQL database.
 *
 * IMPORTANT: Change USERNAME and PASSWORD to match your MySQL setup.
 */
public class DBConnection {
    // ■■ CHANGE THESE TO MATCH YOUR MYSQL ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DB_NAME = "notification_db";
    private static final String USERNAME = "root"; // your MySQL username
    private static final String PASSWORD = ""; // your MySQL password
    // ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    // Load MySQL driver once
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "MySQL Driver not found. Add mysql-connector-j.jar to WEB-INF/lib/", e);
        }
    }
    /**
     * Returns a new database connection.
     * Always call inside try-with-resources so it closes automatically.
     *
     * Usage:
     * try (Connection conn = DBConnection.getConnection()) {
     * // your code here
     * }
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}