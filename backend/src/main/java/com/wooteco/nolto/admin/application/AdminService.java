package com.wooteco.nolto.admin.application;

import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.admin.ui.dto.UserResponse;
import com.wooteco.nolto.auth.infrastructure.JwtTokenProvider;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.notification.application.NotificationCommentDeleteEvent;
import com.wooteco.nolto.notification.application.NotificationFeedDeleteEvent;
import com.wooteco.nolto.notification.domain.NotificationRepository;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ImageService imageService;
    private final FeedRepository feedRepository;
    private final FeedTechRepository feedTechRepository;
    private final UserRepository userRepository;
    private final TechRepository techRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

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
            return new AdminLoginResponse(jwtTokenProvider.createToken(adminPayload).getValue());
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

    @Transactional(readOnly = true)
    public List<FeedResponse> findAllFeeds(User adminUser) {
        adminUser.validateAdmin();
        return FeedResponse.toList(feedRepository.findAllWithFetchJoin());
    }

    public void updateFeed(User adminUser, Long feedId, FeedRequest request) {
        adminUser.validateAdmin();
        Feed findFeed = getFeed(feedId);
        updateFeedTech(request, findFeed);
        findFeed.update(
                request.getTitle(),
                request.getContent(),
                Step.of(request.getStep()),
                request.isSos(),
                request.getStorageUrl(),
                request.getDeployedUrl()
        );
        updateFeedImage(request, findFeed);
    }

    private void updateFeedTech(FeedRequest request, Feed findFeed) {
        List<FeedTech> feedTechs = findFeed.getFeedTechs();
        feedTechRepository.deleteAll(feedTechs);
        feedTechs.clear();
        findFeed.changeTechs(techRepository.findAllById(request.getTechs()));
    }

    private void updateFeedImage(FeedRequest request, Feed findFeed) {
        if (imageService.isEmpty(request.getThumbnailImage())) {
            return;
        }
        String updateThumbnailUrl = imageService.update(findFeed.getThumbnailUrl(), request.getThumbnailImage(), ImageKind.FEED);
        findFeed.changeThumbnailUrl(updateThumbnailUrl);
    }

    public void deleteFeed(User adminUser, Long feedId) {
        adminUser.validateAdmin();
        Feed findFeed = getFeed(feedId);
        applicationEventPublisher.publishEvent(new NotificationFeedDeleteEvent(findFeed));
        feedRepository.delete(findFeed);
    }

    private Feed getFeed(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException(ErrorType.FEED_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers(User adminUser) {
        adminUser.validateAdmin();
        return UserResponse.toList(userRepository.findAll());
    }

    public void deleteUser(User adminUser, Long userId) {
        adminUser.validateAdmin();
        User findUser = getUser(userId);
        notificationRepository.deleteAllByListener(findUser);
        notificationRepository.deleteAllByPublisher(findUser);
        userRepository.delete(findUser);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorType.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<CommentsByFeedResponse> findAllComments(User adminUser) {
        adminUser.validateAdmin();
        return CommentsByFeedResponse.toList(feedRepository.findAllFeedsHavingComments());
    }

    public void deleteComment(User adminUser, Long commentId) {
        adminUser.validateAdmin();
        Comment findComment = getComment(commentId);
        if (findComment.isParentComment()) {
            applicationEventPublisher.publishEvent(new NotificationCommentDeleteEvent(findComment));
        }
        commentRepository.delete(findComment);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorType.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<TechResponse> findAllTechs(User adminUser) {
        adminUser.validateAdmin();
        return TechResponse.toList(techRepository.findAll());
    }

    public void deleteTech(User adminUser, Long techId) {
        adminUser.validateAdmin();
        techRepository.deleteById(techId);
    }
}
