import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EnvReader {
    public static String getEnvValue(String key) {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key + "=")) {
                    return line.substring(key.length() + 1).trim();
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Could not read .env file: " + e.getMessage());
        }
        return null;
    }
}
