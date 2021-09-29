package com.wooteco.nolto.image.application;

import com.wooteco.nolto.image.config.FFmpegConfig;
import com.wooteco.nolto.image.infrastructure.FFmpegConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {ImageConvertService.class, FFmpegConfig.class, FFmpegConverter.class})
class ImageConvertServiceTest {

    @Autowired
    private ImageConvertService imageConvertService;

    @MockBean
    private FFmpegConverter ffmpegConverter;

    @BeforeEach
    void setUp() {
        willDoNothing().given(ffmpegConverter).convertGifToMp4(anyString(), anyString());
    }

    @DisplayName("gif 파일을 mp4파일로 변환한다.")
    @Test
    void convertGifToMp4() {
        // given
        String gifFileName = "jjv1FK.gif";
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        File gifFile = new File(gifFilePath);

        // when
        File mp4File = imageConvertService.convertGifToMp4(gifFile);
        String mp4FileName = mp4File.getName();
        String expectedFileName = "jjv1FK.mp4";
        assertThat(mp4FileName).isEqualTo(expectedFileName);

        // then
        verify(ffmpegConverter, times(1)).convertGifToMp4(anyString(), anyString());
    }
}