package com.wooteco.nolto.tech.application;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class TechServiceTest {

    @Autowired
    private TechService techService;

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private FeedTechRepository feedTechRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private final Tech TECH1_JAVA = new Tech("Java");
    private final Tech TECH2_JAVASCRIPT = new Tech("Javascript");
    private final Tech TECH3_SPRING = new Tech("Spring");
    private final Tech TECH4_JPA = new Tech("Java Persistence API");
    private final Tech TECH5_SPARK_JAVA = new Tech("SparkJava");

    private final User user = new User("1L", SocialType.GOOGLE, "JOEL", "imageUrl");

    private final Feed FEED1 = new Feed("title1", "content1",
            Step.PROGRESS, true, "storageUrl", "", "http://thumbnailUrl.png").writtenBy(user);
    private final Feed FEED2 = new Feed("title2", "content2",
            Step.COMPLETE, false, "storageUrl", "deployUrl", "http://thumbnailUrl.png").writtenBy(user);
    private final Feed FEED3 = new Feed("title3", "content3",
            Step.PROGRESS, true, "storageUrl", "", "http://thumbnailUrl.png").writtenBy(user);
    private final Feed FEED4 = new Feed("title4", "content4",
            Step.PROGRESS, false, "", "deployUrl", "http://thumbnailUrl.png").writtenBy(user);
    private final Feed FEED5 = new Feed("title5", "content5",
            Step.COMPLETE, true, "storageUrl", "deployUrl", "http://thumbnailUrl.png").writtenBy(user);

    @BeforeEach
    void setUp() {
        techRepository.saveAll(Arrays.asList(TECH1_JAVA, TECH2_JAVASCRIPT, TECH3_SPRING, TECH4_JPA, TECH5_SPARK_JAVA));
        userRepository.save(user);
        feedRepository.saveAll(Arrays.asList(FEED1, FEED2, FEED3, FEED4, FEED5));
    }

    @DisplayName("기술이름으로 검색했을 때 해당 문자열이 포함된 기술 스택을 전부 조회할 수 있다.")
    @Test
    void findByTechsContains() {
        // given
        String searchWord = "Java";

        // when
        List<TechResponse> findTechs = techService.findByTechsContains(searchWord);

        // then
        assertThat(findTechs).hasSize(3);
        assertThat(findTechs).extracting("text").contains(TECH1_JAVA.getName(), TECH2_JAVASCRIPT.getName(), TECH4_JPA.getName());
    }

    @DisplayName("기술 이름중 일부 만으로 해당 문자열을 포함된 모든 기술 스택을 조회할 수 있다.")
    @Test
    void findByTechsContainsWithNonCompleteWord() {
        // given
        String searchWord = "Ja";

        // when
        List<TechResponse> findTechs = techService.findByTechsContains(searchWord);

        // then
        assertThat(findTechs).hasSize(3);
        assertThat(findTechs).extracting("text").contains(TECH1_JAVA.getName(), TECH2_JAVASCRIPT.getName(), TECH4_JPA.getName());
    }

    @DisplayName("검색하는 기술 이름에 정확하게 일치하는 기술 스택만 조회할 수 있다. 단, 대소문자는 구분하지 않는다.")
    @Test
    void findAllByNameInIgnoreCase() {
        // given
        String searchWord1 = "Java";
        String searchWord2 = "sPrinG";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).hasSize(1);
        assertThat(findTechs1).extracting("text").contains("Java");
        assertThat(findTechs2).hasSize(1);
        assertThat(findTechs2).extracting("text").contains("Spring");
    }

    @DisplayName("기술 이름을 ',' 로 구분하여 각각 정확하게 일치하는 여러개의 기술 스택을 검색행올 수 있다. 공백이 있을 시 자동으로 제거해준다.")
    @Test
    void findAllByNameInIgnoreCaseMultipleNames() {
        // given
        String searchWord1 = "Java,Spring";
        String searchWord2 = "Java,    Spring";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).hasSize(2);
        assertThat(findTechs1).extracting("text").contains("Java", "Spring");
        assertThat(findTechs2).hasSize(2);
        assertThat(findTechs2).extracting("text").contains("Java", "Spring");
    }

    @DisplayName("검색하는 기술 이름에 정확하게 일치하지 않으면 조회할 수 없다.")
    @Test
    void findAllByNameInIgnoreCaseWithNotEqualString() {
        // given
        String searchWord1 = "Ja";
        String searchWord2 = "Spr";

        // when
        List<TechResponse> findTechs1 = techService.findAllByNameInIgnoreCase(searchWord1);
        List<TechResponse> findTechs2 = techService.findAllByNameInIgnoreCase(searchWord2);

        // then
        assertThat(findTechs1).isEmpty();
        assertThat(findTechs2).isEmpty();
    }

    @DisplayName("전체 피드에서 가장 사용이 많이 된 트렌드 테크 스택 4가지를 조회할 수 있다.")
    @Test
    void findTrendTechs() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, TECH1_JAVA), new FeedTech(FEED1, TECH2_JAVASCRIPT),
                new FeedTech(FEED1, TECH3_SPRING), new FeedTech(FEED1, TECH4_JPA), new FeedTech(FEED1, TECH5_SPARK_JAVA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, TECH1_JAVA), new FeedTech(FEED2, TECH2_JAVASCRIPT),
                new FeedTech(FEED2, TECH3_SPRING), new FeedTech(FEED2, TECH4_JPA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, TECH1_JAVA), new FeedTech(FEED3, TECH2_JAVASCRIPT),
                new FeedTech(FEED3, TECH3_SPRING)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, TECH1_JAVA), new FeedTech(FEED4, TECH2_JAVASCRIPT)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED5, TECH1_JAVA)));

        //when
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).extracting("text")
                .containsExactly(TECH1_JAVA.getName(), TECH2_JAVASCRIPT.getName(), TECH3_SPRING.getName(), TECH4_JPA.getName());
    }

    @DisplayName("전체 피드에서 사용된 테크가 4개 미만이라면, 그것만 반환한다.")
    @Test
    void findTrendTechsUnderFourTechs() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, TECH1_JAVA), new FeedTech(FEED3, TECH2_JAVASCRIPT),
                new FeedTech(FEED3, TECH3_SPRING)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, TECH1_JAVA), new FeedTech(FEED4, TECH2_JAVASCRIPT)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED5, TECH1_JAVA)));

        //when
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).extracting("text")
                .containsExactly(TECH1_JAVA.getName(), TECH2_JAVASCRIPT.getName(), TECH3_SPRING.getName());
    }

    @DisplayName("기술을 사용한 피드가 전혀 없으면 빈 리스트를 반환한다.")
    @Test
    void findTrendTechsWhenFeedDontUseAnyTech() {
        //when
        List<TechResponse> techResponses = techService.findTrendTechs();

        //then
        assertThat(techResponses).isEmpty();
    }
}
