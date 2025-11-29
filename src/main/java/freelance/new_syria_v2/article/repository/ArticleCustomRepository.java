package freelance.new_syria_v2.article.repository;

import freelance.new_syria_v2.article.controller.ArticleController;
import freelance.new_syria_v2.article.service.ArticleMangment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCustomRepository {
    Page<ArticleController.ArticleFilterDto>
    findAllFiltered(ArticleMangment.ArticleFilter filter, Pageable pageable);
}