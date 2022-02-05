package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentLikeServiceTest extends CommentServiceFixture {

    public CommentLikeServiceTest(FeedRepository feedRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, CommentLikeService commentLikeService) {
        super(feedRepository, userRepository, commentRepository, commentService, commentLikeService);
    }

    @Autowired
    private EntityManager em;

    @DisplayName("댓글에 좋아요 추가할 수 있다.")
    @Test
    void addCommentLike() {
        // when
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);

        em.flush();
        em.clear();

        // then
        찰리가_쓴_피드에_찰리가_쓴_댓글 = commentRepository.getById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        assertThat(찰리가_쓴_피드에_찰리가_쓴_댓글.getLikes()).hasSize(1);
    }

    @DisplayName("이미 좋아요가 눌러져있는 댓글에 좋아요 요청을 하면 예외가 발생한다.")
    @Test
    void addCommentLikeTwice() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);

        em.flush();
        em.clear();

        // when then
        찰리 = userRepository.getById(찰리.getId());
        assertThatThrownBy(() -> commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("댓글에 좋아요를 취소할 수 있다.")
    @Test
    void deleteCommentLike() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);
        em.flush();
        em.clear();

        // when
        Comment findComment = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        commentLikeService.deleteCommentLike(findComment.getId(), 찰리);
        em.flush();
        em.clear();
        Comment findComment2 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId());

        // then
        assertThat(findComment2.getLikes()).isEmpty();
    }

    @DisplayName("댓글에 좋아요를 누르지 않은 상태에서 좋아요 취소 요청을 하면 예외가 발생한다.")
    @Test
    void deleteCommentLikeNotLiked() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글.getId(), 찰리);
        em.flush();
        em.clear();

        // when
        Comment findComment1 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        commentLikeService.deleteCommentLike(findComment1.getId(), 찰리);
        em.flush();
        em.clear();
        Comment findComment2 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글.getId());
        assertThat(findComment2.getLikes()).isEmpty();

        // then
        assertThatThrownBy(() -> commentLikeService.deleteCommentLike(findComment2.getId(), 찰리))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("좋아요를 누르지 않은 댓글입니다.");
    }

    @DisplayName("대댓글에 좋아요 추가할 수 있다.")
    @Test
    void addReplyLike() {
        // when
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId(), 찰리);

        em.flush();
        em.clear();

        // then
        찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글 = commentRepository.getById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId());
        assertThat(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getLikes()).hasSize(1);
    }

    @DisplayName("이미 좋아요가 눌러져있는 대댓글에 좋아요 요청을 하면 예외가 발생한다.")
    @Test
    void addReplyLikeTwice() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId(), 찰리);

        em.flush();
        em.clear();

        // when then
        찰리 = userRepository.getById(찰리.getId());
        assertThatThrownBy(() -> commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId(), 찰리))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("대댓글에 좋아요를 취소할 수 있다.")
    @Test
    void deleteReplyLike() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId(), 찰리);
        em.flush();
        em.clear();

        // when
        Comment findComment = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId());
        commentLikeService.deleteCommentLike(findComment.getId(), 찰리);
        em.flush();
        em.clear();
        Comment findComment2 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId());

        // then
        assertThat(findComment2.getLikes()).isEmpty();
    }

    @DisplayName("대댓글에 좋아요를 누르지 않은 상태에서 좋아요 취소 요청을 하면 예외가 발생한다.")
    @Test
    void deleteReplyLikeNotLiked() {
        // given
        commentLikeService.addCommentLike(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId(), 찰리);
        em.flush();
        em.clear();

        // when
        Comment findComment1 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId());
        commentLikeService.deleteCommentLike(findComment1.getId(), 찰리);
        em.flush();
        em.clear();
        Comment findComment2 = commentService.findEntityById(찰리가_쓴_피드에_찰리가_쓴_댓글에_포모가_쓴_대댓글.getId());
        assertThat(findComment2.getLikes()).isEmpty();

        // then
        assertThatThrownBy(() -> commentLikeService.deleteCommentLike(findComment2.getId(), 찰리))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("좋아요를 누르지 않은 댓글입니다.");
    }
}