package com.wooteco.nolto.image.application;

import com.wooteco.nolto.image.config.FfmpegConfig;
import com.wooteco.nolto.image.infrastructure.FfmpegConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ImageConvertService.class, FfmpegConfig.class, FfmpegConverter.class})
class ImageConvertServiceTest {

    private final String gifFileName = "jjv1FK.gif";

    @Autowired
    private ImageConvertService imageConvertService;

    @Test
    void convertGifToMp4() {

        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        File gifFile = new File(gifFilePath);

        // when
        File mp4File = imageConvertService.convertGifToMp4(gifFile);

        // then
        String mp4FileName = mp4File.getName();
        String expectedFileName = "jjv1FK.mp4";
        assertThat(mp4FileName).isEqualTo(expectedFileName);
    }
}