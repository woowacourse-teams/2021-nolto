package com.wooteco.nolto.user.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.user.application.MemberService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import com.wooteco.nolto.user.ui.dto.NicknameValidationResponse;
import com.wooteco.nolto.user.ui.dto.ProfileRequest;
import com.wooteco.nolto.user.ui.dto.ProfileResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members/me")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ValidTokenRequired
    @GetMapping
    public ResponseEntity<MemberResponse> findMemberOfMine(@MemberAuthenticationPrincipal User user) {
        // TODO user의 notifications 수 구하는 로직 필요, user안에서 notifications 가지고 있게 할것인과
        return ResponseEntity.ok(MemberResponse.of(user));
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
}
