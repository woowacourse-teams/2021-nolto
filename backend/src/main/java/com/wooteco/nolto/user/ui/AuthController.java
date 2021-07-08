package com.wooteco.nolto.user.ui;

import com.wooteco.nolto.user.application.AuthService;
import com.wooteco.nolto.user.ui.dto.TokenRequest;
import com.wooteco.nolto.user.ui.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse response = authService.login(tokenRequest);
        return ResponseEntity.ok(response);
    }
}
