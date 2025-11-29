package freelance.new_syria_v2.article.dto;

import freelance.new_syria_v2.categories.entitiy.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LatestNewsDto {
    private UUID id;
    private String header;
    private Long commentCount;
    private String categoryName;
    private LocalDate createdAt;
}
