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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NotificationServiceTest {
    Logger log = LoggerFactory.getLogger(NotificationServiceTest.class);

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

    /**
     * <p>테스트 순서</p>
     * <p> 1. 피드에 좋아요를 누른다.</p>
     * <p> 2. 피드에 댓글이 달린다. </p>
     * <p> 3. 피드에 도움 댓글이 달린다. </p>
     *
     * <p>알림 개수 조회 시 총 3개가 출력</p>
     * <p>알림 조회 시 좋아요 -> 댓글 -> 도움 댓글 순의 알림 데이터가 조회</p>
     * <p></p>
     * <p>[참고1] 알림에 대한 최신 정렬은 memberService에서 하고 있기 때문에 notificationService에서는 등록된 알림순(좋아요 -> 댓글 -> 도움댓글)으로 출력</p>
     * <p>[참고2] 알림 리스너는 비동기적이므로 순서 보장이 안된다. 따라서 타입에 따라 분기를 생성하여 동등성 비교</p>
     *
     * @author 포모
     **/

    @DisplayName("알림 이벤트 리스너가 발동하기 위한 트리거를 실행한다")
    @Test
    @Commit
    @Transactional
    public void triggerForTransactionalEventListener() {
        트리거1_피드에_좋아요를_누른다();
        트리거2_피드에_댓글이_달린_경우_알림을_저장한다();
        트리거3_피드에_도움을_제안하는_댓글이_달린_경우_알림을_저장한다();
    }

    @DisplayName("트랜잭션이 종료된 후 알림 이벤트 리스너가 발동한다. (좋아요/댓글/도움댓글 알림 및 알림 데이터 조회 테스트 동시 수행)")
    @AfterTransaction
    public void startNotificationEventListener() {
        long notificationCount = notificationService.findNotificationCount(찰리);
        List<Notification> notifications = notificationService.findAllByUser(찰리);
        assertThat(notificationCount).isEqualTo(3);
        알림_타입에_따라_동등성을_비교한다(notifications);
    }

    private void 알림_타입에_따라_동등성을_비교한다(List<Notification> notifications) {
        피드에_좋아요를_받은_경우_알림이_올바르게_저장된다(notifications.get(0));
        피드에_댓글이_달린_경우_알림이_올바르게_저장된다(notifications.get(1));
        피드에_도움을_제안하는_댓글이_달린_경우_알림이_올바르게_저장된다(notifications.get(2));
    }

    private void 트리거1_피드에_좋아요를_누른다() {
        log.info("트리거1_피드에_좋아요를_누른다 - start");
        likeService.addLike(포모, 좋아요를_받을_찰리가_쓴_피드.getId());
        log.info("트리거1_피드에_좋아요를_누른다 - success");
    }

    private void 트리거2_피드에_댓글이_달린_경우_알림을_저장한다() {
        log.info("트리거2_피드에_댓글이_달린_경우_알림을_저장한다 - start");
        commentService.createComment(포모, 댓글이_달릴_찰리가_쓴_피드.getId(), new CommentRequest("잘 보고 갑니다!", false));
        log.info("트리거2_피드에_댓글이_달린_경우_알림을_저장한다 - success");
    }

    private void 트리거3_피드에_도움을_제안하는_댓글이_달린_경우_알림을_저장한다() {
        log.info("트리거3_피드에_도움을_제안하는_댓글이_달린_경우_알림을_저장한다 - start");
        commentService.createComment(포모, 도움_댓글이_달릴_찰리가_쓴_피드.getId(), new CommentRequest("잘 보고 갑니다!", true));
        log.info("트리거3_피드에_도움을_제안하는_댓글이_달린_경우_알림을_저장한다 - success");
    }

    private void 피드에_좋아요를_받은_경우_알림이_올바르게_저장된다(Notification notification) {
        log.info("피드에_좋아요를_받은_경우_알림이_올바르게_저장된다 - start");
        알림이_같은지_조회한다(notification, 포모, 좋아요를_받을_찰리가_쓴_피드.getId(), NotificationType.LIKE);
        log.info("피드에_좋아요를_받은_경우_알림이_올바르게_저장된다 - success");
    }

    private void 피드에_댓글이_달린_경우_알림이_올바르게_저장된다(Notification notification) {
        log.info("피드에_좋아요를_받은_경우_알림이_올바르게_저장된다 - start");
        알림이_같은지_조회한다(notification, 포모, 댓글이_달릴_찰리가_쓴_피드.getId(), NotificationType.COMMENT);
        log.info("피드에_댓글이_달린_경우_알림이_올바르게_저장된다 - success");
    }

    private void 피드에_도움을_제안하는_댓글이_달린_경우_알림이_올바르게_저장된다(Notification notification) {
        log.info("피드에_좋아요를_받은_경우_알림이_올바르게_저장된다 - start");
        알림이_같은지_조회한다(notification, 포모, 도움_댓글이_달릴_찰리가_쓴_피드.getId(), NotificationType.COMMENT_SOS);
        log.info("피드에_도움을_제안하는_댓글이_달린_경우_알림이_올바르게_저장된다 - success");
    }

    private void 알림이_같은지_조회한다(Notification response, User publisher, Long feedId, NotificationType type) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getPublisher().getId()).isEqualTo(publisher.getId());
        assertThat(response.getNotificationType()).isEqualTo(type);
        assertThat(response.getFeed().getId()).isEqualTo(feedId);
    }

}