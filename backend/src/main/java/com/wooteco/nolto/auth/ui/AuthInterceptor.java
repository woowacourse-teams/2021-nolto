package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.infrastructure.AuthorizationExtractor;
import lombok.AllArgsConstructor;
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
        String token = AuthorizationExtractor.extract(request);
        request.setAttribute("credential", token);
        if (Objects.isNull(token)) {
            return true;
        }
        authService.validateToken(token);
        return true;
    }
}
