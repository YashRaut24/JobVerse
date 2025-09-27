import javax.swing.*;
import java.awt.*;

class About extends JFrame {
    About() {


        // About panel
        JPanel aboutPanel = new JPanel(null);
        aboutPanel.setBackground(new Color(245, 247, 250));

        // Header panel
        JPanel headerPanel = new JPanel(null);
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.setBounds(0, 0, 650, 70);
        aboutPanel.add(headerPanel);

        // About title label
        JLabel aboutTitleLabel = new JLabel("About JobVerse");
        aboutTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        aboutTitleLabel.setForeground(Color.WHITE);
        aboutTitleLabel.setBounds(25, 20, 400, 30);
        headerPanel.add(aboutTitleLabel);

        // Content card panel
        JPanel contentCardPanel = new JPanel(null);
        contentCardPanel.setBackground(Color.WHITE);
        contentCardPanel.setBounds(30, 100, 590, 460);
        contentCardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // About text area
        JTextArea aboutTextArea = new JTextArea(
                "JobVerse is a comprehensive job portal platform that bridges the gap between talent and opportunity.\n\n" +
                        "Key Features:\n" +
                        "• Multi-role System: Separate dashboards for Job Seekers, Employers, and Admins\n" +
                        "• Smart Job Search: Advanced filtering by location, salary, skills, and job type\n" +
                        "• Real-time Communication: Built-in chat system and instant notifications\n" +
                        "• Application Management: Track applications, schedule interviews, and send offers\n" +
                        "• Company Verification: Admin-controlled employer registration for authenticity\n" +
                        "• Analytics Dashboard: Growth metrics and job trends for data-driven decisions\n" +
                        "• Profile Management: Complete profile setup with resume upload capabilities\n" +
                        "• Report System: Community-driven safety with user reporting features\n" +
                        "• Interview Scheduling: Seamless interview coordination between parties\n\n" +
                        "Our Mission: To create a secure, efficient, and user-friendly platform that transforms\n" +
                        "how job seekers find opportunities and employers discover talent.\n\n" +
                        "Built with modern web technologies to deliver a scalable and robust hiring solution."
        );

        aboutTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        aboutTextArea.setLineWrap(true);
        aboutTextArea.setWrapStyleWord(true);
        aboutTextArea.setEditable(false);
        aboutTextArea.setBackground(Color.WHITE);
        aboutTextArea.setBounds(20, 20, 550, 420);
        contentCardPanel.add(aboutTextArea);

        aboutPanel.add(contentCardPanel);
        add(aboutPanel);

        // Frame Settings
        setTitle("About JobVerse");
        setVisible(true);
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    public static void main(String[] args) {
        About object2 = new About();
    }
}