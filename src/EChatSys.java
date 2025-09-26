import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.Timer;

public class EChatSys extends JFrame {

    JTextArea chatArea;
    JTextField messageField;
    JButton sendButton;
    JList<String> jobSeekerList;
    DefaultListModel<String> listModel;
    JScrollPane chatScrollPane;
    JLabel currentChatLabel;
    JLabel onlineStatusLabel;

    String employer;
    String currentJobSeeker;
    Timestamp lastTimestamp = Timestamp.valueOf("1970-01-01 00:00:00");

    // DB Credentials
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");

    // Load chat history for selected job seeker
    private void loadChatHistory(String jobSeeker) {
        String sql = "SELECT * FROM chatlog WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?) ORDER BY timestamp";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, employer);
            pst.setString(2, jobSeeker);
            pst.setString(3, jobSeeker);
            pst.setString(4, employer);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                appendMessage(rs.getString("sender"), rs.getString("message"), rs.getTimestamp("timestamp"), rs.getString("sender").equals(employer));
            }
        } catch (Exception e) {
            appendSystemMessage("Chat history error: " + e.getMessage());
        }
    }

    // Send message to selected job seeker
    private void sendMessage(String sender, String receiver, String message) {
        String sql = "INSERT INTO chatlog (sender, receiver, message, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, sender);
            pst.setString(2, receiver);
            pst.setString(3, message);
            pst.executeUpdate();
            appendMessage("You", message, new Timestamp(System.currentTimeMillis()), true);
        } catch (Exception e) {
            appendSystemMessage("❌ Failed to send message: " + e.getMessage());
        }
    }

    private void appendMessage(String sender, String message, Timestamp timestamp, boolean isOwnMessage) {
        String timeStr = timestamp.toString().substring(11, 19);
        String displayName = isOwnMessage ? "You" : sender;
        SwingUtilities.invokeLater(() -> chatArea.append("[" + timeStr + "] " + displayName + ": " + message + "\n"));
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void appendSystemMessage(String message) {
        chatArea.append("ℹ️ " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private Timestamp getLatestTimestamp(String jobSeeker) {
        String sql = "SELECT MAX(timestamp) as latest FROM chatlog WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?)";
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, employer);
            pst.setString(2, jobSeeker);
            pst.setString(3, jobSeeker);
            pst.setString(4, employer);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Timestamp latest = rs.getTimestamp("latest");
                return latest != null ? latest : Timestamp.valueOf("1970-01-01 00:00:00");
            }
        } catch (Exception e) {
            appendSystemMessage("Timestamp fetch error: " + e.getMessage());
        }
        return Timestamp.valueOf("1970-01-01 00:00:00");
    }

    EChatSys(String employer) {
        this.employer = employer;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Create components
        listModel = new DefaultListModel<>();
        jobSeekerList = new JList<>(listModel);
        chatArea = new JTextArea();
        currentChatLabel = new JLabel("Select a job seeker to start chatting");
        onlineStatusLabel = new JLabel("●", SwingConstants.CENTER);
        messageField = new JTextField();
        sendButton = new JButton("Send");

        // Style components
        jobSeekerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jobSeekerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chatArea.setEditable(false);
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Layout components
        setLayout(new BorderLayout());

        // Left panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel leftTitle = new JLabel("Job Seekers");
        leftTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        leftPanel.add(leftTitle, BorderLayout.NORTH);

        JScrollPane listScrollPane = new JScrollPane(jobSeekerList);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // Right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        currentChatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JPanel headerContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerContent.setBackground(Color.WHITE);
        headerContent.add(currentChatLabel);
        headerContent.add(Box.createHorizontalStrut(10));
        headerContent.add(onlineStatusLabel);
        chatHeader.add(headerContent, BorderLayout.WEST);
        rightPanel.add(chatHeader, BorderLayout.NORTH);

        chatScrollPane = new JScrollPane(chatArea);
        rightPanel.add(chatScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // Load initial data with separate connection
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            appendSystemMessage("Connected to chat server");

            // Load job seekers list
            String sql = "SELECT DISTINCT sender FROM chatlog WHERE receiver = ?";
            try (PreparedStatement pst = con.prepareStatement(sql)) {
                pst.setString(1, employer);
                ResultSet rs = pst.executeQuery();
                listModel.clear();
                while (rs.next()) {
                    listModel.addElement(rs.getString("sender"));
                }
            }

        } catch (Exception ex) {
            appendSystemMessage("Connection failed: " + ex.getMessage());
        }

        // Event listeners
        jobSeekerList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                currentJobSeeker = jobSeekerList.getSelectedValue();
                if (currentJobSeeker != null) {
                    currentChatLabel.setText("Chat with " + currentJobSeeker);
                    chatArea.setText("");
                    loadChatHistory(currentJobSeeker);
                    lastTimestamp = getLatestTimestamp(currentJobSeeker);
                }
            }
        });

        ActionListener sendAction = e -> {
            String message = messageField.getText().trim();
            if (!message.isEmpty() && currentJobSeeker != null) {
                sendMessage(employer, currentJobSeeker, message);
                messageField.setText("");
            }
        };

        sendButton.addActionListener(sendAction);
        messageField.addActionListener(sendAction);

        // Message polling timer - CREATE FRESH CONNECTION EACH TIME
        Timer messageTimer = new Timer();
        messageTimer.schedule(new TimerTask() {
            public void run() {
                if (currentJobSeeker != null) {
                    String refreshQuery = "SELECT * FROM chatlog WHERE ((sender=? AND receiver=?) OR (sender=? AND receiver=?)) AND timestamp > ? ORDER BY timestamp";

                    // Create fresh connection for each poll
                    try (Connection con = DriverManager.getConnection(url, user, password);
                         PreparedStatement pst = con.prepareStatement(refreshQuery)) {

                        pst.setString(1, employer);
                        pst.setString(2, currentJobSeeker);
                        pst.setString(3, currentJobSeeker);
                        pst.setString(4, employer);
                        pst.setTimestamp(5, lastTimestamp);

                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            String from = rs.getString("sender");
                            String msg = rs.getString("message");
                            Timestamp ts = rs.getTimestamp("timestamp");

                            if (!from.equals(employer)) {
                                SwingUtilities.invokeLater(() -> appendMessage(from, msg, ts, false));
                            }
                            if (ts.after(lastTimestamp)) {
                                lastTimestamp = ts;
                            }
                        }
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> appendSystemMessage("❌ Refresh error: " + ex.getMessage()));
                    }
                }
            }
        }, 2000, 2000);

        // Frame settings
        setTitle("JobVerse - Employer Chat System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        EChatSys object15 = new EChatSys("employer@example.com");
    }
}