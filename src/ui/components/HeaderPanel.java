package ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Reusable header bar for dashboard screens.
 * Displays the page title on the left and a user greeting on the right.
 * Features a subtle bottom shadow for depth separation.
 */
public class HeaderPanel extends JPanel {

    private JLabel titleLabel;
    private JLabel greetingLabel;

    /**
     * Creates a header panel with a title and user greeting.
     *
     * @param title    page title (e.g. "Admin Dashboard")
     * @param userName the logged-in user's name (shown as greeting on right)
     */
    public HeaderPanel(String title, String userName) {
        setBackground(Theme.HEADER_BG);
        setPreferredSize(new Dimension(0, Theme.HEADER_HEIGHT));
        setLayout(new BorderLayout());
        setOpaque(false); // We paint our own bg for shadow

        setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 28));

        // Center — title
        titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Wrap in a panel to manage margins
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        centerPanel.add(titleLabel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Right side — user greeting
        if (userName != null && !userName.isEmpty()) {
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            rightPanel.setOpaque(false);
            rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            // Greeting text
            greetingLabel = new JLabel("Welcome, " + userName);
            greetingLabel.setFont(Theme.FONT_GREETING);
            greetingLabel.setForeground(Theme.TEXT_SECONDARY);

            // Small dot separator
            JLabel dot = new JLabel("  \u2022  ");
            dot.setFont(Theme.FONT_SMALL);
            dot.setForeground(Theme.BORDER);

            // Online indicator
            JLabel statusLabel = new JLabel("Online") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.SUCCESS);
                    g2.fillOval(0, 5, 8, 8);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            statusLabel.setFont(Theme.FONT_SMALL);
            statusLabel.setForeground(Theme.SUCCESS);
            statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

            rightPanel.add(greetingLabel);
            rightPanel.add(dot);
            rightPanel.add(statusLabel);

            add(rightPanel, BorderLayout.EAST);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();

        // White background
        g2.setColor(Theme.HEADER_BG);
        g2.fillRect(0, 0, w, h);

        // Bottom shadow gradient for depth
        GradientPaint shadow = new GradientPaint(
                0, h - 4, new Color(0, 0, 0, 12),
                0, h, new Color(0, 0, 0, 0));
        g2.setPaint(shadow);
        g2.fillRect(0, h - 4, w, 4);

        // Bottom border line
        g2.setColor(Theme.BORDER);
        g2.drawLine(0, h - 1, w, h - 1);

        g2.dispose();
    }

    /**
     * Updates the title text at runtime.
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
