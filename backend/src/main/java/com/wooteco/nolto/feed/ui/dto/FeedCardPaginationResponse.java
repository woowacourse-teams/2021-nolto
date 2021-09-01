package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feeds;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedCardPaginationResponse {

    private final List<FeedCardResponse> feeds;
    private final Long lastDrawnFeedId;

    public static FeedCardPaginationResponse of(Feeds feeds) {
        final List<FeedCardResponse> feedCardResponses = FeedCardResponse.toList(feeds.getFeeds());
        final Long lastFeedId = feeds.findLastFeed().getId();
        return new FeedCardPaginationResponse(feedCardResponses, lastFeedId);
    }
}
