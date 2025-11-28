package freelance.new_syria_v2.auth.service;

import freelance.new_syria_v2.auth.annotaions.CurrentUser;
import freelance.new_syria_v2.auth.controller.ResetPassword;
import freelance.new_syria_v2.auth.email.BrevoEmailService;
import freelance.new_syria_v2.auth.email.EmailBuilder;
import freelance.new_syria_v2.auth.entity.CurrentUserDto;
import freelance.new_syria_v2.auth.entity.OTP;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.auth.jwt.JwtUtils;
import freelance.new_syria_v2.auth.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ResetPasswordService {

    private final CustomUserDetailsService customUserDetailsService;
    private final BrevoEmailService emailService;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${spring.app.otpExpirationMs}")
    private Long expirations;

    public ResetPasswordService(CustomUserDetailsService customUserDetailsService,
                                BrevoEmailService emailService,
                                OtpRepository otpRepository,
                                PasswordEncoder passwordEncoder,
                                JwtUtils jwtUtils) {
        this.customUserDetailsService = customUserDetailsService;
        this.emailService = emailService;
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public String requestResetPassword(String email){
        boolean exists = customUserDetailsService.isPresent(email);
        if (!exists) {
            throw new UsernameNotFoundException("Invalid email!");
        }

        String otpCode = generateOtp();

        OTP otp = new OTP();
        otp.setCode(otpCode);
        otp.setEmail(email);
        otp.setExpiresAt(LocalDateTime.now().plusSeconds(expirations));
        otpRepository.save(otp);

        emailService.sendEmail(
                email,
                null,
                "Your OTP Code",
                EmailBuilder.otpEmail(email, otpCode)
        );

        return "OTP sent successfully!";
    }

    private String generateOtp() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(1000, 10000));
    }

    public String verifyOtp(ResetPassword.RequestResetPasswordDto dto) {

        OTP otp = otpRepository.findByEmailAndCodeOtp(dto.email(), dto.otp())
                .orElseThrow(() -> new RuntimeException("Invalid OTP or email"));

        if (otp.isUsed()) {
            throw new RuntimeException("OTP already used!");
        }

        if (LocalDateTime.now().isAfter(otp.getExpiresAt())) {
            throw new RuntimeException("OTP expired!");
        }

        CustomUserDetails user = (CustomUserDetails) customUserDetailsService.loadUserByUsername(dto.email());
        String token = jwtUtils.generateToken(user).token();

        otp.setUsed(true);
        otpRepository.save(otp);

        return token;
    }

    public String resetPassword(String newPassword, CurrentUserDto user) {
        User userDb =this.customUserDetailsService.findUser(user.id());
        userDb.setPassword(passwordEncoder.encode(newPassword));
        customUserDetailsService.save(userDb);
        return "Password reset successfully!";
    }
}

