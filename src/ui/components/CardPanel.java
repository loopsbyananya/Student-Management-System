package ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * A card-style panel with rounded corners, white background, and drop shadow.
 * Used for grouping content inside dashboards.
 */
public class CardPanel extends JPanel {

    private String cardTitle;

    /**
     * Creates a card panel with optional title.
     *
     * @param title card heading (null for no heading)
     */
    public CardPanel(String title) {
        this.cardTitle = title;
        setBackground(Theme.CARD_BG);
        setLayout(new BorderLayout(0, 12));
        setOpaque(false); // We paint our own background for rounding + shadow

        // Inner padding — generous for breathing room
        setBorder(BorderFactory.createEmptyBorder(
                24 + Theme.SHADOW_SIZE, 24, 24, 24));

        if (title != null && !title.isEmpty()) {
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setOpaque(false);
            titlePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT),
                    BorderFactory.createEmptyBorder(0, 0, 12, 0)
            ));

            JLabel heading = new JLabel(title);
            heading.setFont(Theme.FONT_SUBTITLE);
            heading.setForeground(Theme.TEXT_PRIMARY);
            titlePanel.add(heading, BorderLayout.WEST);

            add(titlePanel, BorderLayout.NORTH);
        }
    }

    /**
     * Convenience: creates a card with no title.
     */
    public CardPanel() {
        this(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int s = Theme.SHADOW_SIZE;
        int w = getWidth();
        int h = getHeight();

        // --- Drop shadow (Modern layered style) ---
        for (int i = 0; i < s; i++) {
            float alpha = 0.02f * (s - i);
            g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
            g2.fillRoundRect(i, i + 3, w - i * 2, h - i * 2,
                    Theme.CARD_ARC + i, Theme.CARD_ARC + i);
        }

        // --- Card body ---
        g2.setColor(getBackground());
        g2.fillRoundRect(s, s, w - s * 2, h - s * 2,
                Theme.CARD_ARC, Theme.CARD_ARC);

        // --- Subtle hairline border ---
        g2.setColor(new Color(226, 232, 240, 150));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(s, s, w - s * 2 - 1, h - s * 2 - 1,
                Theme.CARD_ARC, Theme.CARD_ARC);

        g2.dispose();
    }

    /**
     * Makes the card clickable with hover effects and executes an action on click.
     *
     * @param action runnable to execute
     */
    public void setClickAction(Runnable action) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (action != null) action.run();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(new Color(248, 250, 252)); // Light hover color
                repaint();
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(Theme.CARD_BG);
                repaint();
            }
        });
    }
}
