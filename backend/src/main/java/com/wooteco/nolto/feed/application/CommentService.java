package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.CommentRepository;
import com.wooteco.nolto.feed.ui.dto.CommentRequest;
import com.wooteco.nolto.feed.ui.dto.CommentResponse;
import com.wooteco.nolto.feed.ui.dto.CommentWithReplyResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private FeedService feedService;

    public CommentResponse create(User user, Long feedId, CommentRequest request) {
        Feed findFeed = feedService.findEntityById(feedId);
        Comment comment = new Comment(request.getContent(), request.isHelper()).writtenBy(user);
        findFeed.addComment(comment);
        commentRepository.save(comment);
        return CommentResponse.of(comment, user);
    }

    public List<CommentWithReplyResponse> findAllByFeedId(Long feedId, User user) {
        List<Comment> comments = commentRepository.findAllByFeedId(feedId);
        for (Comment comment : comments) {
            comment.sortReplies();
        }
        return CommentWithReplyResponse.toList(comments, user);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest request, User user) {
        Comment findComment = findEntityById(commentId);
        findComment.update(request.getContent(), request.isHelper());
        commentRepository.flush();
        return CommentResponse.of(findComment, user);
    }

    public void deleteComment(Long commentId) {
        commentRepository.delete(findEntityById(commentId));
    }

    public Comment findEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorType.COMMENT_NOT_FOUND));
    }
}
