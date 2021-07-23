package com.wooteco.nolto.auth.domain;

import com.wooteco.nolto.auth.ui.dto.OAuthTokenResponse;
import com.wooteco.nolto.user.domain.User;

public interface OAuthClient {

    User generateUserInfo(OAuthTokenResponse oauthToken);

    OAuthTokenResponse generateAccessToken(String code);

    boolean checkType(SocialType socialType);
}
