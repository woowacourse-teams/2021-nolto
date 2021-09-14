package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
            throw new IllegalArgumentException("이미지의 사이즈는 0보다 커야합니다.");
        }
    }

    public static ImageSize resizeOf(String filePath) {
        File gifFile = new File(filePath);
        BufferedImage read = null;
        try {
            read = ImageIO.read(gifFile);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorType.IMAGE_RESIZING_FAIL);
        }
        int imageWidth = read.getWidth();
        int imageHeight = read.getHeight();
        double resizeRatio = calculateResizeRatio(imageWidth, imageHeight);

        return new ImageSize((int) (imageWidth * resizeRatio), (int) (imageHeight * resizeRatio));
    }

    private static double calculateResizeRatio(int width, int height) {
        if (width <= MAX_PIXEL && height <= MAX_PIXEL) {
            return RESIZE_NOT_NEEDED;
        }
        if (width < height) {
            return (MAX_PIXEL / height);
        }
        return (MAX_PIXEL / width);
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
