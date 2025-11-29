package freelance.new_syria_v2.article.repository;

import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.controller.ArticleController;
import freelance.new_syria_v2.article.dto.LatestNewsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.entity.Status;

public interface ArticleRepository extends JpaRepository<Article, UUID>
        , JpaSpecificationExecutor<Article> {

	// find aricles by status for the admon
	@Query("select a from Article a where a.status=:status")
	Page<Article> findByStatus(Status status, Pageable pageable);

	Page<Article> findByCategory_NameAndStatus(String name, Status status, Pageable pageable);

    @Query("select a from Article a where a.user.id=:userId")
    Page<Article> findByUser_Id(UUID userId, Pageable pageable);

    @Query(value = """
        select 
                Sum(case when status= 'APPROVED' then 1 else 0 end) as accepted,
                SUM(case when status= 'REJECTED' then 1 else 0 end) as rejected,
                SUM(case when status= 'PENDING'  then 1 else 0 end) as pending,
                count(*) as total
        from article 
    """,nativeQuery = true)
    Map<String,Long> countStatusByUser();

        @Query("""
            SELECT new freelance.new_syria_v2.article.dto.LatestNewsDto(
                    a.id, a.header, COUNT(c),a.category.name,a.createdAt)
            FROM Article a
            LEFT JOIN a.comments c
            where a.status='APPROVED'
            GROUP BY a.id, a.header,a.category.name
            ORDER BY a.createdAt DESC
            
        """)
    Page<LatestNewsDto> getArticleWithCommentCountLatestNews(Pageable pageable);


}
