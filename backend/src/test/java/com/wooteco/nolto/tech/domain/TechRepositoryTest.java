package com.wooteco.nolto.tech.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.FeedTech;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.feed.domain.repository.FeedTechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TechRepositoryTest {

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

    @DisplayName("스택 이름의 첫 단어를 검색한다.")
    @Test
    void findByFirstNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("j");

        // then
        assertThat(findTechs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT, TECH4_JPA);
    }

    @DisplayName("스택 이름의 중간 단어를 검색 안 한다.")
    @Test
    void findByMiddleNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("va");

        // then
        assertThat(findTechs).doesNotContain(TECH1_JAVA, TECH2_JAVASCRIPT);
    }

    @DisplayName("스택 이름의 마지막 단어를 검색 안 한다.")
    @Test
    void findByLastNameContains() {
        // when
        List<Tech> findTechs = techRepository.findByNameStartsWithIgnoreCase("ing");

        // then
        assertThat(findTechs).doesNotContain(TECH3_SPRING);
    }

    @DisplayName("테크 이름 리스트로 그에 대응하는 테크 목록을 받아올 수 있다.")
    @Test
    void findAllByName() {
        // when
        List<String> techNames = Arrays.asList("Java", "Javascript", "Spring");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT, TECH3_SPRING);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 없다면 가져오지 않는다.")
    @Test
    void findAllByNameNoMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).hasSize(0);
    }

    @DisplayName("테크 이름 리스트에 대응하는 테크가 있기도 하고 없기도 하다면, 대응되는 테크만 가져온다")
    @Test
    void findAllByNameSeveralMatching() {
        // when
        List<String> techNames = Arrays.asList("real-wow-tech", "Java", "Javascript");
        List<Tech> techs = techRepository.findAllByNameInIgnoreCase(techNames);

        // then
        assertThat(techs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT);
    }

    @DisplayName("테크 이름이 정확히 일치하는 테크들만 가져온다.(단, 대소문자는 구분하지 않는다.)")
    @Test
    void findAllByNameInIgnoreCase() {
        // given
        String techWord1 = "Java";
        String techWord2 = "jaVA";
        String techWord3 = "Java,JavaScr,UnidentifiedTech";

        String[] splitTechWord1 = techWord1.split(",");
        String[] splitTechWord2 = techWord2.split(",");
        String[] splitTechWord3 = techWord3.split(",");

        // when
        List<Tech> findTechs1 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord1));
        List<Tech> findTechs2 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord2));
        List<Tech> findTechs3 = techRepository.findAllByNameInIgnoreCase(Arrays.asList(splitTechWord3));

        // then
        assertThat(findTechs1).extracting("name").contains("Java");
        assertThat(findTechs2).extracting("name").contains("Java");
        assertThat(findTechs3).extracting("name").contains("Java");
    }

    @DisplayName("테크 이름이 정확히 일치하는 테크들만 가져온다.(단, 대소문자는 구분하지 않는다.)")
    @Test
    void findAllByNameInIgnoreCaseWithNonExistTech() {
        // given
        String techWord = "UnidentifiedTech";

        // when
        List<Tech> findTechs1 = techRepository.findAllByNameInIgnoreCase(Collections.singletonList(techWord));

        // then
        assertThat(findTechs1).isEmpty();
    }

    @DisplayName("전체 피드 중에서 현재 어떤 테크가 가장 많이 쓰이고 있는지 조회할 수 있다.")
    @Test
    void findTrendTech() {
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
        Pageable topTwoTechs = PageRequest.of(0, 2);
        List<Tech> topTwoTrendTechs = techRepository.findTrendTech(topTwoTechs);

        Pageable topThreeTechs = PageRequest.of(0, 3);
        List<Tech> TopThreeTrendTechs = techRepository.findTrendTech(topThreeTechs);

        Pageable topFourTechs = PageRequest.of(0, 4);
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(topTwoTrendTechs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT);
        assertThat(TopThreeTrendTechs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT, TECH3_SPRING);
        assertThat(TopFourTrendTechs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT, TECH3_SPRING, TECH4_JPA);
    }

    @DisplayName("트렌트 테크 조회 시, 동점의 경우 tech_id의 오름차순으로 결과를 정렬해 조회해 올 수 있다.")
    @Test
    void findTrendTechWhenEven() {
        //given
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, TECH1_JAVA), new FeedTech(FEED1, TECH2_JAVASCRIPT),
                new FeedTech(FEED1, TECH3_SPRING), new FeedTech(FEED1, TECH4_JPA), new FeedTech(FEED1, TECH5_SPARK_JAVA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, TECH1_JAVA), new FeedTech(FEED2, TECH2_JAVASCRIPT),
                new FeedTech(FEED2, TECH3_SPRING), new FeedTech(FEED2, TECH4_JPA), new FeedTech(FEED2, TECH5_SPARK_JAVA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, TECH1_JAVA), new FeedTech(FEED3, TECH2_JAVASCRIPT),
                new FeedTech(FEED3, TECH3_SPRING), new FeedTech(FEED3, TECH4_JPA), new FeedTech(FEED3, TECH5_SPARK_JAVA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, TECH1_JAVA), new FeedTech(FEED4, TECH2_JAVASCRIPT),
                new FeedTech(FEED4, TECH3_SPRING), new FeedTech(FEED4, TECH4_JPA), new FeedTech(FEED4, TECH5_SPARK_JAVA)));

        //when
        Pageable topFourTechs = PageRequest.of(0, 4, Sort.by("tech").ascending());
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(TopFourTrendTechs).containsExactly(TECH1_JAVA, TECH2_JAVASCRIPT, TECH3_SPRING, TECH4_JPA);
    }

    @DisplayName("트렌트 테크 조회 시, 동점의 경우 tech_id의 오름차순으로 결과를 조회해 올 수 있다2.")
    @Test
    void findTrendTechWhenEven2() {
        //given (JAVA - 3개 | JAVASCRIPT - 4개 | SPRING - 2개 | JPA - 3개)
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED1, TECH1_JAVA), new FeedTech(FEED1, TECH2_JAVASCRIPT),
                new FeedTech(FEED1, TECH3_SPRING), new FeedTech(FEED1, TECH4_JPA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED2, TECH1_JAVA), new FeedTech(FEED2, TECH2_JAVASCRIPT),
                new FeedTech(FEED2, TECH3_SPRING), new FeedTech(FEED2, TECH4_JPA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED3, TECH1_JAVA), new FeedTech(FEED3, TECH2_JAVASCRIPT),
                new FeedTech(FEED3, TECH4_JPA)));

        feedTechRepository.saveAll(Arrays.asList(new FeedTech(FEED4, TECH2_JAVASCRIPT)));

        //when
        Pageable topFourTechs = PageRequest.of(0, 4, Sort.by("tech").ascending());
        List<Tech> TopFourTrendTechs = techRepository.findTrendTech(topFourTechs);

        //then
        assertThat(TopFourTrendTechs).containsExactly(TECH2_JAVASCRIPT, TECH1_JAVA, TECH4_JPA, TECH3_SPRING);
    }
}
