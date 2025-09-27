import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.*;

class JSProfile extends JFrame {

    // Creates labels
    private JLabel createLabel(String text, Font font, int x, int y, int width, int height, JPanel parent) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        parent.add(label);
        return label;
    }

    private JTextArea summaryDisplayTextArea, skillsDisplayTextArea;
    private JTextField headlineDisplayTextField, phoneDisplayTextField, locationDisplayTextField;
    private JButton saveButton, uploadPhotoButton;
    private JTable educationTable;
    private JLabel profilePicLabel;

    private ImageIcon profileImageIcon;
    private byte[] uploadedImageBytes;

    // Fetch and display profile data
    private void loadProfile(String email, JTextField fullNameTextField) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String passwords = System.getenv("DB_PASS");
        try (Connection con = DriverManager.getConnection(url, user, passwords)) {

            String query = "SELECT JSFullName, JSPhone, JSLocation, JSHeadline, JSSummary, JSSkills, JSDegree, JSInstitution, JSYear, JSProfileImage FROM userprofile WHERE JSEmail=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                fullNameTextField.setText(rs.getString("JSFullName"));
                phoneDisplayTextField.setText(rs.getString("JSPhone"));
                locationDisplayTextField.setText(rs.getString("JSLocation"));
                headlineDisplayTextField.setText(rs.getString("JSHeadline"));
                summaryDisplayTextArea.setText(rs.getString("JSSummary"));
                skillsDisplayTextArea.setText(rs.getString("JSSkills"));

                // Education table
                educationTable.setValueAt(rs.getString("JSDegree"), 0, 0);
                educationTable.setValueAt(rs.getString("JSInstitution"), 0, 1);
                educationTable.setValueAt(rs.getString("JSYear"), 0, 2);

                // Profile image
                byte[] imgBytes = rs.getBytes("JSProfileImage");
                if (imgBytes != null) {
                    ImageIcon imgIcon = new ImageIcon(
                            new ImageIcon(imgBytes).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                    profilePicLabel.setIcon(imgIcon);
                    profilePicLabel.setText("");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Constructor
    JSProfile(String emailToFetch, String role,String firstName,String lastName) {

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.BOLD, 12);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);
        Color fieldBorderColor = new Color(209, 213, 219);
        Color labelColor = new Color(55, 65, 81);

        // Profile panel
        JPanel profilePanel = new JPanel(null);
        profilePanel.setBackground(new Color(248, 250, 252));

        // Header panel
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(10, 10, 571, 80);
        headerPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        // Heading label
        JLabel headingLabel = createLabel("Job Seeker Profile", new Font("Segoe UI", Font.BOLD, 24), 15, 20, 300, 35, headerPanel);
        headingLabel.setForeground(Color.BLACK);
        createLabel("Professional profile information", new Font("Segoe UI", Font.PLAIN, 12), 15, 45, 300, 20, headerPanel)
                .setForeground(Color.BLACK);
        profilePanel.add(headerPanel);

        // Profile Info Panel
        JPanel profileInfoPanel = new JPanel(null);
        profileInfoPanel.setBackground(Color.WHITE);
        profileInfoPanel.setBounds(10, 100, 571, 120);
        profileInfoPanel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 1));

        // Profile Pic label
        profilePicLabel = new JLabel("📷", SwingConstants.CENTER);
        profilePicLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        profilePicLabel.setForeground(new Color(156, 163, 175));
        profilePicLabel.setBounds(10, 10, 80, 80);
        profilePicLabel.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235), 2));
        profileInfoPanel.add(profilePicLabel);

        // Upload photo button (only for jobseeker)
        uploadPhotoButton = new JButton("Upload Photo");
        uploadPhotoButton.setBounds(10, 95, 120, 20);
        profileInfoPanel.add(uploadPhotoButton);

        // Full Name label
        createLabel("Full Name", labelFont, 175, 15, 80, 20, profileInfoPanel).setForeground(labelColor);

        // Full name TextField
        JTextField fullNameTextField = new JTextField();
        fullNameTextField.setFont(fieldFont);
        fullNameTextField.setBounds(175, 35, 180, 25);
        fullNameTextField.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        profileInfoPanel.add(fullNameTextField);

        // Email label (view only)
        createLabel("Email", labelFont, 400, 15, 60, 20, profileInfoPanel).setForeground(labelColor);

        // Email display label
        JLabel emailDisplayLabel = new JLabel(emailToFetch);
        emailDisplayLabel.setFont(fieldFont);
        emailDisplayLabel.setBounds(400, 35, 150, 20);
        emailDisplayLabel.setForeground(new Color(107, 114, 128));
        profileInfoPanel.add(emailDisplayLabel);

        // Phone label
        createLabel("Phone", labelFont, 175, 60, 80, 20, profileInfoPanel).setForeground(labelColor);

        // Phone display TextField
        phoneDisplayTextField = new JTextField();
        phoneDisplayTextField.setFont(fieldFont);
        phoneDisplayTextField.setBounds(175, 80, 180, 25);
        phoneDisplayTextField.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        profileInfoPanel.add(phoneDisplayTextField);

        // Location label
        createLabel("Location", labelFont, 400, 60, 80, 20, profileInfoPanel).setForeground(labelColor);

        // Location display TextField
        locationDisplayTextField = new JTextField();
        locationDisplayTextField.setFont(fieldFont);
        locationDisplayTextField.setBounds(400, 80, 130, 25);
        locationDisplayTextField.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        profileInfoPanel.add(locationDisplayTextField);

        profilePanel.add(profileInfoPanel);

        // Profile tabs
        JTabbedPane profileTabs = new JTabbedPane(JTabbedPane.TOP);
        profileTabs.setBounds(10, 230, 571, 200);
        profileTabs.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Summary Tab panel
        JPanel summaryTabPanel = new JPanel(null);
        summaryTabPanel.setBackground(Color.WHITE);

        // Headline label
        createLabel("Headline", labelFont, 15, 15, 150, 20, summaryTabPanel).setForeground(labelColor);

        // Headline display TextField
        headlineDisplayTextField = new JTextField();
        headlineDisplayTextField.setFont(fieldFont);
        headlineDisplayTextField.setBounds(15, 35, 520, 25);
        headlineDisplayTextField.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        summaryTabPanel.add(headlineDisplayTextField);

        // Summary label
        createLabel("Summary", labelFont, 15, 70, 150, 20, summaryTabPanel).setForeground(labelColor);

        // Summary display TextArea
        summaryDisplayTextArea = new JTextArea();
        summaryDisplayTextArea.setFont(fieldFont);
        summaryDisplayTextArea.setLineWrap(true);
        summaryDisplayTextArea.setWrapStyleWord(true);

        // Allows scrolling for summary display panel
        JScrollPane summaryScroll = new JScrollPane(summaryDisplayTextArea);
        summaryScroll.setBounds(15, 90, 520, 60);
        summaryScroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        summaryTabPanel.add(summaryScroll);

        // Education Tab
        JPanel educationTabPanel = new JPanel(new BorderLayout());
        educationTabPanel.setBackground(Color.WHITE);
        Object[][] eduData = {{"", "", ""}};
        educationTable = new JTable(eduData, new String[]{"Degree", "Institution", "Year"});
        educationTable.setFont(fieldFont);
        educationTable.setRowHeight(30);
        educationTable.getTableHeader().setFont(labelFont);
        educationTabPanel.add(new JScrollPane(educationTable), BorderLayout.CENTER);

        // Skills Tab
        JPanel skillsTab = new JPanel(null);
        skillsTab.setBackground(Color.WHITE);
        createLabel("Technical Skills", labelFont, 15, 15, 200, 20, skillsTab).setForeground(labelColor);

        // Skills display TextArea
        skillsDisplayTextArea = new JTextArea();
        skillsDisplayTextArea.setFont(fieldFont);
        skillsDisplayTextArea.setLineWrap(true);

        // Allows scrolling for skills display TextArea
        JScrollPane skillsScroll = new JScrollPane(skillsDisplayTextArea);
        skillsScroll.setBounds(15, 35, 520, 110);
        skillsScroll.setBorder(BorderFactory.createLineBorder(fieldBorderColor, 1));
        skillsTab.add(skillsScroll);

        profileTabs.addTab("Summary", summaryTabPanel);
        profileTabs.addTab("Education", educationTabPanel);
        profileTabs.addTab("Skills", skillsTab);

        profilePanel.add(profileTabs);

        // Save Button
        saveButton = new JButton("Save");
        saveButton.setBounds(480, 440, 100, 30);
        saveButton.setBackground(new Color(37, 99, 235));
        saveButton.setForeground(Color.WHITE);
        profilePanel.add(saveButton);

        if (role.equalsIgnoreCase("employer")) {
            loadProfile(emailToFetch, fullNameTextField);
        }

        // Role-based control
        if (role.equalsIgnoreCase("employer")) {
            // View-only mode
            fullNameTextField.setEditable(false);
            phoneDisplayTextField.setEditable(false);
            locationDisplayTextField.setEditable(false);
            headlineDisplayTextField.setEditable(false);
            summaryDisplayTextArea.setEditable(false);
            skillsDisplayTextArea.setEditable(false);
            educationTable.setEnabled(false);
            uploadPhotoButton.setVisible(false);
            saveButton.setVisible(false);
        } else {
            // Jobseeker mode
            uploadPhotoButton.addActionListener(e -> chooseAndUploadPhoto(emailToFetch));
            saveButton.addActionListener(e -> {
                insertProfile(emailToFetch, fullNameTextField.getText());
                JOptionPane.showMessageDialog(this, "Profile created successfully!");
                new JobSeekerDashboard(firstName,lastName,emailToFetch);
                dispose();
            });
        }

        add(profilePanel);
        setTitle("Job Seeker Profile");
        setSize(600, 520);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Allows uploading profile photo
    private void chooseAndUploadPhoto(String email) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Profile Picture");
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                uploadedImageBytes = java.nio.file.Files.readAllBytes(selectedFile.toPath());
                ImageIcon newIcon = new ImageIcon(new ImageIcon(uploadedImageBytes).getImage()
                        .getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                profilePicLabel.setIcon(newIcon);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Allows storing profile information in db
    private void insertProfile(String email, String fullName) {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String passwords = System.getenv("DB_PASS");
        try (Connection con = DriverManager.getConnection(url, user, passwords)) {

            // Force JTable to commit any edits
            if (educationTable.isEditing()) {
                educationTable.getCellEditor().stopCellEditing();
            }

            String insert = "INSERT INTO userprofile " +
                    "(JSEmail, JSFullName, JSPhone, JSLocation, JSHeadline, JSSummary, JSSkills, JSDegree, JSInstitution, JSYear, JSProfileImage) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(insert);
            pst.setString(1, email);
            pst.setString(2, fullName);
            pst.setString(3, phoneDisplayTextField.getText());
            pst.setString(4, locationDisplayTextField.getText());
            pst.setString(5, headlineDisplayTextField.getText());
            pst.setString(6, summaryDisplayTextArea.getText());
            pst.setString(7, skillsDisplayTextArea.getText());
            pst.setString(8, (String) educationTable.getValueAt(0, 0));  // Degree
            pst.setString(9, (String) educationTable.getValueAt(0, 1));  // Institution

            String yearValue = (String) educationTable.getValueAt(0, 2);
            if (yearValue == null || yearValue.trim().isEmpty()) {
                yearValue = "";
            }
            pst.setString(10, yearValue);

            pst.setBytes(11, uploadedImageBytes);
            pst.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JSProfile object10 = new JSProfile("newuser@example.com", "jobseeker","","");
    }
}
