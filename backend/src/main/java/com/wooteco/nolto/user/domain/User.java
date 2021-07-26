package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.auth.domain.SocialType;
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
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false)
    @NotBlank
    private String nickName;

    @Column(nullable = false)
    @NotBlank
    private String imageUrl;

    @OneToMany(mappedBy = "author")
    private final List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Like> likes = new ArrayList<>();

    public User(String socialId, SocialType socialType, String nickName, String imageUrl) {
        this(null, socialId, socialType, nickName, imageUrl);
    }

    public User(Long id, String socialId, SocialType socialType, String nickName) {
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

    public void addLike(Like like) {
        this.likes.add(like);
    }

    public boolean SameAs(User user) {
        return getId().equals(user.getId());
    }

    public void delete(Like like) {
        this.likes.remove(like);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isLiked(Feed feed) {
            return false;
        }
    }
}
