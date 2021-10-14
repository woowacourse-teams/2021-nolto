package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.RefreshTokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    private final AuthService authService;

    @GetMapping("login/oauth/{socialType}")
    public ResponseEntity<OAuthRedirectResponse> requestSocialRedirect(@PathVariable String socialType) {
        return ResponseEntity.ok(authService.requestSocialRedirect(socialType));
    }

    @GetMapping("login/oauth/{socialType}/token")
    public ResponseEntity<TokenResponse> signInOAuth(@PathVariable String socialType,
                                                     @RequestParam String code,
                                                     HttpServletRequest request) {
        log.info("Remote Address : {}", request.getLocalAddr());
        TokenResponse tokenResponse = authService.oAuthSignIn(socialType, code, request.getRemoteAddr());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("login/oauth/refreshToken")
    public ResponseEntity<TokenResponse> generateRefreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse tokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(tokenResponse);
    }
}
