import javax.swing.*;
import java.awt.*;
import java.sql.*;

class AdminSignUpPage extends JFrame {

    // TextFields styling
    private JTextField createTextField(Font font, int x, int y, int width, int height, Container container) {

        // TextField
        JTextField textField = new JTextField(); // Creates TextField
        textField.setFont(font); // Sets font
        textField.setBounds(x, y, width, height); // Sets positioning
        textField.setBackground(Color.WHITE); // Sets background color
        textField.setForeground(Color.DARK_GRAY); // Sets text or contents color
        textField.setMargin(new Insets(5, 8, 5, 8)); // Creates margin to TextFields
        textField.setBorder(BorderFactory.createCompoundBorder( // Creates border
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true), // Create border color
                BorderFactory.createEmptyBorder(2, 8, 2, 8) // Extends width and height of TextField
        ));
        textField.setCaretColor(new Color(52, 152, 219)); // Sets blinking cursor's color
        container.add(textField); // Adds to the content
        return textField; // Returns TextFields
    }

    // Labels styling
    private JLabel createLabel(String labelString, Font font, int x, int y, int width, int height, Container container) {
        JLabel label = new JLabel(labelString); // Creates label
        label.setFont(font);
        label.setBounds(x, y, width, height);
        container.add(label);
        return label;
    }

    // Passfields styling
    private JPasswordField createPassField(Font font, int x, int y, int width, int height, Container container) {
        JPasswordField passwordField = new JPasswordField(); // Creates a PasswordField similar to TextField but here the input(password we type) is hidden
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
        JButton button = new JButton(text); // Creates a button
        button.setFont(font);
        button.setBounds(x, y, width, height);
        button.setForeground(fgColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Sets hands cursor

        // Checks if the button is normal button or text button, here login is the text button (flat === true)
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
    AdminSignUpPage() {

        String adminSignUp = "SignUpPage";
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

        // Admin signup container
        Container adminSignUpContainer = getContentPane();
        adminSignUpContainer.setLayout(null);
        adminSignUpContainer.setBackground(Color.WHITE);

        // Admin title label
        JLabel titleLabel = createLabel("Admin Signup", headingFont, 20, 20, 350, 30, adminSignUpContainer);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Full Name Label
        JLabel nameLabel = createLabel("Full Name:", labelFont, 32, 80, 100, 25, adminSignUpContainer);

        // Full Name TextField
        JTextField nameTextField = createTextField(fieldFont, 132, 80, 220, 30, adminSignUpContainer);

        // Email Label
        JLabel emailLabel = createLabel("Email:", labelFont, 32, 130, 100, 25, adminSignUpContainer);

        // Email TextField
        JTextField emailTextField = createTextField(fieldFont, 132, 130, 220, 30, adminSignUpContainer);

        // Username Label
        JLabel userNameLabel = createLabel("Username:", labelFont, 32, 180, 100, 25, adminSignUpContainer);

        // Username TextField
        JTextField userTextField = createTextField(fieldFont, 132, 180, 220, 30, adminSignUpContainer);

        // Password Label
        JLabel passwordLabel = createLabel("Password:", labelFont, 32, 230, 100, 25, adminSignUpContainer);

        // Password TextField
        JPasswordField passwordField = createPassField(fieldFont, 132, 230, 220, 30, adminSignUpContainer);

        // Account Exists Label
        JLabel loginLabel = createLabel("Already have an account?", smallFont, 192, 270, 150, 20, adminSignUpContainer);

        // Back button
        JButton backButton = createButton("Back", buttonFont,30,310,140,40,new Color(108, 117, 125) , Color.white,adminSignUpContainer,false);

        backButton.addActionListener(
                a->{
                    // Opens Landing page
                    new LandingPage();
                    dispose();
                }
        );

        // Login button for existing account
        JButton loginButton = createButton("Login", smallFont, 287, 270, 100, 20, null,new Color(0, 123, 255), adminSignUpContainer, true);

        loginButton.addActionListener(
                a -> {
                    // Opens Admin Login Page
                    new AdminLoginPage();
                    dispose();
                }
        );

        // Signup Button
        JButton signupBtn = createButton("Sign Up", buttonFont, 210, 310, 140, 40,new Color(40, 167, 69), Color.WHITE, adminSignUpContainer, false);

        signupBtn.addActionListener(a -> {

            // Fetching inputs from TextFields and storing in String variables
            String fullname = nameTextField.getText();
            String email = emailTextField.getText();
            String username = userTextField.getText();
            String adminPassword = new String(passwordField.getPassword());

            // Checks every TextField has some input or not
            if (fullname.isEmpty() || email.isEmpty() || username.isEmpty() || adminPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try (Connection con = DriverManager.getConnection(url, user, password)) {
                // Insert input in admin table query
                String adminSql = "INSERT INTO admin(fullname, email, username, password) VALUES(?, ?, ?, ?)";
                try (PreparedStatement pst = con.prepareStatement(adminSql)) {
                    pst.setString(1, fullname);
                    pst.setString(2, email);
                    pst.setString(3, username);
                    pst.setString(4, adminPassword);

                    // Stores when admin signup into their account to track their activity
                    String activities = "INSERT INTO activities (empEmail, activity, user) VALUES (?, ?, ?)";
                    try (PreparedStatement pst6 = con.prepareStatement(activities)) {
                        pst6.setString(1, email);
                        pst6.setString(2, fullname + " " + "(" + email + ")" + " has created their admin account!");
                        pst6.setString(3, "Admin");
                        pst6.executeUpdate();
                    }
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Admin registered successfully!");
                    // Opens admin Page
                    new AdminLoginPage();
                    dispose();

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        });

        // Frame settings
        setTitle("Admin Signup - JobVerse");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        AdminSignUpPage object4 = new AdminSignUpPage();
    }
}