package com.wooteco.nolto.image.application.adapter;

import com.wooteco.nolto.image.application.ImageConvertService;
import com.wooteco.nolto.image.domain.ProcessedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@RequiredArgsConstructor
@Component
public class ImageConvertHandler implements ImageHandlerAdapter {

    private final ImageConvertService imageConvertService;

    @Override
    public boolean supported(String fileName) {
        return fileName.endsWith(".gif");
    }

    @Override
    public ProcessedImage handle(File file) {
        File mp4File = imageConvertService.convertGifToMp4(file);
        String mp4FileName = getFileName(mp4File);
        return new ProcessedImage(mp4File, mp4FileName);
    }
}
