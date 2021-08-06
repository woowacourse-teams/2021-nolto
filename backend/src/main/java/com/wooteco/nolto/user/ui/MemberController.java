package com.wooteco.nolto.user.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.notification.application.NotificationService;
import com.wooteco.nolto.user.application.MemberService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members/me")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ValidTokenRequired
    @GetMapping
    public ResponseEntity<MemberResponse> findMemberOfMine(@MemberAuthenticationPrincipal User user) {
        MemberResponse response = memberService.findMemberOfMine(user);
        return ResponseEntity.ok(response);
    }

    @ValidTokenRequired
    @GetMapping("/history")
    public ResponseEntity<MemberHistoryResponse> findHistory(@MemberAuthenticationPrincipal User user) {
        MemberHistoryResponse memberHistoryResponse = memberService.findHistory(user);
        return ResponseEntity.ok(memberHistoryResponse);
    }

    @ValidTokenRequired
    @GetMapping("/profile/validation")
    public ResponseEntity<NicknameValidationResponse> validateDuplicatedNickname(@MemberAuthenticationPrincipal User user,
                                                                                 @RequestParam String nickname) {
        NicknameValidationResponse response = memberService.validateDuplicated(nickname);
        return ResponseEntity.ok(response);
    }

    @ValidTokenRequired
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> findProfileOfMine(@MemberAuthenticationPrincipal User user) {
        ProfileResponse response = memberService.findProfile(user);
        return ResponseEntity.ok(response);
    }

    @ValidTokenRequired
    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfileOfMine(@MemberAuthenticationPrincipal User user, @Valid @ModelAttribute ProfileRequest profileRequest) {
        ProfileResponse response = memberService.updateProfile(user, profileRequest);
        return ResponseEntity.ok(response);
    }

    @ValidTokenRequired
    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponse>> findNotifications(@MemberAuthenticationPrincipal User user) {
        List<NotificationResponse> notificationResponses = memberService.findNotifications(user);
        return ResponseEntity.ok(notificationResponses);
    }

    @ValidTokenRequired
    @DeleteMapping("/notifications/{notificationId:[\\d]+}")
    public ResponseEntity<Void> deleteNotification(@MemberAuthenticationPrincipal User user, @PathVariable Long notificationId) {
        memberService.deleteNotification(user, notificationId);
        return ResponseEntity.noContent().build();
    }

    @ValidTokenRequired
    @DeleteMapping("/notifications")
    public ResponseEntity<Void> deleteAllNotifications(@MemberAuthenticationPrincipal User user) {
        memberService.deleteAllNotifications(user);
        return ResponseEntity.noContent().build();
    }
}
