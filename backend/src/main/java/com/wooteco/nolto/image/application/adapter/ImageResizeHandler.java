package com.wooteco.nolto.image.application.adapter;

import com.wooteco.nolto.image.FileExtension;
import com.wooteco.nolto.image.application.ImageResizeService;
import com.wooteco.nolto.image.domain.ProcessedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ImageResizeHandler implements ImageHandlerAdapter {

    private final ImageResizeService imageResizeService;

    @Override
    public boolean supported(String fileName) {
        return FileExtension.isNotGifFile(fileName);
    }

    @Override
    public ProcessedImage handle(File file) {
        String fileName = getFileName(file);
        File resizeImage = imageResizeService.resize(file, fileName);
        return new ProcessedImage(resizeImage, fileName);
    }
}
