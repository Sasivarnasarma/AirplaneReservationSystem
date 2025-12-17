package com.fms.ui;

import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel {
    private String title;
    private String value;
    private String trend;
    private Color accentColor;

    private static final int ICON_CIRCLE_SIZE = 40;

    public StatCard(String title, String value, String trend, Color accentColor) {
        this.title = title;
        this.value = value;
        this.trend = trend;
        this.accentColor = accentColor;

        setOpaque(false);
    }

    public void setValue(String value) {
        this.value = value;
        repaint();
    }

    public void setTrend(String trend) {
        this.trend = trend;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = 12;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, width, height, radius, radius);

        g2.setColor(StyleTheme.BORDER_COLOR);
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, width - 1, height - 1, radius, radius);

        int padding = 16;
        int iconX = padding;
        int iconY = (height - ICON_CIRCLE_SIZE) / 2;

        Color lightAccent = new Color(
                accentColor.getRed(),
                accentColor.getGreen(),
                accentColor.getBlue(),
                25);
        g2.setColor(lightAccent);
        g2.fillOval(iconX, iconY, ICON_CIRCLE_SIZE, ICON_CIRCLE_SIZE);

        g2.setColor(accentColor);
        int dotSize = 16;
        int dotOffset = (ICON_CIRCLE_SIZE - dotSize) / 2;
        g2.fillOval(iconX + dotOffset, iconY + dotOffset, dotSize, dotSize);

        int textX = iconX + ICON_CIRCLE_SIZE + 12;
        g2.setColor(StyleTheme.TEXT_SECONDARY);
        g2.setFont(StyleTheme.FONT_SMALL);
        g2.drawString(title, textX, iconY + 14);

        g2.setColor(StyleTheme.TEXT_COLOR);
        g2.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 22));
        g2.drawString(value, textX, iconY + ICON_CIRCLE_SIZE - 6);

        if (trend != null && !trend.isEmpty()) {
            g2.setFont(StyleTheme.FONT_SMALL);
            boolean isPositive = trend.startsWith("+") || trend.contains("↑");
            g2.setColor(isPositive ? StyleTheme.SUCCESS_COLOR : StyleTheme.DANGER_COLOR);

            FontMetrics trendFm = g2.getFontMetrics();
            int trendWidth = trendFm.stringWidth(trend);
            g2.drawString(trend, width - trendWidth - padding, iconY + 14);
        }

        g2.dispose();
    }

    public static StatCard create(String title, String value, String trend, Color accentColor) {
        return new StatCard(title, value, trend, accentColor);
    }

    public static JPanel createStatRow(StatCard... cards) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        row.setOpaque(false);
        for (StatCard card : cards) {
            row.add(card);
        }
        return row;
    }
}
