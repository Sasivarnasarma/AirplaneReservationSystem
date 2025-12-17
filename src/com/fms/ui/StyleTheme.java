package com.fms.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class StyleTheme {
    public static final Color PRIMARY_COLOR = new Color(56, 103, 214);
    public static final Color PRIMARY_DARK = new Color(41, 78, 167);
    public static final Color PRIMARY_LIGHT = new Color(235, 241, 253);

    public static final Color SIDEBAR_BG = new Color(30, 58, 138);
    public static final Color SIDEBAR_HOVER = new Color(43, 77, 168);
    public static final Color SIDEBAR_ACTIVE = new Color(56, 103, 214);

    public static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);
    public static final Color WARNING_COLOR = new Color(245, 158, 11);
    public static final Color WARNING_LIGHT = new Color(254, 243, 199);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);
    public static final Color INFO_COLOR = new Color(59, 130, 246);
    public static final Color INFO_LIGHT = new Color(219, 234, 254);

    public static final Color BG_COLOR_MAIN = new Color(243, 244, 246);
    public static final Color TEXT_COLOR = new Color(31, 41, 55);
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128);
    public static final Color WHITE = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(229, 231, 235);
    public static final Color TABLE_ROW_HOVER = new Color(249, 250, 251);

    public static final String FONT_FAMILY;

    static {
        String family = "Segoe UI";
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fonts = ge.getAvailableFontFamilyNames();
            for (String font : fonts) {
                if ("Poppins".equalsIgnoreCase(font)) {
                    family = "Poppins";
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FONT_FAMILY = family;
    }

    public static final Font FONT_TITLE = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font FONT_HEADING = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font FONT_REGULAR = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_STAT_VALUE = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font FONT_STAT_LABEL = new Font(FONT_FAMILY, Font.PLAIN, 13);

    public static final int BORDER_RADIUS = 12;
    public static final int SIDEBAR_WIDTH = 260;
    public static final int CARD_PADDING = 20;

    public static final float LOGIN_INPUT_BORDER_THICKNESS = 0.5f;
    public static final float LOGIN_INPUT_BORDER_FOCUS_THICKNESS = 2.0f;
    public static final float REGISTER_INPUT_BORDER_THICKNESS = 0.5f;
    public static final float REGISTER_INPUT_BORDER_FOCUS_THICKNESS = 2.0f;

    public static JButton createButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? PRIMARY_DARK
                        : getModel().isRollover() ? PRIMARY_COLOR.brighter() : PRIMARY_COLOR;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 30, 40));
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(BORDER_COLOR);
                if (getModel().isRollover())
                    g2.setColor(PRIMARY_COLOR);

                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(PRIMARY_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 30, 40));
        return btn;
    }

    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? SUCCESS_COLOR.darker()
                        : getModel().isRollover() ? SUCCESS_COLOR.brighter() : SUCCESS_COLOR;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 30, 40));
        return btn;
    }

    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? DANGER_COLOR.darker()
                        : getModel().isRollover() ? DANGER_COLOR.brighter() : DANGER_COLOR;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BOLD);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 30, 40));
        return btn;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_REGULAR);
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SUBTITLE);
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    public static JTextField createTextField() {
        JTextField tf = new JTextField(20);
        tf.setFont(FONT_REGULAR);
        tf.setPreferredSize(new Dimension(tf.getPreferredSize().width, 40));
        return tf;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField pf = new JPasswordField(20);
        pf.setFont(FONT_REGULAR);
        pf.setPreferredSize(new Dimension(pf.getPreferredSize().width, 40));
        return pf;
    }

    public static JPanel createRoundedPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBackground(WHITE);
        return panel;
    }

    public static JPanel createCardPanel() {
        JPanel panel = createRoundedPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));
        return panel;
    }

    public static JLabel createPillLabel(String text, Color bgColor, Color textColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(FONT_SMALL);
        label.setForeground(textColor);
        label.setOpaque(false);
        label.setPreferredSize(new Dimension(85, 26));
        return label;
    }

    public static Color[] getStatusColors(String status) {
        if (status == null)
            status = "";
        switch (status.toUpperCase()) {
            case "CONFIRMED":
            case "PAID":
            case "ON TIME":
            case "BOARDING":
            case "FINISHED":
                return new Color[] { SUCCESS_LIGHT, SUCCESS_COLOR };
            case "PENDING":
            case "DELAYED":
                return new Color[] { WARNING_LIGHT, WARNING_COLOR };
            case "CANCELLED":
                return new Color[] { DANGER_LIGHT, DANGER_COLOR };
            default:
                return new Color[] { INFO_LIGHT, INFO_COLOR };
        }
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(55);
        table.setFont(FONT_REGULAR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(BORDER_COLOR);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_COLOR);
        table.setBackground(WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(BG_COLOR_MAIN);
        header.setForeground(TEXT_SECONDARY);
        header.setPreferredSize(new Dimension(0, 50));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        centerRenderer.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    public static JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                if (getText().isEmpty() && !hasFocus() && placeholder != null) {
                    g2.setColor(new Color(150, 150, 150));
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int x = getInsets().left;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, x, y);
                }
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? PRIMARY_COLOR : new Color(180, 180, 180));
                float stroke = hasFocus() ? 2.0f : 1.0f;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 12, 12);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        field.setPreferredSize(new Dimension(300, 46));
        return field;
    }

    public static class RoundedBorder extends AbstractBorder {
        private int radius;
        private Color color;
        private int thickness = 1;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius;
            this.color = color;
            this.thickness = thickness;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }
    }

    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_REGULAR);
        box.setBackground(WHITE);
        box.setForeground(TEXT_COLOR);
        box.setBorder(new RoundedBorder(12, new Color(180, 180, 180)));
        if (box.getEditor().getEditorComponent() instanceof JTextField) {
            ((JTextField) box.getEditor().getEditorComponent()).setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
