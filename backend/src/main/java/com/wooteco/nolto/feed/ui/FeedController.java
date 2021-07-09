package com.wooteco.nolto.feed.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.UserAuthenticationPrincipal;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.ui.dto.FeedDetailResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@AllArgsConstructor
@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> create(@MemberAuthenticationPrincipal User user, @RequestBody FeedRequest request) {
        FeedDetailResponse response = feedService.create(user, request);
        return ResponseEntity.created(URI.create("/feeds/" + response.getId())).build();
    }

    @GetMapping(value = "/{feedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> findById(@UserAuthenticationPrincipal User user, @PathVariable Long feedId) {
        FeedResponse response = feedService.findById(user, feedId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{feedId}/like")
    public ResponseEntity<Void> addLike(@MemberAuthenticationPrincipal User user, @PathVariable Long feedId) {
        likeService.addLike(user, feedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedId}/like")
    public ResponseEntity<Void> deleteLike(@MemberAuthenticationPrincipal User user, @PathVariable Long feedId) {
        likeService.deleteLike(user, feedId);
        return ResponseEntity.ok().build();
    }
}
