package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.FeedTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedTechRepository extends JpaRepository<FeedTech, Long> {
}
