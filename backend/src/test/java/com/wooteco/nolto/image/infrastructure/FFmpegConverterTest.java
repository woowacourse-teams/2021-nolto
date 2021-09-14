package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.image.config.FFmpegConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Disabled
@SpringBootTest(classes = {FFmpegConverter.class, FFmpegConfig.class})
class FFmpegConverterTest {

    private final String gifFileName = "jjv1FK.gif";

    @Autowired
    private FFmpegConverter ffmpegConverter;

    @Test
    void convertGifToMp4() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        int indexOfExtensionDot = gifFilePath.lastIndexOf(".");
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + ".mp4";

        // when
        assertDoesNotThrow(() -> ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath));
    }
}
