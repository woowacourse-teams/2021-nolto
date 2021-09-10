package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long id;
    private final String nickname;
    private final String imageUrl;
    private final long notifications;

    public MemberResponse(Long id, String nickname, String imageUrl, long notifications) {
        this.id = id;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.notifications = notifications;
    }

    public static MemberResponse of(User user, long notificationCount) {
        return new MemberResponse(user.getId(), user.getNickName(), user.getImageUrl(), notificationCount);
    }
}
