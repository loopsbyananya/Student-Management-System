package model;

/**
 * Represents a teacher in the system.
 * Extends User and adds subject information.
 */
public class Teacher extends User {

    private String subject;

    // Default constructor
    public Teacher() {
        super();
    }

    // Parameterized constructor
    public Teacher(String id, String name, String password, String subject) {
        super(id, name, password, "TEACHER");
        this.subject = subject;
    }

    // --- Getter ---

    public String getSubject() {
        return subject;
    }

    // --- Setter ---

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Teacher{id='" + getId() + "', name='" + getName()
                + "', subject='" + subject + "'}";
    }
}
