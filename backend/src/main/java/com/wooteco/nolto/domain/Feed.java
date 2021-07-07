package com.wooteco.nolto.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
}
