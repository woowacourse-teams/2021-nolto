package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.CommentLike;
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

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private final List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final List<CommentLike> commentLikes = new ArrayList<>();

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

    public void addFeed(Feed feed) {
        this.feeds.add(feed);
    }

    public boolean isLiked(Feed feed) {
        return likes.stream()
                .anyMatch(like -> like.hasFeed(feed));
    }

    public void addLike(Like like) {
        this.likes.add(like);
        like.getFeed().addLike(like);
    }

    public boolean isCommentLiked(Comment comment) {
        return commentLikes.stream()
                .anyMatch(commentLike -> commentLike.hasComment(comment));
    }

    public void addCommentLike(CommentLike commentLike) {
        this.commentLikes.add(commentLike);
        commentLike.getComment().addCommentLike(commentLike);
    }

    public boolean sameAs(User user) {
        return this.id.equals(user.id);
    }

    public void delete(Like like) {
        this.likes.remove(like);
    }

    public void delete(CommentLike like) {
        this.commentLikes.remove(like);
    }

    public Feed findMyFeed(Long feedId) {
        return this.feeds.stream()
                .filter(feed -> feedId.equals(feed.getId()))
                .findAny().orElseThrow(() -> new UnauthorizedException(ErrorType.UNAUTHORIZED_UPDATE_FEED));
    }

    private static class GuestUser extends User {
        @Override
        public boolean isLiked(Feed feed) {
            return false;
        }

        @Override
        public boolean isCommentLiked(Comment comment) {
            return false;
        }
    }
}
