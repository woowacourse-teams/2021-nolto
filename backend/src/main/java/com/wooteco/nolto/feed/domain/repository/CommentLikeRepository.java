package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
