package com.wooteco.nolto.admin.application;

import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final JwtTokenProvider jwtTokenProvider;
    private final FeedService feedService;

    @Value("${admin.id}")
    private String adminId;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.payload}")
    private String adminPayload;

    public AdminLoginResponse login(AdminLoginRequest adminLoginRequest) {
        String id = adminLoginRequest.getId();
        String password = adminLoginRequest.getPassword();
        if (adminId.equals(id) && adminPassword.equals(password)) {
            return new AdminLoginResponse(jwtTokenProvider.createToken(adminPayload));
        }
        throw new UnauthorizedException(ErrorType.ADMIN_ONLY);
    }

    public User getAdminUser(String credentials) {
        String payload = jwtTokenProvider.getPayload(credentials);
        if (adminPayload.equals(payload)) {
            return User.ADMIN_USER;
        }
        throw new UnauthorizedException(ErrorType.ADMIN_ONLY);
    }

    public List<FeedCardResponse> findAllFeeds(User user) {
        return feedService.findAllAsAdmin(user);
    }

    public void deleteFeed(User user, Long feedId) {
        feedService.deleteFeedAsAdmin(user, feedId);
    }
}
