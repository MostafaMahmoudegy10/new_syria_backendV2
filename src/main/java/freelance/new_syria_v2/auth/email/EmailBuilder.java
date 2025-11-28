package freelance.new_syria_v2.auth.email;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Year;

public class EmailBuilder {

    public static String registerationEmail(String email,String name, String link) {

        try {
            // Load HTML file from resources
            String template = new String(
                    EmailBuilder.class.getResourceAsStream("/templates/emails/welcome.html")
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
    public static String otpEmail(String email, String otp) {
        try {
            String template = new String(
                    EmailBuilder.class.getResourceAsStream("/templates/emails/otp.html")
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
            return template
                    .replace("{{otp1}}", String.valueOf(otp.charAt(0)))
                    .replace("{{otp2}}", String.valueOf(otp.charAt(1)))
                    .replace("{{otp3}}", String.valueOf(otp.charAt(2)))
                    .replace("{{otp4}}", String.valueOf(otp.charAt(3)))
                    .replace("{{year}}", String.valueOf(Year.now().getValue()))
                    .replace("{{email}}", email);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load OTP email template", e);
        }
    }
}
