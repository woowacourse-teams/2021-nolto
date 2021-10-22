package com.wooteco.nolto.admin.application;

import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.admin.ui.dto.UserResponse;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.application.CommentService;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.application.MemberService;
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
    private final MemberService memberService;
    private final CommentService commentService;
    private final TechRepository techRepository;

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

    public List<FeedResponse> findAllFeeds(User adminUser) {
        return feedService.findAllFeedsAsAdmin(adminUser);
    }

    public void updateFeed(User adminUser, Long feedId, FeedRequest request) {
        feedService.updateFeedAsAdmin(adminUser, feedId, request);
    }

    public void deleteFeed(User adminUser, Long feedId) {
        feedService.deleteFeedAsAdmin(adminUser, feedId);
    }

    public List<UserResponse> findAllUsers(User adminUser) {
        return memberService.findAllUsersAsAdmin(adminUser);
    }

    public void deleteUser(User adminUser, Long userId) {
        memberService.deleteUserAsAdmin(adminUser, userId);
    }

    public List<CommentsByFeedResponse> findAllComments(User adminUser) {
        return feedService.findAllCommentsByFeedAsAdmin(adminUser);
    }

    public void deleteComment(User adminUser, Long commentId) {
        commentService.deleteCommentAsAdmin(adminUser, commentId);
    }

    public List<TechResponse> findAllTechs(User adminUser) {
        adminUser.validateAdmin();
        List<Tech> allTechs = techRepository.findAll();
        return TechResponse.toList(allTechs);
    }

    public void deleteTech(User adminUser, Long techId) {
        adminUser.validateAdmin();
        techRepository.deleteById(techId);
    }
}
