package com.wooteco.nolto.user.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.ValidTokenRequired;
import com.wooteco.nolto.user.application.UserService;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @ValidTokenRequired
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@MemberAuthenticationPrincipal User user) {
        return ResponseEntity.ok(MemberResponse.of(user));
    }

    @ValidTokenRequired
    @GetMapping("/me/history")
    public ResponseEntity<MemberHistoryResponse> findHistory(@MemberAuthenticationPrincipal User user) {
        MemberHistoryResponse memberHistoryResponse = userService.findHistory(user);
        return ResponseEntity.ok(memberHistoryResponse);
    }
}
