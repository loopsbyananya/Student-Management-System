package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing MySQL database connections.
 * Uses JDBC DriverManager to connect to the student_management_system database.
 *
 * IMPORTANT: Update USER and PASSWORD constants to match your MySQL configuration.
 */
public class DBConnection {

    // Database connection parameters
    private static final String URL = "jdbc:mysql://localhost:3306/student_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "2042124064";  // <-- Set your MySQL root password here

    /**
     * Returns a new connection to the MySQL database.
     * Each caller is responsible for closing the returned connection.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
