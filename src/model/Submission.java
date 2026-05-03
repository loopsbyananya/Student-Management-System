package model;

/**
 * Represents a student's submission for an assignment.
 */
public class Submission {

    private int submissionId;
    private int assignmentId;
    private String studentId;
    private String submissionText;
    private int marks;  // -1 means not graded yet
    private String submissionDate;

    public Submission() {
        this.marks = -1;
    }

    public Submission(int assignmentId, String studentId, String submissionText) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.submissionText = submissionText;
        this.marks = -1;
    }

    public int getSubmissionId() { return submissionId; }
    public void setSubmissionId(int submissionId) { this.submissionId = submissionId; }

    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getSubmissionText() { return submissionText; }
    public void setSubmissionText(String submissionText) { this.submissionText = submissionText; }

    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }

    public String getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(String submissionDate) { this.submissionDate = submissionDate; }

    @Override
    public String toString() {
        return "Submission{id=" + submissionId + ", assignmentId=" + assignmentId + "}";
    }
}
