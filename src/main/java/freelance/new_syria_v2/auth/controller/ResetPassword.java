package freelance.new_syria_v2.auth.controller;

import freelance.new_syria_v2.auth.annotaions.CurrentUser;
import freelance.new_syria_v2.auth.annotaions.IsPublic;
import freelance.new_syria_v2.auth.entity.CurrentUserDto;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.auth.service.ResetPasswordService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth/reset-password")
public class ResetPassword {

    private final ResetPasswordService service;

    public record RequestResetPasswordDto(String email, String otp) {}

    public ResetPassword(ResetPasswordService service) {
        this.service = service;
    }

    @IsPublic
    @PostMapping("/request")
    public String request(@ModelAttribute RequestResetPasswordDto dto) {
        return service.requestResetPassword(dto.email());
    }

    @IsPublic
    @PostMapping("/verify")
    public Map<String,String> verify(@ModelAttribute RequestResetPasswordDto dto) {
        String token=service.verifyOtp(dto);
        return Collections.singletonMap("Token : ",token);
    }

    @PostMapping
    public String reset(@RequestParam String newPassword, @CurrentUser CurrentUserDto user) {
            return this.service.resetPassword(newPassword, user);
    }
}
