package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "feed")
    private List<Tech> techs = new ArrayList<>();

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

    @OneToMany(mappedBy = "feed")
    private List<Like> likes = new ArrayList<>();

    public Feed(List<Tech> techs, String title, String content, Step step, boolean isSos, String storageUrl,
                String deployedUrl, String thumbnailUrl) {
        this(null, techs, title, content, step, isSos, storageUrl, deployedUrl, thumbnailUrl, 0, null,
                new ArrayList<>());
    }

    public Feed(Long id, List<Tech> techs, String title, String content, Step step, boolean isSos, String storageUrl,
                String deployedUrl, String thumbnailUrl, int views, User author, List<Like> likes) {
        validateStep(step, deployedUrl);
        this.id = id;
        this.techs = techs;
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
        author.getFeeds().add(this);
        return this;
    }

    public void validateStep(Step step, String deployedUrl) {
        if (step.equals(Step.COMPLETE) && Objects.isNull(deployedUrl)) {
            throw new IllegalStateException("전시중 Step은 배포 URL이 필수입니다.");
        }
    }

    public boolean isLikedByUser(User user) {
        for (Like like : likes) {
            if (like.getUser().getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
