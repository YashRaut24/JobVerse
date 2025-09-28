import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

class JobSeekerDashboard extends JFrame {
    JPanel homePanel;
    JScrollPane homeScrollPane;
    JPanel searchedJobsPanel;
    JScrollPane searchedJobsScrollPane;

    // Creates label
    private JLabel createLabel(String labelString, Font font, int x, int y, int width, int height, Container container) {
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        container.add(label);
        return label;
    }

    // Creates label with extra styling
    private JLabel createLabelWithProperties(String labelString, Font font, int x, int y, int width, int height, Container container, Color foreground, boolean opaque, int alignment, Border border) {
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        if (foreground != null) label.setForeground(foreground);
        if (opaque) label.setOpaque(true);
        if (alignment != -1) label.setHorizontalAlignment(alignment);
        if (border != null) label.setBorder(border);
        container.add(label);
        return label;
    }

    // Creates bar chart
    private JPanel createBarChartPanel(String title, Map<String, Integer> data) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D graphics2d = (Graphics2D) g;
                graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int panelWidth = this.getWidth();
                int panelHeight = this.getHeight();

                graphics2d.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = graphics2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                graphics2d.drawString(title, (panelWidth - titleWidth) / 2, 30);

                if (data == null || data.isEmpty()) {
                    graphics2d.drawString("No data available", panelWidth / 2 - 50, panelHeight / 2);
                    return;
                }

                int maxValue = data.values().stream().max(Integer::compareTo).orElse(1);
                int chartX = 50;
                int chartY = 60;
                int chartWidth = panelWidth - 100;
                int chartHeight = panelHeight - 100;

                graphics2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight);
                graphics2d.drawLine(chartX, chartY, chartX, chartY + chartHeight);

                graphics2d.setFont(new Font("Arial", Font.PLAIN, 12));
                for (int i = 0; i <= 5; i++) {
                    int value = maxValue * i / 5;
                    String label = String.valueOf(value);
                    int labelWidth = fm.stringWidth(label);
                    int y = chartY + chartHeight - (i * chartHeight / 5);
                    graphics2d.drawString(label, chartX - labelWidth - 5, y + 5);
                    graphics2d.drawLine(chartX - 3, y, chartX, y);
                }

                int barCount = Math.min(data.size(), 20);
                int barWidth = Math.max(20, chartWidth / (barCount * 2));
                int spacing = barWidth / 2;

                List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(data.entrySet());
                sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

                for (int i = 0; i < barCount && i < sortedEntries.size(); i++) {
                    Map.Entry<String, Integer> entry = sortedEntries.get(i);
                    String label = entry.getKey();
                    int value = entry.getValue();
                    int barHeight = (int) ((double) value / maxValue * chartHeight);
                    int x = chartX + spacing + i * (barWidth + spacing);
                    int y = chartY + chartHeight - barHeight;

                    graphics2d.setColor(Color.RED);
                    graphics2d.fillRect(x, y, barWidth, barHeight);
                    graphics2d.setColor(Color.BLACK);
                    graphics2d.drawRect(x, y, barWidth, barHeight);

                    AffineTransform originalTransform = graphics2d.getTransform();
                    graphics2d.rotate(-Math.PI / 2, x + barWidth / 2, chartY + chartHeight + 25);
                    graphics2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    FontMetrics labelFm = graphics2d.getFontMetrics();
                    int labelWidth = labelFm.stringWidth(label);

                    String displayLabel = label;
                    if (labelWidth > 100) {
                        displayLabel = label.substring(0, Math.min(10, label.length())) + "...";
                        labelWidth = labelFm.stringWidth(displayLabel);
                    }

                    graphics2d.drawString(displayLabel, x + barWidth / 2 - labelWidth / 2, chartY + chartHeight + 25);
                    graphics2d.setTransform(originalTransform);

                    String valueStr = String.valueOf(value);
                    graphics2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    FontMetrics valueFm = graphics2d.getFontMetrics();
                    int valueWidth = valueFm.stringWidth(valueStr);
                    graphics2d.drawString(valueStr, x + barWidth / 2 - valueWidth / 2, y - 5);
                }
            }
        };

        panel.setPreferredSize(new Dimension(1100, 400));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        return panel;
    }

    // Creates button
    private JButton createStyledButton(String text, Color bgColor, Color textColor, int fontSize) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        return button;
    }

    // Creates job cards
    public JPanel createJobCard(String companyName, String companyInfo, String jobType, String skills, String salary, String postedDate, String location,
                                String email, int jobId, String employerEmail, String status, Font compFont, Font typeFont, Font skillsFont, Font salaryFont,
                                Font dateFont, ImageIcon logoIcon,String companyEmail
    ) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String passwords = System.getenv("DB_PASS");
        String jobIdNo = String.valueOf(jobId);

        // Job card panel
        JPanel jobCardPanel = new JPanel(null);
        jobCardPanel.setBackground(Color.WHITE);
        jobCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(0, 0, 0, 20)),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                )
        ));
        jobCardPanel.setPreferredSize(new Dimension(800, 120));
        jobCardPanel.setMaximumSize(new Dimension(1200, 120));

        // Logo button
        JButton logoButton = new JButton();
        logoButton.setBackground(new Color(248, 249, 250));
        logoButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        logoButton.setFocusPainted(false);
        logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (logoIcon != null) logoButton.setIcon(logoIcon);
        logoButton.setBounds(10, 20, 60, 60);
        jobCardPanel.add(logoButton);

        logoButton.addActionListener(p -> {
            new CompanyProfile(companyEmail, false, "",false);
        });

        // Status label
        JLabel statusLabel = createLabelWithProperties(status, new Font("Segoe UI", Font.BOLD, 9), 16, 90, 50, 20, jobCardPanel, null, true, SwingConstants.CENTER, BorderFactory.createEmptyBorder(2, 6, 2, 6));

        switch (status.toLowerCase()) {
            case "open":
                statusLabel.setBackground(new Color(34, 197, 94));
                statusLabel.setForeground(Color.WHITE);
                break;
            case "closed":
                statusLabel.setBackground(new Color(239, 68, 68));
                statusLabel.setForeground(Color.WHITE);
                break;
            case "paused":
                statusLabel.setBackground(new Color(251, 146, 60));
                statusLabel.setForeground(Color.WHITE);
                break;
            default:
                statusLabel.setBackground(new Color(107, 114, 128));
                statusLabel.setForeground(Color.WHITE);
        }

        // Company info
        JLabel companyInfoLabel = createLabel(companyInfo, new Font("Segoe UI", Font.BOLD, 14), 80, 10, 300, 20, jobCardPanel);
        companyInfoLabel.setForeground(new Color(17, 24, 39));

        // Job type label
        JLabel jobTypeLabel = createLabel("• "+jobType, new Font("Segoe UI", Font.PLAIN, 12), 80, 35, 100, 20, jobCardPanel);
        jobTypeLabel.setForeground(new Color(75, 85, 99));

        // Skills label
        JLabel skillsLabel = createLabel("• "+skills, new Font("Segoe UI", Font.PLAIN, 11), 80, 60, 300, 20, jobCardPanel);
        skillsLabel.setForeground(new Color(107, 114, 128));

        // Salary label
        JLabel salaryLabel = createLabel("• ₹" +salary+ "/month", new Font("Segoe UI", Font.BOLD, 12), 80, 85, 200, 20, jobCardPanel);
        salaryLabel.setForeground(new Color(16, 185, 129));

        // Job id label
        JLabel jobIdLabel = createLabel("Job Id : " + jobIdNo,new Font("Segoe UI", Font.BOLD, 12), 490,28,150,20,jobCardPanel);
        jobIdLabel.setForeground( new Color(192, 192, 192));

        // Date label
        JLabel dateLabel = createLabel(postedDate, new Font("Segoe UI", Font.PLAIN, 10), 460, 10, 150, 20, jobCardPanel);
        dateLabel.setForeground(new Color(156, 163, 175));

        // Apply button
        JButton applyButton = new JButton("Apply");
        applyButton.setBackground(new Color(40, 167, 69) );
        applyButton.setForeground(Color.WHITE);
        applyButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        applyButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        applyButton.setFocusPainted(false);
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        applyButton.setBounds(500, 84, 60, 25);
        jobCardPanel.add(applyButton);

        applyButton.addActionListener(b -> {
            if(status.equals("Closed")){
                JOptionPane.showMessageDialog(null, "Applications are currently closed. Please check back later.");
            } else if(status.equals("Paused")){
                JOptionPane.showMessageDialog(null, "Applications are temporarily paused. Please try again later.");
            } else {
                new ApplyForm(email, companyName, location, jobType, skills, salary, postedDate, jobId, employerEmail,companyEmail);
            }
        });

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(255, 193, 7));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        saveButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveButton.setBounds(445, 84, 50, 25);
        jobCardPanel.add(saveButton);

        saveButton.addActionListener(f -> {
            try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                String fetchLogoQuery = "SELECT companyLogo, logoFileName FROM jobs WHERE id = ?";
                InputStream logoStream = null;
                String logoFileName = null;
                try (PreparedStatement pstFetch = con.prepareStatement(fetchLogoQuery)) {
                    pstFetch.setInt(1, jobId);
                    ResultSet rs = pstFetch.executeQuery();
                    if (rs.next()) {
                        logoStream = rs.getBinaryStream("companyLogo");
                        logoFileName = rs.getString("logoFileName");
                    }
                }

                String saveQuery = "INSERT INTO SavedJobs(UserEmail, CompanyName, Location, JobType, SkillsRequired, Salary, PostedDate, id, employerEmail, companyLogo, logoFileName, status, savedDate) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, CURDATE())";
                try (PreparedStatement pst2 = con.prepareStatement(saveQuery)) {
                    pst2.setString(1, email);
                    pst2.setString(2, companyName);
                    pst2.setString(3, location);
                    pst2.setString(4, jobType);
                    pst2.setString(5, skills);
                    pst2.setString(6, salary);
                    pst2.setString(7, postedDate);
                    pst2.setInt(8, jobId);
                    pst2.setString(9, employerEmail);

                    if (logoStream != null) {
                        pst2.setBlob(10, logoStream);
                    } else {
                        pst2.setNull(10, java.sql.Types.BLOB);
                    }

                    if (logoFileName != null) {
                        pst2.setString(11, logoFileName);
                    } else {
                        pst2.setNull(11, java.sql.Types.VARCHAR);
                    }

                    pst2.setString(12,status);
                    pst2.executeUpdate();
                    String activites = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                    try(PreparedStatement pst6 = con.prepareStatement(activites)){
                        pst6.setString(1,email);
                        pst6.setString(2,email + " has saved job post of" + companyName);
                        pst6.setString(3,"JobSeeker");
                        pst6.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null, "Job saved successfully!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);

        // Report Button  reportButton.setFont(emojiFont);11
        JButton reportButton = new JButton("\uD83D\uDEA9");
        reportButton.setBackground(new Color(220, 53, 69));
        reportButton.setForeground(Color.WHITE);
        reportButton.setFont(emojiFont);
        reportButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        reportButton.setFocusPainted(false);
        reportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportButton.setBounds(365, 84, 35, 24);
        jobCardPanel.add(reportButton);

        reportButton.addActionListener(r -> {
            new ReportPanel(jobId, email, employerEmail, "employer","Jobseekers");
        });

        // Chat button
        JButton chatButton = new JButton("\uD83D\uDCAC");
        chatButton.setBackground(new Color(0, 123, 255));
        chatButton.setForeground(Color.WHITE);
        chatButton.setFont(emojiFont);
        chatButton.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        chatButton.setFocusPainted(false);
        chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chatButton.setBounds(405, 84, 35, 24);
        jobCardPanel.add(chatButton);

        chatButton.addActionListener(g -> {
            new JSChatSys(email, employerEmail);
        });

        return jobCardPanel;
    }

    JobSeekerDashboard(String firstName, String lastName, String email) {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        // Fonts
        Font headingFont = new Font("Futura", Font.BOLD, 30);
        Font labelFont = new Font("Future", Font.BOLD, 16);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 14);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font compFont = new Font("Futura", Font.BOLD, 16);
        Font typeFont = new Font("Calibri", Font.PLAIN, 14);
        Font skillsFont = new Font("Calibri", Font.PLAIN, 12);
        Font salaryFont = new Font("Calibri", Font.BOLD, 12);
        Font dateFont = new Font("Calibri", Font.ITALIC, 12);
        Font starFont = new Font("Segoe UI Symbol", Font.PLAIN, 35);
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 14);

        // Helps in switching pages (JPanels)
        CardLayout jobseekerCardLayout = new CardLayout();

        // Container for jobseeker
        Container jobSeekerContainer = getContentPane();
        jobSeekerContainer.setLayout(null);
        jobSeekerContainer.setBackground(new Color(230, 230, 230));

        // Job seeker panel
        JPanel jobSeekerPanel = new JPanel(jobseekerCardLayout);
        jobSeekerPanel.setBackground(Color.WHITE);
        jobSeekerPanel.setBounds(190, 90, 591, 468);
        jobSeekerContainer.add(jobSeekerPanel);

        // Jobverse logo
        ImageIcon originalIcon = new ImageIcon("images/JobVerse-removebg-preview.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(190, 60, Image.SCALE_SMOOTH);

        // Logo label
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
        logoLabel.setBounds(15, 8, 150, 62);
        jobSeekerContainer.add(logoLabel);

        // Welcome message
        JLabel welcomeMessage = createLabelWithProperties("Welcome " + firstName + " " + lastName, new Font("Georgia", Font.PLAIN, 16), 80, 8, 600, 40, jobSeekerContainer, new Color(85, 85, 85), false, JLabel.CENTER, BorderFactory.createEmptyBorder(25, 20, 25, 20));
        welcomeMessage.setFont(headingFont);

        // Initialize selectedFilters array to store filter selections
        String[] selectedFilters = new String[3];

        // Filter button
        JButton filterSearchButton = new JButton("Filter");
        filterSearchButton.setFont(new Font("Calibri", Font.BOLD, 14));
        filterSearchButton.setForeground(Color.WHITE);
        filterSearchButton.setBackground(new Color(108, 117, 125));
        filterSearchButton.setFocusPainted(false);
        filterSearchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterSearchButton.setBounds(682, 54, 100, 30);
        filterSearchButton.setFont(buttonFont);
        jobSeekerContainer.add(filterSearchButton);

        final boolean[] display = {false};

        // Filter layout
        CardLayout filterCard = new CardLayout();
        JPanel filterDisplay = new JPanel(filterCard);
        filterDisplay.setBounds(480, 100, 290, 300);
        filterDisplay.setOpaque(false);

        // Filter panel
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(248, 249, 250));
        filterPanel.setLayout(null);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        filterPanel.setPreferredSize(new Dimension(280, 280));

        // Stores job types
        String[] jobTypes = {"", "Full Time", "Internship", "Remote Work", "Part Time"};

        // Stores salaries type
        String[] salaries = {"", "High", "Low"};

        // Stores jobRoles
        String[] jobRoles = {"","Java Backend Developer", "Frontend Developer", "Machine Learning Engineer", "Data Scientist", "Database Administrator", "UI/UX Designer",
                "Android Developer", "iOS Developer", "MERN Stack Developer", "Node.js Developer", "Game Developer", "DevOps Engineer", "Cloud Engineer",
                "Cybersecurity Analyst", "Flutter Developer", "Web Content Developer", "AI Engineer", "Blockchain Developer", "QA/Test Engineer", "Software Engineer"
        };

        // Job types label
        JLabel jobTypesLabel = createLabel("Job Type:", new Font("Arial", Font.BOLD, 12), 20, 20, 80, 30, filterPanel);
        jobTypesLabel.setForeground(new Color(60, 60, 60));

        // Job types dropdown
        JComboBox<String> jobTypeDropdown = new JComboBox<>(jobTypes);
        jobTypeDropdown.setBounds(110, 20, 150, 30);
        jobTypeDropdown.setFont(new Font("Arial", Font.PLAIN, 11));
        jobTypeDropdown.setBackground(Color.WHITE);
        jobTypeDropdown.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        jobTypeDropdown.addActionListener(e -> {
            String selected = (String) jobTypeDropdown.getSelectedItem();
            selectedFilters[0] = (selected != null && !selected.isEmpty()) ? selected : null;
        });
        filterPanel.add(jobTypeDropdown);

        // Location label
        JLabel locationLabel1 = createLabel("Location:", new Font("Arial", Font.BOLD, 12), 20, 65, 80, 30, filterPanel);
        locationLabel1.setForeground(new Color(60, 60, 60));

        // Location TextField
        JTextField location1 = new JTextField();
        location1.setBounds(110, 65, 150, 30);
        location1.setFont(new Font("Arial", Font.PLAIN, 12));
        location1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        location1.setBackground(Color.WHITE);
        filterPanel.add(location1);

        // Salary label
        JLabel salariesLabel = createLabel("Salary:", new Font("Arial", Font.BOLD, 12), 20, 110, 60, 30, filterPanel);
        salariesLabel.setForeground(new Color(60, 60, 60));

        // Salary dropdown
        JComboBox<String> salariesDropdown = new JComboBox<>(salaries);
        salariesDropdown.setBounds(110, 110, 80, 30);
        salariesDropdown.setFont(new Font("Arial", Font.PLAIN, 11));
        salariesDropdown.setBackground(Color.WHITE);
        salariesDropdown.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        salariesDropdown.addActionListener(f -> {
            String selected = (String) salariesDropdown.getSelectedItem();
            selectedFilters[1] = (selected != null && !selected.isEmpty()) ? selected : null;
        });
        filterPanel.add(salariesDropdown);

        // Job roles label
        JLabel jobRolesLabel = createLabel("Job Role:", new Font("Arial", Font.BOLD, 12), 20, 155, 80, 30, filterPanel);
        jobRolesLabel.setForeground(new Color(60, 60, 60));

        // Job roles dropdown
        JComboBox<String> jobRolesDropdown = new JComboBox<>(jobRoles);
        jobRolesDropdown.setBounds(110, 155, 150, 30);
        jobRolesDropdown.setFont(new Font("Arial", Font.PLAIN, 11));
        jobRolesDropdown.setBackground(Color.WHITE);
        jobRolesDropdown.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        jobRolesDropdown.addActionListener(f -> {
            String selected = (String) jobRolesDropdown.getSelectedItem();
            selectedFilters[2] = (selected != null && !selected.isEmpty()) ? selected : null;
        });
        filterPanel.add(jobRolesDropdown);

        // Skills label
        JLabel skillsLabel2 = createLabel("Skills:", new Font("Arial", Font.BOLD, 12), 20, 200, 80, 30, filterPanel);
        skillsLabel2.setForeground(new Color(60, 60, 60));

        // Skills TextField
        JTextField skills2 = new JTextField();
        skills2.setBounds(110, 200, 150, 30);
        skills2.setFont(new Font("Arial", Font.PLAIN, 12));
        skills2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        skills2.setBackground(Color.WHITE);
        filterPanel.add(skills2);

        // Apply Button
        JButton applyFiltersBtn = new JButton("Apply");
        applyFiltersBtn.setBounds(110, 245, 70, 30);
        applyFiltersBtn.setFont(new Font("Arial", Font.BOLD, 11));
        applyFiltersBtn.setBackground(new Color(40, 167, 69));
        applyFiltersBtn.setForeground(Color.WHITE);
        applyFiltersBtn.setFocusPainted(false);
        applyFiltersBtn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        applyFiltersBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(applyFiltersBtn);

        // Clear button
        JButton clearFiltersBtn = new JButton("Clear");
        clearFiltersBtn.setBounds(190, 245, 70, 30);
        clearFiltersBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        clearFiltersBtn.setBackground(new Color(248, 249, 250));
        clearFiltersBtn.setForeground(new Color(108, 117, 125));
        clearFiltersBtn.setFocusPainted(false);
        clearFiltersBtn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        clearFiltersBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterPanel.add(clearFiltersBtn);

        jobSeekerContainer.add(filterDisplay);

        clearFiltersBtn.addActionListener(e -> {
            jobTypeDropdown.setSelectedIndex(0);
            location1.setText("");
            salariesDropdown.setSelectedIndex(0);
            jobRolesDropdown.setSelectedIndex(0);
            skills2.setText("");
            // Clear the selectedFilters array
            selectedFilters[0] = null;
            selectedFilters[1] = null;
            selectedFilters[2] = null;
        });

        // Filter button toggle functionality
        filterSearchButton.addActionListener(e -> {
            if (!display[0]) {
                // Show filter panel
                filterDisplay.removeAll();
                filterDisplay.add(filterPanel, "filterPanel");
                filterCard.show(filterDisplay, "filterPanel");
                filterDisplay.setVisible(true);
                filterDisplay.revalidate();
                filterDisplay.repaint();
                filterDisplay.getParent().setComponentZOrder(filterDisplay, 0);
                filterSearchButton.setBackground(new Color(220, 220, 220));
                filterSearchButton.setText("Hide Filter");
                display[0] = true;
            } else {
                // Hide filter panel
                filterDisplay.removeAll();
                filterDisplay.setVisible(false);
                filterDisplay.revalidate();
                filterDisplay.repaint();
                filterSearchButton.setBackground(new Color(248, 249, 250));
                filterSearchButton.setText("Filter");
                display[0] = false;
            }
        });

        // Search bar TextField
        JTextField searchJobs = new JTextField();
        searchJobs.setFont(new Font("Calibri", Font.PLAIN, 14));
        searchJobs.setForeground(Color.BLACK);
        searchJobs.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchJobs.setColumns(20);
        searchJobs.setBounds(280, 54, 300, 30);
        searchJobs.setFont(fieldFont);
        jobSeekerContainer.add(searchJobs);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Calibri", Font.BOLD, 14));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setOpaque(true);
        searchButton.setBounds(580, 54, 100, 30);
        searchButton.setFont(buttonFont);
        jobSeekerContainer.add(searchButton);

        // Search panel
        searchedJobsPanel = new JPanel();
        searchedJobsPanel.setLayout(new BoxLayout(searchedJobsPanel, BoxLayout.Y_AXIS));
        searchedJobsPanel.setBackground(new Color(230, 230, 230));

        // Allows scrolling for searched job panels
        searchedJobsScrollPane = new JScrollPane(searchedJobsPanel);
        searchedJobsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchedJobsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchedJobsScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        jobSeekerPanel.add(searchedJobsScrollPane, "Searched Jobs");

        applyFiltersBtn.addActionListener(e -> {
            // Hide filter panel after applying
            filterDisplay.removeAll();
            filterDisplay.setVisible(false);
            filterDisplay.revalidate();
            filterDisplay.repaint();
            filterSearchButton.setBackground(new Color(248, 249, 250));
            filterSearchButton.setText("Filter");
            display[0] = false;

            // Trigger search with current filters
            searchButton.doClick();
        });

        searchButton.addActionListener(a -> {
            searchedJobsPanel.removeAll();
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String searchText = searchJobs.getText().trim();

                // Store search history
                String activities = "INSERT INTO activities (empEmail, activity, user) VALUES (?,?,?)";
                try(PreparedStatement pst6 = con.prepareStatement(activities)){
                    pst6.setString(1, email);
                    pst6.setString(2, email + " has searched for " + searchText);
                    pst6.setString(3, "JobSeeker");
                    pst6.executeUpdate();
                }

                // Handle filter search
                String jobType = selectedFilters[0];
                String salarySort = selectedFilters[1];
                String jobRole = selectedFilters[2];
                String locationText = location1.getText().trim();
                String skillsText = skills2.getText().trim();

                StringBuilder query = new StringBuilder("SELECT * FROM jobs WHERE 1=1");
                List<String> params = new ArrayList<>();

                // Add search text filter
                if (!searchText.isEmpty()) {
                    query.append(" AND companyName LIKE ?");
                    params.add("%" + searchText + "%");
                }

                // Add job type filter
                if (jobType != null && !jobType.isEmpty()) {
                    query.append(" AND jobType = ?");
                    params.add(jobType);
                }

                // Add job role filter
                if (jobRole != null && !jobRole.isEmpty()) {
                    query.append(" AND position = ?");
                    params.add(jobRole);
                }

                // Add location filter
                if (!locationText.isEmpty()) {
                    query.append(" AND location LIKE ?");
                    params.add("%" + locationText + "%");
                }

                // Add skills filter
                if (!skillsText.isEmpty()) {
                    query.append(" AND requirement LIKE ?");
                    params.add("%" + skillsText + "%");
                }

                // Add salary sorting
                if ("High".equalsIgnoreCase(salarySort)) {
                    query.append(" ORDER BY salary DESC");
                } else if ("Low".equalsIgnoreCase(salarySort)) {
                    query.append(" ORDER BY salary ASC");
                }

                // Execute search query
                try (PreparedStatement pst = con.prepareStatement(query.toString())) {

                    // Set parameters for prepared statement
                    for (int i = 0; i < params.size(); i++) {
                        pst.setString(i + 1, params.get(i));
                    }

                    ResultSet rs = pst.executeQuery();
                    boolean hasResults = false;

                    while (rs.next()) {
                        hasResults = true;

                        int jobId = rs.getInt("id");
                        String companyName = rs.getString("companyName");
                        String location = rs.getString("location");
                        String companyInfo = companyName + " | " + location;
                        String jobTypeRes = rs.getString("jobType");
                        String skills = rs.getString("requirement");
                        String salary = rs.getString("salary");
                        String postedDate = rs.getString("postedAt");
                        String employerEmail = rs.getString("employerEmail");
                        String statusUpdate = rs.getString("status");
                        String companyEmail = rs.getString("companyEmail");

                        ImageIcon logoIcon = null;
                        Blob logoBlob = rs.getBlob("companyLogo");
                        if (logoBlob != null) {
                            byte[] imgBytes = logoBlob.getBytes(1, (int) logoBlob.length());
                            logoIcon = new ImageIcon(new ImageIcon(imgBytes).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                        }

                        // Create cards for searched jobs
                        JPanel card = createJobCard(companyName, companyInfo, jobTypeRes, skills, salary, postedDate,
                                location, email, jobId, employerEmail, statusUpdate, compFont, typeFont,
                                skillsFont, salaryFont, dateFont, logoIcon,companyEmail);
                        searchedJobsPanel.add(card);
                    }

                    // If no results found, show message
                    if (!hasResults) {
                        JLabel noResultsLabel = createLabelWithProperties("No jobs found matching your criteria.", new Font("Arial", Font.PLAIN, 16), 0, 0, 600, 50, searchedJobsPanel, new Color(100, 100, 100), false, JLabel.CENTER, null);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                JLabel errorLabel = createLabelWithProperties("Error occurred while searching jobs.", new Font("Arial", Font.PLAIN, 16), 0, 0, 600, 50, searchedJobsPanel, Color.RED, false, JLabel.CENTER, null);
            }

            searchedJobsPanel.revalidate();
            searchedJobsPanel.repaint();
            jobseekerCardLayout.show(jobSeekerPanel, "Searched Jobs");
        });

        int count = 0;
        String jsNotificationsSql = "SELECT COUNT(*) FROM jsnotifications WHERE emailJS = ? OR emailJS = 'ALL'";
        try (Connection con = DriverManager.getConnection(url, user, password)){
            try(PreparedStatement pst1 = con.prepareStatement(jsNotificationsSql)){
                pst1.setString(1, email);
                ResultSet rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    count += rs1.getInt(1);
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }

        String announcementsSql = "SELECT COUNT(*) FROM announcements WHERE audience = 'JobSeekers' OR audience = 'All'";
        try (Connection con = DriverManager.getConnection(url, user, password)){
            try(PreparedStatement pst2 = con.prepareStatement(announcementsSql)) {
                ResultSet rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    count += rs2.getInt(1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Notification button
        JButton notificationButton = new JButton("🔔");
        notificationButton.setBounds(0, 10, 70, 30);
        notificationButton.setOpaque(false);
        notificationButton.setContentAreaFilled(false);
        notificationButton.setBorderPainted(false);
        notificationButton.setForeground(new Color(255, 165, 0));
        notificationButton.setFocusPainted(false);
        notificationButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        notificationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notificationButton.setToolTipText("Job Notifications & Updates");

        // Notification count label
        JLabel notificationCountLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        notificationCountLabel.setOpaque(true);
        notificationCountLabel.setBackground(new Color(220, 53, 69));
        notificationCountLabel.setForeground(Color.WHITE);
        notificationCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        notificationCountLabel.setBounds(40, 0, 20, 20);
        notificationCountLabel.setVisible(count > 0);
        notificationCountLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        notificationCountLabel.setPreferredSize(new Dimension(16, 16));
        notificationCountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Notification layered pane
        JLayeredPane notificationLayer = new JLayeredPane();
        notificationLayer.setBounds(720, 5, 70, 50);
        notificationLayer.add(notificationButton, Integer.valueOf(0));
        notificationLayer.add(notificationCountLabel, Integer.valueOf(1));

        notificationButton.addActionListener(f -> {
            try (Connection con = DriverManager.getConnection(url, user, password)) {

                String shortlistedUrl = "SELECT * FROM jsnotifications WHERE emailJS = ? OR emailJS = 'ALL'";
                try(PreparedStatement pst = con.prepareStatement(shortlistedUrl)){
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    // Notification panel
                    JPanel notificationPanel = new JPanel();
                    notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
                    notificationPanel.setBackground(new Color(248, 249, 250));
                    notificationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    boolean hasNotifications = false;

                    while (rs.next()) {
                        hasNotifications = true;
                        String notification = rs.getString("notifications");
                        String time = rs.getString("notificationDate");
                        int notificationId = rs.getInt("notificationId");

                        // Notification card panel
                        JPanel notificationCardPanel = new JPanel(null);
                        notificationCardPanel.setPreferredSize(new Dimension(550, 90));
                        notificationCardPanel.setBackground(Color.WHITE);
                        notificationCardPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                                BorderFactory.createEmptyBorder(10, 10, 10, 10)
                        ));

                        // Notification message textarea
                        JTextArea notificationMessageTextArea = new JTextArea(notification);
                        notificationMessageTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                        notificationMessageTextArea.setLineWrap(true);
                        notificationMessageTextArea.setWrapStyleWord(true);
                        notificationMessageTextArea.setEditable(false);
                        notificationMessageTextArea.setFocusable(false);
                        notificationMessageTextArea.setOpaque(false);
                        notificationMessageTextArea.setBorder(null);
                        notificationMessageTextArea.setBounds(15, 10, 400, 40);
                        notificationCardPanel.add(notificationMessageTextArea);

                        // Notification date label
                        JLabel notificationDateLabel = new JLabel(time);
                        notificationDateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                        notificationDateLabel.setForeground(new Color(107, 114, 128));
                        notificationDateLabel.setBounds(15, 55, 300, 20);
                        notificationCardPanel.add(notificationDateLabel);

                        // Dismiss button
                        JButton dismissButton = new JButton("Dismiss");
                        dismissButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
                        dismissButton.setBackground(new Color(239, 68, 68));
                        dismissButton.setForeground(Color.WHITE);
                        dismissButton.setFocusPainted(false);
                        dismissButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        dismissButton.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                        dismissButton.setBounds(430, 30, 100, 30);

                        notificationCardPanel.add(dismissButton);

                        dismissButton.addActionListener(e -> {
                            try (Connection con2 = DriverManager.getConnection(url, user, password)) {
                                String deleteQuery = "DELETE FROM jsnotifications WHERE notificationId = ?";
                                try (PreparedStatement pst2 = con2.prepareStatement(deleteQuery)) {
                                    pst2.setInt(1, notificationId);
                                    pst2.executeUpdate();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            notificationPanel.remove(notificationCardPanel);
                            notificationPanel.revalidate();
                            notificationPanel.repaint();
                        });

                        notificationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                        notificationPanel.add(notificationCardPanel);
                    }

                    String announcementSql = "SELECT * FROM announcements WHERE audience='JobSeekers' OR audience='All' ORDER BY postedDate DESC";
                    try (PreparedStatement pstAnn = con.prepareStatement(announcementSql);
                         ResultSet rsAnn = pstAnn.executeQuery()) {

                        while (rsAnn.next()) {
                            hasNotifications = true;
                            String title = rsAnn.getString("title");
                            String message = rsAnn.getString("message");
                            String time = rsAnn.getString("postedDate");

                            // Announcement card panel
                            JPanel announcementCardPanel = new JPanel(null);
                            announcementCardPanel.setPreferredSize(new Dimension(550, 100));
                            announcementCardPanel.setBackground(new Color(255, 255, 240));
                            announcementCardPanel.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                            ));

                            // Title label
                            JLabel titleLabel = new JLabel("• " + title);
                            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
                            titleLabel.setBounds(15, 5, 500, 20);
                            announcementCardPanel.add(titleLabel);

                            // Message text area
                            JTextArea messageTextArea = new JTextArea(message);
                            messageTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                            messageTextArea.setLineWrap(true);
                            messageTextArea.setWrapStyleWord(true);
                            messageTextArea.setEditable(false);
                            messageTextArea.setFocusable(false);
                            messageTextArea.setOpaque(false);
                            messageTextArea.setBounds(15, 30, 500, 40);
                            announcementCardPanel.add(messageTextArea);

                            // Date label
                            JLabel dateLabel = new JLabel(time);
                            dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                            dateLabel.setForeground(new Color(107, 114, 128));
                            dateLabel.setBounds(15, 75, 300, 20);
                            announcementCardPanel.add(dateLabel);

                            notificationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                            notificationPanel.add(announcementCardPanel);
                        }
                    }

                    // Wrapper panel
                    JPanel wrapperPanel = new JPanel(new BorderLayout());
                    wrapperPanel.setBackground(new Color(248, 249, 250));
                    if (hasNotifications) {
                        JScrollPane notificationScrollPane = new JScrollPane(notificationPanel);
                        notificationScrollPane.setBorder(null);
                        notificationScrollPane.getVerticalScrollBar().setUnitIncrement(20);
                        wrapperPanel.add(notificationScrollPane, BorderLayout.CENTER);
                    } else {
                        JLabel emptyLabel = new JLabel("No notifications.", SwingConstants.CENTER);
                        emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                        emptyLabel.setForeground(new Color(107, 114, 128));
                        wrapperPanel.add(emptyLabel, BorderLayout.CENTER);
                    }
                    jobSeekerPanel.add(wrapperPanel, "Notifications");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            jobseekerCardLayout.show(jobSeekerPanel, "Notifications");
        });

        jobSeekerContainer.add(notificationLayer);

        // Sidebar Home button
        JButton homeButton = new JButton("Home");
        homeButton.setContentAreaFilled(false);
        homeButton.setOpaque(false);
        homeButton.setFont(new Font("Calibri", Font.BOLD, 14));
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
        jobSeekerContainer.add(homeButton);
        homeButton.setBounds(0, 90, 190, 30);
        homeButton.setFont(buttonFont);

        // Home panel
        homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(230, 230, 230));

        // Allow scrolling for home panel
        homeScrollPane = new JScrollPane(homePanel);
        homeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        homeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        jobSeekerPanel.add(homeScrollPane, "Home");

        homeButton.addActionListener(a -> {
            homePanel.removeAll();

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String jobApplications = "SELECT * FROM jobs";
                try (PreparedStatement pst = con.prepareStatement(jobApplications)) {
                    ResultSet rs = pst.executeQuery();

                    boolean hasJobs = false;

                    while (rs.next()) {
                        hasJobs = true;

                        int jobId = rs.getInt("id");
                        String companyName = rs.getString("companyName");
                        String location = rs.getString("location");
                        String companyInfo = companyName + " " + location;
                        String jobTypeRes = rs.getString("jobType");
                        String skills = rs.getString("requirement");
                        String salary = rs.getString("salary");
                        String postedDate = rs.getString("postedAt");
                        String employerEmail = rs.getString("employerEmail");
                        String statusUpdate = rs.getString("status");
                        String companyEmail = rs.getString("companyEmail");

                        ImageIcon logoIcon = null;
                        Blob logoBlob = rs.getBlob("companyLogo");
                        if (logoBlob != null) {
                            byte[] imgBytes = logoBlob.getBytes(1, (int) logoBlob.length());
                            logoIcon = new ImageIcon(new ImageIcon(imgBytes).getImage()
                                    .getScaledInstance(60, 60, Image.SCALE_SMOOTH));
                        }

                        JPanel card = createJobCard(
                                companyName, companyInfo, jobTypeRes, skills, salary, postedDate,
                                location, email, jobId, employerEmail, statusUpdate,
                                compFont, typeFont, skillsFont, salaryFont, dateFont, logoIcon,companyEmail
                        );
                        homePanel.add(card);
                    }

                    if (!hasJobs) {
                        JLabel noJobsLabel = new JLabel("No job postings available at the moment.");
                        noJobsLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        noJobsLabel.setForeground(new Color(150, 150, 150));
                        noJobsLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JPanel noJobsPanel = new JPanel(new BorderLayout());
                        noJobsPanel.setPreferredSize(new Dimension(560, 100));
                        noJobsPanel.add(noJobsLabel, BorderLayout.CENTER);

                        homePanel.setLayout(new BorderLayout());
                        homePanel.add(noJobsPanel, BorderLayout.CENTER);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            homePanel.revalidate();
            homePanel.repaint();
            jobseekerCardLayout.show(jobSeekerPanel, "Home");
        });

        // Sidebar check hiring button
        JButton checkHiringsButton = new JButton("Check Hirings");
        checkHiringsButton.setContentAreaFilled(false);
        checkHiringsButton.setOpaque(false);
        checkHiringsButton.setFont(new Font("Calibri", Font.BOLD, 14));
        checkHiringsButton.setForeground(Color.BLACK);
        checkHiringsButton.setFocusPainted(false);
        checkHiringsButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        checkHiringsButton.setHorizontalAlignment(SwingConstants.CENTER);
        checkHiringsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkHiringsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                checkHiringsButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                checkHiringsButton.setForeground(Color.BLACK);
            }
        });
        jobSeekerContainer.add(checkHiringsButton);
        checkHiringsButton.setBounds(0, 120, 190, 30);

        // Check hiring panel
        JPanel checkHiringsPanel = new JPanel();
        checkHiringsPanel.setBackground(new Color(245, 247, 250));
        checkHiringsPanel.setLayout(new BorderLayout());
        checkHiringsPanel.setPreferredSize(new Dimension(1200, 900));

        jobSeekerPanel.add(checkHiringsPanel, "Check Hirings");

        checkHiringsButton.addActionListener(e -> {
            checkHiringsPanel.removeAll();
            String[] columnNames = {"Company Name", "Website", "Industry", "Open Positions", "Hiring Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // make all cells non-editable
                }
            };

            try {Connection conn = DriverManager.getConnection(url, user, password);
                String sql = "SELECT companyName, companyWebsite, industry, openPositions, hiringStatus FROM hirings";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    String companyName = rs.getString("companyName");
                    String companyWebsite = rs.getString("companyWebsite");
                    String industry = rs.getString("industry");
                    int openPositions = rs.getInt("openPositions");
                    String hiringStatus = rs.getString("hiringStatus");
                    model.addRow(new Object[]{companyName, companyWebsite, industry, openPositions, hiringStatus});
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JTable table = new JTable(model) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (c instanceof JLabel) {
                        JLabel label = (JLabel) c;
                        if (column == 1) { // Website column
                            label.setHorizontalAlignment(SwingConstants.LEFT);
                            label.setForeground(new Color(30, 144, 255));
                        } else {
                            label.setHorizontalAlignment(SwingConstants.CENTER);
                            label.setForeground(Color.DARK_GRAY);
                        }
                    }
                    return c;
                }
            };

            table.setRowSelectionAllowed(false);
            table.setColumnSelectionAllowed(false);
            table.setCellSelectionEnabled(false);
            table.setFocusable(false);
            table.setRowHeight(28);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            table.setGridColor(new Color(200, 200, 200));
            table.setShowGrid(true);

            // Header of table
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
            header.setBackground(new Color(30, 144, 255));
            header.setForeground(Color.WHITE);
            header.setPreferredSize(new Dimension(header.getWidth(), 30));

            // Allows scrolling for table
            JScrollPane tableScrollPane = new JScrollPane(table);
            tableScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            // Title label
            JLabel titleLabel = createLabelWithProperties("Current Company Hirings", new Font("Segoe UI", Font.BOLD, 18), 0, 0, 0, 0, checkHiringsPanel, new Color(40, 40, 40), false, -1, BorderFactory.createEmptyBorder(15, 15, 10, 15));

            checkHiringsPanel.setLayout(new BorderLayout());
            checkHiringsPanel.add(tableScrollPane, BorderLayout.CENTER);

            checkHiringsPanel.revalidate();
            checkHiringsPanel.repaint();

            ((CardLayout) jobSeekerPanel.getLayout()).show(jobSeekerPanel, "Check Hirings");
        });

        // Sidebar bar appliedButton
        JButton appliedButton = new JButton("Applied Jobs");
        appliedButton.setContentAreaFilled(false);
        appliedButton.setOpaque(false);
        appliedButton.setFont(new Font("Calibri", Font.BOLD, 14));
        appliedButton.setForeground(Color.BLACK);
        appliedButton.setFocusPainted(false);
        appliedButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        appliedButton.setHorizontalAlignment(SwingConstants.CENTER);
        appliedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        appliedButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                appliedButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                appliedButton.setForeground(Color.BLACK);
            }
        });
        appliedButton.setBounds(0, 150, 190, 30);
        appliedButton.setFont(buttonFont);
        jobSeekerContainer.add(appliedButton);

        appliedButton.addActionListener(e -> jobseekerCardLayout.show(jobSeekerPanel, "Applied Jobs"));

        // AppliedPJobPanel
        JPanel appliedJobsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        appliedJobsPanel.setBackground(Color.WHITE);
        appliedJobsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Allows scrolling for appliedjobs panel
        JScrollPane appliedJobsScrollPane = new JScrollPane(appliedJobsPanel);
        appliedJobsScrollPane.setBorder(null);

        // Applied jobs container
        JPanel appliedJobsContainer = new JPanel(new BorderLayout());
        appliedJobsContainer.add(appliedJobsScrollPane, BorderLayout.CENTER);
        jobSeekerPanel.add(appliedJobsContainer, "Applied Jobs");

        appliedButton.addActionListener(e -> {
            appliedJobsPanel.removeAll();
            appliedJobsPanel.setLayout(new GridLayout(0, 2, 10, 10));
            appliedJobsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            if (appliedJobsScrollPane != null) {
                appliedJobsScrollPane.getVerticalScrollBar().setUnitIncrement(20);
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String appliedQuery = "SELECT * FROM AppliedJobs WHERE UserEmail = ?";
                try (PreparedStatement pst = con.prepareStatement(appliedQuery)) {
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    boolean hasAppliedJobs = false;

                    while (rs.next()) {
                        hasAppliedJobs = true;

                        int jobId = rs.getInt("id");
                        String ACompName = rs.getString("CompanyName");
                        String ALocation = rs.getString("Location");
                        String companyInfo = ACompName + " | " + ALocation;
                        String AJobType = rs.getString("JobType");
                        String ASkillsRequired = rs.getString("SkillsRequired");
                        String ASalary = rs.getString("Salary");
                        String APostedDate = rs.getString("PostedDate");
                        String employerEmail = rs.getString("employerEmail");
                        String status = "Unknown";
                        String companyEmail = rs.getString("companyEmail");

                        String jobIdNo = String.valueOf(jobId);

                        String statusQuery = "SELECT status, companyLogo, logoFileName FROM jobs WHERE id = ?";
                        ImageIcon logoIcon = null;
                        try (PreparedStatement pstStatus = con.prepareStatement(statusQuery)) {
                            pstStatus.setInt(1, jobId);
                            ResultSet rsStatus = pstStatus.executeQuery();
                            if (rsStatus.next()) {
                                status = rsStatus.getString("status");
                                Blob logoBlob = rsStatus.getBlob("companyLogo");
                                if (logoBlob != null) {
                                    byte[] imgBytes = logoBlob.getBytes(1, (int) logoBlob.length());
                                    Image img = new ImageIcon(imgBytes).getImage()
                                            .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                                    logoIcon = new ImageIcon(img);
                                }
                            }
                        }

                        // Applied card panel
                        JPanel appliedCardPanel = new JPanel(null);
                        appliedCardPanel.setBackground(Color.WHITE);
                        appliedCardPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 15)),
                                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                                )
                        ));
                        appliedCardPanel.setPreferredSize(new Dimension(270, 160));

                        // Logo button
                        JButton logoButton = new JButton();
                        logoButton.setBackground(new Color(248, 249, 250));
                        logoButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                                BorderFactory.createEmptyBorder(2, 2, 2, 2)
                        ));
                        logoButton.setFocusPainted(false);
                        logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        logoButton.setBounds(10, 10, 60, 60);
                        appliedCardPanel.add(logoButton);
                        if (logoIcon != null) {
                            logoButton.setIcon(logoIcon);
                        } else {
                            logoButton.setText("🏢");
                            logoButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                        }

                        logoButton.addActionListener(p -> new CompanyProfile(companyEmail, false,"",false));

                        Blob resumeBlob = rs.getBlob("ResumePDF");
                        String resumeFileName = rs.getString("FileName");

                        // Status label
                        JLabel statusLabel = createLabelWithProperties(status, new Font("Segoe UI", Font.BOLD, 9),
                                10, 75, 60, 18, appliedCardPanel, null, true,
                                SwingConstants.CENTER, BorderFactory.createEmptyBorder(2, 6, 2, 6));

                        switch (status.toLowerCase()) {
                            case "open":
                                statusLabel.setBackground(new Color(34, 197, 94));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            case "closed":
                                statusLabel.setBackground(new Color(239, 68, 68));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            case "paused":
                                statusLabel.setBackground(new Color(251, 146, 60));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            default:
                                statusLabel.setBackground(new Color(107, 114, 128));
                                statusLabel.setForeground(Color.WHITE);
                        }

                        // Job id label
                        JLabel jobIdLabel = createLabel("Job Id : " + jobIdNo,new Font("Segoe UI", Font.BOLD, 12), 10,95,150,20,appliedCardPanel);
                        jobIdLabel.setForeground(new Color(192, 192, 192));

                        // Company info Label
                        JLabel companyInfoLabel = createLabel(companyInfo, new Font("Segoe UI", Font.BOLD, 12), 80, 8, 170, 18, appliedCardPanel);
                        companyInfoLabel.setForeground(new Color(17, 24, 39));

                        // Job type label
                        JLabel jobTypeLabel = createLabel("• " + AJobType, new Font("Segoe UI", Font.PLAIN, 10), 80, 28, 170, 16, appliedCardPanel);
                        jobTypeLabel.setForeground(new Color(75, 85, 99));

                        // Skills label
                        JLabel skillsLabel = createLabel("• " + (ASkillsRequired.length() > 50 ? ASkillsRequired.substring(0, 50) + "..." : ASkillsRequired),
                                new Font("Segoe UI", Font.PLAIN, 9), 80, 46, 250, 14, appliedCardPanel);
                        skillsLabel.setForeground(new Color(107, 114, 128));

                        // Salary label
                        JLabel salaryLabel = createLabel("• ₹" + ASalary + "/month", new Font("Segoe UI", Font.BOLD, 10), 80, 62, 170, 16, appliedCardPanel);
                        salaryLabel.setForeground(new Color(16, 185, 129));

                        // Date label
                        JLabel dateLabel = createLabel("• Applied: " + APostedDate, new Font("Segoe UI", Font.PLAIN, 10), 80, 80, 170, 14, appliedCardPanel);
                        dateLabel.setForeground(new Color(156, 163, 175));

                        // View resume button
                        JButton viewResumeButton = createStyledButton("Resume", new Color(99, 102, 241), Color.WHITE, 9);
                        viewResumeButton.setBounds(95, 120, 60, 24);
                        viewResumeButton.addActionListener(v -> {
                            try {
                                if (resumeBlob != null) {
                                    byte[] resumeBytes = resumeBlob.getBytes(1, (int) resumeBlob.length());
                                    File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + resumeFileName);
                                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                        fos.write(resumeBytes);
                                    }
                                    Desktop.getDesktop().open(tempFile);
                                } else {
                                    JOptionPane.showMessageDialog(null, "No resume uploaded.");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                        appliedCardPanel.add(viewResumeButton);

                        // Report button
                        JButton reportButton = createStyledButton("🚩", new Color(220, 53, 69), Color.WHITE, 11);
                        reportButton.setFont(emojiFont);
                        reportButton.setBounds(170, 120, 35, 24);
                        appliedCardPanel.add(reportButton);
                        reportButton.addActionListener(h -> new ReportPanel(jobId, email, employerEmail, "employer", "Jobseeker"));

                        // Chat button
                        JButton chatButton = createStyledButton("💬", new Color(0, 123, 255), Color.WHITE, 11);
                        chatButton.setFont(emojiFont);
                        chatButton.setBounds(220, 120, 35, 24);
                        appliedCardPanel.add(chatButton);
                        chatButton.addActionListener(g -> new JSChatSys(email, employerEmail));

                        appliedJobsPanel.add(appliedCardPanel);
                    }

                    if (!hasAppliedJobs) {
                        JLabel noAppliedLabel = new JLabel("You haven’t applied to any jobs yet.");
                        noAppliedLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        noAppliedLabel.setForeground(new Color(150, 150, 150));
                        noAppliedLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JPanel noAppliedPanel = new JPanel(new BorderLayout());
                        noAppliedPanel.setPreferredSize(new Dimension(560, 100));
                        noAppliedPanel.add(noAppliedLabel, BorderLayout.CENTER);

                        appliedJobsPanel.setLayout(new BorderLayout());
                        appliedJobsPanel.add(noAppliedPanel, BorderLayout.CENTER);
                    }
                }

                appliedJobsPanel.revalidate();
                appliedJobsPanel.repaint();
                jobseekerCardLayout.show(jobSeekerPanel, "Applied Jobs");

            } catch (Exception f) {
                f.printStackTrace();
            }
        });

        // Sidebar Saved button
        JButton savedButton = new JButton("Saved Jobs");
        savedButton.setContentAreaFilled(false);
        savedButton.setOpaque(false);
        savedButton.setFont(new Font("Calibri", Font.BOLD, 14));
        savedButton.setForeground(Color.BLACK);
        savedButton.setFocusPainted(false);
        savedButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        savedButton.setHorizontalAlignment(SwingConstants.CENTER);
        savedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        savedButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                savedButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                savedButton.setForeground(Color.BLACK);
            }
        });
        savedButton.setBounds(0, 180, 190, 30);
        savedButton.setFont(buttonFont);
        jobSeekerContainer.add(savedButton);

        // Saved jobs panel
        JPanel savedJobsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        savedJobsPanel.setBackground(Color.WHITE);
        savedJobsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Allows scrolling for saved jobs panel
        JScrollPane savedJobsScrollPane = new JScrollPane(savedJobsPanel);
        savedJobsScrollPane.setBorder(null);

        // Saved jobs container
        JPanel savedJobsContainer = new JPanel(new BorderLayout());
        savedJobsContainer.add(savedJobsScrollPane, BorderLayout.CENTER);
        jobSeekerPanel.add(savedJobsContainer, "Saved Jobs");
        savedButton.addActionListener(f -> jobseekerCardLayout.show(jobSeekerPanel, "Saved Jobs"));

        savedButton.addActionListener(f -> {
            savedJobsPanel.removeAll();
            savedJobsPanel.setLayout(new GridLayout(0, 2, 10, 10));
            savedJobsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            if (savedJobsScrollPane != null) savedJobsScrollPane.getVerticalScrollBar().setUnitIncrement(20);

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String savedQuery = "SELECT * FROM SavedJobs WHERE UserEmail = ?";
                try (PreparedStatement pst = con.prepareStatement(savedQuery)) {
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    boolean hasSavedJobs = false;

                    while (rs.next()) {
                        hasSavedJobs = true;

                        int jobId = rs.getInt("id");
                        String SCompName = rs.getString("CompanyName");
                        String SLocation = rs.getString("Location");
                        String companyInfo = SCompName + " | " + SLocation;
                        String SJobType = rs.getString("JobType");
                        String SSkillsRequired = rs.getString("SkillsRequired");
                        String SSalary = rs.getString("Salary");
                        String SPostedDate = rs.getString("PostedDate");
                        String employerEmail = rs.getString("employerEmail");
                        String status = "Unknown";
                        String companyEmail = rs.getString("companyEmail");

                        String jobIdNo = String.valueOf(jobId);

                        String statusQuery = "SELECT status, companyLogo, logoFileName FROM jobs WHERE id = ?";
                        ImageIcon logoIcon = null;
                        try (PreparedStatement pstStatus = con.prepareStatement(statusQuery)) {
                            pstStatus.setInt(1, jobId);
                            ResultSet rsStatus = pstStatus.executeQuery();
                            if (rsStatus.next()) {
                                status = rsStatus.getString("status");
                                Blob logoBlob = rsStatus.getBlob("companyLogo");
                                if (logoBlob != null) {
                                    byte[] imgBytes = logoBlob.getBytes(1, (int) logoBlob.length());
                                    Image img = new ImageIcon(imgBytes).getImage()
                                            .getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                                    logoIcon = new ImageIcon(img);
                                }
                            }
                        }

                        // Saved card panel
                        JPanel savedCardPanel = new JPanel(null);
                        savedCardPanel.setBackground(Color.WHITE);
                        savedCardPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 15)),
                                BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                                )
                        ));
                        savedCardPanel.setPreferredSize(new Dimension(270, 160));

                        // Logo button
                        JButton logoButton = new JButton();
                        logoButton.setBackground(new Color(248, 249, 250));
                        logoButton.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                                BorderFactory.createEmptyBorder(2, 2, 2, 2)
                        ));
                        logoButton.setFocusPainted(false);
                        logoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        logoButton.setBounds(10, 10, 60, 60);
                        savedCardPanel.add(logoButton);

                        if (logoIcon != null) {
                            logoButton.setIcon(logoIcon);
                        } else {
                            logoButton.setText("🏢");
                            logoButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                        }

                        logoButton.addActionListener(p -> new CompanyProfile(companyEmail, false, "",false));

                        // Status label
                        JLabel statusLabel = createLabelWithProperties(status, new Font("Segoe UI", Font.BOLD, 9),
                                10, 75, 60, 18, savedCardPanel, null, true, SwingConstants.CENTER,
                                BorderFactory.createEmptyBorder(2, 6, 2, 6));

                        switch (status.toLowerCase()) {
                            case "open":
                                statusLabel.setBackground(new Color(34, 197, 94));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            case "closed":
                                statusLabel.setBackground(new Color(239, 68, 68));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            case "paused":
                                statusLabel.setBackground(new Color(251, 146, 60));
                                statusLabel.setForeground(Color.WHITE);
                                break;
                            default:
                                statusLabel.setBackground(new Color(107, 114, 128));
                                statusLabel.setForeground(Color.WHITE);
                        }

                        // Job id label
                        JLabel jobIdLabel = createLabel("Job Id : " + jobIdNo,new Font("Segoe UI", Font.BOLD, 12),
                                10,95,150,20,savedCardPanel);
                        jobIdLabel.setForeground(new Color(192, 192, 192));

                        int labelX = 80;

                        // Company info label
                        JLabel companyInfoLabel = createLabel(companyInfo, new Font("Segoe UI", Font.BOLD, 12),
                                labelX, 8, 170, 18, savedCardPanel);
                        companyInfoLabel.setForeground(new Color(17, 24, 39));

                        // Job type label
                        JLabel jobTypeLabel = createLabel("• " + SJobType, new Font("Segoe UI", Font.PLAIN, 10),
                                labelX, 28, 170, 16, savedCardPanel);
                        jobTypeLabel.setForeground(new Color(75, 85, 99));

                        // Skills label
                        JLabel skillsLabel = createLabel("• " + (SSkillsRequired.length() > 40 ?
                                        SSkillsRequired.substring(0, 40) + "..." : SSkillsRequired),
                                new Font("Segoe UI", Font.PLAIN, 9), labelX, 46, 250, 14, savedCardPanel);
                        skillsLabel.setForeground(new Color(107, 114, 128));

                        // Salary label
                        JLabel salaryLabel = createLabel("• ₹" + SSalary + "/month", new Font("Segoe UI", Font.BOLD, 10),
                                labelX, 62, 170, 16, savedCardPanel);
                        salaryLabel.setForeground(new Color(16, 185, 129));

                        // Posted date label
                        JLabel postedDateLabel = createLabel("• Saved: " + SPostedDate, new Font("Segoe UI", Font.PLAIN, 10),
                                labelX, 80, 170, 14, savedCardPanel);
                        postedDateLabel.setForeground(new Color(156, 163, 175));

                        // Apply button
                        JButton applyButton = createStyledButton("Apply", new Color(40, 167, 69), Color.WHITE, 9);
                        applyButton.setBounds(95, 120, 60, 24);
                        applyButton.addActionListener(g -> new ApplyForm(email, SCompName, SLocation, SJobType, SSkillsRequired, SSalary, SPostedDate, jobId, employerEmail,companyEmail));
                        savedCardPanel.add(applyButton);

                        // Report button
                        JButton reportButton = createStyledButton("🚩", new Color(239, 68, 68), Color.WHITE, 11);
                        reportButton.setBounds(170, 120, 35, 24);
                        reportButton.setFont(emojiFont);
                        reportButton.addActionListener(h -> new ReportPanel(jobId, email, employerEmail, "employer", "Jobseeker"));
                        savedCardPanel.add(reportButton);

                        // Chat button
                        JButton chatButton = createStyledButton("💬", new Color(59, 130, 246), Color.WHITE, 11);
                        chatButton.setBounds(220, 120, 35, 24);
                        chatButton.setFont(emojiFont);
                        chatButton.addActionListener(g -> new JSChatSys(email, employerEmail));
                        savedCardPanel.add(chatButton);

                        savedJobsPanel.add(savedCardPanel);
                    }

                    if (!hasSavedJobs) {
                        JLabel noSavedLabel = new JLabel("You haven’t saved any job applications yet.");
                        noSavedLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        noSavedLabel.setForeground(new Color(150, 150, 150));
                        noSavedLabel.setHorizontalAlignment(SwingConstants.CENTER);

                        JPanel noSavedPanel = new JPanel(new BorderLayout());
                        noSavedPanel.setPreferredSize(new Dimension(560, 100));
                        noSavedPanel.add(noSavedLabel, BorderLayout.CENTER);

                        savedJobsPanel.setLayout(new BorderLayout());
                        savedJobsPanel.add(noSavedPanel, BorderLayout.CENTER);
                    }
                }

                savedJobsPanel.revalidate();
                savedJobsPanel.repaint();
                jobseekerCardLayout.show(jobSeekerPanel, "Saved Jobs");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Profile button
        JButton profileButton = new JButton("Profile");
        profileButton.setContentAreaFilled(false);
        profileButton.setOpaque(false);
        profileButton.setFont(new Font("Calibri", Font.BOLD, 14));
        profileButton.setForeground(Color.BLACK);
        profileButton.setFocusPainted(false);
        profileButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        profileButton.setHorizontalAlignment(SwingConstants.CENTER);
        profileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jobSeekerContainer.add(profileButton);
        profileButton.setBounds(0, 210, 190, 30);
        profileButton.setFont(buttonFont);

        profileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                profileButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                profileButton.setForeground(Color.BLACK);
            }
        });

        profileButton.addActionListener(a -> {

            // Database connection
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String JSProfile = "SELECT * FROM UserProfile WHERE JSEmail = ?";
                try (PreparedStatement pst = con.prepareStatement(JSProfile)) {
                    pst.setString(1, email);
                    ResultSet rs = pst.executeQuery();

                    // Profile Panel
                    JPanel profilePanel = new JPanel(null);
                    profilePanel.setBackground(new Color(248, 250, 252));
                    profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    // Header Panel
                    JPanel headerPanel = new JPanel(null);
                    headerPanel.setBackground(Color.WHITE);
                    headerPanel.setBounds(10, 10, 571, 80);
                    headerPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                            BorderFactory.createEmptyBorder(10, 15, 10, 15)
                    ));

                    // Profile heading panel (My profile)
                    JLabel profileHeadingPanel = createLabel("My Profile", new Font("Segoe UI", Font.BOLD, 24), 15, 20, 200, 35, headerPanel);
                    profileHeadingPanel.setForeground(Color.BLACK);

                    // Profile subtext panel
                    JLabel profileSubtextPanel = createLabel("Manage your professional information", new Font("Segoe UI", Font.PLAIN, 12), 15, 45, 300, 20, headerPanel);
                    profileSubtextPanel.setForeground(Color.BLACK);

                    profilePanel.add(headerPanel);

                    // Profile Information panel
                    JPanel profileInfoPanel = new JPanel(null);
                    profileInfoPanel.setBackground(Color.WHITE);
                    profileInfoPanel.setBounds(10, 100, 571, 120);
                    profileInfoPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                            BorderFactory.createEmptyBorder(15, 20, 15, 20)
                    ));
                    Color fieldBorderColor = new Color(209, 213, 219);

                    // Profile Picture label
                    JLabel profilePicLabel;

                    // Stores selected resume file
                    AtomicReference<File> selectedFile = new AtomicReference<>();

                    // Image icon
                    ImageIcon profileIcon = new ImageIcon();

                    // Profile Picture Styling
                    if (profileIcon.getImage() != null) {
                        Image scaledImg = profileIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        profilePicLabel = new JLabel(new ImageIcon(scaledImg));
                    } else {
                        profilePicLabel = new JLabel("📷");
                        profilePicLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                        profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        profilePicLabel.setVerticalAlignment(SwingConstants.CENTER);
                        profilePicLabel.setForeground(new Color(156, 163, 175));
                    }
                    profilePicLabel.setBounds(10, 10, 80, 80);
                    profilePicLabel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(229, 231, 235), 2),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    profilePicLabel.setOpaque(true);
                    profilePicLabel.setBackground(new Color(249, 250, 251));
                    profileInfoPanel.add(profilePicLabel);

                    Color labelColor = new Color(55, 65, 81);

                    // Full name label
                    JLabel nameLabel = createLabel("Full Name", labelFont, 170, 8, 80, 20, profileInfoPanel);
                    nameLabel.setForeground(labelColor);

                    // Name TextField
                    JTextField nameTextField = new JTextField();
                    nameTextField.setFont(fieldFont);
                    nameTextField.setBounds(165, 32, 180, 25);
                    nameTextField.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(fieldBorderColor, 1),
                            BorderFactory.createEmptyBorder(3, 8, 3, 8)
                    ));
                    profileInfoPanel.add(nameTextField);

                    // Email label
                    JLabel emailLabel = createLabel("Email", labelFont, 400, 8, 60, 20, profileInfoPanel);
                    emailLabel.setForeground(labelColor);

                    // Email display label
                    JLabel emailDisplayLabel = createLabel(email, fieldFont, 400, 35, 150, 20, profileInfoPanel);
                    emailDisplayLabel.setForeground(new Color(107, 114, 128));


                    // Phone label
                    JLabel phoneLabel = createLabel("Phone", labelFont, 170, 57, 80, 20, profileInfoPanel);
                    phoneLabel.setForeground(labelColor);

                    // Phone TextField
                    JTextField phoneTextField = new JTextField();
                    phoneTextField.setFont(fieldFont);
                    phoneTextField.setBounds(165, 80, 180, 25);
                    phoneTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fieldBorderColor, 1),
                            BorderFactory.createEmptyBorder(3, 8, 3, 8)
                    ));
                    profileInfoPanel.add(phoneTextField);


                    // Location label
                    JLabel locationLabel = createLabel("Location", labelFont, 400, 57, 80, 20, profileInfoPanel);
                    locationLabel.setForeground(labelColor);

                    // Location TextField
                    JTextField locationTextField = new JTextField();
                    locationTextField.setFont(fieldFont);
                    locationTextField.setBounds(400, 80, 130, 25);
                    locationTextField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fieldBorderColor, 1),
                            BorderFactory.createEmptyBorder(3, 8, 3, 8)
                    ));
                    profileInfoPanel.add(locationTextField);


                    // Headline TextField
                    JTextField headlineField = new JTextField();

                    // Summary textarea
                    JTextArea summaryArea = new JTextArea();

                    // Skills textarea
                    JTextArea skillsArea = new JTextArea();

                    // Stores education details
                    Object[][] eduData = {{"", "", ""}};


                    // Load existing profile data
                    if (rs.next()) {
                        nameTextField.setText(rs.getString("JSFullName"));
                        phoneTextField.setText(rs.getString("JSPhone"));
                        locationTextField.setText(rs.getString("JSLocation"));
                        headlineField.setText(rs.getString("JSHeadline"));
                        summaryArea.setText(rs.getString("JSSummary"));
                        skillsArea.setText(rs.getString("JSSkills"));
                        eduData[0][0] = rs.getString("JSDegree");
                        eduData[0][1] = rs.getString("JSInstitution");
                        eduData[0][2] = rs.getString("JSYear");

                        byte[] imageBytes = rs.getBytes("JSProfileImage");
                        if (imageBytes != null && imageBytes.length > 0) {
                            ImageIcon profileIconn = new ImageIcon(imageBytes);
                            Image scaledImg = profileIconn.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            profilePicLabel.setIcon(new ImageIcon(scaledImg));
                            profilePicLabel.setText("");
                        }
                    }

                    // Upload Photo Button
                    JButton uploadImageButton = new JButton("Update Photo");
                    uploadImageButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                    uploadImageButton.setForeground(Color.WHITE);
                    uploadImageButton.setBackground(new Color(0, 123, 255));
                    uploadImageButton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(59, 130, 246), 1),
                            BorderFactory.createEmptyBorder(4, 8, 4, 8)
                    ));
                    uploadImageButton.setFocusPainted(false);
                    uploadImageButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    uploadImageButton.setBounds(10, 90, 90, 25);
                    profileInfoPanel.add(uploadImageButton);

                    profilePanel.add(profileInfoPanel);

                    // Handles pdf upload
                    uploadImageButton.addActionListener(e -> {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
                        int result = chooser.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            selectedFile.set(chooser.getSelectedFile());
                            try {
                                Image img = ImageIO.read(selectedFile.get());
                                Image scaled = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                profilePicLabel.setIcon(new ImageIcon(scaled));
                                profilePicLabel.setText("");
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    // Profile tab section
                    JTabbedPane profileTabs = new JTabbedPane(JTabbedPane.TOP);
                    profileTabs.setBounds(10, 230, 571, 180);
                    profileTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    profileTabs.setBackground(Color.WHITE);
                    profileTabs.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

                    // Summary Tab
                    JPanel summaryTab = new JPanel(null);
                    summaryTab.setBackground(Color.WHITE);
                    summaryTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    // Headline label
                    JLabel headlineLabel = createLabel("Professional Headline", labelFont, 15, 15, 150, 20, summaryTab);
                    headlineLabel.setForeground(labelColor);

                    headlineField.setFont(fieldFont);
                    headlineField.setBounds(15, 35, 520, 25);
                    headlineField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(fieldBorderColor, 1),
                            BorderFactory.createEmptyBorder(3, 8, 3, 8)
                    ));
                    summaryTab.add(headlineField);

                    // Summary label
                    JLabel summaryLabel = createLabel("Professional Summary", labelFont, 15, 70, 150, 20, summaryTab);
                    summaryLabel.setForeground(labelColor);

                    summaryArea.setFont(fieldFont);
                    summaryArea.setLineWrap(true);
                    summaryArea.setWrapStyleWord(true);

                    // Allows scrolling for summary textarea
                    JScrollPane summaryScroll = new JScrollPane(summaryArea);
                    summaryScroll.setBounds(15, 90, 520, 60);
                    summaryScroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));

                    summaryTab.add(summaryScroll);

                    // Education tab panel
                    JPanel educationTabPanel = new JPanel(new BorderLayout(10, 10));
                    educationTabPanel.setBackground(Color.WHITE);
                    educationTabPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    // Education table
                    JTable educationTable = new JTable(eduData, new String[]{"Degree", "Institution", "Year"});
                    educationTable.setFont(fieldFont);
                    educationTable.setRowHeight(30);
                    educationTable.getTableHeader().setFont(labelFont);
                    educationTable.getTableHeader().setBackground(new Color(249, 250, 251));
                    educationTable.getTableHeader().setForeground(labelColor);
                    educationTable.setGridColor(new Color(229, 231, 235));
                    educationTable.setSelectionBackground(new Color(239, 246, 255));

                    // Allows scrolling for education table
                    JScrollPane eduScroll = new JScrollPane(educationTable);
                    eduScroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
                    educationTabPanel.add(eduScroll, BorderLayout.CENTER);

                    // Skills Tab
                    JPanel skillsTab = new JPanel(null);
                    skillsTab.setBackground(Color.WHITE);
                    skillsTab.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                    // Skills label
                    JLabel skillsLabel = createLabel("Technical Skills (comma-separated)", labelFont, 15, 15, 200, 20, skillsTab);
                    skillsLabel.setForeground(labelColor);

                    skillsArea.setFont(fieldFont);
                    skillsArea.setLineWrap(true);
                    skillsArea.setWrapStyleWord(true);
                    JScrollPane skillsScroll = new JScrollPane(skillsArea);
                    skillsScroll.setBounds(15, 35, 520, 110);
                    skillsScroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
                    skillsTab.add(skillsScroll);

                    profileTabs.addTab("Summary", summaryTab);
                    profileTabs.addTab("Education", educationTabPanel);
                    profileTabs.addTab("Skills", skillsTab);

                    profilePanel.add(profileTabs);

                    // Button panel
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
                    buttonPanel.setBackground(new Color(248, 250, 252));
                    buttonPanel.setBounds(10, 420, 571, 45);

                    // Save button
                    JButton saveButton = new JButton("Save Profile");
                    saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    saveButton.setForeground(Color.WHITE);
                    saveButton.setBackground(new Color(40, 167, 69));
                    saveButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(21, 128, 61), 1),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));
                    saveButton.setFocusPainted(false);
                    saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    saveButton.addActionListener(f -> {
                        try (Connection con2 = DriverManager.getConnection(url, user, password)) {
                            String check = "SELECT JSEmail FROM UserProfile WHERE JSEmail=?";
                            try (PreparedStatement checkStmt = con2.prepareStatement(check)) {
                                checkStmt.setString(1, email);
                                ResultSet checkRs = checkStmt.executeQuery();

                                boolean isUpdate = checkRs.next();
                                String query;

                                if (isUpdate) {
                                    if (selectedFile.get() != null) {
                                        // Update with image
                                        query = "UPDATE UserProfile SET JSProfileImage=?, JSFullName=?, JSPhone=?, JSLocation=?, JSHeadline=?, JSSummary=?, JSDegree=?, JSInstitution=?, JSYear=?, JSSkills=? WHERE JSEmail=?";
                                    } else {
                                        // Update without changing image
                                        query = "UPDATE UserProfile SET JSFullName=?, JSPhone=?, JSLocation=?, JSHeadline=?, JSSummary=?, JSDegree=?, JSInstitution=?, JSYear=?, JSSkills=? WHERE JSEmail=?";
                                    }
                                } else {
                                    // Insert new profile
                                    query = "INSERT INTO UserProfile(JSProfileImage, JSFullName, JSPhone, JSLocation, JSHeadline, JSSummary, JSDegree, JSInstitution, JSYear, JSSkills, JSEmail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                }

                                try (PreparedStatement pst2 = con2.prepareStatement(query)) {
                                    int index = 1;

                                    if (isUpdate && selectedFile.get() != null) {
                                        // Only set image if new file is selected
                                        FileInputStream fis = new FileInputStream(selectedFile.get());
                                        pst2.setBinaryStream(index++, fis, (int) selectedFile.get().length());
                                    } else if (!isUpdate) {
                                        // Insert: always set image (may be null)
                                        if (selectedFile.get() != null) {
                                            FileInputStream fis = new FileInputStream(selectedFile.get());
                                            pst2.setBinaryStream(index++, fis, (int) selectedFile.get().length());
                                        } else {
                                            pst2.setNull(index++, Types.BLOB);
                                        }
                                    }

                                    // Set rest of the fields
                                    pst2.setString(index++, nameTextField.getText());
                                    pst2.setString(index++, phoneTextField.getText());
                                    pst2.setString(index++, locationTextField.getText());
                                    pst2.setString(index++, headlineField.getText());
                                    pst2.setString(index++, summaryArea.getText());

                                    if (educationTable.isEditing()) {
                                        educationTable.getCellEditor().stopCellEditing();
                                    }
                                    pst2.setString(index++, (String) educationTable.getValueAt(0, 0));
                                    pst2.setString(index++, (String) educationTable.getValueAt(0, 1));
                                    pst2.setString(index++, (String) educationTable.getValueAt(0, 2));

                                    pst2.setString(index++, skillsArea.getText());
                                    pst2.setString(index++, email);

                                    pst2.executeUpdate();

                                    // Add activity
                                    String activities = "INSERT INTO activities (empEmail, activity, user) VALUES (?,?,?)";
                                    try (PreparedStatement pst6 = con2.prepareStatement(activities)) {
                                        pst6.setString(1, email);
                                        pst6.setString(2, email + " has updated their profile");
                                        pst6.setString(3, "JobSeeker");
                                        pst6.executeUpdate();
                                    }

                                    JOptionPane.showMessageDialog(null,
                                            "Profile updated successfully!",
                                            "Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null,
                                    "Error saving profile: " + ex.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    });


                    buttonPanel.add(saveButton);
                    profilePanel.add(buttonPanel);

                    jobSeekerPanel.add(profilePanel, "Profile");
                    jobseekerCardLayout.show(jobSeekerPanel, "Profile");
                    profilePanel.revalidate();
                    profilePanel.repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Database connection error: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            // Query for Applications Submitted
            String submittedQuery = "SELECT COUNT(*) AS totalApps FROM appliedjobs WHERE UserEmail = ?";
            int applicationsSubmitted = 0;
            try (PreparedStatement pst1 = con.prepareStatement(submittedQuery)) {
                pst1.setString(1, email);
                ResultSet rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    applicationsSubmitted = rs1.getInt("totalApps");
                }
            }

            // Query for Interviews Pending from interviewschedule table
            String interviewQuery = "SELECT COUNT(*) AS totalInterviews FROM interviewschedule WHERE jobseeker_email = ?";
            int interviewsPending = 0;
            try (PreparedStatement pst2 = con.prepareStatement(interviewQuery)) {
                pst2.setString(1, email);
                ResultSet rs2 = pst2.executeQuery();
                if (rs2.next()) {
                    interviewsPending = rs2.getInt("totalInterviews");
                }
            }

            // Applications Submitted Panel
            JPanel applicationSubmittedPanel = new JPanel();
            applicationSubmittedPanel.setLayout(null);
            applicationSubmittedPanel.setBackground(new Color(248, 249, 250));
            applicationSubmittedPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));

            JLabel applicationsLabel = createLabel("Applications", new Font("Segoe UI", Font.BOLD, 12),
                    10, 5, 100, 15, applicationSubmittedPanel);
            applicationsLabel.setForeground(new Color(70, 70, 70));

            JLabel submittedLabel = createLabel("Submitted", new Font("Segoe UI", Font.BOLD, 12),
                    10, 20, 100, 15, applicationSubmittedPanel);
            submittedLabel.setForeground(new Color(100, 100, 100));

            JLabel applicationsValueLabel = createLabel(String.valueOf(applicationsSubmitted),
                    new Font("Segoe UI", Font.BOLD, 24), 120, 10, 40, 30, applicationSubmittedPanel);
            applicationsValueLabel.setForeground(new Color(52, 152, 219));

            // Interviews Pending Panel
            JPanel interviewPendingPanel = new JPanel();
            interviewPendingPanel.setLayout(null);
            interviewPendingPanel.setBackground(new Color(248, 249, 250));
            interviewPendingPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));

            JLabel interviewsLabel = createLabel("Interviews", new Font("Segoe UI", Font.BOLD, 12),
                    10, 5, 100, 15, interviewPendingPanel);
            interviewsLabel.setForeground(new Color(70, 70, 70));

            JLabel pendingLabel = createLabel("Pending", new Font("Segoe UI", Font.BOLD, 12),
                    10, 20, 100, 15, interviewPendingPanel);
            pendingLabel.setForeground(new Color(100, 100, 100));

            JLabel interviewsPendingValuesLabel = createLabel(String.valueOf(interviewsPending),
                    new Font("Segoe UI", Font.BOLD, 24), 120, 10, 40, 30, interviewPendingPanel);
            interviewsPendingValuesLabel.setForeground(new Color(46, 204, 113));

            // Add both panels
            jobSeekerContainer.add(applicationSubmittedPanel);
            jobSeekerContainer.add(interviewPendingPanel);
            applicationSubmittedPanel.setBounds(10, 399, 170, 50);
            interviewPendingPanel.setBounds(10, 460, 170, 50);

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Sidebar job trends button
        JButton jobTrends = new JButton("Job Trends");
        jobTrends.setContentAreaFilled(false);
        jobTrends.setOpaque(false);
        jobTrends.setFont(new Font("Calibri", Font.BOLD, 14));
        jobTrends.setForeground(Color.BLACK);
        jobTrends.setFocusPainted(false);
        jobTrends.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        jobTrends.setHorizontalAlignment(SwingConstants.CENTER);
        jobTrends.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jobTrends.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                jobTrends.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                jobTrends.setForeground(Color.BLACK);
            }
        });
        jobSeekerContainer.add(jobTrends);
        jobTrends.setBounds(0, 240, 190, 30);

        // Job Trends panel
        JPanel jobTrendsPanel = new JPanel();
        jobTrendsPanel.setBackground(new Color(248, 249, 250));
        jobTrendsPanel.setLayout(null);
        jobTrendsPanel.setPreferredSize(new Dimension(1200, 900));

        // Allows scrolling for job trends panel
        JScrollPane jobTrendsScrollPane = new JScrollPane(jobTrendsPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jobTrendsScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        jobTrendsScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jobSeekerPanel.add(jobTrendsScrollPane, "Job Trends");

        jobTrends.addActionListener(e -> {
            jobTrendsPanel.removeAll();

            Map<String, Integer> skillsFrequency = new HashMap<>();
            Map<String, Integer> positionsFrequency = new HashMap<>();

            try (Connection connection = DriverManager.getConnection(url, user, password);
                 Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery("SELECT requirement, position FROM jobs")) {

                while (rs.next()) {
                    String requirement = rs.getString("requirement");
                    String position = rs.getString("position");

                    if (requirement != null && !requirement.trim().isEmpty()) {
                        String[] skills = requirement.split("[\\s\\n]+");
                        for (String skill : skills) {
                            if (!skill.trim().isEmpty()) {
                                String normalizedSkill = skill.trim().toLowerCase();
                                skillsFrequency.put(normalizedSkill, skillsFrequency.getOrDefault(normalizedSkill, 0) + 1);
                            }
                        }
                    }

                    if (position != null && !position.trim().isEmpty()) {
                        String normalizedPosition = position.trim();
                        positionsFrequency.put(normalizedPosition, positionsFrequency.getOrDefault(normalizedPosition, 0) + 1);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error fetching job trends data: " + ex.getMessage());
                return;
            }

            // Skills chart panel
            JPanel skillsChartPanel = createBarChartPanel("Top Skills in Demand", skillsFrequency);
            skillsChartPanel.setBackground(Color.WHITE);
            skillsChartPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            skillsChartPanel.setBounds(50, 50, 1100, 400);
            jobTrendsPanel.add(skillsChartPanel);

            // Positions chart panel
            JPanel positionsChartPanel = createBarChartPanel("Job Positions Trend", positionsFrequency);
            positionsChartPanel.setBackground(Color.WHITE);
            positionsChartPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            positionsChartPanel.setBounds(50, 500, 1100, 400);
            jobTrendsPanel.add(positionsChartPanel);
            jobTrendsPanel.revalidate();
            jobTrendsPanel.repaint();
            jobseekerCardLayout.show(jobSeekerPanel, "Job Trends");
        });


        // Rate & Feedback button
        JButton rateFeedbackButton = new JButton("Rate & Feedback");
        rateFeedbackButton.setContentAreaFilled(false);
        rateFeedbackButton.setOpaque(false);
        rateFeedbackButton.setFont(new Font("Calibri", Font.BOLD, 14));
        rateFeedbackButton.setForeground(Color.BLACK);
        rateFeedbackButton.setFocusPainted(false);
        rateFeedbackButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        rateFeedbackButton.setHorizontalAlignment(SwingConstants.CENTER);
        rateFeedbackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateFeedbackButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                rateFeedbackButton.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rateFeedbackButton.setBackground(new Color(59, 130, 246));
            }
        });
        rateFeedbackButton.setBounds(0, 270, 190, 30);;
        jobSeekerContainer.add(rateFeedbackButton);

        // Rate & Feedback panel
        JPanel rateFeedbackPanel = new JPanel();
        rateFeedbackPanel.setBackground(new Color(248, 249, 250));
        rateFeedbackPanel.setLayout(null);

        // Title label
        JLabel titleLabelRF = createLabel("Rate & Feedback", new Font("Segoe UI", Font.BOLD, 22), 200, 20, 300, 30, rateFeedbackPanel);
        titleLabelRF.setForeground(new Color(17, 24, 39));

        // Rating label
        JLabel ratingLabel = createLabel("Your Rating:", new Font("Segoe UI", Font.PLAIN, 14), 50, 80, 120, 25, rateFeedbackPanel);

        // Stars Label
        JLabel[] starsLabel = new JLabel[5];
        int[] rating = {0}; // store selected rating

        // Handles hovering and selection of stars
        for (int i = 0; i < starsLabel.length; i++) {
            starsLabel[i] = new JLabel("☆", SwingConstants.CENTER);
            starsLabel[i].setFont(starFont);
            starsLabel[i].setBounds(150 + (i * 50), 70, 40, 40);
            starsLabel[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final int starValue = i + 1;

            starsLabel[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    rating[0] = starValue;
                    for (int j = 0; j < starsLabel.length; j++) {
                        starsLabel[j].setText(j < starValue ? "★" : "☆");
                        starsLabel[j].setForeground(j < starValue ? new Color(251, 191, 36) : new Color(156, 163, 175));
                    }
                }
            });
            rateFeedbackPanel.add(starsLabel[i]);
        }

        // Like label
        JLabel likeLabel = createLabel("What did you like the most?", new Font("Segoe UI", Font.PLAIN, 14), 50, 130, 250, 25, rateFeedbackPanel);

        // Like textarea
        JTextArea likeTextArea = new JTextArea();
        likeTextArea.setLineWrap(true);
        likeTextArea.setWrapStyleWord(true);
        likeTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        likeTextArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        JScrollPane likeScroll = new JScrollPane(likeTextArea);
        likeScroll.setBounds(50, 160, 480, 70);
        rateFeedbackPanel.add(likeScroll);

        // Dislike label
        JLabel dislikeLabel = createLabel("What could be improved?", new Font("Segoe UI", Font.PLAIN, 14), 50, 240, 250, 25, rateFeedbackPanel);

        // Dislike textarea
        JTextArea dislikeTextArea = new JTextArea();
        dislikeTextArea.setLineWrap(true);
        dislikeTextArea.setWrapStyleWord(true);
        dislikeTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dislikeTextArea.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        JScrollPane dislikeScroll = new JScrollPane(dislikeTextArea);
        dislikeScroll.setBounds(50, 270, 480, 70);
        rateFeedbackPanel.add(dislikeScroll);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(new Color(40, 167, 69));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        submitButton.setBounds(428, 360, 100, 35);
        rateFeedbackPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            if (rating[0] == 0) {
                JOptionPane.showMessageDialog(null, "Please select a rating!");
                return;
            }
            String likeText = likeTextArea.getText().trim();
            String dislikeText = dislikeTextArea.getText().trim();
            if (likeText.isEmpty() || dislikeText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out both feedback fields!");
                return;
            }

            String sql = "INSERT INTO JSfeedback (userEmail, rating, likes, dislikes) VALUES (?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = con.prepareStatement(sql)) {

                pst.setString(1, email);
                pst.setInt(2, rating[0]);
                pst.setString(3, likeText);
                pst.setString(4, dislikeText);

                pst.executeUpdate();
                String activites = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                try(PreparedStatement pst6 = con.prepareStatement(activites)){
                    pst6.setString(1,email);
                    pst6.setString(2,email + " has rated " + rating[0] + " for the app" );
                    pst6.setString(3,"JobSeeker");
                    pst6.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "Thank you for your feedback!");

                likeTextArea.setText("");
                dislikeTextArea.setText("");
                rating[0] = 0;
                for (JLabel star : starsLabel) {
                    star.setText("☆");
                    star.setForeground(Color.GRAY);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving feedback to database!");
            }
        });
        jobSeekerPanel.add(rateFeedbackPanel, "RateFeedback");

        rateFeedbackButton.addActionListener(e -> jobseekerCardLayout.show(jobSeekerPanel, "RateFeedback"));

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBounds(80, 525, 100, 30);
        logoutButton.setFont(buttonFont);
        jobSeekerContainer.add(logoutButton);

        logoutButton.addActionListener(a -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                new LandingPage();
                dispose();
            }
        });

        // Frame settings
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Job Seeker Dashboard");
        setLayout(null);
    }

    public static void main(String[] args) {
        JobSeekerDashboard object9 = new JobSeekerDashboard("User","","yashxyz@gmail.com");
    }
}