package freelance.new_syria_v2.article.service;

import freelance.new_syria_v2.article.entity.Article;
import freelance.new_syria_v2.article.repository.ArticleRepository;
import freelance.new_syria_v2.article.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ArticleAsyncService {

    private final ArticleRepository articleRepository;

    private final ImageRepository imageRepository;

    @Async
    @Transactional
    public void incrementViewsAsync(UUID id) {
        articleRepository.incrementViews(id);
    }
    @Async
    public void deleteImageAsync(String imageUrl) {
        UUID imageId= UUID.fromString(imageUrl.substring(imageUrl.lastIndexOf('/')+1));
        this.imageRepository.deleteById(imageId);
    }
}

