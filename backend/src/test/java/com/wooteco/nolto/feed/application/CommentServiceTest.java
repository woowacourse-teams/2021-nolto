package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.ReplyRequest;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager entityManager;

    private User 찰리 = new User("socialId", SocialType.GOOGLE, "찰리", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 아마찌 = new User("socialId", SocialType.GITHUB, "아마찌", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 조엘 = new User("socialId", SocialType.GITHUB, "조엘", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");
    private User 포모 = new User("socialId", SocialType.GOOGLE, "포모", "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png");

    private Feed 아마찌의_개쩌는_지하철_미션 = new Feed(
            "아마찌의 개쩌는 지하철 미션",
            "난 너무 잘해",
            Step.COMPLETE,
            false,
            "www.github.com/newWisdom",
            "www.github.com/newWisdom",
            "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png"
    );

    @BeforeEach
    void setUp() {
        userRepository.save(찰리);
        userRepository.save(아마찌);
        userRepository.save(조엘);
        userRepository.save(포모);

        아마찌의_개쩌는_지하철_미션.writtenBy(아마찌);
        feedRepository.save(아마찌의_개쩌는_지하철_미션);
    }

    @DisplayName("대댓글을 작성한다. (글 작성자가 아닌 유저)")
    @Test
    void createReply() {
        // given
        Comment 찰리_댓글 = new Comment("역시 아마찌! 우리 팀 최고 아웃풋!", false);
        찰리_댓글.writtenBy(찰리);
        찰리_댓글.setFeed(아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(찰리_댓글);
        entityManager.clear();

        // when
        ReplyRequest 조엘_대댓글 = new ReplyRequest("찰리 나는..?");
        ReplyResponse replyResponse = commentService.createReply(조엘, 아마찌의_개쩌는_지하철_미션.getId(), 찰리_댓글.getId(), 조엘_대댓글);

        // then
        assertThat(replyResponse.getId()).isNotNull();
        assertThat(조엘_대댓글.getContent()).isEqualTo(replyResponse.getContent());
        assertThat(replyResponse.getLikes()).isZero();
        assertThat(replyResponse.isFeedAuthor()).isFalse();
        assertThat(replyResponse.getCreatedAt()).isNotNull();
        assertThat(replyResponse.isModified()).isFalse();
        assertThat(replyResponse.getCommentId()).isEqualTo(찰리_댓글.getId());
        assertThat(replyResponse.getAuthor().getId()).isEqualTo(조엘.getId());
    }

    @DisplayName("대댓글을 작성한다. (글 작성자인 유저)")
    @Test
    void createReplyWithAuthor() {
        // given
        Comment 포모_댓글 = 댓글_생성("아마찌에게 '누난 내 여자라니까' 불러줄 사람 구합니다.", false, 포모, 아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(포모_댓글);
        entityManager.clear();

        // when
        ReplyRequest 아마찌_대댓글 = new ReplyRequest("내 글에서 나가~~");
        ReplyResponse replyResponse = commentService.createReply(
                아마찌,
                아마찌의_개쩌는_지하철_미션.getId(),
                포모_댓글.getId(),
                아마찌_대댓글);

        // then
        assertThat(replyResponse.getId()).isNotNull();
        assertThat(replyResponse.getContent()).isEqualTo(아마찌_대댓글.getContent());
        assertThat(replyResponse.getLikes()).isZero();
        assertThat(replyResponse.isFeedAuthor()).isTrue();
        assertThat(replyResponse.getCreatedAt()).isNotNull();
        assertThat(replyResponse.isModified()).isFalse();
        assertThat(replyResponse.getCommentId()).isEqualTo(포모_댓글.getId());
        assertThat(replyResponse.getAuthor().getId()).isEqualTo(아마찌.getId());
    }

    @DisplayName("대댓글을 조회한다. ")
    @Test
    void findAllById() {
        // given
        Comment 찰리_댓글 = 댓글_생성("킨스 강의 있습니다. 이거 듣고 젠킨스를 통해 배포 자동화 해보시죠", false, 찰리, 아마찌의_개쩌는_지하철_미션);
        commentRepository.saveAndFlush(찰리_댓글);

        Comment 조엘_대댓글 = 댓글_생성("저 듣고 싶어요!!! 도커도 알려주세요 우테코는 왜 도커를 안 알려주는 거야!!!!!!!", true, 조엘, 아마찌의_개쩌는_지하철_미션);
        조엘_대댓글.addParentComment(찰리_댓글);
        Comment 포모_대댓글 = 댓글_생성("오오 젠킨스 강의 탑승해봅니다", false, 포모, 아마찌의_개쩌는_지하철_미션);
        포모_대댓글.addParentComment(찰리_댓글);
        Comment 아마찌_대댓글 = 댓글_생성("내 글에서 광고하지마!!!", false, 아마찌, 아마찌의_개쩌는_지하철_미션);
        아마찌_대댓글.addParentComment(찰리_댓글);
        List<Comment> saveReplies = commentRepository.saveAllAndFlush(Arrays.asList(조엘_대댓글, 포모_대댓글, 아마찌_대댓글));
        entityManager.clear();

        // when
        List<ReplyResponse> findReplies = commentService.findAllRepliesById(찰리, 아마찌의_개쩌는_지하철_미션.getId(), 찰리_댓글.getId());

        // then
        assertThat(findReplies.get(0).getId()).isEqualTo(아마찌_대댓글.getId());
        assertThat(findReplies.get(1).getId()).isEqualTo(포모_대댓글.getId());
        assertThat(findReplies.get(2).getId()).isEqualTo(조엘_대댓글.getId());
    }

    private Comment 댓글_생성(String 댓글_내용, boolean 도움, User 댓글_유저, Feed 피드) {
        Comment 댓글 = new Comment(댓글_내용, 도움);
        댓글.writtenBy(댓글_유저);
        댓글.setFeed(피드);
        return 댓글;
    }
}