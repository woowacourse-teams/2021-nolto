package com.wooteco.nolto.user.domain;

import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.UnauthorizedException;
import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.CommentLike;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Like;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    public static final GuestUser GUEST_USER = new GuestUser();
    public static final AdminUser ADMIN_USER = new AdminUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String nickName;

    @Column(nullable = false)
    @NotBlank
    private String imageUrl;

    private String bio = "";

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    public User(String socialId, SocialType socialType, String nickName, String imageUrl) {
        this(null, socialId, socialType, nickName, imageUrl, "");
    }

    @Builder
    public User(Long id, String socialId, SocialType socialType, String nickName, String imageUrl, String bio) {
        this.id = id;
        this.socialId = socialId;
        this.socialType = socialType;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
        this.bio = bio;
        this.feeds = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.commentLikes = new ArrayList<>();
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
        return this.equals(user);
    }

    public boolean sameAsNickname(String nickName) {
        return this.nickName.equals(nickName);
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

    public List<Feed> findLikedFeeds() {
        return this.likes.stream()
                .map(Like::getFeed)
                .collect(Collectors.toList());
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateProfile(String nickname, String bio) {
        this.nickName = nickname;
        this.bio = bio;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
    }

    public void validateAdmin() {
        if (!this.isAdmin()) {
            throw new UnauthorizedException(ErrorType.ADMIN_ONLY);
        }
    }

    public boolean isAdmin() {
        return this.equals(ADMIN_USER);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    private static class AdminUser extends User {
    }
}
