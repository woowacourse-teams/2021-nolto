package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.tech.domain.Tech;
import com.wooteco.nolto.tech.domain.TechRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static com.wooteco.nolto.UserFixture.아마찌_생성;
import static com.wooteco.nolto.UserFixture.포모_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedRepositoryTest {

    public static final String DEFAULT_THUMBNAIL_IMAGE = "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png";

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechRepository techRepository;

    private User 아마찌;
    private User 포모;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    private Tech techJava;
    private Tech techPython;
    private Tech techJavaScript;

    @BeforeEach
    void setUp() {
        아마찌 = 아마찌_생성();
        포모 = 포모_생성();

        userRepository.save(아마찌);
        userRepository.save(포모);

        feed1 = Feed.builder()
                .title("title1")
                .content("content1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2 = Feed.builder()
                .title("title2")
                .content("content2")
                .step(Step.PROGRESS)
                .isSos(false)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed3 = Feed.builder()
                .title("title3")
                .content("content3")
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl("www.github.com/newWisdom")
                .deployedUrl("www.github.com/woowacourse")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();

        techJava = new Tech("Java");
        techPython = new Tech("Python");
        techJavaScript = new Tech("JavaScript");
        techRepository.saveAll(Arrays.asList(techJava, techPython, techJavaScript));
    }

    @DisplayName("토이 프로젝트 글을 의미하는 Feed를 저장한다.")
    @Test
    void save() {
        // given
        feed1.writtenBy(아마찌);
        feed2.writtenBy(포모);
        feed3.writtenBy(포모);

        // when
        Feed savedFeed1 = feedRepository.save(feed1);
        Feed savedFeed2 = feedRepository.save(feed2);
        Feed savedFeed3 = feedRepository.save(feed3);

        // then
        assertThat(savedFeed1.getId()).isNotNull();
        checkSame(feed1, savedFeed1);

        assertThat(savedFeed2.getId()).isNotNull();
        checkSame(feed2, savedFeed2);

        assertThat(savedFeed3.getId()).isNotNull();
        checkSame(feed3, savedFeed3);
    }

    private void checkSame(Feed feed1, Feed feed2) {
        assertThat(feed1.getTitle()).isEqualTo(feed2.getTitle());
        assertThat(feed1.getContent()).isEqualTo(feed2.getContent());
        assertThat(feed1.getStep()).isEqualTo(feed2.getStep());
        assertThat(feed1.isSos()).isEqualTo(feed2.isSos());
        assertThat(feed1.getStorageUrl()).isEqualTo(feed2.getStorageUrl());
        assertThat(feed1.getDeployedUrl()).isEqualTo(feed2.getDeployedUrl());
        assertThat(feed1.getThumbnailUrl()).isEqualTo(feed2.getThumbnailUrl());
        assertThat(feed1.getViews()).isEqualTo(feed2.getViews());
        assertThat(feed1.getLikes()).isEqualTo(feed2.getLikes());
    }

    @DisplayName("피드의 title 혹은 content에 대응하는 문자열이 있고, feedId/step/isSos 조건들에 만족하는 피드를 페이지네이션으로 검색할 수 있다.")
    @Test
    void findByQuery() {
        //given
        feed1 = Feed.builder()
                .title("title1")
                .content("content1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2 = Feed.builder()
                .title("title2")
                .content("content2")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed3 = Feed.builder()
                .title("title3")
                .content("content3")
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl("www.github.com/newWisdom")
                .deployedUrl("www.github.com/woowacourse")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();

        feedRepository.save(feed1.writtenBy(아마찌));
        feedRepository.save(feed2.writtenBy(아마찌));
        feedRepository.save(feed3.writtenBy(아마찌));

        //when
        String properQuery = "cOnTen";
        String improperQuery = "nothing";

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        Long nextFeedId = 1000000L;

        EnumSet<Step> everyEnumSet = Step.asEnumSet("everything");
        EnumSet<Step> progressEnumSet = Step.asEnumSet("progress");
        EnumSet<Step> completeEnumSet = Step.asEnumSet("complete");

        Pageable maxThreePages = PageRequest.of(0, 15);

        List<Feed> everyStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        List<Feed> everyStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        List<Feed> everyStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        //then
        assertThat(everyStepTrueHelpConditionProperQuery).containsExactly(feed2, feed1);
        assertThat(progressStepTrueHelpConditionProperQuery).containsExactly(feed2, feed1);
        assertThat(completeStepTrueHelpConditionProperQuery).isEmpty();

        assertThat(everyStepNoHelpConditionProperQuery).containsExactly(feed3, feed2, feed1);
        assertThat(progressStepNoHelpConditionProperQuery).containsExactly(feed2, feed1);
        assertThat(completeStepNoHelpConditionProperQuery).containsExactly(feed3);

        assertThat(everyStepNoHelpConditionImproperQuery).isEmpty();
        assertThat(progressStepNoHelpConditionImproperQuery).isEmpty();
        assertThat(completeStepNoHelpConditionImproperQuery).isEmpty();
    }

    @DisplayName("검색된 기술들 중 하나라도 사용했으며, feedId/step/isSos 조건들에 만족하는 피드를 페이지네이션으로 검색할 수 있다.")
    @Test
    void findByTechs() {
        //given
        feed1 = Feed.builder()
                .title("title1")
                .content("content1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed1.changeTechs(Arrays.asList(techJava, techJavaScript));

        feed2 = Feed.builder()
                .title("title2")
                .content("content2")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2.changeTechs(Collections.singletonList(techJava));

        feed3 = Feed.builder()
                .title("title3")
                .content("content3")
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl("www.github.com/newWisdom")
                .deployedUrl("www.github.com/woowacourse")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed3.changeTechs(Collections.singletonList(techPython));

        feedRepository.save(feed1.writtenBy(아마찌));
        feedRepository.save(feed2.writtenBy(아마찌));
        feedRepository.save(feed3.writtenBy(아마찌));

        //when
        String properQuery = "cOnTen";
        String improperQuery = "nothing";

        List<String> JavaOnly = Collections.singletonList(techJava.getName());
        List<String> JavaScriptOrPython = Arrays.asList(techJavaScript.getName(), techPython.getName());

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        Long nextFeedId = 1000000L;

        EnumSet<Step> everyEnumSet = Step.asEnumSet("everything");
        EnumSet<Step> progressEnumSet = Step.asEnumSet("progress");
        EnumSet<Step> completeEnumSet = Step.asEnumSet("complete");

        Pageable maxThreePages = PageRequest.of(0, 15);

        List<Feed> everyStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        List<Feed> everyStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        //then
        assertThat(everyStepTrueHelpConditionUsingJava).containsExactly(feed2, feed1);
        assertThat(progressStepTrueHelpConditionUsingJava).containsExactly(feed2, feed1);
        assertThat(completeStepTrueHelpConditionUsingJava).isEmpty();

        assertThat(everyStepNoHelpConditionUsingJavaOrPython).containsExactly(feed3, feed1);
        assertThat(progressStepNoHelpConditionUsingJavaOrPython).containsExactly(feed1);
        assertThat(completeStepNoHelpConditionUsingJavaOrPython).containsExactly(feed3);
    }

    @DisplayName("피드의 title 혹은 content에 대응하는 문자열이 있고, 검색된 기술들 중 하나라도 사용했으며, feedId/step/isSos 조건들에 만족하는 피드를 페이지네이션으로 검색할 수 있다.")
    @Test
    void findByQueryAndTechs() {
        //given
        feed1 = Feed.builder()
                .title("제목1")
                .content("컨텐츠1")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed1.changeTechs(Arrays.asList(techJava, techJavaScript));

        feed2 = Feed.builder()
                .title("제목2")
                .content("컨텐츠2")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("www.github.com/newWisdom")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2.changeTechs(Collections.singletonList(techJava));

        feed3 = Feed.builder()
                .title("이름3")
                .content("컨텐츠3")
                .step(Step.COMPLETE)
                .isSos(false)
                .storageUrl("www.github.com/newWisdom")
                .deployedUrl("www.github.com/woowacourse")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed3.changeTechs(Collections.singletonList(techPython));

        feedRepository.save(feed1.writtenBy(아마찌));
        feedRepository.save(feed2.writtenBy(아마찌));
        feedRepository.save(feed3.writtenBy(아마찌));

        //when
        String properQuery = "제목";

        List<String> JavaOnly = Collections.singletonList(techJava.getName());
        List<String> JavaScriptOrPython = Arrays.asList(techJavaScript.getName(), techPython.getName());

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        Long nextFeedId = 1000000L;

        EnumSet<Step> everyEnumSet = Step.asEnumSet("everything");
        EnumSet<Step> progressEnumSet = Step.asEnumSet("progress");
        EnumSet<Step> completeEnumSet = Step.asEnumSet("complete");

        Pageable maxThreePages = PageRequest.of(0, 15);

        List<Feed> everyStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        List<Feed> everyStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, nextFeedId, everyEnumSet, maxThreePages);
        List<Feed> progressStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, nextFeedId, progressEnumSet, maxThreePages);
        List<Feed> completeStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, nextFeedId, completeEnumSet, maxThreePages);

        //then
        assertThat(everyStepTrueHelpConditionUsingJavaProperQuery).containsExactly(feed2, feed1);
        assertThat(progressStepTrueHelpConditionUsingJavaProperQuery).containsExactly(feed2, feed1);
        assertThat(completeStepTrueHelpConditionUsingJavaProperQuery).isEmpty();

        assertThat(everyStepNoHelpConditionUsingJavaOrPythonProperQuery).containsExactly(feed1);
        assertThat(progressStepNoHelpConditionUsingJavaOrPythonProperQuery).containsExactly(feed1);
        assertThat(completeStepNoHelpConditionUsingJavaOrPythonProperQuery).isEmpty();
    }
}
