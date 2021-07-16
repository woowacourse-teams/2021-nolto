package com.wooteco.nolto.api;

import com.wooteco.nolto.tech.application.TechService;
import com.wooteco.nolto.tech.ui.TechController;
import com.wooteco.nolto.tech.ui.dto.TechResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TechController.class)
class TechControllerTest extends ControllerTest {

    private static final FieldDescriptor[] TECH = new FieldDescriptor[]{
            fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
            fieldWithPath("text").type(JsonFieldType.STRING).description("기술 스택명")};

    public static final List<TechResponse> TECH_RESPONSES = Arrays.asList(
            new TechResponse(67L, "Spring"), new TechResponse(1149L, "Spring Batch"));

    @MockBean
    private TechService techService;

    @DisplayName("키워드로 기술 스택을 조회한다.")
    @Test
    void findByTechsContains() throws Exception {
        String searchWord = "spring";
        given(techService.findByTechsContains(searchWord)).willReturn(TECH_RESPONSES);

        mockMvc.perform(get("/tags/techs").param("auto_complete", searchWord))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(TECH_RESPONSES)))
                .andDo(document("tech-findByTechsContains",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("기술 스택 목록"))
                                .andWithPrefix("[].", TECH)));
    }
}