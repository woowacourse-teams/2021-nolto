package com.wooteco.nolto.admin.ui;

import com.wooteco.nolto.admin.AdminAuthenticationPrincipal;
import com.wooteco.nolto.admin.application.AdminService;
import com.wooteco.nolto.admin.ui.dto.AdminLoginRequest;
import com.wooteco.nolto.admin.ui.dto.AdminLoginResponse;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.admin.ui.dto.CommentsByFeedResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.admin.ui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<List<FeedResponse>> getAllFeeds(@AdminAuthenticationPrincipal User adminUser) {
        List<FeedResponse> feedResponses = adminService.findAllFeeds(adminUser);
        return ResponseEntity.ok(feedResponses);
    }

    @ValidTokenRequired
    @PutMapping("/feeds/{feedId:[\\d]+}")
    public ResponseEntity<Void> updateFeed(@AdminAuthenticationPrincipal User adminUser, @PathVariable Long feedId,
                                           @ModelAttribute @Valid FeedRequest request) {
        adminService.updateFeed(adminUser, feedId, request);
        return ResponseEntity.ok().build();
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

    @ValidTokenRequired
    @GetMapping("/comments")
    public ResponseEntity<List<CommentsByFeedResponse>> getAllComments(@AdminAuthenticationPrincipal User adminUser) {
        List<CommentsByFeedResponse> commentsByFeedResponse = adminService.findAllComments(adminUser);
        return ResponseEntity.ok(commentsByFeedResponse);
    }

    @ValidTokenRequired
    @DeleteMapping("/comments/{commentId:[\\d]+}")
    public ResponseEntity<Void> deleteComment(@AdminAuthenticationPrincipal User adminUser, @PathVariable Long commentId) {
        adminService.deleteComment(adminUser, commentId);
        return ResponseEntity.noContent().build();
    }

    @ValidTokenRequired
    @GetMapping("/techs")
    public ResponseEntity<List<TechResponse>> findAllTechs(@AdminAuthenticationPrincipal User adminUser) {
        List<TechResponse> allTechs = adminService.findAllTechs(adminUser);
        return ResponseEntity.ok(allTechs);
    }

    @ValidTokenRequired
    @DeleteMapping("/techs/{techId:[\\d]+}")
    public ResponseEntity<Void> deleteTech(@AdminAuthenticationPrincipal User adminUser, @PathVariable Long techId) {
        adminService.deleteTech(adminUser, techId);
        return ResponseEntity.noContent().build();
    }
}
