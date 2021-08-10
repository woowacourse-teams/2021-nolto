package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.notification.domain.NotificationType;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.transaction.Transactional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
public class NotificationEventTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    @MockBean
    private NotificationService notificationService;

    public User 찰리;
    public User 포모;
    public Feed 찰리가_쓴_피드;

    @BeforeTransaction
    public void setUp() {
        찰리 = new User("SOCIAL_ID", SocialType.GITHUB, "찰리", "IMAGE");
        포모 = new User("SOCIAL_ID2", SocialType.GITHUB, "포모", "IMAGE2");
        userRepository.save(찰리);
        userRepository.save(포모);

        찰리가_쓴_피드 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(찰리);
        feedRepository.save(찰리가_쓴_피드);
    }

    @DisplayName("피드에 좋아요를 받은 경우 알림을 저장한다.(본인 유무는 이벤트 발생과 무관)")
    @Test
    void saveWhenFeedLike() {
        NotificationEvent 피드_좋아요_이벤트 = NotificationEvent.likeOf(찰리가_쓴_피드, 포모);
        NotificationEventHandler notificationEventHandler = new NotificationEventHandler(notificationService);
        notificationEventHandler.saveNotification(피드_좋아요_이벤트);
        verify(notificationService, times(1)).save(피드_좋아요_이벤트);
    }

    @DisplayName("피드에 댓글이 등록된 경우 알림을 저장한다.")
    @Test
    void saveWhenComment() {
        Comment 포모_도움_아닌_댓글 = new Comment("첫 댓글", false).writtenBy(포모, 찰리가_쓴_피드);
        NotificationEvent 댓글_이벤트 = NotificationEvent.commentOf(찰리가_쓴_피드, 포모_도움_아닌_댓글, 포모_도움_아닌_댓글.isHelper());
        NotificationEventHandler notificationEventHandler = new NotificationEventHandler(notificationService);
        notificationEventHandler.saveNotification(댓글_이벤트);
        verify(notificationService, times(1)).save(댓글_이벤트);
    }

    @DisplayName("피드에 도움 댓글이 등록된 경우 알림을 저장한다.")
    @Test
    void saveWhenCommentSOS() {
        Comment 포모_도움_댓글 = new Comment("첫 댓글", true).writtenBy(포모, 찰리가_쓴_피드);
        NotificationEvent 도움_댓글_이벤트 = NotificationEvent.commentOf(찰리가_쓴_피드, 포모_도움_댓글, 포모_도움_댓글.isHelper());
        NotificationEventHandler notificationEventHandler = new NotificationEventHandler(notificationService);
        notificationEventHandler.saveNotification(도움_댓글_이벤트);
        verify(notificationService, times(1)).save(도움_댓글_이벤트);
    }
}
