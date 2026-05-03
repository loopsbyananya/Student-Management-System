package ui;

import dao.UserDAO;
import model.User;
import ui.components.StyledButton;
import ui.components.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Modern login screen for the Student Management System.
 * Gradient dark background with a centered card, styled inputs, and polished button.
 */
public class LoginFrame extends JFrame {

    private JTextField idField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Student Management System - Login");
        setSize(920, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Full-screen gradient background
        JPanel background = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(
                        0, 0, Theme.LOGIN_BG_TOP,
                        0, getHeight(), Theme.LOGIN_BG_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        // --- Login card with shadow ---
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = getHeight();

                // Layered shadow
                for (int i = 8; i > 0; i--) {
                    g2.setColor(new Color(0, 0, 0, 4 * (9 - i)));
                    g2.fillRoundRect(i, i + 3, w - i * 2, h - i * 2, 18, 18);
                }

                // Card body
                g2.setColor(Theme.CARD_BG);
                g2.fillRoundRect(8, 8, w - 16, h - 16, 16, 16);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 470));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(52, 48, 48, 48));

        // --- App icon / avatar circle ---
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setOpaque(false);
        JLabel avatar = new JLabel("S") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient circle
                GradientPaint gp = new GradientPaint(0, 0, Theme.PRIMARY,
                        64, 64, new Color(99, 160, 255));
                g2.setPaint(gp);
                g2.fillOval(0, 0, 64, 64);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 28));
        avatar.setForeground(Color.WHITE);
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setPreferredSize(new Dimension(64, 64));
        avatarPanel.add(avatar);
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(avatarPanel);
        card.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- Title ---
        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // --- Subtitle ---
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(Theme.FONT_SMALL);
        subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);

        card.add(Box.createRigidArea(new Dimension(0, 32)));

        // --- ID field ---
        JLabel idLabel = new JLabel("User ID");
        idLabel.setFont(Theme.FONT_FIELD_LABEL);
        idLabel.setForeground(Theme.TEXT_PRIMARY);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(idLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        idField = new JTextField();
        idField.setFont(Theme.FONT_BODY);
        idField.setBackground(Theme.FIELD_BG);
        idField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        idField.setPreferredSize(new Dimension(320, 42));
        idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFocusBorder(idField);
        card.add(idField);

        card.add(Box.createRigidArea(new Dimension(0, 18)));

        // --- Password field ---
        JLabel pwLabel = new JLabel("Password");
        pwLabel.setFont(Theme.FONT_FIELD_LABEL);
        pwLabel.setForeground(Theme.TEXT_PRIMARY);
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pwLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        passwordField = new JPasswordField();
        passwordField.setFont(Theme.FONT_BODY);
        passwordField.setBackground(Theme.FIELD_BG);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        passwordField.setPreferredSize(new Dimension(320, 42));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFocusBorder(passwordField);
        card.add(passwordField);

        card.add(Box.createRigidArea(new Dimension(0, 28)));

        // --- Login button ---
        StyledButton loginButton = new StyledButton("Sign In", StyledButton.Style.PRIMARY);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        loginButton.setPreferredSize(new Dimension(320, 46));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginButton);

        background.add(card);
        setContentPane(background);

        // --- Actions ---
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        getRootPane().setDefaultButton(loginButton);
    }

    /** Adds a blue border + white bg effect on focus. */
    private void addFocusBorder(JComponent field) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBackground(Theme.FIELD_FOCUS_BG);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.PRIMARY, 2),
                        BorderFactory.createEmptyBorder(7, 13, 7, 13)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBackground(Theme.FIELD_BG);
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER, 1),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
    }

    /**
     * Handles the login button click.
     * Validates input, authenticates via UserDAO, and opens the correct dashboard.
     */
    private void handleLogin() {
        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validate input
        if (id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both User ID and Password.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Authenticate using DAO
        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(id, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid User ID or Password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // Clear password field
            return;
        }

        // Login successful — open the appropriate dashboard
        JOptionPane.showMessageDialog(this,
                "Welcome, " + user.getName() + "!",
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);

        // Open dashboard based on role
        switch (user.getRole()) {
            case "ADMIN":
                new AdminDashboard(user).setVisible(true);
                break;
            case "TEACHER":
                new TeacherDashboard(user).setVisible(true);
                break;
            case "STUDENT":
                new StudentDashboard(user).setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this,
                        "Unknown role: " + user.getRole(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
        }

        // Close the login frame
        dispose();
    }
}
