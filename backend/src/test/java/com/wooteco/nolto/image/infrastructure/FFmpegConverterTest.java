package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.image.config.FFmpegConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {FFmpegConverter.class, FFmpegConfig.class})
class FFmpegConverterTest {

    private final String gifFileName = "jjv1FK.gif";
    private final String mp4FileName = "jjv1FK.mp4";

    @Autowired
    private FFmpegConverter ffmpegConverter;

    @AfterEach
    void tearDown() throws IOException {
        URL resource = getClass().getClassLoader().getResource("static/" + mp4FileName);
        Files.delete(Paths.get(resource.getPath()));
    }

    @Test
    void convertGifToMp4() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        int indexOfExtensionDot = gifFilePath.lastIndexOf(".");
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + ".mp4";

        // when
        ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath);

        URL mp4URL = getClass().getClassLoader().getResource("static/" + mp4FileName);
        assertThat(mp4URL.getPath()).isEqualTo(mp4FilePath);
    }
}
