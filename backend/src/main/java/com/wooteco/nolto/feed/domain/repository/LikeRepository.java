package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
