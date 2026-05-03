package ui.components;

import java.awt.*;

/**
 * Centralized theme constants for the Student Management System.
 * All UI classes reference these constants for a consistent look.
 */
public class Theme {

    // --- Color Palette ---
    public static final Color SIDEBAR_BG        = new Color(24, 33, 50);      // Deep navy
    public static final Color SIDEBAR_HOVER     = new Color(38, 50, 70);      // Lighter on hover
    public static final Color SIDEBAR_ACTIVE    = new Color(59, 130, 246);    // Active item highlight
    public static final Color SIDEBAR_INDICATOR = new Color(99, 160, 255);    // Left strip accent
    public static final Color HEADER_BG         = Color.WHITE;
    public static final Color CONTENT_BG        = new Color(234, 239, 245);   // Slightly darker light-grey
    public static final Color CARD_BG           = Color.WHITE;                // Pure white for contrast
    public static final Color PRIMARY           = new Color(59, 130, 246);    // Blue
    public static final Color PRIMARY_HOVER     = new Color(37, 99, 235);     // Darker blue
    public static final Color PRIMARY_LIGHT     = new Color(219, 234, 254);   // Light blue tint
    public static final Color DANGER            = new Color(239, 68, 68);     // Red
    public static final Color DANGER_HOVER      = new Color(220, 38, 38);
    public static final Color SUCCESS           = new Color(16, 185, 129);    // Emerald green
    public static final Color SUCCESS_LIGHT     = new Color(209, 250, 229);   // Light green tint
    public static final Color AMBER             = new Color(245, 158, 11);    // Amber accent
    public static final Color PURPLE            = new Color(139, 92, 246);    // Purple accent
    public static final Color TEXT_PRIMARY      = new Color(15, 23, 42);      // Near black
    public static final Color TEXT_SECONDARY    = new Color(100, 116, 139);   // Grey
    public static final Color TEXT_LIGHT        = Color.WHITE;
    public static final Color TEXT_MUTED        = new Color(148, 163, 184);   // Muted grey
    public static final Color BORDER            = new Color(226, 232, 240);   // Subtle border
    public static final Color BORDER_LIGHT      = new Color(241, 245, 249);   // Very subtle border
    public static final Color TABLE_HEADER_BG   = new Color(30, 41, 59);     // Dark header
    public static final Color TABLE_ALT_ROW     = new Color(248, 250, 252);   // Zebra stripe
    public static final Color TABLE_HOVER_ROW   = new Color(237, 244, 254);   // Row hover highlight
    public static final Color LOGIN_BG_TOP      = new Color(15, 23, 42);      // Gradient top
    public static final Color LOGIN_BG_BOTTOM   = new Color(30, 58, 95);      // Gradient bottom
    public static final Color SHADOW            = new Color(0, 0, 0, 25);     // Card shadow color
    public static final Color SHADOW_DARK       = new Color(0, 0, 0, 40);     // Deeper shadow
    public static final Color FIELD_BG          = new Color(249, 250, 251);   // Input field bg
    public static final Color FIELD_FOCUS_BG    = Color.WHITE;                // Input field on focus

    // --- Fonts ---
    public static final Font FONT_TITLE         = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE      = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_BODY          = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD     = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_SMALL         = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR       = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SIDEBAR_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_TABLE_HEADER  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_CELL    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_FIELD_LABEL   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BTN           = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_STAT_NUMBER   = new Font("Segoe UI", Font.BOLD, 52);
    public static final Font FONT_GREETING      = new Font("Segoe UI", Font.PLAIN, 13);

    // --- Dimensions ---
    public static final int SIDEBAR_WIDTH       = 230;
    public static final int HEADER_HEIGHT       = 68;
    public static final int CARD_ARC            = 14;    // Card corner rounding
    public static final int BTN_ARC             = 10;    // Button corner rounding
    public static final int TABLE_ROW_HEIGHT    = 40;
    public static final int SHADOW_SIZE         = 6;     // Shadow blur offset
    public static final int NAV_BTN_HEIGHT      = 46;    // Sidebar button height
    public static final int NAV_BTN_SPACING     = 2;     // Gap between nav items
    public static final int INDICATOR_WIDTH     = 4;     // Active indicator strip width

    // Private constructor — not instantiable
    private Theme() { }
}
