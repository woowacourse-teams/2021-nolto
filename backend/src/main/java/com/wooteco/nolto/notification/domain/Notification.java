package com.wooteco.nolto.notification.domain;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User listener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User publisher;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    public Notification(User listener, Feed feed, User publisher, NotificationType notificationType) {
        this.listener = listener;
        this.feed = feed;
        this.publisher = publisher;
        this.notificationType = notificationType;
    }
}
