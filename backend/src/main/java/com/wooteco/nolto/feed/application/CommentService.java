package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.ui.dto.*;
import com.wooteco.nolto.notification.application.NotificationCommentDeleteEvent;
import com.wooteco.nolto.notification.application.NotificationEvent;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedService feedService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CommentResponse createComment(User user, Long feedId, CommentRequest request) {
        Feed findFeed = feedService.findEntityById(feedId);
        Comment comment = new Comment(request.getContent(), request.isHelper()).writtenBy(user, findFeed);
        commentRepository.save(comment);
        applicationEventPublisher.publishEvent(NotificationEvent.commentOf(findFeed, comment, request.isHelper()));
        return CommentResponse.of(comment, user);
    }

    public List<CommentWithReplyResponse> findAllByFeedId(Long feedId, User user) {
        List<Comment> comments = commentRepository.findAllByFeedIdAndParentCommentIdIsNull(feedId);
        comments.sort(Comparator.comparing(Comment::getCreatedDate, Comparator.reverseOrder()));
        return CommentWithReplyResponse.toList(comments, user);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest request, User user) {
        Comment findComment = findEntityById(commentId);
        if (!findComment.isAuthor(user)) {
            throw new UnauthorizedException(ErrorType.UNAUTHORIZED_UPDATE_COMMENT);
        }
        notifyWhenChangedToHelper(request, findComment);
        findComment.update(request.getContent(), request.isHelper());
        Comment updatedComment = commentRepository.saveAndFlush(findComment);
        return CommentResponse.of(updatedComment, user);
    }

    private void notifyWhenChangedToHelper(CommentRequest request, Comment findComment) {
        boolean isChanged = findComment.changedToHelper(request.isHelper());
        if (isChanged) {
            applicationEventPublisher.publishEvent(NotificationEvent.commentOf(findComment.getFeed(), findComment, request.isHelper()));
        }
    }

    public void deleteComment(User user, Long commentId) {
        Comment comment = findEntityById(commentId);
        if (!comment.isAuthor(user)) {
            throw new UnauthorizedException(ErrorType.UNAUTHORIZED_DELETE_COMMENT);
        }
        if (!comment.isReply()) {
            applicationEventPublisher.publishEvent(new NotificationCommentDeleteEvent(comment));
        }
        user.deleteComment(comment);
        commentRepository.delete(comment);
    }

    public Comment findEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorType.COMMENT_NOT_FOUND));
    }

    public CommentResponse createReply(User user, Long feedId, Long commentId, CommentRequest request) {
        Feed findFeed = feedService.findEntityById(feedId);
        Comment reply = Comment.createReply(request.getContent(), request.isHelper()).writtenBy(user, findFeed);
        Comment comment = findEntityById(commentId);
        reply.addParentComment(comment);

        Comment saveReply = commentRepository.save(reply);
        applicationEventPublisher.publishEvent(NotificationEvent.replyOf(comment, saveReply));
        return CommentResponse.of(saveReply, user);
    }

    public List<ReplyResponse> findAllRepliesById(User user, Long feedId, Long commentId) {
        List<Comment> replies = commentRepository.findAllByFeedIdAndParentCommentId(feedId, commentId);
        replies.sort(Comparator.comparing(Comment::getCreatedDate, Comparator.reverseOrder()));
        return ReplyResponse.toList(replies, user);
    }
}
