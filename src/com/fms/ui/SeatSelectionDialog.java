package com.fms.ui;

import com.fms.dao.DAOFactory;
import com.fms.dao.IFlightDAO;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SeatSelectionDialog extends JDialog {
    private List<String> selectedSeats = new ArrayList<>();
    private List<String> bookedSeats;
    private int maxSeats;
    private boolean confirmed = false;
    private JLabel statusLabel;

    public SeatSelectionDialog(Frame parent, int flightId, int maxSeats) {
        super(parent, "Select Seats", true);
        this.maxSeats = maxSeats;
        try {
            this.bookedSeats = DAOFactory.getBookingDAO().getBookedSeats(flightId);
        } catch (com.fms.exception.FMSException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading booked seats: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            this.bookedSeats = new ArrayList<>();
        }

        setSize(400, 600);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(StyleTheme.BG_COLOR_MAIN);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(StyleTheme.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        statusLabel = new JLabel("Select " + maxSeats + " seats");
        statusLabel.setFont(StyleTheme.FONT_SUBTITLE);
        headerPanel.add(statusLabel);
        add(headerPanel, BorderLayout.NORTH);

        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleTheme.BG_COLOR_MAIN);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        legendPanel.setOpaque(false);
        legendPanel.add(createLegendItem(Color.LIGHT_GRAY, "Available"));
        legendPanel.add(createLegendItem(StyleTheme.PRIMARY_COLOR, "Selected"));
        legendPanel.add(createLegendItem(new Color(231, 76, 60), "Booked"));
        contentPanel.add(legendPanel, BorderLayout.NORTH);

        IFlightDAO flightDAO = DAOFactory.getFlightDAO();
        com.fms.model.Flight flight = null;
        try {
            flight = flightDAO.getAllFlights().stream()
                    .filter(f -> f.getFlightId() == flightId).findFirst().orElse(null);
        } catch (com.fms.exception.FMSException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading flight details: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        int totalCapacity = 60;
        if (flight != null) {
            totalCapacity = flight.getSeatsAvailable() + bookedSeats.size();
        }

        int columns = 6;
        int rows = (int) Math.ceil((double) totalCapacity / columns);

        JPanel gridPanel = new JPanel(new GridLayout(rows, columns, 8, 8));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        char[] cols = { 'A', 'B', 'C', 'D', 'E', 'F' };
        int seatCounter = 0;

        for (int row = 1; row <= rows; row++) {
            for (char col : cols) {
                seatCounter++;
                if (seatCounter > totalCapacity) {
                    gridPanel.add(new JLabel(""));
                    continue;
                }

                String seatNum = row + "" + col;
                JButton btn = new JButton(seatNum);
                btn.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 10));
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.putClientProperty("JButton.buttonType", "roundRect");

                if (bookedSeats.contains(seatNum)) {
                    btn.setBackground(new Color(231, 76, 60));
                    btn.setForeground(Color.WHITE);
                    btn.setToolTipText("Already Booked");
                } else {
                    btn.setBackground(Color.LIGHT_GRAY);
                    btn.setForeground(Color.BLACK);
                    btn.addActionListener(e -> toggleSeat(btn, seatNum));
                }

                gridPanel.add(btn);
            }
        }
        contentPanel.add(gridPanel, BorderLayout.CENTER);
        add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(StyleTheme.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JButton confirmBtn = StyleTheme.createButton("Confirm Selection");
        JButton cancelBtn = StyleTheme.createSecondaryButton("Cancel");

        confirmBtn.addActionListener(e -> {
            if (selectedSeats.size() == maxSeats) {
                confirmed = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Please select exactly " + maxSeats + " seats.",
                        "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(cancelBtn);
        btnPanel.add(Box.createHorizontalStrut(10));
        btnPanel.add(confirmBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createLegendItem(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setOpaque(false);

        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel l = new JLabel(text);
        l.setFont(StyleTheme.FONT_REGULAR);

        p.add(colorBox);
        p.add(l);
        return p;
    }

    private void toggleSeat(JButton btn, String seatNum) {
        if (selectedSeats.contains(seatNum)) {
            selectedSeats.remove(seatNum);
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.BLACK);
        } else {
            if (selectedSeats.size() < maxSeats) {
                selectedSeats.add(seatNum);
                btn.setBackground(StyleTheme.PRIMARY_COLOR);
                btn.setForeground(Color.WHITE);
            } else {
                JOptionPane.showMessageDialog(this, "You can only select " + maxSeats + " seats.", "Max Seats Reached",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        statusLabel.setText("Select " + maxSeats + " seats (" + selectedSeats.size() + " selected)");
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getSelectedSeatsString() {
        return String.join(",", selectedSeats);
    }
}
