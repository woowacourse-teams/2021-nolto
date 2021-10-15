package com.wooteco.nolto.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooteco.nolto.admin.application.AdminService;
import com.wooteco.nolto.auth.application.AuthService;
import com.wooteco.nolto.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.wooteco.nolto.UserFixture.ID_있는_유저_생성;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public abstract class ControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private RestDocumentationContextProvider restDocumentationContextProvider;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected AdminService adminService;

    protected static final String BEARER = "Bearer ";
    protected static final String ACCESS_TOKEN = "accessToken";
    protected static final String ACCESS_TOKEN_OPTIONAL = "accessTokenOptional";
    protected static final User LOGIN_USER = ID_있는_유저_생성(1L);

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .apply(MockMvcRestDocumentation.documentationConfiguration(
                        restDocumentationContextProvider
                ))
                .build();
    }

    protected static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("nolto.kro.kr")
                        .removePort(),
                prettyPrint());
    }

    protected static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(prettyPrint());
    }
}
