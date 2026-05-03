# Student Management System

A Java OOP-based Student Management System with Swing GUI, MySQL database, and JDBC connectivity.

## Prerequisites

- **Java JDK 8+** installed
- **MySQL Server** running on `localhost:3306`
- **MySQL Connector/J** (JDBC driver) — download from [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/)

## Setup Instructions

### 1. Set Up the Database

Open MySQL Workbench and run the SQL script:

```
sql/schema.sql
```

This creates the `student_management_system` database with all tables and sample data.

### 2. Configure Database Credentials

Edit `src/util/DBConnection.java` and set your MySQL credentials:

```java
private static final String USER = "root";
private static final String PASSWORD = "";  // <-- your MySQL password
```

### 3. Add MySQL JDBC Driver

Download `mysql-connector-j-8.x.x.jar` and place it in the `lib/` folder.

### 4. Compile

```bash
# From the project root directory
javac -cp "lib/*" -d out src/model/*.java src/util/*.java src/dao/*.java src/ui/*.java src/Main.java
```

### 5. Run

```bash
java -cp "out:lib/*" Main
```

On Windows, use `;` instead of `:` as the classpath separator:

```bash
java -cp "out;lib/*" Main
```

## Test Accounts

| Role    | User ID | Password     |
|---------|---------|-------------|
| Admin   | admin1  | admin123    |
| Teacher | T001    | teacher123  |
| Student | S001    | student123  |

## Project Structure

```
src/
├── Main.java              # Entry point
├── model/                 # Data models (OOP hierarchy)
│   ├── User.java          # Base class
│   ├── Student.java       # extends User
│   ├── Teacher.java       # extends User
│   └── Admin.java         # extends User
├── dao/                   # Data Access Objects
│   ├── UserDAO.java       # Authentication
│   ├── StudentDAO.java    # Student CRUD
│   ├── TeacherDAO.java    # Teacher CRUD
│   ├── AttendanceDAO.java # Attendance operations
│   └── MarksDAO.java      # Marks operations
├── ui/                    # Swing GUI
│   ├── LoginFrame.java    # Login screen
│   ├── AdminDashboard.java
│   ├── TeacherDashboard.java
│   └── StudentDashboard.java
└── util/
    └── DBConnection.java  # JDBC connection helper
```
