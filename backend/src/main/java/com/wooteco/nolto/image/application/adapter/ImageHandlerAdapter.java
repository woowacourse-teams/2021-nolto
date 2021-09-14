package com.wooteco.nolto.image.application.adapter;

import com.wooteco.nolto.image.FileExtension;
import com.wooteco.nolto.image.domain.ProcessedImage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.UUID;

public interface ImageHandlerAdapter {

    default String getFileName(File file) {
        String fileOriginName = file.getName();
        String uuid = validUuidReplacement();
        String extension = FilenameUtils.getExtension(fileOriginName);
        return uuid + FileExtension.FILENAME_EXTENSION_DOT + extension;
    }

    default String validUuidReplacement() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    boolean supported(String fileName);

    ProcessedImage handle(File file);
}
