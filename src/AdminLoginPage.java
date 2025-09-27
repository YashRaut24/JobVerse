import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AdminLoginPage extends JFrame {

    // TextFields styling
    private JTextField createTextField(Font font, int x, int y, int width, int height, Container container) {
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBounds(x, y, width, height);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.DARK_GRAY);
        textField.setMargin(new Insets(5, 8, 5, 8));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        textField.setCaretColor(new Color(52, 152, 219));
        container.add(textField);
        return textField;
    }

    // Labels styling
    private JLabel createLabel(String labelString, Font font, int x, int y, int width, int height, Container container) {
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        container.add(label);
        return label;
    }

    // Passfields styling
    private JPasswordField createPassField(Font font, int x, int y, int width, int height, Container container) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setBounds(x, y, width, height);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.DARK_GRAY);
        passwordField.setMargin(new Insets(5, 8, 5, 8));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        passwordField.setCaretColor(new Color(52, 152, 219));
        container.add(passwordField);
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
            button.setFocusPainted(false);
            if (bgColor != null) {
                button.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 2, true));
                button.setContentAreaFilled(true);
            }
        }
        container.add(button);
        return button;
    }

    // Constructor
    AdminLoginPage() {

        String adminLogin = "Admin Login";
        // Database Credentials
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        // Fonts
        Font headingFont = new Font("Futura", Font.BOLD, 24);
        Font labelFont = new Font("Futura", Font.BOLD, 16);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 14);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font smallFont = new Font("Calibri", Font.PLAIN, 12);

        // Admin Login page Container
        Container adminLoginContainer = getContentPane();
        adminLoginContainer.setLayout(null);
        adminLoginContainer.setBackground(Color.WHITE);

        // Title label
        JLabel titleLabel = createLabel("Admin Login", headingFont, 20, 20, 350, 30, adminLoginContainer);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Username Label
        JLabel userNameLabel = createLabel("Username:", labelFont, 32, 80, 100, 25, adminLoginContainer);

        // Username TextField
        JTextField userNameTextField = createTextField(fieldFont, 132, 80, 220, 30, adminLoginContainer);

        // Password label
        JLabel passwordLabel = createLabel("Password:", labelFont, 32, 130, 100, 25, adminLoginContainer);

        // Password TextField
        JPasswordField passwordField = createPassField(fieldFont, 132, 130, 220, 30, adminLoginContainer);

        // Sign Up Label
        JLabel signUpLabel = createLabel("Don't have an account?", smallFont, 192, 170, 150, 20, adminLoginContainer);

        // Signup Button
        JButton signupButton = createButton("Sign Up", smallFont, 280, 170, 100, 20, null, new Color(40, 167, 69), adminLoginContainer, true);

        signupButton.addActionListener(b -> {
            // Opens Admin Signup page
            new AdminSignUpPage();
            dispose();
        });

        // Back button
        JButton backButton = createButton("Back", buttonFont,30,200,140,40,new Color(108, 117, 125), Color.white,adminLoginContainer,false);

        backButton.addActionListener(
                a->{
                    // Opens Landing page
                    new LandingPage();
                    dispose();
                }
        );

        // Login Button
        JButton loginButton = createButton("Login", buttonFont, 210, 200, 140, 40,new Color(0, 123, 255), Color.WHITE, adminLoginContainer, false);

        loginButton.addActionListener(a -> {
            String username = userNameTextField.getText();
            String adminPassword = new String(passwordField.getPassword());

            // Checks every TextField has an input or not
            if (username.isEmpty() || adminPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                String adminCheckSql = "SELECT * FROM admin WHERE username = ? AND password = ?";
                try (PreparedStatement pst = con.prepareStatement(adminCheckSql)) {

                    // Checks the values in table
                    pst.setString(1, username);
                    pst.setString(2, adminPassword);

                    // Stores whether it is present in it or not
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        // If present
                        // Get some values from this table and stores in string variables
                        String email = rs.getString("email");
                        String fullname = rs.getString("fullname");

                        // Stores whenever admin logins into their account to track their activity
                        String activities = "INSERT INTO activities (empEmail, activity, user) VALUES (?, ?, ?)";
                        try (PreparedStatement pst6 = con.prepareStatement(activities)) {
                            pst6.setString(1, email);
                            pst6.setString(2, fullname + " " + "(" + email + ")" + " has logged into their admin account!");
                            pst6.setString(3, "Admin");
                            pst6.executeUpdate();
                        }
                        // Displays when the TextField's input is present in table
                        JOptionPane.showMessageDialog(this, "Login Successful as Admin!");
                        // Opens Admin Dashboard
                        new AdminDashboard( email, fullname);
                        dispose();
                    } else {
                        // If not present
                        JOptionPane.showMessageDialog(this, "Invalid credentials!");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        // Frame Settings
        setTitle("Admin Login - JobVerse");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        AdminLoginPage object5 = new AdminLoginPage();
    }
}