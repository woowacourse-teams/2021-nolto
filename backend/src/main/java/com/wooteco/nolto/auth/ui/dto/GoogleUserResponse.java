package com.wooteco.nolto.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoogleUserResponse {
    private String sub;
    private String name;
    private String picture;
}
