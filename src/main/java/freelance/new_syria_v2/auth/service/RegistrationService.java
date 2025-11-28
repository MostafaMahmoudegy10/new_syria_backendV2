package freelance.new_syria_v2.auth.service;

import freelance.new_syria_v2.auth.email.EmailBuilder;
import freelance.new_syria_v2.auth.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import freelance.new_syria_v2.auth.dto.RegistrationDto;
import freelance.new_syria_v2.auth.email.BrevoEmailService;
import freelance.new_syria_v2.auth.entity.User;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final CustomUserDetailsService userService;
    private final PasswordEncoder encoder;
    private final BrevoEmailService emailService;
    private final JwtUtils  jwtUtils;

    @Value("${spring.app.servername}")
    private  String serverLink;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    public String register(RegistrationDto registerDto) {
    	boolean present = this.userService.isPresent(registerDto.getEmail());

    	if(present) {
    		throw new BadCredentialsException("Email you try to register with already in the system");
    	}

        // Create new user
        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setUserName(registerDto.getUserName());
        User savedUser=userService.save(user);

        CustomUserDetails userUnEnabled = (CustomUserDetails) this.userService.loadUserByUsername(savedUser.getEmail());
        //generate token and send it with email
        var token=this.jwtUtils.generateToken(userUnEnabled);

       String url = serverLink + "/auth/confirm?token=" + token.token();
//     //send email to vefy user
	  emailService.sendEmail(registerDto.getEmail(),registerDto.getUserName(),"verfecation email"
              , EmailBuilder.registerationEmail(registerDto.getEmail(),registerDto.getUserName(),url) );

      LOGGER.info("New user registered with email: {}", savedUser.getEmail());

      return savedUser.getEmail();
    }

    public String confirmEmail(String unConfirmedToken) {

       boolean isConfirmed = this.jwtUtils.validateToken(unConfirmedToken);
       if(isConfirmed) {
        String email=jwtUtils.getEmailFromToken(unConfirmedToken);
          CustomUserDetails user=(CustomUserDetails) this.userService.loadUserByUsername(email);
          User savedUser=user.getUser();
          savedUser.setEnabled(true);
          this.userService.save(savedUser);
          return email;
       }
       return "Your emai UnConfirmed";
    }

}
