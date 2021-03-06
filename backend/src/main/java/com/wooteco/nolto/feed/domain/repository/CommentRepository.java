package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select distinct com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "left join fetch com.likes " +
            "where com.feed.id = :feedId and com.parentComment.id is null " +
            "order by com.createdDate desc, com.id desc")
    List<Comment> findAllByFeedIdAndParentCommentIdIsNull(@Param("feedId") Long feedId);

    @Query("select distinct com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "left join fetch com.likes " +
            "where com.feed.id = :feedId and com.parentComment.id = :parentCommentId " +
            "order by com.createdDate desc, com.id desc")
    List<Comment> findAllByFeedIdAndParentCommentIdWithFetchJoin(@Param("feedId") Long feedId, @Param("parentCommentId") Long parentCommentId);
}
