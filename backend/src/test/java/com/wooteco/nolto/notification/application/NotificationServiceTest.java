package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.application.CommentService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.notification.domain.Notification;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TestTransaction;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class NotificationServiceTest {

    @Autowired
    public FeedRepository feedRepository;
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public LikeService likeService;
    @Autowired
    public NotificationService notificationService;
    @Autowired
    public CommentService commentService;

    public User 찰리;
    public User 포모;
    public Feed 좋아요를_받을_찰리가_쓴_피드;
    public Feed 댓글이_달릴_찰리가_쓴_피드;
    public Feed 도움_댓글이_달릴_찰리가_쓴_피드;

    @BeforeTransaction
    public void setUp() {
        찰리 = new User("SOCIAL_ID", SocialType.GITHUB, "찰리", "IMAGE");
        포모 = new User("SOCIAL_ID2", SocialType.GITHUB, "포모", "IMAGE2");
        userRepository.save(찰리);
        userRepository.save(포모);

        좋아요를_받을_찰리가_쓴_피드 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(찰리);
        댓글이_달릴_찰리가_쓴_피드 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(찰리);
        도움_댓글이_달릴_찰리가_쓴_피드 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(찰리);
        feedRepository.saveAll(Arrays.asList(좋아요를_받을_찰리가_쓴_피드, 댓글이_달릴_찰리가_쓴_피드, 도움_댓글이_달릴_찰리가_쓴_피드));
    }

    @DisplayName("피드에 좋아요를 받은 경우 알림을 저장한다.")
    @Test
    void saveWhenFeedLike() {
        // when
        likeService.addLike(포모, 좋아요를_받을_찰리가_쓴_피드.getId());
        TestTransaction.flagForCommit();
        TestTransaction.end();  // 트랜잭션 강제 종료시켜버리기!

        // then
        TestTransaction.start();
        long notificationCount = notificationService.findNotificationCount(찰리);
        List<Notification> notifications = notificationService.findAllByUser(찰리);
        assertThat(notificationCount).isOne();
        알림이_같은지_조회한다(notifications.get(0), 포모, 좋아요를_받을_찰리가_쓴_피드.getId(), NotificationType.LIKE);
    }

    @DisplayName("피드에 댓글을 받은 경우 알림을 저장한다.")
    @Test
    void saveWhenComment() {
        // when
        commentService.createComment(포모, 댓글이_달릴_찰리가_쓴_피드.getId(), new CommentRequest("잘 보고 갑니다!", false));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        TestTransaction.start();
        long notificationCount = notificationService.findNotificationCount(찰리);
        List<Notification> notifications = notificationService.findAllByUser(찰리);
        assertThat(notificationCount).isOne();
        알림이_같은지_조회한다(notifications.get(0), 포모, 댓글이_달릴_찰리가_쓴_피드.getId(), NotificationType.COMMENT);
    }

    @DisplayName("피드에 도움 댓글을 받은 경우 알림을 저장한다.")
    @Test
    void saveWhenHelperComment() {
        // when
        commentService.createComment(포모, 도움_댓글이_달릴_찰리가_쓴_피드.getId(), new CommentRequest("잘 보고 갑니다!", true));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        // then
        TestTransaction.start();
        long notificationCount = notificationService.findNotificationCount(찰리);
        List<Notification> notifications = notificationService.findAllByUser(찰리);
        assertThat(notificationCount).isOne();
        알림이_같은지_조회한다(notifications.get(0), 포모, 도움_댓글이_달릴_찰리가_쓴_피드.getId(), NotificationType.COMMENT_SOS);
    }

    private void 알림이_같은지_조회한다(Notification response, User publisher, Long feedId, NotificationType type) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getPublisher().getId()).isEqualTo(publisher.getId());
        assertThat(response.getNotificationType()).isEqualTo(type);
        assertThat(response.getFeed().getId()).isEqualTo(feedId);
    }
}