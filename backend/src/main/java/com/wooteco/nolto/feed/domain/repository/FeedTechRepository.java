package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.tech.domain.Tech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedTechRepository extends JpaRepository<FeedTech, Long> {
    @Query("select feedTech " +
            "from FeedTech as feedTech " +
            "join fetch feedTech.feed " +
            "where feedTech.tech = :tech")
    List<FeedTech> findByTech(Tech tech);
}
