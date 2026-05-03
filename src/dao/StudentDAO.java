package dao;

import model.Student;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations.
 * Handles adding and retrieving student records from the database.
 */
public class StudentDAO {

    /**
     * Adds a new student to both the 'students' table and the 'users' table.
     * The user entry is needed so the student can log in.
     *
     * @param student the Student object to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean addStudent(Student student) {
        String insertUser = "INSERT INTO users (id, name, password, role) VALUES (?, ?, ?, 'STUDENT')";
        String insertStudent = "INSERT INTO students (student_id, name, class) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into users table
            try (PreparedStatement ps1 = conn.prepareStatement(insertUser)) {
                ps1.setString(1, student.getId());
                ps1.setString(2, student.getName());
                ps1.setString(3, student.getPassword());
                ps1.executeUpdate();
            }

            // Insert into students table
            try (PreparedStatement ps2 = conn.prepareStatement(insertStudent)) {
                ps2.setString(1, student.getId());
                ps2.setString(2, student.getName());
                ps2.setString(3, student.getStudentClass());
                ps2.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            // Roll back on error to keep data consistent
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Retrieves all students from the database.
     *
     * @return a list of Student objects
     */
    public List<Student> getAllStudents() {
        String sql = "SELECT student_id, name, class FROM students";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getString("student_id"));
                s.setName(rs.getString("name"));
                s.setStudentClass(rs.getString("class"));
                students.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Removes a student from both 'students' and 'users' tables.
     * Uses CASCADE so attendance/marks/submissions are also removed.
     *
     * @param studentId the student's ID
     * @return true if deletion was successful
     */
    public boolean deleteStudent(String studentId) {
        // Delete from students first (cascade handles attendance/marks),
        // then delete from users
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM students WHERE student_id = ?")) {
                ps1.setString(1, studentId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM users WHERE id = ?")) {
                ps2.setString(1, studentId);
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /** Returns total number of students. */
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting students: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Searches students by ID or name (partial match).
     *
     * @param query search term (matched against both student_id and name)
     * @return list of matching students
     */
    public List<Student> searchStudents(String query) {
        String sql = "SELECT student_id, name, class FROM students " +
                     "WHERE student_id LIKE ? OR name LIKE ? ORDER BY name";
        List<Student> students = new ArrayList<>();
        String pattern = "%" + query + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getString("student_id"));
                    s.setName(rs.getString("name"));
                    s.setStudentClass(rs.getString("class"));
                    students.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Gets all students enrolled in a specific subject.
     *
     * @param subjectId the subject ID
     * @return list of students enrolled in that subject
     */
    public List<Student> getStudentsBySubject(String subjectId) {
        String sql = "SELECT s.student_id, s.name, s.class FROM students s " +
                     "JOIN student_subject ss ON s.student_id = ss.student_id " +
                     "WHERE ss.subject_id = ? ORDER BY s.name";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getString("student_id"));
                    s.setName(rs.getString("name"));
                    s.setStudentClass(rs.getString("class"));
                    students.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by subject: " + e.getMessage());
        }
        return students;
    }

    /**
     * Gets all distinct class values in the system.
     *
     * @return list of class names
     */
    public List<String> getAllClasses() {
        String sql = "SELECT DISTINCT class FROM students ORDER BY class";
        List<String> classes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                classes.add(rs.getString("class"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting classes: " + e.getMessage());
        }
        return classes;
    }

    /**
     * Gets all students in a specific class.
     *
     * @param className the class name
     * @return list of students in that class
     */
    public List<Student> getStudentsByClass(String className) {
        String sql = "SELECT student_id, name, class FROM students WHERE class = ? ORDER BY name";
        List<Student> students = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, className);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getString("student_id"));
                    s.setName(rs.getString("name"));
                    s.setStudentClass(rs.getString("class"));
                    students.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting students by class: " + e.getMessage());
        }
        return students;
    }

    public boolean updateStudent(Student student) {
        String updateUsers = "UPDATE users SET name = ? WHERE id = ?";
        String updateStudents = "UPDATE students SET name = ?, class = ? WHERE student_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updateUsers)) {
                ps1.setString(1, student.getName());
                ps1.setString(2, student.getId());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(updateStudents)) {
                ps2.setString(1, student.getName());
                ps2.setString(2, student.getStudentClass());
                ps2.setString(3, student.getId());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public Student getStudentById(String id) {
        String sql = "SELECT student_id, name, class FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Student s = new Student();
                    s.setId(rs.getString("student_id"));
                    s.setName(rs.getString("name"));
                    s.setStudentClass(rs.getString("class"));
                    return s;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
