package freelance.new_syria_v2.article.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.controller.ArticleController;
import freelance.new_syria_v2.article.dto.FiredTopicProjection;
import freelance.new_syria_v2.article.dto.LatestNewsDto;
import freelance.new_syria_v2.article.repository.ArticleCustomRepository;
import freelance.new_syria_v2.article.schdeular.entity.MonthlyReport;
import freelance.new_syria_v2.article.schdeular.repository.MonthlyReportRepository;
import freelance.new_syria_v2.auth.entity.ArticleUserDto;
import jakarta.persistence.criteria.JoinType;
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

    private final ArticleCustomRepository customRepository;

    private final MonthlyReportRepository monthlyReportRepository;

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

    @Transactional
    public Page<ArticleController.ArticleFilterDto> findArticles(ArticleFilter filter, int page, int size) {
       Pageable pageable = PageRequest.of(page, size);
        System.out.println(filter.categoryName);
       return this.customRepository.findAllFiltered(filter, pageable);
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

    public List<MonthlyReport> statsMonthly(){
        return this.monthlyReportRepository.findAllByOrderByYearAscMonthAsc();
    }

    @Transactional
    public long incrementLike(UUID id) {
        int updated = articleRepository.incrementLikes(id);

        if (updated == 0) {
            throw new RuntimeException("Article not found");
        }

        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"))
                .getReacts();
    }
    public Page<FiredTopicProjection> getRankedArticles(int page, int size) {
        Page<FiredTopicProjection> res = articleRepository.findFiredTopics(PageRequest.of(page, size));
        return res;
    }
}
