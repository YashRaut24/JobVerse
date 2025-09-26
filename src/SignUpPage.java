import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class SignUpPage extends JFrame {

    // TextFields styling
    private JTextField createTextField(Font font,int x, int y, int width, int height, JPanel panel){
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setBounds(x, y, width, height);
        panel.add(textField);
        return textField;
    }

    // Labels styling
    private JLabel createLabel(String labelString,Font font, int x, int y, int width, int height, Container container){
        JLabel label = new JLabel(labelString);
        label.setFont(font);
        label.setBounds(x,y,width,height);
        container.add(label);
        return label;
    }

    // PassFields styling
    private JPasswordField createPassField(Font font,int x, int y, int width, int height, JPanel panel){
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
    SignUpPage() {

        // Database credentials
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASS");

        // Fonts
        Font headingFont = new Font("Futura", Font.BOLD, 30);
        Font labelFont = new Font("Calibri", Font.PLAIN, 14);
        Font fieldFont = new Font("Calibri", Font.PLAIN, 14);
        Font buttonFont = new Font("Calibri", Font.BOLD, 14);
        Font smallFont = new Font("Calibri", Font.PLAIN, 12);

        // Signup container
        Container signUpContainer = getContentPane();
        signUpContainer.setLayout(null);
        signUpContainer.setBackground(Color.WHITE);

        // Heading Panel
        JLabel headingPanel = createLabel("Create your account", headingFont, 155, 8, 300, 40, signUpContainer);

        // User type label
        JLabel userTypeLabel = createLabel("I am a ",labelFont, 50, 80, 100, 20 ,signUpContainer);

        // Radiobutton (Checkbox) for jobseeker
        JRadioButton jobSeekerButton = new JRadioButton("Job Seeker");
        jobSeekerButton.setFont(labelFont);
        jobSeekerButton.setBounds(92, 80, 100, 20);
        jobSeekerButton.setSelected(true);
        signUpContainer.add(jobSeekerButton);

        // Radiobutton for employer
        JRadioButton employerButton = new JRadioButton("Employer");
        employerButton.setFont(labelFont);
        employerButton.setBounds(200, 80, 100, 20);
        signUpContainer.add(employerButton);

        // Single selection of button
        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(jobSeekerButton);
        userTypeGroup.add(employerButton);

        // Jobseeker panel
        JPanel jobSeekerPanel = new JPanel();
        jobSeekerPanel.setLayout(null);
        jobSeekerPanel.setBounds(50, 110, 500, 370);
        jobSeekerPanel.setBackground(new Color(240, 240, 240));
        signUpContainer.add(jobSeekerPanel);

        // Jobseeker first name label
        JLabel firstNameLabel = createLabel("First Name : ", labelFont, 20, 20, 200, 20,jobSeekerPanel);

        // Jobseeker first name textfield for input
        JTextField firstNameField = createTextField(fieldFont,20,40,220,25,jobSeekerPanel);

        // Jobseeker last name label
        JLabel lastNameLabel = createLabel("Last Name : ",labelFont, 260, 20, 200, 20,jobSeekerPanel );

        // Jobseeker last name textfield for input
        JTextField lastNameField = createTextField(fieldFont, 260, 40, 220, 25,jobSeekerPanel);

        // Jobseeker age label
        JLabel ageLabel = createLabel("Age : ", labelFont, 20, 75, 200, 20,jobSeekerPanel);

        // Jobseeker age textfield for input
        JTextField ageTextField = createTextField(fieldFont,20, 95, 220, 25, jobSeekerPanel);

        // Jobseeker gender label
        JLabel genderLabel = createLabel("Gender : ", labelFont, 260, 75, 200, 20,jobSeekerPanel);

        // Jobseeker gender selection (Dropdown)
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Select Gender", "Male", "Female", "Other"});
        genderCombo.setFont(fieldFont);
        genderCombo.setBounds(260, 95, 220, 25);
        jobSeekerPanel.add(genderCombo);

        // Jobseeker email label
        JLabel emailLabel = createLabel("Email : ", labelFont, 20 ,130, 200, 20,jobSeekerPanel);;

        // Jobseeker email TextField for input
        JTextField emailTextField = createTextField(fieldFont, 20, 150, 460, 25, jobSeekerPanel);

        // Jobseeker password label
        JLabel passwordLabel = createLabel("Password : ", labelFont, 20, 180, 200, 20, jobSeekerPanel);

        // Jobseeker password TextField for input
        JPasswordField passwordField = createPassField(fieldFont, 20, 200, 460, 25, jobSeekerPanel);

        // Jobseeker confirm password label
        JLabel confirmPasswordLabel = createLabel("Confirm Password : ", labelFont,20,230,200,20, jobSeekerPanel);

        // Jobseeker password confirmation PassField for input
        JPasswordField confirmPasswordField = createPassField(fieldFont,20, 250, 460, 25, jobSeekerPanel);

        // Terms and conditions checkbox
        JCheckBox jobSeekerTermsCheck = new JCheckBox("I agree to the Terms and Conditions");
        jobSeekerTermsCheck.setFont(smallFont);
        jobSeekerTermsCheck.setBounds(16, 285, 200, 20);
        jobSeekerPanel.add(jobSeekerTermsCheck);

        // Jobseeker signup button
        JButton jobSeekerSignupButton = createButton("Create Account", buttonFont, 349, 315, 130, 35, new Color(40, 167, 69), Color.WHITE, jobSeekerPanel, false);
        jobSeekerSignupButton.addActionListener(
                a->{
                    // Checks whether jobseeker's password TextField and confirm password PassField matches or not
                    // If matches
                    String jobseekerFirstName = firstNameField.getText();
                    String jobseekerLastName = lastNameField.getText();
                    String age = ageTextField.getText();
                    String jobseekerEmail = emailTextField.getText();
                    String jobseekerPassword = passwordField.getText();
                    String genderSelect =  genderCombo.getSelectedItem().toString();
                    boolean agreeSelect = jobSeekerTermsCheck.isSelected();

                    if(jobseekerFirstName.isEmpty() || jobseekerLastName.isEmpty() || age.isEmpty() || jobseekerEmail.isEmpty() || jobseekerPassword.isEmpty() || genderSelect.equals("Select Gender")){
                        JOptionPane.showMessageDialog(this, "Please fill all fields");
                        return;
                    }

                    if(!agreeSelect){
                        JOptionPane.showMessageDialog(this, "Please agree to our terms and condition");
                        return;
                    }

                    // If matches
                    if(passwordField.getText().equals(confirmPasswordField.getText())){
                        try(Connection con = DriverManager.getConnection(url,user,password)){
                            // Stores signup credentials in jobseekers table
                            String sql = "INSERT INTO jobseekers(firstname,lastname,age,gender,email,password)VALUES(?,?,?,?,?,?)";
                            try(PreparedStatement pst = con.prepareStatement(sql)){
                                pst.setString(1,jobseekerFirstName);
                                pst.setString(2,jobseekerLastName);
                                pst.setString(3,age);
                                pst.setString(4,genderSelect);
                                pst.setString(5,jobseekerEmail);
                                pst.setString(6,jobseekerPassword);
                                pst.executeUpdate();

                                String firstName = firstNameField.getText();
                                String lastName = lastNameField.getText();
                                String email = emailTextField.getText();

                                // Tracks/Stores jobseekers signup activities
                                String activitesSql = "INSERT INTO activities (empEmail , activity,user) VALUES (?,?,?)";
                                try(PreparedStatement pst6 = con.prepareStatement(activitesSql)){
                                    pst6.setString(1,email);
                                    pst6.setString(2, firstName + " " + lastName + "(" + email +")" +" has created a new Jobseeker account!" );
                                    pst6.setString(3,"JobSeeker");
                                    pst6.executeUpdate();
                                }
                                JOptionPane.showMessageDialog(null,"Account created successfully. You're all set to begin your job search!");

                                JButton setProfileButton = new JButton("Set Profile");
                                setProfileButton.setFocusPainted(false);

                                Object[] options = { setProfileButton };

                                JOptionPane pane = new JOptionPane(
                                        "Just one more step to explore opportunities.\nComplete your profile so employers can reach you.",
                                        JOptionPane.INFORMATION_MESSAGE,
                                        JOptionPane.DEFAULT_OPTION,
                                        null,
                                        options,
                                        options[0]
                                );

                                JDialog dialog = pane.createDialog("Complete Your Profile");
                                dialog.setModal(true);

                                setProfileButton.addActionListener(ev -> {
                                    dialog.dispose();
                                    new JSProfile(email, "jobseeker",firstName,lastName);
                                    dispose();
                                });

                                dialog.setVisible(true);

                                dispose();
                            }
                        }
                        catch (Exception e){
                            JOptionPane.showMessageDialog(null,e.getMessage());
                        }
                    }
                    // If not matches
                    else{
                        JOptionPane.showMessageDialog(null,"Password donot match");
                    }
                }
        );

        // Container for employer signup
        JPanel employerPanel = new JPanel();
        employerPanel.setLayout(null);
        employerPanel.setBounds(50, 110, 500, 400);
        employerPanel.setBackground(new Color(240, 240, 240));
        employerPanel.setVisible(false);
        signUpContainer.add(employerPanel);

        // Employer full name label
        JLabel employerNameLabel = createLabel("Your Full Name:", labelFont, 20, 20, 200, 20, employerPanel);

        // Employer full name TextField
        JTextField employerNameTextField = createTextField(fieldFont, 20, 40, 200, 25, employerPanel);

        // Employer role label
        JLabel employerRoleLabel = createLabel("Your Job Role:", labelFont, 260, 20, 200, 20, employerPanel);

        // Employer role TextField
        JTextField employerRoleTextField = createTextField(fieldFont, 260, 40, 220, 25, employerPanel);

        // Company Name
        JLabel companyNameLabel = createLabel("Company Name:", labelFont, 20, 80, 200, 20, employerPanel);

        // Employer company name TextField
        JTextField companyNameTextField = createTextField(fieldFont, 20, 100, 220, 25, employerPanel);

        // Employer Email label
        JLabel employerEmailLabel = createLabel("Employer Email:", labelFont, 260, 80, 200, 20, employerPanel);

        // Employer email TextField
        JTextField employerEmailTextField = createTextField(fieldFont, 260, 100, 220, 25, employerPanel);

        // Password
        JLabel employerPasswordLabel = createLabel("Password:", labelFont, 20, 140, 200, 20, employerPanel);

        // Employer password PassField
        JPasswordField EmployerPasswordField = createPassField(fieldFont, 20, 160, 220, 25, employerPanel);

        // Confirm Password
        JLabel employerConfirmPassLabel = createLabel("Confirm Password:", labelFont, 260, 140, 200, 20, employerPanel);

        // Employer confirm password PassField
        JPasswordField EmployerConfirmPassField = createPassField(fieldFont, 260, 160, 220, 25, employerPanel);

        // Company Email (moved below Password → left side)
        JLabel companyEmailLabel = createLabel("Company Email:", labelFont, 20, 200, 200, 20, employerPanel);

        // Employer company email TextField
        JTextField companyEmailTextField = createTextField(fieldFont, 20, 220, 220, 25, employerPanel);

        // Website (right side of Company Email, smaller width)
        JLabel websiteLabel = createLabel("Website:", labelFont, 260, 200, 200, 20, employerPanel);

        // Employer website TextField
        JTextField websiteTextField = createTextField(fieldFont, 260, 220, 220, 25, employerPanel);

        // Industry
        JLabel industryLabel = createLabel("Industry:", labelFont, 20, 260, 200, 20, employerPanel);

        // Industry dropdown
        JComboBox<String> industryCombo = new JComboBox<>(
                new String[]{"Select Industry", "Technology", "Finance", "Healthcare", "Education", "Manufacturing"});
        industryCombo.setFont(fieldFont);
        industryCombo.setBounds(20, 280, 460, 25);
        employerPanel.add(industryCombo);

        // Terms and condition
        JCheckBox employerTermsCheck = new JCheckBox("I agree to the Terms and Conditions");
        employerTermsCheck.setFont(smallFont);
        employerTermsCheck.setBounds(20, 316, 250, 20);
        employerPanel.add(employerTermsCheck);

        // Signup button
        JButton employerSignupButton = createButton("Create Account", buttonFont, 349, 350, 130, 35,
                new Color(40, 167, 69), Color.WHITE, employerPanel, false);

        employerSignupButton.addActionListener(a -> {
            String employerName = employerNameTextField.getText();
            String employerRole = employerRoleTextField.getText();
            String companyName = companyNameTextField.getText();
            String employerEmail = employerEmailTextField.getText();
            String companyEmail = companyEmailTextField.getText();
            String websiteUrl = websiteTextField.getText();
            String industrySelect = industryCombo.getSelectedItem().toString();
            boolean agreeSelect = employerTermsCheck.isSelected();
            String empPassword = EmployerPasswordField.getText();

            if (employerName.isEmpty() || employerRole.isEmpty() || companyName.isEmpty() ||
                    employerEmail.isEmpty() || companyEmail.isEmpty() || empPassword.isEmpty() ||
                    websiteUrl.isEmpty() || industrySelect.equals("Select Industry")) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            if (!agreeSelect) {
                JOptionPane.showMessageDialog(this, "Please agree to our terms and condition");
                return;
            }

            if (EmployerPasswordField.getText().equals(EmployerConfirmPassField.getText())) {
                try (Connection con = DriverManager.getConnection(url, user, password)) {
                    boolean signUpContinue = false;

                    String companyCheckSql = "SELECT * FROM companies WHERE employersEmail = ? AND employersName = ? AND companyEmail = ?";
                    try (PreparedStatement pst = con.prepareStatement(companyCheckSql)) {
                        pst.setString(1, employerEmail);
                        pst.setString(2, employerName);
                        pst.setString(3,companyEmail);
                        ResultSet rs = pst.executeQuery();
                        if (rs.next()) {
                            signUpContinue = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "No user found in this company");
                        }
                    }

                    if (signUpContinue) {
                        String employersSql =
                                "INSERT INTO employers(fullname, jobrole, companyname, employeremail, password, companyemail, website, industry) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement pst = con.prepareStatement(employersSql)) {
                            pst.setString(1, employerName);
                            pst.setString(2, employerRole);
                            pst.setString(3, companyName);
                            pst.setString(4, employerEmail);
                            pst.setString(5, empPassword);
                            pst.setString(6, companyEmail);
                            pst.setString(7, websiteUrl);
                            pst.setString(8, industrySelect);

                            int rowsInserted = pst.executeUpdate();

                            if (rowsInserted > 0) {
                                String activites = "INSERT INTO activities (empEmail , activity, user) VALUES (?,?,?)";
                                try (PreparedStatement pst6 = con.prepareStatement(activites)) {
                                    pst6.setString(1, employerEmail);
                                    pst6.setString(2, employerName + " (" + employerEmail + ") has created a new Employer account!");
                                    pst6.setString(3, "Employers");
                                    pst6.executeUpdate();
                                }
                                JOptionPane.showMessageDialog(null, "Account created successfully. You're all set to work for your company!");
                                new LoginPage();
                                dispose();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
            }
        });

        // Button select styling
        ActionListener userTypeListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jobSeekerPanel.setVisible(jobSeekerButton.isSelected());
                employerPanel.setVisible(employerButton.isSelected());
            }
        };
        jobSeekerButton.addActionListener(userTypeListener);
        employerButton.addActionListener(userTypeListener);

        // Back button
        JButton backButton = createButton("Back", smallFont, 50, 520, 80, 30,new Color(108, 117, 125), Color.WHITE, signUpContainer, false);

        backButton.addActionListener(
                a->{
                    // Opens landing page
                    new LandingPage();
                    dispose();
                }
        );

        // Existing account label
        JLabel loginLabel = createLabel("Already have an account?", smallFont, 370, 520, 150, 20, signUpContainer);

        // Login button
        JButton loginButton = createButton("Login", smallFont, 470, 520, 100, 20,null, new Color(0, 123, 255), signUpContainer, true);

        loginButton.addActionListener(
                a->{
                    new LoginPage();
                    dispose();
                }
        );

        // Frame settings
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Jobverse - Sign Up");
        setVisible(true);
    }

    public static void main(String[] args) {
        SignUpPage object6 = new SignUpPage();
    }
}