package com.fms.ui;

import com.fms.dao.IBookingDAO;
import com.fms.dao.DAOFactory;
import com.fms.dao.IFlightDAO;
import com.fms.model.Booking;
import com.fms.model.Flight;
import com.fms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerDashboard extends JFrame {
    private User user;
    private IFlightDAO flightDAO;
    private IBookingDAO bookingDAO;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    private DefaultTableModel searchModel, myBookingsModel;
    private JTable searchTable, myBookingsTable;

    private JComboBox<String> srcField, destField;
    private JButton dateBtn;

    public CustomerDashboard(User user) {
        try {
            this.user = user;
            try {
                flightDAO = DAOFactory.getFlightDAO();
                bookingDAO = DAOFactory.getBookingDAO();
            } catch (Exception e) {
                System.err.println("Database init failed: " + e.getMessage());
            }

            setTitle("Travel Agency - Welcome " + (user != null ? user.getFirstName() : "User"));
            setSize(1280, 850);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            cardLayout = new CardLayout();
            mainContentPanel = new JPanel(cardLayout);

            mainContentPanel.add(createHomeScrollPanel(), "HOME");
            mainContentPanel.add(createMyBookingsPanel(), "BOOKINGS");
            mainContentPanel.add(createProfilePanel(), "PROFILE");

            add(mainContentPanel);
        } catch (Exception t) {
            t.printStackTrace();
            JOptionPane.showMessageDialog(null, "Critical Error: " + t.getMessage());
        }
    }

    private void navigateTo(String cardName) {
        cardLayout.show(mainContentPanel, cardName);
        if ("BOOKINGS".equals(cardName))
            loadMyBookings();
        else if ("HOME".equals(cardName))
            loadAvailableFlights();
    }

    private JScrollPane createHomeScrollPanel() {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(StyleTheme.BG_COLOR_MAIN);

        JLayeredPane topLayeredPane = new JLayeredPane();
        topLayeredPane.setPreferredSize(new Dimension(1280, 480));

        JPanel heroPanel = new JPanel() {
            private Image heroImg;
            {
                try {
                    java.net.URL imgUrl = getClass().getResource("/com/fms/resources/Client Hero.png");
                    if (imgUrl == null) {
                        String path = "src/com/fms/resources/Client Hero.png";
                        if (new File(path).exists())
                            heroImg = new ImageIcon(path).getImage();
                    } else {
                        heroImg = new ImageIcon(imgUrl).getImage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (heroImg != null) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    double widthScale = (double) getWidth() / heroImg.getWidth(null);
                    double heightScale = (double) getHeight() / heroImg.getHeight(null);
                    double scale = Math.max(widthScale, heightScale);
                    int width = (int) (heroImg.getWidth(null) * scale);
                    int height = (int) (heroImg.getHeight(null) * scale);
                    int x = (getWidth() - width) / 2;
                    int y = (getHeight() - height) / 2;
                    g2.drawImage(heroImg, x, y, width, height, null);
                } else {
                    g.setColor(new Color(30, 58, 138));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        heroPanel.setLayout(new BorderLayout());
        heroPanel.setBounds(0, 0, 1280, 400);

        JPanel headerBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        headerBar.setOpaque(false);
        headerBar.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JLabel logo = new JLabel("IM AIRLINES");
        logo.setFont(StyleTheme.FONT_TITLE);
        try {
            java.net.URL logoUrl = getClass().getResource("/com/fms/resources/IMAirlines - Dark Logo.png");
            if (logoUrl != null) {
                ImageIcon ic = new ImageIcon(logoUrl);
                Image img = ic.getImage().getScaledInstance(140, -1, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(img));
                logo.setText("");
            }
        } catch (Exception e) {
        }

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        nav.setOpaque(false);
        nav.add(createGhostNavButton("My Bookings", e -> navigateTo("BOOKINGS")));
        nav.add(createGhostNavButton("Profile", e -> navigateTo("PROFILE")));
        nav.add(createGhostNavButton("Logout", e -> {
            new LoginFrame().setVisible(true);
            dispose();
        }));

        headerBar.add(logo, BorderLayout.WEST);
        headerBar.add(nav, BorderLayout.EAST);

        heroPanel.add(headerBar, BorderLayout.NORTH);

        JPanel searchWrapper = new JPanel(new GridBagLayout());
        searchWrapper.setOpaque(false);
        searchWrapper.setBounds(0, 350, 1280, 120);

        JPanel searchCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        searchCard.setBackground(Color.WHITE);
        searchCard.setBorder(new StyleTheme.RoundedBorder(25, new Color(220, 220, 220), 1));

        srcField = new JComboBox<>();
        destField = new JComboBox<>();

        StyleTheme.styleComboBox(srcField);
        StyleTheme.styleComboBox(destField);
        srcField.setPreferredSize(new Dimension(170, 45));
        destField.setPreferredSize(new Dimension(170, 45));

        try {
            if (flightDAO != null) {
                srcField.addItem("From");
                for (String s : flightDAO.getDistinctSources())
                    srcField.addItem(s);

                destField.addItem("To");
                for (String s : flightDAO.getDistinctDestinations())
                    destField.addItem(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateBtn = StyleTheme.createSecondaryButton("Select Date");
        dateBtn.setPreferredSize(new Dimension(140, 45));
        dateBtn.addActionListener(e -> {
            DatePicker dp = new DatePicker(this);
            String s = dp.setPickedDate();
            if (s != null && !s.isEmpty())
                dateBtn.setText(s);
            repaint();
        });

        JButton searchBtn = createIconSearchButton();
        JButton resetBtn = StyleTheme.createSecondaryButton("Reset");
        resetBtn.setPreferredSize(new Dimension(70, 45));
        resetBtn.addActionListener(e -> {
            if (srcField.getItemCount() > 0)
                srcField.setSelectedIndex(0);
            if (destField.getItemCount() > 0)
                destField.setSelectedIndex(0);
            dateBtn.setText("Select Date");
            loadAvailableFlights();
        });

        searchCard.add(srcField);
        searchCard.add(destField);
        searchCard.add(dateBtn);
        searchCard.add(Box.createHorizontalStrut(10));
        searchCard.add(searchBtn);
        searchCard.add(resetBtn);

        searchWrapper.add(searchCard);

        topLayeredPane.add(heroPanel, JLayeredPane.DEFAULT_LAYER);
        topLayeredPane.add(searchWrapper, JLayeredPane.PALETTE_LAYER);

        topLayeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = topLayeredPane.getWidth();
                heroPanel.setBounds(0, 0, w, 400);
                searchWrapper.setBounds(0, 350, w, 120);
            }
        });

        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(StyleTheme.BG_COLOR_MAIN);
        contentArea.setBorder(BorderFactory.createEmptyBorder(10, 40, 40, 40));

        JLabel tableTitle = new JLabel("Recommended Flights");
        tableTitle.setFont(StyleTheme.FONT_TITLE);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 5, 15, 0));

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new StyleTheme.RoundedBorder(15, StyleTheme.BORDER_COLOR));

        String[] cols = { "ID", "Flight No", "Source", "Destination", "Date", "Time", "Seats", "Price", "Status",
                "Book" };
        searchModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };
        searchTable = new JTable(searchModel);
        StyleTheme.styleTable(searchTable);

        searchTable.getColumnModel().getColumn(0).setMinWidth(0);
        searchTable.getColumnModel().getColumn(0).setMaxWidth(0);

        searchTable.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        searchTable.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        searchTable.getColumnModel().getColumn(9).setMaxWidth(120);
        searchTable.getColumnModel().getColumn(9).setMinWidth(100);

        JScrollPane tableScroll = new JScrollPane(searchTable);
        tableScroll.getViewport().setBackground(Color.WHITE);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tableCard.add(tableScroll, BorderLayout.CENTER);

        contentArea.add(tableTitle, BorderLayout.NORTH);
        contentArea.add(tableCard, BorderLayout.CENTER);

        scrollContent.add(topLayeredPane);
        scrollContent.add(contentArea);

        JScrollPane mainScroll = new JScrollPane(scrollContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        loadAvailableFlights();
        return mainScroll;
    }

    private JButton createGhostNavButton(String text, java.awt.event.ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(StyleTheme.FONT_BOLD);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (((AbstractButton) c).getModel().isRollover()) {
                    g2.setColor(StyleTheme.PRIMARY_DARK);
                } else {
                    g2.setColor(StyleTheme.PRIMARY_COLOR);
                }
                g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);

                super.paint(g2, c);
                g2.dispose();
            }
        });

        btn.addActionListener(action);
        return btn;
    }

    private JButton createIconSearchButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(45, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.addActionListener(e -> performSearch());
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StyleTheme.PRIMARY_COLOR);
                g2.fillOval(0, 0, c.getWidth(), c.getHeight());
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f));
                int cx = c.getWidth() / 2, cy = c.getHeight() / 2;
                g2.drawOval(cx - 5, cy - 5, 10, 10);
                g2.drawLine(cx + 2, cy + 2, cx + 6, cy + 6);
                g2.dispose();
            }
        });
        return btn;
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFont(StyleTheme.FONT_SMALL);
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(StyleTheme.PRIMARY_COLOR);
            g2.fillRoundRect(5, 8, getWidth() - 10, getHeight() - 16, 15, 15);
            g2.setColor(Color.WHITE);
            g2.setFont(StyleTheme.FONT_SMALL);
            FontMetrics fm = g2.getFontMetrics();
            String text = "Select";
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(text, x, y);
            g2.dispose();
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private boolean isPushed;
        private CustomerDashboard dashboard;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox, CustomerDashboard dashboard) {
            super(checkBox);
            this.dashboard = dashboard;
            button = new JButton("Select") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getModel().isPressed())
                        g2.setColor(StyleTheme.PRIMARY_COLOR.darker());
                    else
                        g2.setColor(StyleTheme.PRIMARY_COLOR);
                    g2.fillRoundRect(5, 8, getWidth() - 10, getHeight() - 16, 15, 15);
                    g2.setColor(Color.WHITE);
                    FontMetrics fm = g2.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth("Select")) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("Select", x, y);
                    g2.dispose();
                }
            };
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            currentRow = row;
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed)
                dashboard.promptBooking(currentRow);
            isPushed = false;
            return "Select";
        }
    }

    public void promptBooking(int row) {
        try {
            int flightId = (int) searchModel.getValueAt(row, 0);
            int availableSeats = (int) searchModel.getValueAt(row, 6);
            String status = (String) searchModel.getValueAt(row, 8);

            if ("CANCELLED".equalsIgnoreCase(status)) {
                ModernDialog.showError(this, "Flight Cancelled.");
                return;
            }
            if (availableSeats <= 0) {
                ModernDialog.showError(this, "Full.");
                return;
            }

            String input = ModernDialog.showInput(this, "Book", "Seats (1-6):");
            if (input == null)
                return;
            int seats = Integer.parseInt(input);
            if (seats < 1 || seats > 6 || seats > availableSeats) {
                ModernDialog.showError(this, "Invalid seats.");
                return;
            }

            SeatSelectionDialog sd = new SeatSelectionDialog(this, flightId, seats);
            sd.setVisible(true);
            if (sd.isConfirmed()) {
                String ss = sd.getSelectedSeatsString();
                String priceStr = (String) searchModel.getValueAt(row, 7);
                double price = Double.parseDouble(priceStr.replace("LKR ", "").trim());
                PaymentDialog pd = new PaymentDialog(this, price * seats);
                pd.setVisible(true);
                if (pd.isPaymentSuccess()) {
                    Booking b = new Booking(user.getUserId(), flightId, ss);
                    if (bookingDAO.createBooking(b)) {
                        flightDAO.updateSeats(flightId, seats);
                        ModernDialog.showSuccess(this, "Pending<br>Approval");
                        loadAvailableFlights();
                    }
                }
            }
        } catch (Exception e) {
            ModernDialog.showError(this, e.getMessage());
        }
    }

    private void performSearch() {
        if (flightDAO == null)
            return;
        try {
            Date d = null;
            String dateStr = dateBtn.getText();
            if (dateStr != null && !dateStr.isEmpty() && !dateStr.equals("Select Date"))
                d = Date.valueOf(dateStr);
            if (dateStr != null && !dateStr.isEmpty() && !dateStr.equals("Select Date"))
                d = Date.valueOf(dateStr);
            String src = (String) srcField.getSelectedItem();
            if ("From".equals(src))
                src = "";

            String dst = (String) destField.getSelectedItem();
            if ("To".equals(dst))
                dst = "";
            List<Flight> fs = flightDAO.searchFlights(src, dst, d);
            searchModel.setRowCount(0);
            for (Flight f : fs)
                addFlightRow(f);
            if (fs.isEmpty())
                ModernDialog.showInfo(this, "No flights found.");
        } catch (Exception e) {
        }
    }

    private void loadAvailableFlights() {
        if (flightDAO == null)
            return;
        searchModel.setRowCount(0);
        try {
            for (Flight f : flightDAO.getAllFlights())
                addFlightRow(f);
        } catch (Exception e) {
        }
    }

    private void addFlightRow(Flight f) {
        searchModel.addRow(new Object[] { f.getFlightId(), f.getFlightNumber(), f.getSource(), f.getDestination(),
                f.getDate(), f.getTime(), f.getSeatsAvailable(), String.format("LKR %.2f", f.getPrice()), f.getStatus(),
                "Select" });
    }

    private JPanel createMyBookingsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleTheme.BG_COLOR_MAIN);
        p.add(createHeader("My Bookings"), BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new StyleTheme.RoundedBorder(10, StyleTheme.BORDER_COLOR));
        card.setBackground(Color.WHITE);

        String[] cols = { "Booking ID", "Flight", "Source", "Destination", "Date", "Status", "Seats" };
        myBookingsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        myBookingsTable = new JTable(myBookingsModel);
        StyleTheme.styleTable(myBookingsTable);

        myBookingsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        myBookingsTable.getColumnModel().getColumn(0).setMaxWidth(120);

        myBookingsTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        myBookingsTable.getColumnModel().getColumn(1).setMaxWidth(100);

        myBookingsTable.getColumnModel().getColumn(4).setPreferredWidth(160);
        myBookingsTable.getColumnModel().getColumn(4).setMaxWidth(200);

        myBookingsTable.getColumnModel().getColumn(5).setCellRenderer(new PillStatusRenderer());
        myBookingsTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        myBookingsTable.getColumnModel().getColumn(5).setMaxWidth(140);

        myBookingsTable.getColumnModel().getColumn(6).setPreferredWidth(150);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        myBookingsTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        myBookingsTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        myBookingsTable.getColumnModel().getColumn(5).setCellRenderer(new PillStatusRenderer());

        JScrollPane sp = new JScrollPane(myBookingsTable);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(BorderFactory.createEmptyBorder());
        card.add(sp, BorderLayout.CENTER);

        JPanel acts = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acts.setBackground(Color.WHITE);
        JButton ref = StyleTheme.createSecondaryButton("Refresh");
        ref.addActionListener(e -> loadMyBookings());
        JButton dl = StyleTheme.createButton("Download");
        dl.addActionListener(e -> {
            int row = myBookingsTable.getSelectedRow();
            if (row == -1) {
                ModernDialog.showWarning(this, "Please select a booking.");
                return;
            }
            String status = (String) myBookingsModel.getValueAt(row, 5);
            if (!"CONFIRMED".equalsIgnoreCase(status)) {
                ModernDialog.showWarning(this, "Only confirmed bookings can be downloaded.");
                return;
            }

            int bookingId = (int) myBookingsModel.getValueAt(row, 0);
            String flightNo = (String) myBookingsModel.getValueAt(row, 1);
            String source = (String) myBookingsModel.getValueAt(row, 2);
            String dest = (String) myBookingsModel.getValueAt(row, 3);
            Object date = myBookingsModel.getValueAt(row, 4);
            Object seatInfo = myBookingsModel.getValueAt(row, 6);

            String fileName = "Ticket_" + bookingId + ".txt";
            try (FileWriter fw = new FileWriter(fileName)) {
                fw.write("=========================================\n");
                fw.write("       AIRPLANE RESERVATION TICKET       \n");
                fw.write("=========================================\n");
                fw.write("Booking ID  : " + bookingId + "\n");
                fw.write("Passenger   : " + user.getFirstName() + " " + user.getLastName() + "\n");
                fw.write("-----------------------------------------\n");
                fw.write("Flight No   : " + flightNo + "\n");
                fw.write("From        : " + source + "\n");
                fw.write("To          : " + dest + "\n");
                fw.write("Date        : " + date + "\n");
                fw.write("Seats       : " + seatInfo + "\n");
                fw.write("Status      : " + status + "\n");
                fw.write("=========================================\n");
                fw.write("Thank you for flying with us!\n");

                ModernDialog.showSuccess(this, "Ticket saved to " + fileName);

                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File(fileName);
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        ModernDialog.showWarning(this, "Could not open file automatically.");
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
                ModernDialog.showError(this, "Error saving ticket: " + ex.getMessage());
            }
        });
        acts.add(ref);
        acts.add(dl);
        card.add(acts, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        wrapper.add(card);
        p.add(wrapper, BorderLayout.CENTER);
        return p;
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(StyleTheme.BG_COLOR_MAIN);
        p.add(createHeader("Profile Settings"), BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField fn = StyleTheme.createModernTextField("First Name");
        fn.setText(user.getFirstName());
        JTextField ln = StyleTheme.createModernTextField("Last Name");
        ln.setText(user.getLastName());
        JTextField em = StyleTheme.createModernTextField("Email");
        em.setText(user.getEmail());
        JTextField nic = StyleTheme.createModernTextField("NIC");
        JTextField ppt = StyleTheme.createModernTextField("Passport");

        if (user instanceof com.fms.model.Customer) {
            nic.setText(((com.fms.model.Customer) user).getNic());
            ppt.setText(((com.fms.model.Customer) user).getPassportNumber());
        }

        g.gridx = 0;
        g.gridy = 0;
        form.add(StyleTheme.createLabel("First Name"), g);
        g.gridx = 1;
        form.add(fn, g);
        g.gridx = 0;
        g.gridy++;
        form.add(StyleTheme.createLabel("Last Name"), g);
        g.gridx = 1;
        form.add(ln, g);
        g.gridx = 0;
        g.gridy++;
        form.add(StyleTheme.createLabel("Email"), g);
        g.gridx = 1;
        form.add(em, g);
        g.gridx = 0;
        g.gridy++;
        form.add(StyleTheme.createLabel("NIC"), g);
        g.gridx = 1;
        form.add(nic, g);
        g.gridx = 0;
        g.gridy++;
        form.add(StyleTheme.createLabel("Passport"), g);
        g.gridx = 1;
        form.add(ppt, g);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setOpaque(false);
        JButton save = StyleTheme.createButton("Save Changes");
        save.addActionListener(e -> {
            String fNameTxt = fn.getText().trim();
            String lNameTxt = ln.getText().trim();
            String emailTxt = em.getText().trim();
            String nicTxt = nic.getText().trim();
            String passportTxt = ppt.getText().trim();

            if (fNameTxt.isEmpty() || lNameTxt.isEmpty() || emailTxt.isEmpty()) {
                ModernDialog.showError(this, "Name and Email are required.");
                return;
            }

            if (!emailTxt.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                ModernDialog.showWarning(this, "Invalid Email Address.");
                return;
            }

            if (user instanceof com.fms.model.Customer) {
                if (nicTxt.isEmpty() || passportTxt.isEmpty()) {
                    ModernDialog.showError(this, "NIC and Passport are required for Customers.");
                    return;
                }
                if (!nicTxt.matches("^[a-zA-Z0-9]{9,12}$")) {
                    ModernDialog.showWarning(this, "Invalid NIC Number (9-12 characters).");
                    return;
                }
                if (!passportTxt.matches("^[a-zA-Z0-9]{6,9}$")) {
                    ModernDialog.showWarning(this, "Invalid Passport Number (6-9 characters).");
                    return;
                }
            }

            user.setFirstName(fNameTxt);
            user.setLastName(lNameTxt);
            user.setEmail(emailTxt);
            if (user instanceof com.fms.model.Customer) {
                ((com.fms.model.Customer) user).setNic(nicTxt);
                ((com.fms.model.Customer) user).setPassportNumber(passportTxt);
            }
            try {
                if (new com.fms.dao.UserDAO().updateUser(user)) {
                    ModernDialog.showSuccess(this, "Profile Updated!");
                } else {
                    ModernDialog.showError(this, "Update Failed.");
                }
            } catch (Exception ex) {
                ModernDialog.showError(this, "Error updating profile: " + ex.getMessage());
            }
        });
        JButton pass = StyleTheme.createSecondaryButton("Change Password");
        pass.addActionListener(e -> {
            while (true) {
                JPanel passPanel = new JPanel(new GridLayout(4, 1, 5, 5));
                JPasswordField pfNew = new JPasswordField();
                JPasswordField pfConfirm = new JPasswordField();

                passPanel.add(new JLabel("New Password:"));
                passPanel.add(pfNew);
                passPanel.add(new JLabel("Confirm Password:"));
                passPanel.add(pfConfirm);

                int ok = JOptionPane.showConfirmDialog(this, passPanel, "Change Password", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (ok != JOptionPane.OK_OPTION) {
                    break;
                }

                String newPass = new String(pfNew.getPassword());
                String confirmPass = new String(pfConfirm.getPassword());

                if (!newPass.equals(confirmPass)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,}$";
                if (!newPass.matches(passwordPattern)) {
                    JOptionPane.showMessageDialog(this,
                            "Password must be at least 6 characters long and contain:\n" +
                                    "- At least one letter\n" +
                                    "- At least one number\n" +
                                    "- At least one symbol (@$!%*#?&)\n" +
                                    "Please try again.",
                            "Weak Password", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                try {
                    if (new com.fms.dao.UserDAO().updatePassword(user.getUsername(), newPass)) {
                        JOptionPane.showMessageDialog(this, "Password Changed Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Password Change Failed.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error changing password: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
        });
        btns.add(pass);
        btns.add(save);
        g.gridx = 1;
        g.gridy++;
        form.add(btns, g);

        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        wrap.add(form);
        p.add(wrap, BorderLayout.CENTER);
        return p;
    }

    private JPanel createHeader(String title) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Color.WHITE);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, StyleTheme.BORDER_COLOR));
        h.add(StyleTheme.createTitleLabel("  " + title), BorderLayout.WEST);
        JButton b = StyleTheme.createSecondaryButton("Home");
        b.addActionListener(e -> navigateTo("HOME"));
        JPanel hr = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        hr.setOpaque(false);
        hr.add(b);
        h.add(hr, BorderLayout.EAST);
        return h;
    }

    private void loadMyBookings() {
        if (bookingDAO == null)
            return;
        myBookingsModel.setRowCount(0);
        try {
            for (Booking b : bookingDAO.getBookingsByUserId(user.getUserId()))
                myBookingsModel.addRow(new Object[] { b.getBookingId(), b.getFlightNumber(), b.getSource(),
                        b.getDestination(), b.getBookingDate(), b.getStatus(), b.getSeatNumbers() });
        } catch (Exception e) {
        }
    }
}
