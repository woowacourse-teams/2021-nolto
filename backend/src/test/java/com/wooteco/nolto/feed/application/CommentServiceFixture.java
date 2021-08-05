package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CommentServiceFixture {

    public final static CommentRequest COMMENT_REQUEST_WITHOUT_HELPER = new CommentRequest("첫 댓글", false);
    public final static CommentRequest COMMENT_REQUEST_WITH_HELPER = new CommentRequest("두 번째 댓글", true);

    public final FeedRepository feedRepository;
    public final UserRepository userRepository;
    public final CommentRepository commentRepository;
    public final CommentService commentService;
    public final CommentLikeService commentLikeService;

    public User user1;
    public User user2;
    public Feed feed;
    public Comment comment1;
    public Comment comment2;
    public Comment reply1;
    public Comment reply2;
    public Comment reply3;

    public CommentServiceFixture(FeedRepository feedRepository, UserRepository userRepository,
                                 CommentRepository commentRepository, CommentService commentService, CommentLikeService commentLikeService) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "SOCIAL_ID", SocialType.GITHUB, "NICKNAME", "IMAGE");
        user2 = new User(2L, "SOCIAL_ID2", SocialType.GITHUB, "NICKNAME2", "IMAGE2");
        userRepository.save(user1);
        userRepository.save(user2);

        feed = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(user1);
        feedRepository.save(feed);

        comment1 = new Comment("첫 댓글", false).writtenBy(user1);
        comment1.setFeed(feed);
        commentRepository.save(comment1);
        comment2 = new Comment("두 번째 댓글", true).writtenBy(user2);
        comment2.setFeed(feed);
        commentRepository.save(comment2);
        reply1 = new Comment("첫 대댓글", false).writtenBy(user1);
        comment1.addReply(reply1);
        reply2 = new Comment("두 번째 대댓글", false).writtenBy(user2);
        comment1.addReply(reply2);
        reply3 = new Comment("세 번째 대댓글", false).writtenBy(user2);
        comment1.addReply(reply3);
        commentRepository.save(reply1);
        commentRepository.save(reply2);
        commentRepository.save(reply3);
    }
}
