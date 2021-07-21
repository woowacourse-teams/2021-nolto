package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.tech.domain.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedTechRepository extends JpaRepository<FeedTech, Long> {
    FeedTech findByFeedAndTech(Feed feed, Tech tech);
    void deleteAllByFeed(Feed feed);
}
