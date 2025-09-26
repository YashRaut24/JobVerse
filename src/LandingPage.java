import javax.swing.*;
import java.awt.*;

class LandingPage extends JFrame {

    //Label styling
    private JLabel createNavLabel(String text) {
        //Creates new label
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(text.equals("About")){
                    new About();
                }else{
                    new Contact();
                }
            }
        });
        return label;
    }

    // Button styling
    private JButton createStyledButton(String text, Font font, int width, int height, Color bgColor) {
        // Creates new button
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Features card styling
    private JPanel createFeatureCard(String title, String desc, Font titleFont, Font descFont, Color lightColor, Color accentColor) {
        // Creates new Card Panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(lightColor, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title label
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);

        // Description label
        JLabel descriptionLabel = new JLabel("<html><center>" + desc + "</center></html>", JLabel.CENTER);
        descriptionLabel.setFont(descFont);
        descriptionLabel.setForeground(new Color(108, 117, 125));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(descriptionLabel);
        return card;
    }

    // Constructor
    LandingPage() {

        // Fonts
        Font titleFont = new Font("SansSerif", Font.BOLD, 34);
        Font sectionFont = new Font("SansSerif", Font.BOLD, 20);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 17);
        Font featureTitleFont = new Font("SansSerif", Font.BOLD, 16);
        Font featureDescFont = new Font("SansSerif", Font.PLAIN, 13);

        // Landing page panel (Main Panel)
        JPanel LandingPagePanel = new JPanel();
        LandingPagePanel.setLayout(new BoxLayout(LandingPagePanel, BoxLayout.Y_AXIS));
        LandingPagePanel.setBackground(new Color(248, 249, 250));
        LandingPagePanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Navbar Panel
        JPanel navBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        navBarPanel.setBackground(new Color(52, 58, 64));
        navBarPanel.add(createNavLabel("About"));
        navBarPanel.add(createNavLabel("Contact"));

        // JobPortal Logo
        ImageIcon logo = new ImageIcon("src/JobVerse-removebg-preview.png");
        Image scaledImage = logo.getImage().getScaledInstance(340, -1, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(logoLabel);
        LandingPagePanel.add(Box.createVerticalStrut(12));

        // Title label
        JLabel titleLabel = new JLabel("Welcome to JobVerse", JLabel.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(33, 37, 41));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(titleLabel);
        LandingPagePanel.add(Box.createVerticalStrut(18));

        // JobSeeker/Employers Sections
        JLabel jobseekerLabel = new JLabel("Jobseekers / Employers", JLabel.CENTER);
        jobseekerLabel.setFont(sectionFont);
        jobseekerLabel.setForeground(new Color(73, 80, 87));
        jobseekerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(jobseekerLabel);

        // JobSeekers/Employers Login Button
        JButton loginButton = createStyledButton("Login", buttonFont, 140, 50, new Color(0, 123, 255));

        loginButton.addActionListener(
                a->{
                    // Opens login page
                    new LoginPage();
                    dispose();
                }
        );

        // JobSeekers/Employers Signup Button
        JButton signUpButton = createStyledButton("Sign Up", buttonFont, 140, 50, new Color(40, 167, 69));

        signUpButton.addActionListener(
                a->{
                    // Opens signup page
                    new SignUpPage();
                    dispose();
                }
        );

        // JobSeeker and Employer Panels
        JPanel JSEPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 10));
        JSEPanel.setOpaque(false);
        JSEPanel.add(loginButton);
        JSEPanel.add(signUpButton);
        JSEPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(JSEPanel);
        LandingPagePanel.add(Box.createVerticalStrut(15));

        // Admin label
        JLabel adminLabel = new JLabel("Admin", JLabel.CENTER);
        adminLabel.setFont(sectionFont);
        adminLabel.setForeground(new Color(73, 80, 87));
        adminLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(adminLabel);

        // Admin Login Button
        JButton adminButton = createStyledButton("Admin Login", buttonFont, 180, 55, new Color(108, 117, 125));

        adminButton.addActionListener(
                a->{
                    // Opens admin page
                    new AdminLoginPage();
                    dispose();
                }
        );

        // Admin Panel
        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        adminPanel.setOpaque(false);
        adminPanel.add(adminButton);
        adminPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(adminPanel);
        LandingPagePanel.add(Box.createVerticalStrut(20));

        // Features panel
        JPanel featuresPanel = new JPanel(new GridLayout(1, 3, 12, 12));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        LandingPagePanel.add(featuresPanel);
        LandingPagePanel.add(Box.createVerticalStrut(12));

        // Feature 1
        featuresPanel.add(createFeatureCard("🔍 Search Jobs", "Find jobs that match your skills.",
                featureTitleFont, featureDescFont, new Color(206, 212, 218), new Color(73, 80, 87)));

        // Feature 2
        featuresPanel.add(createFeatureCard("👨‍💼 Hire Talent", "Employers can hire top talent easily.",
                featureTitleFont, featureDescFont, new Color(206, 212, 218), new Color(0, 123, 255)));

        // Feature 3
        featuresPanel.add(createFeatureCard("📈 Build Career", "Grow your career with JobVerse.",
                featureTitleFont, featureDescFont, new Color(206, 212, 218), new Color(40, 167, 69)));

        featuresPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Footer label
        JLabel footerLabel = new JLabel("© 2025 JobVerse. All Rights Reserved.", JLabel.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footerLabel.setForeground(new Color(107, 114, 128));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        LandingPagePanel.add(footerLabel);

        setLayout(new BorderLayout());
        add(navBarPanel, BorderLayout.NORTH);
        add(LandingPagePanel, BorderLayout.CENTER);

        // Frame settings
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("JobVerse");
        setVisible(true);
    }

    public static void main(String[] args) {
        LandingPage object1 = new LandingPage();
    }
}