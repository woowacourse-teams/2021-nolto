package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.auth.domain.SocialType;
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

    private User user1;
    private User user2;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    private Tech techJava;
    private Tech techPython;
    private Tech techJavaScript;

    @BeforeEach
    void setUp() {
        user1 = new User("123456L", SocialType.GITHUB, "아마찌", DEFAULT_THUMBNAIL_IMAGE);
        user2 = new User("654321L", SocialType.GOOGLE, "지그", DEFAULT_THUMBNAIL_IMAGE);

        userRepository.save(user1);
        userRepository.save(user2);

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
        feed1.writtenBy(user1);
        feed2.writtenBy(user2);
        feed3.writtenBy(user2);

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

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열이 포함되어 있다면 조회해 올 수 있다.")
    @Test
    void searchByKeyword() {
        //given
        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));
        feedRepository.save(feed3.writtenBy(user2));

        //when
        List<Feed> feedsContainingTitle = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("title", "title");
        List<Feed> feedsContainingContent1 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("content1", "content1");
        List<Feed> feedsContainingContent2 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("content2", "content2");
        List<Feed> feedsContainingTitle3 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("title3", "title3");
        List<Feed> feedsContainingTle = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("tle", "tle");

        //then
        assertThat(feedsContainingTitle).contains(feed1, feed2, feed3);
        assertThat(feedsContainingContent1).contains(feed1);
        assertThat(feedsContainingContent2).contains(feed2);
        assertThat(feedsContainingTitle3).contains(feed3);
        assertThat(feedsContainingTle).contains(feed1, feed2, feed3);
    }

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열이 포함되어 있다면 조회해 올 수 있고, 대소문자 상관없이 조회가 가능하다")
    @Test
    void searchByKeywordIgnoringCase() {
        //given
        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));
        feedRepository.save(feed3.writtenBy(user2));

        String query1 = "TITLE";
        String query2 = "title";
        String query3 = "TiTLe";

        //when
        List<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        List<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        List<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);

        //then
        assertThat(query1Result).contains(feed1, feed2, feed3);
        assertThat(query2Result).contains(feed1, feed2, feed3);
        assertThat(query3Result).contains(feed1, feed2, feed3);
    }

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열에 한글이 포함되어 있다면 조회해 올 수 있다.")
    @Test
    void searchByKeywordWithKorean() {
        //given
        feed1 = Feed.builder()
                .title("조엘 프로젝트")
                .content("조엘의 환상적인 토이 프로젝트로 초대합니다 룰루랄라")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("storageUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2 = Feed.builder()
                .title("놀토 프로젝트")
                .content("놀토는 정말 세계에서 제일가는 팀입니다. 우테코 최고 아웃풋이죠")
                .step(Step.PROGRESS)
                .isSos(false)
                .storageUrl("storageUrl")
                .deployedUrl("deployUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "조엘";
        String query2 = "우테코";
        String query3 = "프로젝트";
        String query4 = "에서 제일";

        //when
        List<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        List<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        List<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        List<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

        //then
        assertThat(query1Result).contains(feed1);
        assertThat(query2Result).contains(feed2);
        assertThat(query3Result).contains(feed1, feed2);
        assertThat(query4Result).contains(feed2);
    }

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열에 특수문자가 포함되어 있다면 조회해 올 수 있다.")
    @Test
    void searchByKeywordWithSpecialCharacter() {
        //given
        feed1 = Feed.builder()
                .title("조엘 프로젝트프로젝트$$$$")
                .content("조엘의 ### && *** 룰루랄라")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("storageUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2 = Feed.builder()
                .title("놀토 프로젝트%%%%")
                .content("놀토는 ()() @@@ 우테코 최고 아웃풋")
                .step(Step.PROGRESS)
                .isSos(false)
                .storageUrl("storageUrl")
                .deployedUrl("deployUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "$$";
        String query2 = "##";
        String query3 = "%%";
        String query4 = ")(";

        //when
        List<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        List<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        List<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        List<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

        //then
        assertThat(query1Result).contains(feed1);
        assertThat(query2Result).contains(feed1);
        assertThat(query3Result).contains(feed2);
        assertThat(query4Result).contains(feed2);
    }


    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열에 영어, 한글, 특수문자가 포함되어 있다면 조회해 올 수 있다.")
    @Test
    void searchByKeywordWithAllTogether() {
        //given
        feed1 = Feed.builder()
                .title("JOEL 프로젝트$$$$")
                .content("조엘의 ### && *** LUlu lala")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("storageUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();
        feed2 = Feed.builder()
                .title("놀토 project%%%%")
                .content("놀토는 ()() @@@ wootecho 최고 output")
                .step(Step.PROGRESS)
                .isSos(false)
                .storageUrl("storageUrl")
                .deployedUrl("deployUrl")
                .thumbnailUrl(DEFAULT_THUMBNAIL_IMAGE)
                .build();

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "JOEL 프로젝트$$$";
        String query2 = "의 ### && *** LUlu lala";
        String query3 = "놀토 project%%%%";
        String query4 = ")() @@@ wootecho 최";

        //when
        List<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        List<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        List<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        List<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

        //then
        assertThat(query1Result).contains(feed1);
        assertThat(query2Result).contains(feed1);
        assertThat(query3Result).contains(feed2);
        assertThat(query4Result).contains(feed2);
    }

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열에 대응하는 결과가 없다면, 빈 리스트를 반환한다.")
    @Test
    void searchByKeywordWithNoResult() {
        //given
        feedRepository.save(feed1.writtenBy(user1));

        String query1 = "절대 아무도 검색하지 않을 것 같은 INPUT";

        //when
        List<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);

        //then
        assertThat(query1Result).isEmpty();
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

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user1));
        feedRepository.save(feed3.writtenBy(user1));

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

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user1));
        feedRepository.save(feed3.writtenBy(user1));

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

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user1));
        feedRepository.save(feed3.writtenBy(user1));

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
