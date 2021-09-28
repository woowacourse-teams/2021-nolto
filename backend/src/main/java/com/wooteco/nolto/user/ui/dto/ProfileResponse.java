package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String nickname;
    private String bio;
    private String imageUrl;
    private long notifications;
    private LocalDateTime createdAt;

    public ProfileResponse(Long id, String nickname, String bio, String imageUrl, long notifications, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.imageUrl = imageUrl;
        this.notifications = notifications;
        this.createdAt = createdAt;
    }

    public static ProfileResponse of(User user, long notifications) {
        return new ProfileResponse(user.getId(), user.getNickName(), user.getBio(), user.getImageUrl(),
                notifications, user.getCreatedDate());
    }
}
