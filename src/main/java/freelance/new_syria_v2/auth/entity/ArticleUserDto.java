package freelance.new_syria_v2.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticleUserDto {
    private String imageUrl;
    private LocalDate createdAt;
    private String header;
    private String bio;
    private UUID articleId;
}
