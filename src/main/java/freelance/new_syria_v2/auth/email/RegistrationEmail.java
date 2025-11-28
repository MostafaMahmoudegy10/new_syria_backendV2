package freelance.new_syria_v2.auth.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Year;

public class RegistrationEmail {

    public static String buildEmail(String email,String name, String link) {

        try {
            // Load HTML file from resources
            String template = new String(
                    RegistrationEmail.class.getResourceAsStream("/templates/emails/welcome.html")
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            return template
                    .replace("{{name}}", name)
                    .replace("{{link}}", link)
                    .replace("{{year}}", String.valueOf(Year.now().getValue()))
                    .replace("{{email}}",email);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}
