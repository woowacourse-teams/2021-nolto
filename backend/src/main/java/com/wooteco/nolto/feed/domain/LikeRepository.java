package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndFeed(User user, Feed feed);
}
