package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OAuthController {
    private final AuthService authService;

    @GetMapping("login/oauth/{socialType}")
    public ResponseEntity<OAuthRedirectResponse> requestSocialRedirect(@PathVariable String socialType) {
        return ResponseEntity.ok(authService.requestSocialRedirect(socialType));
    }

    @GetMapping("login/oauth/{socialType}/token")
    public ResponseEntity<TokenResponse> signInOAuth(@PathVariable String socialType, @RequestParam String code) {
        TokenResponse tokenResponse = authService.oAuthSignIn(socialType, code);
        return ResponseEntity.ok(tokenResponse);
    }
}
