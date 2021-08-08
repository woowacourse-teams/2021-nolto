package com.wooteco.nolto.notification.application;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationFeedDeleteEvent {
    private Feed feed;
}
