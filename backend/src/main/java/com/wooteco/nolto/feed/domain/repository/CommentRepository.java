package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select com " +
            "from Comment as com " +
            "where com.feed.id = :feedId and com.parentComment.id is null " +
            "order by com.likes.size desc , com.createdDate desc")
    List<Comment> findAllByFeedId(@Param("feedId") Long feedId);

    List<Comment> findAllByFeedIdAndParentCommentId(Long feedId, Long parentCommentId);

}
