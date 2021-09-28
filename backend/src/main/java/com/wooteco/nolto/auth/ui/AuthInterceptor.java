package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.infrastructure.AuthorizationExtractor;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return isOptionsMethod(request) || isHandlerMethod(handler) || validTokenRequired(handler) || validateToken(request);
    }

    public boolean isOptionsMethod(HttpServletRequest request) {
        return request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name());
    }

    public boolean isHandlerMethod(Object handler) {
        return !(handler instanceof HandlerMethod);
    }

    public boolean validTokenRequired(Object handler) {
        ValidTokenRequired validTokenRequired = ((HandlerMethod) handler).getMethodAnnotation(ValidTokenRequired.class);
        return Objects.isNull(validTokenRequired);
    }

    public boolean validateToken(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request);
        if (Objects.isNull(token)) {
            throw new UnauthorizedException(ErrorType.TOKEN_NEEDED);
        }
        authService.validateToken(token);
        return true;
    }
}
