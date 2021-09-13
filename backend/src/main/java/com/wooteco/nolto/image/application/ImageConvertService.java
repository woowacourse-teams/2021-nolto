package com.wooteco.nolto.image.application;

import com.wooteco.nolto.image.infrastructure.FfmpegConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageConvertService {

    private static final String FILENAME_EXTENSION_DOT = ".";
    private static final String GIF_EXTENSION_NAME = ".gif";
    private static final String MP4_EXTENSION_NAME = ".mp4";

    private final FfmpegConverter ffmpegConverter;

    public File convertGifToMp4(File gifFile) {

        String gifFilePath = gifFile.getPath();
        if (!gifFilePath.endsWith(GIF_EXTENSION_NAME)) {
            throw new IllegalArgumentException("gif 확장자가 아닌 파일입니다.");
        }
        int indexOfExtensionDot = gifFilePath.lastIndexOf(FILENAME_EXTENSION_DOT);
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + MP4_EXTENSION_NAME;

        ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath);

        return new File(mp4FilePath);
    }
}
