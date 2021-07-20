package com.wooteco.nolto.auth.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleRedirectResponse {
    private String client_id;
    private String redirect_uri;
    private String scope;
    private String response_type;
}
