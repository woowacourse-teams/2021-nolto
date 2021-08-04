package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthorResponse {
    private final Long id;
    private final String nickname;
    private final String imageUrl;

    public static AuthorResponse of(User author) {
        return new AuthorResponse(author.getId(), author.getNickName(), author.getImageUrl());
    }
}
