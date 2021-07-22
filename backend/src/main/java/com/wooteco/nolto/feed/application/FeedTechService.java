package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
@Transactional
public class FeedTechService {

    private final TechRepository techRepository;
    private final FeedTechRepository feedTechRepository;

    public void save(Feed feed, List<Long> techIds) {
        List<Tech> techs = techRepository.findAllById(techIds);
        techs.forEach(tech -> feedTechRepository.save(new FeedTech(feed, tech)));
    }

    public Set<Feed> findFeedUsingTech(List<String> techNames) {
        List<Tech> techs = techRepository.findAllByNameIn(techNames);
        List<FeedTech> findFeedTech = findFeedTechByTech(techs);
        return findFeedByFeedTech(findFeedTech);
    }

    private List<FeedTech> findFeedTechByTech(List<Tech> techs) {
        List<FeedTech> findFeedTech = new ArrayList<>();
        techs.forEach(tech -> findFeedTech.addAll(feedTechRepository.findByTech(tech)));
        return findFeedTech;
    }

    private Set<Feed> findFeedByFeedTech(List<FeedTech> feedTechs) {
        Set<Feed> findFeed = new HashSet<>();
        feedTechs.forEach(feedTech -> findFeed.add(feedTech.getFeed()));
        return findFeed;
    }
}
