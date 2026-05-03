package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A styled JButton with rounded corners, custom colors, hover animation,
 * and pressed state feedback.
 * Supports PRIMARY, DANGER, and SECONDARY variants.
 */
public class StyledButton extends JButton {

    /** Button style variants. */
    public enum Style { PRIMARY, DANGER, SECONDARY }

    private Color baseColor;
    private Color hoverColor;
    private Color textColor;
    private Color currentColor;
    private final Style style;
    private boolean isHovered = false;
    private boolean isPressed = false;

    /**
     * Creates a styled button.
     *
     * @param text  button label
     * @param style visual style (PRIMARY, DANGER, SECONDARY)
     */
    public StyledButton(String text, Style style) {
        super(text);
        this.style = style;
        applyStyle(style);
        currentColor = baseColor;
        setFont(Theme.FONT_BTN);
        setForeground(textColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, 42));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                isPressed = false;
                currentColor = baseColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    /** Convenience constructor defaulting to PRIMARY style. */
    public StyledButton(String text) {
        this(text, Style.PRIMARY);
    }

    private void applyStyle(Style s) {
        switch (s) {
            case DANGER:
                baseColor = Theme.DANGER;
                hoverColor = Theme.DANGER_HOVER;
                textColor = Theme.TEXT_LIGHT;
                break;
            case SECONDARY:
                baseColor = Theme.CONTENT_BG;
                hoverColor = Theme.BORDER;
                textColor = Theme.TEXT_PRIMARY;
                break;
            case PRIMARY:
            default:
                baseColor = Theme.PRIMARY;
                hoverColor = Theme.PRIMARY_HOVER;
                textColor = Theme.TEXT_LIGHT;
                break;
        }
        setForeground(textColor);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // High-end shadow
        if (!isPressed && style != Style.SECONDARY) {
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(2, 4, w - 4, h - 4, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        // Button body
        int yOffset = isPressed ? 1 : 0;
        g2.setColor(currentColor);
        g2.fillRoundRect(0, yOffset, w, h - (isPressed ? 0 : 2), Theme.BTN_ARC, Theme.BTN_ARC);

        // Subtle gradient overlay
        if (!isPressed && style != Style.SECONDARY) {
            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255, 20), 0, h, new Color(0, 0, 0, 20));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h - 2, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        // Border for secondary
        if (style == Style.SECONDARY) {
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(0, 0, w - 1, h - 2, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
