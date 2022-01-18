package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.NotFoundException;
import com.wooteco.nolto.image.domain.ProcessedImage;
import com.wooteco.nolto.image.domain.repository.ImageRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Repository
public class LocalImageRepository implements ImageRepository {

    @Value("${image.path}")
    private String path;

    @Override
    public String save(ProcessedImage processedImage) {
        return null;
    }

    public byte[] findFile(String fileName) {
        try {
            File file = new File(path + fileName);
            return IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            throw new NotFoundException(ErrorType.IMAGE_NOT_FOUND);
        }
    }
}
