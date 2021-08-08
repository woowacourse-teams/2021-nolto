package com.wooteco.nolto.acceptance;

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
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("기술 태그 관련 기능")
public class TechAcceptanceTest extends AcceptanceTest {

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private FeedTechRepository feedTechRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    private TechResponse 자바;
    private TechResponse 자바스크립트;
    private TechResponse 리액트;
    private TechResponse 리액트_네이티브;

    private Tech JAVA;
    private Tech JAVASCRIPT;
    private Tech REACT;
    private Tech REACT_NATIVE;

    @BeforeEach
    void setUpOnTechAcceptance() {
        JAVA = techRepository.save(new Tech("Java"));
        JAVASCRIPT = techRepository.save(new Tech("JavaScript"));
        REACT = techRepository.save(new Tech("React"));
        REACT_NATIVE = techRepository.save(new Tech("React Native"));

        자바 = new TechResponse(JAVA.getId(), JAVA.getName());
        자바스크립트 = new TechResponse(JAVASCRIPT.getId(), JAVASCRIPT.getName());
        리액트 = new TechResponse(REACT.getId(), REACT.getName());
        리액트_네이티브 = new TechResponse(REACT_NATIVE.getId(), REACT_NATIVE.getName());
    }

    @DisplayName("auto_complete 파라미터로 기술 태그의 자동 완성 기능을 사용할 수 있다.")
    @Test
    void findByTechsContains() {
        // given
        String autoComplete1 = "J";
        String autoComplete2 = "R";

        // when
        ExtractableResponse<Response> response1 = 기술_태그_조회한다(autoComplete1);
        ExtractableResponse<Response> response2 = 기술_태그_조회한다(autoComplete2);

        // then
        기술_태그_조회_된다(response1, Arrays.asList(자바, 자바스크립트));
        기술_태그_조회_된다(response2, Arrays.asList(리액트, 리액트_네이티브));
    }

    @DisplayName("auto_complete 파라미터로 존재하지 않는 기술 태그를 조회해서 응답한다.")
    @Test
    void findByTechsContainsWithNonExistTechTag() {
        // given
        String autoComplete = "절대 존재하지 않는 기술 이름";

        // when
        ExtractableResponse<Response> response = 기술_태그_조회한다(autoComplete);

        // then
        기술_태그_조회_된다(response, Collections.emptyList());
    }

    @DisplayName("names 파라미터로 이름과 정확히 일치하는 기술 태그들을 조회해서 응답한다.")
    @Test
    void findAllByNameInIgnoreCase() {
        // given
        String names = "Java,React";

        // when
        ExtractableResponse<Response> response = 기술_태그_이름과_정확히_일치하는_기술만_조회한다(names);

        // then
        기술_태그_조회_된다(response, Arrays.asList(자바, 리액트));
    }

    @DisplayName("names 파라미터로 기술이름과 정확하게 일치하지 않으면 기술 태그를 응답하지 않는다.")
    @Test
    void findAllByNameInIgnoreCaseWithNotEquslString() {
        // given
        String names1 = "Jav,Reac";
        String names2 = "Java, JavaScr";

        // when
        ExtractableResponse<Response> response1 = 기술_태그_이름과_정확히_일치하는_기술만_조회한다(names1);
        ExtractableResponse<Response> response2 = 기술_태그_이름과_정확히_일치하는_기술만_조회한다(names2);

        // then
        기술_태그_조회_된다(response1, Collections.emptyList());
        기술_태그_조회_된다(response2, Collections.singletonList(자바));
    }

    @DisplayName("names 파라미터로 기술이름 양 옆에 공백이 있어도 기술 이름만 일치하면 응답해준다.")
    @Test
    void findAllByNameInIgnoreCaseWithTrim() {
        // given
        String names = "  Java , React  ";

        // when
        ExtractableResponse<Response> response = 기술_태그_이름과_정확히_일치하는_기술만_조회한다(names);

        // then
        기술_태그_조회_된다(response, Arrays.asList(자바, 리액트));
    }

    @DisplayName("names 파라미터에 중복된 기술이름 있으면 중복을 제외하고 반환한다.")
    @Test
    void findAllByNameInIgnoreCaseWithDuplicatedName() {
        // given
        String names = "  Java , Java  ";

        // when
        ExtractableResponse<Response> response = 기술_태그_이름과_정확히_일치하는_기술만_조회한다(names);

        // then
        기술_태그_조회_된다(response, Arrays.asList(자바));
    }

    @DisplayName("현재 피드들 중에서 가장 많이 쓰이는 트렌드 테크를 조회할 수 있다.")
    @Test
    void findTrendTechs() {
        //given
        setUpFeedAndFeedTech();

        //when
        ExtractableResponse<Response> response = 트렌드_기술_태그_조회한다();

        //then
        기술_태그_조회_된다(response, Arrays.asList(자바, 자바스크립트, 리액트, 리액트_네이티브));
    }

    private void setUpFeedAndFeedTech() {
        User user = new User("1L", SocialType.GOOGLE, "JOEL", "imageUrl");
        Feed feed = new Feed("title", "content", Step.PROGRESS, true, "storageUrl",
                "", "http://thumbnailUrl.png").writtenBy(user);
        userRepository.save(user);
        feedRepository.save(feed);
        feedTechRepository.saveAll(Arrays.asList(new FeedTech(feed, JAVA), new FeedTech(feed, JAVASCRIPT),
                new FeedTech(feed, REACT), new FeedTech(feed, REACT_NATIVE)));
    }

    @DisplayName("기술 태그들이 피드에서 사용되지 않았다면, 빈 리스트가 반환된다.")
    @Test
    void foundTrendTechIsEmpty() {
        //when
        ExtractableResponse<Response> response = 트렌드_기술_태그_조회한다();

        //then
        기술_태그_조회_된다(response, Collections.emptyList());
    }

    public ExtractableResponse<Response> 기술_태그_조회한다(String autoComplete) {
        return given().log().all()
                .param("auto_complete", autoComplete)
                .when().get("/tags/techs")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 기술_태그_이름과_정확히_일치하는_기술만_조회한다(String searchWord) {
        return given().log().all()
                .param("names", searchWord)
                .when().get("/tags/techs/search")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 트렌드_기술_태그_조회한다() {
        return given().log().all()
                .when().get("/tags/techs/trend")
                .then().log().all()
                .extract();
    }

    public void 기술_태그_조회_된다(ExtractableResponse<Response> response, List<TechResponse> techResponses) {
        List<TechResponse> findResponses = response.jsonPath().getList(".", TechResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findResponses).hasSize(techResponses.size());
        for (TechResponse findResponse : findResponses) {
            assertThat(techResponses).extracting("id").contains(findResponse.getId());
            assertThat(techResponses).extracting("text").contains(findResponse.getText());
        }
    }
}
