package freelance.new_syria_v2.article.dto;

import java.util.UUID;

public interface FiredTopicProjection {
    UUID getId();
    String getHeader();
    Long getTotalViews();
}
