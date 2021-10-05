package com.wooteco.nolto.image.application.adapter;

import com.wooteco.nolto.image.application.ImageConvertService;
import com.wooteco.nolto.image.application.ImageResizeService;
import com.wooteco.nolto.image.config.FFmpegConfig;
import com.wooteco.nolto.image.domain.ProcessedImage;
import com.wooteco.nolto.image.infrastructure.FFmpegConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("ImageHandlerAdapterTest 테스트")
@SpringBootTest(classes = {ImageResizeHandler.class, ImageConvertHandler.class,
        ImageResizeService.class, ImageConvertService.class, FFmpegConverter.class, FFmpegConfig.class})
class ImageHandlerAdapterTest {

    @Autowired
    private List<ImageHandlerAdapter> imageHandlerAdapters;

    @MockBean
    private FFmpegConverter ffmpegConverter;

    @BeforeEach
    void setUp() throws IOException {
        willDoNothing().given(ffmpegConverter).convertGifToMp4(anyString(), anyString());
    }

    private final String gifFileName = "jjv1FK.gif";
    private final String pngFileName = "pretty_cat.png";

    @DisplayName(".gif 확장자인 파일이 아니라면 ImageResizeHandler를 사용한다.")
    @Test
    void imageResizeHandler() {
        // given
        ImageHandlerAdapter imageHandlerAdapter1 = imageHandlerAdapters.stream()
                .filter(imageHandlerAdapter -> imageHandlerAdapter.supported(pngFileName))
                .findAny().get();
        assertThat(imageHandlerAdapter1).isInstanceOf(ImageResizeHandler.class);
        URL resource = getClass().getClassLoader().getResource("static/" + pngFileName);

        // when
        ProcessedImage processedImage = imageHandlerAdapter1.handle(new File(resource.getPath()));
        File resizedFile = processedImage.getFile();

        // then
        String expectedFileName = "pretty_cat.png";
        assertThat(resizedFile).hasName(expectedFileName);
    }

    @DisplayName(".gif 확장자인 파일이라면 ImageConvertHandler를 사용한다.")
    @Test
    void imageConvertHandler() {
        // given
        ImageHandlerAdapter imageHandlerAdapter1 = imageHandlerAdapters.stream()
                .filter(imageHandlerAdapter -> imageHandlerAdapter.supported(gifFileName))
                .findAny().get();

        assertThat(imageHandlerAdapter1).isInstanceOf(ImageConvertHandler.class);

        // when
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        ProcessedImage processedImage = imageHandlerAdapter1.handle(new File(resource.getPath()));

        // then
        File convertFile = processedImage.getFile();
        String expectedConvertFileName = "jjv1FK.mp4";
        assertThat(convertFile).hasName(expectedConvertFileName);
    }
}