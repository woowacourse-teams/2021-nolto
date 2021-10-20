package com.wooteco.nolto.image.infrastructure;

import com.madgag.gif.fmsware.GifDecoder;
import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageSize {

    private static final double MAX_PIXEL = 400.0;
    private static final double RESIZE_NOT_NEEDED = 1.0;

    private final int width;
    private final int height;

    private ImageSize(int width, int height) {
        validateSize(width, height);
        this.width = width;
        this.height = height;
    }

    private void validateSize(int width, int height) {
        if (width <= 0 && height <= 0) {
            log.error("이미지 사이즈는 0보다 커야합니다.");
            throw new InternalServerErrorException(ErrorType.IMAGE_RESIZING_FAIL);
        }
    }

    public static ImageSize of(String filePath) {
        File imageFile = new File(filePath);
        try {
            return of(ImageIO.read(imageFile));
        } catch (IOException e) {
            log.error("이미지 파일을 읽는데 실패했습니다.", e);
            throw new InternalServerErrorException(ErrorType.IMAGE_RESIZING_FAIL);
        }
    }

    public static ImageSize ofGif(String gifFilePath) {
        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(gifFilePath);
        return of(gifDecoder.getImage());
    }

    public static ImageSize of(BufferedImage image) {
        return new ImageSize(image.getWidth(), image.getHeight());
    }

    public ImageSize resize() {
        double resizeRatio = calculateResizeRatio(width, height);
        return new ImageSize((int) (width * resizeRatio), (int) (height * resizeRatio));
    }

    private double calculateResizeRatio(int width, int height) {
        if (width <= MAX_PIXEL && height <= MAX_PIXEL) {
            return RESIZE_NOT_NEEDED;
        }
        if (width < height) {
            return (MAX_PIXEL / height);
        }
        return (MAX_PIXEL / width);
    }

    public boolean doNotNeedResize(BufferedImage originalImage) {
        return width == originalImage.getWidth() && height == originalImage.getHeight();
    }

    public int getWidthOnesRounded() {
        return (int) (Math.ceil(this.width / 10.0) * 10);
    }

    public int getHeightOnesRounded() {
        return (int) (Math.ceil(this.height / 10.0) * 10);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
