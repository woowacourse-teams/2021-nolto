package com.wooteco.nolto.user.application;

import com.wooteco.nolto.feed.domain.Comment;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public MemberHistoryResponse findHistory(User user) {
        List<Feed> likedFeeds = user.findLikedFeeds();
        List<Feed> myFeeds = user.getFeeds();
        List<Comment> myComments = user.getComments();
        return MemberHistoryResponse.of(likedFeeds, myFeeds, myComments);
    }
}
