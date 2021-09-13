package com.wooteco.nolto.feed.application.searchstrategy;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NoneStrategy extends SearchStrategy {

    public NoneStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> search(String query, String techs) {
        return new ArrayList<>();
    }

    @Override
    public List<Feed> searchWithCondition(String query, String techs, boolean help, long nextFeedId, EnumSet<Step> steps, Pageable pageable) {
        return new ArrayList<>();
    }
}
