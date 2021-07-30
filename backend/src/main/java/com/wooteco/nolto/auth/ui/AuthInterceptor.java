package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.infrastructure.AuthorizationExtractor;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import lombok.AllArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        ValidTokenRequired validTokenRequired = ((HandlerMethod) handler).getMethodAnnotation(ValidTokenRequired.class);
        if (Objects.isNull(validTokenRequired)) {
            return true;
        }

        String token = AuthorizationExtractor.extract(request);
        if (Objects.isNull(token)) {
            throw new UnauthorizedException(ErrorType.TOKEN_NEEDED);
        }
        authService.validateToken(token);
        return true;
    }
}
