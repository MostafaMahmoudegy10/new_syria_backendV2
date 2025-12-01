package freelance.new_syria_v2.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import freelance.new_syria_v2.article.dto.CompleteProfileDto;
import freelance.new_syria_v2.article.entity.Image;
import freelance.new_syria_v2.article.utils.ImageUtil;
import freelance.new_syria_v2.auth.dto.UserDto;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.auth.repository.UserRepository;
import freelance.new_syria_v2.country.CountryService;
import freelance.new_syria_v2.country.entity.Country;
import freelance.new_syria_v2.exceptions.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final CountryService countryService;
	private final ImageUtil imageUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> isExisted = this.userRepository.findByEmail(email);
		if (isExisted.isPresent()) {
			User user = isExisted.get();
			LOGGER.debug("The user with email {} fetched from db", email);

			return new CustomUserDetails(user);
		}
		LOGGER.error("The email {} you tried to fetch is not present in db", email);
		return null;
	}

	@Transactional
	public User save(User user) {
		return this.userRepository.save(user);
	}

	public User findOptionalByEmail(String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
				"user name with " + email + " not found in the systme please enter a valid email"));
	}

	public boolean isPresent(String email) {
		return this.userRepository.findByEmail(email).isPresent();
	}

	public User findUser(UUID id) {
		return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("user with this id not found"));
	}

	public List<User> findAll() {
		return this.userRepository.findAll();
	}

	public void deleteUser(UUID id) {
		this.userRepository.deleteById(id);
	}

	public String completeProfile(UUID id, CompleteProfileDto dto) {
		// find user in db
		User user = this.findUser(id);
		boolean flag = false;

		if (dto.getPhoneNumber() != null) {
			user.setPhoneNumber(dto.getPhoneNumber());
			flag = true;
		}
		if (dto.getBio() != null) {
			user.setBio(dto.getBio());
			flag = true;
		}
		if (dto.getCountryName() != null) {
			user.setCountryName(dto.getCountryName());
			flag = true;
		}
		if (dto.getFile() != null) {
			Image image = imageUtil.from(dto.getFile());
			String imageUrl = imageUtil.imageUrl(image.getId());
			user.setImageUrl(imageUrl);
			flag = true;
		}
		if (flag) {
			user.setCompletedProfile(flag);
			this.userRepository.save(user);
			return "profile completed ";
		}

		throw new NotFoundException("please make sure to enter all the values of complete prof");
	}

	public static UserDto from() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		UserDto userDto = new UserDto(user.getUser().getId(), user.getUser().getEmail(),
				user.getUser().getRole().name(), user.getUser().getUserName());
		return userDto;
	}
}
