package com.wooteco.nolto.user.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NicknameValidationResponse {
    private boolean isIsUsable;

    public NicknameValidationResponse(boolean isIsUsable) {
        this.isIsUsable = isIsUsable;
    }
}
