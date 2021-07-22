package com.wooteco.nolto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
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

@Profile("local")
@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final TechRepository techRepository;

    @Override
    public void run(ApplicationArguments args) {
        User mickey = new User(null, "1", "github", "미키", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg");
        List<User> users = Arrays.asList(
                mickey,
                new User(null, "2", "github", "아마찌", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User(null, "3", "github", "지그", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User(null, "4", "github", "포모", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User(null, "5", "github", "조엘", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg"),
                new User(null, "6", "github", "찰리", "https://dksykemwl00pf.cloudfront.net/amazzi.jpeg")
        );
        userRepository.saveAll(users);

        Tech tech1 = new Tech("Apollo Client");
        Tech tech2 = new Tech("WebGL");
        Tech saveTech1 = techRepository.save(tech1);
        Tech saveTech2 = techRepository.save(tech2);

        Feed feed1 = new Feed("title1", "content1", Step.PROGRESS, true, "https://github.com/woowacourse-teams/2021-nolto", "", "https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png").writtenBy(mickey);
        Feed feed2 = new Feed("title2", "content2", Step.COMPLETE, false, "https://github.com/woowacourse-teams/2021-nolto", "http://woowa.jofilm.com", "https://dksykemwl00pf.cloudfront.net/KakaoTalk_Photo_2021-07-19-14-25-01.png").writtenBy(mickey);
        feed1.setFeedTechs(Arrays.asList(new FeedTech(feed1, saveTech1)));
        feed2.setFeedTechs(Arrays.asList(new FeedTech(feed2, saveTech1)));
        feed2.setFeedTechs(Arrays.asList(new FeedTech(feed2, saveTech2)));

        Feed saveFeed1 = feedRepository.save(feed1);
        Feed saveFeed2 = feedRepository.save(feed2);
    }
}
