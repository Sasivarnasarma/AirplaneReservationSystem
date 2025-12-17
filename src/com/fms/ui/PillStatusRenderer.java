package com.fms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class PillStatusRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        panel.setOpaque(true);

        if (isSelected) {
            panel.setBackground(StyleTheme.PRIMARY_LIGHT);
        } else {
            panel.setBackground(Color.WHITE);
        }

        String status = value != null ? value.toString() : "";
        Color[] colors = StyleTheme.getStatusColors(status);

        JLabel pill = new JLabel(status, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(colors[0]);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                g2.dispose();
                super.paintComponent(g);
            }
        };

        pill.setFont(StyleTheme.FONT_SMALL);
        pill.setForeground(colors[1]);
        pill.setOpaque(false);
        pill.setPreferredSize(new Dimension(90, 28));
        pill.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        panel.add(pill);

        return panel;
    }
}
