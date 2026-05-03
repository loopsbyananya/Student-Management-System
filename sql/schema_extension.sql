-- ============================================================
-- Student Management System — Schema Extension
-- Run this script AFTER schema.sql to add new tables.
-- ============================================================

USE student_management_system;

-- ============================================================
-- SUBJECTS table — stores available subjects with teacher mapping
-- ============================================================
CREATE TABLE IF NOT EXISTS subjects (
    subject_id VARCHAR(20) PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    teacher_id VARCHAR(20),
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE SET NULL
);

-- ============================================================
-- STUDENT_SUBJECT table — maps students to subjects (enrollment)
-- ============================================================
CREATE TABLE IF NOT EXISTS student_subject (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(20) NOT NULL,
    subject_id VARCHAR(20) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, subject_id)
);

-- ============================================================
-- ASSIGNMENTS table — assignments created by teachers
-- ============================================================
CREATE TABLE IF NOT EXISTS assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    subject_id VARCHAR(20) NOT NULL,
    teacher_id VARCHAR(20) NOT NULL,
    due_date DATE NOT NULL,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE
);

-- ============================================================
-- SUBMISSIONS table — student assignment submissions
-- ============================================================
CREATE TABLE IF NOT EXISTS submissions (
    submission_id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT NOT NULL,
    student_id VARCHAR(20) NOT NULL,
    submission_text TEXT,
    marks INT DEFAULT NULL,
    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_submission (assignment_id, student_id)
);

-- ============================================================
-- ANNOUNCEMENTS table — teacher announcements (with subject)
-- ============================================================
CREATE TABLE IF NOT EXISTS announcements (
    announcement_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    teacher_id VARCHAR(20) NOT NULL,
    subject_id VARCHAR(20),
    date DATE NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE SET NULL
);

-- ============================================================
-- SEED DATA — Computer Science Subjects
-- ============================================================
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('DSA', 'Data Structures and Algorithms', 'T001');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('COA', 'Computer Organization and Architecture', 'T002');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('DAA', 'Design and Analysis of Algorithms', 'T003');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('LINALG', 'Linear Algebra', 'T004');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('OOP', 'Object Oriented Programming', 'T002');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('CN', 'Computer Networks', 'T005');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('DBMS', 'Database Management Systems', 'T003');
INSERT IGNORE INTO subjects (subject_id, subject_name, teacher_id) VALUES ('OS', 'Operating Systems', 'T001');

-- ============================================================
-- SEED DATA — Student-Subject Enrollment
-- ============================================================
-- Ananya Singh (S001): DSA, OOP, DBMS
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S001', 'DSA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S001', 'OOP');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S001', 'DBMS');

-- Ranjan Yadav (S002): COA, OS, CN
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S002', 'COA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S002', 'OS');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S002', 'CN');

-- Piyush Singh Jimiwal (S003): DAA, LINALG, DBMS, OOP
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S003', 'DAA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S003', 'LINALG');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S003', 'DBMS');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S003', 'OOP');

-- Daksh Mishra (S004): DSA, COA, DAA, OS
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S004', 'DSA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S004', 'COA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S004', 'DAA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S004', 'OS');

-- Sneha Patel (S005): OOP, CN, DBMS, LINALG
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S005', 'OOP');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S005', 'CN');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S005', 'DBMS');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S005', 'LINALG');

-- Karan Gupta (S006): DSA, DAA, OS, CN
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S006', 'DSA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S006', 'DAA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S006', 'OS');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S006', 'CN');

-- Riya Kapoor (S007): COA, OOP, LINALG
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S007', 'COA');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S007', 'OOP');
INSERT IGNORE INTO student_subject (student_id, subject_id) VALUES ('S007', 'LINALG');

-- ============================================================
-- SEED DATA — Assignments (one per subject)
-- ============================================================
INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('Implement Stack and Queue', 'Implement Stack and Queue using linked list in C/Java. Include push, pop, enqueue, dequeue operations.', 'DSA', 'T001', '2026-05-15');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('CPU Pipeline Design', 'Design a 5-stage instruction pipeline and explain hazard handling mechanisms.', 'COA', 'T002', '2026-05-18');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('Greedy vs Dynamic Programming', 'Compare Greedy and DP approaches for the Knapsack problem. Provide time complexity analysis.', 'DAA', 'T003', '2026-05-20');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('Matrix Transformations', 'Solve problems on eigenvalues, eigenvectors, and linear transformations from Chapter 5.', 'LINALG', 'T004', '2026-05-16');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('Design Patterns in Java', 'Implement Singleton, Factory, and Observer design patterns with real-world examples.', 'OOP', 'T002', '2026-05-22');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('TCP/IP Socket Programming', 'Build a simple client-server chat application using TCP sockets in Java.', 'CN', 'T005', '2026-05-19');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('ER Diagram and Normalization', 'Design an ER diagram for a library management system and normalize to 3NF.', 'DBMS', 'T003', '2026-05-17');

INSERT IGNORE INTO assignments (title, description, subject_id, teacher_id, due_date)
    VALUES ('Process Scheduling Simulation', 'Simulate FCFS, SJF, and Round Robin scheduling algorithms. Compare turnaround times.', 'OS', 'T001', '2026-05-21');

-- ============================================================
-- SEED DATA — Announcements (with subject_id for filtering)
-- ============================================================
INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('DSA Mid-Term Schedule', 'DSA mid-term exam is scheduled for May 25th. Syllabus covers Arrays, Linked Lists, Stacks, Queues, and Trees.', 'T001', 'DSA', '2026-05-01');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('OOP Lab Submission Deadline', 'All OOP lab submissions must be completed by May 20th. Late submissions will not be accepted.', 'T002', 'OOP', '2026-05-02');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('DBMS Project Teams', 'DBMS project teams have been finalized. Check the notice board for your team assignments.', 'T003', 'DBMS', '2026-05-01');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('COA Extra Class', 'An extra class on Cache Memory and Virtual Memory will be held on Saturday, May 10th at 10 AM.', 'T002', 'COA', '2026-05-03');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('Computer Networks Lab', 'CN lab will be held in Lab 3 instead of Lab 1 starting next week.', 'T005', 'CN', '2026-05-02');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('Linear Algebra Tutorial', 'Additional tutorial session for Linear Algebra will be conducted every Wednesday at 3 PM.', 'T004', 'LINALG', '2026-05-01');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('OS Assignment Clarification', 'For the OS scheduling assignment, use arrival time 0 for all processes in FCFS simulation.', 'T001', 'OS', '2026-05-03');

INSERT IGNORE INTO announcements (title, message, teacher_id, subject_id, date)
    VALUES ('DAA Quiz Next Week', 'A surprise quiz on Graph Algorithms will be held next week. Prepare BFS, DFS, and Dijkstra.', 'T003', 'DAA', '2026-05-02');

-- ============================================================
-- SEED DATA — Sample submissions
-- ============================================================
-- Ananya Singh submitted DSA assignment (assignment_id = 1)
INSERT IGNORE INTO submissions (assignment_id, student_id, submission_text, marks)
    VALUES (1, 'S001', 'Implemented Stack and Queue using singly linked list in Java with all operations.', 85);

-- Ananya Singh submitted OOP assignment (assignment_id = 5)
INSERT IGNORE INTO submissions (assignment_id, student_id, submission_text)
    VALUES (5, 'S001', 'Implemented Singleton with thread-safety, Factory for Shape creation, and Observer for event system.');

-- Ranjan Yadav submitted COA assignment (assignment_id = 2)
INSERT IGNORE INTO submissions (assignment_id, student_id, submission_text, marks)
    VALUES (2, 'S002', 'Designed 5-stage pipeline with data forwarding and branch prediction for hazard resolution.', 72);

-- Piyush Singh Jimiwal submitted DAA assignment (assignment_id = 3)
INSERT IGNORE INTO submissions (assignment_id, student_id, submission_text)
    VALUES (3, 'S003', 'Compared 0/1 Knapsack using Greedy and DP. DP gives optimal, Greedy is approximate. Analysis included.');
