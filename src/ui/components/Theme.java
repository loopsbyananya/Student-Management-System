package ui.components;

import java.awt.*;

/**
 * Centralized theme constants for the Student Management System.
 * All UI classes reference these constants for a consistent look.
 */
public class Theme {

    // --- Color Palette ---
    // --- Color Palette (Modern Indigo/Slate) ---
    public static final Color SIDEBAR_BG        = new Color(15, 23, 42);      // Slate 900
    public static final Color SIDEBAR_HOVER     = new Color(30, 41, 59);      // Slate 800
    public static final Color SIDEBAR_ACTIVE    = new Color(79, 70, 229);     // Indigo 600
    public static final Color SIDEBAR_INDICATOR = new Color(129, 140, 248);    // Indigo 400
    public static final Color HEADER_BG         = new Color(255, 255, 255, 240); // Transparent white
    public static final Color CONTENT_BG        = new Color(248, 250, 252);   // Slate 50
    public static final Color CARD_BG           = Color.WHITE;
    public static final Color PRIMARY           = new Color(79, 70, 229);     // Indigo 600
    public static final Color PRIMARY_HOVER     = new Color(67, 56, 202);     // Indigo 700
    public static final Color PRIMARY_LIGHT     = new Color(238, 242, 255);   // Indigo 50
    public static final Color DANGER            = new Color(225, 29, 72);     // Rose 600
    public static final Color DANGER_HOVER      = new Color(190, 18, 60);     // Rose 700
    public static final Color SUCCESS           = new Color(5, 150, 105);     // Emerald 600
    public static final Color SUCCESS_LIGHT     = new Color(209, 250, 229);   // Emerald 100
    public static final Color AMBER             = new Color(217, 119, 6);     // Amber 600
    public static final Color PURPLE            = new Color(124, 58, 237);    // Violet 600
    public static final Color TEXT_PRIMARY      = new Color(15, 23, 42);      // Slate 900
    public static final Color TEXT_SECONDARY    = new Color(71, 85, 105);     // Slate 600
    public static final Color TEXT_LIGHT        = Color.WHITE;
    public static final Color TEXT_MUTED        = new Color(148, 163, 184);   // Slate 400
    public static final Color BORDER            = new Color(226, 232, 240);   // Slate 200
    public static final Color BORDER_LIGHT      = new Color(241, 245, 249);   // Slate 100
    public static final Color TABLE_HEADER_BG   = new Color(15, 23, 42);      // Slate 900
    public static final Color TABLE_ALT_ROW     = new Color(252, 253, 254);
    public static final Color TABLE_HOVER_ROW   = new Color(241, 245, 249);   // Slate 100
    public static final Color LOGIN_BG_TOP      = new Color(15, 23, 42);
    public static final Color LOGIN_BG_BOTTOM   = new Color(30, 41, 59);
    public static final Color SHADOW            = new Color(0, 0, 0, 15);     // Softer shadow
    public static final Color SHADOW_DARK       = new Color(0, 0, 0, 30);
    public static final Color FIELD_BG          = Color.WHITE;
    public static final Color FIELD_FOCUS_BG    = Color.WHITE;

    // --- Fonts (Inter-like style) ---
    public static final Font FONT_TITLE         = new Font("Inter", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE      = new Font("Inter", Font.BOLD, 18);
    public static final Font FONT_BODY          = new Font("Inter", Font.PLAIN, 14);
    public static final Font FONT_BODY_BOLD     = new Font("Inter", Font.BOLD, 14);
    public static final Font FONT_SMALL         = new Font("Inter", Font.PLAIN, 12);
    public static final Font FONT_SIDEBAR       = new Font("Inter", Font.PLAIN, 15);
    public static final Font FONT_SIDEBAR_TITLE = new Font("Inter", Font.BOLD, 20);
    public static final Font FONT_TABLE_HEADER  = new Font("Inter", Font.BOLD, 13);
    public static final Font FONT_TABLE_CELL    = new Font("Inter", Font.PLAIN, 13);
    public static final Font FONT_FIELD_LABEL   = new Font("Inter", Font.BOLD, 13);
    public static final Font FONT_BTN           = new Font("Inter", Font.BOLD, 13);
    public static final Font FONT_STAT_NUMBER   = new Font("Inter", Font.BOLD, 48);
    public static final Font FONT_GREETING      = new Font("Inter", Font.PLAIN, 14);

    // --- Dimensions & Styling ---
    public static final int SIDEBAR_WIDTH       = 260;
    public static final int HEADER_HEIGHT       = 72;
    public static final int CARD_ARC            = 16;    // More rounded
    public static final int BTN_ARC             = 12;    // More rounded
    public static final int TABLE_ROW_HEIGHT    = 48;    // Taller rows
    public static final int SHADOW_SIZE         = 8;
    public static final int NAV_BTN_HEIGHT      = 50;
    public static final int NAV_BTN_SPACING     = 4;
    public static final int INDICATOR_WIDTH     = 4;     // Active indicator strip width

    // Private constructor — not instantiable
    private Theme() { }
}
