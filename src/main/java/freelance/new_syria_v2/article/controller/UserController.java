package freelance.new_syria_v2.article.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.service.ArticleMangment;
import freelance.new_syria_v2.auth.annotaions.CurrentUser;
import freelance.new_syria_v2.auth.entity.ArticleUserDto;
import freelance.new_syria_v2.auth.entity.CurrentUserDto;
import freelance.new_syria_v2.auth.publicendpoints.CurrentUserArgumentResolver;
import freelance.new_syria_v2.auth.service.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import freelance.new_syria_v2.article.dto.CompleteProfileDto;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.auth.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {
	private final CustomUserDetailsService userService;

    private final ArticleMangment articleMangment;

	@GetMapping()
	public ResponseEntity<User> findUser(@CurrentUser CurrentUserDto currentUser) {
		User user = this.userService.findUser(currentUser.id());
		return ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	@DeleteMapping()
	public ResponseEntity<String> deleteUser(@CurrentUser CurrentUserDto currentUser) {
		this.userService.deleteUser(currentUser.id());
		return ResponseEntity.ok("user deleted succefully");
	}

	@PostMapping("/complete-profile")
	public ResponseEntity<String> completeProfile(@ModelAttribute()CompleteProfileDto dto,
                                                  @CurrentUser CurrentUserDto currentUser) {
		 this.userService.completeProfile(currentUser.id(), dto);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("user profile has completed");
	}
    @GetMapping("/articles")
    public Page<ArticleUserDto> userArticles(
        @CurrentUser CurrentUserDto currentUserDto,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        return this.articleMangment.findUserArticles(currentUserDto.id(),page,size);
    }

    @GetMapping("/articles/status-count")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String,Long> getStatusCount() {
        return this.articleMangment.getUserArticleStatus();
    }
}
