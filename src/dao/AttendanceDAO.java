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
     * Marks attendance for a student on a given date.
     *
     * @param studentId the student's ID
     * @param date      the date in YYYY-MM-DD format
     * @param status    "Present" or "Absent"
     * @return true if the record was inserted successfully, false otherwise
     */
    public boolean markAttendance(String studentId, String date, String status) {
        String sql = "INSERT INTO attendance (student_id, date, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.setString(2, date);
            ps.setString(3, status);
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
     * Each record is returned as a String array: [date, status].
     *
     * @param studentId the student's ID
     * @return a list of String arrays with date and status
     */
    public List<String[]> getAttendanceByStudent(String studentId) {
        String sql = "SELECT date, status FROM attendance WHERE student_id = ? ORDER BY date DESC";
        List<String[]> records = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] row = new String[2];
                    row[0] = rs.getString("date");
                    row[1] = rs.getString("status");
                    records.add(row);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving attendance: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Retrieves all attendance records (for admin views).
     * Each record: [student_id, student_name, date, status].
     */
    public List<String[]> getAllAttendance() {
        String sql = "SELECT a.student_id, s.name, a.date, a.status " +
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
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all attendance: " + e.getMessage());
        }
        return records;
    }
}
