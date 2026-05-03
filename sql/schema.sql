-- ============================================================
-- Student Management System — Database Schema
-- Run this script in MySQL Workbench to set up the database.
-- ============================================================

-- Create the database
DROP DATABASE IF EXISTS student_management_system;
CREATE DATABASE student_management_system;
USE student_management_system;

-- ============================================================
-- USERS table — stores login credentials and roles
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'TEACHER', 'STUDENT') NOT NULL
);

-- ============================================================
-- STUDENTS table — stores student information
-- ============================================================
CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    class VARCHAR(50) NOT NULL
);

-- ============================================================
-- TEACHERS table — stores teacher information
-- ============================================================
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL
);

-- ============================================================
-- ATTENDANCE table — tracks daily attendance for students
-- ============================================================
CREATE TABLE IF NOT EXISTS attendance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    date DATE NOT NULL,
    status ENUM('Present', 'Absent') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- ============================================================
-- MARKS table — stores marks/grades for students per subject
-- ============================================================
CREATE TABLE IF NOT EXISTS marks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    marks INT NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
);

-- ============================================================
-- SEED DATA — Admin account
-- ============================================================
INSERT IGNORE INTO users (id, name, password, role) VALUES ('admin1', 'Admin User', 'admin123', 'ADMIN');

-- ============================================================
-- SEED DATA — Teachers (Indian names)
-- ============================================================
INSERT IGNORE INTO users (id, name, password, role) VALUES ('T001', 'Dr. Rajesh Kumar', 'teacher123', 'TEACHER');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('T002', 'Prof. Neha Sharma', 'teacher123', 'TEACHER');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('T003', 'Dr. Amit Verma', 'teacher123', 'TEACHER');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('T004', 'Prof. Pooja Singh', 'teacher123', 'TEACHER');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('T005', 'Dr. Vivek Gupta', 'teacher123', 'TEACHER');

INSERT IGNORE INTO teachers (teacher_id, name, subject) VALUES ('T001', 'Dr. Rajesh Kumar', 'DSA, Operating Systems');
INSERT IGNORE INTO teachers (teacher_id, name, subject) VALUES ('T002', 'Prof. Neha Sharma', 'COA, OOP');
INSERT IGNORE INTO teachers (teacher_id, name, subject) VALUES ('T003', 'Dr. Amit Verma', 'DAA, DBMS');
INSERT IGNORE INTO teachers (teacher_id, name, subject) VALUES ('T004', 'Prof. Pooja Singh', 'Linear Algebra');
INSERT IGNORE INTO teachers (teacher_id, name, subject) VALUES ('T005', 'Dr. Vivek Gupta', 'Computer Networks');

-- ============================================================
-- SEED DATA — Students (Indian names)
-- ============================================================
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S001', 'Ananya Singh', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S002', 'Ranjan Yadav', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S003', 'Piyush Singh Jimiwal', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S004', 'Daksh Mishra', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S005', 'Sneha Patel', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S006', 'Karan Gupta', 'student123', 'STUDENT');
INSERT IGNORE INTO users (id, name, password, role) VALUES ('S007', 'Riya Kapoor', 'student123', 'STUDENT');

INSERT IGNORE INTO students (student_id, name, class) VALUES ('S001', 'Ananya Singh', 'CS-A');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S002', 'Ranjan Yadav', 'CS-A');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S003', 'Piyush Singh Jimiwal', 'CS-B');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S004', 'Daksh Mishra', 'CS-A');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S005', 'Sneha Patel', 'CS-B');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S006', 'Karan Gupta', 'CS-A');
INSERT IGNORE INTO students (student_id, name, class) VALUES ('S007', 'Riya Kapoor', 'CS-B');

-- ============================================================
-- SEED DATA — Attendance records
-- ============================================================
-- Ananya Singh
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S001', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S001', '2026-04-29', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S001', '2026-04-30', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S001', '2026-05-01', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S001', '2026-05-02', 'Present');

-- Ranjan Yadav
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S002', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S002', '2026-04-29', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S002', '2026-04-30', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S002', '2026-05-01', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S002', '2026-05-02', 'Present');

-- Piyush Singh Jimiwal
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S003', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S003', '2026-04-29', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S003', '2026-04-30', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S003', '2026-05-01', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S003', '2026-05-02', 'Present');

-- Daksh Mishra
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S004', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S004', '2026-04-29', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S004', '2026-04-30', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S004', '2026-05-01', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S004', '2026-05-02', 'Present');

-- Sneha Patel
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S005', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S005', '2026-04-29', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S005', '2026-04-30', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S005', '2026-05-01', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S005', '2026-05-02', 'Absent');

-- Karan Gupta
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S006', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S006', '2026-04-29', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S006', '2026-04-30', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S006', '2026-05-01', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S006', '2026-05-02', 'Present');

-- Riya Kapoor
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S007', '2026-04-28', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S007', '2026-04-29', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S007', '2026-04-30', 'Present');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S007', '2026-05-01', 'Absent');
INSERT IGNORE INTO attendance (student_id, date, status) VALUES ('S007', '2026-05-02', 'Present');

-- ============================================================
-- SEED DATA — Marks records
-- ============================================================
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S001', 'DSA', 88);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S001', 'OOP', 92);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S001', 'DBMS', 78);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S002', 'COA', 72);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S002', 'OS', 65);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S002', 'CN', 81);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S003', 'DAA', 90);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S003', 'Linear Algebra', 85);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S003', 'DBMS', 76);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S003', 'OOP', 88);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S004', 'DSA', 55);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S004', 'COA', 62);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S004', 'DAA', 48);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S004', 'OS', 70);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S005', 'OOP', 95);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S005', 'CN', 87);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S005', 'DBMS', 91);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S005', 'Linear Algebra', 82);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S006', 'DSA', 74);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S006', 'DAA', 68);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S006', 'OS', 77);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S006', 'CN', 83);

INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S007', 'COA', 86);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S007', 'OOP', 93);
INSERT IGNORE INTO marks (student_id, subject, marks) VALUES ('S007', 'Linear Algebra', 79);
