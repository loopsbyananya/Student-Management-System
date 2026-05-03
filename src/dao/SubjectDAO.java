package dao;

import model.Subject;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Subject operations — manage subjects and student-subject enrollment.
 */
public class SubjectDAO {

    /** Adds a new subject. */
    public boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects (subject_id, subject_name) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subject.getSubjectId());
            ps.setString(2, subject.getSubjectName());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            return false;
        }
    }

    /** Retrieves all subjects. */
    public List<Subject> getAllSubjects() {
        String sql = "SELECT subject_id, subject_name FROM subjects ORDER BY subject_name";
        List<Subject> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Subject s = new Subject();
                s.setSubjectId(rs.getString("subject_id"));
                s.setSubjectName(rs.getString("subject_name"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving subjects: " + e.getMessage());
        }
        return list;
    }

    /** Assigns a student to a subject. */
    public boolean assignStudentToSubject(String studentId, String subjectId) {
        String sql = "INSERT INTO student_subject (student_id, subject_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, subjectId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error assigning student to subject: " + e.getMessage());
            return false;
        }
    }

    /** Gets all subjects a student is enrolled in. */
    public List<Subject> getSubjectsByStudent(String studentId) {
        String sql = "SELECT s.subject_id, s.subject_name FROM subjects s " +
                     "JOIN student_subject ss ON s.subject_id = ss.subject_id " +
                     "WHERE ss.student_id = ? ORDER BY s.subject_name";
        List<Subject> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject s = new Subject();
                    s.setSubjectId(rs.getString("subject_id"));
                    s.setSubjectName(rs.getString("subject_name"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student subjects: " + e.getMessage());
        }
        return list;
    }

    /** Gets all subjects mapped to a specific teacher. */
    public List<Subject> getSubjectsByTeacher(String teacherId) {
        String sql = "SELECT subject_id, subject_name FROM subjects WHERE teacher_id = ? ORDER BY subject_name";
        List<Subject> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Subject s = new Subject();
                    s.setSubjectId(rs.getString("subject_id"));
                    s.setSubjectName(rs.getString("subject_name"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving subjects for teacher: " + e.getMessage());
        }
        return list;
    }
}
