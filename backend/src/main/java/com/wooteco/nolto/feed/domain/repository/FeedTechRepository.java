package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.tech.domain.Tech;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedTechRepository extends JpaRepository<FeedTech, Long> {
    List<FeedTech> findByTech(Tech tech);
}
