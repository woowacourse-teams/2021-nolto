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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@ActiveProfiles("test")
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

    public User 찰리;
    public User 포모;
    public Feed 찰리가_쓴_피드;
    public Comment 찰리가_쓴_피드에_찰리가_쓴_댓글;
    public Comment 찰리가_쓴_피드에_포모가_쓴_댓글;
    public Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글;
    public Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글;
    public Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글;

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
        찰리 = new User("SOCIAL_ID", SocialType.GITHUB, "찰리", "IMAGE");
        포모 = new User("SOCIAL_ID2", SocialType.GITHUB, "포모", "IMAGE2");
        userRepository.saveAndFlush(찰리);
        userRepository.saveAndFlush(포모);

        찰리가_쓴_피드 = new Feed(
                "title",
                "content",
                Step.PROGRESS,
                true,
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://github.com/woowacourse-teams/2021-nolto",
                "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png").writtenBy(찰리);
        feedRepository.saveAndFlush(찰리가_쓴_피드);

        찰리가_쓴_피드에_찰리가_쓴_댓글 = new Comment("첫 댓글", false).writtenBy(찰리);
        찰리가_쓴_피드에_찰리가_쓴_댓글.setFeed(찰리가_쓴_피드);
        찰리가_쓴_피드에_포모가_쓴_댓글 = new Comment("두 번째 댓글", true).writtenBy(포모);
        찰리가_쓴_피드에_포모가_쓴_댓글.setFeed(찰리가_쓴_피드);
        commentRepository.saveAllAndFlush(Arrays.asList(찰리가_쓴_피드에_찰리가_쓴_댓글, 찰리가_쓴_피드에_포모가_쓴_댓글));
        찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글 = new Comment("첫 대댓글", false).writtenBy(찰리);
        찰리가_쓴_피드에_찰리가_쓴_댓글.addReply(찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글 = new Comment("두 번째 대댓글", false).writtenBy(포모);
        찰리가_쓴_피드에_찰리가_쓴_댓글.addReply(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글 = new Comment("세 번째 대댓글", false).writtenBy(포모);
        찰리가_쓴_피드에_찰리가_쓴_댓글.addReply(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글);
        commentRepository.saveAllAndFlush(Arrays.asList(찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글, 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글, 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글));
    }
}
