package freelance.new_syria_v2.article.repository;

import java.util.UUID;

import freelance.new_syria_v2.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import freelance.new_syria_v2.article.entity.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	Page<Comment> findByArticleId(UUID articleId, Pageable pageable);
}
