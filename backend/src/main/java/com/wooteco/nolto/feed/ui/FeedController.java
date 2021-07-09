package com.wooteco.nolto.feed.ui;

import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.ui.dto.FeedDetailResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.AuthenticationPrincipal;
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

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal User user, @RequestBody FeedRequest request) {
        FeedDetailResponse response = feedService.create(user, request);
        return ResponseEntity.created(URI.create("/feeds/" + response.getId())).build();
    }

    @GetMapping(value = "/{feedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> findById(@AuthenticationPrincipal User user, @PathVariable Long feedId) {
        FeedResponse response = feedService.findById(user, feedId);
        return ResponseEntity.ok(response);
    }
}
