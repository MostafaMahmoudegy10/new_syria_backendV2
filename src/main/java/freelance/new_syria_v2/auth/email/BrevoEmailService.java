package freelance.new_syria_v2.auth.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BrevoEmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.api.url}")
    private String apiUrl;

    public void sendEmail(String toEmail, String toName, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        System.out.println(apiKey);
        Map<String, Object> body = new HashMap<>();

        // Sender
        Map<String, String> sender = new HashMap<>();
        sender.put("email", "info@newsyria.news");
        sender.put("name", "hamada kirata");

        // Recipient
        Map<String, String> to = new HashMap<>();
        to.put("email", toEmail);
        to.put("name", toName);

        // Build body
        body.put("sender", sender);
        body.put("to", List.of(to));
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json"); // ✅ مهم جداً
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Email sent successfully: " + response.getBody());
            } else {
                System.err.println("❌ Failed to send email: " + response.getBody());
            }
        }
    }

