package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Marks operations.
 * Handles inserting and retrieving marks/grades from the database.
 */
public class MarksDAO {

    /**
     * Inserts marks for a student in a specific subject.
     *
     * @param studentId the student's ID
     * @param subject   the subject name
     * @param marks     the marks obtained
     * @return true if the record was inserted successfully, false otherwise
     */
    public boolean insertMarks(String studentId, String subject, int marks) {
        String sql = "INSERT INTO marks (student_id, subject, marks) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, subject);
            ps.setInt(3, marks);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error inserting marks: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all marks for a specific student.
     * Each record is returned as a String array: [subject, marks].
     *
     * @param studentId the student's ID
     * @return a list of String arrays with subject and marks
     */
    public List<String[]> getMarksByStudent(String studentId) {
        String sql = "SELECT subject, marks FROM marks WHERE student_id = ? ORDER BY subject";
        List<String[]> records = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[2];
                    row[0] = rs.getString("subject");
                    row[1] = String.valueOf(rs.getInt("marks"));
                    records.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving marks: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }
}
