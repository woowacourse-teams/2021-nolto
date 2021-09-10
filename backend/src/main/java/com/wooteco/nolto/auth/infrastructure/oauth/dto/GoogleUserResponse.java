package com.wooteco.nolto.auth.infrastructure.oauth.dto;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleUserResponse implements OAuthUserResponse {
    private String sub;
    private String name;
    private String picture;

    public User toUser() {
        return new User(sub, SocialType.GOOGLE, name, picture);
    }
}
