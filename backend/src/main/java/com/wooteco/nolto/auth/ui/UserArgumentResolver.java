package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.UserAuthenticationPrincipal;
import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.infrastructure.AuthorizationExtractor;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@AllArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserAuthenticationPrincipal.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String credentials = AuthorizationExtractor.extract(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        if(Objects.isNull(credentials)) {
            return User.GUEST_USER;
        }
        User findMember = authService.findUserByToken(credentials);
        return findMember;
    }
}
