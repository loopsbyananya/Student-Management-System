package dao;

import model.Submission;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Submission operations.
 */
public class SubmissionDAO {

    /** Submits an assignment (student side). */
    public boolean submitAssignment(Submission s) {
        String sql = "INSERT INTO submissions (assignment_id, student_id, submission_text, submission_date) " +
                     "VALUES (?, ?, ?, CURDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getAssignmentId());
            ps.setString(2, s.getStudentId());
            ps.setString(3, s.getSubmissionText());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error submitting assignment: " + e.getMessage());
            return false;
        }
    }

    /** Gets all submissions by a student. */
    public List<String[]> getSubmissionsByStudent(String studentId) {
        String sql = "SELECT a.title, a.subject_id, s.submission_text, " +
                     "COALESCE(CAST(s.marks AS CHAR), 'Not Graded') AS marks " +
                     "FROM submissions s JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "WHERE s.student_id = ? ORDER BY a.due_date DESC";
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        rs.getString("title"),
                        rs.getString("subject_id"),
                        rs.getString("submission_text"),
                        rs.getString("marks")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving submissions: " + e.getMessage());
        }
        return list;
    }

    /** Gets all submissions for a specific assignment. */
    public List<String[]> getSubmissionsByAssignment(int assignmentId) {
        String sql = "SELECT s.student_id, st.name, s.submission_text, " +
                     "COALESCE(CAST(s.marks AS CHAR), 'Not Graded') AS marks " +
                     "FROM submissions s JOIN students st ON s.student_id = st.student_id " +
                     "WHERE s.assignment_id = ? ORDER BY st.name";
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("submission_text"),
                        rs.getString("marks")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assignment submissions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Gets assignment status for a student — shows every assignment for enrolled subjects
     * with submission status (Submitted / Not Submitted) and marks if graded.
     * Returns: [assignment_id, title, subject_id, due_date, status, marks]
     */
    public List<String[]> getAssignmentStatusForStudent(String studentId) {
        String sql = "SELECT a.assignment_id, a.title, a.subject_id, a.due_date, " +
                     "CASE WHEN sub.submission_id IS NOT NULL THEN 'Submitted' ELSE 'Not Submitted' END AS status, " +
                     "COALESCE(CAST(sub.marks AS CHAR), '-') AS marks, a.description, COALESCE(a.min_files, 1) as min_files " +
                     "FROM assignments a " +
                     "JOIN student_subject ss ON a.subject_id = ss.subject_id " +
                     "LEFT JOIN submissions sub ON a.assignment_id = sub.assignment_id AND sub.student_id = ss.student_id " +
                     "WHERE ss.student_id = ? ORDER BY a.due_date DESC";
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        String.valueOf(rs.getInt("assignment_id")),
                        rs.getString("title"),
                        rs.getString("subject_id"),
                        rs.getString("due_date"),
                        rs.getString("status"),
                        rs.getString("marks"),
                        rs.getString("description"),
                        String.valueOf(rs.getInt("min_files"))
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assignment status: " + e.getMessage());
        }
        return list;
    }

    /** Updates the marks for a specific submission. */
    public boolean updateMarks(int submissionId, int marks) {
        String sql = "UPDATE submissions SET marks = ? WHERE submission_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, marks);
            ps.setInt(2, submissionId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating marks: " + e.getMessage());
            return false;
        }
    }

    /** Gets submission details for grading. */
    public List<String[]> getSubmissionsByAssignmentWithId(int assignmentId) {
        String sql = "SELECT s.submission_id, s.student_id, st.name, s.submission_text, " +
                     "COALESCE(CAST(s.marks AS CHAR), 'Not Graded') AS marks " +
                     "FROM submissions s JOIN students st ON s.student_id = st.student_id " +
                     "WHERE s.assignment_id = ? ORDER BY st.name";
        List<String[]> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new String[]{
                        String.valueOf(rs.getInt("submission_id")),
                        rs.getString("student_id"),
                        rs.getString("name"),
                        rs.getString("submission_text"),
                        rs.getString("marks")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assignment submissions: " + e.getMessage());
        }
        return list;
    }
}
