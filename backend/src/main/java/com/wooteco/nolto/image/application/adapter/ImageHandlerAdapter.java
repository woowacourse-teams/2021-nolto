package com.wooteco.nolto.image.application.adapter;

import com.wooteco.nolto.image.domain.ProcessedImage;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.UUID;

public interface ImageHandlerAdapter {
    String FILENAME_EXTENSION_DOT = ".";

    boolean supported(String fileName);

    ProcessedImage handle(File file);

    default String getFileName(File file) {
        String fileOriginName = file.getName();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = FilenameUtils.getExtension(fileOriginName);
        return uuid + FILENAME_EXTENSION_DOT + extension;
    }
}
