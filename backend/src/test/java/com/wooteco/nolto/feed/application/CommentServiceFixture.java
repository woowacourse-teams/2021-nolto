package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
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

import static com.wooteco.nolto.FeedFixture.진행중_단계의_피드_생성;
import static com.wooteco.nolto.UserFixture.찰리_생성;
import static com.wooteco.nolto.UserFixture.포모_생성;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CommentServiceFixture {

    protected final static CommentRequest 도움_제안_없는_댓글_요청 = new CommentRequest("첫 댓글", false);
    protected final static CommentRequest 도움_제안_댓글_요청 = new CommentRequest("두 번째 댓글", true);

    protected final FeedRepository feedRepository;
    protected final UserRepository userRepository;
    protected final CommentRepository commentRepository;
    protected final CommentService commentService;
    protected final CommentLikeService commentLikeService;

    protected User 찰리;
    protected User 포모;
    protected Feed 찰리가_쓴_피드;
    protected Comment 찰리가_쓴_피드에_찰리가_쓴_댓글;
    protected Comment 찰리가_쓴_피드에_포모가_쓴_댓글;
    protected Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글;
    protected Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글;
    protected Comment 찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글;

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
        찰리 = 찰리_생성();
        포모 = 포모_생성();
        userRepository.saveAndFlush(찰리);
        userRepository.saveAndFlush(포모);

        찰리가_쓴_피드 = 진행중_단계의_피드_생성().writtenBy(찰리);
        feedRepository.saveAndFlush(찰리가_쓴_피드);

        찰리가_쓴_피드에_찰리가_쓴_댓글 = new Comment("첫 댓글", false).writtenBy(찰리, 찰리가_쓴_피드);
        commentRepository.saveAndFlush(찰리가_쓴_피드에_찰리가_쓴_댓글);
        찰리가_쓴_피드에_포모가_쓴_댓글 = new Comment("두 번째 댓글", true).writtenBy(포모, 찰리가_쓴_피드);
        commentRepository.saveAndFlush(찰리가_쓴_피드에_포모가_쓴_댓글);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글 = new Comment("첫 대댓글", false).writtenBy(찰리, 찰리가_쓴_피드);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글.addParentComment(찰리가_쓴_피드에_찰리가_쓴_댓글);
        commentRepository.saveAndFlush(찰리가_쓴_피드에_찰리가_쓴_댓글에_찰리가_쓴_대댓글);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글 = new Comment("두 번째 대댓글", false).writtenBy(포모, 찰리가_쓴_피드);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.addParentComment(찰리가_쓴_피드에_찰리가_쓴_댓글);
        commentRepository.saveAndFlush(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글 = new Comment("세 번째 대댓글", false).writtenBy(포모, 찰리가_쓴_피드);
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글.addParentComment(찰리가_쓴_피드에_찰리가_쓴_댓글);
        commentRepository.saveAndFlush(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_두번째_대댓글);
    }
}
