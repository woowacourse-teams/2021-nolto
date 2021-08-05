package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByFeedId(Long feedId);
}
