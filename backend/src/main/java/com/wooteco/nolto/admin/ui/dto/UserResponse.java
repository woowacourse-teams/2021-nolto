package com.wooteco.nolto.admin.ui.dto;

import com.wooteco.nolto.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponse {

    private Long id;
    private String nickname;
    private String bio;
    private String socialId;
    private String socialType;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public UserResponse(Long id, String nickname, String bio, String socialId, String socialType, String imageUrl, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.socialId = socialId;
        this.socialType = socialType;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static List<UserResponse> toList(List<User> users) {
        return users.stream()
                .map(UserResponse::of)
                .collect(Collectors.toList());
    }

    public static UserResponse of(User user) {
        return new UserResponse(
                user.getId(),
                user.getNickName(),
                user.getBio(),
                user.getSocialId(),
                user.getSocialType().name(),
                user.getImageUrl(),
                user.getCreatedDate(),
                user.getModifiedDate());
    }
}
