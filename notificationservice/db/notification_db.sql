-- ============================================================
-- SmartCampus - Notification Service
-- Group 9 | Database Script
-- HOW TO RUN:
-- Open MySQL Workbench or terminal, then run:
-- source /path/to/notification_db.sql
-- OR paste the whole content into MySQL Workbench and run.
-- ============================================================
-- 1. Create the database
CREATE DATABASE IF NOT EXISTS notification_db;
-- 2. Select it
USE notification_db;
-- 3. Drop table if already exists (safe to re-run)
DROP TABLE IF EXISTS notifications;
-- 4. Create the notifications table
CREATE TABLE notifications (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               user_id INT NOT NULL,
                               title VARCHAR(255) NOT NULL,
                               message TEXT NOT NULL,
                               type VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
                               is_read TINYINT(1) NOT NULL DEFAULT 0,
                               created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               read_at DATETIME NULL
);
-- 5. Add indexes for performance
CREATE INDEX idx_user ON notifications(user_id);
CREATE INDEX idx_is_read ON notifications(is_read);
-- 6. Sample data for testing
INSERT INTO notifications (user_id, title, message, type, is_read) VALUES
                                                                       (1, 'Welcome to SmartCampus',
                                                                        'Your account has been created successfully. Welcome!',
                                                                        'SYSTEM', 0),
                                                                       (1, 'Payment Confirmed',
                                                                        'Your tuition payment of RWF 500,000 has been received.',
                                                                        'PAYMENT', 0),
                                                                       (1, 'Enrollment Approved',
                                                                        'You have been successfully enrolled in CS101.',
                                                                        'ENROLLMENT', 0),
                                                                       (2, 'Result Published',
                                                                        'Your results for Semester 1, 2024 are now available.',
                                                                        'RESULT', 0),
                                                                       (2, 'Attendance Alert',
                                                                        'Your attendance in MATH201 is below 75%. Please attend classes.',
                                                                        'ATTENDANCE', 0),
                                                                       (3, 'Payment Due',
                                                                        'Your tuition balance of RWF 200,000 is due on 30 Jan 2025.',
                                                                        'PAYMENT', 0),
                                                                       (3, 'Welcome to SmartCampus',
                                                                        'Your account has been created. Please complete your profile.',
                                                                        'SYSTEM', 1);
-- Verify
SELECT * FROM notifications;