package freelance.new_syria_v2.article.repository;

import java.util.Map;
import java.util.UUID;

import freelance.new_syria_v2.article.dto.ArticleFilterStatus;
import freelance.new_syria_v2.article.dto.LatestNewsDto;
import freelance.new_syria_v2.article.schdeular.repository.MonthlyStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.entity.Status;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, UUID>
        , JpaSpecificationExecutor<Article> {

	// find aricles by status for the admon
	@Query("""
            select  new freelance.new_syria_v2.article.dto.ArticleFilterStatus(a.id,a.header,a.createdAt,a.status,size(a.comments)) 
            from Article a 
            where a.status=:status            
            """)
	Page<ArticleFilterStatus> findByStatus(Status status, Pageable pageable);

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

    @Query(
            value = """
            SELECT 
                COUNT(a.id) AS totalPosts,
                COALESCE(SUM(c.comment_count), 0) AS totalComments
            FROM article a
            LEFT JOIN (
                SELECT article_id, COUNT(*) AS comment_count
                FROM comment
                GROUP BY article_id
            ) c ON a.id = c.article_id
            WHERE EXTRACT(YEAR FROM a.created_at) = :year
            AND EXTRACT(MONTH FROM a.created_at) = :month
        """, nativeQuery = true
    )
    MonthlyStatsProjection getStatsForMonth(@Param("year") int year, @Param("month") int month);

}
