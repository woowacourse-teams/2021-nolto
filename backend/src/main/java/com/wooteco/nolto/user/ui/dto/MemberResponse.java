package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private final Long id;
    private final String socialType;
    private final String nickName;
    private final String imageUrl;

    public static MemberResponse of(User user) {
        return new MemberResponse(user.getId(), user.getSocialType().name(), user.getNickName(), user.getImageUrl());
    }
}
