package com.wooteco.nolto.feed.ui;

import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.AuthenticationPrincipal;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@AllArgsConstructor
@RestController("/feeds")
public final class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal User user ,@RequestBody FeedRequest request) {
        FeedResponse response = feedService.create(user, request);
        return ResponseEntity.created(URI.create("/feeds/" + response.getId())).build();
    }
}
