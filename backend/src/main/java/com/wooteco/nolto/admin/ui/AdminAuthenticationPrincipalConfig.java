package com.wooteco.nolto.admin.ui;

import com.wooteco.nolto.admin.application.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AdminAuthenticationPrincipalConfig implements WebMvcConfigurer {

    private final AdminService adminService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalAdminArgumentResolver());
    }

    @Bean
    public AdminArgumentResolver createAuthenticationPrincipalAdminArgumentResolver() {
        return new AdminArgumentResolver(adminService);
    }
}
