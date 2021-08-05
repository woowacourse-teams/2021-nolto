package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.user.domain.UserRepository;
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
    private EntityManager entityManager;

    @Test
    void addCommentLike() {
        // when
        commentLikeService.addCommentLike(comment1.getId(), user1);

        entityManager.flush();
        entityManager.clear();

        // then
        assertThat(comment1.getLikes()).hasSize(1);
    }

    @Test
    void addCommentLikeTwice() {
        // given
        commentLikeService.addCommentLike(comment1.getId(), user1);

        entityManager.flush();
        entityManager.clear();

        // when then
        assertThatThrownBy(() -> commentLikeService.addCommentLike(comment1.getId(), user1))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void deleteCommentLike() {
        // given
        commentLikeService.addCommentLike(comment1.getId(), user1);
        entityManager.flush();
        entityManager.clear();

        // when
        Comment findComment = commentService.findEntityById(comment1.getId());
        commentLikeService.deleteCommentLike(findComment.getId(), user1);
        entityManager.flush();
        entityManager.clear();
        Comment findComment2 = commentService.findEntityById(comment1.getId());

        // then
        assertThat(findComment2.getLikes()).hasSize(0);
    }

    @Test
    void deleteCommentLikeNotLiked() {
        // given
        commentLikeService.addCommentLike(comment1.getId(), user1);
        entityManager.flush();
        entityManager.clear();
        assertThat(comment1.getLikes()).hasSize(1);

        // when
        Comment findComment1 = commentService.findEntityById(comment1.getId());
        commentLikeService.deleteCommentLike(findComment1.getId(), user1);
        entityManager.flush();
        entityManager.clear();
        Comment findComment2 = commentService.findEntityById(comment1.getId());
        assertThat(findComment2.getLikes()).hasSize(0);

        // then
        assertThatThrownBy(() -> commentLikeService.deleteCommentLike(findComment2.getId(), user1))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("좋아요를 누르지 않았습니다.");
    }
}