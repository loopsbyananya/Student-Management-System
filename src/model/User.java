package model;

/**
 * Base class representing a user in the system.
 * All user types (Student, Teacher, Admin) extend this class.
 */
public class User {

    private String id;
    private String name;
    private String password;
    private String role;

    // Default constructor
    public User() {
    }

    // Parameterized constructor
    public User(String id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // --- Getters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // --- Setters ---

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', role='" + role + "'}";
    }
}
