package com.wooteco.nolto.user.ui.dto;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberHistoryResponse {
    private final List<FeedHistoryResponse> likedFeeds;
    private final List<FeedHistoryResponse> myFeeds;
    private final List<CommentHistoryResponse> myComments;

    public MemberHistoryResponse(List<FeedHistoryResponse> likedFeeds, List<FeedHistoryResponse> myFeeds, List<CommentHistoryResponse> myComments) {
        this.likedFeeds = likedFeeds;
        this.myFeeds = myFeeds;
        this.myComments = myComments;
    }

    public static MemberHistoryResponse of(List<Feed> likedFeeds, List<Feed> myFeeds, List<Comment> myComments) {
        return new MemberHistoryResponse(
                FeedHistoryResponse.toList(likedFeeds),
                FeedHistoryResponse.toList(myFeeds),
                CommentHistoryResponse.toList(myComments)
        );
    }
}
