package com.fms.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DatePicker extends JDialog {
    private LocalDate selectedDate;
    private YearMonth currentYearMonth;
    private JLabel monthLabel;
    private JPanel daysPanel;
    private JDialog dialog;
    private boolean commit = false;

    public DatePicker(Frame parent) {
        super(parent, true);
        dialog = this;
        setUndecorated(true);

        currentYearMonth = YearMonth.now();
        selectedDate = LocalDate.now();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton prevBtn = createNavButton("<");
        prevBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        JButton nextBtn = createNavButton(">");
        nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(StyleTheme.FONT_BOLD);
        monthLabel.setForeground(StyleTheme.PRIMARY_COLOR);

        headerPanel.add(prevBtn, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        daysPanel.setBackground(Color.WHITE);

        String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 12));
            lbl.setForeground(Color.GRAY);
            daysPanel.add(lbl);
        }

        mainPanel.add(daysPanel, BorderLayout.CENTER);

        JButton closeBtn = new JButton("Cancel");
        closeBtn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 12));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(Color.RED);
        closeBtn.addActionListener(e -> dispose());
        mainPanel.add(closeBtn, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);

        updateCalendar();
        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(StyleTheme.FONT_BOLD);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setForeground(StyleTheme.TEXT_COLOR);
        return btn;
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        daysPanel.removeAll();

        String[] days = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 12));
            lbl.setForeground(Color.GRAY);
            daysPanel.add(lbl);
        }

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        int emptySlots = (dayOfWeek == 7) ? 0 : dayOfWeek;

        for (int i = 0; i < emptySlots; i++) {
            daysPanel.add(new JLabel(""));
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            int day = i;
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setFocusPainted(false);
            dayBtn.setBorder(null);
            dayBtn.setBackground(Color.WHITE);
            dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            LocalDate date = currentYearMonth.atDay(day);
            if (date.equals(LocalDate.now())) {
                dayBtn.setForeground(StyleTheme.PRIMARY_COLOR);
                dayBtn.setFont(StyleTheme.FONT_BOLD);
            }

            dayBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    dayBtn.setBackground(new Color(240, 240, 240));
                }

                public void mouseExited(MouseEvent e) {
                    dayBtn.setBackground(Color.WHITE);
                }
            });

            dayBtn.addActionListener(e -> {
                selectedDate = date;
                commit = true;
                dispose();
            });

            daysPanel.add(dayBtn);
        }

        dialog.pack();
        dialog.repaint();
    }

    public String setPickedDate() {
        if (!commit)
            return "";
        return selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
