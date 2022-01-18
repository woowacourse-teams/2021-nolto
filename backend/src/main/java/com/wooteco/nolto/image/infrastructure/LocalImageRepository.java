package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.image.domain.ProcessedImage;
import com.wooteco.nolto.image.domain.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class LocalImageRepository implements ImageRepository {

    @Value("${image.url}")
    private String imageUrl;

    @Value("${image.path}")
    private String path;

    @Override
    public String save(ProcessedImage processedImage) {
        String imageName = processedImage.getImageName();
        File processedFile = processedImage.getFile();
        File serverSaveFile = new File(path + imageName);
        try {
            FileUtils.copyFile(processedFile, serverSaveFile);
            serverSaveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrl + imageName;
    }

    public byte[] findFile(String fileName) {
        try {
            File file = new File(path + fileName);
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            throw new NotFoundException(ErrorType.IMAGE_NOT_FOUND);
        }
    }

    public void deleteFile(String fileName) {
        Path fileToDeletePath = Paths.get(this.path + fileName);
        try {
            Files.deleteIfExists(fileToDeletePath);
        } catch (IOException e) {
            throw new InternalServerErrorException(ErrorType.IMAGE_DELETION_FAILED);
        }
    }
}
