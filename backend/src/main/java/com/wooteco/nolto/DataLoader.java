package com.wooteco.nolto;

import com.wooteco.nolto.feed.domain.*;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Profile("!test")
@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final TechRepository techRepository;
    private final FeedTechRepository feedTechRepository;

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

        Tech tech1 = new Tech("Apollo Client");
        Tech tech2 = new Tech("WebGL");
        Tech saveTech1 = techRepository.save(tech1);
        Tech saveTech2 = techRepository.save(tech2);

        Feed feed1 = new Feed("title1", "content1", Step.PROGRESS, true, "", "", "").writtenBy(mickey);
        Feed feed2 = new Feed("title2", "content2", Step.COMPLETE, false, "", "http://woowa.jofilm.com", "").writtenBy(mickey);
        Feed saveFeed1 = feedRepository.save(feed1);
        Feed saveFeed2 = feedRepository.save(feed2);

        feedTechRepository.save(new FeedTech(saveFeed1, saveTech1));
        feedTechRepository.save(new FeedTech(saveFeed1, saveTech2));
        feedTechRepository.save(new FeedTech(saveFeed2, saveTech1));
        feedTechRepository.save(new FeedTech(saveFeed2, saveTech2));
    }
}
