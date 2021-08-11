package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);
        if (techs.isEmpty()) {
            return new HashSet<>();
        }

        List<Set<Feed>> feedUsingEachTech = findFeedUsingEachTech(techs);
        return findFeedUsingAllTech(feedUsingEachTech);
    }

    private List<Set<Feed>> findFeedUsingEachTech(List<Tech> techs) {
        return techs.stream()
                .map(this::findFeedUsingSpecificTech)
                .collect(Collectors.toList());
    }

    private Set<Feed> findFeedUsingSpecificTech(Tech tech) {
        return feedTechRepository.findByTech(tech)
                .stream()
                .map(FeedTech::getFeed)
                .collect(Collectors.toSet());
    }

    private Set<Feed> findFeedUsingAllTech(List<Set<Feed>> feedUsingEachTech) {
        Set<Feed> feedUsingAllTech = feedUsingEachTech.get(0);
        for (Set<Feed> usingEachTech : feedUsingEachTech) {
            feedUsingAllTech.retainAll(usingEachTech);
        }
        return feedUsingAllTech;
    }
}
