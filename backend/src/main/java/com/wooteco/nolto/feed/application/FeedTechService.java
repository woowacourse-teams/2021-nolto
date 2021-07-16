package com.wooteco.nolto.feed.application;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FeedTechService {

    private final TechRepository techRepository;
    private final FeedTechRepository feedTechRepository;

    public void save(Feed feed, List<Long> techIds) {
        List<Tech> techs = techRepository.findAllById(techIds);
        techs.forEach(tech -> feedTechRepository.save(new FeedTech(feed, tech)));
    }
}
