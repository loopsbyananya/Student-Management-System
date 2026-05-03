package dao;

import model.Assignment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Assignment operations.
 */
public class AssignmentDAO {

    /** Creates a new assignment. */
    public boolean addAssignment(Assignment a) {
        String sql = "INSERT INTO assignments (title, description, subject_id, teacher_id, due_date, min_files) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getDescription());
            ps.setString(3, a.getSubjectId());
            ps.setString(4, a.getTeacherId());
            ps.setString(5, a.getDueDate());
            ps.setInt(6, a.getMinFiles());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding assignment: " + e.getMessage());
            return false;
        }
    }

    /** Gets all assignments (for admin/teacher views). */
    public List<Assignment> getAllAssignments() {
        String sql = "SELECT assignment_id, title, description, subject_id, teacher_id, due_date, min_files " +
                     "FROM assignments ORDER BY due_date DESC";
        List<Assignment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Assignment a = new Assignment();
                a.setAssignmentId(rs.getInt("assignment_id"));
                a.setTitle(rs.getString("title"));
                a.setDescription(rs.getString("description"));
                a.setSubjectId(rs.getString("subject_id"));
                a.setTeacherId(rs.getString("teacher_id"));
                a.setDueDate(rs.getString("due_date"));
                a.setMinFiles(rs.getInt("min_files"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assignments: " + e.getMessage());
        }
        return list;
    }

    /** Gets assignments for subjects a student is enrolled in. */
    public List<Assignment> getAssignmentsForStudent(String studentId) {
        String sql = "SELECT a.assignment_id, a.title, a.description, a.subject_id, " +
                     "a.teacher_id, a.due_date, a.min_files FROM assignments a " +
                     "JOIN student_subject ss ON a.subject_id = ss.subject_id " +
                     "WHERE ss.student_id = ? ORDER BY a.due_date DESC";
        List<Assignment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = new Assignment();
                    a.setAssignmentId(rs.getInt("assignment_id"));
                    a.setTitle(rs.getString("title"));
                    a.setDescription(rs.getString("description"));
                    a.setSubjectId(rs.getString("subject_id"));
                    a.setTeacherId(rs.getString("teacher_id"));
                    a.setDueDate(rs.getString("due_date"));
                    a.setMinFiles(rs.getInt("min_files"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student assignments: " + e.getMessage());
        }
        return list;
    }

    /** Gets count of all assignments (for reports). */
    public int getAssignmentCount() {
        String sql = "SELECT COUNT(*) FROM assignments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error counting assignments: " + e.getMessage());
        }
        return 0;
    }

    /** Gets assignments for a specific subject. */
    public List<Assignment> getAssignmentsBySubject(String subjectId) {
        String sql = "SELECT assignment_id, title, description, subject_id, teacher_id, due_date, min_files " +
                     "FROM assignments WHERE subject_id = ? ORDER BY due_date DESC";
        List<Assignment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Assignment a = new Assignment();
                    a.setAssignmentId(rs.getInt("assignment_id"));
                    a.setTitle(rs.getString("title"));
                    a.setDescription(rs.getString("description"));
                    a.setSubjectId(rs.getString("subject_id"));
                    a.setTeacherId(rs.getString("teacher_id"));
                    a.setDueDate(rs.getString("due_date"));
                    a.setMinFiles(rs.getInt("min_files"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving subject assignments: " + e.getMessage());
        }
        return list;
    }
}
