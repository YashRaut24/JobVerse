import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class LoginPage extends JFrame {

    // Labels styling
    private JLabel createLabel(String labelString, Font font, int x, int y, int width, int height, Container container) {
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        container.add(label);
        return label;
    }

    // TextFields styling
    private JTextField createTextField(Font font, int x, int y, int width, int height, JPanel panel) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBounds(x, y, width, height);
        panel.add(textField);
        return textField;
    }

    // PassFields styling
    private JPasswordField createPassField(Font font, int x, int y, int width, int height, JPanel panel) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(x, y, width, height);
        panel.add(passwordField);
        return passwordField;
    }

    // Button styling
    private JButton createButton(String text, Font font, int x, int y, int width, int height, Color bgColor, Color fgColor, Container container, boolean flat) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBounds(x, y, width, height);
        button.setForeground(fgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (flat) {
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
        } else {
            button.setBackground(bgColor);
        }
        container.add(button);
        return button;
    }

    // Constructor
    LoginPage() {

        // Database credentials
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String passwords = System.getenv("DB_PASS");

        // Fonts
        Font headingFont = new Font("Futura", Font.BOLD, 28);
        Font labelFont = new Font("Calibri", Font.PLAIN, 14);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 14);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font smallFont = new Font("Calibri", Font.PLAIN, 12);

        // Login page container
        Container loginPageContainer = getContentPane();
        loginPageContainer.setLayout(null);
        loginPageContainer.setBackground(Color.WHITE);

        // Heading label
        JLabel heading = createLabel("Login to Jobverse", headingFont, 100, 20, 300, 40, loginPageContainer);
        heading.setHorizontalAlignment(SwingConstants.CENTER);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBounds(75, 80, 350, 280);
        loginPanel.setBackground(new Color(245, 245, 245));
        loginPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));
        loginPageContainer.add(loginPanel);

        // Email label
        JLabel emailLabel = createLabel("Email:", labelFont, 20, 20, 310, 20, loginPanel);

        // Email TextField for input
        JTextField emailTextField = createTextField(fieldFont, 20, 42, 310, 30, loginPanel);

        // Password label
        JLabel passwordLabel = createLabel("Password:", labelFont, 20, 82, 310, 20, loginPanel);

        // Password PassField for input
        JPasswordField passwordField = createPassField(fieldFont, 20, 104, 310, 30, loginPanel);

        // Show Password Checkbox
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(smallFont);
        showPassword.setBounds(20, 140, 120, 20);
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });
        loginPanel.add(showPassword);

        // Login button
        JButton loginButton = createButton("Login", buttonFont, 190, 205, 140, 35, new Color(0, 123, 255), Color.WHITE, loginPanel, false);

        loginButton.addActionListener(a -> {
            String email = emailTextField.getText();
            String password = new String(passwordField.getPassword());
            try (Connection con = DriverManager.getConnection(url, user, passwords)) {
                boolean loginSuccess = false;
                // Jobseeker login check
                String jobseekerSql = "SELECT * FROM jobseekers WHERE email=? AND password=?";
                try (PreparedStatement pst = con.prepareStatement(jobseekerSql)) {
                    pst.setString(1, email);
                    pst.setString(2, password);
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        String status = rs.getString("status");
                        // Checks status, if banned or not
                        if ("banned".equalsIgnoreCase(status)) {
                            JOptionPane.showMessageDialog(null, "Your JobVerse account has been banned.");
                        } else {
                            String firstName = rs.getString("firstname");
                            String lastName = rs.getString("lastname");
                            loginSuccess = true;
                            String activites = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                            try (PreparedStatement pst6 = con.prepareStatement(activites)) {
                                pst6.setString(1, email);
                                pst6.setString(2, firstName + " " + lastName + "(" + email + ")" + " has logged into their Jobseeker account!");
                                pst6.setString(3, "JobSeeker");
                                pst6.executeUpdate();
                            }
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(null, "Login Successful as Job Seeker");
                                new JobSeekerDashboard(firstName, lastName, email);
                                dispose();
                            });
                        }
                    }
                }
                // Employer login check
                if (!loginSuccess) {
                    String employerSql = "SELECT * FROM employers WHERE employerEmail=? AND password=?";
                    try (PreparedStatement pst = con.prepareStatement(employerSql)) {
                        pst.setString(1, email);
                        pst.setString(2, password);
                        ResultSet rs = pst.executeQuery();
                        if (rs.next()) {
                            String companyEmail = rs.getString("companyEmail");
                            String statusSql = "SELECT status FROM employers WHERE employerEmail=?";
                            try (PreparedStatement pst1 = con.prepareStatement(statusSql)) {
                                pst1.setString(1, email);
                                ResultSet rs1 = pst1.executeQuery();
                                // Checks status, if banned or not
                                if (rs1.next() && "banned".equalsIgnoreCase(rs1.getString("status"))) {
                                    JOptionPane.showMessageDialog(null, "Your Employer account has been banned.");
                                } else {
                                    String empName = rs.getString("fullname");
                                    String companyName = rs.getString("companyname");
                                    loginSuccess = true;
                                    String activites = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                                    try (PreparedStatement pst6 = con.prepareStatement(activites)) {
                                        pst6.setString(1, email);
                                        pst6.setString(2, empName + " " + "(" + email + ")" + " has logged into their Jobseeker account!");
                                        pst6.setString(3, "Employer");
                                        pst6.executeUpdate();
                                    }
                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(null, "Login Successful as Employer");
                                        new EmployerDashboard(empName, companyName, email,companyEmail);
                                        dispose();
                                    });
                                }
                            }
                        }
                    }
                }
                // If neither Jobseeker nor Employer login succeeded
                if (!loginSuccess) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid login credentials. Please check your email and password.",
                            "Login Failed",
                            JOptionPane.WARNING_MESSAGE
                    );
                }

            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()));
            }
        });

        // Sign Up if no account label
        JLabel signUpLabel = createLabel("Don't have an account?", smallFont, 250, 365, 150, 25, loginPageContainer);

        // Signup button
        JButton signUpButton = createButton("Sign up", smallFont, 350, 365, 80, 25, null,new Color(40, 167, 69) , loginPageContainer, true);

        signUpButton.addActionListener(a -> {
            // Opens signup page
            new SignUpPage();
            dispose();
        });

        // Back Button
        JButton backButton = createButton("Back", buttonFont, 75, 380, 80, 35,new Color(108, 117, 125), Color.WHITE, loginPageContainer, false);

        backButton.addActionListener(a -> {
            // Open landing page
            new LandingPage();
            dispose();
        });

        // Frame settings
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Jobverse - Login");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginPage object7 = new LoginPage();
    }
}
