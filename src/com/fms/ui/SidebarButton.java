package com.fms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class SidebarButton extends JButton {
    private boolean isSelected = false;
    private boolean isHovered = false;
    private String iconPath;
    private static final int BUTTON_HEIGHT = 44;
    private static final int ICON_SIZE = 20;
    private static final int BORDER_RADIUS = 8;

    private static final Color HOVER_BG = new Color(243, 244, 246);
    private static final Color ACTIVE_BG = new Color(56, 103, 214);
    private static final Color TEXT_DEFAULT = new Color(75, 85, 99);
    private static final Color TEXT_ACTIVE = Color.WHITE;

    public SidebarButton(String text, String iconPath) {
        super(text);
        this.iconPath = iconPath;

        setFont(StyleTheme.FONT_REGULAR);
        setForeground(TEXT_DEFAULT);
        setBackground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(12);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        setPreferredSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, BUTTON_HEIGHT));
        setMaximumSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, BUTTON_HEIGHT));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        loadIcon();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    private void loadIcon() {
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                java.net.URL iconUrl = getClass().getResource(iconPath);
                if (iconUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(iconUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE,
                            Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(scaledImage));
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Icon not found, continue without icon
            }
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        if (selected) {
            setForeground(TEXT_ACTIVE);
            setFont(StyleTheme.FONT_BOLD);
        } else {
            setForeground(TEXT_DEFAULT);
            setFont(StyleTheme.FONT_REGULAR);
        }
        repaint();
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bgColor;
        if (isSelected) {
            bgColor = ACTIVE_BG;
        } else if (isHovered) {
            bgColor = HOVER_BG;
        } else {
            bgColor = null;
        }

        if (bgColor != null) {
            g2.setColor(bgColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS));
        }

        g2.dispose();

        super.paintComponent(g);
    }

    public static ImageIcon createSidebarIcon(String resourcePath, Color color) {
        try {
            java.net.URL iconUrl = SidebarButton.class.getResource(resourcePath);
            if (iconUrl != null) {
                ImageIcon originalIcon = new ImageIcon(iconUrl);
                Image img = originalIcon.getImage();

                java.awt.image.BufferedImage coloredImg = new java.awt.image.BufferedImage(
                        ICON_SIZE, ICON_SIZE, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = coloredImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(img, 0, 0, ICON_SIZE, ICON_SIZE, null);
                g2.dispose();

                return new ImageIcon(coloredImg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
