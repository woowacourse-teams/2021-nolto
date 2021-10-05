package com.wooteco.nolto.notification.application;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static com.wooteco.nolto.UserFixture.찰리_생성;
import static com.wooteco.nolto.UserFixture.포모_생성;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NotificationServiceTest {

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

    @BeforeEach
    public void setUp() {
        찰리 = 찰리_생성();
        포모 = 포모_생성();
        userRepository.save(찰리);
        userRepository.save(포모);

        좋아요를_받을_찰리가_쓴_피드 = 찰리가_쓴_피드를_생성();
        댓글이_달릴_찰리가_쓴_피드 = 찰리가_쓴_피드를_생성();
        도움_댓글이_달릴_찰리가_쓴_피드 = 찰리가_쓴_피드를_생성();
        feedRepository.saveAll(Arrays.asList(좋아요를_받을_찰리가_쓴_피드, 댓글이_달릴_찰리가_쓴_피드, 도움_댓글이_달릴_찰리가_쓴_피드));
    }

    @DisplayName("피드에 좋아요를 받은 경우 알림을 저장한다.")
    @Test
    void saveWhenFeedLikeWithEventListener() {
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
    void saveWhenCommentWithEventListener() {
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
    void saveWhenHelperCommentEventListener() {
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

    private Feed 찰리가_쓴_피드를_생성() {
        return Feed.builder()
                .title("title")
                .content("난 너무 잘해")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build().writtenBy(찰리);
    }
}