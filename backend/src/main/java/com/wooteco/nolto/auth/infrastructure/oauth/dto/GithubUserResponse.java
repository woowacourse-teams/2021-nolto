package com.wooteco.nolto.auth.infrastructure.oauth.dto;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserResponse implements OAuthUserResponse {
    private Long id;
    private String login;
    private String avatar_url;

    public User toUser() {
        return new User(String.valueOf(id), SocialType.GITHUB, login, avatar_url);
    }
}
