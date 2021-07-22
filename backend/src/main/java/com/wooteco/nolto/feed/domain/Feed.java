package com.wooteco.nolto.feed.domain;

import com.amazonaws.util.StringUtils;
import com.wooteco.nolto.BaseEntity;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false)
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

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<FeedTech> feedTechs = new ArrayList<>();

    public Feed(String title, String content, Step step, boolean isSos, String storageUrl, String deployedUrl, String thumbnailUrl) {
        this(null, title, content, step, isSos, storageUrl, deployedUrl, thumbnailUrl, 0, null, new ArrayList<>());
    }

    public Feed(Long id, String title, String content, Step step, boolean isSos, String storageUrl,
                String deployedUrl, String thumbnailUrl) {
        this(id, title, content, step, isSos, storageUrl, deployedUrl, thumbnailUrl, 0, null,
                new ArrayList<>());
    }

    public Feed(Long id, String title, String content, Step step, boolean isSos, String storageUrl,
                String deployedUrl, String thumbnailUrl, int views, User author, List<Like> likes) {
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
        this.likes = likes;
    }

    public Feed writtenBy(User author) {
        this.author = author;
        author.addFeed(this);
        return this;
    }

    public void validateStep(Step step, String deployedUrl) {
        if (step.equals(Step.COMPLETE) && StringUtils.isNullOrEmpty(deployedUrl)) {
            throw new IllegalStateException("COMPLETE 단계는 배포 URL이 필수입니다.");
        }
    }

    public int likesCount() {
        return likes.size();
    }

    public void increaseView() {
        this.views++;
    }

    public List<Tech> getTechs() {
        return feedTechs.stream()
                .map(FeedTech::getTech)
                .collect(Collectors.toList());
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
