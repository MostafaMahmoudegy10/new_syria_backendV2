package freelance.new_syria_v2.article.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.dto.LatestNewsDto;
import freelance.new_syria_v2.auth.entity.ArticleUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.entity.Comment;
import freelance.new_syria_v2.article.entity.Status;
import freelance.new_syria_v2.article.repository.ArticleRepository;
import freelance.new_syria_v2.article.repository.CommentRepository;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.auth.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArticleMangment {
	public record commentDto(String commentContent, Status commentStatus) {
	}

	public record ArticleFilter(String categoryName, Status status, LocalDate startDate, LocalDate endDate) {
	}

	private final CommentRepository repository;

	private final ArticleService articleService;

	private final ArticleRepository articleRepository;

	private final CustomUserDetailsService userService;

	@Transactional
	public commentDto createComment(UUID articleId, UUID userId, String commentContent) {
		// Fetch the Article
		Article article = this.articleService.findById(articleId);

		// fetch the user
		User user = userService.findUser(userId);

		Comment comment = new Comment();
		// connect the comment to article
		comment.setArticle(article);
		// connect the user with article
		comment.setUser(user);
		// set time
		comment.setCommentDate(LocalDateTime.now());
		// SET status
		comment.setCommentStatus(Status.PENDING);
		// set content
		comment.setCommentContent(commentContent);
		// create comment entity
		Comment saveComment = this.repository.save(comment);

		return new commentDto(saveComment.getCommentContent(), saveComment.getCommentStatus());
	}

	public Page<Comment> getCommentsByArticle(UUID articleId, int page, int size) {
		// make sure article is present
		articleService.findById(articleId);
		// pagenation
		Pageable pageable = PageRequest.of(page, size, Sort.by("commentDate").descending());
		return repository.findByArticleId(articleId, pageable);
	}

	@Transactional()
	public Page<Article> findArticles(ArticleFilter filter, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

		Specification<Article> spec = Specification.where(null);

		if (filter.categoryName() != null && !filter.categoryName().isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("name"), filter.categoryName()));
		}

		if (filter.status() != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), filter.status()));
		}

		if (filter.startDate() != null && filter.endDate() != null) {
			spec = spec
					.and((root, query, cb) -> cb.between(root.get("createdAt"), filter.startDate(), filter.endDate()));
		}

		return articleRepository.findAll(spec, pageable);
	}

    public Page<ArticleUserDto> findUserArticles(UUID userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Article> articles = this.articleRepository.findByUser_Id(userId, pageable);

        Page<ArticleUserDto> dtoPage = articles.map(article -> {
            ArticleUserDto dto = new ArticleUserDto();
            dto.setImageUrl(article.getImageUrl());
            dto.setBio(article.getBio());
            dto.setCreatedAt(article.getCreatedAt());
            dto.setHeader(article.getHeader());
            dto.setArticleId(article.getId());
            return dto;
        });

        return dtoPage;
    }

    public Map<String, Long> getUserArticleStatus() {
        return articleRepository.countStatusByUser();
    }
    public Page<LatestNewsDto> getLatestNews(int page, int size) {
        //make the paggenation object
        Pageable pageable = PageRequest.of(page,size);

        return  this.articleRepository.getArticleWithCommentCountLatestNews(pageable);
    }
}
