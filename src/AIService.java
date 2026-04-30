import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIService {

    private static final String API_KEY = System.getenv("API_KEY");

    public static String askGemini(String query) {
        try {
            String prompt =
"You are a Gitlet CLI assistant.\n" +
"Convert the user query into a Gitlet command.\n" +
"\nSTRICT RULES:\n" +
"1. Output MUST start with 'gitlet'\n" +
"2. Output ONLY the command\n" +
"3. No backticks, no explanation\n" +
"4. Always include full command\n\n" +

"Examples:\n" +
"show history -> gitlet log\n" +
"undo last commit -> gitlet reset HEAD~1\n" +
"switch to feature branch -> gitlet checkout feature\n\n" +

"User: " + query;

            String jsonInput =
                    "{ \"contents\": [ { \"parts\": [ { \"text\": " +
                    quoteJson(prompt) +
                    " } ] } ] }";

            URL url = new URL(
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
            );

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("x-goog-api-key", API_KEY);
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            InputStream stream = (status >= 200 && status < 300)
                    ? conn.getInputStream()
                    : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            String body = response.toString();
            //System.out.println("RAW RESPONSE: " + body);

            if (status < 200 || status >= 300) {
                return "API Error: HTTP " + status;
            }

            String marker = "\"text\": \"";
            int start = body.indexOf(marker);
            if (start == -1) {
                return "No text found in API response.";
            }
            start += marker.length();

            StringBuilder result = new StringBuilder();
            boolean escape = false;
            for (int i = start; i < body.length(); i++) {
                char c = body.charAt(i);
                if (escape) {
                    switch (c) {
                        case 'n': result.append('\n'); break;
                        case 't': result.append('\t'); break;
                        case 'r': result.append('\r'); break;
                        case '"': result.append('"'); break;
                        case '\\': result.append('\\'); break;
                        default: result.append(c);
                    }
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    break;
                } else {
                    result.append(c);
                }
            }

            return result.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "AI Error occurred.";
        }
    }

    private static String quoteJson(String s) {
        return "\"" + s
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
}