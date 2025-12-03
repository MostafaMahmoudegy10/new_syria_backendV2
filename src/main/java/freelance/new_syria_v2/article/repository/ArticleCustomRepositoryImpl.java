package freelance.new_syria_v2.article.repository;
import freelance.new_syria_v2.article.controller.ArticleController;
import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.service.ArticleMangment;
import freelance.new_syria_v2.auth.entity.User;
import freelance.new_syria_v2.categories.entitiy.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleCustomRepositoryImpl implements ArticleCustomRepository {

    @PersistenceContext
    private EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(ArticleCustomRepositoryImpl.class);

    @Override
    public Page<ArticleController.ArticleFilterDto> findAllFiltered(ArticleMangment.ArticleFilter filter, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();

        // ========= SELECT =========
        CriteriaQuery<ArticleController.ArticleFilterDto> query = cb.createQuery(ArticleController.ArticleFilterDto.class);
        Root<Article> root = query.from(Article.class);

        Join<Article, User> user = root.join("user", JoinType.INNER);
        Join<Article, Category> category = root.join("category", JoinType.INNER);

        // projection + constructor mapping
        query.select(cb.construct(
                ArticleController.ArticleFilterDto.class,
                root.get("id"),
                root.get("header"),
                root.get("imageUrl"),
                user.get("imageUrl"),
                root.get("createdAt"),
                root.get("bio"),
                category.get("name"),
                user.get("userName")
        ));

        // ========= WHERE =========
        List<Predicate> predicates = new ArrayList<>();

        if (filter.categoryName() != null && !filter.categoryName().isEmpty()) {
            predicates.add(cb.equal(category.get("name"), filter.categoryName()));
        }

        predicates.add(cb.equal(root.get("status"), "APPROVED"));

        if (filter.startDate() != null && filter.endDate() != null) {
            predicates.add(cb.between(root.get("createdAt"), filter.startDate(), filter.endDate()));
        }


        query.where(predicates.toArray(new Predicate[0]));

        // ========= SORT =========
        query.orderBy(cb.desc(root.get("createdAt")));

        // ========= EXECUTE =========
        var typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<ArticleController.ArticleFilterDto> resultList = typedQuery.getResultList();

        // ========= COUNT =========
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Article> countRoot = countQuery.from(Article.class);
        Join<Article, User> countUser = countRoot.join("user", JoinType.INNER);

        List<Predicate> countPredicates = new ArrayList<>();

        if (filter.categoryName() != null && !filter.categoryName().isEmpty()) {
            Join<Article, Category> countCategory = countRoot.join("category", JoinType.INNER);
            countPredicates.add(cb.equal(countCategory.get("name"), filter.categoryName()));
        }

        if (filter.status() != null) {
            countPredicates.add(cb.equal(countRoot.get("status"), filter.status()));
        }

        if (filter.startDate() != null && filter.endDate() != null) {
            countPredicates.add(cb.between(countRoot.get("createdAt"), filter.startDate(), filter.endDate()));
        }


        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }


}
