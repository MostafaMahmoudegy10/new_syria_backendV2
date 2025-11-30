package freelance.new_syria_v2.article.dto;

import freelance.new_syria_v2.article.entity.Status;

import java.time.LocalDate;
import java.util.UUID;

public record  ArticleFilterStatus(UUID id, String header , LocalDate createdAt, Status status, int count){}
