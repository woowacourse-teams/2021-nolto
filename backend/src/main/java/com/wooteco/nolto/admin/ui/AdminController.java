package com.wooteco.nolto.admin.ui;

import com.wooteco.nolto.admin.AdminAuthenticationPrincipal;
import com.wooteco.nolto.admin.application.AdminService;
import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ValidTokenRequired
    @GetMapping("/feeds")
    public ResponseEntity<List<FeedCardResponse>> getAllFeeds(@AdminAuthenticationPrincipal User user) {
        List<FeedCardResponse> feedCardResponses = adminService.findAllFeeds(user);
        return ResponseEntity.ok(feedCardResponses);
    }
}
