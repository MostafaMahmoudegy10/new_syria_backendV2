package freelance.new_syria_v2.article.dto;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CompleteProfileDto {

	private String countryName;

	private String phoneNumber;

	private String bio;

	@Nullable
	private MultipartFile file;

}
