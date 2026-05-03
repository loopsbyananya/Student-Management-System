package dao;

import model.Teacher;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Teacher operations.
 * Handles adding and retrieving teacher records from the database.
 */
public class TeacherDAO {

    /**
     * Adds a new teacher to both the 'teachers' table and the 'users' table.
     * The user entry is needed so the teacher can log in.
     *
     * @param teacher the Teacher object to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean addTeacher(Teacher teacher) {
        String insertUser = "INSERT INTO users (id, name, password, role) VALUES (?, ?, ?, 'TEACHER')";
        String insertTeacher = "INSERT INTO teachers (teacher_id, name, subject) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Insert into users table
            try (PreparedStatement ps1 = conn.prepareStatement(insertUser)) {
                ps1.setString(1, teacher.getId());
                ps1.setString(2, teacher.getName());
                ps1.setString(3, teacher.getPassword());
                ps1.executeUpdate();
            }

            // Insert into teachers table
            try (PreparedStatement ps2 = conn.prepareStatement(insertTeacher)) {
                ps2.setString(1, teacher.getId());
                ps2.setString(2, teacher.getName());
                ps2.setString(3, teacher.getSubject());
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
            System.err.println("Error adding teacher: " + e.getMessage());
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
     * Retrieves all teachers from the database.
     *
     * @return a list of Teacher objects
     */
    public List<Teacher> getAllTeachers() {
        String sql = "SELECT teacher_id, name, subject FROM teachers";
        List<Teacher> teachers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Teacher t = new Teacher();
                t.setId(rs.getString("teacher_id"));
                t.setName(rs.getString("name"));
                t.setSubject(rs.getString("subject"));
                teachers.add(t);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving teachers: " + e.getMessage());
            e.printStackTrace();
        }

        return teachers;
    }

    /**
     * Removes a teacher from both 'teachers' and 'users' tables.
     *
     * @param teacherId the teacher's ID
     * @return true if deletion was successful
     */
    public boolean deleteTeacher(String teacherId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM teachers WHERE teacher_id = ?")) {
                ps1.setString(1, teacherId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM users WHERE id = ?")) {
                ps2.setString(1, teacherId);
                ps2.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error deleting teacher: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); }
                catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    /** Returns total number of teachers. */
    public int getTeacherCount() {
        String sql = "SELECT COUNT(*) FROM teachers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting teachers: " + e.getMessage());
        }
        return 0;
    }

    public boolean updateTeacher(Teacher teacher) {
        String updateUsers = "UPDATE users SET name = ? WHERE id = ?";
        String updateTeachers = "UPDATE teachers SET name = ?, subject = ? WHERE teacher_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(updateUsers)) {
                ps1.setString(1, teacher.getName());
                ps1.setString(2, teacher.getId());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(updateTeachers)) {
                ps2.setString(1, teacher.getName());
                ps2.setString(2, teacher.getSubject());
                ps2.setString(3, teacher.getId());
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

    public Teacher getTeacherById(String id) {
        String sql = "SELECT teacher_id, name, subject FROM teachers WHERE teacher_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Teacher t = new Teacher();
                    t.setId(rs.getString("teacher_id"));
                    t.setName(rs.getString("name"));
                    t.setSubject(rs.getString("subject"));
                    return t;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
