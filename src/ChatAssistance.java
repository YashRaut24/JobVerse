import javax.swing.*;
import java.awt.*;

class ChatAssistance extends JFrame {
    ChatAssistance(String role) {

        // Main container
        Container assistanceContainer = getContentPane();
        assistanceContainer.setLayout(null);

        // Chat display text area (scrollable)
        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        chatTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        chatScrollPane.setBounds(0, 0, 450, 500);
        assistanceContainer.add(chatScrollPane);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(0, 500, 450, 100);
        bottomPanel.setLayout(null);

        // Scrollable chat area
        JTextArea chatTextField = new JTextArea(2, 20);
        chatTextField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane chatInputScrollPane = new JScrollPane(chatTextField);
        chatInputScrollPane.setBounds(10, 7, 320, 50);
        bottomPanel.add(chatInputScrollPane);

        // Send button
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(340, 7, 90, 50);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        sendButton.setBackground(new Color(66, 133, 244));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPanel.add(sendButton);

        sendButton.addActionListener(e -> {
            String text = chatTextField.getText().trim();
            if (!text.isEmpty()) {
                chatTextArea.append("You: " + text + "\n");
                chatTextField.setText("");
            }
        });

        assistanceContainer.add(bottomPanel);

        // Frame settings
        setTitle("💬 Chat with VerserAI (" + role +")");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ChatAssistance("ROLE");
    }
}
