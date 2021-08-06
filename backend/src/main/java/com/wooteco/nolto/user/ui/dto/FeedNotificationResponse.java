package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedNotificationResponse {

    private final Long id;
    private final String title;

    public static FeedNotificationResponse of(Feed feed) {
        return new FeedNotificationResponse(feed.getId(), feed.getTitle());
    }
}
