package com.wooteco.nolto.feed.ui;

import com.wooteco.nolto.auth.MemberAuthenticationPrincipal;
import com.wooteco.nolto.auth.UserAuthenticationPrincipal;
import com.wooteco.nolto.feed.application.FeedService;
import com.wooteco.nolto.feed.application.LikeService;
import com.wooteco.nolto.feed.ui.dto.FeedCardResponse;
import com.wooteco.nolto.feed.ui.dto.FeedRequest;
import com.wooteco.nolto.feed.ui.dto.FeedResponse;
import com.wooteco.nolto.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> create(@MemberAuthenticationPrincipal User user, @ModelAttribute @Valid FeedRequest request) {
        Long feedId = feedService.create(user, request);
        return ResponseEntity.created(URI.create("/feeds/" + feedId)).build();
    }

    @GetMapping(value = "/{feedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeedResponse> findById(@UserAuthenticationPrincipal User user, @PathVariable Long feedId) {
        FeedResponse response = feedService.findById(user, feedId);
        return ResponseEntity.ok(response);
    }

    @PutMapping( "/{feedId}")
    public ResponseEntity<Void> update(@MemberAuthenticationPrincipal User user, @PathVariable Long feedId, @ModelAttribute @Valid FeedRequest request) {
        feedService.update(user, feedId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> delete(@MemberAuthenticationPrincipal User user, @PathVariable Long feedId) {
        feedService.delete(user, feedId);
        return ResponseEntity.noContent().build();
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

    @GetMapping("/recent")
    public ResponseEntity<List<FeedCardResponse>> recentResponse(@RequestParam(required = false, defaultValue = "all") String filter) {
        List<FeedCardResponse> feeds = feedService.findAll(filter);
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/hot")
    public ResponseEntity<List<FeedCardResponse>> hotResponse() {
        List<FeedCardResponse> feeds = feedService.findHotFeeds();
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FeedCardResponse>> searchResponse(@RequestParam(required = false, defaultValue = "") String query,
                                                                 @RequestParam(required = false, defaultValue = "") String techs) {
        List<FeedCardResponse> feeds = feedService.search(query, techs);
        return ResponseEntity.ok(feeds);
    }
}
