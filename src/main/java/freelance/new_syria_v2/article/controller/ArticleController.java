package freelance.new_syria_v2.article.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.dto.ArticleFilterStatus;
import freelance.new_syria_v2.article.dto.FiredTopicProjection;
import freelance.new_syria_v2.article.dto.LatestNewsDto;
import freelance.new_syria_v2.article.schdeular.entity.MonthlyReport;
import freelance.new_syria_v2.auth.annotaions.CurrentUser;
import freelance.new_syria_v2.auth.annotaions.IsPublic;
import freelance.new_syria_v2.auth.entity.CurrentUserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import freelance.new_syria_v2.article.dto.ArticleDto;
import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.entity.Comment;
import freelance.new_syria_v2.article.entity.Status;
import freelance.new_syria_v2.article.service.ArticleMangment;
import freelance.new_syria_v2.article.service.ArticleMangment.ArticleFilter;
import freelance.new_syria_v2.article.service.ArticleMangment.commentDto;
import freelance.new_syria_v2.article.service.ArticleService;
import freelance.new_syria_v2.auth.service.CustomUserDetails;
import lombok.AllArgsConstructor;

@RestController()
@RequestMapping("/articles")
@AllArgsConstructor
public class ArticleController {
	public record ArticleCreated(UUID id, String imageUrl, String categoryName, String bio, String header,
			Status status) { }
    public record ArticleFilterDto(UUID id, String header
            , String imageUrl, String userImageUrl, LocalDate createdAt,String bio,String categoryName,String userName){}

    private final ArticleService service;
	private final ArticleMangment articleMangment;

	// make an article for user or admin
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public String save(@ModelAttribute("dto") ArticleDto dto, @CurrentUser CurrentUserDto user) {
		ArticleCreated res = this.service.save(dto,user);
		return "article maked successfully";
	}

	// admin can review the post and make it approved or rejected
	@PutMapping("/{id}/review")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> reviewPosts(@PathVariable("id") UUID id, @RequestParam("status") boolean status) {
		String res = this.service.reviewPosts(id, status);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	// for the admin to show what he approved and what no
	@GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ArticleFilterStatus>> findAllByStatus(
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "status", required = false) String status,
			@RequestParam(name = "sort", defaultValue = "desc", required = false) String sort) {
		Status statusOfArticle = Status.from(status);
		Page<ArticleFilterStatus> articles = this.service.findByStatus(statusOfArticle, page, size, sort);
		return ResponseEntity.ok(articles);
	}

	// find an article by id
	@GetMapping("{id}")
    @IsPublic()
	public ResponseEntity<Article> findArticleById(@PathVariable("id") UUID id) {
		Article article = this.service.findById(id);
		return ResponseEntity.ok(article);
	}


	@PostMapping("/comments/{articleId}")
	public ResponseEntity<commentDto> addComment(@PathVariable("articleId") UUID articleId,
			@RequestParam("commentContent") String commentContent,
			@AuthenticationPrincipal CustomUserDetails currentUser) {
		commentDto commentDto = this.articleMangment.createComment(articleId, currentUser.getUser().getId(),
				commentContent);
		return ResponseEntity.ok(commentDto);
	}

	@GetMapping("/comments/{articleId}")
    @IsPublic()
	public ResponseEntity<Page<Comment>> commentsOfArticle(@PathVariable("articleId") UUID articleId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Page<Comment> commentsByArticle = this.articleMangment.getCommentsByArticle(articleId, page, size);
		return ResponseEntity.ok(commentsByArticle);
	}

	@PostMapping("/filter")
    @IsPublic()
	public Page<ArticleFilterDto> filterArticles(@ModelAttribute ArticleFilter filter,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		return this.articleMangment.findArticles(filter, page, size);
	}

    @GetMapping("/latest_news")
    @IsPublic()
    public Page<LatestNewsDto> getLatestNews(@RequestParam(defaultValue = "0") int page,
     @RequestParam(defaultValue = "5") int size) {

       return this.articleMangment.getLatestNews(page, size);
    }

    @GetMapping("/stats/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>statsMonthly(){
       List<MonthlyReport> list= this.articleMangment.statsMonthly();
        return ResponseEntity.ok(list);
    }

   @PostMapping("/{id}/likes")
   @PreAuthorize("hasAnyRole('ADMIN','USER')")
   public ResponseEntity<Map<String,Long>> likedArticle(@PathVariable("id") UUID id){
       Map<String,Long> map= Map.of("likes",this.articleMangment.incrementLike(id));
        return ResponseEntity.ok(map);
    }
    @PostMapping("/{id}/unlike")
    public ResponseEntity<?> unlikedArticle(@PathVariable("id") UUID id){
        this.articleMangment.decrementLike(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ranked")
    @IsPublic()
    public Page<FiredTopicProjection> getRankedArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return this.articleMangment.getRankedArticles(page, size);
    }
    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") long commentId) {
        this.articleMangment.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{articleId}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<?> deleteArticle(@PathVariable("articleId") UUID articleId,@CurrentUser CurrentUserDto dto) {
        this.articleMangment.deleteArticle(articleId,dto.id());
        return ResponseEntity.ok().build();
    }

}
