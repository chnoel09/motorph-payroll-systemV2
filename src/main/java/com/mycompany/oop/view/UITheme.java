/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITheme {

    // ============================================================
    // COLOR PALETTE — Blue & Red
    // ============================================================

    // Sidebar
    public static final Color SIDEBAR_BG = new Color(18, 32, 60);
    public static final Color SIDEBAR_HOVER = new Color(30, 52, 90);

    // Primary accent
    public static final Color ACCENT = new Color(210, 43, 43);
    public static final Color ACCENT_HOVER = new Color(185, 30, 30);

    // Secondary accent
    public static final Color BLUE = new Color(37, 99, 195);
    public static final Color BLUE_LIGHT = new Color(59, 130, 246);

    // Backgrounds
    public static final Color BG = new Color(241, 243, 248);
    public static final Color CARD_BG = Color.WHITE;

    // Text
    public static final Color TEXT_PRIMARY = new Color(20, 30, 50);
    public static final Color TEXT_SECONDARY = new Color(100, 110, 125);
    public static final Color TEXT_WHITE = new Color(240, 242, 248);
    public static final Color TEXT_SIDEBAR = new Color(155, 170, 200);

    // Borders
    public static final Color BORDER = new Color(210, 218, 230);

    // Table
    public static final Color TABLE_HEADER_BG = new Color(18, 32, 60);
    public static final Color TABLE_ALT_ROW = new Color(245, 247, 252);

    // Status
    public static final Color DANGER = new Color(210, 43, 43);
    public static final Color DANGER_HOVER = new Color(185, 30, 30);
    public static final Color SUCCESS = new Color(34, 160, 70);

    // Legacy aliases for compatibility with existing Group files
    public static final Color MAIN_GRAY = BG;
    public static final Color DASHBOARD_BG = BG;

    // ============================================================
    // FONTS
    // ============================================================

    public static final Font FONT_PAGE_TITLE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_NAV = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 12);

    // ============================================================
    // THEME INITIALIZATION
    // ============================================================

    public static void initTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
    }

    // ============================================================
    // PAGE HEADER
    // ============================================================

    public static JPanel createTitleBar(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setPreferredSize(new Dimension(0, 52));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(0, 24, 0, 24)
        ));

        JLabel label = new JLabel(title);
        label.setFont(FONT_SECTION);
        label.setForeground(TEXT_PRIMARY);

        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    // ============================================================
    // DEFAULT BUTTON
    // ============================================================

    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(225, 230, 240));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(210, 218, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(225, 230, 240));
            }
        });

        return btn;
    }

    // ============================================================
    // ACCENT BUTTON
    // ============================================================

    public static JButton createAccentButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 22, 8, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(ACCENT_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT);
            }
        });

        return btn;
    }

    // ============================================================
    // BLUE BUTTON
    // ============================================================

    public static JButton createBlueButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBackground(BLUE);
        btn.setForeground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 22, 8, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BLUE_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BLUE);
            }
        });

        return btn;
    }

    // ============================================================
    // SIDEBAR NAVIGATION BUTTON
    // ============================================================

    public static JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(TEXT_SIDEBAR);
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 16));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("sidebar.active", false);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(SIDEBAR_HOVER);
                btn.setForeground(TEXT_WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean active = Boolean.TRUE.equals(btn.getClientProperty("sidebar.active"));
                if (active) {
                    btn.setBackground(SIDEBAR_HOVER);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(TEXT_SIDEBAR);
                }
            }
        });

        return btn;
    }

    // ============================================================
    // SIDEBAR DANGER BUTTON
    // ============================================================

    public static JButton createSidebarDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NAV);
        btn.setForeground(new Color(200, 140, 140));
        btn.setBackground(SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 20, 12, 16));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(60, 25, 25));
                btn.setForeground(new Color(255, 170, 170));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(new Color(200, 140, 140));
            }
        });

        return btn;
    }

    // ============================================================
    // CRUD DANGER BUTTON
    // ============================================================

    public static JButton createCrudDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(254, 240, 240));
        btn.setForeground(DANGER);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(DANGER);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(254, 240, 240));
                btn.setForeground(DANGER);
            }
        });

        return btn;
    }

    // ============================================================
    // FORM BUTTON
    // ============================================================

    public static JButton createFormButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(225, 230, 240));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBorder(new EmptyBorder(8, 22, 8, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ============================================================
    // PANEL STYLES
    // ============================================================

    public static JPanel createRaisedPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createLineBorder(BORDER));
        return panel;
    }

    public static JPanel createInsetPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    public static JPanel createDashboardCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    // ============================================================
    // TABLE STYLING
    // ============================================================

    public static void styleTable(JTable table) {
        table.setRowHeight(36);
        table.setFont(FONT_TABLE);
        table.setGridColor(new Color(230, 235, 245));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(220, 230, 245));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);

                label.setBackground(TABLE_HEADER_BG);
                label.setForeground(Color.WHITE);
                label.setFont(FONT_TABLE_HEADER);
                label.setBorder(new EmptyBorder(0, 12, 0, 12));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        t, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW);
                }

                if (c instanceof JComponent component) {
                    component.setBorder(new EmptyBorder(0, 12, 0, 12));
                }

                return c;
            }
        });
    }

    // ============================================================
    // TABLE SCROLL PANE
    // ============================================================

    public static JScrollPane createTableScrollPane(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }
}