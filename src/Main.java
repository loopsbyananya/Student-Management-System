import ui.LoginFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point for the Student Management System.
 * Launches the login screen on the Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
