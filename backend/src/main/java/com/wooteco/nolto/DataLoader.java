package com.wooteco.nolto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedRepository;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    @Override
    public void run(ApplicationArguments args) {
        User mickey = new User(null, "user1@email.com", "user1", "미키", "imageUrl");
        List<User> users = Arrays.asList(
                mickey,
                new User(null, "user2@email.com", "user2", "아마찌", "imageUrl"),
                new User(null, "user3@email.com", "user3", "지그", "imageUrl"),
                new User(null, "user4@email.com", "user4", "포모", "imageUrl"),
                new User(null, "user5@email.com", "user5", "조엘", "imageUrl"),
                new User(null, "user6@email.com", "user6", "찰리", "imageUrl")
        );
        userRepository.saveAll(users);

        Feed feed1 = new Feed(new ArrayList<>(), "title1", "content1", Step.PROGRESS, true, "", "", "").writtenBy(mickey);
        Feed feed2 = new Feed(new ArrayList<>(), "title2", "content2", Step.COMPLETE, false, "", "http://woowa.jofilm.com", "").writtenBy(mickey);
        feedRepository.save(feed1);
        feedRepository.save(feed2);
    }
}
