package com.wooteco.nolto.tech.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TechRepository extends JpaRepository<Tech, Long> {
    List<Tech> findByNameStartsWithIgnoreCase(String name);

    List<Tech> findAllByNameInIgnoreCase(List<String> techNames);

    @Query("select t " +
            "from FeedTech as ft, Tech as t " +
            "where t.id = ft.tech.id " +
            "group by ft.tech.id " +
            "order by count(ft.feed.id) DESC ")
    List<Tech> findTrendTech(Pageable pageable);

}
