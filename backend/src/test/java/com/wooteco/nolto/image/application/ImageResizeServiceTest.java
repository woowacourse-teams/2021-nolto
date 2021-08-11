package com.wooteco.nolto.image.application;

import com.wooteco.nolto.exception.InternalServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.wooteco.nolto.image.application.ImageServiceTest.FILE_PATH;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ImageResizeServiceTest {
    private final ImageResizeService imageResizeService = new ImageResizeService();

    private static final String CAR_PIC_1920X1080 = "car-picture.jpg";
    private static final String PRETTY_CAT_PIC_400X400 = "pretty_cat.png";

    @DisplayName("width나 height 둘 중 하나라도 400px이 넘어가는 이미지 파일의 경우, 긴 쪽을 400px로 사이즈를 조정한다.")
    @Test
    void resizeImageOver400px() throws IOException {
        File originalFile = new File(new File("").getAbsolutePath() + FILE_PATH + CAR_PIC_1920X1080);
        File resizedFile = imageResizeService.resize(originalFile, originalFile.getName());
        BufferedImage resizedImage = ImageIO.read(resizedFile);

        assertThat(resizedImage.getWidth()).isLessThanOrEqualTo(400);
        assertThat(resizedImage.getHeight()).isLessThanOrEqualTo(400);
        assertThat(resizedFile.length()).isLessThan(originalFile.length());
        Files.delete(Paths.get(resizedFile.getPath()));
    }

    @DisplayName("width와 height 둘 모두 400px 이하라면, 리사이징이 되지 않는다.")
    @Test
    void resizeImageLessThanOrEqualTo400px() {
        File originalFile = new File(new File("").getAbsolutePath() + FILE_PATH + PRETTY_CAT_PIC_400X400);
        File resizedFile = imageResizeService.resize(originalFile, originalFile.getName());
        assertThat(originalFile).isEqualTo(resizedFile);
    }

    @DisplayName("알맞지 않은 파일이 매개변수로 들어오면, IMAGE_RESIZING_FAIL 예외가 발생한다")
    @Test
    void imageResizingFailure() {
        assertThatThrownBy(() -> imageResizeService.resize(null, "not-available.jpg"))
                .isInstanceOf(InternalServerErrorException.class)
                .hasMessage("이미지 리사이징에 실패하였습니다.");
    }
}
