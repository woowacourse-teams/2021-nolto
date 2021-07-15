package com.wooteco.nolto.tech.domain;

import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Tech {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;

    @OneToMany
    private List<Feed> feeds;

    public Tech(String name) {
        this.name = name;
    }
}
