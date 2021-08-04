package com.wooteco.nolto.user.ui.dto;

public class NicknameValidationResponse {
    private final boolean isUsable;

    public NicknameValidationResponse(boolean isUsable) {
        this.isUsable = isUsable;
    }
}
