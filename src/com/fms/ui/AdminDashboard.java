package com.fms.ui;

import com.fms.dao.IBookingDAO;
import com.fms.dao.IFlightDAO;
import com.fms.dao.DAOFactory;
import com.fms.dao.IUserDAO;
import com.fms.model.Booking;
import com.fms.model.Flight;
import com.fms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User user;
    private IFlightDAO flightDAO;
    private IBookingDAO bookingDAO;
    private IUserDAO userDAO;
    private JTable flightTable, bookingTable, userTable;
    private DefaultTableModel flightModel, bookingModel, userModel, recentModel;

    private SidebarButton dashboardBtn, flightsBtn, bookingsBtn, usersBtn;
    private JButton logoutBtn;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private StatCard totalBookingsCard, totalRevenueCard, activeFlightsCard, totalUsersCard;

    private static final int LOGO_HEIGHT = 55;

    public AdminDashboard(User user) {
        this.user = user;
        this.flightDAO = DAOFactory.getFlightDAO();
        this.bookingDAO = DAOFactory.getBookingDAO();
        this.userDAO = DAOFactory.getUserDAO();

        setTitle("IM Airlines - Admin Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(StyleTheme.BG_COLOR_MAIN);

        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(StyleTheme.BG_COLOR_MAIN);

        contentPanel.add(createDashboardOverviewPanel(), "dashboard");
        contentPanel.add(createFlightPanel(), "flights");
        contentPanel.add(createBookingPanel(), "bookings");
        contentPanel.add(createUserPanel(), "users");

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        showPanel("dashboard", dashboardBtn);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setPreferredSize(new Dimension(StyleTheme.SIDEBAR_WIDTH, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, StyleTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 16, 20, 16)));

        JPanel brandPanel = new JPanel();
        brandPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brandPanel.setOpaque(false);
        brandPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        brandPanel.setMaximumSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, 60));

        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/com/fms/resources/IMAirlines - Dark Logo.png"));
            int origWidth = logoIcon.getIconWidth();
            int origHeight = logoIcon.getIconHeight();
            int targetWidth = (int) ((double) origWidth / origHeight * LOGO_HEIGHT);
            Image scaledLogo = logoIcon.getImage().getScaledInstance(targetWidth, LOGO_HEIGHT, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            brandPanel.add(logoLabel);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel brandLabel = new JLabel("IM Airlines");
            brandLabel.setFont(new Font(StyleTheme.FONT_FAMILY, Font.BOLD, 18));
            brandLabel.setForeground(StyleTheme.TEXT_COLOR);
            brandPanel.add(brandLabel);
        }

        sidebar.add(brandPanel);
        sidebar.add(Box.createVerticalStrut(25));

        dashboardBtn = new SidebarButton("Dashboard", "/com/fms/resources/icons/dashboard.svg");
        flightsBtn = new SidebarButton("Flights", "/com/fms/resources/icons/flights.svg");
        bookingsBtn = new SidebarButton("Bookings", "/com/fms/resources/icons/bookings.svg");
        usersBtn = new SidebarButton("Users", "/com/fms/resources/icons/clients.svg");

        dashboardBtn.addActionListener(e -> showPanel("dashboard", dashboardBtn));
        flightsBtn.addActionListener(e -> showPanel("flights", flightsBtn));
        bookingsBtn.addActionListener(e -> showPanel("bookings", bookingsBtn));
        usersBtn.addActionListener(e -> showPanel("users", usersBtn));

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(flightsBtn);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(bookingsBtn);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(usersBtn);

        sidebar.add(Box.createVerticalGlue());

        JSeparator separator = new JSeparator();
        separator.setForeground(StyleTheme.BORDER_COLOR);
        separator.setBackground(StyleTheme.BORDER_COLOR);
        separator.setMaximumSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(separator);
        sidebar.add(Box.createVerticalStrut(16));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoPanel.setMaximumSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, 50));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

        JLabel userNameLabel = new JLabel(user.getFullName());
        userNameLabel.setFont(StyleTheme.FONT_BOLD);
        userNameLabel.setForeground(StyleTheme.TEXT_COLOR);
        userNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userRoleLabel = new JLabel("Administrator");
        userRoleLabel.setFont(StyleTheme.FONT_SMALL);
        userRoleLabel.setForeground(StyleTheme.TEXT_SECONDARY);
        userRoleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoPanel.add(userNameLabel);
        userInfoPanel.add(Box.createVerticalStrut(2));
        userInfoPanel.add(userRoleLabel);

        sidebar.add(userInfoPanel);
        sidebar.add(Box.createVerticalStrut(12));

        logoutBtn = StyleTheme.createDangerButton("Logout");
        logoutBtn.setMaximumSize(new Dimension(StyleTheme.SIDEBAR_WIDTH - 32, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private void showPanel(String panelName, SidebarButton selectedBtn) {
        cardLayout.show(contentPanel, panelName);

        dashboardBtn.setSelected(dashboardBtn == selectedBtn);
        flightsBtn.setSelected(flightsBtn == selectedBtn);
        bookingsBtn.setSelected(bookingsBtn == selectedBtn);
        usersBtn.setSelected(usersBtn == selectedBtn);

        switch (panelName) {
            case "dashboard":
                refreshDashboardStats();
                if (recentModel != null) {
                    loadRecentBookings(recentModel);
                }
                break;
            case "flights":
                loadFlights();
                break;
            case "bookings":
                loadBookings();
                break;
            case "users":
                loadUsers();
                break;
        }
    }

    private JPanel createDashboardOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleTheme.BG_COLOR_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 24, 0));

        JLabel titleLabel = StyleTheme.createTitleLabel("Dashboard Overview");
        JLabel subtitleLabel = new JLabel("Welcome back, " + user.getFirstName() + "!");
        subtitleLabel.setFont(StyleTheme.FONT_REGULAR);
        subtitleLabel.setForeground(StyleTheme.TEXT_SECONDARY);

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setOpaque(false);
        titleSection.add(titleLabel);
        titleSection.add(Box.createVerticalStrut(4));
        titleSection.add(subtitleLabel);

        headerPanel.add(titleSection, BorderLayout.WEST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setOpaque(false);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        statsPanel.setPreferredSize(new Dimension(100, 100));

        totalBookingsCard = StatCard.create("Total Bookings", "0", "", StyleTheme.PRIMARY_COLOR);
        totalRevenueCard = StatCard.create("Total Revenue", "LKR 0", "", StyleTheme.SUCCESS_COLOR);
        activeFlightsCard = StatCard.create("Active Flights", "0", "", StyleTheme.WARNING_COLOR);
        totalUsersCard = StatCard.create("Total Users", "0", "", StyleTheme.INFO_COLOR);

        statsPanel.add(totalBookingsCard);
        statsPanel.add(totalRevenueCard);
        statsPanel.add(activeFlightsCard);
        statsPanel.add(totalUsersCard);

        contentArea.add(statsPanel);
        contentArea.add(Box.createVerticalStrut(28));

        JPanel recentBookingsCard = StyleTheme.createCardPanel();
        recentBookingsCard.setLayout(new BorderLayout());
        recentBookingsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel recentTitle = StyleTheme.createSubtitleLabel("Recent Bookings");
        recentTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        recentBookingsCard.add(recentTitle, BorderLayout.NORTH);

        String[] cols = { "Booking ID", "User", "Flight", "Route", "Date", "Status" };
        recentModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable recentTable = new JTable(recentModel);
        StyleTheme.styleTable(recentTable);
        recentTable.getColumnModel().getColumn(5).setCellRenderer(new PillStatusRenderer());

        JScrollPane scrollPane = new JScrollPane(recentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        recentBookingsCard.add(scrollPane, BorderLayout.CENTER);

        contentArea.add(recentBookingsCard);

        panel.add(contentArea, BorderLayout.CENTER);

        refreshDashboardStats();
        loadRecentBookings(recentModel);

        return panel;
    }

    private void refreshDashboardStats() {
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            List<Flight> flights = flightDAO.getAllFlights();
            List<User> users = userDAO.getAllUsers();

            int totalBookings = bookings.size();
            double totalRevenue = 0;
            int activeFlights = 0;

            for (Booking b : bookings) {
                if ("CONFIRMED".equals(b.getStatus())) {
                    totalRevenue += b.getTotalPrice();
                }
            }

            for (Flight f : flights) {
                String status = f.getStatus();
                if ("ON TIME".equals(status) || "BOARDING".equals(status) || "DELAYED".equals(status)) {
                    activeFlights++;
                }
            }

            totalBookingsCard.setValue(String.valueOf(totalBookings));
            totalRevenueCard.setValue("LKR " + String.format("%.2f", totalRevenue));
            activeFlightsCard.setValue(String.valueOf(activeFlights));
            totalUsersCard.setValue(String.valueOf(users.size()));

        } catch (com.fms.exception.FMSException e) {
        }
    }

    private void loadRecentBookings(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            int count = 0;
            for (Booking b : bookings) {
                if (count >= 5)
                    break;
                model.addRow(new Object[] {
                        b.getBookingId(),
                        b.getUsername(),
                        b.getFlightNumber(),
                        b.getSource() + " TO " + b.getDestination(),
                        b.getBookingDate(),
                        b.getStatus()
                });
                count++;
            }
        } catch (com.fms.exception.FMSException e) {
        }
    }

    private JPanel createFlightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleTheme.BG_COLOR_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = StyleTheme.createTitleLabel("Manage Flights");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton addBtn = StyleTheme.createButton("+ Add Flight");
        JButton refreshBtn = StyleTheme.createSecondaryButton("Refresh");

        addBtn.addActionListener(e -> showAddFlightDialog());
        refreshBtn.addActionListener(e -> loadFlights());

        actionPanel.add(refreshBtn);
        actionPanel.add(addBtn);
        headerPanel.add(actionPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tableCard = StyleTheme.createCardPanel();
        tableCard.setLayout(new BorderLayout());

        String[] cols = { "ID", "Number", "Source", "Destination", "Date", "Time", "Seats", "Price", "Status" };
        flightModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        flightTable = new JTable(flightModel);
        StyleTheme.styleTable(flightTable);
        flightTable.getColumnModel().getColumn(8).setCellRenderer(new PillStatusRenderer());
        loadFlights();

        JScrollPane scrollPane = new JScrollPane(flightTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setOpaque(false);

        JButton statusBtn = StyleTheme.createButton("Update Status");
        statusBtn.addActionListener(e -> updateFlightStatus());
        btnPanel.add(statusBtn);

        tableCard.add(btnPanel, BorderLayout.SOUTH);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    private void updateFlightStatus() {
        int row = flightTable.getSelectedRow();
        if (row == -1) {
            ModernDialog.showWarning(this, "Please select a flight first.");
            return;
        }
        int fId = (int) flightModel.getValueAt(row, 0);
        String currentStatus = (String) flightModel.getValueAt(row, 8);

        String[] options = { "ON TIME", "DELAYED", "CANCELLED", "BOARDING", "FINISHED" };
        String newStatus = ModernDialog.showSelection(this, "Update Flight Status", "Select new status:", options,
                currentStatus);

        if (newStatus != null) {
            try {
                if (flightDAO.updateFlightStatus(fId, newStatus)) {
                    if ("CANCELLED".equalsIgnoreCase(newStatus)) {
                        List<Booking> bookings = bookingDAO.getBookingsByFlightId(fId);
                        java.util.Set<String> notifiedUsers = new java.util.HashSet<>();
                        boolean hasBookings = false;

                        for (Booking b : bookings) {
                            if (!"CANCELLED".equalsIgnoreCase(b.getStatus())) {
                                bookingDAO.updateBookingStatus(b.getBookingId(), "CANCELLED");
                                User u = userDAO.getUserByUsername(b.getUsername());
                                if (u != null) {
                                    notifiedUsers.add(u.getEmail() + " (User: " + u.getUsername() + ")");
                                    hasBookings = true;
                                }
                            }
                        }

                        if (hasBookings) {
                            StringBuilder sb = new StringBuilder();
                            for (String s : notifiedUsers) {
                                sb.append("• ").append(s).append("<br>");
                            }
                            ModernDialog.showInfo(this,
                                    "Flight cancelled. All associated bookings have been cancelled.<br><br>" +
                                            "Mock emails sent to:<br>" + sb.toString());
                        }
                    }
                    ModernDialog.showSuccess(this, "Flight status updated successfully!");
                    loadFlights();
                } else {
                    ModernDialog.showError(this, "Failed to update status.");
                }
            } catch (com.fms.exception.FMSException ex) {
                ModernDialog.showError(this, "Error: " + ex.getMessage());
            }
        }
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleTheme.BG_COLOR_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = StyleTheme.createTitleLabel("Manage Bookings");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = StyleTheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadBookings());
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tableCard = StyleTheme.createCardPanel();
        tableCard.setLayout(new BorderLayout());

        String[] cols = { "ID", "User", "Flight", "Source", "Destination", "Date", "Status", "Seats", "Total Cost" };
        bookingModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingModel);
        StyleTheme.styleTable(bookingTable);
        bookingTable.getColumnModel().getColumn(6).setCellRenderer(new PillStatusRenderer());

        bookingTable.getColumnModel().getColumn(0).setMaxWidth(60);
        bookingTable.getColumnModel().getColumn(0).setPreferredWidth(50);

        loadBookings();

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setOpaque(false);

        JButton cancelBtn = StyleTheme.createDangerButton("Cancel");
        cancelBtn.addActionListener(e -> updateBookingStatus("CANCELLED"));

        JButton approveBtn = StyleTheme.createSuccessButton("Approve");
        approveBtn.addActionListener(e -> updateBookingStatus("CONFIRMED"));

        btnPanel.add(cancelBtn);
        btnPanel.add(approveBtn);

        tableCard.add(btnPanel, BorderLayout.SOUTH);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(StyleTheme.BG_COLOR_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = StyleTheme.createTitleLabel("Registered Users");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshBtn = StyleTheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadUsers());
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel tableCard = StyleTheme.createCardPanel();
        tableCard.setLayout(new BorderLayout());

        String[] cols = { "User ID", "Username", "Role", "First Name", "Last Name", "Email", "NIC", "Passport" };
        userModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userModel);
        StyleTheme.styleTable(userTable);
        loadUsers();

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        btnPanel.setOpaque(false);

        JButton roleBtn = StyleTheme.createButton("Toggle Role");
        roleBtn.addActionListener(e -> toggleUserRole());
        btnPanel.add(roleBtn);

        tableCard.add(btnPanel, BorderLayout.SOUTH);
        panel.add(tableCard, BorderLayout.CENTER);

        return panel;
    }

    private void toggleUserRole() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            ModernDialog.showWarning(this, "Please select a user first.");
            return;
        }

        int userId = (int) userModel.getValueAt(row, 0);
        String currentRole = (String) userModel.getValueAt(row, 2);
        String username = (String) userModel.getValueAt(row, 1);

        if (username.equals(user.getUsername())) {
            ModernDialog.showWarning(this, "You cannot change your own role.");
            return;
        }

        String newRole = currentRole.equals("ADMIN") ? "CUSTOMER" : "ADMIN";

        boolean confirm = ModernDialog.showConfirm(this, "Confirm Role Change",
                "Change role of " + username + " to " + newRole + "?");

        if (confirm) {
            try {
                if (userDAO.updateUserRole(userId, newRole)) {
                    ModernDialog.showSuccess(this, "User role updated to " + newRole);
                    loadUsers();
                } else {
                    ModernDialog.showError(this, "Failed to update role.");
                }
            } catch (com.fms.exception.FMSException ex) {
                ModernDialog.showError(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void updateBookingStatus(String status) {
        int row = bookingTable.getSelectedRow();
        if (row == -1) {
            ModernDialog.showWarning(this, "Please select a booking first.");
            return;
        }
        int bookingId = (int) bookingModel.getValueAt(row, 0);
        String username = (String) bookingModel.getValueAt(row, 1);

        try {
            if (bookingDAO.updateBookingStatus(bookingId, status)) {
                User u = userDAO.getUserByUsername(username);
                String emailMsg = "";
                if (u != null) {
                    emailMsg = "<br><br>Mock email sent to: " + u.getEmail() + "<br>Subject: Booking " + status;
                }
                ModernDialog.showSuccess(this, "Booking updated to " + status + emailMsg);
                loadBookings();
            } else {
                ModernDialog.showError(this, "Failed to update booking.");
            }
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error: " + e.getMessage());
        }
    }

    private void loadFlights() {
        flightModel.setRowCount(0);
        try {
            List<Flight> flights = flightDAO.getAllFlights();
            for (Flight f : flights) {
                flightModel.addRow(new Object[] {
                        f.getFlightId(), f.getFlightNumber(), f.getSource(), f.getDestination(),
                        f.getDate(), f.getTime(), f.getSeatsAvailable(), "LKR " + f.getPrice(), f.getStatus()
                });
            }
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error loading flights: " + e.getMessage());
        }
    }

    private void loadBookings() {
        bookingModel.setRowCount(0);
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            for (Booking b : bookings) {
                bookingModel.addRow(new Object[] {
                        b.getBookingId(), b.getUsername(), b.getFlightNumber(), b.getSource(), b.getDestination(),
                        b.getBookingDate(), b.getStatus(), b.getSeatNumbers(),
                        String.format("LKR %.2f", b.getTotalPrice())
                });
            }
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error loading bookings: " + e.getMessage());
        }
    }

    private void loadUsers() {
        userModel.setRowCount(0);
        try {
            List<User> users = userDAO.getAllUsers();
            for (User u : users) {
                String nic = "-";
                String passport = "-";
                if (u instanceof com.fms.model.Customer) {
                    nic = ((com.fms.model.Customer) u).getNic();
                    passport = ((com.fms.model.Customer) u).getPassportNumber();
                }
                userModel.addRow(new Object[] {
                        u.getUserId(), u.getUsername(), u.getRole(),
                        u.getFirstName(), u.getLastName(), u.getEmail(), nic, passport
                });
            }
        } catch (com.fms.exception.FMSException e) {
            ModernDialog.showError(this, "Error loading users: " + e.getMessage());
        }
    }

    private void showAddFlightDialog() {
        JDialog dialog = new JDialog(this, "Add New Flight", true);
        dialog.setSize(500, 600);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, StyleTheme.BORDER_COLOR),
                BorderFactory.createEmptyBorder(25, 30, 20, 30)));

        JLabel headerTitle = new JLabel("Add New Flight");
        headerTitle.setFont(StyleTheme.FONT_SUBTITLE);
        headerTitle.setForeground(StyleTheme.TEXT_COLOR);
        headerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headerSubtitle = new JLabel("Enter the flight details below");
        headerSubtitle.setFont(StyleTheme.FONT_REGULAR);
        headerSubtitle.setForeground(StyleTheme.TEXT_SECONDARY);
        headerSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(headerTitle);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(headerSubtitle);

        dialog.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField numField = StyleTheme.createTextField();

        JTextField srcField = StyleTheme.createTextField();
        JTextField destField = StyleTheme.createTextField();

        JTextField dateField = StyleTheme.createTextField();
        dateField.setEditable(false);
        JButton dateBtn = StyleTheme.createSecondaryButton("Select");
        dateBtn.addActionListener(e -> {
            DatePicker dp = new DatePicker(this);
            String selectedDate = dp.setPickedDate();
            if (!selectedDate.isEmpty())
                dateField.setText(selectedDate);
        });
        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setOpaque(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        datePanel.add(dateBtn, BorderLayout.EAST);

        SpinnerDateModel timeModel = new SpinnerDateModel(new java.util.Date(), null, null,
                java.util.Calendar.HOUR_OF_DAY);
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setFont(StyleTheme.FONT_REGULAR);

        JTextField seatsField = StyleTheme.createTextField();
        seatsField.setText("60");
        JTextField priceField = StyleTheme.createTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(StyleTheme.createLabel("Flight Number:"), gbc);
        gbc.gridx = 1;
        formPanel.add(numField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Source:"), gbc);
        gbc.gridx = 1;
        formPanel.add(srcField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Destination:"), gbc);
        gbc.gridx = 1;
        formPanel.add(destField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Date:"), gbc);
        gbc.gridx = 1;
        formPanel.add(datePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Time:"), gbc);
        gbc.gridx = 1;
        formPanel.add(timeSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Total Seats:"), gbc);
        gbc.gridx = 1;
        formPanel.add(seatsField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(StyleTheme.createLabel("Price (LKR):"), gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton cancelBtn = StyleTheme.createSecondaryButton("Cancel");
        JButton saveBtn = StyleTheme.createButton("Save Flight");

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            try {
                String fNum = numField.getText().trim();
                String src = srcField.getText().trim();
                String dst = destField.getText().trim();
                String dateStr = dateField.getText().trim();
                java.util.Date utilTime = (java.util.Date) timeSpinner.getValue();
                String seatsStr = seatsField.getText().trim();
                String priceStr = priceField.getText().trim();

                if (fNum.isEmpty() || src.isEmpty() || dst.isEmpty() || dateStr.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required.");
                }
                if (src.equalsIgnoreCase(dst)) {
                    throw new IllegalArgumentException("Source and Destination cannot be the same.");
                }

                Date fDate = Date.valueOf(dateStr);
                Time fTime = new Time(utilTime.getTime());

                java.sql.Timestamp flightTimestamp = java.sql.Timestamp.valueOf(dateStr + " " + fTime.toString());
                if (flightTimestamp.before(new java.sql.Timestamp(System.currentTimeMillis()))) {
                    throw new IllegalArgumentException("Flight time must be in the future!");
                }

                int seats = Integer.parseInt(seatsStr);
                if (seats <= 0)
                    throw new IllegalArgumentException("Seats must be positive.");
                if (seats > 100)
                    throw new IllegalArgumentException("Maximum 100 seats allowed.");

                double price = Double.parseDouble(priceStr);
                if (price <= 0)
                    throw new IllegalArgumentException("Price must be positive.");

                Flight f = new Flight(fNum, src, dst, fDate, fTime, seats, price);
                if (flightDAO.addFlight(f)) {
                    ModernDialog.showSuccess(dialog, "Flight added successfully!");
                    loadFlights();
                    dialog.dispose();
                } else {
                    ModernDialog.showError(dialog, "Error adding flight. Duplicate number?");
                }
            } catch (IllegalArgumentException ex) {
                ModernDialog.showWarning(dialog, ex.getMessage());
            } catch (com.fms.exception.FMSException ex) {
                ModernDialog.showError(dialog, "Database error: " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                ModernDialog.showError(dialog, "Invalid input format.");
            }
        });

        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
