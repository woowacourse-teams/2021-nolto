package com.wooteco.nolto.admin.ui;

import com.wooteco.nolto.admin.application.AdminService;
import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> loginAsAdmin(@RequestBody AdminLoginRequest adminLoginRequest) {
        AdminLoginResponse adminAccessToken = adminService.login(adminLoginRequest);
        return ResponseEntity.ok(adminAccessToken);
    }
}
