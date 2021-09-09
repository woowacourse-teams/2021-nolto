package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;

@Getter
public class NotificationFeedDeleteEvent {
    private Feed feed;

    public NotificationFeedDeleteEvent(Feed feed) {
        this.feed = feed;
    }
}
