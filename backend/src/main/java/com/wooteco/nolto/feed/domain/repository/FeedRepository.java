package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Set<Feed> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleText, String contentText);
}
