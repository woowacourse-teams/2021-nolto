package com.wooteco.nolto.admin.ui;

import com.wooteco.nolto.admin.AdminAuthenticationPrincipal;
import com.wooteco.nolto.admin.application.AdminService;
import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
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
    public ResponseEntity<List<FeedCardResponse>> getAllFeeds(@AdminAuthenticationPrincipal User adminUser) {
        List<FeedCardResponse> feedCardResponses = adminService.findAllFeeds(adminUser);
        return ResponseEntity.ok(feedCardResponses);
    }

    @ValidTokenRequired
    @DeleteMapping("/feeds/{feedId:[\\d]+}")
    public ResponseEntity<Void> deleteFeed(@AdminAuthenticationPrincipal User adminUser, @PathVariable Long feedId) {
        adminService.deleteFeed(adminUser, feedId);
        return ResponseEntity.noContent().build();
    }

    @ValidTokenRequired
    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(@AdminAuthenticationPrincipal User adminUser) {
        List<UserResponse> userResponses = adminService.findAllUsers(adminUser);
        return ResponseEntity.ok(userResponses);
    }

    @ValidTokenRequired
    @DeleteMapping("/users/{userId:[\\d]+}")
    public ResponseEntity<Void> deleteUser(@AdminAuthenticationPrincipal User adminUser, @PathVariable Long userId) {
        adminService.deleteUser(adminUser, userId);
        return ResponseEntity.noContent().build();
    }
}
