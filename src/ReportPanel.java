import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;

public class ReportPanel extends JFrame {
    private JTextField reasonField;
    private JTextArea descriptionArea;
    private final int jobID;
    private final String reportedBy;
    private final String reportedAgainst;
    private final String reportedRole;
    private  final String reporter;

    // Handles report submission
    private void submitReport(ActionEvent e) {
        String reason = reasonField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Displays an message if TextField and textarea input is empty
        if (reason.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out both reason and description.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            String sql ="INSERT INTO reports (jobID,reportedBy, reportedAgainst, reportedRole, reason, description) VALUES (?, ?, ?, ?, ?, ?)";
            if (jobID > 0) {
                try(PreparedStatement pst = con.prepareStatement(sql)){
                    pst.setInt(1,jobID);
                    pst.setString(2, reportedBy);
                    pst.setString(3, reportedAgainst);
                    pst.setString(4, reportedRole);
                    pst.setString(5, reason);
                    pst.setString(6, description);
                    String activites = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                    try(PreparedStatement pst6 = con.prepareStatement(activites)){
                        pst6.setString(1,reportedBy);
                        pst6.setString(2, reportedBy + " has reported " + reportedAgainst);
                        pst6.setString(3,reporter);
                        pst6.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(null,"Reported Successfully!");
                    pst.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Constructor
    ReportPanel(int id, String reportedBy, String reportedAgainst, String reportedRole,String reporter) {

        this.jobID = id;
        this.reportedBy = reportedBy;
        this.reportedAgainst = reportedAgainst;
        this.reportedRole = reportedRole;
        this.reporter = reporter;

        // Fonts
        Font labelFont = new Font("Calibri", Font.PLAIN, 14);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 14);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);

        // Report panel container
        Container reportPanelContainer = getContentPane();
        reportPanelContainer.setLayout(null);
        reportPanelContainer.setBackground(Color.WHITE);

        // Report panel
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(null);
        reportPanel.setBounds(50, 50, 400, 300);
        reportPanel.setBackground(new Color(240, 240, 240));
        reportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        reportPanelContainer.add(reportPanel);

        // Reason label
        JLabel reasonLabel = new JLabel("Report Reason:");
        reasonLabel.setFont(labelFont);
        reasonLabel.setBounds(20, 20, 200, 25);
        reportPanel.add(reasonLabel);

        // Reason TExtField
        reasonField = new JTextField();
        reasonField.setFont(fieldFont);
        reasonField.setBounds(20, 45, 350, 30);
        reportPanel.add(reasonField);

        // Description label
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(labelFont);
        descriptionLabel.setBounds(20, 90, 200, 25);
        reportPanel.add(descriptionLabel);

        // Description textarea
        descriptionArea = new JTextArea();
        descriptionArea.setFont(fieldFont);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        // Allows scrolling to description textarea
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBounds(20, 115, 350, 100);
        reportPanel.add(scrollPane);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(buttonFont);
        submitButton.setBounds(280, 230, 90, 30);
        submitButton.setBackground(new Color(0, 120, 215));
        submitButton.setForeground(Color.WHITE);
        reportPanel.add(submitButton);

        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(buttonFont);
        cancelButton.setBounds(180, 230, 90, 30);
        cancelButton.setBackground(new Color(200, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        reportPanel.add(cancelButton);

        submitButton.addActionListener(this::submitReport);
        cancelButton.addActionListener(e -> dispose());

        // Frame settings
        setSize(500, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Report Panel");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
            ReportPanel object16 = new ReportPanel(0, "test@example.com", "test2@example.com", "jobseeker","employer");
    }
}