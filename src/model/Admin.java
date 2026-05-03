package model;

/**
 * Represents an admin user in the system.
 * Extends User — no additional fields for now.
 * This class exists to maintain the OOP hierarchy and can be
 * extended with admin-specific functionality later.
 */
public class Admin extends User {

    // Default constructor
    public Admin() {
        super();
    }

    // Parameterized constructor
    public Admin(String id, String name, String password) {
        super(id, name, password, "ADMIN");
    }

    @Override
    public String toString() {
        return "Admin{id='" + getId() + "', name='" + getName() + "'}";
    }
}
