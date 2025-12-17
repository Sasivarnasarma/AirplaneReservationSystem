package com.fms.ui;

import com.fms.dao.DAOFactory;
import com.fms.dao.IUserDAO;
import com.fms.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JLabel userErrorLabel, passErrorLabel;

    private static final int FIELD_HEIGHT = 46;
    private static final int FORM_WIDTH = 340;

    public LoginFrame() {
        setTitle("IM Airlines - Login");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createImagePanel());

        mainPanel.add(createRightPanel());

        add(mainPanel);
    }

    private JPanel createImagePanel() {
        return new JPanel() {
            private Image bgImage;
            {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/com/fms/resources/Login Panel Image.png"));
                    bgImage = icon.getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                    bgImage = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    double scaleW = (double) getWidth() / bgImage.getWidth(null);
                    double scaleH = (double) getHeight() / bgImage.getHeight(null);
                    double scale = Math.max(scaleW, scaleH);
                    int w = (int) (bgImage.getWidth(null) * scale);
                    int h = (int) (bgImage.getHeight(null) * scale);
                    g2.drawImage(bgImage, (getWidth() - w) / 2, (getHeight() - h) / 2, w, h, this);
                    g2.dispose();
                } else {
                    Graphics2D g2 = (Graphics2D) g.create();
                    GradientPaint gp = new GradientPaint(0, 0, StyleTheme.PRIMARY_COLOR, 0, getHeight(),
                            new Color(30, 58, 138));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            }
        };
    }

    private JPanel createRightPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 28));
        title.setForeground(StyleTheme.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(title);

        JLabel subtitle = new JLabel("Sign in to access your dashboard");
        subtitle.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 14));
        subtitle.setForeground(StyleTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(subtitle);
        form.add(Box.createVerticalStrut(20));

        JLabel userLbl = createLabel("Username");
        form.add(userLbl);
        form.add(Box.createVerticalStrut(5));
        userField = createTextField("Enter your username");
        form.add(userField);
        userErrorLabel = createErrorLabel();
        form.add(userErrorLabel);
        form.add(Box.createVerticalStrut(5));

        JLabel passLbl = createLabel("Password");
        form.add(passLbl);
        form.add(Box.createVerticalStrut(5));
        passField = createPasswordField("Enter your password");
        form.add(passField);
        passErrorLabel = createErrorLabel();
        form.add(passErrorLabel);
        form.add(Box.createVerticalStrut(15));

        JButton loginBtn = createPrimaryButton("Sign In");
        loginBtn.addActionListener(e -> login());
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(10));

        JLabel orLabel = new JLabel("OR");
        orLabel.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 12));
        orLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(orLabel);
        form.add(Box.createVerticalStrut(10));

        JButton registerBtn = createSecondaryButton("Create Account");
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
        form.add(registerBtn);
        form.add(Box.createVerticalStrut(15));

        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        forgotPanel.setOpaque(false);
        forgotPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel forgotLbl = new JLabel("Forgot password?");
        forgotLbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 13));
        forgotLbl.setForeground(StyleTheme.TEXT_SECONDARY);

        JButton forgotBtn = new JButton("Reset");
        forgotBtn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 13));
        forgotBtn.setForeground(StyleTheme.PRIMARY_COLOR);
        forgotBtn.setBorderPainted(false);
        forgotBtn.setContentAreaFilled(false);
        forgotBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotBtn.addActionListener(e -> forgotPassword());

        JLabel pipeLbl = new JLabel(" | ");
        pipeLbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 13));
        pipeLbl.setForeground(StyleTheme.TEXT_SECONDARY);

        JButton findUserBtn = new JButton("Find Username");
        findUserBtn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 13));
        findUserBtn.setForeground(StyleTheme.PRIMARY_COLOR);
        findUserBtn.setBorderPainted(false);
        findUserBtn.setContentAreaFilled(false);
        findUserBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        findUserBtn.addActionListener(e -> forgotUsername());

        forgotPanel.add(forgotLbl);
        forgotPanel.add(forgotBtn);
        forgotPanel.add(pipeLbl);
        forgotPanel.add(findUserBtn);
        form.add(forgotPanel);

        wrapper.add(form);
        rootPane.setDefaultButton(loginBtn);
        return wrapper;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 14));
        lbl.setForeground(StyleTheme.TEXT_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JLabel createErrorLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 12));
        lbl.setForeground(StyleTheme.DANGER_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                if (getText().isEmpty() && !hasFocus()) {
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
                g2.setColor(hasFocus() ? StyleTheme.PRIMARY_COLOR : new Color(180, 180, 180));
                float stroke = hasFocus() ? StyleTheme.LOGIN_INPUT_BORDER_FOCUS_THICKNESS
                        : StyleTheme.LOGIN_INPUT_BORDER_THICKNESS;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 12, 12);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 14));
        field.setForeground(StyleTheme.TEXT_COLOR);
        field.setCaretColor(StyleTheme.TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        field.setPreferredSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                if (getPassword().length == 0 && !hasFocus()) {
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
                g2.setColor(hasFocus() ? StyleTheme.PRIMARY_COLOR : new Color(180, 180, 180));
                float stroke = hasFocus() ? StyleTheme.LOGIN_INPUT_BORDER_FOCUS_THICKNESS
                        : StyleTheme.LOGIN_INPUT_BORDER_THICKNESS;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 12, 12);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 14));
        field.setForeground(StyleTheme.TEXT_COLOR);
        field.setCaretColor(StyleTheme.TEXT_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        field.setPreferredSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed() ? StyleTheme.PRIMARY_COLOR.darker()
                        : getModel().isRollover() ? StyleTheme.PRIMARY_COLOR.brighter() : StyleTheme.PRIMARY_COLOR;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMinimumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setPreferredSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setMaximumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                g2.setColor(new Color(200, 200, 200));
                float stroke = StyleTheme.LOGIN_INPUT_BORDER_THICKNESS;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 10, 10);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 15));
        btn.setForeground(StyleTheme.TEXT_COLOR);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMinimumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setPreferredSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setMaximumSize(new Dimension(FORM_WIDTH, FIELD_HEIGHT));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void login() {
        userErrorLabel.setText(" ");
        passErrorLabel.setText(" ");

        String username = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (username.isEmpty()) {
            userErrorLabel.setText("Username is required");
            return;
        }
        if (password.isEmpty()) {
            passErrorLabel.setText("Password is required");
            return;
        }

        IUserDAO dao = DAOFactory.getUserDAO();
        try {
            User user = dao.loginUser(username, password);
            if (user != null) {
                if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new CustomerDashboard(user).setVisible(true);
                }
                dispose();
            } else {
                passErrorLabel.setText("Invalid credentials");
            }
        } catch (com.fms.exception.FMSException e) {
            e.printStackTrace();
            ModernDialog.showError(this, e.getMessage());
        }
    }

    private void forgotPassword() {
        String username = JOptionPane.showInputDialog(this, "Enter your Username:", "Password Recovery",
                JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty())
            return;
        username = username.trim();

        IUserDAO dao = DAOFactory.getUserDAO();
        try {
            User user = dao.getUserByUsername(username);
            if (user == null) {
                ModernDialog.showError(this, "User not found!");
                return;
            }

            String email = user.getEmail();
            if (email == null || email.isEmpty()) {
                ModernDialog.showError(this, "No email registered.");
                return;
            }

            String otp = ModernDialog.showOTPInput(this, "Verify Identity",
                    "Enter the 6-digit code sent to " + maskEmail(email) + "<br>(Mock: Enter 000000)");
            if (otp == null || !"000000".equals(otp)) {
                if (otp != null)
                    ModernDialog.showError(this, "Invalid OTP.");
                return;
            }

            String newPass = JOptionPane.showInputDialog(this, "Enter New Password:", "Reset Password",
                    JOptionPane.QUESTION_MESSAGE);
            if (newPass == null)
                return;

            String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
            if (!newPass.matches(pattern)) {
                ModernDialog.showWarning(this, "Password must be 6+ chars with letter, number & symbol");
                return;
            }

            if (dao.updatePassword(username, newPass)) {
                ModernDialog.showSuccess(this, "Password reset! Please login.");
            } else {
                ModernDialog.showError(this, "Reset failed.");
            }
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error: " + e.getMessage());
        }
    }

    private String maskEmail(String email) {
        int at = email.indexOf("@");
        if (at <= 3)
            return email.charAt(0) + "***" + email.substring(at);
        return email.substring(0, 3) + "***" + email.substring(at);
    }

    private void forgotUsername() {
        String email = JOptionPane.showInputDialog(this, "Enter your registered Email:", "Find Username",
                JOptionPane.QUESTION_MESSAGE);
        if (email == null || email.trim().isEmpty())
            return;
        email = email.trim();

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            ModernDialog.showWarning(this, "Invalid email format.");
            return;
        }

        IUserDAO dao = DAOFactory.getUserDAO();
        try {
            User user = dao.getUserByEmail(email);
            if (user == null) {
                ModernDialog.showError(this, "Email not found in system.");
                return;
            }

            String otp = ModernDialog.showOTPInput(this, "Verify Email",
                    "Enter the 6-digit code sent to " + maskEmail(email) + "<br>(Mock: Enter 000000)");
            if (otp == null || !"000000".equals(otp)) {
                if (otp != null)
                    ModernDialog.showError(this, "Invalid OTP.");
                return;
            }

            ModernDialog.showSuccess(this, "Your username is: " + user.getUsername());
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error: " + e.getMessage());
        }
    }
}
