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
                baseColor = new Color(235, 239, 244);
                hoverColor = new Color(215, 221, 228);
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

        // Subtle shadow below the button
        if (!isPressed && style != Style.SECONDARY) {
            g2.setColor(new Color(0, 0, 0, 18));
            g2.fillRoundRect(1, 2, w - 2, h - 1, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        // Button body — shift down slightly when pressed
        int yOffset = isPressed ? 1 : 0;
        g2.setColor(currentColor);
        g2.fillRoundRect(0, yOffset, w, h - 1, Theme.BTN_ARC, Theme.BTN_ARC);

        // Subtle highlight at top for 3D feel (for primary/danger only)
        if (style != Style.SECONDARY && !isPressed) {
            GradientPaint highlight = new GradientPaint(
                    0, yOffset, new Color(255, 255, 255, 30),
                    0, yOffset + h / 3, new Color(255, 255, 255, 0));
            g2.setPaint(highlight);
            g2.fillRoundRect(0, yOffset, w, h / 3, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        // Border for secondary style
        if (style == Style.SECONDARY) {
            g2.setColor(Theme.BORDER);
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, yOffset, w - 1, h - 2, Theme.BTN_ARC, Theme.BTN_ARC);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
