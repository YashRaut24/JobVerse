import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.*;

class CompanyProfile extends JFrame {
    // UI Components
    JLabel logoLabel;
    JTextField companyNameTextfield, taglineTextfield, foundedTextfield, headquartersTextfield, industryTextfield, sizeTextfield, websiteTextfield, linkedinTextfield, twitterTextfield, instagramTextfield, openPositionsTextfield, hiringStatusTextfield,companyEmailTextfield;
    JTextArea aboutTextArea;
    JButton uploadLogoButton, saveButton, backButton;

    // Data handling
    File selectedLogoFile = null;
    boolean isEmployer;
    String companyEmail;
    String currentLogoFileName = null;
    Blob currentLogoBlob = null;

    // Labels styling
    private JLabel createLabel(String labelString, Font font, int x, int y, int width, int height, Container container) {
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        container.add(label);
        return label;
    }

    // TextFields styling
    private JTextField createTextField(Font font, int x, int y, int width, int height, Container container) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBounds(x, y, width, height);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        container.add(textField);
        return textField;
    }

    // Button styling
    private JButton createButton(String text, Font font, int x, int y, int width, int height,
                                 Color bgColor, Color fgColor, Container container, boolean flat) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBounds(x, y, width, height);
        button.setForeground(fgColor);

        if (flat) {
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
        } else {
            button.setBackground(bgColor);
        }

        container.add(button);
        return button;
    }

    // Handles logo upload
    private void uploadLogo(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedLogoFile = chooser.getSelectedFile();
            try {
                ImageIcon icon = new ImageIcon(selectedLogoFile.getAbsolutePath());
                Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading image: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Loads company profile
    private void loadCompanyProfile(String companyEmail) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT companyEmail, companyName, tagline, foundedYear, headquarters, industry, companySize, " +
                    "website, linkedin, twitter, instagram, aboutCompany, logo, logoFileName " +
                    "FROM companyProfile WHERE companyEmail = ?";
            try(PreparedStatement ps = con.prepareStatement(query)){
                ps.setString(1, companyEmail);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    companyNameTextfield.setText(rs.getString("companyName"));
                    companyEmailTextfield.setText(rs.getString("companyEmail"));
                    taglineTextfield.setText(rs.getString("tagline"));
                    foundedTextfield.setText(rs.getString("foundedYear"));
                    headquartersTextfield.setText(rs.getString("headquarters"));
                    industryTextfield.setText(rs.getString("industry"));
                    sizeTextfield.setText(rs.getString("companySize"));
                    websiteTextfield.setText(rs.getString("website"));
                    linkedinTextfield.setText(rs.getString("linkedin"));
                    twitterTextfield.setText(rs.getString("twitter"));
                    instagramTextfield.setText(rs.getString("instagram"));
                    aboutTextArea.setText(rs.getString("aboutCompany"));

                    currentLogoFileName = rs.getString("logoFileName");
                    currentLogoBlob = rs.getBlob("logo");
                    if (currentLogoBlob != null) {
                        InputStream is = currentLogoBlob.getBinaryStream();
                        ImageIcon icon = new ImageIcon(is.readAllBytes());
                        Image scaled = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                        logoLabel.setIcon(new ImageIcon(scaled));
                    }
                }

                // Load hiring information
                String hiringQuery = "SELECT openPositions, hiringStatus FROM hirings WHERE companyEmail = ?";
                PreparedStatement ps2 = con.prepareStatement(hiringQuery);
                ps2.setString(1, companyEmail);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    openPositionsTextfield.setText(String.valueOf(rs2.getInt("openPositions")));
                    hiringStatusTextfield.setText(rs2.getString("hiringStatus"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading company profile: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // If employer edits the company profile it is saved by this method
    private void saveChanges() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");
        try {
            conn = DriverManager.getConnection(url, user, password);

            String checkSql = "SELECT COUNT(*) FROM companyProfile WHERE companyEmail=?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, companyEmail);
            rs = checkPs.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;
            rs.close();
            checkPs.close();

            String sql;
            if (exists) {
                sql = "UPDATE companyProfile SET companyName=?, tagline=?, foundedYear=?, headquarters=?, industry=?, " +
                        "companySize=?, website=?, linkedin=?, twitter=?, instagram=?, " +
                        "aboutCompany=?, logo=?, logoFileName=? WHERE companyEmail=?";
                ps = conn.prepareStatement(sql);

                setCompanyProfileParams(ps);
                ps.setString(14, companyEmail);

            } else {
                sql = "INSERT INTO companyProfile (companyName, tagline, foundedYear, headquarters, industry, " +
                        "companySize, website, linkedin, twitter, instagram, aboutCompany, logo, logoFileName, companyEmail) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);

                setCompanyProfileParams(ps);
                ps.setString(14, companyEmailTextfield.getText().trim());
            }

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, exists ? "Profile updated successfully!" : "Profile created successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Save failed. Please try again later.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving profile: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignore) {}
            try { if (ps != null) ps.close(); } catch (Exception ignore) {}
            try { if (conn != null) conn.close(); } catch (Exception ignore) {}
        }
    }


    private void setCompanyProfileParams(PreparedStatement ps) throws Exception {
        ps.setString(1, companyNameTextfield.getText());
        ps.setString(2, taglineTextfield.getText());

        String yearText = foundedTextfield.getText().trim();
        if (!yearText.isEmpty()) {
            try {
                ps.setInt(3, Integer.parseInt(yearText));
            } catch (NumberFormatException ex) {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
        } else {
            ps.setNull(3, java.sql.Types.INTEGER);
        }

        ps.setString(4, headquartersTextfield.getText());
        ps.setString(5, industryTextfield.getText());
        ps.setString(6, sizeTextfield.getText());
        ps.setString(7, websiteTextfield.getText());
        ps.setString(8, linkedinTextfield.getText());
        ps.setString(9, twitterTextfield.getText());
        ps.setString(10, instagramTextfield.getText());
        ps.setString(11, aboutTextArea.getText());

        if (selectedLogoFile != null) {
            FileInputStream fis = new FileInputStream(selectedLogoFile);
            ps.setBinaryStream(12, fis, (int) selectedLogoFile.length());
            ps.setString(13, selectedLogoFile.getName());
        } else if (currentLogoBlob != null && currentLogoFileName != null) {
            ps.setBlob(12, currentLogoBlob);
            ps.setString(13, currentLogoFileName);
        } else {
            ps.setNull(12, java.sql.Types.BLOB);
            ps.setNull(13, java.sql.Types.VARCHAR);
        }

    }

    // Constructor
    CompanyProfile(String companyEmail, boolean isEmployer, String adminEmail, boolean isAdmin) {
        this.isEmployer = isEmployer;
        this.companyEmail = companyEmail;

        // Fonts
        Font titleFont = new Font("SansSerif", Font.BOLD, 24);
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 13);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);
        Font linkFont = new Font("SansSerif", Font.PLAIN, 12);
        Font smallFont = new Font("SansSerif", Font.PLAIN, 11);

        // Container for company profile
        Container companyProfileContainer = getContentPane();
        companyProfileContainer.setLayout(null);
        companyProfileContainer.setBackground(Color.WHITE);

        // Title label
        JLabel title = createLabel("Company Profile", titleFont, 250, 20, 200, 30, companyProfileContainer);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Logo label
        logoLabel = new JLabel();
        logoLabel.setBounds(320, 60, 80, 80);
        logoLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(233, 236, 239), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        companyProfileContainer.add(logoLabel);

        // Upload Logo Button (only for employer or admin)
        if (isEmployer || isAdmin) {
            uploadLogoButton = createButton("Upload Logo", linkFont, 410, 90, 100, 25,
                    null, new Color(73, 80, 87), companyProfileContainer, true);
            uploadLogoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            uploadLogoButton.setFocusPainted(false);
            uploadLogoButton.addActionListener(this::uploadLogo);
        }

        // Company Name label
        JLabel companyNameLabel = createLabel("Company Name:", labelFont, 50, 160, 150, 20, companyProfileContainer);

        // Company name TextField
        companyNameTextfield = createTextField(fieldFont, 50, 180, 180, 30, companyProfileContainer);
        companyNameTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) companyNameTextfield.setBackground(new Color(248, 249, 250));

        // Tagline label
        JLabel taglineLabel = createLabel("Tagline:", labelFont, 270, 160, 150, 20, companyProfileContainer);

        // Tagline TextField
        taglineTextfield = createTextField(fieldFont, 270, 180, 180, 30, companyProfileContainer);
        taglineTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) taglineTextfield.setBackground(new Color(248, 249, 250));

        // Founded label
        JLabel foundedLabel = createLabel("Founded:", labelFont, 490, 160, 150, 20, companyProfileContainer);

        // Founded TextField
        foundedTextfield = createTextField(fieldFont, 490, 180, 150, 30, companyProfileContainer);
        foundedTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) foundedTextfield.setBackground(new Color(248, 249, 250));

        // Industry label
        JLabel industryLabel = createLabel("Industry:", labelFont, 50, 230, 150, 20, companyProfileContainer);

        // Industry TextField
        industryTextfield = createTextField(fieldFont, 50, 250, 180, 30, companyProfileContainer);
        industryTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) industryTextfield.setBackground(new Color(248, 249, 250));

        // Headquarters label
        JLabel headquartersLabel = createLabel("Headquarters:", labelFont, 270, 230, 150, 20, companyProfileContainer);

        // Headquarters TextField
        headquartersTextfield = createTextField(fieldFont, 270, 250, 180, 30, companyProfileContainer);
        headquartersTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) headquartersTextfield.setBackground(new Color(248, 249, 250));

        // Company Email label
        JLabel emailLabel = createLabel("Company Email:", labelFont, 490, 230, 150, 20, companyProfileContainer);

        // Company Email TextField
        companyEmailTextfield = createTextField(fieldFont, 490, 250, 150, 30, companyProfileContainer);
        companyEmailTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) companyEmailTextfield.setBackground(new Color(248, 249, 250));

        // Company Size label
        JLabel sizeLabel = createLabel("Company Size:", labelFont, 490, 300, 150, 20, companyProfileContainer);

        // Company Size TextField
        sizeTextfield = createTextField(fieldFont, 490, 320, 150, 30, companyProfileContainer);
        sizeTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) sizeTextfield.setBackground(new Color(248, 249, 250));

        // Open positions label
        JLabel openPositionsLabel = createLabel("Open Positions:", labelFont, 50, 300, 150, 20, companyProfileContainer);

        // Open position TextField
        openPositionsTextfield = createTextField(fieldFont, 50, 320, 180, 30, companyProfileContainer);
        openPositionsTextfield.setEditable(false);
        openPositionsTextfield.setBackground(new Color(248, 249, 250));

        // Hiring status label
        JLabel hiringStatusLabel = createLabel("Hiring Status:", labelFont, 270, 300, 150, 20, companyProfileContainer);

        // Hiring status TextField
        hiringStatusTextfield = createTextField(fieldFont, 270, 320, 180, 30, companyProfileContainer);
        hiringStatusTextfield.setEditable(false);
        hiringStatusTextfield.setBackground(new Color(248, 249, 250));

        // Website label
        JLabel websiteLabel = createLabel("Website:", labelFont, 50, 370, 150, 20, companyProfileContainer);

        // Website TextField
        websiteTextfield = createTextField(fieldFont, 50, 390, 140, 30, companyProfileContainer);
        websiteTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) websiteTextfield.setBackground(new Color(248, 249, 250));

        // Linkedin label
        JLabel linkedinLabel = createLabel("LinkedIn:", labelFont, 210, 370, 150, 20, companyProfileContainer);

        // Linkedin TextField
        linkedinTextfield = createTextField(fieldFont, 210, 390, 140, 30, companyProfileContainer);
        linkedinTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) linkedinTextfield.setBackground(new Color(248, 249, 250));

        // Twitter label
        JLabel twitterLabel = createLabel("Twitter:", labelFont, 370, 370, 150, 20, companyProfileContainer);

        // Twitter TextField
        twitterTextfield = createTextField(fieldFont, 370, 390, 140, 30, companyProfileContainer);
        twitterTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) twitterTextfield.setBackground(new Color(248, 249, 250));

        // Instagram label
        JLabel instagramLabel = createLabel("Instagram:", labelFont, 530, 370, 150, 20, companyProfileContainer);

        // Instagram TextField
        instagramTextfield = createTextField(fieldFont, 530, 390, 110, 30, companyProfileContainer);
        instagramTextfield.setEditable(isEmployer || isAdmin);
        if (!(isEmployer || isAdmin)) instagramTextfield.setBackground(new Color(248, 249, 250));

        // About Label
        JLabel aboutLabel = createLabel("About Company:", labelFont, 50, 440, 150, 20, companyProfileContainer);

        // About textarea
        aboutTextArea = new JTextArea(4, 30);
        aboutTextArea.setFont(fieldFont);
        aboutTextArea.setLineWrap(true);
        aboutTextArea.setWrapStyleWord(true);
        aboutTextArea.setEditable(isEmployer || isAdmin);

        if (!(isEmployer || isAdmin)) {
            aboutTextArea.setBackground(new Color(248, 249, 250));
            aboutTextArea.setForeground(Color.DARK_GRAY);
        }

        aboutTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(233, 236, 239), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Allow scrolling for about TextArea
        JScrollPane scrollPane = new JScrollPane(aboutTextArea);
        scrollPane.setBounds(50, 460, 590, 80);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(233, 236, 239), 1));
        companyProfileContainer.add(scrollPane);

        // If it is employer save button is visible else nothing
        if (isEmployer || isAdmin) {
            saveButton = createButton("Save Changes", buttonFont, 200, 560, 150, 40,
                    new Color(73, 80, 87), Color.WHITE, companyProfileContainer, false);
            saveButton.setFocusPainted(false);
            saveButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            saveButton.addActionListener(e -> saveChanges());
        }

        // Back button
        backButton = createButton("Back", buttonFont, 370, 560, 140, 40,new Color(134, 142, 150), Color.WHITE, companyProfileContainer, false);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> dispose());

        // Footer label
        JLabel footer = createLabel("© 2025 JobVerse. All rights reserved", smallFont, 250, 620, 200, 25, companyProfileContainer);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setForeground(new Color(134, 142, 150));

        // Load existing data only for employer
        if (isEmployer || (!isEmployer && !isAdmin)) {
            loadCompanyProfile(companyEmail.trim());
        }

        // Frame settings
        setVisible(true);
        setTitle("Company Profile");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public static void main(String[] args) {
        CompanyProfile object14 = new CompanyProfile("employer@example.com", true, "",true);
    }
}