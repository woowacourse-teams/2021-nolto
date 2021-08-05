package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.CommentLike;
import com.wooteco.nolto.feed.domain.repository.CommentLikeRepository;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Transactional
@Service
public class CommentLikeService {

    private final CommentService commentService;
    private final CommentLikeRepository commentLikeRepository;

    public void addCommentLike(Long commentId, User user) {
        Comment findComment = commentService.findEntityById(commentId);
        if (user.isCommentLiked(findComment)) {
            throw new BadRequestException(ErrorType.ALREADY_LIKED_COMMENT);
        }
        CommentLike commentLike = new CommentLike(user, findComment);
        user.addCommentLike(commentLike);
        commentLikeRepository.save(commentLike);
    }

    public void deleteCommentLike(Long commentId, User user) {
        Comment findComment = commentService.findEntityById(commentId);
        CommentLike findCommentLike = findComment.findLikeBy(user)
                .orElseThrow(() -> new BadRequestException(ErrorType.NOT_LIKED));
        user.delete(findCommentLike);
        commentLikeRepository.delete(findCommentLike);
    }
}
