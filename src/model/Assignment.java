package model;

/**
 * Represents an assignment created by a teacher for a subject.
 */
public class Assignment {

    private int assignmentId;
    private String title;
    private String description;
    private String subjectId;
    private String teacherId;
    private String dueDate;
    private int minFiles = 1;

    public Assignment() { }

    public Assignment(String title, String description, String subjectId,
                      String teacherId, String dueDate) {
        this.title = title;
        this.description = description;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.dueDate = dueDate;
        this.minFiles = 1;
    }

    public Assignment(String title, String description, String subjectId,
                      String teacherId, String dueDate, int minFiles) {
        this.title = title;
        this.description = description;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.dueDate = dueDate;
        this.minFiles = minFiles;
    }

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public int getMinFiles() { return minFiles; }
    public void setMinFiles(int minFiles) { this.minFiles = minFiles; }

    @Override
    public String toString() {
        return "Assignment{id=" + assignmentId + ", title='" + title + "'}";
    }
}
