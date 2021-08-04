package com.wooteco.nolto.user.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.user.application.UserService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import com.wooteco.nolto.user.ui.dto.ProfileResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @ValidTokenRequired
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@MemberAuthenticationPrincipal User user) {
        // TODO user의 notifications 수 구하는 로직 필요, user안에서 notifications 가지고 있게 할것인과
        return ResponseEntity.ok(MemberResponse.of(user));
    }

    @ValidTokenRequired
    @GetMapping("/me/profile/validation")
    public ResponseEntity<MemberResponse> validateDuplicatedNickname(@MemberAuthenticationPrincipal User user,
                                                                     @RequestParam String nickname) {
        userService.validateDuplicated(nickname);
        return ResponseEntity.ok(MemberResponse.of(user));
    }

    @ValidTokenRequired
    @GetMapping("/me/profile")
    public ResponseEntity<ProfileResponse> findProfileOfMine(@MemberAuthenticationPrincipal User user) {
        ProfileResponse response = userService.findProfile(user);
        return ResponseEntity.ok(response);
    }
}
