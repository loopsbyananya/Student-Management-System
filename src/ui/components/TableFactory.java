package ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Utility class for creating consistently styled JTables
 * with alternating row colors, row hover highlight, and styled headers.
 */
public class TableFactory {

    /**
     * Creates a styled, read-only JTable wrapped in a JScrollPane.
     *
     * @param tableModel the data model
     * @return a JScrollPane containing the styled table
     */
    public static JScrollPane createStyledTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel);
        table.setEnabled(false);
        table.setRowHeight(Theme.TABLE_ROW_HEIGHT);
        table.setFont(Theme.FONT_TABLE_CELL);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(Theme.BORDER_LIGHT);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.PRIMARY_LIGHT);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        // --- Row hover tracking ---
        final int[] hoveredRow = {-1};

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow[0]) {
                    hoveredRow[0] = row;
                    table.repaint();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.repaint();
            }
        });

        // --- Cell renderer with zebra + hover ---
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row == hoveredRow[0]) {
                        c.setBackground(Theme.TABLE_HOVER_ROW);
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : Theme.TABLE_ALT_ROW);
                    }
                }
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
                return c;
            }
        };
        cellRenderer.setVerticalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // --- Style the header ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.TABLE_HEADER_BG);
        header.setForeground(Theme.TEXT_LIGHT);
        header.setFont(Theme.FONT_TABLE_HEADER);
        header.setPreferredSize(new Dimension(0, 52)); // Taller header
        header.setReorderingAllowed(false);
        header.setBorder(null);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBackground(Theme.TABLE_HEADER_BG);
                label.setForeground(Theme.TEXT_LIGHT);
                label.setFont(Theme.FONT_TABLE_HEADER);
                label.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
                return label;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        // --- ScrollPane styling ---
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Round the scrollpane corners via a custom border
        scrollPane.setOpaque(false);

        return scrollPane;
    }

    /**
     * Creates a styled, interactive (selectable) JTable wrapped in a JScrollPane.
     * Unlike createStyledTable, the table cells are selectable/clickable.
     *
     * @param tableModel the data model
     * @return a JScrollPane containing the styled interactive table
     */
    public static JScrollPane createInteractiveTable(DefaultTableModel tableModel) {
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setRowHeight(Theme.TABLE_ROW_HEIGHT);
        table.setFont(Theme.FONT_TABLE_CELL);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(Theme.BORDER_LIGHT);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(Theme.PRIMARY_LIGHT);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        // --- Row hover tracking ---
        final int[] hoveredRow = {-1};

        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow[0]) {
                    hoveredRow[0] = row;
                    table.repaint();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.repaint();
            }
        });

        // --- Cell renderer with zebra + hover ---
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    if (row == hoveredRow[0]) {
                        c.setBackground(Theme.TABLE_HOVER_ROW);
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : Theme.TABLE_ALT_ROW);
                    }
                }
                setForeground(Theme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
                return c;
            }
        };
        cellRenderer.setVerticalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // --- Style the header ---
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.TABLE_HEADER_BG);
        header.setForeground(Theme.TEXT_LIGHT);
        header.setFont(Theme.FONT_TABLE_HEADER);
        header.setPreferredSize(new Dimension(0, 44));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Theme.PRIMARY));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBackground(Theme.TABLE_HEADER_BG);
                label.setForeground(Theme.TEXT_LIGHT);
                label.setFont(Theme.FONT_TABLE_HEADER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1,
                                new Color(50, 60, 80)),
                        BorderFactory.createEmptyBorder(0, 16, 0, 16)
                ));
                return label;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        // --- ScrollPane styling ---
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setOpaque(false);

        return scrollPane;
    }

    // Private constructor
    private TableFactory() { }
}
