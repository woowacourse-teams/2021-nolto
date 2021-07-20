package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.GithubUserResponse;
import com.wooteco.nolto.auth.ui.dto.OauthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.OauthTokenResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class OauthController {

    @Value("${oauth.github.client.id}")
    private String clientId;

    @Value("${oauth.github.client.secret}")
    private String clientSecret;

    @Value("${oauth.github.scope}")
    private String scope;

    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    private final AuthService authService;

    public OauthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login/oauth/github")
    public ResponseEntity<OauthRedirectResponse> githubLogin() {
        return ResponseEntity.ok(new OauthRedirectResponse(this.clientId, this.redirectUri, this.scope));
    }

    @GetMapping("login/oauth/github/token")
    public ResponseEntity<TokenResponse> githubSignUp(@RequestParam String code) {
        OauthTokenResponse token = generateAccessToken(code).getBody();
        TokenResponse tokenResponse = authService.loginGithub(Objects.requireNonNull(generateUserInfo(token).getBody()));
        return ResponseEntity.ok(tokenResponse);
    }

    private ResponseEntity<GithubUserResponse> generateUserInfo(OauthTokenResponse oauthToken) {
        HttpHeaders headers = requestUserInfoHeaders(oauthToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                httpEntity,
                GithubUserResponse.class
        );
    }

    private HttpHeaders requestUserInfoHeaders(OauthTokenResponse oauthToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", oauthToken.getToken_type() + " " + oauthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset-utf-8");
        return headers;
    }

    private ResponseEntity<OauthTokenResponse> generateAccessToken(String code) {
        HttpEntity<MultiValueMap<String, String>> request = generateAccessTokenRequest(code);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                request,
                OauthTokenResponse.class
        );
    }

    private HttpEntity<MultiValueMap<String, String>> generateAccessTokenRequest(String code) {
        MultiValueMap<String, String> param = this.generateAccessTokenRequestParam(code);
        HttpHeaders headers = this.generateAccessTokenRequestHeaders();
        return new HttpEntity<>(param, headers);
    }

    private HttpHeaders generateAccessTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset-utf-8");
        return headers;
    }

    private MultiValueMap<String, String> generateAccessTokenRequestParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", clientId);
        param.add("client_secret", clientSecret);
        param.add("code", code);
        param.add("redirect_uri", redirectUri);
        return param;
    }
}