package com.wooteco.nolto.auth.infrastructure.oauth.dto;

import com.wooteco.nolto.user.domain.User;

public interface OAuthUserResponse {
    User toUser();
}
