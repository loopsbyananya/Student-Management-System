package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Attendance operations.
 * Handles marking and retrieving attendance records from the database.
 */
public class AttendanceDAO {

    /**
     * Marks attendance for a student on a given date for a specific subject.
     *
     * @param studentId the student's ID
     * @param date      the date in YYYY-MM-DD format
     * @param status    "Present" or "Absent"
     * @param subject   the subject name
     * @return true if the record was inserted successfully, false otherwise
     */
    public boolean markAttendance(String studentId, String date, String status, String subject) {
        String sql = "INSERT INTO attendance (student_id, date, status, subject) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, date);
            ps.setString(3, status);
            ps.setString(4, subject);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all attendance records for a specific student.
     * Each record: [date, status, subject].
     */
    public List<String[]> getAttendanceByStudent(String studentId) {
        String sql = "SELECT date, status, subject FROM attendance WHERE student_id = ? ORDER BY date DESC";
        List<String[]> records = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(new String[]{
                        rs.getString("date"),
                        rs.getString("subject"),
                        rs.getString("status")
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving attendance: " + e.getMessage());
        }

        return records;
    }

    /**
     * Retrieves all attendance records (for admin views).
     * Each record: [student_id, student_name, date, status, subject].
     */
    public List<String[]> getAllAttendance() {
        String sql = "SELECT a.student_id, s.name, a.date, a.status, a.subject " +
                     "FROM attendance a JOIN students s ON a.student_id = s.student_id " +
                     "ORDER BY a.date DESC, s.name";
        List<String[]> records = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                records.add(new String[]{
                    rs.getString("student_id"),
                    rs.getString("name"),
                    rs.getString("date"),
                    rs.getString("subject"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all attendance: " + e.getMessage());
        }
        return records;
    }
}
