package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MemberHistoryResponse {
    private final List<FeedHistoryResponse> likedFeeds;
    private final List<FeedHistoryResponse> myFeeds;
    private final List<CommentHistoryResponse> myComments;

    public static MemberHistoryResponse of(List<Feed> likedFeeds, List<Feed> myFeeds, List<Comment> myComments) {
        return new MemberHistoryResponse(
                FeedHistoryResponse.toList(likedFeeds),
                FeedHistoryResponse.toList(myFeeds),
                CommentHistoryResponse.toList(myComments)
        );
    }
}
