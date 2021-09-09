package com.wooteco.nolto.user.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequest {
    @NotBlank
    private String nickname;
    @NotNull
    private String bio = "";
    private MultipartFile image;

    public ProfileRequest(String nickname, String bio, MultipartFile image) {
        this.nickname = nickname;
        this.bio = bio;
        this.image = image;
    }
}
