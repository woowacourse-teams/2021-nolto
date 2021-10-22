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

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId and feed.step in :steps and feed.isSos in :help " +
            "and (upper(feed.title) like upper(concat('%', :query, '%')) or upper(feed.content) like upper(concat('%', :query, '%'))) " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findByQuery(@Param("query") String query, @Param("help") Set<Boolean> helpCondition, @Param("feedId") Long feedId, @Param("steps") EnumSet<Step> steps, Pageable pageable);

    @Query("select distinct feed from " +
            "Feed as feed " +
            "join fetch feed.author " +
            "join feed.feedTechs ft " +
            "where feed.id <= :feedId and feed.step in :steps and feed.isSos in :help " +
            "and ft.tech.name in :techNames " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findByTechs(@Param("techNames") List<String> techNames, @Param("help") Set<Boolean> helpCondition, @Param("feedId") Long feedId, @Param("steps") EnumSet<Step> steps, Pageable pageable);

    @Query("select distinct feed from " +
            "Feed as feed " +
            "join fetch feed.author " +
            "join feed.feedTechs ft " +
            "where feed.id <= :feedId and feed.step in :steps and feed.isSos in :help " +
            "and (upper(feed.title) like upper(concat('%', :query, '%')) or upper(feed.content) like upper(concat('%', :query, '%'))) " +
            "and ft.tech.name in :techNames " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findByQueryAndTechs(@Param("query") String query, @Param("techNames") List<String> techNames, @Param("help") Set<Boolean> helpCondition, @Param("feedId") Long feedId, @Param("steps") EnumSet<Step> steps, Pageable pageable);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId and feed.step in :steps " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findWithoutHelp(@Param("steps") EnumSet<Step> steps, @Param("feedId") Long feedId, Pageable pageable);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId and feed.step in :steps and feed.isSos = :help " +
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findWithHelp(@Param("steps") EnumSet<Step> steps, @Param("help") Boolean help, @Param("feedId") Long feedId, Pageable pageable);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "left join fetch feed.feedTechs")
    List<Feed> findAllWithFetchJoin();

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "join fetch feed.comments")
    List<Feed> findAllFeedsHavingComments();
}
