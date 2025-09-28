import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

public class AdminDashboard extends JFrame {

    // Database Credentials
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");

    JPanel mainPanel;
    CardLayout cardLayout;
    JTable userTable;
    DefaultTableModel userModel;

    // Creates labels
    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        if (font != null) label.setFont(font);
        if (color != null) label.setForeground(color);
        return label;
    }

    // Creates buttons
    private JButton createButton(String text, Color bgColor, Color fgColor, ActionListener action) {
        JButton button = new JButton(text);
        if (bgColor != null) button.setBackground(bgColor);
        if (fgColor != null) button.setForeground(fgColor);
        if (action != null) button.addActionListener(action);
        return button;
    }

    AdminDashboard(String email, String name) {

        // Database credentials
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String passwords = System.getenv("DB_PASS");
        String AName = name;

        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Color labelColor = new Color(44, 62, 80);

        // Card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Dashboard panel
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBounds(0, 0, 710, 550);

        // Stores total users,jobs,reports jobverse has
        int totalUsers = 0, totalJobs = 0, totalReports = 0;

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            // Total users count = employers + jobseekers
            String countSql = "SELECT (SELECT COUNT(*) FROM employers) + (SELECT COUNT(*) FROM jobseekers)";
            try (PreparedStatement pst = con.prepareStatement(countSql);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    totalUsers = rs.getInt(1);
                }
            }
            // Total jobs count
            String jobSql = "SELECT COUNT(*) FROM jobs";
            try (PreparedStatement pst = con.prepareStatement(jobSql);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    totalJobs = rs.getInt(1);
                }
            }

            // Total reports count
            String reportSql = "SELECT COUNT(*) FROM reports";
            try (PreparedStatement pst = con.prepareStatement(reportSql);
                 ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    totalReports = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.add(createStatCard("Total Users", String.valueOf(totalUsers)));
        statsPanel.add(createStatCard("Total Jobs", String.valueOf(totalJobs)));
        statsPanel.add(createStatCard("Reports", String.valueOf(totalReports)));
        dashboardPanel.add(statsPanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(null);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.darkGray);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBounds(0, 0, 900, 60);

        // Title label
        JLabel titleLabel = createLabel("WELCOME " + AName + " (ADMIN)", new Font("Arial", Font.BOLD, 22), Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Right panel
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        rightPanel.setBackground(Color.darkGray);

        // Queries label
        JLabel queriesLabel = new JLabel("👤");
        queriesLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        queriesLabel.setForeground(Color.WHITE);
        queriesLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Queries count label
        JLabel queriesCountLabel = new JLabel("0", SwingConstants.CENTER);
        queriesCountLabel.setFont(new Font("Arial", Font.BOLD, 10));
        queriesCountLabel.setOpaque(true);
        queriesCountLabel.setBackground(Color.RED);
        queriesCountLabel.setForeground(Color.WHITE);
        queriesCountLabel.setBounds(28, 0, 15, 15);
        queriesCountLabel.setVisible(false);
        queriesCountLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        // Queries layered pane
        JLayeredPane queriesLayeredPane = new JLayeredPane();
        queriesLayeredPane.setPreferredSize(new Dimension(45, 35));
        queriesLabel.setBounds(5, 0, 40, 35);
        queriesLayeredPane.add(queriesLabel, JLayeredPane.DEFAULT_LAYER);
        queriesLayeredPane.add(queriesCountLabel, JLayeredPane.PALETTE_LAYER);
        rightPanel.add(queriesLayeredPane);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        contentPanel.add(headerPanel);

        try (Connection con = DriverManager.getConnection(url, user, passwords)) {
            // Fetch queries from database
            String queriesSql = "SELECT COUNT(*) FROM queries WHERE DATE(NOW()) = CURDATE()";
            try (PreparedStatement ps = con.prepareStatement(queriesSql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int todayCount = rs.getInt(1);
                    if (todayCount > 0) {
                        queriesCountLabel.setText(String.valueOf(todayCount));
                        queriesCountLabel.setVisible(true);
                    } else {
                        queriesCountLabel.setVisible(false);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        queriesLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // Queries panel
                JPanel queriesPanel = new JPanel();
                queriesPanel.setLayout(new BoxLayout(queriesPanel, BoxLayout.Y_AXIS));
                queriesPanel.setBackground(new Color(240, 240, 240));

                String sql = "SELECT message_id, name, email, message, submission_date FROM queries ORDER BY submission_date DESC";
                try (Connection con = DriverManager.getConnection(url, user, passwords)){
                    try( PreparedStatement pst = con.prepareStatement(sql)){
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            String name = rs.getString("name");
                            String email = rs.getString("email");
                            String message = rs.getString("message");
                            Timestamp submissionDate = rs.getTimestamp("submission_date");

                            // Queries card panel
                            JPanel queriesCardPanel = new JPanel(new BorderLayout(10, 10));
                            queriesCardPanel.setBackground(Color.WHITE);
                            queriesCardPanel.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
                            ));
                            int cardWidth = 640;

                            // Header label
                            JLabel headerLabel = new JLabel("<html><b>" + name + "</b> (" + email + ")</html>");
                            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
                            headerLabel.setForeground(new Color(40, 40, 40));
                            queriesCardPanel.add(headerLabel, BorderLayout.NORTH);

                            // Message area
                            JTextArea messageTextArea = new JTextArea(message);
                            messageTextArea.setLineWrap(true);
                            messageTextArea.setWrapStyleWord(true);
                            messageTextArea.setEditable(false);
                            messageTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                            messageTextArea.setBackground(new Color(248, 248, 248));
                            messageTextArea.setBorder(BorderFactory.createEmptyBorder(20, 8, 20, 8));
                            messageTextArea.setSize(cardWidth - 30, Short.MAX_VALUE);

                            // Preferred text size height
                            Dimension preferredTextSize = messageTextArea.getPreferredSize();
                            int defaultHeight = 120;
                            int messageHeight = preferredTextSize.height + 70;
                            int cardHeight = Math.max(defaultHeight, messageHeight);

                            queriesCardPanel.setMaximumSize(new Dimension(cardWidth, cardHeight));
                            queriesCardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));
                            queriesCardPanel.setMinimumSize(new Dimension(cardWidth, cardHeight));

                            // Allows scrolling for message TextField
                            JScrollPane messageScrollPane = new JScrollPane(messageTextArea);
                            messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
                            messageScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                            messageScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                            queriesCardPanel.add(messageScrollPane, BorderLayout.CENTER);

                            // Submitted date label
                            JLabel submittedDateLabel = new JLabel( submissionDate.toString());
                            submittedDateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                            submittedDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                            submittedDateLabel.setForeground(new Color(120, 120, 120));
                            queriesCardPanel.add(submittedDateLabel, BorderLayout.SOUTH);

                            queriesPanel.add(Box.createVerticalStrut(15));
                            queriesPanel.add(queriesCardPanel);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Allows scrolling for queries panel
                JScrollPane queriesScrollPane = new JScrollPane(queriesPanel);
                queriesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                queriesScrollPane.getVerticalScrollBar().setUnitIncrement(20);

                // Queries container
                JPanel queriesContainer = new JPanel(new BorderLayout());
                queriesContainer.add(queriesScrollPane, BorderLayout.CENTER);

                mainPanel.add(queriesContainer, "Queries");
                cardLayout.show(mainPanel, "Queries");
            }
        });

        // Sidebar panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(240, 240, 240));
        sidebarPanel.setBounds(0, 60, 179, 550);

        // Home button
        JButton homeButton = new JButton("Home");
        homeButton.setContentAreaFilled(false);
        homeButton.setOpaque(false);
        homeButton.setFont(buttonFont);
        homeButton.setForeground(Color.BLACK);
        homeButton.setFocusPainted(false);
        homeButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        homeButton.setHorizontalAlignment(SwingConstants.CENTER);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homeButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeButton.setForeground(Color.BLACK);
            }
        });
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        homeButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(homeButton);
//        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Stores column names of dashboard section
        String[] columnNames = {"Report ID", "Role", "Reason", "Date", "Action"};
        DefaultTableModel reportModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only Action column is editable
                return column == 4;
            }
        };

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String reportsSql = "SELECT reportID, reportedRole, reason, reportDate, status FROM reports";
            try(PreparedStatement pst = con.prepareStatement(reportsSql)){
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String status = rs.getString("status");
                    String actionText = "Pending".equalsIgnoreCase(status) ? "View" : status;
                    reportModel.addRow(new Object[]{
                            rs.getInt("reportID"),
                            rs.getString("reportedRole"),
                            rs.getString("reason"),
                            rs.getTimestamp("reportDate"),
                            actionText
                    });
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(dashboardPanel, "Error loading reports: " + e.getMessage());
        }

        // Report table
        JTable table = new JTable(reportModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());

        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            JButton button = new JButton();
            int editingRow;
            {
                button.setOpaque(true);
                button.setBackground(new Color(0, 120, 215));
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);

                button.addActionListener(e -> {
                    fireEditingStopped();
                    handleViewButtonClick(editingRow, reportModel, table);
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editingRow = row;
                button.setText(value == null ? "View" : value.toString());
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return button.getText();
            }

            // Handles view button click
            private void handleViewButtonClick(int row, DefaultTableModel model, JTable table) {
                int reportID = (int) model.getValueAt(row, 0);
                String role = (String) model.getValueAt(row, 1);

                String description = "";
                String reportedBy = "";
                String reportedAgainst = "";
                String status = "";

                try (Connection con = DriverManager.getConnection(url, user, password)) {
                    String reportsSql = "SELECT description, reportedBy, reportedAgainst, status FROM reports WHERE reportID=?";
                    try(PreparedStatement pst = con.prepareStatement(reportsSql)){
                        pst.setInt(1, reportID);
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {
                            description = rs.getString("description");
                            reportedBy = rs.getString("reportedBy");
                            reportedAgainst = rs.getString("reportedAgainst");
                            status = rs.getString("status");
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dashboardPanel, "Error fetching report details: " + ex.getMessage());
                    return;
                }

                // Displays details about report on message box
                String message = String.format(
                        "Report ID: %d\nRole: %s\nReason: %s\nDescription: %s\nReported By: %s\nReported Against: %s\nStatus: %s",
                        reportID, role, model.getValueAt(row, 2), description, reportedBy, reportedAgainst, status
                );

                // Stores actions options
                Object[] options = {"Ban", "Hold", "Cancel"};
                int choice = JOptionPane.showOptionDialog(dashboardPanel, message, "Review Report",
                        JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[2]);

                if (choice == JOptionPane.YES_OPTION) {
                    updateReportStatus(reportID, "Banned");
                    updateUserStatus(role, reportedAgainst, "banned");
                    model.setValueAt("Banned", row, 4);
                } else if (choice == JOptionPane.NO_OPTION) {
                    updateReportStatus(reportID, "Hold");
                    updateUserStatus(role, reportedAgainst, "hold");
                    model.setValueAt("Hold", row, 4);
                }
            }

            // Updates the report if any action is taken
            private void updateReportStatus(int reportID, String newStatus) {
                try (Connection con = DriverManager.getConnection(url, user, password)) {
                    String update = "UPDATE reports SET status=? WHERE reportID=?";
                    PreparedStatement pst = con.prepareStatement(update);
                    pst.setString(1, newStatus);
                    pst.setInt(2, reportID);
                    pst.executeUpdate();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dashboardPanel, "Error updating report status: " + e.getMessage());
                }
            }

            // Updates user's activity
            private void updateUserStatus(String role, String email, String newStatus) {
                String table = null, emailCol = null;
                if ("jobseeker".equalsIgnoreCase(role) || "job seeker".equalsIgnoreCase(role)) {
                    table = "jobseekers";
                    emailCol = "email";
                } else if ("employer".equalsIgnoreCase(role)) {
                    table = "employers";
                    emailCol = "employerEmail";
                } else {
                    return;
                }

                try (Connection con = DriverManager.getConnection(url, user , password)) {
                    String update = "UPDATE " + table + " SET status=? WHERE " + emailCol + "=?";
                    try(PreparedStatement pst = con.prepareStatement(update)){
                        pst.setString(1, newStatus);
                        pst.setString(2, email);
                        pst.executeUpdate();
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(dashboardPanel, "Error updating user status: " + e.getMessage());
                }
            }
        });

        dashboardPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(dashboardPanel, "Home");

        // Users button
        JButton usersButton = new JButton("Users");
        usersButton.setContentAreaFilled(false);
        usersButton.setOpaque(false);
        usersButton.setFont(buttonFont);
        usersButton.setForeground(Color.BLACK);
        usersButton.setFocusPainted(false);
        usersButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        usersButton.setHorizontalAlignment(SwingConstants.CENTER);
        usersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        usersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                usersButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                usersButton.setForeground(Color.BLACK);
            }
        });
        usersButton.addActionListener(e -> cardLayout.show(mainPanel, "users"));
        usersButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(usersButton);
        // User panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBounds(0, 0, 710, 550);

        // Filter panel
        JPanel filtersPanel = new JPanel(new FlowLayout());
        filtersPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
        filtersPanel.add(createLabel("Role:", null, null));

        // Dropdown for role selection
        JComboBox<String> roleFilter = new JComboBox<>(new String[]{"All", "Job Seeker", "Employer"});
        filtersPanel.add(roleFilter);
        filtersPanel.add(createLabel("Status:", null, null));

        // Dropdown for status
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Active", "Hold", "Banned"});
        filtersPanel.add(statusFilter);

        // Search TextField
        JTextField searchTextField = new JTextField(15);
        searchTextField.setPreferredSize(new Dimension(200, 28));
        filtersPanel.add(searchTextField);

        // Search button
        JButton searchButton = createButton("Search",new Color(0, 123, 255), Color.WHITE, null);
        filtersPanel.add(searchButton);

        userPanel.add(filtersPanel, BorderLayout.NORTH);

        // Table columns names of user section
        String[] cols = {"User ID", "Name", "Email", "Role", "Status", "Actions"};
        userModel = new DefaultTableModel(null, cols) {
            public boolean isCellEditable(int row, int col) {
                return col == 5; // Only Actions column editable
            }
        };

        // JTable user table
        userTable = new JTable(userModel);
        userTable.setRowHeight(30);

        TableColumn actionCol = userTable.getColumnModel().getColumn(5);
        actionCol.setCellRenderer(new BanButtonRenderer());
        actionCol.setCellEditor(new BanButtonEditor());

        userPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        mainPanel.add(userPanel, "users");

        // Function to load users with filters + search
        Runnable loadUserData = () -> {
            userModel.setRowCount(0); // Clear table
            String selectedRole = roleFilter.getSelectedItem().toString();
            String selectedStatus = statusFilter.getSelectedItem().toString();
            String searchText = searchTextField.getText().trim();

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                if (selectedRole.equals("All") || selectedRole.equals("Job Seeker")) {
                    StringBuilder jsSql = new StringBuilder("SELECT id, firstname, lastname, email, status FROM jobseekers WHERE 1=1");

                    if (!selectedStatus.equalsIgnoreCase("All")) jsSql.append(" AND status = ?");
                    if (!searchText.isEmpty()) {
                        jsSql.append(" AND (CAST(id AS CHAR) LIKE ? OR LOWER(CONCAT(firstname, ' ', lastname)) LIKE ? OR LOWER(email) LIKE ? OR LOWER(status) LIKE ?)");
                    }

                    try(PreparedStatement pst = con.prepareStatement(jsSql.toString())){
                        int paramIndex = 1;
                        if (!selectedStatus.equalsIgnoreCase("All")) pst.setString(paramIndex++, selectedStatus);
                        if (!searchText.isEmpty()) {
                            String likeText = "%" + searchText.toLowerCase() + "%";
                            pst.setString(paramIndex++, likeText);
                            pst.setString(paramIndex++, likeText);
                            pst.setString(paramIndex++, likeText);
                            pst.setString(paramIndex++, likeText);
                        }

                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            String fullName = rs.getString("firstname") + " " + rs.getString("lastname");
                            String status = rs.getString("status");
                            String actionText = status.equalsIgnoreCase("banned") ? "Unban" : "Ban";

                            userModel.addRow(new Object[]{
                                    rs.getInt("id"),
                                    fullName,
                                    rs.getString("email"),
                                    "Job Seeker",
                                    status,
                                    actionText
                            });
                        }
                    }

                    if (selectedRole.equals("All") || selectedRole.equals("Employer")) {
                        StringBuilder empSql = new StringBuilder("SELECT id, fullname, employerEmail, status FROM employers WHERE 1=1");

                        if (!selectedStatus.equalsIgnoreCase("All")) empSql.append(" AND status = ?");
                        if (!searchText.isEmpty()) {
                            empSql.append(" AND (CAST(id AS CHAR) LIKE ? OR LOWER(fullname) LIKE ? OR LOWER(employerEmail) LIKE ? OR LOWER(status) LIKE ?)");
                        }

                        try(PreparedStatement pst = con.prepareStatement(empSql.toString())){
                            int paramIndex = 1;
                            if (!selectedStatus.equalsIgnoreCase("All")) pst.setString(paramIndex++, selectedStatus);
                            if (!searchText.isEmpty()) {
                                String likeText = "%" + searchText.toLowerCase() + "%";
                                pst.setString(paramIndex++, likeText);
                                pst.setString(paramIndex++, likeText);
                                pst.setString(paramIndex++, likeText);
                                pst.setString(paramIndex++, likeText);
                            }

                            ResultSet rs = pst.executeQuery();
                            while (rs.next()) {
                                String status = rs.getString("status");
                                String actionText = status.equalsIgnoreCase("banned") ? "Unban" : "Ban";

                                userModel.addRow(new Object[]{
                                        rs.getInt("id"),
                                        rs.getString("fullname"),
                                        rs.getString("employerEmail"),
                                        "Employer",
                                        status,
                                        actionText
                                });
                            }
                        }

                    }
                }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(userPanel, "Error loading users: " + e.getMessage());
            }
        };

        loadUserData.run();

        roleFilter.addActionListener(e -> loadUserData.run());
        statusFilter.addActionListener(e -> loadUserData.run());

        searchButton.addActionListener(e -> loadUserData.run());

        // Jobs button
        JButton jobsButton = new JButton("Jobs");
        jobsButton.setContentAreaFilled(false);
        jobsButton.setOpaque(false);
        jobsButton.setFont(buttonFont);
        jobsButton.setForeground(Color.BLACK);
        jobsButton.setFocusPainted(false);
        jobsButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        jobsButton.setHorizontalAlignment(SwingConstants.CENTER);
        jobsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jobsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jobsButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jobsButton.setForeground(Color.BLACK);
            }
        });
        jobsButton.addActionListener(e -> cardLayout.show(mainPanel, "jobs"));
        jobsButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(jobsButton);

        // Job panel
        JPanel jobsPanel = new JPanel(null);
        jobsPanel.setBounds(0, 0, 710, 550);

        // Filters panel
        JPanel filtersPanell = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filtersPanell.setBorder(BorderFactory.createTitledBorder("Filters"));
        filtersPanell.setBounds(0, 0, 710, 50);

        // Filters
        filtersPanell.add(createLabel("Status:", null, null));
        String[] filters = {"All", "Open", "Closed", "Paused"};
        JComboBox<String> statusFilterr = new JComboBox<>(filters);
        filtersPanell.add(statusFilterr);

        // Search TextField
        JTextField searchTextFieldd = new JTextField(15);
        searchTextFieldd.setPreferredSize(new Dimension(180,28));
        filtersPanell.add(searchTextFieldd);

        // Search Button
        JButton searchJobsButton = createButton("Search", new Color(0, 123, 255), Color.WHITE, null);
        filtersPanell.add(searchJobsButton);

        jobsPanel.add(filtersPanell);

        // Table Columns names
        String[] cols1 = {"Job ID", "Title", "Company", "Posted Date", "Status", "Action"};
        DefaultTableModel model = new DefaultTableModel(cols1, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 5; // Only Action column editable
            }
        };

        // Table
        JTable jobsTable = new JTable(model);
        jobsTable.setRowHeight(30);
        jobsTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());

        // Allows scrolling for jobs table
        JScrollPane jobsTableScrollPane = new JScrollPane(jobsTable);
        jobsTableScrollPane.setBounds(0, 55, 710, 495);
        jobsPanel.add(jobsTableScrollPane);
        mainPanel.add(jobsPanel, "jobs");

        Runnable loadJobs = () -> {
            model.setRowCount(0);
            String selectedFilter = statusFilterr.getSelectedItem().toString();
            String searchText = searchTextFieldd.getText().trim();

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                StringBuilder jobsSql = new StringBuilder("SELECT id, position, companyName, postedAt, status FROM jobs WHERE 1=1");

                // Add filter condition
                if (!selectedFilter.equals("All")) {
                    jobsSql.append(" AND status = ?");
                }

                // Add search condition (search in all displayed columns)
                if (!searchText.isEmpty()) {
                    jobsSql.append(" AND (CAST(id AS CHAR) LIKE ? OR position LIKE ? OR companyName LIKE ? OR postedAt LIKE ? OR status LIKE ?)");
                }

                try(PreparedStatement pst = con.prepareStatement(jobsSql.toString())){
                    int paramIndex = 1;
                    if (!selectedFilter.equals("All")) {
                        pst.setString(paramIndex++, selectedFilter);
                    }
                    if (!searchText.isEmpty()) {
                        String likeText = "%" + searchText + "%";
                        pst.setString(paramIndex++, likeText); // Job ID
                        pst.setString(paramIndex++, likeText); // Title
                        pst.setString(paramIndex++, likeText); // Company
                        pst.setString(paramIndex++, likeText); // Posted Date
                        pst.setString(paramIndex++, likeText); // Status
                    }

                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String position = rs.getString("position");
                        String company = rs.getString("companyName");
                        Timestamp postedAt = rs.getTimestamp("postedAt");
                        String status = rs.getString("status");

                        model.addRow(new Object[]{id, position, company, postedAt.toString(), status, "Delete"});
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(jobsPanel, "Error loading jobs: " + e.getMessage());
            }
        };

        loadJobs.run();

        statusFilterr.addActionListener(e -> loadJobs.run());

        searchJobsButton.addActionListener(e -> loadJobs.run());

        // Delete button editor
        jobsTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            JButton button = new JButton();
            int editingRow;

            {
                button.setOpaque(true);
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
                button.setBorderPainted(false);

                button.addActionListener(e -> {
                    fireEditingStopped();
                    int modelRow = jobsTable.convertRowIndexToModel(editingRow);
                    int jobId = (int) model.getValueAt(modelRow, 0);

                    int confirm = JOptionPane.showConfirmDialog(jobsPanel,
                            "Are you sure you want to delete job ID " + jobId + "?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        try (Connection con = DriverManager.getConnection(url, user, password)) {
                            String delSql = "DELETE FROM jobs WHERE id = ?";
                            try(PreparedStatement pst = con.prepareStatement(delSql)){
                                pst.setInt(1, jobId);
                                int deleted = pst.executeUpdate();
                                if (deleted > 0) {
                                    model.removeRow(modelRow);
                                    JOptionPane.showMessageDialog(jobsPanel, "Job deleted successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(jobsPanel, "Failed to delete job.");
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(jobsPanel, "Error deleting job: " + ex.getMessage());
                        }
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editingRow = row;
                button.setText(value == null ? "" : value.toString());
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return button.getText();
            }
        });
        mainPanel.add(jobsPanel, "jobs");

        // Add Company's employers button
        JButton addCompanyEmployersButton = new JButton("Add Company");
        addCompanyEmployersButton.setContentAreaFilled(false);
        addCompanyEmployersButton.setOpaque(false);
        addCompanyEmployersButton.setFont(buttonFont);
        addCompanyEmployersButton.setForeground(Color.BLACK);
        addCompanyEmployersButton.setFocusPainted(false);
        addCompanyEmployersButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        addCompanyEmployersButton.setHorizontalAlignment(SwingConstants.CENTER);
        addCompanyEmployersButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addCompanyEmployersButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addCompanyEmployersButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addCompanyEmployersButton.setForeground(Color.BLACK);
            }
        });
        addCompanyEmployersButton.addActionListener(e -> cardLayout.show(mainPanel, "addCompanyEmployersButton"));
        addCompanyEmployersButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(addCompanyEmployersButton);

        // Add company employers panel
        JPanel addCompanyEmployersPanel = new JPanel(null);
        addCompanyEmployersPanel.setBackground(Color.WHITE);

        // Company logo label
        JLabel companyLogoLabel = new JLabel("🏢", SwingConstants.CENTER);
        companyLogoLabel.setBounds(65, 30, 120, 120);
        companyLogoLabel.setOpaque(true);
        companyLogoLabel.setBackground(Color.WHITE);
        companyLogoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        addCompanyEmployersPanel.add(companyLogoLabel);

        // Set profile button
        JButton setProfileButton = new JButton("Set Profile");
        setProfileButton.setContentAreaFilled(false);
        setProfileButton.setOpaque(true);
        setProfileButton.setFont(new Font("Calibri", Font.BOLD, 12));
        setProfileButton.setForeground(Color.WHITE);
        setProfileButton.setBackground(Color.BLACK);
        setProfileButton.setFocusPainted(false);
        setProfileButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        setProfileButton.setHorizontalAlignment(SwingConstants.CENTER);
        setProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setProfileButton.setBounds(75, 160, 100, 30);
        addCompanyEmployersPanel.add(setProfileButton);

        setProfileButton.addActionListener(k -> new CompanyProfile("", false,email,true));

        // Company Name label
        JLabel companyNameLabel = createLabel("Company Name:", labelFont, labelColor);
        companyNameLabel.setBounds(250, 20, 120, 25);
        addCompanyEmployersPanel.add(companyNameLabel);

        // Company name TextField
        JTextField companyNameTextField = new JTextField();
        companyNameTextField.setBounds(375, 20, 250, 25);
        addCompanyEmployersPanel.add(companyNameTextField);

        // Company Email label
        JLabel companyEmailLabel = createLabel("Company Email:", labelFont, labelColor);
        companyEmailLabel.setBounds(250, 60, 120, 25);
        addCompanyEmployersPanel.add(companyEmailLabel);

        // Company email TextField
        JTextField companyEmailTextField = new JTextField();
        companyEmailTextField.setBounds(375, 60, 250, 25);
        addCompanyEmployersPanel.add(companyEmailTextField);

        // Employer Name label
        JLabel employerNameLabel = createLabel("Employer Name:", labelFont, labelColor);
        employerNameLabel.setBounds(250, 100, 120, 25);
        addCompanyEmployersPanel.add(employerNameLabel);

        // Employer name TextField
        JTextField employerNameField = new JTextField();
        employerNameField.setBounds(375, 100, 250, 25);
        addCompanyEmployersPanel.add(employerNameField);

        // Employer Email label
        JLabel employerEmailLabel = createLabel("Employer Email:", labelFont, labelColor);
        employerEmailLabel.setBounds(250, 140, 120, 25);
        addCompanyEmployersPanel.add(employerEmailLabel);

        // Employer Email TextField
        JTextField employerEmailTextField = new JTextField();
        employerEmailTextField.setBounds(375, 140, 250, 25);
        addCompanyEmployersPanel.add(employerEmailTextField);

        // Add employer button
        JButton addEmployerButton = createButton("Add Employer", new Color(40, 167, 69), Color.WHITE, null);
        addEmployerButton.setBounds(502, 175, 120, 30);
        addCompanyEmployersPanel.add(addEmployerButton);

        // Add column names to company's table
        String[] columnNamess = {"ID", "Company Name", "Employer Email", "Employer Name", "Action"};
        DefaultTableModel companyEmployerModel = new DefaultTableModel(columnNamess, 0);
        JTable companyEmployerTable = new JTable(companyEmployerModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // only action column editable
            }
        };
        companyEmployerTable.setRowHeight(28);

        companyEmployerTable.getColumn("Action").setCellRenderer(new DeleteButtonRenderer());
        companyEmployerTable.getColumn("Action").setCellEditor(new DeleteButtonEditor(new JCheckBox(), companyEmployerModel, url, user, password));

        // Allows scrolling for company's table
        JScrollPane tableScrollPane = new JScrollPane(companyEmployerTable);
        tableScrollPane.setBounds(3, 220, 700, 250);
        addCompanyEmployersPanel.add(tableScrollPane);

        mainPanel.add(addCompanyEmployersPanel, "AddCompanyEmployers");

        addCompanyEmployersButton.addActionListener(o -> {
            loadCompanies(companyEmployerModel, url, user, password);
            cardLayout.show(mainPanel, "AddCompanyEmployers");
        });

        addEmployerButton.addActionListener(e -> {
            String companyName = companyNameTextField.getText().trim();
            String companyEmail = companyEmailTextField.getText().trim();
            String employerName = employerNameField.getText().trim();
            String employerEmail = employerEmailTextField.getText().trim();

            if (!companyName.isEmpty()) {
                try (Connection con = DriverManager.getConnection(url, user, password)) {
                    String sql = "INSERT INTO companies (companyName, companyEmail, employersName, employersEmail) VALUES (?, ?, ?, ?)";
                    try(PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                        pst.setString(1, companyName);
                        pst.setString(2, companyEmail.isEmpty() ? null : companyEmail);
                        pst.setString(3, employerName.isEmpty() ? null : employerName);
                        pst.setString(4, employerEmail.isEmpty() ? null : employerEmail);
                        pst.executeUpdate();
                        ResultSet rs = pst.getGeneratedKeys();
                        if (rs.next()) {
                            int id = rs.getInt(1);
                            companyEmployerModel.addRow(new Object[]{id, companyName, companyEmail, employerName, "Delete"});
                        }

                        companyNameTextField.setText("");
                        companyEmailTextField.setText("");
                        employerNameField.setText("");
                        employerEmailTextField.setText("");

                        JOptionPane.showMessageDialog(null, "Employer added successfully!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error adding employer: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Company Name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Analytics button
        JButton analyticsButton = new JButton("Analytics");
        analyticsButton.setContentAreaFilled(false);
        analyticsButton.setOpaque(false);
        analyticsButton.setFont(buttonFont);
        analyticsButton.setForeground(Color.BLACK);
        analyticsButton.setFocusPainted(false);
        analyticsButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        analyticsButton.setHorizontalAlignment(SwingConstants.CENTER);
        analyticsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        analyticsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                analyticsButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                analyticsButton.setForeground(Color.BLACK);
            }
        });
        analyticsButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(analyticsButton);

        // Analytics panel
        JPanel analyticsPanel = new JPanel(null);
        analyticsPanel.setBounds(0, 0, 710, 550);

        // Tabs for analytics
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBounds(0, 0, 710, 550);

        // User growth panel
        JPanel userGrowthPanel = new JPanel(null);
        JLabel userGrowthLabel = new JLabel("User Growth Chart");
        userGrowthLabel.setBounds(10, 10, 300, 25);
        userGrowthPanel.add(userGrowthLabel);

        try (Connection con = DriverManager.getConnection(url, user, password)) {

            // Jobseekers chart
            DefaultCategoryDataset jobseekersDataset = new DefaultCategoryDataset();
            String jobSeekersSql = "SELECT DATE_FORMAT(signup_date,'%Y-%m') AS month, COUNT(*) AS total FROM jobseekers GROUP BY month ORDER BY month";
            try (PreparedStatement pst = con.prepareStatement(jobSeekersSql);
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    jobseekersDataset.addValue(rs.getInt("total"), "Jobseekers", rs.getString("month"));
                }
            }

            // Jobseekers chart
            ChartPanel jsChart = new ChartPanel(ChartFactory.createLineChart("Jobseekers Growth", "Month", "Total", jobseekersDataset));
            jsChart.setBounds(10, 40, 340, 200);
            userGrowthPanel.add(jsChart);

            // Employers chart
            DefaultCategoryDataset employersDataset = new DefaultCategoryDataset();
            String employersSql ="SELECT DATE_FORMAT(signup_date,'%Y-%m') AS month, COUNT(*) AS total FROM employers GROUP BY month ORDER BY month";
            try (PreparedStatement pst = con.prepareStatement(employersSql);
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    employersDataset.addValue(rs.getInt("total"), "Employers", rs.getString("month"));
                }
            }

            // Employers chart
            ChartPanel empChart = new ChartPanel(ChartFactory.createLineChart("Employers Growth", "Month", "Total", employersDataset));
            empChart.setBounds(360, 40, 340, 200);
            userGrowthPanel.add(empChart);

            // Combined(Jobseekers + employers) chart
            DefaultCategoryDataset combinedDataset = new DefaultCategoryDataset();
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT month, SUM(total_users) AS total FROM (" +
                            "SELECT DATE_FORMAT(signup_date,'%Y-%m') AS month, COUNT(*) AS total_users FROM jobseekers GROUP BY month " +
                            "UNION ALL " +
                            "SELECT DATE_FORMAT(signup_date,'%Y-%m') AS month, COUNT(*) AS total_users FROM employers GROUP BY month" +
                            ") AS combined GROUP BY month ORDER BY month");
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    combinedDataset.addValue(rs.getInt("total"), "Users", rs.getString("month"));
                }
            }
            ChartPanel combinedChart = new ChartPanel(ChartFactory.createLineChart("Users Growth(Jobseekers & Employers)", "Month", "Total", combinedDataset));
            combinedChart.setBounds(10, 250, 690, 250);
            userGrowthPanel.add(combinedChart);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tabs.addTab("User Growth", userGrowthPanel);

        // Job Postings panel
        JPanel jobPostingsPanell = new JPanel(null);
        JLabel jobPostingsLabell = new JLabel("Job Postings Chart");
        jobPostingsLabell.setBounds(10, 10, 300, 25);
        jobPostingsPanell.add(jobPostingsLabell);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            // Job posts chart
            DefaultCategoryDataset jobPostsDataset = new DefaultCategoryDataset();
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT DATE_FORMAT(postedAt,'%Y-%m') AS month, COUNT(*) AS total FROM jobs GROUP BY month ORDER BY month");
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    jobPostsDataset.addValue(rs.getInt("total"), "Job Posts", rs.getString("month"));
                }
            }
            // Jobs chart
            ChartPanel jobsChart = new ChartPanel(ChartFactory.createLineChart("Job Posts per Month", "Month", "Total", jobPostsDataset));
            jobsChart.setBounds(10, 40, 690, 200);
            jobPostingsPanell.add(jobsChart);

            // Applied jobs chart
            DefaultCategoryDataset appliedJobsDataset = new DefaultCategoryDataset();
            try (PreparedStatement pst = con.prepareStatement(
                    "SELECT DATE_FORMAT(currentDate,'%Y-%m') AS month, COUNT(*) AS total FROM appliedjobs GROUP BY month ORDER BY month");
                 ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    appliedJobsDataset.addValue(rs.getInt("total"), "Applied Jobs", rs.getString("month"));
                }
            }

            // Applied jobs chart
            ChartPanel appliedChart = new ChartPanel(ChartFactory.createLineChart("Applied Jobs per Month", "Month", "Total", appliedJobsDataset));
            appliedChart.setBounds(10, 250, 690, 250);
            jobPostingsPanell.add(appliedChart);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        tabs.addTab("Job Postings", jobPostingsPanell);

        analyticsPanel.add(tabs);
        mainPanel.add(analyticsPanel, "Analytics");

        analyticsButton.addActionListener(e -> cardLayout.show(mainPanel, "Analytics"));

        // Job postings panel
        JPanel jobPostingsPanel = new JPanel(null);

        // Job postings label
        JLabel jobPostingsLabel = createLabel("Job Postings Chart", null, null);
        jobPostingsLabel.setBounds(10, 10, 300, 25);
        jobPostingsPanel.add(jobPostingsLabel);

        analyticsPanel.add(tabs);
        mainPanel.add(analyticsPanel, "Analytics");

        // Activities button
        JButton activitiesButton = new JButton("User Activities");
        activitiesButton.setContentAreaFilled(false);
        activitiesButton.setOpaque(false);
        activitiesButton.setFont(buttonFont);
        activitiesButton.setForeground(Color.BLACK);
        activitiesButton.setFocusPainted(false);
        activitiesButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        activitiesButton.setHorizontalAlignment(SwingConstants.CENTER);
        activitiesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        activitiesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                activitiesButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                activitiesButton.setForeground(Color.BLACK);
            }
        });
        activitiesButton.addActionListener(e -> cardLayout.show(mainPanel, "user activities"));
        activitiesButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(activitiesButton);

        // User activities panel
        JPanel userActivitiesPanel = new JPanel(null);
        userActivitiesPanel.setBounds(0, 0, 710, 550);

        // Left panel
        JPanel leftPanel = new JPanel(null);
        leftPanel.setBounds(0, 0, 350, 550);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Job Seeker Activities"));

        DefaultListModel<String> seekerModel = new DefaultListModel<>();
        JList<String> seekerList = new JList<>(seekerModel);
        JScrollPane seekerScroll = new JScrollPane(seekerList);
        seekerScroll.setBounds(10, 30, 330, 510);
        leftPanel.add(seekerScroll);

        userActivitiesPanel.add(leftPanel);

        // Right panel
        JPanel rightPanell = new JPanel(null);
        rightPanell.setBounds(360, 0, 350, 550);
        rightPanell.setBorder(BorderFactory.createTitledBorder("Employer Activities"));

        DefaultListModel<String> employerModel = new DefaultListModel<>();
        JList<String> employerList = new JList<>(employerModel);
        JScrollPane employerScroll = new JScrollPane(employerList);
        employerScroll.setBounds(10, 30, 330, 510);
        rightPanell.add(employerScroll);

        userActivitiesPanel.add(rightPanell);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String actitivitesSql = "SELECT activity, user, activityTime FROM activities ORDER BY activityTime DESC";
            try (PreparedStatement pst = con.prepareStatement(actitivitesSql)) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    String activity = rs.getString("activity");
                    String userType = rs.getString("user");
                    String time = rs.getString("activityTime");
                    String entry = time + " - " + activity;

                    if ("JobSeeker".equalsIgnoreCase(userType)) {
                        seekerModel.addElement(entry);
                    } else if ("Employer".equalsIgnoreCase(userType)) {
                        employerModel.addElement(entry);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainPanel.add(userActivitiesPanel, "user activities");

        // Announcement button
        JButton announcementButton = new JButton("Announcements");
        announcementButton.setContentAreaFilled(false);
        announcementButton.setOpaque(false);
        announcementButton.setFont(buttonFont);
        announcementButton.setForeground(Color.BLACK);
        announcementButton.setFocusPainted(false);
        announcementButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        announcementButton.setHorizontalAlignment(SwingConstants.CENTER);
        announcementButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        announcementButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                announcementButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                announcementButton.setForeground(Color.BLACK);
            }
        });
        announcementButton.addActionListener(e -> cardLayout.show(mainPanel, "Announcements"));
        announcementButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(announcementButton);


        announcementButton.addActionListener(a -> {

            // Announcement panel
            JPanel announcementPanel = new JPanel(null);
            announcementPanel.setBackground(Color.white);
            mainPanel.add(announcementPanel, "Announcements");

            // Audience label
            JLabel audienceLabel = new JLabel("Audience:");
            audienceLabel.setBounds(30, 30, 100, 25);
            announcementPanel.add(audienceLabel);

            // Dropdown for all, employers, jobseekers
            JComboBox<String> audienceDropdown = new JComboBox<>(new String[]{"All", "Employers", "JobSeekers"});
            audienceDropdown.setBounds(140, 30, 200, 25);
            announcementPanel.add(audienceDropdown);

            // Title label
            JLabel titleLabell = new JLabel("Title:");
            titleLabell.setBounds(30, 70, 100, 25);
            announcementPanel.add(titleLabell);

            // Title textField
            JTextField titleTextField = new JTextField();
            titleTextField.setBounds(140, 70, 300, 25);
            announcementPanel.add(titleTextField);

            // Message label
            JLabel messageLabel = new JLabel("Message:");
            messageLabel.setBounds(30, 110, 100, 25);
            announcementPanel.add(messageLabel);

            // Message TextArea
            JTextArea messageTextArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(messageTextArea);
            scrollPane.setBounds(140, 110, 400, 150);
            announcementPanel.add(scrollPane);

            // Stores names for column in announcement table
            String[] columns = {"ID", "Audience", "Title", "Message", "Posted Date"};

            // Creates data for table
            DefaultTableModel modell = new DefaultTableModel(columns, 0);

            // Creates table
            JTable tablee = new JTable(modell);

            // Allows scrolling to announcement table
            JScrollPane tableScroll = new JScrollPane(tablee);
            tableScroll.setBounds(3, 330, 700, 200);
            announcementPanel.add(tableScroll);

            Runnable loadAnnouncements = () -> {
                modell.setRowCount(0);
                try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                    String sql = "SELECT * FROM announcements ORDER BY postedDate DESC";
                    try (PreparedStatement pst = con.prepareStatement(sql);
                         ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            modell.addRow(new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("audience"),
                                    rs.getString("title"),
                                    rs.getString("message"),
                                    rs.getTimestamp("postedDate")
                            });
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error loading announcements: " + ex.getMessage());
                }
            };

            // Post button
            JButton postButton = createButton("Post", new Color(40, 167, 69), Color.white, e -> {
                String audience = (String) audienceDropdown.getSelectedItem();
                String title = titleTextField.getText();
                String message = messageTextArea.getText();

                if (title.isEmpty() || message.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Title and message cannot be empty!");
                    return;
                }

                try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                    String sql = "INSERT INTO announcements (audience, title, message) VALUES (?, ?, ?)";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, audience);
                        pst.setString(2, title);
                        pst.setString(3, message);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Announcement posted successfully!");
                        loadAnnouncements.run();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error posting announcement: " + ex.getMessage());
                }
            });
            postButton.setBounds(438, 280, 100, 35);
            announcementPanel.add(postButton);

            // Edit button
            JButton editButton = createButton("Edit", new Color(108, 117, 125), Color.white, e -> {
                int row = tablee.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Select an announcement from the table to edit!");
                    return;
                }

                int id = (int) modell.getValueAt(row, 0);
                String newTitle = JOptionPane.showInputDialog("Enter new Title:", modell.getValueAt(row, 2));
                String newMessage = JOptionPane.showInputDialog("Enter new Message:", modell.getValueAt(row, 3));

                if (newTitle == null || newMessage == null) return;

                try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                    String sql = "UPDATE announcements SET title=?, message=? WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setString(1, newTitle);
                        pst.setString(2, newMessage);
                        pst.setInt(3, id);
                        int rows = pst.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null, "Announcement updated successfully!");
                            loadAnnouncements.run();
                        } else {
                            JOptionPane.showMessageDialog(null, "No announcement found with that ID.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error editing announcement: " + ex.getMessage());
                }
            });
            editButton.setBounds(330, 280, 100, 35);
            announcementPanel.add(editButton);

            // Delete button
            JButton deleteButton = createButton("Delete",new Color(220, 53, 69) , Color.white, e -> {
                int row = tablee.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Select an announcement from the table to delete!");
                    return;
                }

                int id = (int) modell.getValueAt(row, 0);
                try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                    String sql = "DELETE FROM announcements WHERE id=?";
                    try (PreparedStatement pst = con.prepareStatement(sql)) {
                        pst.setInt(1, id);
                        int rows = pst.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null, "Announcement deleted successfully!");
                            loadAnnouncements.run();
                        } else {
                            JOptionPane.showMessageDialog(null, "No announcement found with that ID.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error deleting announcement: " + ex.getMessage());
                }
            });
            deleteButton.setBounds(222, 280, 100, 35);
            announcementPanel.add(deleteButton);
            loadAnnouncements.run();

            cardLayout.show(mainPanel, "Announcements");
        });

        // Ratings and feedback button
        JButton ratingsFeedbackButton = new JButton("Feedbacks");
        ratingsFeedbackButton.setContentAreaFilled(false);
        ratingsFeedbackButton.setOpaque(false);
        ratingsFeedbackButton.setFont(buttonFont);
        ratingsFeedbackButton.setForeground(Color.BLACK);
        ratingsFeedbackButton.setFocusPainted(false);
        ratingsFeedbackButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        ratingsFeedbackButton.setHorizontalAlignment(SwingConstants.CENTER);
        ratingsFeedbackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ratingsFeedbackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ratingsFeedbackButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ratingsFeedbackButton.setForeground(Color.BLACK);
            }
        });
        ratingsFeedbackButton.addActionListener(e -> cardLayout.show(mainPanel, "Feedbacks"));
        ratingsFeedbackButton.setMaximumSize(new Dimension(180, 35));
        sidebarPanel.add(ratingsFeedbackButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 240)));

        ratingsFeedbackButton.addActionListener(a -> {

            // Ratings feedback panel
            JPanel ratingsFeedbackPanel = new JPanel(null);
            ratingsFeedbackPanel.setBackground(Color.white);
            mainPanel.add(ratingsFeedbackPanel, "Feedbacks");

            // Left panel (Jobseekers feedbacks)
            JPanel leftPanell = new JPanel(new BorderLayout());
            leftPanell.setBounds(0, 0, 350, 550);
            leftPanell.setBorder(BorderFactory.createTitledBorder("Job Seeker Feedbacks"));

            // Job seekers container
            JPanel jsContainer = new JPanel();
            jsContainer.setLayout(new BoxLayout(jsContainer, BoxLayout.Y_AXIS));
            jsContainer.setOpaque(false);

            try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                String sql = "SELECT * FROM jsfeedback ORDER BY feedbackDate DESC";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {
                        JPanel card = new JPanel(new BorderLayout(6, 6)) {
                            @Override
                            public Dimension getMaximumSize() {
                                return new Dimension(Integer.MAX_VALUE, 200);
                            }
                        };
                        card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                                BorderFactory.createLineBorder(Color.DARK_GRAY, 1)
                        ));
                        card.setBackground(new Color(173, 216, 230));
                        card.setOpaque(true);

                        // Top panel
                        JPanel topPanel = new JPanel();
                        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
                        topPanel.setOpaque(false);
                        topPanel.add(new JLabel("Name: " + rs.getString("name")));
                        topPanel.add(new JLabel("Email: " + rs.getString("userEmail")));
                        topPanel.add(new JLabel("Rating: " + rs.getInt("rating")));

                        // Center panel
                        JPanel centerPanel = new JPanel();
                        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                        centerPanel.setOpaque(false);

                        // Likes textarea
                        JTextArea likesTextArea = new JTextArea("Likes: " + rs.getString("likes"));
                        likesTextArea.setWrapStyleWord(true);
                        likesTextArea.setLineWrap(true);
                        likesTextArea.setEditable(false);
                        likesTextArea.setOpaque(false);

                        // Improvement textarea
                        JTextArea improvementTextArea = new JTextArea("Improvements : " + rs.getString("dislikes"));
                        improvementTextArea.setWrapStyleWord(true);
                        improvementTextArea.setLineWrap(true);
                        improvementTextArea.setEditable(false);
                        improvementTextArea.setOpaque(false);

                        centerPanel.add(likesTextArea);
                        centerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
                        centerPanel.add(improvementTextArea);

                        // Date label
                        JLabel dateLabell = new JLabel("Date: " + rs.getTimestamp("feedbackDate"));

                        card.add(topPanel, BorderLayout.NORTH);
                        card.add(centerPanel, BorderLayout.CENTER);
                        card.add(dateLabell, BorderLayout.SOUTH);

                        jsContainer.add(card);
                        jsContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Jobseeker scroll pane
            JScrollPane jsScrollPane = new JScrollPane(jsContainer);
            jsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            jsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            leftPanell.add(jsScrollPane, BorderLayout.CENTER);
            ratingsFeedbackPanel.add(leftPanell);
            jsScrollPane.getVerticalScrollBar().setUnitIncrement(20);

            // Right panel (Employers panel)
            JPanel rightPanelll = new JPanel(new BorderLayout());
            rightPanelll.setBounds(360, 0, 350, 550);
            rightPanelll.setBorder(BorderFactory.createTitledBorder("Employer Feedbacks"));

            // Employers container
            JPanel eContainer = new JPanel();
            eContainer.setLayout(new BoxLayout(eContainer, BoxLayout.Y_AXIS));
            eContainer.setOpaque(false);

            try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                String sql = "SELECT * FROM efeedback ORDER BY feedbackDate DESC";
                try (PreparedStatement pst = con.prepareStatement(sql);
                     ResultSet rs = pst.executeQuery()) {

                    while (rs.next()) {
                        JPanel card = new JPanel(new BorderLayout(6, 6)) {
                            @Override
                            public Dimension getMaximumSize() {
                                return new Dimension(Integer.MAX_VALUE, 200);
                            }
                        };
                        card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(8, 8, 8, 8),
                                BorderFactory.createLineBorder(Color.DARK_GRAY, 1)
                        ));
                        card.setBackground(new Color(255, 220, 220));
                        card.setOpaque(true);

                        // Top panel
                        JPanel topPanel = new JPanel();
                        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
                        topPanel.setOpaque(false);
                        topPanel.add(new JLabel("Name: " + rs.getString("name")));
                        topPanel.add(new JLabel("Email: " + rs.getString("userEmail")));
                        topPanel.add(new JLabel("Rating: " + rs.getInt("rating")));

                        // Center panel
                        JPanel centerPanel = new JPanel();
                        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
                        centerPanel.setOpaque(false);

                        // Likes textarea
                        JTextArea likesTextArea = new JTextArea("Likes: " + rs.getString("likes"));
                        likesTextArea.setWrapStyleWord(true);
                        likesTextArea.setLineWrap(true);
                        likesTextArea.setEditable(false);
                        likesTextArea.setOpaque(false);

                        // Improvement textarea
                        JTextArea improvementsTextArea = new JTextArea("Improvements : " + rs.getString("dislikes"));
                        improvementsTextArea.setWrapStyleWord(true);
                        improvementsTextArea.setLineWrap(true);
                        improvementsTextArea.setEditable(false);
                        improvementsTextArea.setOpaque(false);

                        centerPanel.add(likesTextArea);
                        centerPanel.add(Box.createRigidArea(new Dimension(0, 6)));
                        centerPanel.add(improvementsTextArea);

                        JLabel dateLabell = new JLabel("Date: " + rs.getTimestamp("feedbackDate"));

                        card.add(topPanel, BorderLayout.NORTH);
                        card.add(centerPanel, BorderLayout.CENTER);
                        card.add(dateLabell, BorderLayout.SOUTH);

                        eContainer.add(card);
                        eContainer.add(Box.createRigidArea(new Dimension(0, 8)));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Employer scroll pane
            JScrollPane eScrollPane = new JScrollPane(eContainer);
            eScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            eScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            rightPanelll.add(eScrollPane, BorderLayout.CENTER);
            ratingsFeedbackPanel.add(rightPanelll);
            eScrollPane.getVerticalScrollBar().setUnitIncrement(20);

            SwingUtilities.invokeLater(() -> {
                jsScrollPane.getVerticalScrollBar().setValue(0);
                eScrollPane.getVerticalScrollBar().setValue(0);
            });

            cardLayout.show(mainPanel, "Feedbacks");
        });


        // Logout button
        JButton logoutButton = createButton("Logout", Color.RED, Color.WHITE, a -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                new LandingPage();
                dispose();
            }
        });
        logoutButton.setBounds(70,575,100,30);
        contentPanel.add(logoutButton);


        contentPanel.add(sidebarPanel);

        mainPanel.setBounds(180, 60, 710, 550);
        contentPanel.add(mainPanel);

        // Frame settings
        setContentPane(contentPanel);
        cardLayout.show(mainPanel, "dashboard");
        setVisible(true);
        setTitle("Admin Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Create cards
    JPanel createStatCard(String title, String value) {
        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBackground(new Color(245, 245, 245));

        // Title label
        JLabel titleLabel = createLabel(title, null, null);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Value label
        JLabel valueLabel = createLabel(value, new Font("Arial", Font.BOLD, 28), null);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        cardPanel.add(valueLabel);

        return cardPanel;
    }

    class DeleteButtonRenderer extends JButton implements TableCellRenderer {
        public DeleteButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setBackground(new Color(220, 53, 69)); // red delete
            setForeground(Color.WHITE);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Delete");
            return this;
        }
    }

    class DeleteButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private int row;
        private DefaultTableModel model;
        private String url, user, password;

        public DeleteButtonEditor(JCheckBox checkBox, DefaultTableModel model,
                                  String url, String user, String password) {
            super(checkBox);
            this.model = model;
            this.url = url;
            this.user = user;
            this.password = password;

            button = new JButton("Delete");
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this employer?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection con = DriverManager.getConnection(url, user, password)) {
                        int id = (int) model.getValueAt(row, 0);
                        String sql = "DELETE FROM companies WHERE id = ?";
                        PreparedStatement pst = con.prepareStatement(sql);
                        pst.setInt(1, id);
                        pst.executeUpdate();

                        // Cancel editing before modifying model
                        SwingUtilities.invokeLater(() -> {
                            model.removeRow(row);
                            JOptionPane.showMessageDialog(null, "Employer deleted successfully!");
                        });

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error deleting employer: " + ex.getMessage());
                    }
                }
            }
            clicked = false;
            return "Delete";
        }

    }

    private void loadCompanies(DefaultTableModel model, String url, String user, String password) {
        model.setRowCount(0); // clear old rows
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM companies";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("companyName"),
                        rs.getString("employersEmail"),
                        rs.getString("employersName"),
                        "Delete"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error loading companies: " + ex.getMessage());
        }
    }


    // Button renderer for tables
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setBackground(new Color(0, 120, 215));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            return this;
        }
    }

    // Custom renderer for Action column
    class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value == null ? "" : value.toString();
            setText(status);

            // Set color based on status
            if ("Banned".equalsIgnoreCase(status)) {
                setBackground(Color.RED);
            } else if ("Hold".equalsIgnoreCase(status)) {
                setBackground(new Color(255, 165, 0));
            } else {
                setBackground(new Color(0, 120, 215));
            }

            return this;
        }
    }


    // Renderer for Ban/Unban buttons
    class BanButtonRenderer extends JButton implements TableCellRenderer {
        public BanButtonRenderer() {
            setOpaque(true);
            setBorderPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            if ("Unban".equals(value)) {
                setBackground(new Color(0, 200, 0));
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.RED);
                setForeground(Color.WHITE);
            }
            return this;
        }
    }

    // Editor for Ban/Unban buttons
    class BanButtonEditor extends DefaultCellEditor {
        JButton button;
        int editingRow;

        public BanButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            button.setText(value.toString());
            if ("Unban".equals(value)) {
                button.setBackground(new Color(0, 200, 0));
                button.setForeground(Color.BLACK);
            } else {
                button.setBackground(Color.RED);
                button.setForeground(Color.WHITE);
            }
            return button;
        }

        public Object getCellEditorValue() {
            String currentAction = button.getText();
            String userId = userModel.getValueAt(editingRow, 0).toString();
            String role = userModel.getValueAt(editingRow, 3).toString();
            String newStatus = currentAction.equals("Ban") ? "banned" : "active";
            String newAction = currentAction.equals("Ban") ? "Unban" : "Ban";

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to " + currentAction.toLowerCase() + " this user?",
                    "Confirm " + currentAction, JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection con = DriverManager.getConnection(url, user, password)) {
                    String sql = role.equals("Job Seeker") ?
                            "UPDATE jobseekers SET status = ? WHERE id = ?" :
                            "UPDATE employers SET status = ? WHERE id = ?";

                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, newStatus);
                    pst.setInt(2, Integer.parseInt(userId));
                    pst.executeUpdate();

                    userModel.setValueAt(newStatus, editingRow, 4);
                    userModel.setValueAt(newAction, editingRow, 5);

                    JOptionPane.showMessageDialog(null, "User status updated successfully!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
                }
            }
            return userModel.getValueAt(editingRow, 5);
        }

    }

    public static void main(String[] args) {
        AdminDashboard object8 = new AdminDashboard("Admin","admin@gmail.com");
    }
}