package com.wooteco.nolto.notification.domain;

import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User listener;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User publisher;

    @Enumerated(EnumType.STRING)
    @NotNull
    private NotificationType notificationType;

    public Notification(User listener, Feed feed, Comment comment, User publisher, NotificationType notificationType) {
        this(null, listener, feed, comment, publisher, notificationType);
    }

    @Builder
    public Notification(Long id, User listener, Feed feed, Comment comment, User publisher, NotificationType notificationType) {
        this.id = id;
        this.listener = listener;
        this.feed = feed;
        this.comment = comment;
        this.publisher = publisher;
        this.notificationType = notificationType;
    }

    public boolean isListener(User user) {
        return this.listener.sameAs(user);
    }
}
