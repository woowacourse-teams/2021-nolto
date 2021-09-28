package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

@Getter
public class AuthorResponse {
    private final Long id;
    private final String nickname;
    private final String imageUrl;

    public AuthorResponse(Long id, String nickname, String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public static AuthorResponse of(User author) {
        return new AuthorResponse(author.getId(), author.getNickName(), author.getImageUrl());
    }
}
