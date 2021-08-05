package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.CommentWithReplyResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentServiceTest extends CommentServiceFixture {

    public CommentServiceTest(FeedRepository feedRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, CommentLikeService commentLikeService) {
        super(feedRepository, userRepository, commentRepository, commentService, commentLikeService);
    }

    @DisplayName("댓글을 1개 저장한다.")
    @Test
    void create() {
        // when
        CommentResponse response = commentService.create(user1, feed.getId(), COMMENT_REQUEST_WITHOUT_HELPER);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getContent()).isEqualTo(COMMENT_REQUEST_WITHOUT_HELPER.getContent());
        assertThat(response.isHelper()).isEqualTo(COMMENT_REQUEST_WITHOUT_HELPER.isHelper());
        assertThat(response.getAuthor().getId()).isEqualTo(user1.getId());
        assertThat(response.getLikes()).isEqualTo(0);
        assertThat(response.isLiked()).isFalse();
        assertThat(response.isModified()).isFalse();
        assertThat(response.isFeedAuthor()).isTrue();
        assertThat(response.getCreateAt()).isNotNull();
    }

    @DisplayName("특정 피드에 대한 댓글과 대댓글 전체를 조회한다.")
    @Test
    void findAllByFeedId() {
        // when
        List<CommentWithReplyResponse> allByFeedId = commentService.findAllByFeedId(feed.getId(), user1);

        // then
        checkSameCommentWithReplyResponse(allByFeedId.get(0), comment1, user1);
        checkSameCommentWithReplyResponse(allByFeedId.get(1), comment2, user1);
    }

    @DisplayName("댓글을 수정할 수 있다.")
    @Test
    void updateComment() {
        // given
        CommentResponse response = commentService.create(user1, feed.getId(), COMMENT_REQUEST_WITHOUT_HELPER);
        String updateContent = "수정된 댓글 내용";
        boolean updateHelper = true;

        // when
        CommentResponse commentResponse = commentService.updateComment(response.getId(), new CommentRequest(updateContent, updateHelper), user1);

        // then
        assertThat(commentResponse.getContent()).isEqualTo(updateContent);
        assertThat(commentResponse.isHelper()).isEqualTo(updateHelper);
        assertThat(commentResponse.isModified()).isTrue();
    }

    @DisplayName("댓글 삭제시 댓글의 대댓글도 함께 삭제된다.")
    @Test
    void deleteComment() {
        // when
        commentService.deleteComment(comment1.getId());

        // then
        assertThatThrownBy(() -> commentService.findEntityById(comment1.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(reply1.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(reply2.getId()))
                .isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> commentService.findEntityById(reply3.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    private void checkSameCommentResponse(CommentResponse response, Comment comment, User user) {
        CommentResponse commentResponse = CommentResponse.of(comment, user);
        assertThat(response.getId()).isEqualTo(commentResponse.getId());
        assertThat(response.getContent()).isEqualTo(commentResponse.getContent());
        assertThat(response.isHelper()).isEqualTo(commentResponse.isHelper());
        assertThat(response.getLikes()).isEqualTo(commentResponse.getLikes());
        assertThat(response.isLiked()).isEqualTo(commentResponse.isLiked());
        assertThat(response.isFeedAuthor()).isEqualTo(commentResponse.isFeedAuthor());
        assertThat(response.getCreateAt()).isEqualTo(commentResponse.getCreateAt());
        assertThat(response.getId()).isEqualTo(commentResponse.getId());
        assertThat(response.isModified()).isEqualTo(commentResponse.isModified());
        assertThat(response.getAuthor().getId()).isEqualTo(commentResponse.getAuthor().getId());
    }

    private void checkSameCommentWithReplyResponse(CommentWithReplyResponse response, Comment comment, User user) {
        CommentWithReplyResponse responseFromComment = CommentWithReplyResponse.of(comment, user);
        assertThat(response.getId()).isEqualTo(responseFromComment.getId());
        assertThat(response.getContent()).isEqualTo(responseFromComment.getContent());
        assertThat(response.isHelper()).isEqualTo(responseFromComment.isHelper());
        assertThat(response.getLikes()).isEqualTo(responseFromComment.getLikes());
        assertThat(response.isLiked()).isEqualTo(responseFromComment.isLiked());
        assertThat(response.isFeedAuthor()).isEqualTo(responseFromComment.isFeedAuthor());
        assertThat(response.getCreateAt()).isEqualTo(responseFromComment.getCreateAt());
        assertThat(response.getId()).isEqualTo(responseFromComment.getId());
        assertThat(response.isModified()).isEqualTo(responseFromComment.isModified());
        assertThat(response.getAuthor().getId()).isEqualTo(responseFromComment.getAuthor().getId());
        assertThat(response.getReplies()).hasSize(responseFromComment.getReplies().size());
    }
}