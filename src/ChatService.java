import java.io.*;
import java.net.*;

public class ChatService {
    private String apiKey;
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";

    public ChatService(String apiKey) {
        this.apiKey = apiKey;

        if (this.apiKey == null || this.apiKey.isEmpty()) {
            System.out.println("⚠️ API key not found in .env file!");
        }
    }

    public String sendMessage(String userMessage) throws IOException {
        String fullUrl = API_URL + "?key=" + apiKey;

        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInput = createJsonRequest(userMessage);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return parseResponse(response.toString());
    }

    private String parseResponse(String jsonResponse) {
        try {
            // Find the start of the text field
            int textStart = jsonResponse.indexOf("\"text\": \"");
            if (textStart == -1) {
                return "Error: Could not find text in response";
            }

            textStart += 9;

            StringBuilder result = new StringBuilder();
            boolean escapeNext = false;

            for (int i = textStart; i < jsonResponse.length(); i++) {
                char c = jsonResponse.charAt(i);

                if (escapeNext) {
                    // Handle escaped characters
                    switch (c) {
                        case 'n':
                            result.append('\n');
                            break;
                        case 't':
                            result.append('\t');
                            break;
                        case 'r':
                            result.append('\r');
                            break;
                        case '\\':
                            result.append('\\');
                            break;
                        case '"':
                            result.append('"');
                            break;
                        default:
                            result.append(c);
                    }
                    escapeNext = false;
                } else {
                    if (c == '\\') {
                        escapeNext = true;
                    } else if (c == '"') {
                        // Found the closing quote
                        break;
                    } else {
                        result.append(c);
                    }
                }
            }

            return result.toString();

        } catch (Exception e) {
            return "Error parsing response: " + e.getMessage();
        }
    }

    private String createJsonRequest(String message) {
        message = message.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        return "{"
                + "\"contents\":[{"
                + "\"parts\":[{\"text\":\"" + message + "\"}]"
                + "}]"
                + "}";
    }

    public static void main(String[] args) {
        try {
            String apiKey = EnvReader.getEnvValue("GEMINI_API_KEY");
            ChatService service = new ChatService(apiKey);
            String response = service.sendMessage("Give me 5 job search tips");
            System.out.println("AI Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}