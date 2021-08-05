package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String nickname;
    private String bio;
    private String imageUrl;
    private int notifications;
    private LocalDateTime createdAt;

    public static ProfileResponse of(User user, int notifications) {
        return new ProfileResponse(user.getId(), user.getNickName(), user.getBio(), user.getImageUrl(),
                notifications, user.getCreatedDate());
    }
}
