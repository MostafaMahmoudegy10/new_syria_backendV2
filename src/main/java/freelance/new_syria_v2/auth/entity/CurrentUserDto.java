package freelance.new_syria_v2.auth.entity;

import java.util.UUID;

public record CurrentUserDto(UUID id, String username, String email,String role,String  numOfArticles) {}
