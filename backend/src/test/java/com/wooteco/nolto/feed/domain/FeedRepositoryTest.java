package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.feed.domain.repository.FeedRepository;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FeedRepositoryTest {
    public static final String DEFAULT_THUMBNAIL_IMAGE = "https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png";

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    private Feed feed1;
    private Feed feed2;
    private Feed feed3;

    @BeforeEach
    void setUp() {
        user1 = new User("123456L", SocialType.GITHUB, "아마찌", DEFAULT_THUMBNAIL_IMAGE);
        user2 = new User("654321L", SocialType.GOOGLE, "지그", DEFAULT_THUMBNAIL_IMAGE);

        userRepository.save(user1);
        userRepository.save(user2);

        feed1 = new Feed("title1", "content1", Step.PROGRESS, true,
                "www.github.com/woowacourse", "", DEFAULT_THUMBNAIL_IMAGE);
        feed2 = new Feed("title2", "content2", Step.PROGRESS, false,
                "www.github.com/woowacourse", "", DEFAULT_THUMBNAIL_IMAGE);
        feed3 = new Feed("title3", "content3", Step.COMPLETE, false,
                "www.github.com/woowacourse", "www.github.com/woowacourse", DEFAULT_THUMBNAIL_IMAGE);
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
        Set<Feed> feedsContainingTitle = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("title", "title");
        Set<Feed> feedsContainingContent1 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("content1", "content1");
        Set<Feed> feedsContainingContent2 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("content2", "content2");
        Set<Feed> feedsContainingTitle3 = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("title3", "title3");
        Set<Feed> feedsContainingTle = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase("tle", "tle");

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
        Set<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        Set<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        Set<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);

        //then
        assertThat(query1Result).contains(feed1, feed2, feed3);
        assertThat(query2Result).contains(feed1, feed2, feed3);
        assertThat(query3Result).contains(feed1, feed2, feed3);
    }

    @DisplayName("피드의 title과 content에 검색하고자 하는 문자열에 한글이 포함되어 있다면 조회해 올 수 있다.")
    @Test
    void searchByKeywordWithKorean() {
        //given
        feed1 = new Feed("조엘 프로젝트", "조엘의 환상적인 토이 프로젝트로 초대합니다 룰루랄라", Step.PROGRESS, true,
                "storageUrl", "", "http://thumbnailUrl.ppnngg");
        feed2 = new Feed("놀토 프로젝트", "놀토는 정말 세계에서 제일가는 팀입니다. 우테코 최고 아웃풋이죠", Step.PROGRESS, false,
                "", "deployUrl", "http://thumbnailUrl.pnggg");

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "조엘";
        String query2 = "우테코";
        String query3 = "프로젝트";
        String query4 = "에서 제일";

        //when
        Set<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        Set<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        Set<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        Set<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

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
        feed1 = new Feed("조엘 프로젝트$$$$", "조엘의 ### && *** 룰루랄라", Step.PROGRESS, true,
                "storageUrl", "", "http://thumbnailUrl.ppnngg");
        feed2 = new Feed("놀토 프로젝트%%%%", "놀토는 ()() @@@ 우테코 최고 아웃풋", Step.PROGRESS, false,
                "", "deployUrl", "http://thumbnailUrl.pnggg");

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "$$";
        String query2 = "##";
        String query3 = "%%";
        String query4 = ")(";

        //when
        Set<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        Set<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        Set<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        Set<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

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
        feed1 = new Feed("JOEL 프로젝트$$$$", "조엘의 ### && *** LUlu lala", Step.PROGRESS, true,
                "storageUrl", "", "http://thumbnailUrl.ppnngg");
        feed2 = new Feed("놀토 project%%%%", "놀토는 ()() @@@ wootecho 최고 output", Step.PROGRESS, false,
                "", "deployUrl", "http://thumbnailUrl.pnggg");

        feedRepository.save(feed1.writtenBy(user1));
        feedRepository.save(feed2.writtenBy(user2));

        String query1 = "JOEL 프로젝트$$$";
        String query2 = "의 ### && *** LUlu lala";
        String query3 = "놀토 project%%%%";
        String query4 = ")() @@@ wootecho 최";

        //when
        Set<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);
        Set<Feed> query2Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query2, query2);
        Set<Feed> query3Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query3, query3);
        Set<Feed> query4Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query4, query4);

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
        Set<Feed> query1Result = feedRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query1, query1);

        //then
        assertThat(query1Result).hasSize(0);
    }
}
