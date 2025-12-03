package freelance.new_syria_v2.article.dto;

import java.util.UUID;

public interface FiredTopicProjection {
    UUID getArticleId();
    String getArticleHeader();
    Long  getTotal();
}
