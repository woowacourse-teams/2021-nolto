package com.wooteco.nolto.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

}
