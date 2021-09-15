package com.wooteco.nolto.image.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class ImageResizeService {

    private static final double MAX_PIXEL = 400.0;
    private static final double RESIZE_NOT_NEEDED = 1.0;

    public File resize(File file, String fileName) {
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage resizedImage = resizeImage(image, image.getWidth(), image.getHeight());
            if (resizedImage.equals(image)) {
                return file;
            }
            File resizedFile = new File(fileName);
            ImageIO.write(resizedImage, FilenameUtils.getExtension(fileName), resizedFile);
            return resizedFile;
        } catch (Exception e) {
            throw new InternalServerErrorException(ErrorType.IMAGE_RESIZING_FAIL);
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int originalWidth, int originalHeight) {
        double resizeRatio = calculateResizeRatio(originalWidth, originalHeight);
        if (resizeRatio == RESIZE_NOT_NEEDED) {
            return originalImage;
        }
        return resizeImageByRatio(originalImage, originalWidth, originalHeight, resizeRatio);
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

    private BufferedImage resizeImageByRatio(BufferedImage originalImage, int originalWidth, int originalHeight, double resizeRatio) {
        int resizedWidth = (int) (resizeRatio * originalWidth);
        int resizedHeight = (int) (resizeRatio * originalHeight);

        Image resizedImage = originalImage.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_ARGB);
        resizedBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
        return resizedBufferedImage;
    }
}
