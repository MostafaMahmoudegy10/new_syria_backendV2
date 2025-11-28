package freelance.new_syria_v2.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailSend {

    private final JavaMailSender mailSender;

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    @Async
    public void send(String to, String emailHtml,String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(emailHtml, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(senderEmail);

            mailSender.send(mimeMessage);

            this.LOGGER.debug("email sent successfully {}",to);

        } catch (MessagingException e) {
            LOGGER.error("❌ Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
