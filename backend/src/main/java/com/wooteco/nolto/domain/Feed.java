package com.wooteco.nolto.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Feed {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // List<Tech>

    private String title;
    private String content;
    private String step;
    private boolean isSOS;
    private String storageUrl;
    private String deployedUrl;
    private String thumbnailUrl;
    private int views;
//    private User author;
    // List<Like>
}
