import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import javax.imageio.ImageIO;
import javax.activation.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class EmployerDashboard extends JFrame {

    // Suggests position based on skills
    public String getSuggestedPosition(String[] skills) {
        if (skills == null || skills.length == 0 || skills[0] == null || skills[0].isEmpty()) {
            return "Software Engineer";
        }

        skills[0] = skills[0].toLowerCase();

        // Technical
        if (skills[0].contains("java") && skills[0].contains("spring")) {
            return "Java Backend Developer";
        } else if ((skills[0].contains("react") || skills[0].contains("javascript")) && skills[0].contains("html")) {
            return "Frontend Developer";
        } else if ((skills[0].contains("python") && skills[0].contains("machine learning")) || skills[0].contains("nlp")) {
            return "Machine Learning Engineer";
        } else if (skills[0].contains("data science") || (skills[0].contains("python") && skills[0].contains("pandas"))) {
            return "Data Scientist";
        } else if ((skills[0].contains("sql") || skills[0].contains("mysql") || skills[0].contains("oracle")) && skills[0].contains("database")) {
            return "Database Administrator";
        } else if ((skills[0].contains("html") && skills[0].contains("css")) && (skills[0].contains("design") || skills[0].contains("ui") || skills[0].contains("ux"))) {
            return "UI/UX Designer";
        } else if (skills[0].contains("android") || skills[0].contains("kotlin")) {
            return "Android Developer";
        } else if (skills[0].contains("ios") || skills[0].contains("swift")) {
            return "iOS Developer";
        } else if ((skills[0].contains(("react")) || skills[0].contains("node") || skills[0].contains("express")) && skills[0].contains("mongodb")) {
            return "MERN Stack Developer";
        } else if (skills[0].contains("node") || skills[0].contains("express")) {
            return "Node.js Developer";
        } else if ((skills[0].contains("c++") || skills[0].contains("c#")) && skills[0].contains("game")) {
            return "Game Developer";
        } else if (skills[0].contains("devops") || skills[0].contains("docker") || skills[0].contains("kubernetes")) {
            return "DevOps Engineer";
        } else if (skills[0].contains("cloud") || skills[0].contains("aws") || skills[0].contains("azure") || skills[0].contains("gcp")) {
            return "Cloud Engineer";
        } else if (skills[0].contains("cybersecurity") || skills[0].contains("network security")) {
            return "Cybersecurity Analyst";
        } else if ((skills[0].contains("flutter") || skills[0].contains("dart"))) {
            return "Flutter Developer";
        } else if ((skills[0].contains("wordpress") || skills[0].contains("seo"))) {
            return "Web Content Developer";
        } else if (skills[0].contains("ai") || skills[0].contains("artificial intelligence")) {
            return "AI Engineer";
        } else if (skills[0].contains("blockchain") || skills[0].contains("solidity") || skills[0].contains("web3")) {
            return "Blockchain Developer";
        } else if (skills[0].contains("testing") || skills[0].contains("selenium") || skills[0].contains("junit")) {
            return "QA/Test Engineer";

            // Non-Technical
        } else if (skills[0].contains("accounting") || skills[0].contains("finance") || skills[0].contains("tally")) {
            return "Accountant";
        } else if (skills[0].contains("marketing") || skills[0].contains("digital marketing") || skills[0].contains("seo") || skills[0].contains("ads")) {
            return "Marketing Specialist";
        } else if (skills[0].contains("sales") || skills[0].contains("business development")) {
            return "Sales Executive";
        } else if (skills[0].contains("hr") || skills[0].contains("human resources") || skills[0].contains("recruitment")) {
            return "HR Manager";
        } else if (skills[0].contains("teacher") || skills[0].contains("tutor") || skills[0].contains("education")) {
            return "Teacher/Educator";
        } else if (skills[0].contains("nurse") || skills[0].contains("healthcare") || skills[0].contains("hospital management")) {
            return "Nurse/Healthcare Professional";
        } else if (skills[0].contains("doctor") || skills[0].contains("mbbs") || skills[0].contains("physician")) {
            return "Doctor";
        } else if (skills[0].contains("mechanical") || skills[0].contains("cad") || skills[0].contains("solidworks")) {
            return "Mechanical Engineer";
        } else if (skills[0].contains("civil") || skills[0].contains("autocad") || skills[0].contains("construction")) {
            return "Civil Engineer";
        } else if (skills[0].contains("electrical") || skills[0].contains("electronics") || skills[0].contains("circuits")) {
            return "Electrical Engineer";
        } else if (skills[0].contains("law") || skills[0].contains("legal") || skills[0].contains("advocate")) {
            return "Lawyer/Legal Advisor";
        } else if (skills[0].contains("content writing") || skills[0].contains("copywriting") || skills[0].contains("blogging")) {
            return "Content Writer";
        } else if (skills[0].contains("graphic design") || skills[0].contains("photoshop") || skills[0].contains("illustrator")) {
            return "Graphic Designer";
        } else if (skills[0].contains("project management") || skills[0].contains("pmp") || skills[0].contains("scrum")) {
            return "Project Manager";
        } else if (skills[0].contains("chef") || skills[0].contains("cooking") || skills[0].contains("culinary")) {
            return "Chef";
        } else if (skills[0].contains("fashion") || skills[0].contains("textile") || skills[0].contains("apparel")) {
            return "Fashion Designer";
        } else if (skills[0].contains("music") || skills[0].contains("composer") || skills[0].contains("instrument")) {
            return "Musician";
        } else if (skills[0].contains("photography") || skills[0].contains("camera") || skills[0].contains("editing")) {
            return "Photographer";
        } else if (skills[0].contains("journalism") || skills[0].contains("reporting") || skills[0].contains("media")) {
            return "Journalist";
        } else if (skills[0].contains("aviation") || skills[0].contains("pilot") || skills[0].contains("aircraft")) {
            return "Pilot/Aviation Professional";
        } else if (skills[0].contains("banking") || skills[0].contains("loan") || skills[0].contains("investment")) {
            return "Banking Officer";
        } else if (skills[0].contains("data entry") || skills[0].contains("typing") || skills[0].contains("ms office")) {
            return "Data Entry Operator";
        } else if (skills[0].contains("supply chain") || skills[0].contains("logistics") || skills[0].contains("warehouse")) {
            return "Supply Chain Manager";
        } else if (skills[0].contains("customer service") || skills[0].contains("support") || skills[0].contains("call center")) {
            return "Customer Support Executive";
        } else if (skills[0].contains("entrepreneur") || skills[0].contains("startup") || skills[0].contains("business")) {
            return "Entrepreneur";
        } else {
            return "Professional";
        }

    }

    // Creates label
    private JLabel createLabel(String text, Font font, Color foreground, int horizontalAlignment, int x, int y, int width, int height, Border border, boolean opaque,
                                Color background, Container container
    ) {
        JLabel label = new JLabel(text, horizontalAlignment);
        if (font != null) label.setFont(font);
        if (foreground != null) label.setForeground(foreground);
        if (border != null) label.setBorder(border);
        if (opaque) {
            label.setOpaque(true);
            if (background != null) label.setBackground(background);
        }
        if (width > 0 && height > 0) {
            label.setBounds(x, y, width, height);
        }
        if (container != null) {
            container.add(label);
        }
        return label;
    }

    // Creates TextField
    private JTextField createTextField(String text, Font font, Color foreground, Color background, int x, int y, int width, int height, boolean editable, Border border, Container container
    ) {

        JTextField textField = new JTextField(text);
        if (font != null) textField.setFont(font);
        if (foreground != null) textField.setForeground(foreground);
        if (background != null) textField.setBackground(background);
        if (border != null) textField.setBorder(border);
        textField.setEditable(editable);
        if (width > 0 && height > 0) {
            textField.setBounds(x, y, width, height);
        }
        if (container != null) {
            container.add(textField);
        }
        return textField;
    }

    // Create application cards
    public JPanel createApplicantCard(String name, String email, String phone, String location, String jobRole, String degree, String institution, String year,
                                      String headline, String summary, int jobId, ImageIcon profileImageIcon, String[] skills, String companyName, String empEmail,
                                      String skillsRoles, String reason, Font buttonFont) {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        String jobIdNo = String.valueOf(jobId);

        // Applicant card panel
        JPanel applicantCardPanel = new JPanel(null);
        applicantCardPanel.setBackground(Color.WHITE);
        applicantCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        applicantCardPanel.setPreferredSize(new Dimension(280, 380));

        // Profile picture label
        JLabel profilePicLabel;
        if (profileImageIcon != null) {
            profilePicLabel = new JLabel(profileImageIcon);
        } else {
            profilePicLabel = new JLabel("👤");
            profilePicLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
            profilePicLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        profilePicLabel.setBounds(90, 10, 100, 100);
        applicantCardPanel.add(profilePicLabel);

        // Job id
        JLabel jobIdLabel = createLabel("Job Id : " + jobIdNo,new Font("Segoe UI", Font.BOLD, 14),new Color(192, 192, 192),
                SwingConstants.CENTER,180,10,100,20,null,false,null,applicantCardPanel);

        // Name label
        JLabel nameLabel = createLabel(name, new Font("Segoe UI", Font.BOLD, 16), new Color(40, 40, 40),
                SwingConstants.CENTER, 10, 110, 260, 20, null, false, null, applicantCardPanel);

        // Phone/Email label
        JLabel phoneLabel = createLabel(email + " | " + phone, new Font("Segoe UI", Font.PLAIN, 12), new Color(80, 80, 80),
                SwingConstants.CENTER, 10, 135, 260, 15, null, false, null, applicantCardPanel);

        // Job role + skills label
        JLabel jobRoleLabel = createLabel("<html><div style='text-align:center;'>Applied for " + jobRole + "<br>Skills required: " + skillsRoles + "</div></html>",
                new Font("Segoe UI", Font.PLAIN, 12), new Color(100, 100, 100), SwingConstants.CENTER,
                10, 150, 260, 40, null, false, null, applicantCardPanel);

        // Reason textarea
        if (reason == null || reason.trim().isEmpty()) reason = "No reason provided.";

        // Reason TextArea
        JTextArea reasonTextArea = new JTextArea();
        reasonTextArea.setLineWrap(true);
        reasonTextArea.setWrapStyleWord(true);
        reasonTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reasonTextArea.setEditable(false);
        reasonTextArea.setText(reason);

        // Allows scrolling for reason TextArea
        JScrollPane reasonScrollPane = new JScrollPane(reasonTextArea);
        reasonScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        reasonScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        reasonScrollPane.setBounds(10, 190, 260, 100);
        applicantCardPanel.add(reasonScrollPane);

        // View resume button
        JButton viewResumeButton = new JButton("View Resume");
        viewResumeButton.setBackground(new Color(75, 0, 130));
        viewResumeButton.setForeground(Color.WHITE);
        viewResumeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        viewResumeButton.setFocusPainted(false);
        viewResumeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewResumeButton.setBounds(10, 300, 120, 30);
        applicantCardPanel.add(viewResumeButton);

        viewResumeButton.addActionListener(x -> {
            String appliedJobsFileSql = "SELECT ResumePDF, FileName FROM appliedjobs WHERE JobID = ? AND UserEmail = ?";
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                try (PreparedStatement pst = con.prepareStatement(appliedJobsFileSql)) {
                    pst.setInt(1, jobId);
                    pst.setString(2, email);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        byte[] pdfData = rs.getBytes("ResumePDF");
                        String fileName = rs.getString("FileName");
                        if (pdfData != null) {
                            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
                            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                fos.write(pdfData);
                            }
                            Desktop.getDesktop().open(tempFile);
                        } else {
                            JOptionPane.showMessageDialog(null, "No resume found.", "Not Found", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // View profile button
        JButton viewProfileButton = new JButton("View Profile");
        viewProfileButton.setBackground(new Color(0, 0, 139));
        viewProfileButton.setForeground(Color.WHITE);
        viewProfileButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewProfileButton.setBounds(148, 300, 120, 30);
        applicantCardPanel.add(viewProfileButton);

        viewProfileButton.addActionListener(i -> new JSProfile( email,"employer", "",""));

        // Shortlist button
        JButton shortlistButton = new JButton("✔");
        shortlistButton.setBackground(new Color(40, 167, 69));
        shortlistButton.setForeground(Color.WHITE);
        shortlistButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        shortlistButton.setFocusPainted(false);
        shortlistButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        shortlistButton.setBounds(10, 340, 60, 30);
        applicantCardPanel.add(shortlistButton);

        shortlistButton.addActionListener(p -> {
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String shortlistedSql = "INSERT INTO shortlistedJobSeekers (role, position, companyName, jobSeekerEmail, name) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(shortlistedSql)) {
                    String position = getSuggestedPosition(skills);
                    pst.setString(1, jobRole);
                    pst.setString(2, position);
                    pst.setString(3, companyName);
                    pst.setString(4, email);
                    pst.setString(5, name);
                    pst.executeUpdate();
                }

                String updateStatusSql = "UPDATE appliedjobs SET statusUpdate='Shortlisted' WHERE JobID=? AND UserEmail=?";
                try (PreparedStatement pst2 = con.prepareStatement(updateStatusSql)) {
                    pst2.setInt(1, jobId);
                    pst2.setString(2, email);
                    pst2.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Candidate " + name + " has been shortlisted successfully!", "Shortlisted", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error while shortlisting candidate: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            // Remove card after shortlisting
            Container parent = applicantCardPanel.getParent();
            if (parent != null) {
                parent.remove(applicantCardPanel);
                parent.revalidate();
                parent.repaint();
            }
        });


        // Reject button
        JButton rejectButton = new JButton("✖");
        rejectButton.setBackground(new Color(220, 53, 69));
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        rejectButton.setFocusPainted(false);
        rejectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rejectButton.setBounds(76, 340, 60, 30);
        applicantCardPanel.add(rejectButton);

        rejectButton.addActionListener(l -> {
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String rejectedSql = "INSERT INTO rejectedJobSeekers (role, position, companyName, jobSeekerEmail, name) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(rejectedSql)) {
                    String position = getSuggestedPosition(skills);
                    pst.setString(1, jobRole);
                    pst.setString(2, position);
                    pst.setString(3, companyName);
                    pst.setString(4, email);
                    pst.setString(5, name);
                    pst.executeUpdate();
                }

                String updateStatusSql = "UPDATE appliedjobs SET statusUpdate='Rejected' WHERE JobID=? AND UserEmail=?";
                try (PreparedStatement pst2 = con.prepareStatement(updateStatusSql)) {
                    pst2.setInt(1, jobId);
                    pst2.setString(2, email);
                    pst2.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Candidate " + name + " has been rejected.", "Rejected", JOptionPane.WARNING_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while rejecting candidate: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Remove card after rejection
            Container parent = applicantCardPanel.getParent();
            if (parent != null) {
                parent.remove(applicantCardPanel);
                parent.revalidate();
                parent.repaint();
            }
        });

        // Chat button
        JButton chatButton = new JButton("💬");
        chatButton.setBackground(new Color(70, 130, 180));
        chatButton.setForeground(Color.WHITE);
        chatButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        chatButton.setFocusPainted(false);
        chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chatButton.setBounds(142, 340, 60, 30);
        applicantCardPanel.add(chatButton);

        chatButton.addActionListener(g -> new JSChatSys(empEmail, email));

        // Report button
        JButton reportButton = new JButton("🚩");
        reportButton.setBackground(new Color(255, 140, 0));
        reportButton.setForeground(Color.WHITE);
        reportButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        reportButton.setFocusPainted(false);
        reportButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportButton.setBounds(208, 340, 60, 30);
        applicantCardPanel.add(reportButton);

        reportButton.addActionListener(h -> new ReportPanel(jobId, empEmail, email, "jobseeker", "Employer"));

        return applicantCardPanel;
    }

    // Triggers/Handles when star is clicked in ratings and feedback section
    private void updateStars(JLabel[] stars, int rating) {
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setText("★");
                stars[i].setForeground(new Color(255, 193, 7));
            } else {
                stars[i].setText("☆");
                stars[i].setForeground(new Color(200, 200, 200));
            }
        }
    }

    // Constructor
    EmployerDashboard(String empName,String companyName, String empEmail,String companyEmail) {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        // Fonts
        Font headingFont = new Font("Futura", Font.BOLD, 30);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 18);
        Font companyFont = new Font("Arial", Font.BOLD, 16);
        Font positionFont = new Font("Arial", Font.BOLD, 14);
        Font detailFont = new Font("Arial", Font.PLAIN, 12);
        Font enhancedLabelFont = new Font("Segoe UI", Font.BOLD, 12);

        // Employer card layout
        CardLayout employerCardLayout = new CardLayout();

        // Employer container
        Container employerContainer = getContentPane();
        employerContainer.setLayout(null);
        employerContainer.setBackground(new Color(230, 230, 230));

        // Employer panel
        JPanel employerPanel = new JPanel(employerCardLayout);
        employerPanel.setBackground(Color.WHITE);
        employerPanel.setBounds(190, 90, 591, 468);
        employerContainer.add(employerPanel);

        // JobVerse logo
        ImageIcon originalIcon = new ImageIcon("images/company_logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        // JobVerse logo label
        JLabel logoLabel = createLabel("", null, null, SwingConstants.LEFT, 3, 3, 84, 84, null, false, null, employerContainer);
        logoLabel.setIcon(new ImageIcon(scaledImage));

        // Edit profile button
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setContentAreaFilled(false);
        editProfileButton.setOpaque(true);
        editProfileButton.setFont(new Font("Calibri", Font.BOLD, 12));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBackground(Color.BLACK);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        editProfileButton.setHorizontalAlignment(SwingConstants.CENTER);
        editProfileButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfileButton.setPreferredSize(new Dimension(150, 40));
        editProfileButton.setBounds(94,50,100,30);
        employerContainer.add(editProfileButton);

        editProfileButton.addActionListener(k->new CompanyProfile(companyEmail,true,"",false));

        // Messages button
        JButton messagesButton = new JButton("💬");
        messagesButton.setBounds(602, 50, 55, 30);
        messagesButton.setBackground(new Color(255, 165, 0) );
        messagesButton.setForeground(Color.WHITE);
        messagesButton.setFocusPainted(false);
        messagesButton.setBorder(BorderFactory.createLineBorder(new Color(25, 100, 25), 1, true));
        messagesButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        messagesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        messagesButton.setToolTipText("Messages from Job Seekers & Employers");
        employerContainer.add(messagesButton);

        messagesButton.addActionListener(r -> new EChatSys(empEmail));

        String placeholder = "Search candidates, applications...";

        // Search TextField
        JTextField searchTextfield = createTextField(placeholder, new Font("Arial", Font.PLAIN, 14), new Color(150, 150, 150),
                null, 200, 50, 300, 30, true, BorderFactory.createLineBorder(new Color(200, 200, 200),
                        1, true), employerContainer);

        searchTextfield.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchTextfield.getText().equals(placeholder)) {
                    searchTextfield.setText("");
                    searchTextfield.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchTextfield.getText().trim().isEmpty()) {
                    searchTextfield.setText(placeholder);
                    searchTextfield.setForeground(new Color(150, 150, 150));
                }
            }
        });

        // Search Button
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(510, 50, 80, 30);
        searchButton.setFont(new Font("Arial", Font.BOLD, 12));
        searchButton.setBackground(new Color(0, 123, 255));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employerContainer.add(searchButton);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        // Search results label
        JLabel searchResultsLabel = createLabel("SEARCH JOBSEEKERS", new Font("Segoe UI", Font.BOLD, 18), new Color(50, 50, 50), SwingConstants.CENTER, 0, 0, 0, 0,
                BorderFactory.createEmptyBorder(20, 0, 20, 0), false, null, null);
        searchPanel.add(searchResultsLabel, BorderLayout.NORTH);

        // Search Card container
        JPanel searchCardContainer = new JPanel(new GridLayout(0, 2, 15, 15));
        searchCardContainer.setBackground(Color.WHITE);

        // Allows scrolling for searchCardContainer
        JScrollPane searchScrollPane = new JScrollPane(searchCardContainer);
        searchScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        searchScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        searchScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        searchScrollPane.setBorder(null);
        searchPanel.add(searchScrollPane, BorderLayout.CENTER);

        employerPanel.add(searchPanel, "Search Candidates");

        searchButton.addActionListener(a -> {
            searchCardContainer.removeAll();
            String toSearch = searchTextfield.getText().trim();

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                // Log activity
                String activitiesSql = "INSERT INTO activities (empEmail, activity, user) VALUES (?,?,?)";
                try (PreparedStatement pst6 = con.prepareStatement(activitiesSql)) {
                    pst6.setString(1, empEmail);
                    pst6.setString(2, companyName + " has searched for " + toSearch);
                    pst6.setString(3, "Employer");
                    pst6.executeUpdate();
                }

                // Fetch candidates + applied jobs
                String query = "SELECT up.*, aj.JobType, aj.JobId, aj.SkillsRequired, aj.reason FROM userprofile up " +
                        "INNER JOIN appliedjobs aj ON up.JSEmail = aj.UserEmail " +
                        "WHERE aj.CompanyName = ?";

                if (!toSearch.isEmpty()) {
                    query += " AND up.JSFullName LIKE ?";
                }

                try (PreparedStatement pst = con.prepareStatement(query)) {
                    pst.setString(1, companyName);
                    if (!toSearch.isEmpty()) {
                        pst.setString(2, "%" + toSearch + "%");
                    }

                    ResultSet rs = pst.executeQuery();
                    boolean hasResults = false;

                    while (rs.next()) {
                        hasResults = true;

                        String name = rs.getString("JSFullName");
                        String email = rs.getString("JSEmail");
                        String phone = rs.getString("JSPhone");
                        String location = rs.getString("JSLocation");
                        String jobRole = rs.getString("JobType");
                        int jobId = rs.getInt("JobId");
                        String degree = rs.getString("JSDegree");
                        String institution = rs.getString("JSInstitution");
                        String year = rs.getString("JSYear");
                        String headline = rs.getString("JSHeadline");
                        String summary = rs.getString("JSSummary");
                        String skillsRoles = rs.getString("SkillsRequired");
                        String reason = rs.getString("reason");
                        String[] skills = skillsRoles != null ? new String[]{skillsRoles} : new String[]{""};

                        // Profile image
                        ImageIcon profileImageIcon = null;
                        Blob imageBlob = rs.getBlob("JSProfileImage");
                        if (imageBlob != null) {
                            try (InputStream is = imageBlob.getBinaryStream()) {
                                BufferedImage img = ImageIO.read(is);
                                if (img != null) {
                                    Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                    profileImageIcon = new ImageIcon(scaledImg);
                                }
                            } catch (IOException ex) {
                                profileImageIcon = new ImageIcon(
                                        new ImageIcon(getClass().getResource("/images/DefaultProfile.png")).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                            }
                        }
                        if (profileImageIcon == null) {
                            ImageIcon defaultIcon = new ImageIcon("images/DefaultProfile.png");
                            Image scaledImg = defaultIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                            profileImageIcon = new ImageIcon(scaledImg);
                        }

                        // Applicant card panel
                        JPanel applicantCard = createApplicantCard(name, email, phone, location, jobRole, degree, institution, year, headline, summary, jobId,
                                profileImageIcon, skills, companyName, empEmail, skillsRoles, reason, buttonFont
                        );
                        searchCardContainer.add(applicantCard);
                    }

                    // If no results
                    if (!hasResults) {
                        JPanel noResultsPanel = new JPanel();
                        noResultsPanel.setBackground(Color.WHITE);
                        createLabel("No candidates found.", new Font("Segoe UI", Font.PLAIN, 16),
                                new Color(100, 100, 100), SwingConstants.LEFT, 0, 0, 0, 0, null, false, null, noResultsPanel);
                        searchCardContainer.add(noResultsPanel);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                JPanel errorPanel = new JPanel();
                errorPanel.setBackground(Color.WHITE);
                JLabel errorLabel = new JLabel("Error occurred while searching candidates.");
                errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                errorLabel.setForeground(Color.RED);
                errorPanel.add(errorLabel);
                searchCardContainer.add(errorPanel);
            }
            searchCardContainer.revalidate();
            searchCardContainer.repaint();
            employerCardLayout.show(employerPanel, "Search Candidates");
        });

        // Employer card container
        JPanel employerCardContainer1 = new JPanel();
        employerCardContainer1.setLayout(new GridLayout(0, 2, 15, 15));

        // Notifications button
        JButton notificationButton = new JButton("🔔");
        notificationButton.setBounds(720, 15, 70, 30);
        notificationButton.setOpaque(false);
        notificationButton.setContentAreaFilled(false);
        notificationButton.setBorderPainted(false);
        notificationButton.setForeground(new Color(255, 165, 0));
        notificationButton.setFocusPainted(false);
        notificationButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        notificationButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        notificationButton.setToolTipText("Job Notifications & Updates");

        int jobAlertCount = 0;
        int annCount = 0;

        // Job Alerts Count
        String jobsCountSql = "SELECT COUNT(*) FROM jobs j " +
                "WHERE j.companyName = ? " +
                "AND DATEDIFF(CURDATE(), j.postedAt) >= 3 " +
                "AND NOT EXISTS (SELECT 1 FROM appliedjobs a WHERE a.JobID = j.id)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(jobsCountSql)) {
            ps.setString(1, companyName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    jobAlertCount = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Announcements Count
        String annSql = "SELECT COUNT(*) FROM announcements WHERE audience = 'Employers' OR audience = 'All'";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(annSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                annCount = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Final total
        int totalCount = jobAlertCount + annCount;

        // Badge label
        JLabel badgeLabel = new JLabel(String.valueOf(totalCount), SwingConstants.CENTER);
        badgeLabel.setFont(new Font("Arial", Font.BOLD, 11));
        badgeLabel.setForeground(Color.BLACK);
        badgeLabel.setOpaque(true);
        badgeLabel.setBackground(new Color(255, 215, 0));
        badgeLabel.setBounds(45, 0, 18, 18);
        badgeLabel.setVisible(totalCount > 0);
        badgeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // Notification LayeredPane
        JLayeredPane notificationPane = new JLayeredPane();
        notificationPane.setBounds(720, 4, 70, 50);
        notificationButton.setBounds(0, 10, 70, 30);
        notificationPane.add(notificationButton, Integer.valueOf(0));
        notificationPane.add(badgeLabel, Integer.valueOf(1));

        employerContainer.add(notificationPane);

        notificationButton.addActionListener(a -> {

            // Notifications panel
            JPanel notificationsPanel = new JPanel();
            notificationsPanel.setLayout(new BorderLayout());
            notificationsPanel.setBackground(Color.WHITE);

            // Notification label
            JLabel notificationsLabel = createLabel("NOTIFICATIONS", new Font("Arial", Font.BOLD, 20), null,
                    SwingConstants.CENTER, 0, 0, 0, 0,
                    BorderFactory.createEmptyBorder(10, 0, 10, 0), false, null, null);
            notificationsPanel.add(notificationsLabel, BorderLayout.NORTH);

            // Container for cards
            JPanel notificationCardsContainer = new JPanel();
            notificationCardsContainer.setLayout(new BoxLayout(notificationCardsContainer, BoxLayout.Y_AXIS));
            notificationCardsContainer.setBackground(Color.WHITE);

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                String jobsSql = "SELECT id, position, postedAt, DATEDIFF(CURDATE(), j.postedAt) AS daysSincePosted FROM jobs j WHERE j.companyName = ? " +
                        "AND DATEDIFF(CURDATE(), j.postedAt) >= 3 AND NOT EXISTS (SELECT 1 FROM appliedjobs a WHERE a.JobID = j.id)";

                try (PreparedStatement ps = conn.prepareStatement(jobsSql)) {
                    ps.setString(1, companyName);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        int jobId = rs.getInt("id");
                        String jobTitle = rs.getString("position");
                        int days = rs.getInt("daysSincePosted");

                        // Card panel
                        JPanel cardPanel = new JPanel(new BorderLayout());
                        cardPanel.setBackground(new Color(255, 250, 250));
                        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
                        cardPanel.setPreferredSize(new Dimension(550, 200));
                        cardPanel.setMaximumSize(new Dimension(550, 200));

                        String message = "<html><body style='width: 500px;'>"
                                + "<b>⚠️ No Applications Received Yet</b><br>"
                                + "Your job post for <b>“" + jobTitle + "”</b> has been live for <b>" + days + " days</b>, but no candidates have applied.<br><br>"
                                + "You may want to:<ul>"
                                + "<li>Check if the salary or requirements are competitive</li>"
                                + "<li>Add more detail to make it stand out</li>"
                                + "<li>Repost or promote to reach more candidates</li>"
                                + "</ul></body></html>";

                        // Notification card panel
                        JLabel notificationCardLabel = createLabel(message, new Font("Arial", Font.PLAIN, 14), null,
                                SwingConstants.LEFT, 0, 0, 0, 0,
                                BorderFactory.createEmptyBorder(10, 10, 10, 10), false, null, null);
                        cardPanel.add(notificationCardLabel, BorderLayout.CENTER);

                        notificationCardsContainer.add(cardPanel);
                        notificationCardsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }

                String announcementsSql = "SELECT id, title, message, postedDate FROM announcements WHERE audience = 'Employers' OR audience = 'All' ORDER BY postedDate DESC";

                try (PreparedStatement pst = conn.prepareStatement(announcementsSql)) {
                    ResultSet rs2 = pst.executeQuery();

                    while (rs2.next()) {
                        int annId = rs2.getInt("id");
                        String title = rs2.getString("title");
                        String message = rs2.getString("message");
                        String date = rs2.getString("postedDate");

                        // Announcement card
                        JPanel announcementCard = new JPanel(new BorderLayout());
                        announcementCard.setBackground(new Color(255, 255, 240));
                        announcementCard.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 150)));
                        announcementCard.setPreferredSize(new Dimension(550, 150));
                        announcementCard.setMaximumSize(new Dimension(550, 150));

                        String annMessage = "<html><body style='width: 500px;'>"
                                + "<b>" + title + "</b><br>" + message + "<br><br>"
                                + "<i>" + date + "</i></body></html>";

                        // Announcement label
                        JLabel announcementLabel = createLabel(annMessage, new Font("Arial", Font.PLAIN, 14), null,
                                SwingConstants.LEFT, 0, 0, 0, 0,
                                BorderFactory.createEmptyBorder(10, 10, 10, 10), false, null, null);
                        announcementCard.add(announcementLabel, BorderLayout.CENTER);

                        notificationCardsContainer.add(announcementCard);
                        notificationCardsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JLabel errorLabel = new JLabel("⚠️ Error fetching notifications.", JLabel.CENTER);
                errorLabel.setForeground(Color.RED);
                notificationCardsContainer.add(errorLabel);
            }

            // Allows scrolling for notification cards
            JScrollPane notificationsScrollPane = new JScrollPane(notificationCardsContainer);
            notificationsScrollPane.setBorder(null);
            notificationsScrollPane.setPreferredSize(new Dimension(570, 600));
            notificationsScrollPane.getVerticalScrollBar().setUnitIncrement(20);

            notificationsPanel.add(notificationsScrollPane, BorderLayout.CENTER);

            employerPanel.add(notificationsPanel, "⩍");
            employerCardLayout.show(employerPanel, "⩍");
        });

        // Request button
        JButton requestButton = new JButton("Requests");
        requestButton.setBackground(new Color(108, 117, 125));
        requestButton.setForeground(Color.WHITE);
        requestButton.setFocusPainted(false);
        requestButton.setBorder(BorderFactory.createLineBorder(new Color(50, 0, 100), 1, true));
        requestButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        requestButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        requestButton.setToolTipText("Job Requests & Applications");
        requestButton.setFont(buttonFont);
        requestButton.setBounds(670, 50, 100, 30);
        employerContainer.add(requestButton);

        // Request panel
        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BorderLayout());
        requestPanel.setBackground(Color.WHITE);

        int applyCount = 0;

        // Red dot panel
        JLabel redDotLabel = new JLabel(String.valueOf(applyCount));
        redDotLabel.setOpaque(true);
        redDotLabel.setBackground(Color.RED);
        redDotLabel.setForeground(Color.WHITE);
        redDotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        redDotLabel.setVerticalAlignment(SwingConstants.CENTER);
        redDotLabel.setFont(new Font("Arial", Font.BOLD, 12));
        redDotLabel.setBounds(760, 40, 20, 20);
        redDotLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        employerContainer.add(redDotLabel);

        String appliedCount = "SELECT COUNT(*) FROM appliedJobs WHERE companyName = ? AND DATE(currentDate) = CURDATE() AND statusUpdate='Pending'";

        try (Connection con1 = DriverManager.getConnection(url, user, password)) {
            try (PreparedStatement pst1 = con1.prepareStatement(appliedCount)) {
                pst1.setString(1, companyName);
                ResultSet rs1 = pst1.executeQuery();
                if (rs1.next()) {
                    applyCount = rs1.getInt(1);
                }
                redDotLabel.setText(String.valueOf(applyCount));
                redDotLabel.setVisible(applyCount > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestButton.addActionListener(z->employerCardLayout.show(employerPanel, "Requests"));

        try (Connection con3 = DriverManager.getConnection(url, user, password)) {
            String applicantsInfo = "SELECT * FROM userprofile";
            String appliedApplicants = "SELECT * FROM appliedjobs WHERE CompanyName=? AND DATE(currentDate) = CURDATE() AND statusUpdate='Pending'";

            try (PreparedStatement pst0 = con3.prepareStatement(appliedApplicants)) {
                pst0.setString(1, companyName);
                ResultSet rs0 = pst0.executeQuery();

                // Allows employerCardContainer1 in request panel to scroll
                JScrollPane requestScrollPanel = new JScrollPane(employerCardContainer1);
                requestScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                requestScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                requestScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
                requestScrollPanel.setBorder(null);
                requestPanel.setLayout(new BorderLayout());
                requestPanel.add(requestScrollPanel, BorderLayout.CENTER);

                // Header label
                JLabel headerLabel = createLabel("APPLICANTS (Today)", new Font("Segoe UI", Font.BOLD, 18), new Color(50, 50, 50), SwingConstants.CENTER, 0, 0, 0, 0,
                        BorderFactory.createEmptyBorder(20, 0, 20, 0), false, null, null);
                requestPanel.add(headerLabel, BorderLayout.NORTH);

                while (rs0.next()) {
                    int jobId = rs0.getInt("JobId");
                    String jobRole = rs0.getString("JobType");
                    String userEmail = rs0.getString("UserEmail");
                    String skillsRole = rs0.getString("skillsRequired");
                    String reason = rs0.getString("reason");
                    try (PreparedStatement pst9 = con3.prepareStatement(applicantsInfo)) {
                        ResultSet rs9 = pst9.executeQuery();
                        while (rs9.next()) {
                            String jsEmail = rs9.getString("JSEmail");
                            if (userEmail.equals(jsEmail)) {
                                String name = rs9.getString("JSFullName");
                                String email = rs9.getString("JSEmail");
                                String phone = rs9.getString("JSPhone");
                                String location = rs9.getString("JSLocation");
                                String[] skills = { rs9.getString("JSSkills") };
                                String degree = rs9.getString("JSDegree");
                                String institution = rs9.getString("JSInstitution");
                                String year = rs9.getString("JSYear");
                                String headline = rs9.getString("JSHeadline");
                                String summary = rs9.getString("JSSummary");

                                // Create profile image icon
                                ImageIcon profileImageIcon = null;
                                Blob imageBlob = rs9.getBlob("JSProfileImage");
                                if (imageBlob != null) {
                                    try (InputStream is = imageBlob.getBinaryStream()) {
                                        BufferedImage img = ImageIO.read(is);
                                        if (img != null) {
                                            Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                            profileImageIcon = new ImageIcon(scaledImg);
                                        }
                                    } catch (IOException ex) {
                                        profileImageIcon = null;
                                    }
                                }

                                JPanel applicantCard = createApplicantCard(name, email, phone, location, jobRole,degree, institution, year, headline, summary,jobId,
                                        profileImageIcon, skills, companyName, empEmail, skillsRole, reason, buttonFont
                                );
                                employerCardContainer1.add(applicantCard);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        employerPanel.add(requestPanel, "Requests");

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
        homeButton.setFont(buttonFont);
        homeButton.setBounds(0, 90, 190, 30);
        employerContainer.add(homeButton);

        // Create home panel
        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(new Color(230, 230, 230));

        // Allows scrolling for home panel
        JScrollPane homeScrollPane = new JScrollPane(homePanel);
        homeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        homeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        homeScrollPane.getVerticalScrollBar().setUnitIncrement(20);

        employerPanel.add(homeScrollPane, "Home");

        homeButton.addActionListener(a -> {
            homePanel.removeAll();

            // Title panel
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            titlePanel.setBackground(new Color(230, 230, 230));

            // Home label
            JLabel homeLabel = createLabel("APPLICANTS", new Font("Segoe UI", Font.BOLD, 24), new Color(50, 50, 50),
                    SwingConstants.LEFT, 0, 0, 0, 0, null, false, null, titlePanel);
            homePanel.add(titlePanel);

            // Card Container
            JPanel cardContainer = new JPanel(new GridLayout(0, 2, 15, 15));
            cardContainer.setBackground(new Color(230, 230, 230));
            cardContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

            String status = "Pending";
            try (Connection con1 = DriverManager.getConnection(url, user, password)) {
                // Modified query to exclude shortlisted candidates
                String appliedJobsSql = "SELECT * FROM appliedjobs WHERE CompanyName = ? AND statusUpdate =?";
                String userProfileSql = "SELECT * FROM userprofile WHERE JSEmail = ?";

                try (PreparedStatement pst1 = con1.prepareStatement(appliedJobsSql)) {
                    pst1.setString(1, companyName);
                    pst1.setString(2,status);
                    ResultSet rs1 = pst1.executeQuery();

                    while (rs1.next()) {
                        String jobRole = rs1.getString("JobType");
                        String userEmail = rs1.getString("UserEmail");
                        String skillsRoles = rs1.getString("SkillsRequired");
                        String reason = rs1.getString("Reason");
                        int jobId = rs1.getInt("JobID");
                        String statusUpdate = rs1.getString("statusUpdate");

                        // Skip if already shortlisted (additional safety check)
                        if ("Shortlisted".equals(statusUpdate)) {
                            continue;
                        }

                        try (PreparedStatement pst2 = con1.prepareStatement(userProfileSql)) {
                            pst2.setString(1, userEmail);
                            ResultSet rs2 = pst2.executeQuery();

                            if (rs2.next()) {
                                String name = rs2.getString("JSFullName");
                                String email = rs2.getString("JSEmail");
                                String phone = rs2.getString("JSPhone");
                                String location = rs2.getString("JSLocation");
                                String[] skills = {rs2.getString("JSSkills")};
                                String degree = rs2.getString("JSDegree");
                                String institution = rs2.getString("JSInstitution");
                                String year = rs2.getString("JSYear");
                                String headline = rs2.getString("JSHeadline");
                                String summary = rs2.getString("JSSummary");

                                // Profile image
                                ImageIcon profileImageIcon = null;
                                Blob imageBlob = rs2.getBlob("JSProfileImage");
                                if (imageBlob != null) {
                                    try (InputStream is = imageBlob.getBinaryStream()) {
                                        BufferedImage img = ImageIO.read(is);
                                        if (img != null) {
                                            Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                            profileImageIcon = new ImageIcon(scaledImg);
                                        }
                                    } catch (IOException ex) {
                                        profileImageIcon = new ImageIcon(
                                                new ImageIcon("images/DefaultProfile.png")
                                                        .getImage()
                                                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH)
                                        );
                                    }
                                }
                                if (profileImageIcon == null) {
                                    ImageIcon defaultIcon = new ImageIcon("images/DefaultProfile.png");
                                    Image scaledImg = defaultIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                                    profileImageIcon = new ImageIcon(scaledImg);
                                }

                                // Applicants card
                                JPanel applicantCard = createApplicantCard(name, email, phone, location, jobRole, degree, institution, year, headline, summary, jobId,
                                        profileImageIcon, skills,  companyName, empEmail,skillsRoles,reason,buttonFont);
                                cardContainer.add(applicantCard);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            homePanel.add(cardContainer);
            homePanel.revalidate();
            homePanel.repaint();
            employerCardLayout.show(employerPanel, "Home");
        });

        // Sidebar post jobs button
        JButton postButton = new JButton("Post Jobs");
        postButton.setContentAreaFilled(false);
        postButton.setOpaque(false);
        postButton.setFont(new Font("Calibri", Font.BOLD, 14));
        postButton.setForeground(Color.BLACK);
        postButton.setFocusPainted(false);
        postButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        postButton.setHorizontalAlignment(SwingConstants.CENTER);
        postButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                postButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                postButton.setForeground(Color.BLACK);
            }
        });
        postButton.setFont(buttonFont);
        postButton.setBounds(0, 120, 190, 30);
        employerContainer.add(postButton);

        postButton.addActionListener(a -> employerCardLayout.show(employerPanel, "Post Jobs"));

        // Post job panel
        JPanel postJobsPanel = new JPanel();
        postJobsPanel.setLayout(null);
        postJobsPanel.setBackground(new Color(245, 247, 250));
        add(postJobsPanel);

        // Title label
        JLabel titleLabel = createLabel("Post a New Job", titleFont, new Color(37, 99, 235), SwingConstants.LEFT,
                65, 10, 200, 30, null, false, null, postJobsPanel);

        // Company Name label
        JLabel companyNameLabel = createLabel("Company Name : ", labelFont, new Color(55, 65, 81), SwingConstants.LEFT,
                65, 50, 130, 25, null, false, null, postJobsPanel);

        // Company Name TextField
        JTextField companyTextField = createTextField(companyName, fieldFont, null, new Color(229, 231, 235),
                65, 75, 200, 35, false, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)), postJobsPanel);

        // Location label
        JLabel locationLabel = createLabel("Location : ", labelFont, new Color(55, 65, 81), SwingConstants.LEFT,
                335, 50, 100, 25, null, false, null, postJobsPanel);

        // Location TextField
        JTextField locationTextField = createTextField("", fieldFont, null, Color.WHITE, 335, 75, 200, 35,
                true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)), postJobsPanel);

        // Job Type label
        JLabel jobTypeLabel = createLabel("Job Type : ", labelFont, new Color(55, 65, 81), SwingConstants.LEFT,
                65, 140, 100, 25, null, false, null, postJobsPanel);

        // Job type TextField
        JTextField jobTypeTextField = createTextField("", fieldFont, null, Color.WHITE, 65, 165, 200, 35,
                true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)), postJobsPanel);
        // Salary label
        JLabel salaryLabel = createLabel("Salary : ", labelFont, new Color(55, 65, 81), SwingConstants.LEFT,
                335, 140, 100, 25, null, false, null, postJobsPanel);

        // Salary TextField
        JTextField salaryTextField = createTextField("", fieldFont, null, Color.WHITE, 335, 165, 200, 35,
                true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)), postJobsPanel);

        // Requirements label
        JLabel requirementsLabel = createLabel("Requirements :", labelFont, new Color(55, 65, 81), JLabel.LEFT,
                65, 230, 200, 25, null, false, null, postJobsPanel
        );

        // Requirement textarea
        JTextArea requirementsTextArea = new JTextArea();
        requirementsTextArea.setFont(fieldFont);
        requirementsTextArea.setBackground(Color.WHITE);
        requirementsTextArea.setLineWrap(true);
        requirementsTextArea.setWrapStyleWord(true);
        requirementsTextArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        // Allows scrolling for requirements textarea
        JScrollPane requirementsTextAreascrollPane1 = new JScrollPane(requirementsTextArea);
        requirementsTextAreascrollPane1.setBounds(65, 255, 470, 120);
        requirementsTextAreascrollPane1.setBorder(BorderFactory.createLineBorder(new Color(209, 213, 219), 1));
        requirementsTextAreascrollPane1.setBackground(Color.WHITE);
        requirementsTextAreascrollPane1.getViewport().setBackground(Color.WHITE);

        postJobsPanel.add(requirementsTextAreascrollPane1);

        // Post job button
        JButton postJobButton = new JButton("Post Job");
        postJobButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        postJobButton.setBounds(412, 400, 120, 40);
        postJobButton.setBackground(new Color(0, 123, 255));
        postJobButton.setForeground(Color.WHITE);
        postJobButton.setFocusPainted(false);
        postJobButton.setBorder(BorderFactory.createEmptyBorder());
        postJobButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postJobsPanel.add(postJobButton);

        employerPanel.add(postJobsPanel, "Post Jobs");

        postJobButton.addActionListener(a -> {
            String location = locationTextField.getText().trim();
            String jobType = jobTypeTextField.getText().trim();
            String salary = salaryTextField.getText().trim();
            String requirement = requirementsTextArea.getText().trim();
            String[] requirementText = { requirement };

            if (location.isEmpty() || jobType.isEmpty() || salary.isEmpty() || requirement.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields.");
                return;
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                InputStream companyLogoStream = null;
                String logoFileName = null;
                String PCompanyName = null;
                String latestEmpEmail = null;
                String companyCheckSql = "SELECT * FROM companies WHERE companyName = ? AND employersEmail = ?";
                try (PreparedStatement psCompanyCheck = con.prepareStatement(companyCheckSql)) {
                    psCompanyCheck.setString(1, companyName);
                    psCompanyCheck.setString(2, empEmail);

                    try (ResultSet rsCompanyCheck = psCompanyCheck.executeQuery()) {
                        if (rsCompanyCheck.next()) {
                            String companyProfileSql = "SELECT companyName, logo, logoFileName, companyEmail " +
                                    "FROM companyprofile WHERE companyEmail = ? OR companyName = ?";
                            try (PreparedStatement psCompany = con.prepareStatement(companyProfileSql)) {
                                psCompany.setString(1, companyEmail);
                                psCompany.setString(2, companyName);
                                try (ResultSet rsCompany = psCompany.executeQuery()) {
                                    if (rsCompany.next()) {
                                        PCompanyName = rsCompany.getString("companyName");
                                        Blob logoBlob = rsCompany.getBlob("logo");
                                        if (logoBlob != null) {
                                            companyLogoStream = logoBlob.getBinaryStream();
                                        }
                                        logoFileName = rsCompany.getString("logoFileName");
                                        latestEmpEmail = rsCompany.getString("companyEmail");
                                    }
                                }
                            }
                        }
                    }
                }


                if (PCompanyName == null || latestEmpEmail == null) {
                    JOptionPane.showMessageDialog(null, "No company profile found for this employer.");
                    return;
                }

                String jobsSql = "INSERT INTO jobs(companyName,companyEmail companyLogo, logoFileName, location, jobType, salary, requirement, position, employerEmail) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(jobsSql)) {
                    String position = getSuggestedPosition(requirementText);
                    pst.setString(1, PCompanyName);
                    pst.setString(2,companyEmail);
                    if (companyLogoStream != null) {
                        pst.setBlob(3, companyLogoStream);
                    } else {
                        pst.setNull(3, java.sql.Types.BLOB);
                    }
                    pst.setString(4, logoFileName);
                    pst.setString(5, location);
                    pst.setString(6, jobType);
                    pst.setString(7, salary);
                    pst.setString(8, requirement);
                    pst.setString(9, position);
                    pst.setString(10, empEmail);
                    pst.executeUpdate();

                    String employerCheck = "SELECT * FROM companies WHERE employersEmail = ?";
                    String activitiesUrl = "INSERT INTO activities (empEmail, activity,user) VALUES (?,?,?)";
                    String message =companyName+ "has posted a new job opening for the position of " + position + " as " + jobType + "!";
                    try (PreparedStatement pstt = con.prepareStatement(employerCheck)) {
                        pstt.setString(1, empEmail);
                        ResultSet rs7 = pstt.executeQuery();
                        if (rs7.next()) {
                            try (PreparedStatement psttt = con.prepareStatement(activitiesUrl)) {
                                psttt.setString(1, empEmail);
                                psttt.setString(2, message);
                                psttt.setString(3,"Employer");
                                psttt.executeUpdate();
                            }
                        }
                    }
                    String hirings = "hirings";
                    String jsnotificationsSql = "INSERT INTO jsnotifications (notifications, emailJS) VALUES (?, ?)";
                    String notification;
                    String emailjs = "all";
                    if (jobType.equalsIgnoreCase("Full Time")) {
                        notification = PCompanyName + " has just posted a new Full Time job opening for the position of " + position + ". Apply now!";
                    } else if (jobType.equalsIgnoreCase("Part Time")) {
                        notification = PCompanyName + " has just posted a new Part Time job opening for the position of " + position + ". Apply now!";
                    } else {
                        notification = PCompanyName + " has just posted a new Internship offer for the position of " + position + ". Apply now!";
                    }
                    try (PreparedStatement pst00 = con.prepareStatement(jsnotificationsSql)) {
                        pst00.setString(1, notification);
                        pst00.setString(2, emailjs);
                        pst00.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(null, "Job posted successfully!");
                locationTextField.setText("");
                jobTypeTextField.setText("");
                salaryTextField.setText("");
                requirementsTextArea.setText("");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });

        // Sidebar view applications button
        JButton viewApplicationsButton = new JButton("View Applications");
        viewApplicationsButton.setContentAreaFilled(false);
        viewApplicationsButton.setOpaque(false);
        viewApplicationsButton.setFont(new Font("Calibri", Font.BOLD, 14));
        viewApplicationsButton.setForeground(Color.BLACK);
        viewApplicationsButton.setFocusPainted(false);
        viewApplicationsButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        viewApplicationsButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewApplicationsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewApplicationsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewApplicationsButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewApplicationsButton.setForeground(Color.BLACK);
            }
        });
        viewApplicationsButton.setFont(buttonFont);
        viewApplicationsButton.setBounds(0, 150, 190, 30);
        employerContainer.add(viewApplicationsButton);

        viewApplicationsButton.addActionListener(a -> {
            // Remove existing view applications panel before creating new one
            for (Component comp : employerPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    Container parent = comp.getParent();
                    if (parent instanceof JPanel) {
                        JPanel parentPanel = (JPanel) parent;
                        if (parentPanel.getLayout() instanceof CardLayout) {
                            // Remove the existing "View Applications" panel
                            employerPanel.remove(comp);
                            break;
                        }
                    }
                }
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                List<Object[]> allApplications = new ArrayList<>();

                // Updated query to get shortlisted candidates from appliedjobs table
                String shortlistedJobSeekersSql = "SELECT aj.UserEmail, aj.JobType, aj.currentDate " +
                        "FROM appliedjobs aj " +
                        "JOIN userprofile up ON aj.UserEmail = up.JSEmail " +
                        "WHERE aj.CompanyName = ? AND aj.statusUpdate = 'Shortlisted'";
                try (PreparedStatement pst = con.prepareStatement(shortlistedJobSeekersSql)) {
                    pst.setString(1, companyName);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        // Get user name from userprofile
                        String userEmail = rs.getString("UserEmail");
                        String getUserNameSql = "SELECT JSFullName FROM userprofile WHERE JSEmail = ?";
                        try (PreparedStatement pst2 = con.prepareStatement(getUserNameSql);) {
                            pst2.setString(1, userEmail);
                            ResultSet rs2 = pst2.executeQuery();
                            String userName = userEmail; // fallback to email
                            if (rs2.next()) {
                                userName = rs2.getString("JSFullName");
                            }
                            allApplications.add(new Object[]{
                                    userName,
                                    rs.getString("JobType"),
                                    "Shortlisted",
                                    rs.getString("currentDate")
                            });
                        }
                    }
                }

                String rejectedUrl = "SELECT name, position, time FROM rejectedJobSeekers WHERE companyName = ?";
                try (PreparedStatement pst1 = con.prepareStatement(rejectedUrl)) {
                    pst1.setString(1, companyName);
                    ResultSet rs1 = pst1.executeQuery();
                    while (rs1.next()) {
                        allApplications.add(new Object[]{
                                rs1.getString("name"),
                                rs1.getString("position"),
                                "Rejected",
                                rs1.getString("time")
                        });
                    }
                }

                // View applications panel
                JPanel viewApplicationsPanel = new JPanel(new BorderLayout());
                viewApplicationsPanel.setBackground(new Color(245, 248, 252));
                viewApplicationsPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

                // Header panel
                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setBackground(new Color(245, 248, 252));
                headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

                // View label
                JLabel viewLabel = createLabel("JOB APPLICATIONS", new Font("Arial", Font.BOLD, 24), new Color(33, 37, 41),
                        SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, null);

                // totalApplicationCount label
                JLabel totalApplicationCountLabel = createLabel("Total Applications: " + allApplications.size(),
                        new Font("Arial", Font.PLAIN, 14), new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0,
                        null, false, null, null);

                headerPanel.add(viewLabel, BorderLayout.CENTER);
                headerPanel.add(totalApplicationCountLabel, BorderLayout.SOUTH);

                viewApplicationsPanel.add(headerPanel, BorderLayout.NORTH);

                // If not application found
                if (allApplications.isEmpty()) {

                    // Empty view application panel
                    JPanel emptyViewApplicationPanel = new JPanel();
                    emptyViewApplicationPanel.setLayout(new BoxLayout(emptyViewApplicationPanel, BoxLayout.Y_AXIS));
                    emptyViewApplicationPanel.setBackground(new Color(245, 248, 252));
                    emptyViewApplicationPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

                    // Empty viewApplication message label
                    JLabel emptyViewApplicationMessageLabel = createLabel("No applications found", new Font("Arial", Font.BOLD, 18),
                            new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, emptyViewApplicationPanel);
                    emptyViewApplicationMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    // Empty viewApplication submessage label
                    JLabel emptyViewApplicationSubMessageLabel = createLabel("Applications will appear here once candidates apply",
                            new Font("Arial", Font.PLAIN, 14), new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0,
                            null, false, null, emptyViewApplicationPanel);
                    emptyViewApplicationSubMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    emptyViewApplicationPanel.add(Box.createVerticalGlue());
                    emptyViewApplicationPanel.add(emptyViewApplicationMessageLabel);
                    emptyViewApplicationPanel.add(Box.createVerticalStrut(8));
                    emptyViewApplicationPanel.add(emptyViewApplicationSubMessageLabel);
                    emptyViewApplicationPanel.add(Box.createVerticalGlue());

                    viewApplicationsPanel.add(emptyViewApplicationPanel, BorderLayout.CENTER);
                } else {

                    // Stores column names of table
                    String[] columnNames = {"Applicant", "Job Title", "Status", "Date"};
                    Object[][] data = allApplications.toArray(new Object[0][]);

                    JTable applicationsTable = new JTable(data, columnNames) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };
                    applicationsTable.setFont(new Font("Arial", Font.PLAIN, 13));
                    applicationsTable.setRowHeight(40);
                    applicationsTable.setGridColor(new Color(230, 230, 230));
                    applicationsTable.setSelectionBackground(new Color(0, 123, 255, 100));
                    applicationsTable.setSelectionForeground(new Color(33, 37, 41));
                    applicationsTable.setShowVerticalLines(false);
                    applicationsTable.setIntercellSpacing(new Dimension(10, 5));
                    applicationsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                            // Table label
                            JLabel tableLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                            if (!isSelected) {
                                if (row % 2 == 0) {
                                    tableLabel.setBackground(Color.WHITE);
                                } else {
                                    tableLabel.setBackground(new Color(248, 249, 250));
                                }
                            } else {
                                tableLabel.setBackground(new Color(0, 123, 255, 100));
                            }
                            tableLabel.setForeground(new Color(33, 37, 41));
                            tableLabel.setHorizontalAlignment(JLabel.LEFT);
                            tableLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                            tableLabel.setOpaque(true);

                            if (column == 2) {
                                String status = (String) value;
                                tableLabel.setHorizontalAlignment(JLabel.CENTER);
                                tableLabel.setFont(new Font("Arial", Font.BOLD, 12));
                                if ("Shortlisted".equals(status)) {
                                    if (!isSelected) {
                                        tableLabel.setBackground(new Color(40, 167, 69));
                                        tableLabel.setForeground(Color.WHITE);
                                    }
                                    tableLabel.setText("Shortlisted");
                                } else if ("Rejected".equals(status)) {
                                    if (!isSelected) {
                                        tableLabel.setBackground(new Color(220, 53, 69));
                                        tableLabel.setForeground(Color.WHITE);
                                    }
                                    tableLabel.setText("Rejected");
                                }
                                tableLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                            } else {
                                tableLabel.setFont(new Font("Arial", Font.PLAIN, 13));
                                tableLabel.setText(value != null ? value.toString() : "");
                            }
                            return tableLabel;
                        }
                    });

                    // Table header
                    JTableHeader tableHeader = applicationsTable.getTableHeader();
                    tableHeader.setFont(new Font("Arial", Font.BOLD, 14));
                    tableHeader.setBackground(new Color(33, 37, 41));
                    tableHeader.setForeground(Color.WHITE);
                    tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 45));
                    tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value,
                                                                       boolean isSelected, boolean hasFocus, int row, int column) {
                            JLabel label = new JLabel(value.toString(), JLabel.CENTER);
                            label.setFont(new Font("Arial", Font.BOLD, 14));
                            label.setForeground(Color.WHITE);
                            label.setOpaque(true);
                            label.setBackground(new Color(33, 37, 41));
                            label.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
                            return label;
                        }
                    });

                    // Set column widths
                    applicationsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
                    applicationsTable.getColumnModel().getColumn(1).setPreferredWidth(250);
                    applicationsTable.getColumnModel().getColumn(2).setPreferredWidth(140);
                    applicationsTable.getColumnModel().getColumn(3).setPreferredWidth(150);

                    // Allows scrolling for applications table
                    JScrollPane applicationsTableScrollPane = new JScrollPane(applicationsTable);
                    applicationsTableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    ));
                    applicationsTableScrollPane.getViewport().setBackground(Color.WHITE);
                    applicationsTableScrollPane.setBackground(Color.WHITE);
                    applicationsTableScrollPane.getVerticalScrollBar().setUnitIncrement(16);

                    // Summary panel
                    JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                    summaryPanel.setBackground(new Color(245, 248, 252));
                    summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

                    int shortlistedCount = 0;
                    int rejectedCount = 0;

                    for (Object[] app : allApplications) {
                        if ("Shortlisted".equals(app[2])) {
                            shortlistedCount++;
                        } else if ("Rejected".equals(app[2])) {
                            rejectedCount++;
                        }
                    }

                    // Shortlisted card panel
                    JPanel shortlistedCardPanel = new JPanel();
                    shortlistedCardPanel.setLayout(new BoxLayout(shortlistedCardPanel, BoxLayout.Y_AXIS));
                    shortlistedCardPanel.setBackground(Color.WHITE);
                    shortlistedCardPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                            BorderFactory.createEmptyBorder(15, 20, 15, 20)
                    ));
                    shortlistedCardPanel.setPreferredSize(new Dimension(150, 80));

                    // Shortlisted Title label
                    JLabel shortlistedTitleLabel = createLabel("Shortlisted", new Font("Arial", Font.PLAIN, 12),
                            new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, shortlistedCardPanel);
                    shortlistedTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    // Shortlisted count label
                    JLabel shortlistedCountLabel = createLabel(String.valueOf(shortlistedCount), new Font("Arial", Font.BOLD, 24),
                            new Color(40, 167, 69), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, shortlistedCardPanel);
                    shortlistedCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    // Rejected card panel
                    JPanel rejectedCardPanel = new JPanel();
                    rejectedCardPanel.setLayout(new BoxLayout(rejectedCardPanel, BoxLayout.Y_AXIS));
                    rejectedCardPanel.setBackground(Color.WHITE);
                    rejectedCardPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                            BorderFactory.createEmptyBorder(15, 20, 15, 20)
                    ));
                    rejectedCardPanel.setPreferredSize(new Dimension(150, 80));

                    // Rejected label
                    JLabel rejectedLabel = createLabel("Rejected", new Font("Arial", Font.PLAIN, 12), new Color(108, 117, 125),
                            SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, rejectedCardPanel);
                    rejectedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    // Rejected count label
                    JLabel rejectedCountLabel = createLabel(String.valueOf(rejectedCount), new Font("Arial", Font.BOLD, 24),
                            new Color(220, 53, 69), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, rejectedCardPanel);
                    rejectedCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    rejectedCardPanel.add(Box.createVerticalStrut(5));
                    rejectedCardPanel.add(rejectedCountLabel);
                    summaryPanel.add(rejectedCardPanel);

                    summaryPanel.add(shortlistedCardPanel);

                    viewApplicationsPanel.add(applicationsTableScrollPane, BorderLayout.CENTER);
                    viewApplicationsPanel.add(summaryPanel, BorderLayout.SOUTH);
                }

                // Add the new panel and show it
                employerPanel.add(viewApplicationsPanel, "View Applications");
                employerCardLayout.show(employerPanel, "View Applications");

            } catch (Exception e) {
                e.printStackTrace();

                // Error panel
                JPanel errorPanel = new JPanel();
                errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
                errorPanel.setBackground(new Color(245, 248, 252));
                errorPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

                // Error message label
                JLabel errorMessageLabel = createLabel("Unable to load applications", new Font("Arial", Font.BOLD, 18),
                        new Color(220, 53, 69), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, errorPanel);
                errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Error submessage label
                JLabel errorSubMessageLabel = createLabel("Please check your connection and try again",
                        new Font("Arial", Font.PLAIN, 14), new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0,
                        null, false, null, errorPanel);
                errorSubMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                errorPanel.add(Box.createVerticalGlue());
                errorPanel.add(errorMessageLabel);
                errorPanel.add(Box.createVerticalStrut(8));
                errorPanel.add(errorSubMessageLabel);
                errorPanel.add(Box.createVerticalGlue());

                // Error view panel
                JPanel errorViewPanel = new JPanel(new BorderLayout());
                errorViewPanel.setBackground(new Color(245, 248, 252));
                errorViewPanel.add(errorPanel, BorderLayout.CENTER);

                employerPanel.add(errorViewPanel, "View Applications");
                employerCardLayout.show(employerPanel, "View Applications");
            }
        });

        // Sidebar view button
        JButton viewPostedJobsButton = new JButton("View Posted Jobs");
        viewPostedJobsButton.setContentAreaFilled(false);
        viewPostedJobsButton.setOpaque(false);
        viewPostedJobsButton.setFont(new Font("Calibri", Font.BOLD, 14));
        viewPostedJobsButton.setForeground(Color.BLACK);
        viewPostedJobsButton.setFocusPainted(false);
        viewPostedJobsButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        viewPostedJobsButton.setHorizontalAlignment(SwingConstants.CENTER);
        viewPostedJobsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewPostedJobsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewPostedJobsButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewPostedJobsButton.setForeground(Color.BLACK);
            }
        });
        viewPostedJobsButton.setFont(buttonFont);
        viewPostedJobsButton.setBounds(0,180,190,30);
        employerContainer.add(viewPostedJobsButton);

        // Posted jobs panel
        JPanel postedJobsPanel = new JPanel();
        postedJobsPanel.setLayout(new BoxLayout(postedJobsPanel, BoxLayout.Y_AXIS));
        postedJobsPanel.setBackground(new Color(245, 248, 252));
        postedJobsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Allows scrolling for posted jobs panel
        JScrollPane postedJobsScrollPane = new JScrollPane(postedJobsPanel);
        postedJobsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        postedJobsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        postedJobsScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        postedJobsScrollPane.setBorder(null);
        postedJobsScrollPane.getViewport().setBackground(new Color(245, 248, 252));

        employerPanel.add(postedJobsScrollPane, "View Posted Jobs");

        viewPostedJobsButton.addActionListener(a -> {
            postedJobsPanel.removeAll();
            String jobsSql = "SELECT * FROM jobs WHERE companyName = ?";

            try (Connection conn = DriverManager.getConnection(url, user, password);
                 PreparedStatement pstt = conn.prepareStatement(jobsSql)) {
                pstt.setString(1, companyName);
                ResultSet rss = pstt.executeQuery();

                boolean hasJobs = false;

                while (rss.next()) {
                    hasJobs = true;

                    String statusValue = rss.getString("status");
                    String companyName1 = rss.getString("companyName");
                    String location = rss.getString("location");
                    String companyInfo = companyName1 + " - " + location;
                    String jobType = rss.getString("jobType");
                    String skills = rss.getString("requirement");
                    String salary = rss.getString("salary");
                    String postedDate = rss.getString("postedAt");
                    String position = rss.getString("position");
                    int id = rss.getInt("id");
                    String editUpdate = rss.getString("editUpdate");
                    String jobIdNo = String.valueOf(id);

                    // View PostedJobsCard panel
                    JPanel postedJobsCardPanel = new JPanel(null);
                    postedJobsCardPanel.setBackground(Color.WHITE);
                    postedJobsCardPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    postedJobsCardPanel.setPreferredSize(new Dimension(580, 130));
                    postedJobsCardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

                    // Delete button
                    JButton deleteButton = new JButton("Delete");
                    deleteButton.setBackground(new Color(220, 53, 69));
                    deleteButton.setForeground(Color.WHITE);
                    deleteButton.setFocusPainted(false);
                    deleteButton.setBorderPainted(false);
                    deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
                    postedJobsCardPanel.add(deleteButton);
                    deleteButton.setBounds(470, 85, 80, 30);

                    // Edit button
                    JButton editButton = new JButton("Edit");
                    editButton.setBackground(new Color(108, 117, 125));
                    editButton.setForeground(Color.WHITE);
                    editButton.setFocusPainted(false);
                    editButton.setBorderPainted(false);
                    editButton.setFont(new Font("Arial", Font.BOLD, 12));
                    postedJobsCardPanel.add(editButton);
                    editButton.setBounds(380, 85, 80, 30);

                    // Company info label
                    JLabel companyInfoLabel = createLabel(companyInfo, companyFont, new Color(33, 37, 41), SwingConstants.LEFT,
                            10, 10, 350, 20, null, false, null, postedJobsCardPanel);

                    // Position label
                    JLabel positionLabel = createLabel(position, positionFont, new Color(40, 167, 69), SwingConstants.LEFT,
                            10, 30, 350, 20, null, false, null, postedJobsCardPanel);

                    // Job type label
                    JLabel jobTypeLabell = createLabel(jobType, detailFont, new Color(108, 117, 125), SwingConstants.LEFT,
                            10, 50, 120, 20, null, false, null, postedJobsCardPanel);

                    // Skills label
                    JLabel skillsLabel = createLabel("Skills: " + skills, detailFont, new Color(108, 117, 125), SwingConstants.LEFT,
                            10, 70, 400, 20, null, false, null, postedJobsCardPanel);

                    // Salary label
                    JLabel salaryLabell = createLabel("Salary: " + salary, detailFont, new Color(40, 167, 69), SwingConstants.LEFT,
                            10, 90, 200, 20, null, false, null, postedJobsCardPanel);

                    // Job id label
                    JLabel jobIdLabel = createLabel("Job Id : " +jobIdNo,  new Font("Arial", Font.BOLD, 12),  new Color(192, 192, 192), SwingConstants.LEFT,
                            10,108,100,20,null,false,null,postedJobsCardPanel);

                    // Date label
                    JLabel dateLabel = createLabel("Posted: " + postedDate, detailFont, new Color(108, 117, 125), SwingConstants.LEFT,
                            400, 10, 150, 20, null, false, null, postedJobsCardPanel);

                    // State label
                    JLabel statusLabel = createLabel(statusValue, new Font("Arial", Font.BOLD, 10), null, SwingConstants.CENTER,
                            490, 36, 60, 20, null, true, null, postedJobsCardPanel);

                    if ("open".equalsIgnoreCase(statusValue)) {
                        statusLabel.setBackground(new Color(40, 167, 69));
                        statusLabel.setForeground(Color.WHITE);
                    } else if ("paused".equalsIgnoreCase(statusValue)) {
                        statusLabel.setBackground(new Color(255, 193, 7));
                        statusLabel.setForeground(Color.BLACK);
                    } else if ("closed".equalsIgnoreCase(statusValue)) {
                        statusLabel.setBackground(new Color(220, 53, 69));
                        statusLabel.setForeground(Color.WHITE);
                    } else {
                        statusLabel.setBackground(new Color(108, 117, 125));
                        statusLabel.setForeground(Color.WHITE);
                    }

                    if ("Edited".equalsIgnoreCase(editUpdate)) {
                        JLabel editedDisplay = createLabel("EDITED", new Font("Arial", Font.BOLD, 10), new Color(33, 37, 41),
                                SwingConstants.CENTER, 418, 36, 60, 20, null, true, new Color(255, 193, 7), postedJobsCardPanel);
                    }

                    deleteButton.addActionListener(b -> {
                        String jobssSql = "DELETE FROM jobs WHERE id = ?";
                        String deletedApplicationsSql = "INSERT INTO deletedApplications (id, companyName, location, jobType, salary, requirement, postedAt, position) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        try (Connection con1 = DriverManager.getConnection(url, user, password)) {
                            try (PreparedStatement pst2 = con1.prepareStatement(jobssSql)) {
                                pst2.setInt(1, id);
                                int deleteJobApp = pst2.executeUpdate();

                                if (deleteJobApp > 0) {
                                    try (PreparedStatement pst3 = con1.prepareStatement(deletedApplicationsSql)) {
                                        pst3.setInt(1, id);
                                        pst3.setString(2, companyName1);
                                        pst3.setString(3, location);
                                        pst3.setString(4, jobType);
                                        pst3.setString(5, salary);
                                        pst3.setString(6, skills);
                                        pst3.setString(7, postedDate);
                                        pst3.setString(8, position);

                                        String employerCheck = "SELECT * FROM companies WHERE employersEmail = ?";
                                        String activitiesUrl = "INSERT INTO activities (empEmail,activity,user)VALUES(?,?,?)";
                                        String message = companyName + " has deleted the job posting for the position of " + position + " as " + jobType + "!";
                                        try (PreparedStatement pst4 = con1.prepareStatement(employerCheck)) {
                                            pst4.setString(1, empEmail);
                                            ResultSet rs4 = pst4.executeQuery();
                                            if (rs4.next()) {
                                                try (PreparedStatement psttt = con1.prepareStatement(activitiesUrl)) {
                                                    psttt.setString(1, empEmail);
                                                    psttt.setString(2, message);
                                                    psttt.setString(3, "Employer");
                                                    psttt.executeUpdate();
                                                }
                                            }
                                        }
                                        pst3.executeUpdate();
                                    }
                                    postedJobsPanel.remove(postedJobsCardPanel);
                                    postedJobsPanel.revalidate();
                                    postedJobsPanel.repaint();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    editButton.addActionListener(e -> {
                        companyInfoLabel.setVisible(false);
                        positionLabel.setVisible(false);
                        jobTypeLabell.setVisible(false);
                        statusLabel.setVisible(false);
                        skillsLabel.setVisible(false);
                        salaryLabell.setVisible(false);
                        editButton.setVisible(false);
                        deleteButton.setVisible(false);

                        // Company name edit TextField
                        JTextField companyNameEditTextField = createTextField(companyName1, null, null, null, 10, 10, 200, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Company location edit TextField
                        JTextField companyLocationEditTextField = createTextField(location, null, null, null, 220, 10, 150, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Position edit TextField
                        JTextField positionEditTextField = createTextField(position, null, null, null, 10, 40, 200, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Job type edit TextField
                        JTextField jobTypeEditTextField = createTextField(jobType, null, null, null, 220, 40, 100, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Salary edit TextField
                        JTextField salaryEditTextField = createTextField(salary, null, null, null, 10, 90, 150, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Skills edit TextField
                        JTextField skillsEditField = createTextField(skills, null, null, null, 10, 70, 360, 25,
                                true, BorderFactory.createLineBorder(new Color(206, 212, 218)),
                                postedJobsCardPanel);

                        // Status dropdown
                        String[] statuses = {"Open", "Paused", "Closed"};
                        JComboBox<String> statusDropdown = new JComboBox<>(statuses);
                        statusDropdown.setBounds(330, 40, 80, 25);
                        statusDropdown.setSelectedItem(statusValue);
                        statusDropdown.setBackground(Color.WHITE);
                        postedJobsCardPanel.add(statusDropdown);

                        // Save edit button
                        JButton saveEditButton = new JButton("Save");
                        saveEditButton.setBackground(new Color(40, 167, 69));
                        saveEditButton.setForeground(Color.WHITE);
                        saveEditButton.setFocusPainted(false);
                        saveEditButton.setBorderPainted(false);
                        saveEditButton.setFont(new Font("Arial", Font.BOLD, 12));
                        saveEditButton.setBounds(420, 70, 70, 30);
                        postedJobsCardPanel.add(saveEditButton);

                        // Cancel button
                        JButton cancelButton = new JButton("Cancel");
                        cancelButton.setBackground(new Color(108, 117, 125));
                        cancelButton.setForeground(Color.WHITE);
                        cancelButton.setFocusPainted(false);
                        cancelButton.setBorderPainted(false);
                        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
                        cancelButton.setBounds(500, 70, 70, 30);
                        postedJobsCardPanel.add(cancelButton);

                        postedJobsCardPanel.revalidate();
                        postedJobsCardPanel.repaint();

                        saveEditButton.addActionListener(z -> {
                            String updatedCompany = companyNameEditTextField.getText();
                            String updatedLocation = companyLocationEditTextField.getText();
                            String updatedPosition = positionEditTextField.getText();
                            String updatedJobType = jobTypeEditTextField.getText();
                            String updatedSkills = skillsEditField.getText();
                            String updatedSalary = salaryEditTextField.getText();
                            String updatedStatus = (String) statusDropdown.getSelectedItem();

                            try (Connection con = DriverManager.getConnection(url, user, password)) {
                                String jobssSql = "UPDATE jobs SET companyName = ?, location = ?, position = ?, jobType = ?, requirement = ?, salary = ?, status = ?, editUpdate = ? WHERE id = ?";
                                PreparedStatement pst = con.prepareStatement(jobssSql);
                                pst.setString(1, updatedCompany);
                                pst.setString(2, updatedLocation);
                                pst.setString(3, updatedPosition);
                                pst.setString(4, updatedJobType);
                                pst.setString(5, updatedSkills);
                                pst.setString(6, updatedSalary);
                                pst.setString(7, updatedStatus);
                                pst.setString(8, "Edited");
                                pst.setInt(9, id);

                                int rowsAffected = pst.executeUpdate();
                                if (rowsAffected > 0) {
                                    String activities = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                                    try (PreparedStatement pst6 = con.prepareStatement(activities)) {
                                        pst6.setString(1, empEmail);
                                        pst6.setString(2, companyName + " has edited their job post of " + updatedPosition + " as " + updatedJobType);
                                        pst6.setString(3, "Employer");
                                        pst6.executeUpdate();
                                    }
                                    JOptionPane.showMessageDialog(null, "Job \"" + updatedPosition + "\" has been updated successfully!", "Job Updated", JOptionPane.INFORMATION_MESSAGE);

                                    // Update labels
                                    companyInfoLabel.setText(updatedCompany + " - " + updatedLocation);
                                    positionLabel.setText(updatedPosition);
                                    jobTypeLabell.setText(updatedJobType);
                                    skillsLabel.setText("Skills: " + updatedSkills);
                                    salaryLabell.setText("Salary: " + updatedSalary);
                                    statusLabel.setText(updatedStatus);

                                    if ("open".equalsIgnoreCase(updatedStatus)) {
                                        statusLabel.setBackground(new Color(40, 167, 69));
                                        statusLabel.setForeground(Color.WHITE);
                                    } else if ("paused".equalsIgnoreCase(updatedStatus)) {
                                        statusLabel.setBackground(new Color(255, 193, 7));
                                        statusLabel.setForeground(Color.BLACK);
                                    } else if ("closed".equalsIgnoreCase(updatedStatus)) {
                                        statusLabel.setBackground(new Color(220, 53, 69));
                                        statusLabel.setForeground(Color.WHITE);
                                    }

                                    boolean hasEditedLabel = false;
                                    for (Component comp : postedJobsCardPanel.getComponents()) {
                                        if (comp instanceof JLabel && ((JLabel) comp).getText().equals("EDITED")) {
                                            hasEditedLabel = true;
                                            break;
                                        }
                                    }

                                    if (!hasEditedLabel) {
                                        JLabel editedDisplay = createLabel("EDITED", new Font("Arial", Font.BOLD, 10), new Color(33, 37, 41),
                                                SwingConstants.CENTER, 400, 30, 80, 20, null, true, new Color(255, 193, 7), postedJobsCardPanel
                                        );
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Error while updating job: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }

                            postedJobsCardPanel.remove(companyLocationEditTextField);
                            postedJobsCardPanel.remove(companyLocationEditTextField);
                            postedJobsCardPanel.remove(positionEditTextField);
                            postedJobsCardPanel.remove(jobTypeEditTextField);
                            postedJobsCardPanel.remove(skillsEditField);
                            postedJobsCardPanel.remove(salaryEditTextField);
                            postedJobsCardPanel.remove(statusDropdown);
                            postedJobsCardPanel.remove(saveEditButton);
                            postedJobsCardPanel.remove(cancelButton);

                            companyInfoLabel.setVisible(true);
                            positionLabel.setVisible(true);
                            jobTypeLabell.setVisible(true);
                            statusLabel.setVisible(true);
                            skillsLabel.setVisible(true);
                            salaryLabell.setVisible(true);
                            editButton.setVisible(true);
                            deleteButton.setVisible(true);
                            postedJobsCardPanel.revalidate();
                            postedJobsCardPanel.repaint();
                        });

                        cancelButton.addActionListener(z -> {
                            postedJobsCardPanel.remove(companyNameEditTextField);
                            postedJobsCardPanel.remove(companyLocationEditTextField);
                            postedJobsCardPanel.remove(positionEditTextField);
                            postedJobsCardPanel.remove(jobTypeEditTextField);
                            postedJobsCardPanel.remove(skillsEditField);
                            postedJobsCardPanel.remove(salaryEditTextField);
                            postedJobsCardPanel.remove(statusDropdown);
                            postedJobsCardPanel.remove(saveEditButton);
                            postedJobsCardPanel.remove(cancelButton);

                            companyInfoLabel.setVisible(true);
                            positionLabel.setVisible(true);
                            jobTypeLabell.setVisible(true);
                            statusLabel.setVisible(true);
                            skillsLabel.setVisible(true);
                            salaryLabell.setVisible(true);
                            editButton.setVisible(true);
                            deleteButton.setVisible(true);
                            postedJobsCardPanel.revalidate();
                            postedJobsCardPanel.repaint();
                        });
                    });

                    postedJobsPanel.add(postedJobsCardPanel);
                    postedJobsPanel.add(Box.createVerticalStrut(10));
                }

                if (!hasJobs) {
                    JLabel noJobsLabel = new JLabel("You haven't yet posted any job applications");
                    noJobsLabel.setFont(new Font("Arial", Font.BOLD, 14));
                    noJobsLabel.setForeground(new Color(150, 150, 150));
                    noJobsLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    JPanel noJobsPanel = new JPanel(new BorderLayout());
                    noJobsPanel.setPreferredSize(new Dimension(580, 100));
                    noJobsPanel.add(noJobsLabel, BorderLayout.CENTER);

                    postedJobsPanel.add(noJobsPanel);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            postedJobsPanel.revalidate();
            postedJobsPanel.repaint();
            employerCardLayout.show(employerPanel, "View Posted Jobs");
        });

        // Sidebar schedule interview button
        JButton scheduleInterviewButton = new JButton("Schedule Interview");
        scheduleInterviewButton.setContentAreaFilled(false);
        scheduleInterviewButton.setOpaque(false);
        scheduleInterviewButton.setFont(new Font("Calibri", Font.BOLD, 14));
        scheduleInterviewButton.setForeground(Color.BLACK);
        scheduleInterviewButton.setFocusPainted(false);
        scheduleInterviewButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        scheduleInterviewButton.setHorizontalAlignment(SwingConstants.CENTER);
        scheduleInterviewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        scheduleInterviewButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                scheduleInterviewButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                scheduleInterviewButton.setForeground(Color.BLACK);
            }
        });
        employerContainer.add(scheduleInterviewButton);
        scheduleInterviewButton.setBounds(0,210,190,30);

        // Interview scheduling panel
        JPanel interviewSchedulingPanel = new JPanel(new BorderLayout());
        interviewSchedulingPanel.setBackground(new Color(245, 247, 250));
        employerPanel.add(interviewSchedulingPanel, "Schedule Interview");

        scheduleInterviewButton.addActionListener(
                a -> {
                    interviewSchedulingPanel.removeAll();
                    interviewSchedulingPanel.setLayout(null);
                    interviewSchedulingPanel.setBackground(new Color(245, 247, 250));

                    int panelWidth = interviewSchedulingPanel.getWidth() > 0 ? interviewSchedulingPanel.getWidth() : 700;
                    int panelHeight = interviewSchedulingPanel.getHeight() > 0 ? interviewSchedulingPanel.getHeight() : 600;

                    int leftMargin = 40;
                    int availableWidth = panelWidth - (leftMargin * 2);
                    int labelWidth = 140;
                    int fieldWidth = Math.min(220, availableWidth - labelWidth - 20);
                    int gapY = 35;
                    int y = 15;

                    // Schedule interview label
                    JLabel scheduleInterviewLabel = createLabel("Schedule Interview", titleFont, new Color(44, 62, 80),
                            SwingConstants.CENTER, leftMargin, y, availableWidth, 30,
                            BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)),
                                    BorderFactory.createEmptyBorder(5, 0, 8, 0)
                            ), false, null, interviewSchedulingPanel);
                    y += 45;

                    // Job ID label
                    JLabel jobIdLabel = createLabel("Job ID:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Job ID TextField
                    JTextField jobIdTextField = createTextField("", null, null, null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    y += gapY;

                    // Jobseeker name label
                    JLabel jobseekerNameLabel = createLabel("Jobseeker Name:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Jobseeker name TextField
                    JTextField jobseekerNameTextField = createTextField("", null, null, null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    y += gapY;

                    // Jobseeker email label
                    JLabel jobseekerEmailLabel = createLabel("Jobseeker Email:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Jobseeker email TextField
                    JTextField jobseekerEmailTextField = createTextField("", null, null, null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    y += gapY;

                    // Employer Email label
                    JLabel employerEmailLabel = createLabel("Employer Email:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Employer email TextField
                    JTextField employerEmailTextField = createTextField("", null, null, null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    y += gapY;

                    int halfFieldWidth = (fieldWidth - 10) / 2;

                    // Date label
                    JLabel dateLabel = createLabel("Date:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Date TextField
                    JTextField dateTextField = createTextField("yyyy-MM-dd", null, Color.GRAY, null, leftMargin + labelWidth + 10, y, halfFieldWidth, 28,
                            true, null, interviewSchedulingPanel);

                    // Date placeholder behavior
                    dateTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (dateTextField.getText().equals("yyyy-MM-dd")) {
                                dateTextField.setText("");
                                dateTextField.setForeground(Color.BLACK);
                            }
                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            if (dateTextField.getText().isEmpty()) {
                                dateTextField.setText("yyyy-MM-dd");
                                dateTextField.setForeground(Color.GRAY);
                            }
                        }
                    });

                    // Time label
                    JLabel timeLabel = createLabel("Time:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin + labelWidth + 25 + halfFieldWidth, y, 50, 25, null, false, null, interviewSchedulingPanel);

                    // Time TextField
                    JTextField timeTextField = createTextField("HH:mm", null, Color.GRAY, null, leftMargin + labelWidth + 75 + halfFieldWidth, y, halfFieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    timeTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (timeTextField.getText().equals("HH:mm")) {
                                timeTextField.setText("");
                                timeTextField.setForeground(Color.BLACK);
                            }
                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            if (timeTextField.getText().isEmpty()) {
                                timeTextField.setText("HH:mm");
                                timeTextField.setForeground(Color.GRAY);
                            }
                        }
                    });
                    y += gapY;

                    // Interview mode label
                    JLabel interviewModeLabel = createLabel("Interview Mode:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Dropdown for interview mode
                    String[] modes = {"Online", "Offline"};
                    JComboBox<String> modeCombo = new JComboBox<>(modes);
                    modeCombo.setBounds(leftMargin + labelWidth + 10, y, fieldWidth, 28);
                    modeCombo.setFont(fieldFont);
                    modeCombo.setBackground(Color.WHITE);
                    modeCombo.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
                    modeCombo.setFocusable(true);
                    interviewSchedulingPanel.add(modeCombo);
                    y += gapY;

                    // Location label
                    JLabel locationLabell = createLabel("Location:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);


                    // Location TextField
                    JTextField locationTextFieldd = createTextField("Zoom link or Office address", null, new Color(107, 114, 128), null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    locationTextFieldd.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (locationTextFieldd.getText().equals("Zoom link or Office address")) {
                                locationTextFieldd.setText("");
                                locationTextFieldd.setForeground(Color.BLACK);
                            }
                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            if (locationTextFieldd.getText().isEmpty()) {
                                locationTextFieldd.setText("Zoom link or Office address");
                                locationTextFieldd.setForeground(new Color(107, 114, 128));
                            }
                        }
                    });
                    y += gapY;

                    // Remarks label
                    JLabel remarksLabel = createLabel("Remarks:", labelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, interviewSchedulingPanel);

                    // Remarks TextField
                    JTextField remarksTextField = createTextField("Optional notes...", null, new Color(107, 114, 128), null, leftMargin + labelWidth + 10, y, fieldWidth, 28,
                            true, null, interviewSchedulingPanel);
                    remarksTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (remarksTextField.getText().equals("Optional notes...")) {
                                remarksTextField.setText("");
                                remarksTextField.setForeground(Color.BLACK);
                            }

                        }
                        @Override
                        public void focusLost(FocusEvent e) {
                            if (remarksTextField.getText().isEmpty()) {
                                remarksTextField.setText("Optional notes...");
                                remarksTextField.setForeground(new Color(107, 114, 128));
                            }
                        }
                    });
                    y += gapY + 15;

                    int buttonWidth = Math.min(300, availableWidth);
                    int buttonX = leftMargin + (availableWidth - buttonWidth) / 2;

                    // Schedule interview button
                    JButton scheduleInterviewButtonn = new JButton("Schedule Interview");
                    scheduleInterviewButtonn.setBounds(265, y, 140, 38);
                    scheduleInterviewButtonn.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    scheduleInterviewButtonn.setBackground (new Color(0, 123, 255));
                    scheduleInterviewButtonn.setForeground(Color.WHITE);
                    scheduleInterviewButtonn.setFocusPainted(false);
                    scheduleInterviewButtonn.setBorder(BorderFactory.createEmptyBorder());
                    scheduleInterviewButtonn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    interviewSchedulingPanel.add(scheduleInterviewButtonn);

                    scheduleInterviewButtonn.addActionListener(
                            b -> {

                                String interviewScheduleSql = "INSERT INTO interviewSchedule (job_id,jobseeker_name, jobseeker_email, employer_email, scheduled_date, scheduled_time, mode, location, remarks) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?)";
                                try (Connection con = DriverManager.getConnection(url, user, password)) {
                                    try (PreparedStatement pst1 = con.prepareStatement(interviewScheduleSql)) {
                                        int jobId = Integer.parseInt(jobIdTextField.getText().trim());
                                        String JSName = jobseekerNameTextField.getText().trim();
                                        String JSEmail = jobseekerEmailTextField.getText().trim();
                                        String EEmail = employerEmailTextField.getText().trim();

                                        String SDate = dateTextField.getText().trim();
                                        if (SDate.equals("yyyy-MM-dd")) SDate = "";

                                        String STime = timeTextField.getText().trim();
                                        if (STime.equals("HH:mm")) STime = "";

                                        String SMode = modeCombo.getSelectedItem().toString();

                                        String ILocation = locationTextFieldd.getText().trim();
                                        if (ILocation.equals("Zoom link or Office address")) ILocation = "";

                                        String IRemarks = remarksTextField.getText().trim();
                                        if (IRemarks.equals("Optional notes...")) IRemarks = "";

                                        String interviewMessage = "Your interview with " + companyName
                                                + " has been scheduled on " + SDate
                                                + " at " + STime + " (" + SMode + "). Please check your email for details.";

                                        String jsNotificationSql = "INSERT INTO jsnotifications (notifications, emailJS) VALUES (?, ?)";

                                        try (PreparedStatement pst = con.prepareStatement(jsNotificationSql)) {
                                            pst.setString(1, interviewMessage);
                                            pst.setString(2, JSEmail);
                                            pst.executeUpdate();
                                            System.out.println("Interview notification sent successfully to " + JSEmail);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }


                                        String companiesSql = "SELECT * FROM companies WHERE employersEmail = ?";
                                        String activitiesSql = "INSERT INTO activities (empEmail,activity,user)VALUES(?,?,?)";
                                        String message = companyName + " has scheduled an interview for " + JSName + " on " + SDate + " " + STime + " (" + SMode + ")!";

                                        try (PreparedStatement pstt = con.prepareStatement(companiesSql)) {
                                            pstt.setString(1, empEmail);
                                            ResultSet rs7 = pstt.executeQuery();
                                            if (rs7.next()) {
                                                try (PreparedStatement psttt = con.prepareStatement(activitiesSql)) {
                                                    psttt.setString(1, empEmail);
                                                    psttt.setString(2, message);
                                                    psttt.setString(3, "Employer");
                                                    psttt.executeUpdate();
                                                }
                                            }
                                        }

                                        if (jobId == 0 || JSName.isEmpty() || JSEmail.isEmpty() || EEmail.isEmpty() || SDate.isEmpty() || STime.isEmpty() || SMode == null || SMode.trim().isEmpty() || ILocation.isEmpty()) {
                                            JOptionPane.showMessageDialog(null, "⚠ Please fill all required fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                                            return;
                                        }

                                        pst1.setInt(1, jobId);
                                        pst1.setString(2, JSName);
                                        pst1.setString(3, JSEmail);
                                        pst1.setString(4, EEmail);
                                        pst1.setString(5, SDate);
                                        pst1.setString(6, STime);
                                        pst1.setString(7, SMode);
                                        pst1.setString(8, ILocation);
                                        pst1.setString(9, IRemarks);

                                        int rowsInserted = pst1.executeUpdate();

                                        if (rowsInserted > 0) {
                                            JOptionPane.showMessageDialog(null, "Interview scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                                            jobIdTextField.setText("");
                                            jobseekerNameTextField.setText("");
                                            jobseekerEmailTextField.setText("");
                                            employerEmailTextField.setText("");
                                            dateTextField.setText("yyyy-MM-dd");
                                            dateTextField.setForeground(Color.GRAY);
                                            timeTextField.setText("HH:mm");
                                            timeTextField.setForeground(Color.GRAY);
                                            modeCombo.setSelectedIndex(0);
                                            locationTextFieldd.setText("Office address");
                                            locationTextField.setForeground(new Color(107, 114, 128));
                                            remarksTextField.setText("Optional notes...");
                                            remarksTextField.setForeground(new Color(107, 114, 128));
                                        }

                                    }
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, "Please enter a valid Job ID number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Error scheduling interview: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                            }
                    );
                    interviewSchedulingPanel.revalidate();
                    interviewSchedulingPanel.repaint();
                    employerCardLayout.show(employerPanel, "Schedule Interview");
                }
        );

        // Sidebar offer letter button
        JButton offerLetterButton = new JButton("Offer Letter");
        offerLetterButton.setContentAreaFilled(false);
        offerLetterButton.setOpaque(false);
        offerLetterButton.setFont(new Font("Calibri", Font.BOLD, 14));
        offerLetterButton.setForeground(Color.BLACK);
        offerLetterButton.setFocusPainted(false);
        offerLetterButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        offerLetterButton.setHorizontalAlignment(SwingConstants.CENTER);
        offerLetterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        offerLetterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                offerLetterButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                offerLetterButton.setForeground(Color.BLACK);
            }
        });
        offerLetterButton.setBounds(0,240,190,30);
        employerContainer.add(offerLetterButton);

        // Offer scheduling panel
        JPanel offerSchedulingPanel = new JPanel();
        offerSchedulingPanel.setBackground(new Color(245, 247, 250));
        employerPanel.add(offerSchedulingPanel, "Offer Letter");

        offerLetterButton.addActionListener(
                a -> {
                    offerSchedulingPanel.removeAll();
                    offerSchedulingPanel.setLayout(null);
                    offerSchedulingPanel.setBackground(new Color(245, 247, 250));

                    int panelWidth = offerSchedulingPanel.getWidth() > 0 ? offerSchedulingPanel.getWidth() : 700;

                    int gapY = 40;
                    int y = 20;
                    int leftMargin = 50;
                    int availableWidth = panelWidth - (leftMargin * 2);
                    int labelWidth = 150;
                    int fieldWidth = Math.min(250, availableWidth - labelWidth - 20);

                    // Header label
                    JLabel HeaderLabel = createLabel("Send Offer Letter", new Font("Segoe UI", Font.BOLD, 24),
                            new Color(44, 62, 80), SwingConstants.CENTER, leftMargin, y, availableWidth, 35,
                            BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)),
                                    BorderFactory.createEmptyBorder(5, 0, 10, 0)
                            ), false, null, offerSchedulingPanel);
                    y += 55;

                    // Job ID Label
                    JLabel jobIdLabel = createLabel("Job ID:", enhancedLabelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Job ID TextField
                    JTextField jobIdTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);

                    offerSchedulingPanel.add(jobIdLabel);
                    y += gapY;

                    // Jobseeker Name label
                    JLabel jobseekerNameLabel = createLabel("Jobseeker Name:", enhancedLabelFont, new Color(52, 73, 94),
                            SwingConstants.LEFT, leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Jobseeker name TextField
                    JTextField jobseekerNameTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);;
                    y += gapY;

                    // Jobseeker email label
                    JLabel jobseekerEmailLabel = createLabel("Jobseeker Email:", enhancedLabelFont, new Color(52, 73, 94),
                            SwingConstants.LEFT, leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Jobseeker email TextField
                    JTextField jobseekerEmailTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);
                    y += gapY;

                    // Employer email label
                    JLabel employerEmailLabel = createLabel("Employer Email:", enhancedLabelFont, new Color(52, 73, 94),
                            SwingConstants.LEFT, leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Employer email TextField
                    JTextField employerEmailTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);
                    y += gapY;

                    // Position label
                    JLabel positionLabel = createLabel("Position:", enhancedLabelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Position TextField
                    JTextField positionTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);
                    y += gapY;

                    // Package label
                    JLabel packageLabel = createLabel("Package:", enhancedLabelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Package TextField
                    JTextField packageTextField = createTextField("", fieldFont, null, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);
                    y += gapY;

                    // Joining date label
                    JLabel joiningDateLabel = createLabel("Joining Date:", enhancedLabelFont, new Color(52, 73, 94), SwingConstants.LEFT,
                            leftMargin, y, labelWidth, 25, null, false, null, offerSchedulingPanel);

                    // Joining date TextField
                    JTextField joiningDateTextField = createTextField("yyyy-MM-dd", fieldFont, Color.GRAY, Color.WHITE, leftMargin + labelWidth + 10, y, fieldWidth, 30,
                            true, BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                                    BorderFactory.createEmptyBorder(5, 8, 5, 8)), offerSchedulingPanel);

                    // Add focus listener for placeholder behavior
                    joiningDateTextField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (joiningDateTextField.getText().equals("yyyy-MM-dd")) {
                                joiningDateTextField.setText("");
                                joiningDateTextField.setForeground(Color.BLACK);
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            if (joiningDateTextField.getText().isEmpty()) {
                                joiningDateTextField.setForeground(Color.GRAY);
                                joiningDateTextField.setText("yyyy-MM-dd");
                            }
                        }
                    });

                    y += gapY + 20;
                    int buttonY = y;
                    int button1Width = 180;
                    int button2Width = 140;
                    int buttonGap = 20;
                    int totalButtonWidth = button1Width + button2Width + buttonGap;

                    // Send offer button
                    JButton sendOfferButton = new JButton("Send Offer");
                    sendOfferButton.setBounds(156 + button1Width + buttonGap, buttonY, 100, 35);
                    sendOfferButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    sendOfferButton.setBackground(new Color(0, 123, 255));
                    sendOfferButton.setForeground(Color.WHITE);
                    sendOfferButton.setBorder(BorderFactory.createEmptyBorder());
                    sendOfferButton.setFocusPainted(false);
                    sendOfferButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    offerSchedulingPanel.add(sendOfferButton);

                    sendOfferButton.addActionListener(
                            p -> {
                                String offerLettersSql = "INSERT INTO offerletters (job_id, jobseeker_name, jobseeker_email, employer_email, position,package, joining_date) VALUES (?, ?, ?, ?,?, ?, ?)";

                                String companyNamee = companyName;

                                try (Connection con1 = DriverManager.getConnection(url, user, password)) {
                                    try (PreparedStatement pst2 = con1.prepareStatement(offerLettersSql)) {
                                        int jobId = Integer.parseInt(jobIdTextField.getText().trim());
                                        String JSName = jobseekerNameTextField.getText().trim();
                                        String JSEmail = jobseekerEmailTextField.getText().trim();
                                        String EEmail = employerEmailTextField.getText().trim();
                                        String position = positionTextField.getText().trim();
                                        String salary = packageTextField.getText().trim();
                                        String joiningDate = joiningDateTextField.getText().trim();

                                        if (joiningDate.equals("yyyy-MM-dd")) {
                                            joiningDate = "";
                                        }

                                        String offerMessage = "You have received a job offer from " + companyNamee + "! Please check your email for details.";

                                        String jsNotificationSql = "INSERT INTO jsnotifications (notifications, emailJS) VALUES (?, ?)";

                                        try (PreparedStatement pst = con1.prepareStatement(jsNotificationSql)) {
                                            pst.setString(1, offerMessage);
                                            pst.setString(2, JSEmail);
                                            pst.executeUpdate();
                                            System.out.println("Offer notification sent successfully to " + JSEmail);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                        String companiesSql = "SELECT * FROM companies WHERE employersEmail = ?";
                                        String activitiesSql = "INSERT INTO activities (empEmail,activity,user)VALUES(?,?,?)";
                                        String message = companyName + " has sent a offer letter to " + JSName + " for " + position + "!";

                                        try (PreparedStatement pstt = con1.prepareStatement(companiesSql)) {
                                            pstt.setString(1, empEmail);
                                            ResultSet rs7 = pstt.executeQuery();
                                            if (rs7.next()) {
                                                try (PreparedStatement psttt = con1.prepareStatement(activitiesSql)) {
                                                    psttt.setString(1, empEmail);
                                                    psttt.setString(2, message);
                                                    psttt.setString(3, "Employer");

                                                    String sqlUpdate = "UPDATE hirings h SET h.hiringStatus = ( CASE WHEN EXISTS ( SELECT 1 FROM offerletters o " +
                                                            " WHERE o.companyName = h.companyName AND o.offer_date >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) " +
                                                            " THEN 'Hired' ELSE 'No recent activity' END) WHERE h.companyName = ?";

                                                    try (PreparedStatement pst = con1.prepareStatement(sqlUpdate)) {
                                                        pst.setString(1, companyName);
                                                        int rowsUpdated = pst.executeUpdate();

                                                        if (rowsUpdated == 0) {
                                                            String companyProfileSql = "SELECT * FROM companyProfile WHERE companyName = ?";
                                                            try (PreparedStatement pst1 = con1.prepareStatement(companyProfileSql)) {
                                                                pst1.setString(1, companyName);
                                                                try (ResultSet rs1 = pst1.executeQuery()) {
                                                                    if (rs1.next()) {
                                                                        String getCompanyName = rs1.getString("companyName");
                                                                        String getCompanyWebsite = rs1.getString("website");
                                                                        String getIndustry = rs1.getString("industry");
                                                                        int getOPosition = rs1.getInt("openPositions");
                                                                        String getHStatus = rs1.getString("hiringStatus");

                                                                        String hiringsSql = "INSERT INTO hirings " +
                                                                                "(companyName, companyWebsite, industry, openPositions, hiringStatus) " +
                                                                                "VALUES (?, ?, ?, ?, ?)";
                                                                        try (PreparedStatement pst3 = con1.prepareStatement(hiringsSql)) {
                                                                            pst3.setString(1, getCompanyName);
                                                                            pst3.setString(2, getCompanyWebsite);
                                                                            pst3.setString(3, getIndustry);
                                                                            pst3.setInt(4, getOPosition);
                                                                            pst3.setString(5, getHStatus);
                                                                            pst3.executeUpdate();
                                                                            System.out.println("Company inserted into hirings: " + getCompanyName);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            System.out.println("Rows updated: " + rowsUpdated);
                                                        }

                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    psttt.executeUpdate();
                                                }
                                            }
                                        }

                                        if (jobId == 0 || JSName.isEmpty() || JSEmail.isEmpty() || EEmail.isEmpty() || joiningDate.isEmpty()) {
                                            JOptionPane.showMessageDialog(null, "Please fill all the fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                                            return;
                                        }

                                        pst2.setInt(1, jobId);
                                        pst2.setString(2, JSName);
                                        pst2.setString(3, JSEmail);
                                        pst2.setString(4, EEmail);
                                        pst2.setString(5, position);
                                        pst2.setString(6, salary);
                                        pst2.setString(7, joiningDate);

                                        int rowsInserted = pst2.executeUpdate();
                                        if (rowsInserted > 0) {
                                            JOptionPane.showMessageDialog(null, "Offer letter sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Error sending offer letter: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                    );
                    offerSchedulingPanel.revalidate();
                    offerSchedulingPanel.repaint();
                    employerCardLayout.show(employerPanel, "Offer Letter");
                }
        );

        // Sidebar activities button
        JButton activitiesButton = new JButton("Activities");
        activitiesButton.setContentAreaFilled(false);
        activitiesButton.setOpaque(false);
        activitiesButton.setFont(new Font("Calibri", Font.BOLD, 14));
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
        activitiesButton.setFont(buttonFont);
        activitiesButton.setBounds(0,270,190,30);
        employerContainer.add(activitiesButton);

        // Activities panel
        JPanel activitiesPanel = new JPanel();
        activitiesPanel.setLayout(new BoxLayout(activitiesPanel, BoxLayout.Y_AXIS));
        activitiesPanel.setBackground(new Color(245, 248, 252));
        activitiesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Allows scrolling for activities panel
        JScrollPane activitiesPanelScrollPane = new JScrollPane(activitiesPanel);
        activitiesPanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        activitiesPanelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        activitiesPanelScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        activitiesPanelScrollPane.setBorder(null);
        activitiesPanelScrollPane.getViewport().setBackground(new Color(245, 248, 252));

        activitiesPanelScrollPane.setBounds(0, 0, 600, 400);
        employerPanel.add(activitiesPanelScrollPane, "Activities");

        activitiesButton.addActionListener(y -> {
            activitiesPanel.removeAll();

            // Header panel
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(245, 248, 252));
            headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
            headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            // Header label
            JLabel headerLabel = createLabel("RECENT ACTIVITIES", new Font("Arial", Font.BOLD, 22), new Color(33, 37, 41),
                    SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, null);
            headerPanel.add(headerLabel, BorderLayout.CENTER);

            String activitiesSql = "SELECT * FROM activities WHERE empEmail = ? ORDER BY activityTime DESC";

            int activityCount = 0;

            try(Connection con = DriverManager.getConnection(url,user,password)) {
                try(PreparedStatement pst = con.prepareStatement(activitiesSql)) {
                    pst.setString(1, empEmail);
                    ResultSet rs = pst.executeQuery();

                    while(rs.next()) {
                        String message = rs.getString("activity");
                        String messageTime = rs.getString("activityTime");
                        activityCount++;

                        // Activities card panel
                        JPanel activitiesCard = new JPanel(null);
                        activitiesCard.setBackground(Color.WHITE);
                        activitiesCard.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                                BorderFactory.createEmptyBorder(12, 15, 12, 15)
                        ));
                        activitiesCard.setPreferredSize(new Dimension(580, 65));
                        activitiesCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

                        // Icon label
                        JLabel iconLabel = createLabel("", new Font("Arial", Font.PLAIN, 16), null, SwingConstants.LEFT,
                                10, 15, 20, 20, null, false, null, activitiesCard);

                        if (message.toLowerCase().contains("deleted")) {
                            iconLabel.setText("●");
                            iconLabel.setForeground(new Color(220, 53, 69));
                        } else if (message.toLowerCase().contains("edit")) {
                            iconLabel.setText("●");
                            iconLabel.setForeground(new Color(255, 193, 7));
                        } else if (message.toLowerCase().contains("post")) {
                            iconLabel.setText("●");
                            iconLabel.setForeground(new Color(40, 167, 69));
                        } else {
                            iconLabel.setText("●");
                            iconLabel.setForeground(new Color(0, 123, 255));
                        }

                        // Activity message
                        JLabel messageLabel = createLabel("<html>" + message + "</html>", new Font("Arial", Font.PLAIN, 13), new Color(33, 37, 41), JLabel.LEFT,
                                30, 8, 420, 40, null, false, null, activitiesCard
                        );
                        messageLabel.setVerticalAlignment(JLabel.TOP);

                        // Message time label
                        JLabel messageTimeLabel = createLabel(messageTime, new Font("Arial", Font.PLAIN, 11), new Color(108, 117, 125),
                                SwingConstants.RIGHT, 457, 8, 120, 20, null, false, null, activitiesCard);

                        // Activity status indicator
                        JLabel statusIndicatorLabel = createLabel("-", new Font("Arial", Font.PLAIN, 12), null, SwingConstants.LEFT,
                                455, 10, 10, 15, null, false, null, activitiesCard);

                        if (message.toLowerCase().contains("deleted")) {
                            statusIndicatorLabel.setForeground(new Color(220, 53, 69));
                        } else if (message.toLowerCase().contains("edit")) {
                            statusIndicatorLabel.setForeground(new Color(255, 193, 7));
                        } else if (message.toLowerCase().contains("post")) {
                            statusIndicatorLabel.setForeground(new Color(40, 167, 69));
                        } else {
                            statusIndicatorLabel.setForeground(new Color(0, 123, 255));
                        }
                        activitiesPanel.add(activitiesCard);
                        activitiesPanel.add(Box.createVerticalStrut(8));
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();

                // Error panel
                JPanel errorPanel = new JPanel();
                errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
                errorPanel.setBackground(new Color(245, 248, 252));
                errorPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
                errorPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                // Error message label
                JLabel errorMessageLabel = createLabel("Unable to load activities", new Font("Arial", Font.BOLD, 16),
                        new Color(220, 53, 69), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, errorPanel);
                errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Error submessage label
                JLabel errorSubMessageLabel = createLabel("Please check your connection and try again",
                        new Font("Arial", Font.PLAIN, 12), new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0,
                        null, false, null, errorPanel);
                errorSubMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                errorPanel.add(Box.createVerticalGlue());
                errorPanel.add(errorMessageLabel);
                errorPanel.add(Box.createVerticalStrut(8));
                errorPanel.add(errorSubMessageLabel);
                errorPanel.add(Box.createVerticalGlue());

                activitiesPanel.add(errorPanel);
            }

            if (activityCount == 0) {

                // Empty panel
                JPanel emptyPanel = new JPanel();
                emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
                emptyPanel.setBackground(new Color(245, 248, 252));
                emptyPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
                emptyPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                // Empty message label
                JLabel emptyMessageLabel = createLabel("No recent activities", new Font("Arial", Font.BOLD, 16),
                        new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, emptyPanel);
                emptyMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Empty submessage label
                JLabel emptySubMessageLabel = createLabel("Your activities will appear here as you interact with the system",
                        new Font("Arial", Font.PLAIN, 12), new Color(108, 117, 125), SwingConstants.CENTER, 0, 0, 0, 0,
                        null, false, null, emptyPanel);
                emptySubMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                emptyPanel.add(Box.createVerticalGlue());
                emptyPanel.add(emptyMessageLabel);
                emptyPanel.add(Box.createVerticalStrut(8));
                emptyPanel.add(emptySubMessageLabel);
                emptyPanel.add(Box.createVerticalGlue());

                activitiesPanel.add(emptyPanel);
            } else {
                // Summary panel
                JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                summaryPanel.setBackground(new Color(245, 248, 252));
                summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
                summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                // Summary label
                JLabel summaryLabel = createLabel("Showing " + activityCount + " recent activities",
                        new Font("Arial", Font.PLAIN, 12), new Color(108, 117, 125), SwingConstants.LEFT, 0, 0, 0, 0,
                        null, false, null, summaryPanel);
            }
            activitiesPanel.revalidate();
            activitiesPanel.repaint();
            employerCardLayout.show(employerPanel, "Activities");
        });

        // Sidebar rate & feedback button
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
                rateFeedbackButton.setForeground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                rateFeedbackButton.setForeground(Color.BLACK);
            }
        });
        employerContainer.add(rateFeedbackButton);
        rateFeedbackButton.setBounds(0, 300, 190, 30);

        // Rate feedback panel
        JPanel rateFeedbackPanel = new JPanel();
        rateFeedbackPanel.setBackground(new Color(248, 249, 250));
        rateFeedbackPanel.setLayout(null);

        // Title label
        JLabel titleLabell = createLabel("Rate & Feedback", new Font("Arial", Font.BOLD, 22), new Color(33, 37, 41),
                SwingConstants.CENTER, 50, 20, 480, 30, null, false, null, rateFeedbackPanel);

        // Rating panel
        JPanel ratingPanel = new JPanel();
        ratingPanel.setLayout(null);
        ratingPanel.setBackground(Color.WHITE);
        ratingPanel.setBounds(50, 70, 480, 90);
        ratingPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        rateFeedbackPanel.add(ratingPanel);

        // Rating label
        JLabel ratingLabel = createLabel("Your Rating:", new Font("Arial", Font.BOLD, 14), null, SwingConstants.LEFT,
                20, 20, 100, 25, null, false, null, ratingPanel);

        final JLabel[] stars = new JLabel[5];
        final int[] currentRating = {0};

        for (int i = 0; i < 5; i++) {
            stars[i] = new JLabel("★", SwingConstants.CENTER);
            stars[i].setFont(new Font("Segoe UI Symbol", Font.PLAIN, 30));
            stars[i].setForeground(new Color(220, 220, 220));
            stars[i].setBounds(140 + (i * 50), 10, 45, 45);
            stars[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            stars[i].setOpaque(false);

            final int starIndex = i;
            stars[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    currentRating[0] = starIndex + 1;
                    updateStars(stars, currentRating[0]);
                }
            });
            ratingPanel.add(stars[i]);
        }

        // Rating Text label
        JLabel ratingTextLabel = createLabel("Click to rate", new Font("Arial", Font.ITALIC, 11),
                new Color(108, 117, 125), SwingConstants.CENTER, 150, 50, 225, 20, null, false, null, ratingPanel);

        // Ratings likes label
        JLabel ratingLikeLabel = createLabel("What did you like the most?", new Font("Arial", Font.BOLD, 14),
                new Color(33, 37, 41), SwingConstants.LEFT, 50, 180, 250, 25, null, false, null, rateFeedbackPanel);

        // Ratings  like textarea
        JTextArea ratingLikeTextArea = new JTextArea();
        ratingLikeTextArea.setLineWrap(true);
        ratingLikeTextArea.setWrapStyleWord(true);
        ratingLikeTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingLikeTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Allows scrolling for rating like text area
        JScrollPane ratingLikeTextAreaScroll = new JScrollPane(ratingLikeTextArea);
        ratingLikeTextAreaScroll.setBounds(50, 210, 480, 70);
        ratingLikeTextAreaScroll.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        rateFeedbackPanel.add(ratingLikeTextAreaScroll);

        // Ratings dislikes label
        JLabel ratingDislikeLabel = createLabel("What could be improved?", new Font("Arial", Font.BOLD, 14),
                new Color(33, 37, 41), SwingConstants.LEFT, 50, 300, 250, 25, null, false, null, rateFeedbackPanel);

        // Ratings dislike textArea
        JTextArea ratingDislikeTextArea = new JTextArea();
        ratingDislikeTextArea.setLineWrap(true);
        ratingDislikeTextArea.setWrapStyleWord(true);
        ratingDislikeTextArea.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingDislikeTextArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Allows scrolling for rating dislike textarea
        JScrollPane ratingDislikeTextAreaScroll = new JScrollPane(ratingDislikeTextArea);
        ratingDislikeTextAreaScroll.setBounds(50, 330, 480, 70);
        ratingDislikeTextAreaScroll.setBorder(BorderFactory.createLineBorder(new Color(206, 212, 218), 1));
        rateFeedbackPanel.add(ratingDislikeTextAreaScroll);

        // Submit Button
        JButton submitButton = new JButton("Submit Feedback");
        submitButton.setBounds(390, 420, 140, 35);
        submitButton.setFont(new Font("Arial", Font.BOLD, 13));
        submitButton.setBackground(new Color(40, 167, 69));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBorderPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rateFeedbackPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            if (currentRating[0] == 0) {
                JOptionPane.showMessageDialog(rateFeedbackPanel, "Please select a rating!");
                return;
            }
            String likeText = ratingLikeTextArea.getText().trim();
            String dislikeText = ratingDislikeTextArea.getText().trim();
            if (likeText.isEmpty() || dislikeText.isEmpty()) {
                JOptionPane.showMessageDialog(rateFeedbackPanel, "Please fill out both feedback fields!");
                return;
            }
            String EFeedbackSql = "INSERT INTO Efeedback (userEmail,name, rating, likes, dislikes) VALUES (?, ?, ?, ?)";
            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement pst = con.prepareStatement(EFeedbackSql)) {
                pst.setString(1, empEmail);
                pst.setString(2,empName);
                pst.setInt(3, currentRating[0]);
                pst.setString(4, likeText);
                pst.setString(5, dislikeText);
                String activitiesSql = "INSERT INTO activities (empEmail, activity, user) VALUES (?,?,?)";
                try(PreparedStatement pst6 = con.prepareStatement(activitiesSql)){
                    pst6.setString(1, empEmail);
                    pst6.setString(2, empName + " has rated " + currentRating[0] + " stars for the app.");
                    pst6.setString(3, "Employer");
                    pst6.executeUpdate();
                }
                pst.executeUpdate();
                JOptionPane.showMessageDialog(rateFeedbackPanel, "Thank you for your feedback!");
                ratingLikeTextArea.setText("");
                ratingDislikeTextArea.setText("");
                currentRating[0] = 0;
                updateStars(stars, 0);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(rateFeedbackPanel, "Error saving feedback to database!");
            }
        });

        employerPanel.add(rateFeedbackPanel, "RateFeedback");

        rateFeedbackButton.addActionListener(e -> employerCardLayout.show(employerPanel, "RateFeedback"));

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int interviewTotal = 0;
        int interviewToday = 0;
        String today = LocalDate.now().toString();

        try {
            if (conn != null) {
                // Get total interview count
                String totalQuery = "SELECT COUNT(*) FROM interviewschedule";
                PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
                ResultSet totalRs = totalStmt.executeQuery();
                if (totalRs.next()) {
                    interviewTotal = totalRs.getInt(1);
                }

                // Get today's interview count
                String todayQuery = "SELECT COUNT(*) FROM interviewschedule WHERE DATE(created_at) = ?";
                PreparedStatement todayStmt = conn.prepareStatement(todayQuery);
                todayStmt.setString(1, today);
                ResultSet todayRs = todayStmt.executeQuery();
                if (todayRs.next()) {
                    interviewToday = todayRs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int offerTotal = 0;
        int offerToday = 0;

        try {
            if (conn != null) {
                // Get total offer count
                String totalQuery = "SELECT COUNT(*) FROM offerletters";
                PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
                ResultSet totalRs = totalStmt.executeQuery();
                if (totalRs.next()) {
                    offerTotal = totalRs.getInt(1);
                }

                // Get today's offer count
                String todayQuery = "SELECT COUNT(*) FROM offerletters WHERE DATE(created_at) = ?";
                PreparedStatement todayStmt = conn.prepareStatement(todayQuery);
                todayStmt.setString(1, today);
                ResultSet todayRs = todayStmt.executeQuery();
                if (todayRs.next()) {
                    offerToday = todayRs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Interview schedule panel
        JPanel interviewScheduledPanel = new JPanel();
        interviewScheduledPanel.setLayout(new GridLayout(2, 1, 2, 2));
        employerContainer.add(interviewScheduledPanel);

        // Total interview scheduled label
        JLabel totalInterviewScheduledLabel = createLabel("Interviews: " + interviewTotal,
                new Font("Arial", Font.BOLD, 11), Color.WHITE, SwingConstants.CENTER, 0, 0, 0, 0,
                null, false, null, interviewScheduledPanel);

        // Today's interview scheduled label
        JLabel interviewScheduledTodayLabel = createLabel("Today: " + interviewToday, new Font("Arial", Font.PLAIN, 10),
                Color.LIGHT_GRAY, SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, interviewScheduledPanel);

        // Offer letters scheduled panel
        JPanel scheduledOfferLettersPanel = new JPanel();
        scheduledOfferLettersPanel.setLayout(new GridLayout(2, 1, 2, 2));
        employerContainer.add(scheduledOfferLettersPanel);

        // Total offer letters scheduled label
        JLabel totalScheduledOfferLetter = createLabel("Offers: " + offerTotal, new Font("Arial", Font.BOLD, 11),
                Color.WHITE, SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, scheduledOfferLettersPanel);

        // Today's offer letter scheduled label
        JLabel offerLetterScheduledTodayLabel = createLabel("Today: " + offerToday, new Font("Arial", Font.PLAIN, 10),
                Color.LIGHT_GRAY, SwingConstants.CENTER, 0, 0, 0, 0, null, false, null, scheduledOfferLettersPanel);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(180, 30, 50), 1, true));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setToolTipText("Sign Out");
        logoutButton.setFont(buttonFont);
        logoutButton.setBounds(80, 525, 100, 30);
        employerContainer.add(logoutButton);


        logoutButton.addActionListener(a -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                new LandingPage();
                dispose();
            }
        });

        // Welcome message
        JLabel welcomeMessage = createLabel("Welcome " + empName, headingFont, null, SwingConstants.CENTER,
                200, 5, 500, 40, null, false, null, employerContainer);

        employerContainer.setComponentZOrder(redDotLabel, 0);


        // Frame settings
        setVisible(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Employer Dashboard");
    }
    public static void main(String [] args){
        String defaultName = "CompanyX";
        String defaultEmail = "companyx@gmail.com";
        EmployerDashboard object13 = new EmployerDashboard("Employer",defaultName,defaultEmail,"default");
    }
}