package freelance.new_syria_v2.auth.controller;

import java.util.Map;

import freelance.new_syria_v2.auth.annotaions.IsPublic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import freelance.new_syria_v2.auth.dto.LoginDto;
import freelance.new_syria_v2.auth.dto.LoginResponse;
import freelance.new_syria_v2.auth.dto.RegistrationDto;
import freelance.new_syria_v2.auth.service.LoginService;
import freelance.new_syria_v2.auth.service.RegistrationService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

	private final LoginService loginService;
	private final RegistrationService registrationService;

	private static final Logger LOGGER=LoggerFactory.getLogger(AuthController.class);
	
	public AuthController(LoginService loginService, RegistrationService registrationService) {
		super();
		this.loginService = loginService;
		this.registrationService = registrationService;
	}

	@PostMapping("/login")
    @IsPublic()
    public ResponseEntity<LoginResponse> login(@RequestBody()LoginDto dto) {
		return new ResponseEntity<LoginResponse>(this.loginService.login(dto),HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/register")
    @IsPublic()
    public String register(@Valid @RequestBody()RegistrationDto dto) {
		LOGGER.info("the email you try to register with Email: {} ",dto.getEmail());
		return this.registrationService.register(dto);
	}
	
	@GetMapping("/confirm")
    @IsPublic()
    public ResponseEntity<Void> confirmEmail(@PathParam("token")String token) {
		registrationService.confirmEmail(token);
        return ResponseEntity
                .status(302)
                .header("Location", "https://newsyria.news/login")
                .build();
	}


}
