package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.AllTokenResponse;
import com.wooteco.nolto.auth.ui.dto.OAuthRedirectResponse;
import com.wooteco.nolto.auth.ui.dto.RefreshTokenRequest;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
    public ResponseEntity<AllTokenResponse> signInOAuth(@PathVariable String socialType,
                                                        @RequestParam String code,
                                                        HttpServletRequest request) {
        AllTokenResponse allTokenResponse = authService.oAuthSignIn(socialType, code, getRemoteAddress(request));
        return ResponseEntity.ok(allTokenResponse);
    }

    private String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (Objects.isNull(ip)) {
            throw new NotFoundException(ErrorType.ADDRESS_NOT_FOUND);
        }
        log.info("Remote Address : {}", ip);
        return ip;
    }


    @PostMapping("login/oauth/refreshToken")
    public ResponseEntity<AllTokenResponse> generateRefreshToken(@RequestBody RefreshTokenRequest request) {
        AllTokenResponse allTokenResponse = authService.refreshToken(request);
        return ResponseEntity.ok(allTokenResponse);
    }
}
