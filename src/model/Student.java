package model;

/**
 * Represents a student in the system.
 * Extends User and adds class/section information.
 */
public class Student extends User {

    // Using 'studentClass' to avoid conflict with Java keyword 'class'
    private String studentClass;

    // Default constructor
    public Student() {
        super();
    }

    // Parameterized constructor
    public Student(String id, String name, String password, String studentClass) {
        super(id, name, password, "STUDENT");
        this.studentClass = studentClass;
    }

    // --- Getter ---

    public String getStudentClass() {
        return studentClass;
    }

    // --- Setter ---

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    @Override
    public String toString() {
        return "Student{id='" + getId() + "', name='" + getName()
                + "', class='" + studentClass + "'}";
    }
}
