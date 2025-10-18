import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class ChatAssistance extends JFrame {
    private ChatService chatService;  // ADD THIS LINE

    ChatAssistance(String role) {
        String apiKey = EnvReader.getEnvValue("GEMINI_API_KEY");
        chatService = new ChatService(apiKey);  // ADD THIS LINE

        Container assistanceContainer = getContentPane();
        assistanceContainer.setLayout(null);

        // Chat display area
        JTextArea chatTextArea = new JTextArea();
        chatTextArea.setLineWrap(true);
        chatTextArea.setWrapStyleWord(true);
        chatTextArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane chatScrollPane = new JScrollPane(chatTextArea);
        chatScrollPane.setBounds(0, 0, 450, 500);
        assistanceContainer.add(chatScrollPane);

        // Bottom panel for input
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBounds(0, 500, 450, 100);
        bottomPanel.setLayout(null);

        JTextArea chatTextField = new JTextArea(2, 20);
        chatTextField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane chatInputScrollPane = new JScrollPane(chatTextField);
        chatInputScrollPane.setBounds(10, 7, 320, 50);
        bottomPanel.add(chatInputScrollPane);

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
                // Display user's message
                chatTextArea.append("You: " + text + "\n");
                chatTextField.setText("");
                scrollToBottom(chatTextArea);
                chatTextArea.append("VerserAI: Thinking...\n");

                sendButton.setEnabled(false);

                final String userMessage = text;

                // Create background worker to call API
                SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {

                    @Override
                    protected String doInBackground() throws Exception {
                        // This runs in background thread (doesn't freeze GUI)
                        return chatService.sendMessage(userMessage);
                    }

                    @Override
                    protected void done() {
                        try {
                            String aiResponse = get();
                            String currentText = chatTextArea.getText();
                            int lastThinking = currentText.lastIndexOf("VerserAI: Thinking...\n");
                            if (lastThinking != -1) {
                                chatTextArea.setText(currentText.substring(0, lastThinking));
                            }

                            chatTextArea.append("VerserAI: " + aiResponse + "\n\n");
                            scrollToBottom(chatTextArea);
                            sendButton.setEnabled(true);

                        } catch (Exception ex) {
                            chatTextArea.append("Error: " + ex.getMessage() + "\n\n");
                            sendButton.setEnabled(true);
                        }
                    }
                };
                worker.execute();
            }
        });

        assistanceContainer.add(bottomPanel);

        // Frame settings
        setTitle("💬 Chat with VerserAI (" + role + ")");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void scrollToBottom(JTextArea textArea) {
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        new ChatAssistance("ROLE");
    }
}
