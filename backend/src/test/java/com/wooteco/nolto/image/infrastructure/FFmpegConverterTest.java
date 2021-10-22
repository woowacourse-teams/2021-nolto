package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.InternalServerErrorException;
import com.wooteco.nolto.image.config.FFmpegConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {FFmpegConverter.class, FFmpegConfig.class})
class FFmpegConverterTest {

    private final String gifFileName = "asdf.gif";
    private final String mp4FileName = "asdf.mp4";

    @Autowired
    private FFmpegConverter ffmpegConverter;

    @AfterEach
    void tearDown() throws IOException {
        변환_후_생성된_파일삭제();
    }

    private void 변환_후_생성된_파일삭제() throws IOException {
        URL resource = getClass().getClassLoader().getResource("static/" + mp4FileName);
        if (Objects.nonNull(resource)) {
            if (System.getProperty("os.name").contains("Windows")) {
                Files.delete(Paths.get(resource.getPath().substring(1)));
                return;
            }

            Files.delete(Paths.get(resource.getPath()));
        }
    }

    @DisplayName("ffmpeg 명령어를 사용해서 gif파일을 mp4 파일로 변환한다.")
    @Test
    void convertGifToMp4() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        int indexOfExtensionDot = gifFilePath.lastIndexOf(".");
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + ".mp4";
        if (System.getProperty("os.name").contains("Windows")) {
            gifFilePath = gifFilePath.replaceAll("/", "\\\\").substring(1);
            mp4FilePath = mp4FilePath.replaceAll("/", "\\\\").substring(1);
        }

        // when
        ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath);

        // then
        String mp4Path = getClass().getClassLoader().getResource("static/" + mp4FileName).getPath();
        if (System.getProperty("os.name").contains("Windows")) {
            mp4Path = mp4Path.replaceAll("/", "\\\\").substring(1);
        }
        assertThat(mp4Path).isEqualTo(mp4FilePath);
    }

    @DisplayName("gif파일이 파일 경로에 존재하지 않으면 예외가 발생한다. ")
    @Test
    void convertGifToMp4WithInvalidGifFilePath() {
        // given
        String gifFilePath = "static/매우잘못된경로에있는파일.gif";
        int indexOfExtensionDot = gifFilePath.lastIndexOf(".");
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + ".mp4";

        // when then
        assertThatThrownBy(() -> ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath))
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("gif파일을 mp4파일로 변환에 실패하였습니다.");
    }

    @DisplayName("gif파일이 파일 경로에 존재하지 않으면 예외가 발생한다. ")
    @Test
    void convertGifToMp4WithInvalidMp4FilePath() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        String mp4FilePath = "static/이런경로가없는데/매우잘못된경로에있는파일.mp4";

        // when then
        assertThatThrownBy(() -> ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath))
                .isInstanceOf(RuntimeException.class);
    }
}
