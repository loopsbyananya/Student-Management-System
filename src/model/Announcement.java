package model;

/**
 * Represents an announcement posted by a teacher.
 */
public class Announcement {

    private int announcementId;
    private String title;
    private String message;
    private String teacherId;
    private String subjectId;
    private String date;
    private String teacherName;

    public Announcement() { }

    public Announcement(String title, String message, String teacherId, String date) {
        this.title = title;
        this.message = message;
        this.teacherId = teacherId;
        this.date = date;
    }

    public Announcement(String title, String message, String teacherId, String subjectId, String date) {
        this.title = title;
        this.message = message;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
        this.date = date;
    }

    public int getAnnouncementId() { return announcementId; }
    public void setAnnouncementId(int announcementId) { this.announcementId = announcementId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    @Override
    public String toString() {
        return "Announcement{id=" + announcementId + ", title='" + title + "'}";
    }
}
