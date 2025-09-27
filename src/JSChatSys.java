import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class JSChatSys extends JFrame {

    JTextArea chatArea;
    JTextField messageField;
    JButton sendButton;
    Timestamp lastTimestamp = Timestamp.valueOf("1970-01-01 00:00:00");

    // DB Credentials
    String url = System.getenv("DB_URL");
    String user = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");

    // Sends message to employers
    private void sendMessage(String jobSeekerName, String employerName) {
        String msg = messageField.getText().trim();
        if (!msg.isEmpty()) {
            boolean sent = false;
            String insertSql = "INSERT INTO chatlog (sender, receiver, message, timestamp) VALUES (?, ?, ?, NOW())";

            // Create new connection for sending message
            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement insert = con.prepareStatement(insertSql)) {

                insert.setString(1, jobSeekerName);
                insert.setString(2, employerName);
                insert.setString(3, msg);
                insert.executeUpdate();
                sent = true;
            } catch (Exception ex) {
                appendSystemMessage("❌ Failed to send message: " + ex.getMessage());
            }

            if (sent) {
                appendMessage("You", msg, true);
                messageField.setText("");
                messageField.requestFocus();
            }
        }
    }

    private void appendMessage(String sender, String message, boolean isOwnMessage) {
        String prefix = isOwnMessage ? "You" : sender;
        String color = isOwnMessage ? "🔵" : "🟢";
        chatArea.append(color + " " + prefix + ": " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    private void appendSystemMessage(String message) {
        chatArea.append("ℹ️ " + message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }


    JSChatSys(String jobSeekerName, String employerName) {

        String JSChat = "JSChat";

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignore) {}

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setBackground(new Color(248, 249, 250));
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Message TextField
        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Send button
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(0, 123, 255));
        sendButton.setForeground(Color.WHITE);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 58, 64));
        JLabel headerLabel = new JLabel("JobVerse Chat");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Load chat history - use separate connection
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            appendSystemMessage("Connected to chat server");

            // Load chat history
            String historyQuery = "SELECT sender, message, timestamp FROM chatlog WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?) ORDER BY timestamp";

            try (PreparedStatement ps = con.prepareStatement(historyQuery)) {
                ps.setString(1, jobSeekerName);
                ps.setString(2, employerName);
                ps.setString(3, employerName);
                ps.setString(4, jobSeekerName);

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String sender = rs.getString("sender");
                    String message = rs.getString("message");
                    Timestamp ts = rs.getTimestamp("timestamp");

                    appendMessage(sender, message, sender.equals(jobSeekerName));
                    if (ts.after(lastTimestamp)) lastTimestamp = ts;
                }
            }

        } catch (Exception ex) {
            appendSystemMessage("Connection failed: " + ex.getMessage());
        }

        sendButton.addActionListener(e -> sendMessage(jobSeekerName, employerName));
        messageField.addActionListener(e -> sendMessage(jobSeekerName, employerName));

        Timer messageTimer = new Timer();
        messageTimer.schedule(new TimerTask() {
            public void run() {
                // Create new connection for each refresh
                String refreshQuery = "SELECT sender, message, timestamp FROM chatlog WHERE sender=? AND receiver=? AND timestamp > ? ORDER BY timestamp";

                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement pst = con.prepareStatement(refreshQuery)) {

                    pst.setString(1, employerName);
                    pst.setString(2, jobSeekerName);
                    pst.setTimestamp(3, lastTimestamp);

                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        String from = rs.getString("sender");
                        String msg = rs.getString("message");
                        Timestamp ts = rs.getTimestamp("timestamp");

                        SwingUtilities.invokeLater(() -> appendMessage(from, msg, false));
                        lastTimestamp = ts;
                    }
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> appendSystemMessage("❌ Refresh error: " + e.getMessage()));
                }
            }
        }, 2000, 2000);

        // Frame settings
        setTitle("💬 Chat with " + employerName);
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        JSChatSys object12 = new JSChatSys("JobSeekerName", "EmployerName");
    }
}