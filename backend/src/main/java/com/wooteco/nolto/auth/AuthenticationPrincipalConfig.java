package com.wooteco.nolto.auth;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.AuthInterceptor;
import com.wooteco.nolto.auth.ui.MemberArgumentResolver;
import com.wooteco.nolto.auth.ui.UserArgumentResolver;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalMemberArgumentResolver());
        argumentResolvers.add(createAuthenticationPrincipalUserArgumentResolver());
    }

    @Bean
    public MemberArgumentResolver createAuthenticationPrincipalMemberArgumentResolver() {
        return new MemberArgumentResolver(authService);
    }

    @Bean
    public UserArgumentResolver createAuthenticationPrincipalUserArgumentResolver() {
        return new UserArgumentResolver(authService);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(authService);
    }
}
