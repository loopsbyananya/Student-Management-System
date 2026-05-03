package dao;

import model.Announcement;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Announcement operations.
 */
public class AnnouncementDAO {

    /** Posts a new announcement. */
    public boolean addAnnouncement(Announcement a) {
        String sql = "INSERT INTO announcements (title, message, teacher_id, subject_id, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getMessage());
            ps.setString(3, a.getTeacherId());
            if (a.getSubjectId() != null && !a.getSubjectId().isEmpty()) {
                ps.setString(4, a.getSubjectId());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            ps.setString(5, a.getDate());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding announcement: " + e.getMessage());
            return false;
        }
    }

    /** Gets all announcements (most recent first). */
    public List<Announcement> getAllAnnouncements() {
        String sql = "SELECT a.announcement_id, a.title, a.message, a.teacher_id, " +
                     "a.date, t.name AS teacher_name FROM announcements a " +
                     "LEFT JOIN teachers t ON a.teacher_id = t.teacher_id " +
                     "ORDER BY a.date DESC";
        List<Announcement> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Announcement an = new Announcement();
                an.setAnnouncementId(rs.getInt("announcement_id"));
                an.setTitle(rs.getString("title"));
                an.setMessage(rs.getString("message"));
                an.setTeacherId(rs.getString("teacher_id"));
                an.setTeacherName(rs.getString("teacher_name"));
                an.setDate(rs.getString("date"));
                list.add(an);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving announcements: " + e.getMessage());
        }
        return list;
    }

    /** Gets announcements for subjects a student is enrolled in. */
    public List<Announcement> getAnnouncementsByStudent(String studentId) {
        String sql = "SELECT a.announcement_id, a.title, a.message, a.teacher_id, a.subject_id, " +
                     "a.date, t.name AS teacher_name FROM announcements a " +
                     "LEFT JOIN teachers t ON a.teacher_id = t.teacher_id " +
                     "JOIN student_subject ss ON a.subject_id = ss.subject_id " +
                     "WHERE ss.student_id = ? " +
                     "ORDER BY a.date DESC";
        List<Announcement> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Announcement an = new Announcement();
                    an.setAnnouncementId(rs.getInt("announcement_id"));
                    an.setTitle(rs.getString("title"));
                    an.setMessage(rs.getString("message"));
                    an.setTeacherId(rs.getString("teacher_id"));
                    an.setSubjectId(rs.getString("subject_id"));
                    an.setTeacherName(rs.getString("teacher_name"));
                    an.setDate(rs.getString("date"));
                    list.add(an);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student announcements: " + e.getMessage());
        }
        return list;
    }

    /** Gets announcements for a specific subject. */
    public List<Announcement> getAnnouncementsBySubject(String subjectId) {
        String sql = "SELECT a.announcement_id, a.title, a.message, a.teacher_id, a.subject_id, " +
                     "a.date, t.name AS teacher_name FROM announcements a " +
                     "LEFT JOIN teachers t ON a.teacher_id = t.teacher_id " +
                     "WHERE a.subject_id = ? " +
                     "ORDER BY a.date DESC";
        List<Announcement> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Announcement an = new Announcement();
                    an.setAnnouncementId(rs.getInt("announcement_id"));
                    an.setTitle(rs.getString("title"));
                    an.setMessage(rs.getString("message"));
                    an.setTeacherId(rs.getString("teacher_id"));
                    an.setSubjectId(rs.getString("subject_id"));
                    an.setTeacherName(rs.getString("teacher_name"));
                    an.setDate(rs.getString("date"));
                    list.add(an);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving subject announcements: " + e.getMessage());
        }
        return list;
    }
}
