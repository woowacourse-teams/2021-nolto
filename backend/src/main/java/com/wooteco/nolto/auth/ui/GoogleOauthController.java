package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.GoogleRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.GoogleUserResponse;
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
public class GoogleOauthController {
    @Value("${oauth.google.client.id}")
    private String clientId;

    @Value("${oauth.google.client.secret}")
    private String clientSecret;

    @Value("${oauth.google.scope}")
    private String scope;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    private final AuthService authService;

    public GoogleOauthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("login/oauth/google")
    public ResponseEntity<GoogleRedirectResponse> googleLogin() {
        return ResponseEntity.ok(new GoogleRedirectResponse(this.clientId, this.redirectUri, this.scope, "code"));
    }

    @GetMapping("login/oauth/google/token")
    public ResponseEntity<TokenResponse> googleSignUp(@RequestParam String code) {
        OauthTokenResponse token = generateAccessToken(code).getBody();
        GoogleUserResponse googleUserResponse = generateUserInfo(token).getBody();
        TokenResponse tokenResponse = authService.loginGoogle(Objects.requireNonNull(googleUserResponse));
        return ResponseEntity.ok(tokenResponse);

    }

    private ResponseEntity<GoogleUserResponse> generateUserInfo(OauthTokenResponse oauthToken) {
        HttpHeaders headers = requestUserInfoHeaders(oauthToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.GET,
                httpEntity,
                GoogleUserResponse.class
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
                "https://oauth2.googleapis.com/token",
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
        param.add("grant_type", "authorization_code");
        return param;
    }
}
