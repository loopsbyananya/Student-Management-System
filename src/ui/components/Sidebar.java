package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Reusable sidebar navigation panel.
 * Dark background with flat navigation buttons, hover effects, active indicator strip,
 * and active state styling.
 */
public class Sidebar extends JPanel {

    private final List<JButton> navButtons = new ArrayList<>();
    private JButton activeButton = null;
    private JPanel container;
    private JPanel navPanel;

    /**
     * Creates a sidebar with the given title (e.g. "Admin Panel").
     *
     * @param title    sidebar heading
     * @param userName name of the logged-in user
     */
    public Sidebar(String title, String userName) {
        setPreferredSize(new Dimension(250, 0));
        setMinimumSize(new Dimension(250, 0));
        setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
        setBackground(Theme.SIDEBAR_BG);
        setLayout(new BorderLayout());

        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Theme.SIDEBAR_BG);

        // Wrap the container in a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Theme.SIDEBAR_BG);
        scrollPane.getViewport().setBackground(Theme.SIDEBAR_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0)); // Slim scrollbar

        add(scrollPane, BorderLayout.CENTER);

        // --- App title / branding ---
        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
        brandPanel.setBackground(Theme.SIDEBAR_BG);
        brandPanel.setBorder(BorderFactory.createEmptyBorder(28, 24, 6, 24));
        brandPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        titleLabel.setForeground(Theme.TEXT_LIGHT);
        brandPanel.add(titleLabel);

        container.add(brandPanel);

        // --- User info with avatar initial ---
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setBackground(Theme.SIDEBAR_BG);
        userPanel.setBorder(BorderFactory.createEmptyBorder(6, 24, 20, 24));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Avatar circle with initial letter
        String initial = userName.isEmpty() ? "?" : userName.substring(0, 1).toUpperCase();
        JLabel avatarLabel = new JLabel(initial) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.SIDEBAR_ACTIVE);
                g2.fillOval(0, 0, 32, 32);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avatarLabel.setForeground(Theme.TEXT_LIGHT);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(32, 32));
        avatarLabel.setMinimumSize(new Dimension(32, 32));
        avatarLabel.setMaximumSize(new Dimension(32, 32));
        userPanel.add(avatarLabel);

        userPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel userLabel = new JLabel(userName);
        userLabel.setFont(Theme.FONT_SMALL);
        userLabel.setForeground(Theme.TEXT_MUTED);
        userPanel.add(userLabel);

        container.add(userPanel);

        // --- Divider ---
        JPanel dividerWrapper = new JPanel(new BorderLayout());
        dividerWrapper.setBackground(Theme.SIDEBAR_BG);
        dividerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        dividerWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(45, 55, 72));
        sep.setBackground(new Color(45, 55, 72));
        dividerWrapper.add(sep, BorderLayout.CENTER);
        container.add(dividerWrapper);

        // --- Section label ---
        JPanel menuLabelPanel = new JPanel();
        menuLabelPanel.setLayout(new BoxLayout(menuLabelPanel, BoxLayout.Y_AXIS));
        menuLabelPanel.setBackground(Theme.SIDEBAR_BG);
        menuLabelPanel.setBorder(BorderFactory.createEmptyBorder(16, 24, 6, 24));
        menuLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        menuLabel.setForeground(new Color(80, 95, 120));
        menuLabelPanel.add(menuLabel);
        container.add(menuLabelPanel);

        // --- Navigation buttons container (NEW) ---
navPanel = new JPanel();
navPanel.setBackground(Theme.SIDEBAR_BG);
navPanel.setLayout(new GridLayout(0, 1, 0, 8)); // equal spacing, responsive
container.add(navPanel);
    }

    /**
     * Adds a navigation button to the sidebar with hover effects and
     * a colored left indicator strip when active.
     *
     * @param label    button text
     * @param icon     Unicode icon string — can be ""
     * @param listener action to perform on click
     * @return the created JButton
     */
    public JButton addNavButton(String label, String icon, ActionListener listener) {
        // Wrapper panel to draw the active indicator strip
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Find the button in this wrapper
                Component[] children = getComponents();
                for (Component c : children) {
                    if (c instanceof JButton && c == activeButton) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Theme.SIDEBAR_INDICATOR);
                        // Draw rounded indicator strip on the left
                        g2.fillRoundRect(0, 6, Theme.INDICATOR_WIDTH,
                                getHeight() - 12, 3, 3);
                        g2.dispose();
                    }
                }
            }
        };
        wrapper.setBackground(Theme.SIDEBAR_BG);
        wrapper.setOpaque(true);
        wrapper.setPreferredSize(null); // allow full expansion

        JButton btn = new JButton(icon.isEmpty() ? label : icon + "   " + label);
        btn.setFont(Theme.FONT_SIDEBAR);
        btn.setForeground(Theme.TEXT_MUTED);
        btn.setBackground(Theme.SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        // Hover and active styling
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(Theme.SIDEBAR_HOVER);
                    btn.setForeground(Theme.TEXT_LIGHT);
                    wrapper.setBackground(Theme.SIDEBAR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(Theme.SIDEBAR_BG);
                    btn.setForeground(Theme.TEXT_MUTED);
                    wrapper.setBackground(Theme.SIDEBAR_BG);
                }
            }
        });

        btn.addActionListener(listener);
        navButtons.add(btn);

        wrapper.add(btn, BorderLayout.CENTER);
        navPanel.add(wrapper);


        return btn;
    }

    /**
     * Adds vertical glue to push the logout button to the bottom.
     */
    public void addSpacer() {
        container.add(Box.createVerticalGlue());

        // Add a separator above logout
        JPanel dividerWrapper = new JPanel(new BorderLayout());
        dividerWrapper.setBackground(Theme.SIDEBAR_BG);
        dividerWrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 8, 20));
        dividerWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 9));

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(45, 55, 72));
        sep.setBackground(new Color(45, 55, 72));
        dividerWrapper.add(sep, BorderLayout.CENTER);
        container.add(dividerWrapper);
    }

    /**
     * Marks the given button as the active/selected item.
     * Resets previous active button and repaints for indicator.
     */
    public void setActiveButton(JButton btn) {
        // Reset previous
        if (activeButton != null) {
            activeButton.setBackground(Theme.SIDEBAR_BG);
            activeButton.setForeground(Theme.TEXT_MUTED);
            if (activeButton.getParent() != null) {
                activeButton.getParent().setBackground(Theme.SIDEBAR_BG);
                activeButton.getParent().repaint();
            }
        }
        activeButton = btn;
        if (btn != null) {
            btn.setBackground(new Color(30, 41, 59)); // Slate 800
            btn.setForeground(Theme.SIDEBAR_INDICATOR);
            if (btn.getParent() != null) {
                btn.getParent().setBackground(new Color(30, 41, 59));
                btn.getParent().repaint();
            }
        }
    }
}
