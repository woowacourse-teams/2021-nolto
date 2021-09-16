package com.wooteco.nolto.auth.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class TokenRequest {

    @Email(message = "올바른 Email 양식이 아닙니다.")
    @NotBlank(message = "Email은 빈 값일 수 없습니다.")
    private String email;

    @NotBlank(message = "password는 빈 값일 수 없습니다.")
    private String password;
}
