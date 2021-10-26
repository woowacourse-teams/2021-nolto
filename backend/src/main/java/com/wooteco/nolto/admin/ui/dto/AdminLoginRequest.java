package com.wooteco.nolto.admin.ui.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginRequest {

    private final String id;
    private final String password;

    public AdminLoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
