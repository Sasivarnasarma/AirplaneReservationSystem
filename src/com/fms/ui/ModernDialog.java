package com.fms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ModernDialog {

    private static final int DIALOG_WIDTH = 380;
    private static final int DIALOG_PADDING = 30;

    public static void showSuccess(Component parent, String message) {
        showMessage(parent, "Success", message, StyleTheme.SUCCESS_COLOR, "success");
    }

    public static void showError(Component parent, String message) {
        showMessage(parent, "Error", message, StyleTheme.DANGER_COLOR, "error");
    }

    public static void showWarning(Component parent, String message) {
        showMessage(parent, "Warning", message, StyleTheme.WARNING_COLOR, "warning");
    }

    public static void showInfo(Component parent, String message) {
        showMessage(parent, "Information", message, StyleTheme.PRIMARY_COLOR, "info");
    }

    private static void showMessage(Component parent, String title, String message, Color accentColor,
            String iconType) {
        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 56;
                int x = (getWidth() - size) / 2;
                int y = 0;

                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 30));
                g2.fillOval(x, y, size, size);

                int innerSize = 40;
                int innerX = x + (size - innerSize) / 2;
                int innerY = y + (size - innerSize) / 2;
                g2.setColor(accentColor);
                g2.fillOval(innerX, innerY, innerSize, innerSize);

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                int centerX = x + size / 2;
                int centerY = y + size / 2;

                if ("success".equals(iconType)) {
                    int[] xPoints = { centerX - 10, centerX - 3, centerX + 10 };
                    int[] yPoints = { centerY, centerY + 8, centerY - 8 };
                    g2.drawPolyline(xPoints, yPoints, 3);
                } else if ("error".equals(iconType)) {
                    g2.drawLine(centerX - 8, centerY - 8, centerX + 8, centerY + 8);
                    g2.drawLine(centerX + 8, centerY - 8, centerX - 8, centerY + 8);
                } else if ("warning".equals(iconType)) {
                    g2.drawLine(centerX, centerY - 10, centerX, centerY + 2);
                    g2.fillOval(centerX - 2, centerY + 6, 5, 5);
                } else if ("info".equals(iconType)) {
                    g2.fillOval(centerX - 2, centerY - 10, 5, 5);
                    g2.drawLine(centerX, centerY - 3, centerX, centerY + 10);
                }

                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(DIALOG_WIDTH - 60, 65));
        iconPanel.setMaximumSize(new Dimension(DIALOG_WIDTH - 60, 65));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align: center; width: 260px;'>" + message + "</div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(StyleTheme.FONT_REGULAR);
        messageLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton okBtn = createRoundedButton("OK", accentColor);
        okBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        okBtn.addActionListener(e -> dialog.dispose());

        contentPanel.add(iconPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(okBtn);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    public static boolean showConfirm(Component parent, String title, String message) {
        final boolean[] result = { false };

        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 56;
                int x = (getWidth() - size) / 2;
                int y = 0;

                g2.setColor(new Color(StyleTheme.PRIMARY_COLOR.getRed(), StyleTheme.PRIMARY_COLOR.getGreen(),
                        StyleTheme.PRIMARY_COLOR.getBlue(), 30));
                g2.fillOval(x, y, size, size);

                int innerSize = 40;
                int innerX = x + (size - innerSize) / 2;
                int innerY = y + (size - innerSize) / 2;
                g2.setColor(StyleTheme.PRIMARY_COLOR);
                g2.fillOval(innerX, innerY, innerSize, innerSize);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 24));
                FontMetrics fm = g2.getFontMetrics();
                String q = "?";
                int textX = x + (size - fm.stringWidth(q)) / 2;
                int textY = y + (size + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(q, textX, textY);

                g2.dispose();
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(DIALOG_WIDTH - 60, 65));
        iconPanel.setMaximumSize(new Dimension(DIALOG_WIDTH - 60, 65));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align: center; width: 260px;'>" + message + "</div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(StyleTheme.FONT_REGULAR);
        messageLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton noBtn = createSecondaryRoundedButton("No");
        noBtn.addActionListener(e -> {
            result[0] = false;
            dialog.dispose();
        });

        JButton yesBtn = createRoundedButton("Yes", StyleTheme.PRIMARY_COLOR);
        yesBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        btnPanel.add(noBtn);
        btnPanel.add(yesBtn);

        contentPanel.add(iconPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(btnPanel);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }

    public static String showSelection(Component parent, String title, String message, String[] options,
            String defaultOption) {
        final String[] result = { null };

        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(StyleTheme.FONT_REGULAR);
        messageLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(StyleTheme.FONT_REGULAR);
        comboBox.setSelectedItem(defaultOption);
        comboBox.setMaximumSize(new Dimension(DIALOG_WIDTH - 80, 44));
        comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = createSecondaryRoundedButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton okBtn = createRoundedButton("OK", StyleTheme.PRIMARY_COLOR);
        okBtn.addActionListener(e -> {
            result[0] = (String) comboBox.getSelectedItem();
            dialog.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(comboBox);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(btnPanel);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }

    public static String showOTPInput(Component parent, String title, String message) {
        final String[] result = { null };

        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(
                "<html><div style='text-align: center; width: 280px;'>" + message + "</div></html>");
        messageLabel.setFont(StyleTheme.FONT_REGULAR);
        messageLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        otpPanel.setOpaque(false);
        otpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField[] otpFields = new JTextField[6];
        for (int i = 0; i < 6; i++) {
            final int index = i;
            otpFields[i] = new JTextField(1) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }

                @Override
                protected void paintBorder(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(hasFocus() ? StyleTheme.PRIMARY_COLOR : StyleTheme.BORDER_COLOR);
                    g2.setStroke(new BasicStroke(hasFocus() ? 2 : 1));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    g2.dispose();
                }
            };
            otpFields[i].setOpaque(false);
            otpFields[i].setPreferredSize(new Dimension(48, 56));
            otpFields[i].setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 24));
            otpFields[i].setHorizontalAlignment(JTextField.CENTER);
            otpFields[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            otpFields[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c)) {
                        e.consume();
                        return;
                    }
                    if (otpFields[index].getText().length() >= 1) {
                        e.consume();
                        if (index < 5) {
                            otpFields[index + 1].requestFocus();
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        if (otpFields[index].getText().isEmpty() && index > 0) {
                            otpFields[index - 1].requestFocus();
                        }
                    } else if (Character.isDigit(e.getKeyChar()) && !otpFields[index].getText().isEmpty()
                            && index < 5) {
                        otpFields[index + 1].requestFocus();
                    }
                }
            });

            otpPanel.add(otpFields[i]);
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = createSecondaryRoundedButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton verifyBtn = createRoundedButton("Verify", StyleTheme.PRIMARY_COLOR);
        verifyBtn.addActionListener(e -> {
            StringBuilder otp = new StringBuilder();
            for (JTextField field : otpFields) {
                otp.append(field.getText());
            }
            if (otp.length() == 6) {
                result[0] = otp.toString();
                dialog.dispose();
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(verifyBtn);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(otpPanel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(btnPanel);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        otpFields[0].requestFocus();
        dialog.setVisible(true);

        return result[0];
    }

    private static JDialog createBaseDialog(Component parent, String title) {
        Window window = parent instanceof Window ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(window, title, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(false);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return dialog;
    }

    private static JButton createRoundedButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(StyleTheme.FONT_BOLD);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(100, 44));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private static JButton createSecondaryRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(StyleTheme.BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(StyleTheme.FONT_BOLD);
        button.setForeground(StyleTheme.TEXT_COLOR);
        button.setPreferredSize(new Dimension(100, 44));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    public static String showInput(Component parent, String title, String message) {
        final String[] result = { null };

        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(StyleTheme.FONT_REGULAR);
        messageLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField inputField = StyleTheme.createTextField();
        inputField.setMaximumSize(new Dimension(DIALOG_WIDTH - 60, 40));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = createSecondaryRoundedButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton okBtn = createRoundedButton("OK", StyleTheme.PRIMARY_COLOR);
        okBtn.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(inputField);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(btnPanel);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setSize(DIALOG_WIDTH, dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }

    public static boolean showForm(Component parent, String title, JPanel formPanel) {
        final boolean[] result = { false };

        JDialog dialog = createBaseDialog(parent, title);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING, DIALOG_PADDING));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleTheme.FONT_SUBTITLE);
        titleLabel.setForeground(StyleTheme.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton cancelBtn = createSecondaryRoundedButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton okBtn = createRoundedButton("Submit", StyleTheme.PRIMARY_COLOR);
        okBtn.addActionListener(e -> {
            result[0] = true;
            dialog.dispose();
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(okBtn);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(btnPanel);

        dialog.add(contentPanel);
        dialog.pack();
        int width = Math.max(DIALOG_WIDTH, formPanel.getPreferredSize().width + 60);
        dialog.setSize(width, dialog.getHeight());
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        return result[0];
    }
}
