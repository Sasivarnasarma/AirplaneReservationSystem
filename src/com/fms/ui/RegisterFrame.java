package com.fms.ui;

import com.fms.dao.DAOFactory;
import com.fms.dao.IUserDAO;
import com.fms.model.Customer;
import com.fms.model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField userField, firstNameField, lastNameField, emailField, nicField, passportField;
    private JPasswordField passField, confirmPassField;
    private JLabel firstNameError, lastNameError, emailError, nicError, passportError, userError, passError,
            confirmPassError;

    private static final int FIELD_HEIGHT = 40;
    private static final int BUTTON_HEIGHT = 44;
    private static final int FORM_WIDTH = 360;
    private static final int HALF_WIDTH = 170;

    public RegisterFrame() {
        setTitle("IM Airlines - Create Account");
        setSize(1000, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JLabel title = new JLabel("Create an account");
        title.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 26));
        title.setForeground(StyleTheme.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.add(title);

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        subPanel.setOpaque(false);
        JLabel subLbl = new JLabel("Already have an account?");
        subLbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 13));
        subLbl.setForeground(StyleTheme.TEXT_SECONDARY);
        JButton loginLink = new JButton("Log in");
        loginLink.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 13));
        loginLink.setForeground(StyleTheme.PRIMARY_COLOR);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        subPanel.add(subLbl);
        subPanel.add(loginLink);
        form.add(subPanel);
        form.add(Box.createVerticalStrut(25));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(FORM_WIDTH + 30, 65));

        JPanel fnPanel = createFieldGroup("First Name", "Tharindu", HALF_WIDTH);
        firstNameField = (JTextField) fnPanel.getClientProperty("field");
        firstNameError = (JLabel) fnPanel.getClientProperty("error");
        row1.add(fnPanel);

        JPanel lnPanel = createFieldGroup("Last Name", "Bandara", HALF_WIDTH);
        lastNameField = (JTextField) lnPanel.getClientProperty("field");
        lastNameError = (JLabel) lnPanel.getClientProperty("error");
        row1.add(lnPanel);

        form.add(row1);

        JPanel emailPanel = createFieldGroup("Email Address", "you@imairlines.lk", FORM_WIDTH);
        emailField = (JTextField) emailPanel.getClientProperty("field");
        emailError = (JLabel) emailPanel.getClientProperty("error");
        form.add(emailPanel);

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        row2.setOpaque(false);
        row2.setMaximumSize(new Dimension(FORM_WIDTH + 30, 65));

        JPanel nicPanel = createFieldGroup("NIC Number", "9-12 characters", HALF_WIDTH);
        nicField = (JTextField) nicPanel.getClientProperty("field");
        nicError = (JLabel) nicPanel.getClientProperty("error");
        row2.add(nicPanel);

        JPanel passportPanel = createFieldGroup("Passport No", "6-9 characters", HALF_WIDTH);
        passportField = (JTextField) passportPanel.getClientProperty("field");
        passportError = (JLabel) passportPanel.getClientProperty("error");
        row2.add(passportPanel);

        form.add(row2);

        JPanel userPanel = createFieldGroup("Username", "Choose a username", FORM_WIDTH);
        userField = (JTextField) userPanel.getClientProperty("field");
        userError = (JLabel) userPanel.getClientProperty("error");
        form.add(userPanel);

        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        row3.setOpaque(false);
        row3.setMaximumSize(new Dimension(FORM_WIDTH + 30, 65));

        JPanel passPanel = createPasswordGroup("Password", "6+ with #@!", HALF_WIDTH);
        passField = (JPasswordField) passPanel.getClientProperty("field");
        passError = (JLabel) passPanel.getClientProperty("error");
        row3.add(passPanel);

        JPanel confirmPanel = createPasswordGroup("Confirm Password", "Re-enter", HALF_WIDTH);
        confirmPassField = (JPasswordField) confirmPanel.getClientProperty("field");
        confirmPassError = (JLabel) confirmPanel.getClientProperty("error");
        row3.add(confirmPanel);

        form.add(row3);
        form.add(Box.createVerticalStrut(20));

        JButton registerBtn = createPrimaryButton("Create Account");
        registerBtn.addActionListener(e -> register());
        form.add(registerBtn);

        wrapper.add(form);
        return wrapper;
    }

    private JPanel createFieldGroup(String label, String placeholder, int width) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 12));
        lbl.setForeground(StyleTheme.TEXT_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));

        JTextField field = createTextField(placeholder, width);
        panel.add(field);

        JLabel err = new JLabel(" ");
        err.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 11));
        err.setForeground(StyleTheme.DANGER_COLOR);
        err.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(err);

        panel.putClientProperty("field", field);
        panel.putClientProperty("error", err);
        return panel;
    }

    private JPanel createPasswordGroup(String label, String placeholder, int width) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 12));
        lbl.setForeground(StyleTheme.TEXT_COLOR);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));

        JPasswordField field = createPasswordField(placeholder, width);
        panel.add(field);

        JLabel err = new JLabel(" ");
        err.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 11));
        err.setForeground(StyleTheme.DANGER_COLOR);
        err.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(err);

        panel.putClientProperty("field", field);
        panel.putClientProperty("error", err);
        return panel;
    }

    private JTextField createTextField(String placeholder, int width) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g3.setColor(new Color(150, 150, 150));
                    g3.setFont(getFont());
                    FontMetrics fm = g3.getFontMetrics();
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g3.drawString(placeholder, getInsets().left, y);
                    g3.dispose();
                }
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? StyleTheme.PRIMARY_COLOR : new Color(180, 180, 180));
                float stroke = hasFocus() ? StyleTheme.REGISTER_INPUT_BORDER_FOCUS_THICKNESS
                        : StyleTheme.REGISTER_INPUT_BORDER_THICKNESS;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 10, 10);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 13));
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        field.setPreferredSize(new Dimension(width, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(width, FIELD_HEIGHT));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder, int width) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g3.setColor(new Color(150, 150, 150));
                    g3.setFont(getFont());
                    FontMetrics fm = g3.getFontMetrics();
                    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                    g3.drawString(placeholder, getInsets().left, y);
                    g3.dispose();
                }
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? StyleTheme.PRIMARY_COLOR : new Color(180, 180, 180));
                float stroke = hasFocus() ? StyleTheme.REGISTER_INPUT_BORDER_FOCUS_THICKNESS
                        : StyleTheme.REGISTER_INPUT_BORDER_THICKNESS;
                g2.setStroke(new BasicStroke(stroke));
                int s = (int) Math.ceil(stroke / 2);
                g2.drawRoundRect(s, s, getWidth() - 2 * s - 1, getHeight() - 2 * s - 1, 10, 10);
                g2.dispose();
            }
        };
        field.setOpaque(false);
        field.setFont(new Font(StyleTheme.FONT_FAMILY, Font.PLAIN, 13));
        field.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        field.setPreferredSize(new Dimension(width, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(width, FIELD_HEIGHT));
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
        btn.setPreferredSize(new Dimension(FORM_WIDTH, BUTTON_HEIGHT));
        btn.setMaximumSize(new Dimension(FORM_WIDTH, BUTTON_HEIGHT));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void clearAllErrors() {
        firstNameError.setText(" ");
        lastNameError.setText(" ");
        emailError.setText(" ");
        nicError.setText(" ");
        passportError.setText(" ");
        userError.setText(" ");
        passError.setText(" ");
        confirmPassError.setText(" ");
    }

    private void register() {
        clearAllErrors();

        String fName = firstNameField.getText().trim();
        String lName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String nic = nicField.getText().trim();
        String passport = passportField.getText().trim();
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());

        boolean err = false;

        if (fName.isEmpty()) {
            firstNameError.setText("Required");
            err = true;
        }
        if (lName.isEmpty()) {
            lastNameError.setText("Required");
            err = true;
        }
        if (email.isEmpty()) {
            emailError.setText("Required");
            err = true;
        } else if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            emailError.setText("Invalid format");
            err = true;
        }
        if (nic.isEmpty()) {
            nicError.setText("Required");
            err = true;
        } else if (!nic.matches("^[a-zA-Z0-9]{9,12}$")) {
            nicError.setText("9-12 chars");
            err = true;
        }
        if (passport.isEmpty()) {
            passportError.setText("Required");
            err = true;
        } else if (!passport.matches("^[a-zA-Z0-9]{6,9}$")) {
            passportError.setText("6-9 chars");
            err = true;
        }
        if (username.isEmpty()) {
            userError.setText("Required");
            err = true;
        } else if (!username.matches("[a-zA-Z0-9_]+")) {
            userError.setText("Invalid");
            err = true;
        }
        if (password.isEmpty()) {
            passError.setText("Required");
            err = true;
        } else {
            String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
            if (!password.matches(pattern)) {
                passError.setText("Weak");
                err = true;
            }
        }
        if (confirmPass.isEmpty()) {
            confirmPassError.setText("Required");
            err = true;
        } else if (!password.equals(confirmPass)) {
            confirmPassError.setText("Mismatch");
            err = true;
        }

        if (err)
            return;

        String otp = ModernDialog.showOTPInput(this, "Verify Email",
                "Enter code sent to " + email + "<br>(Mock: 000000)");
        if (otp == null || !"000000".equals(otp)) {
            if (otp != null)
                ModernDialog.showError(this, "Invalid OTP!");
            return;
        }

        IUserDAO dao = DAOFactory.getUserDAO();
        try {
            if (dao.getUserByUsername(username) != null) {
                userError.setText("Taken");
                return;
            }
            if (dao.getUserByEmail(email) != null) {
                emailError.setText("Registered");
                return;
            }

            User newUser = new Customer(username, password, fName, lName, email, nic, passport);
            if (dao.registerUser(newUser)) {
                ModernDialog.showSuccess(this, "Success! Please login.");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                ModernDialog.showError(this, "Registration failed.");
            }
        } catch (com.fms.exception.FMSException e) {
            e.printStackTrace();
            ModernDialog.showError(this, "Error: " + e.getMessage());
        }
    }
}
