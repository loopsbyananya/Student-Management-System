package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object for user authentication.
 * Handles login verification against the USERS table.
 */
public class UserDAO {

    /**
     * Authenticates a user by checking their ID and password against the database.
     *
     * @param id       the user's login ID
     * @param password the user's password
     * @return a User object if credentials are valid, null otherwise
     */
    public User authenticate(String id, String password) {
        if (id == null || password == null) return null;

        id = id.trim();
        password = password.trim();

        String sql = "SELECT * FROM users WHERE TRIM(id)=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String dbPassword = rs.getString("password").trim();

                if (dbPassword.equalsIgnoreCase(password)) {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(dbPassword);
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
