package com.wooteco.nolto.admin.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminLoginResponse {

    private String adminAccessToken;

    public AdminLoginResponse(String adminAccessToken) {
        this.adminAccessToken = adminAccessToken;
    }
}
