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

    private UserRepository userRepository;
    private FeedRepository feedRepository;

    @Override
    public void run(ApplicationArguments args) {
        User mickey = new User(null, "user1@email.com", "user1", "미키", "imageUrl", new ArrayList<>(), Collections.emptyList());
        List<User> users = Arrays.asList(
                mickey,
                new User(null, "user2@email.com", "user2", "아마찌", "imageUrl", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user3@email.com", "user3", "지그", "imageUrl", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user4@email.com", "user4", "포모", "imageUrl", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user5@email.com", "user5", "조엘", "imageUrl", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user6@email.com", "user6", "찰리", "imageUrl", Collections.emptyList(), Collections.emptyList())
        );
        userRepository.saveAll(users);

        Feed feed1 = new Feed(new ArrayList<>(), "title1", "content1", Step.PROGRESS, true, "", "", "").writtenBy(mickey);
        Feed feed2 = new Feed(new ArrayList<>(), "title2", "content2", Step.COMPLETE, false, "", "http://woowa.jofilm.com", "").writtenBy(mickey);
        feedRepository.save(feed1);
        feedRepository.save(feed2);
    }
}
