package com.wooteco.nolto.feed.domain.repository;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    Set<Feed> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleText, String contentText);

    @Query(value = "select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author u " +
            "where feed.id <= :feedId and feed.step in :steps " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findWithoutHelp(@Param("steps") EnumSet<Step> steps, @Param("feedId") Long feedId, Pageable pageable);

    @Query(value = "select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author u " +
            "where feed.id <= :feedId and feed.step in :steps and feed.isSos =:help " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findWithHelp(@Param("steps") EnumSet<Step> steps, @Param("help") Boolean help, @Param("feedId") Long feedId, Pageable pageable);
}
