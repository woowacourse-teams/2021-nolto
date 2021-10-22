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

import java.util.*;

import static com.wooteco.nolto.FeedFixture.*;
import static com.wooteco.nolto.UserFixture.아마찌_생성;
import static com.wooteco.nolto.UserFixture.포모_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedRepositoryTest {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechRepository techRepository;

    private static final EnumSet<Step> EVERYTHING = Step.asEnumSet("everything");
    private static final EnumSet<Step> PROGRESS = Step.asEnumSet("progress");
    private static final EnumSet<Step> COMPLETE = Step.asEnumSet("complete");
    private static final PageRequest PAGEABLE_REQUEST = PageRequest.of(0, 15);
    private static final long NEXT_FEED_ID = 1000000L;

    private User 아마찌;
    private User 포모;

    private Feed 진행중_단계의_피드1;
    private Feed 진행중_단계의_피드2;
    private Feed 전시중_단계의_피드3;

    private Tech java;
    private Tech python;
    private Tech javaScript;

    @BeforeEach
    void setUp() {
        아마찌 = 아마찌_생성();
        포모 = 포모_생성();

        userRepository.save(아마찌);
        userRepository.save(포모);

        진행중_단계의_피드1 = 진행중_단계의_피드_생성();
        진행중_단계의_피드2 = 진행중_단계의_피드_생성();
        전시중_단계의_피드3 = 전시중_단계의_피드_생성();

        java = new Tech("Java");
        python = new Tech("Python");
        javaScript = new Tech("JavaScript");
        techRepository.saveAll(Arrays.asList(java, python, javaScript));
    }

    @DisplayName("토이 프로젝트 글을 의미하는 Feed를 저장한다.")
    @Test
    void save() {
        // given
        진행중_단계의_피드1.writtenBy(아마찌);
        진행중_단계의_피드2.writtenBy(포모);
        전시중_단계의_피드3.writtenBy(포모);

        // when
        Feed 저장된_진행중_단계의_피드1 = feedRepository.save(진행중_단계의_피드1);
        Feed 저장된_진행중_단계의_피드2 = feedRepository.save(진행중_단계의_피드2);
        Feed 저장된_전시중_단계의_피드3 = feedRepository.save(전시중_단계의_피드3);

        // then
        assertThat(저장된_진행중_단계의_피드1.getId()).isNotNull();
        checkSame(진행중_단계의_피드1, 저장된_진행중_단계의_피드1);

        assertThat(저장된_진행중_단계의_피드2.getId()).isNotNull();
        checkSame(진행중_단계의_피드2, 저장된_진행중_단계의_피드2);

        assertThat(저장된_전시중_단계의_피드3.getId()).isNotNull();
        checkSame(전시중_단계의_피드3, 저장된_전시중_단계의_피드3);
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
        진행중_단계의_피드1 = 진행중_단계의_SOS_피드_생성("title1", "content1").writtenBy(아마찌);
        진행중_단계의_피드2 = 진행중_단계의_SOS_피드_생성("title2", "content2").writtenBy(아마찌);
        전시중_단계의_피드3 = 전시중_단계의_피드_생성("title3", "content3").writtenBy(아마찌);

        feedRepository.saveAll(Arrays.asList(진행중_단계의_피드1, 진행중_단계의_피드2, 전시중_단계의_피드3));

        //when
        String properQuery = "cOnTen";
        String improperQuery = "nothing";

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        List<Feed> everyStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepTrueHelpConditionProperQuery = feedRepository.findByQuery(properQuery, trueHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        List<Feed> everyStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepNoHelpConditionProperQuery = feedRepository.findByQuery(properQuery, NoHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        List<Feed> everyStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepNoHelpConditionImproperQuery = feedRepository.findByQuery(improperQuery, NoHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        //then
        assertThat(everyStepTrueHelpConditionProperQuery).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(progressStepTrueHelpConditionProperQuery).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(completeStepTrueHelpConditionProperQuery).isEmpty();

        assertThat(everyStepNoHelpConditionProperQuery).containsExactly(전시중_단계의_피드3, 진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(progressStepNoHelpConditionProperQuery).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(completeStepNoHelpConditionProperQuery).containsExactly(전시중_단계의_피드3);

        assertThat(everyStepNoHelpConditionImproperQuery).isEmpty();
        assertThat(progressStepNoHelpConditionImproperQuery).isEmpty();
        assertThat(completeStepNoHelpConditionImproperQuery).isEmpty();
    }

    @DisplayName("검색된 기술들 중 하나라도 사용했으며, feedId/step/isSos 조건들에 만족하는 피드를 페이지네이션으로 검색할 수 있다.")
    @Test
    void findByTechs() {
        //given
        진행중_단계의_피드1 = 진행중_단계의_SOS_피드_생성("title1", "content1").writtenBy(아마찌);
        진행중_단계의_피드2 = 진행중_단계의_SOS_피드_생성("title2", "content2").writtenBy(아마찌);
        전시중_단계의_피드3 = 전시중_단계의_피드_생성("title3", "content3").writtenBy(아마찌);

        진행중_단계의_피드1.changeTechs(Arrays.asList(java, javaScript));
        진행중_단계의_피드2.changeTechs(Collections.singletonList(java));
        전시중_단계의_피드3.changeTechs(Collections.singletonList(python));

        feedRepository.saveAll(Arrays.asList(진행중_단계의_피드1, 진행중_단계의_피드2, 전시중_단계의_피드3));

        //when
        List<String> JavaOnly = Collections.singletonList(java.getName());
        List<String> JavaScriptOrPython = Arrays.asList(javaScript.getName(), python.getName());

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        List<Feed> everyStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepTrueHelpConditionUsingJava = feedRepository.findByTechs(JavaOnly, trueHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        List<Feed> everyStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepNoHelpConditionUsingJavaOrPython = feedRepository.findByTechs(JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        //then
        assertThat(everyStepTrueHelpConditionUsingJava).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(progressStepTrueHelpConditionUsingJava).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(completeStepTrueHelpConditionUsingJava).isEmpty();

        assertThat(everyStepNoHelpConditionUsingJavaOrPython).containsExactly(전시중_단계의_피드3, 진행중_단계의_피드1);
        assertThat(progressStepNoHelpConditionUsingJavaOrPython).containsExactly(진행중_단계의_피드1);
        assertThat(completeStepNoHelpConditionUsingJavaOrPython).containsExactly(전시중_단계의_피드3);
    }

    @DisplayName("피드의 title 혹은 content에 대응하는 문자열이 있고, 검색된 기술들 중 하나라도 사용했으며, feedId/step/isSos 조건들에 만족하는 피드를 페이지네이션으로 검색할 수 있다.")
    @Test
    void findByQueryAndTechs() {
        //given
        진행중_단계의_피드1 = 진행중_단계의_SOS_피드_생성("제목1", "컨텐츠1").writtenBy(아마찌);
        진행중_단계의_피드2 = 진행중_단계의_SOS_피드_생성("제목2", "컨텐츠2").writtenBy(아마찌);
        전시중_단계의_피드3 = 전시중_단계의_피드_생성("이름3", "컨텐츠3").writtenBy(아마찌);

        진행중_단계의_피드1.changeTechs(Arrays.asList(java, javaScript));
        진행중_단계의_피드2.changeTechs(Collections.singletonList(java));
        전시중_단계의_피드3.changeTechs(Collections.singletonList(python));

        feedRepository.saveAllAndFlush(Arrays.asList(진행중_단계의_피드1, 진행중_단계의_피드2, 전시중_단계의_피드3));

        //when
        String properQuery = "제목";

        List<String> JavaOnly = Collections.singletonList(java.getName());
        List<String> JavaScriptOrPython = Arrays.asList(javaScript.getName(), python.getName());

        Set<Boolean> trueHelpCondition = new HashSet<>(Collections.singletonList(true));
        Set<Boolean> NoHelpCondition = new HashSet<>(Arrays.asList(true, false));

        List<Feed> everyStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepTrueHelpConditionUsingJavaProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaOnly, trueHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        List<Feed> everyStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, EVERYTHING, PAGEABLE_REQUEST);
        List<Feed> progressStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, PROGRESS, PAGEABLE_REQUEST);
        List<Feed> completeStepNoHelpConditionUsingJavaOrPythonProperQuery = feedRepository.findByQueryAndTechs(properQuery, JavaScriptOrPython, NoHelpCondition, NEXT_FEED_ID, COMPLETE, PAGEABLE_REQUEST);

        //then
        assertThat(everyStepTrueHelpConditionUsingJavaProperQuery).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(progressStepTrueHelpConditionUsingJavaProperQuery).containsExactly(진행중_단계의_피드2, 진행중_단계의_피드1);
        assertThat(completeStepTrueHelpConditionUsingJavaProperQuery).isEmpty();

        assertThat(everyStepNoHelpConditionUsingJavaOrPythonProperQuery).containsExactly(진행중_단계의_피드1);
        assertThat(progressStepNoHelpConditionUsingJavaOrPythonProperQuery).containsExactly(진행중_단계의_피드1);
        assertThat(completeStepNoHelpConditionUsingJavaOrPythonProperQuery).isEmpty();
    }

    @DisplayName("findAllFeedsHavingComments 메서드는 커멘트가 달려있는 피드만 가져온다")
    @Test
    void findAllWithFetchJoin() {
        //given
        진행중_단계의_피드1 = 진행중_단계의_SOS_피드_생성("제목1", "컨텐츠1").writtenBy(아마찌);
        진행중_단계의_피드2 = 진행중_단계의_SOS_피드_생성("제목2", "컨텐츠2").writtenBy(아마찌);
        전시중_단계의_피드3 = 전시중_단계의_피드_생성("이름3", "컨텐츠3").writtenBy(아마찌);
        feedRepository.saveAllAndFlush(Arrays.asList(진행중_단계의_피드1, 진행중_단계의_피드2, 전시중_단계의_피드3));

        //when
        final List<Feed> allWithFetchJoin = feedRepository.findAllFeedsHavingComments();
        assertThat(allWithFetchJoin).isEmpty();
    }
}
