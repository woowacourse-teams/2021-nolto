package com.wooteco.nolto.image.application;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import com.wooteco.nolto.image.infrastructure.ImageSize;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class ImageResizeService {

    public File resize(File file, String fileName) {
        try {
            BufferedImage image = ImageIO.read(file);
            BufferedImage resizedImage = resizeImage(image, fileName);

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

    private BufferedImage resizeImage(BufferedImage originalImage, String fileName) {
        ImageSize imageSize = ImageSize.of(originalImage).resize();
        if (imageSize.doNotNeedResize(originalImage)) {
            return originalImage;
        }
        return resizeImageByRatio(originalImage, imageSize, fileName);
    }

    private BufferedImage resizeImageByRatio(BufferedImage originalImage, ImageSize resizedImageSize, String fileName) {
        int resizedWidth = resizedImageSize.getWidth();
        int resizedHeight = resizedImageSize.getHeight();

        Image resizedImage = originalImage.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = getBufferedImage(fileName, resizedWidth, resizedHeight);
        resizedBufferedImage.getGraphics().drawImage(resizedImage, 0, 0, null);
        return resizedBufferedImage;
    }

    private BufferedImage getBufferedImage(String fileName, int resizedWidth, int resizedHeight) {
        if ("png".equals(FilenameUtils.getExtension(fileName))) {
            return new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_ARGB);
        } else {
            return new BufferedImage(resizedWidth, resizedHeight, BufferedImage.TYPE_INT_RGB);
        }
    }
}
