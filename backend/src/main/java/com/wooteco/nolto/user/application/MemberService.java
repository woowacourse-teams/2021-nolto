package com.wooteco.nolto.user.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.image.application.ImageKind;
import com.wooteco.nolto.image.application.ImageService;
import com.wooteco.nolto.notification.application.NotificationService;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.wooteco.nolto.exception.ErrorType.ALREADY_EXIST_NICKNAME;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private final ImageService imageService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public MemberHistoryResponse findHistory(User user) {
        List<Feed> likedFeeds = user.findLikedFeeds();
        likedFeeds.sort(Comparator.comparing(Feed::getCreatedDate, Comparator.reverseOrder()).thenComparing(Feed::getId, Comparator.reverseOrder()));
        List<Feed> myFeeds = user.getFeeds();
        myFeeds.sort(Comparator.comparing(Feed::getCreatedDate, Comparator.reverseOrder()).thenComparing(Feed::getId, Comparator.reverseOrder()));
        List<Comment> myComments = user.getComments();
        myComments.sort(Comparator.comparing(Comment::getCreatedDate, Comparator.reverseOrder()).thenComparing(Comment::getId, Comparator.reverseOrder()));
        return MemberHistoryResponse.of(likedFeeds, myFeeds, myComments);
    }

    public NicknameValidationResponse validateDuplicated(String nickname) {
        boolean isExistNickname = userRepository.existsByNickName(nickname);
        return new NicknameValidationResponse(!isExistNickname);
    }

    public ProfileResponse findProfile(User user) {
        long notificationCount = notificationService.findNotificationCount(user);
        return ProfileResponse.of(user, notificationCount);
    }

    public ProfileResponse updateProfile(User user, ProfileRequest request) {
        String nicknameToChange = request.getNickname();
        if (!user.sameAsNickname(nicknameToChange) && userRepository.existsByNickName(nicknameToChange)) {
            throw new BadRequestException(ALREADY_EXIST_NICKNAME);
        }
        updateIfImageExist(request, user);
        user.updateProfile(nicknameToChange, request.getBio());
        return ProfileResponse.of(user, 0);
    }

    private void updateIfImageExist(ProfileRequest request, User user) {
        if (imageService.isEmpty(request.getImage())) return;

        String updateImageUrl = imageService.update(user.getImageUrl(), request.getImage(), ImageKind.USER);
        user.changeImageUrl(updateImageUrl);
    }

    public List<NotificationResponse> findNotifications(User user) {
        List<Notification> notifications = notificationService.findAllByUser(user);
        notifications.sort(Comparator.comparing(Notification::getCreatedDate, Comparator.reverseOrder()).thenComparing(Notification::getId, Comparator.reverseOrder()));
        return NotificationResponse.toList(notifications);
    }

    public void deleteNotification(User user, Long notificationId) {
        notificationService.delete(user, notificationId);
    }

    public void deleteAllNotifications(User user) {
        notificationService.deleteAll(user);
    }

    public MemberResponse findMemberOfMine(User user) {
        long notificationCount = notificationService.findNotificationCount(user);
        return MemberResponse.of(user, notificationCount);
    }
}
