import javax.swing.*;
import java.awt.*;
import java.sql.*;

class Contact extends JFrame {

    // Database credentials
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String passwords = System.getenv("DB_PASS");

    Contact() {

        String Contact = "Contact";

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(44, 62, 80);

        // Title label
        JLabel contactTitleLabel = new JLabel("Contact Us", SwingConstants.CENTER);
        contactTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        contactTitleLabel.setBounds(0, 20, 650, 40); // adjusted width to 650
        add(contactTitleLabel);

        // Contact info TextArea
        JTextArea contactInfoTextArea = new JTextArea(
                "We'd love to hear from you!\n\n" +
                        "📧 Email: support@jobverse.com\n" +
                        "📞 Phone: +91 9876543210\n" +
                        "📍 Address: 123, Tech Street, Pune, India\n\n" +
                        "For queries related to job applications, employer registration, or technical issues, feel free to reach out."
        );
        contactInfoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        contactInfoTextArea.setEditable(false);
        contactInfoTextArea.setLineWrap(true);
        contactInfoTextArea.setWrapStyleWord(true);
        contactInfoTextArea.setBackground(new Color(245, 248, 255));
        contactInfoTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        contactInfoTextArea.setBounds(30, 80, 590, 170);
        add(contactInfoTextArea);

        // Form Title label
        JLabel formTitleLabel = new JLabel("Send us a Message");
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        formTitleLabel.setBounds(30, 260, 300, 30);
        add(formTitleLabel);

        // Name label
        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(labelColor);
        nameLabel.setBounds(30, 310, 100, 25);
        add(nameLabel);

        // Name TextField
        JTextField nameTextField = new JTextField();
        nameTextField.setFont(labelFont);
        nameTextField.setBounds(150, 310, 250, 28);
        add(nameTextField);

        // Email label
        JLabel emailLabel = new JLabel("Your Email:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(labelColor);
        emailLabel.setBounds(30, 350, 100, 25);
        add(emailLabel);

        // Email TextField
        JTextField emailTextField = new JTextField();
        emailTextField.setFont(labelFont);
        emailTextField.setBounds(150, 350, 250, 28);
        add(emailTextField);

        // Message label
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(labelFont);
        messageLabel.setForeground(labelColor);
        messageLabel.setBounds(30, 390, 100, 25);
        add(messageLabel);

        // Message TextArea
        JTextArea messageTextArea = new JTextArea();
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setFont(labelFont);
        JScrollPane scrollPane = new JScrollPane(messageTextArea);
        scrollPane.setBounds(150, 390, 400, 120);
        add(scrollPane);

        // Submit Button
        JButton submitButton = new JButton("Send Message");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        submitButton.setBackground(new Color(23, 162, 184));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setBounds(370, 530, 180, 40);
        add(submitButton);

        submitButton.addActionListener(e -> {
            String name = nameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String message = messageTextArea.getText().trim();

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String insertQuery = "INSERT INTO queries (name, email, message) VALUES (?, ?, ?)";
                try (Connection con = DriverManager.getConnection(url, user, passwords);
                     PreparedStatement pst = con.prepareStatement(insertQuery)) {
                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, message);
                    int rows = pst.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(this, "Thank you! Your message has been sent.Please check mail for further updates");
                        nameTextField.setText("");
                        emailTextField.setText("");
                        messageTextArea.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "⚠ Failed to send message.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Frame settings
        setTitle("Contact JobVerse");
        setSize(650, 650);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        Contact object3 = new Contact();
    }
}
