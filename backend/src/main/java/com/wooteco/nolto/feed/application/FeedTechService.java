package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        Map<Feed, Integer> feedWithTechCount = calculateFeedWithTechCount(findFeedTech);
        return findFeedUsingAllTech(feedWithTechCount, techs.size());
    }

    private List<FeedTech> findFeedTechByTech(List<Tech> techs) {
        List<FeedTech> findFeedTech = new ArrayList<>();
        techs.forEach(tech -> findFeedTech.addAll(feedTechRepository.findByTech(tech)));
        return findFeedTech;
    }

    private Map<Feed, Integer> calculateFeedWithTechCount(List<FeedTech> feedTechs) {
        Map<Feed, Integer> feedWithTechCount = new HashMap<>();
        for (FeedTech feedTech : feedTechs) {
            Feed feed = feedTech.getFeed();
            feedWithTechCount.computeIfPresent(feed, (Feed, Integer) -> ++Integer);
            feedWithTechCount.putIfAbsent(feed, 1);
        }
        return feedWithTechCount;
    }

    private Set<Feed> findFeedUsingAllTech(Map<Feed, Integer> feedWithTechCount, int totalTechCount) {
        return feedWithTechCount.keySet()
                .stream()
                .filter(feed -> feedWithTechCount.get(feed) == totalTechCount)
                .collect(Collectors.toSet());
    }
}
