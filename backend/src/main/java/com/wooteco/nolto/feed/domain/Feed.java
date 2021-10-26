package com.wooteco.nolto.feed.domain;

import com.amazonaws.util.StringUtils;
import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Step step;

    @Column(nullable = false)
    private boolean isSos;

    private String storageUrl;
    private String deployedUrl;
    private String thumbnailUrl;

    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_feed_to_author"), nullable = false)
    private User author;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedTech> feedTechs = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Feed(Long id, String title, String content, Step step, boolean isSos, String storageUrl,
                String deployedUrl, String thumbnailUrl, int views, User author) {
        validateStep(step, deployedUrl);
        this.id = id;
        this.title = title;
        this.content = content;
        this.step = step;
        this.isSos = isSos;
        this.storageUrl = storageUrl;
        this.deployedUrl = deployedUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.views = views;
        this.author = author;
        this.likes = new ArrayList<>();
        this.feedTechs = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Feed writtenBy(User author) {
        this.author = author;
        author.addFeed(this);
        return this;
    }

    public void update(String title, String content, Step step, boolean sos, String storageUrl, String deployedUrl) {
        this.title = title;
        this.content = content;
        this.step = step;
        this.isSos = sos;
        this.storageUrl = storageUrl;
        this.deployedUrl = deployedUrl;
    }

    public void validateStep(Step step, String deployedUrl) {
        if (step.equals(Step.COMPLETE) && StringUtils.isNullOrEmpty(deployedUrl)) {
            throw new BadRequestException(ErrorType.MISSING_DEPLOY_URL);
        }
    }

    public int likesCount() {
        return likes.size();
    }

    public void increaseView(boolean alreadyView) {
        if (alreadyView) {
            return;
        }
        this.views++;
    }

    public List<Tech> getTechs() {
        return feedTechs.stream()
                .map(FeedTech::getTech)
                .collect(Collectors.toList());
    }

    public boolean notSameAuthor(User user) {
        return !author.sameAs(user);
    }

    public void changeThumbnailUrl(String updateThumbnailUrl) {
        this.thumbnailUrl = updateThumbnailUrl;
    }

    public void changeTechs(List<Tech> techs) {
        this.feedTechs.addAll(techs.stream()
                .map(tech -> new FeedTech(this, tech))
                .collect(Collectors.toList()));
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addLike(Like like) {
        this.likes.add(like);
    }

    public void delete(Like like) {
        this.likes.remove(like);
    }

    public Optional<Like> findLikeBy(User user) {
        return likes.stream()
                .filter(like -> like.sameAs(user))
                .findAny();
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
    }

    public Map<Comment, List<Comment>> mapByCommentAndReplies() {
        Map<Comment, List<Comment>> commentAndReplies = new HashMap<>();
        for (Comment comment : comments) {
            if (comment.isParentComment()) {
                commentAndReplies.put(comment, comment.getReplies());
            }
        }
        return commentAndReplies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feed feed = (Feed) o;
        return Objects.equals(id, feed.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
