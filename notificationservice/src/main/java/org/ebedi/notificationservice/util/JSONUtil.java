package org.ebedi.notificationservice.util;


/**
 * JSONUtil.java  * Builds simple JSON strings without any external library.  * This keeps the project compliant with the "no Spring / no extra frameworks" rule.  */
public class JSONUtil {
    /**      * Returns a JSON success response.      * Example output: {"status":"success","message":"Done"}      */
    public static String success(String message) {
        return "{\"status\":\"success\",\"message\":\"" + escape(message) + "\"}";     }
    /**      * Returns a JSON error response.      * Example output: {"status":"error","message":"Not found"}      */
    public static String error(String message) {
        return "{\"status\":\"error\",\"message\":\"" + escape(message) + "\"}";     }
    /**      * Escapes special characters so text is safe to put inside a JSON string.      * Handles: backslash, double-quote, newline, carriage return, tab.      */
    public static String escape(String s) {         if (s == null) return "";         return s.replace("\\", "\\\\")                 .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")                 .replace("\t", "\\t");
    }
}

