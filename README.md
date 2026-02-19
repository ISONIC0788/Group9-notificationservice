
# SmartCampus - Notification Service (Group 9)

## 📌 Project Overview
The **Notification Service** is a core microservice of the **SmartCampus Distributed University Management System**. Developed by **Group 9**, this independent service manages all system-wide alerts and messages sent to users (students, lecturers, admins).

This project adheres strictly to the assignment's microservice architecture rules: it runs as an independent Dynamic Web Project (WAR) on its own dedicated port, uses its own isolated database schema, follows the MVC design pattern, and communicates with other microservices via standard HTTP/JSON requests.

---

## 🛠️ Technology Stack
Conforming to the strict assignment requirements (No Spring Boot, No Hibernate):
* **Backend:** Java Servlets (Jakarta EE 6.0), JSP
* **Database:** MySQL 8+
* **Data Access:** Raw JDBC (`mysql-connector-j`)
* **Web Server:** Apache Tomcat
* **Frontend:** HTML5, CSS3, Bootstrap 5.3 (via CDN)
* **Build Tool:** Maven

---

## 🚀 Installation & Setup Guide

### 1. Database Configuration
This service requires its own isolated database.
1. Open your MySQL client (e.g., MySQL Workbench or terminal).
2. Execute the provided database script located at `db/notification_db.sql`.
3. This script will automatically create the `notification_db` database, the `notifications` table, and insert dummy data for testing.

### 2. Update Database Credentials
If your local MySQL username and password differ from the defaults, update the configuration:
1. Open `src/main/java/org/ebedi/notificationservice/util/DBConnection.java`.
2. Update the `USERNAME` and `PASSWORD` constants to match your local MySQL environment:
   ```java
   private static final String USERNAME = "root"; 
   private static final String PASSWORD = "your_password_here"; // Update this if needed


### 3. Server Configuration (Port 8089)

To avoid conflicts with the other 9 groups, this service **must** run on **Port 8089**.

* **If using IntelliJ IDEA:** Edit your Tomcat Run Configuration and change the HTTP port to `8089`.
* **If using standalone Tomcat:** Open `[TOMCAT_HOME]/conf/server.xml`, locate the HTTP Connector `<Connector port="8080"...>`, and change `8080` to `8089`.

### 4. Build and Deploy

Build the project using Maven wrapper to generate the WAR file:

```bash
# On Linux/macOS
./mvnw clean package

# On Windows
mvnw.cmd clean package

```

Deploy the resulting `notificationservice-1.0-SNAPSHOT.war` (located in the `target` folder) to your Tomcat server.
Once running, access the web UI at: `http://localhost:8089/notificationservice/`

---

## 🔗 Microservice Integration Contract

As part of the distributed architecture, this service interacts with other groups' microservices over HTTP.

### 1. Consuming External Endpoints (Validation)

Before a notification is created, this service communicates with **Group 2 (Student Service)** to validate if the `userId` exists.

* **Target:** `GET https://klgyt-154-68-72-191.a.free.pinggy.link/Group_II_Student_Service_war_exploded/{userId}`
* **Implementation:** Handled via standard `java.net.HttpURLConnection` inside `NotificationService.java`.

### 2. Triggering Notifications (For Groups 5, 7, and 8)

Other services (like Enrollment, Result, and Payment) must trigger this service when an event occurs in their system.

**Tell other groups to send an HTTP POST request to this endpoint:**

* **URL:** `POST http://localhost:8089/notificationservice/api/notifications`
* **Headers:** `Content-Type: application/json`
* **Body Format:**
```json
{
  "userId": 1,
  "title": "Payment Confirmed",
  "message": "Your payment of 500,000 RWF was successfully processed.",
  "type": "PAYMENT"
}

```



*(Valid `type` values: `GENERAL`, `PAYMENT`, `ENROLLMENT`, `RESULT`, `ATTENDANCE`, `SYSTEM`)*

---

## 📡 REST API Reference

The service exposes the following JSON endpoints via the `NotificationAPIController` for cross-service communication.

| Method | Endpoint | Description |
| --- | --- | --- |
| **GET** | `/api/notifications` | Fetch all system notifications |
| **GET** | `/api/notifications?userId={id}` | Fetch all notifications for a specific user |
| **GET** | `/api/notifications?userId={id}&unread=true` | Fetch only unread notifications for a user |
| **GET** | `/api/notifications/count?userId={id}` | Get the total unread count for a user |
| **GET** | `/api/notifications/{id}` | Fetch a specific notification by its ID |
| **POST** | `/api/notifications` | Create a new notification (See trigger section) |
| **PUT** | `/api/notifications/{id}/read` | Mark a specific notification as read |
| **DELETE** | `/api/notifications/{id}` | Delete a specific notification |

---

## 🖥️ Web User Interface (JSP)

The service includes a fully functional web interface for administrators and users, driven by `NotificationController.java`:

* **Dashboard (`index.jsp`):** The landing page with quick links to APIs and views.
* **All Notifications (`all-notifications.jsp`):** Admin view displaying all notifications across the system.
* **User Inbox (`user-notifications.jsp`):** A specific user's inbox showing read/unread status and counts.
* **Create Notification (`create-notification.jsp`):** A form for admins to manually dispatch alerts to users.
* **View Notification (`view-notification.jsp`):** Detailed view of a single notification.

```

```