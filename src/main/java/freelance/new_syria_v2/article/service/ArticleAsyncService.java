package freelance.new_syria_v2.article.service;

import freelance.new_syria_v2.article.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ArticleAsyncService {

    private final ArticleRepository articleRepository;

    public ArticleAsyncService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Async
    @Transactional
    public void incrementViewsAsync(UUID id) {
        articleRepository.incrementViews(id);
    }
}

