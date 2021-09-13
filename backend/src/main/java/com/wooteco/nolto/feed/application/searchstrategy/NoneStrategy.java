package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;

import java.util.HashSet;
import java.util.Set;

public class NoneStrategy extends SearchStrategy {

    public NoneStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public Set<Feed> search(String query, String techs) {
        return new HashSet<>();
    }
}
