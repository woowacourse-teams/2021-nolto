package com.wooteco.nolto.auth.ui;

import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.auth.ui.dto.TokenRequest;
import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest tokenRequest) {
        TokenResponse response = authService.login(tokenRequest);
        return ResponseEntity.ok(response);
    }
}
