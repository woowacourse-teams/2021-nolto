package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.AccessTokenResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.RefreshTokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.util.cookie.RefreshTokenCookieManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public ResponseEntity<AccessTokenResponse> signInOAuth(@PathVariable String socialType,
                                                           @RequestParam String code,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {
        log.info("Remote Address : {}", request.getLocalAddr());
        TokenResponse tokenResponse = authService.oAuthSignIn(socialType, code, request.getRemoteAddr());
        RefreshTokenCookieManager.setRefreshToken(response, tokenResponse);
        return ResponseEntity.ok(tokenResponse.getAccessTokenResponse());
    }

    @PostMapping("login/oauth/refreshToken")
    public ResponseEntity<AccessTokenResponse> generateRefreshToken(@RequestBody RefreshTokenRequest request,
                                                                    HttpServletResponse response) {
        TokenResponse tokenResponse = authService.reissueToken(request);
        RefreshTokenCookieManager.setRefreshToken(response, tokenResponse);
        return ResponseEntity.ok(tokenResponse.getAccessTokenResponse());
    }
}
