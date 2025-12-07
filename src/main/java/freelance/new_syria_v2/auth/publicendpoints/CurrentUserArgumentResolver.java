package freelance.new_syria_v2.auth.publicendpoints;

import freelance.new_syria_v2.article.repository.ArticleRepository;
import freelance.new_syria_v2.auth.annotaions.CurrentUser;
import freelance.new_syria_v2.auth.entity.CurrentUserDto;
import freelance.new_syria_v2.auth.repository.UserRepository;
import freelance.new_syria_v2.auth.service.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@AllArgsConstructor
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final ArticleRepository repository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null &&
                parameter.getParameterType().equals(CurrentUserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        CustomUserDetails cud=(CustomUserDetails) authentication.getPrincipal();

        String numOfArticles=this.repository.countAllArticlesByUserId(cud.getUser().getId());

        return new CurrentUserDto(
                cud.getUser().getId(),
                cud.getUser().getUserName(),
                cud.getUser().getEmail(),
                cud.getUser().getRole().name(),
                numOfArticles
        );
    }
}

