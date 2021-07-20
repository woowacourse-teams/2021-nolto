package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private String socialType;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String nickName;

    @Column(nullable = false)
    @NotBlank
    private String imageUrl;

    @OneToMany(mappedBy = "author")
    private final List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private final List<Like> likes = new ArrayList<>();

    public User(String socialId, String socialType, String nickName, String imageUrl) {
        this(null, socialId, socialType, nickName, imageUrl);
    }

    public User(Long id, String socialId, String socialType, String nickName) {
        this(id, socialId, socialType, nickName, null);
    }

    public void update(String nickName, String imageUrl) {
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }

    public boolean isLiked(Feed feed) {
        return likes.stream()
                .anyMatch(like -> like.hasFeed(feed));
    }

    public void addFeed(Feed feed) {
        this.feeds.add(feed);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isLiked(Feed feed) {
            return false;
        }
    }
}
