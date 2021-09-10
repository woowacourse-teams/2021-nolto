package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;

@Getter
public class FeedNotificationResponse {

    private final Long id;
    private final String title;

    public FeedNotificationResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static FeedNotificationResponse of(Feed feed) {
        return new FeedNotificationResponse(feed.getId(), feed.getTitle());
    }
}
