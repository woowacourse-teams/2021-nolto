package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private final Long id;
    private final String nickname;
    private final String imageUrl;
    private final int notifications;

    public static MemberResponse of(User user) {
        return new MemberResponse(user.getId(), user.getNickName(), user.getImageUrl(), 0);
    }
}
