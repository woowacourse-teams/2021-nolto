package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.ui.dto.ReplyRequest;
import com.wooteco.nolto.feed.ui.dto.ReplyResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class CommentService {

    private final FeedService feedService;
    private final CommentRepository commentRepository;

    public ReplyResponse createReply(User user, Long feedId, Long commentId, ReplyRequest request) {
        Comment comment = findEntityById(commentId);
        Comment reply = new Comment(request.getContent(), false).writtenBy(user);
        reply.addParentComment(comment);
        reply.setFeed(feedService.findEntityById(feedId));

        Comment saveReply = commentRepository.save(reply);
        return ReplyResponse.of(saveReply, false);
    }

    public Comment findEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorType.COMMENT_NOT_FOUND));
    }

    public List<ReplyResponse> findAllRepliesById(User user, Long feedId, Long commentId) {
        List<Comment> replies = commentRepository.findAllByFeedIdAndParentCommentId(feedId, commentId);
        replies.sort(Comparator.comparing(Comment::getCreatedDate, Comparator.reverseOrder()));
        return ReplyResponse.toList(replies, user);
    }

    public ReplyResponse updateReply(User user, Long feedId, Long commentId, Long replyId, ReplyRequest request) {
        Comment reply = findEntityById(replyId);
        if (!reply.getAuthor().sameAs(user)) {
            throw new UnauthorizedException(ErrorType.UNAUTHORIZED_UPDATE_COMMENT);
        }

        reply.changeContent(request.getContent());
        commentRepository.flush();
        return ReplyResponse.of(reply, reply.isLike(user));
    }

    public void deleteReply(User user, Long feedId, Long commentId, Long replyId) {
        Feed feed = feedService.findEntityById(feedId);
        Comment reply = findEntityById(replyId);
        if (!reply.getAuthor().sameAs(user)) {
            throw new UnauthorizedException(ErrorType.UNAUTHORIZED_DELETE_COMMENT);
        }
        reply.getParentComment().getReplies().remove(reply);
        user.deleteComment(reply);
        feed.deleteComment(reply);
        commentRepository.delete(reply);
    }
}
