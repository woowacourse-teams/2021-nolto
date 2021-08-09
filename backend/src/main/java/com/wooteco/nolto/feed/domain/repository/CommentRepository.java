package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "where com.feed.id = :feedId and com.parentComment.id is null " +
            "order by com.likes.size desc , com.createdDate desc")
    List<Comment> findAllByFeedIdOrderByLike(@Param("feedId") Long feedId);

    @Query(value = "select com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "where com.feed.id = :feedId and com.parentComment.id is null " +
            "order by com.createdDate desc")
    List<Comment> findAllByFeedIdAndParentCommentIdIsNull(Long feedId);

    List<Comment> findAllByFeedIdAndParentCommentId(Long feedId, Long parentCommentId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from Comment c where c.id = :commentId or c.parentComment.id = :commentId")
    int deleteByCommentId(@Param("commentId") Long commentId);

}
