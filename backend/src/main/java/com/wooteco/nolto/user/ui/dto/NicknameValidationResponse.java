package com.wooteco.nolto.user.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NicknameValidationResponse {
    private final boolean isIsUsable;
}
