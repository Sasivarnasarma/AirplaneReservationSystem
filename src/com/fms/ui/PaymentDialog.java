package com.fms.ui;

import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {
    private boolean paymentSuccess = false;
    private JTextField cardField, nameField, cvvField;
    private JComboBox<String> monthBox, yearBox;

    public PaymentDialog(JFrame parent, double amount) {
        super(parent, "Payment Gateway", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        JLabel titleLbl = new JLabel("Total Amount");
        titleLbl.setFont(StyleTheme.FONT_REGULAR);
        titleLbl.setForeground(Color.GRAY);
        titleLbl.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLbl, gbc);

        gbc.gridy++;
        JLabel amountLbl = new JLabel(String.format("LKR %.2f", amount));
        amountLbl.setFont(new Font("SansSerif", Font.BOLD, 24));
        amountLbl.setForeground(StyleTheme.PRIMARY_COLOR);
        amountLbl.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(amountLbl, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 2, 0);

        mainPanel.add(StyleTheme.createLabel("Card Holder Name"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        nameField = StyleTheme.createModernTextField("Name on Card");
        mainPanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 2, 0);
        mainPanel.add(StyleTheme.createLabel("Card Number"), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardField = StyleTheme.createModernTextField("16-digit Card Number");
        mainPanel.add(cardField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        JPanel rowPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        rowPanel.setOpaque(false);

        JPanel expiryPanel = new JPanel(new BorderLayout());
        expiryPanel.setOpaque(false);
        JLabel expLbl = StyleTheme.createLabel("Expiry");
        expLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        expiryPanel.add(expLbl, BorderLayout.NORTH);

        JPanel pickerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        pickerPanel.setOpaque(false);

        String[] months = new String[12];
        for (int i = 0; i < 12; i++)
            months[i] = String.format("%02d", i + 1);
        monthBox = new JComboBox<>(months);

        int currentYear = java.time.Year.now().getValue();
        String[] years = new String[15];
        for (int i = 0; i < 15; i++)
            years[i] = String.valueOf(currentYear + i);
        yearBox = new JComboBox<>(years);

        monthBox.setBackground(Color.WHITE);
        yearBox.setBackground(Color.WHITE);
        monthBox.setFont(StyleTheme.FONT_REGULAR);
        yearBox.setFont(StyleTheme.FONT_REGULAR);
        ((JLabel) monthBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        ((JLabel) yearBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);

        pickerPanel.add(monthBox);
        pickerPanel.add(yearBox);
        expiryPanel.add(pickerPanel, BorderLayout.CENTER);

        JPanel cvvPanel = new JPanel(new BorderLayout());
        cvvPanel.setOpaque(false);
        JLabel cvvLbl = StyleTheme.createLabel("CVV");
        cvvLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
        cvvPanel.add(cvvLbl, BorderLayout.NORTH);
        cvvField = StyleTheme.createModernTextField("3-digit CVV");
        cvvPanel.add(cvvField, BorderLayout.CENTER);

        rowPanel.add(expiryPanel);
        rowPanel.add(cvvPanel);
        mainPanel.add(rowPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 5, 0);
        JButton payBtn = StyleTheme.createButton("Pay Now");
        payBtn.addActionListener(e -> processPayment());
        mainPanel.add(payBtn, gbc);

        JButton cancelBtn = StyleTheme.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 0, 0);
        mainPanel.add(cancelBtn, gbc);

        add(mainPanel);
    }

    private void processPayment() {
        String name = nameField.getText().trim();
        String card = cardField.getText().trim();
        String cvv = cvvField.getText().trim();

        if (name.isEmpty() || card.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!card.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "Invalid Card Number (16 digits required)", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cvv.matches("\\d{3}")) {
            JOptionPane.showMessageDialog(this, "Invalid CVV (3 digits required)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedMonth = Integer.parseInt((String) monthBox.getSelectedItem());
        int selectedYear = Integer.parseInt((String) yearBox.getSelectedItem());

        java.time.YearMonth currentYM = java.time.YearMonth.now();
        java.time.YearMonth selectedYM = java.time.YearMonth.of(selectedYear, selectedMonth);

        if (selectedYM.isBefore(currentYM)) {
            JOptionPane.showMessageDialog(this, "Card Expiry Date cannot be in the past!", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog processing = new JDialog(this, "Processing", true);
        processing.setSize(200, 100);
        processing.setLayout(new GridBagLayout());
        processing.setLocationRelativeTo(this);
        processing.add(new JLabel("Processing Payment..."));

        Timer timer = new Timer(1500, e -> {
            processing.dispose();
            paymentSuccess = true;
            JOptionPane.showMessageDialog(this, "Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
        processing.setVisible(true);
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }
}
