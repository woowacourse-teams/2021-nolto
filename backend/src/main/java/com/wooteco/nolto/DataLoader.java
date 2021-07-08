package com.wooteco.nolto;

import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        List<User> users = Arrays.asList(
                new User(null, "user1@email.com", "user1", "미키", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user2@email.com", "user2", "아마찌", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user3@email.com", "user3", "지그", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user4@email.com", "user4", "포모", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user5@email.com", "user5", "조엘", Collections.emptyList(), Collections.emptyList()),
                new User(null, "user6@email.com", "user6", "찰리", Collections.emptyList(), Collections.emptyList())
        );
        userRepository.saveAll(users);
    }
}
