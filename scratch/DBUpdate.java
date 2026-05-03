import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBUpdate {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/student_management_system";
        String user = "root";
        String password = "2042124064";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            
            // Update 'General' to 'DBMS' so it looks better in the screenshot
            int rows = stmt.executeUpdate("UPDATE attendance SET subject = 'DBMS' WHERE subject = 'General'");
            System.out.println("Database updated: Changed " + rows + " 'General' records to 'DBMS'.");
            
        } catch (Exception e) {
            System.err.println("Error updating database: " + e.getMessage());
        }
    }
}
