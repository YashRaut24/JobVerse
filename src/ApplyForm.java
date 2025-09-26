import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;

class ApplyForm extends JFrame {

    private JTextField emailField, phoneField;
    private JTextArea reasonArea;
    private JLabel fileLabel;
    private File selectedFile;
    private final String companyName, location, jobType, skills, salary, postedDate, employerEmail;
    private final int jobId;


    // Labels styling
    private JLabel createLabel(String text, Font font, int x, int y, int w, int h, Container c) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setBounds(x, y, w, h);
        c.add(lbl);
        return lbl;
    }

    // TextFields styling
    private JTextField createTextField(String text, Font font, int x, int y, int w, int h, Container c) {
        JTextField tf = new JTextField(text);
        tf.setFont(font);
        tf.setBounds(x, y, w, h);
        c.add(tf);
        return tf;
    }

    // Buttons styling
    private JButton createButton(String text, Font font, int x, int y, int w, int h, Container c) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setBounds(x, y, w, h);
        btn.setFocusPainted(false);
        c.add(btn);
        return btn;
    }

    // Adds pdf from device
    private void chooseResume() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            fileLabel.setText(selectedFile.getName());
        }
    }

    // Handles the submission of application form
    private void handleApply() {
        // Gets value from TextFields
        String email = emailField.getText();
        String phone = phoneField.getText();
        String reason = reasonArea.getText();

        // If any TextField is empty it displays this message
        if (phone.isEmpty() || reason.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and upload a resume.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If we select any other files than pdf it displays this message
        if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
            JOptionPane.showMessageDialog(this, "Only PDF files are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        String appliedJobsSql = "INSERT INTO appliedjobs " +
                "(UserEmail, CompanyName, Location, JobType, SkillsRequired, Salary, PostedDate, currentDate, Note, Reason, ResumePDF, FileName, id, employerEmail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            boolean insertSuccess = false;

            try (PreparedStatement pst = con.prepareStatement(appliedJobsSql);
                 FileInputStream fis = new FileInputStream(selectedFile)) {

                pst.setString(1, email);
                pst.setString(2, companyName);
                pst.setString(3, location);
                pst.setString(4, jobType);
                pst.setString(5, skills);
                pst.setBigDecimal(6, new java.math.BigDecimal(salary));
                pst.setString(7, postedDate);
                pst.setDate(8, Date.valueOf(LocalDate.now()));
                pst.setString(9, phone);
                pst.setString(10, reason);
                pst.setBinaryStream(11, fis, (int) selectedFile.length());
                pst.setString(12, selectedFile.getName());
                pst.setInt(13, jobId);
                pst.setString(14, employerEmail);

                int rows = pst.executeUpdate();
                if (rows > 0) {
                    insertSuccess = true;

                    // Add activity log
                    String activityQuery = "INSERT INTO activities (empEmail, activity, user) VALUES (?, ?, ?)";
                    try (PreparedStatement pst2 = con.prepareStatement(activityQuery)) {
                        pst2.setString(1, email);
                        pst2.setString(2, email + " applied for " + companyName);
                        pst2.setString(3, "JobSeeker");
                        pst2.executeUpdate();
                    }

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    });
                }
            }

            if (!insertSuccess) {
                JOptionPane.showMessageDialog(null, "Submission failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    // Constructor
    ApplyForm(String userEmail, String companyName, String location, String jobType, String skills, String salary, String postedDate, int jobId, String employerEmail) {

        this.companyName = companyName;
        this.location = location;
        this.jobType = jobType;
        this.skills = skills;
        this.salary = salary;
        this.postedDate = postedDate;
        this.jobId = jobId;
        this.employerEmail = employerEmail;

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);

        // Form panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBounds(0, 0, 420, 520);
        formPanel.setBackground(Color.WHITE);

        // Email label
        createLabel("Email:", labelFont, 20, 20, 150, 20, formPanel);

        // Email TextField
        emailField = createTextField(userEmail, fieldFont, 20, 42, 310, 30, formPanel);
        emailField.setEditable(false);
        emailField.setBackground(new Color(230, 230, 230));

        // Phone label
        createLabel("Phone:", labelFont, 20, 85, 150, 20, formPanel);

        // Phone TextField
        phoneField = createTextField("", fieldFont, 20, 107, 310, 30, formPanel);

        // Resume upload label
        createLabel("Upload Resume:", labelFont, 20, 150, 200, 20, formPanel);

        // Upload button
        JButton uploadButton = createButton("Choose PDF", fieldFont, 20, 172, 150, 30, formPanel);

        // File label
        fileLabel = createLabel("No file chosen", new Font("Segoe UI", Font.ITALIC, 12), 180, 172, 200, 30, formPanel);

        uploadButton.addActionListener(e -> chooseResume());

        // Reason label
        createLabel("Why should we hire you?", labelFont, 20, 215, 310, 20, formPanel);

        // Reason textarea
        reasonArea = new JTextArea();
        reasonArea.setFont(fieldFont);
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);

        // Allows scrolling
        JScrollPane scrollPane = new JScrollPane(reasonArea);
        scrollPane.setBounds(20, 237, 310, 120);
        formPanel.add(scrollPane);

        // Apply button
        JButton applyButton = createButton("Apply Now", new Font("Segoe UI", Font.BOLD, 14), 20, 380, 310, 40, formPanel);
        applyButton.setBackground(new Color(70, 130, 180));
        applyButton.setForeground(Color.WHITE);

        applyButton.addActionListener(e -> handleApply());

        add(formPanel);

        // Frame settings
        setTitle("Apply for Job");
        setSize(365, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        ApplyForm object11 = new ApplyForm("john@example.com", "TechCorp", "Pune","Full-Time", "Java, SQL", "70000.00", "2025-08-06", 1, "emailemployer");
    }
}
