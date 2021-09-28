package com.wooteco.nolto.image.application;

import com.wooteco.nolto.image.FileExtension;
import com.wooteco.nolto.image.infrastructure.FFmpegConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageConvertService {

    private final FFmpegConverter ffmpegConverter;

    public File convertGifToMp4(File gifFile) {

        String gifFilePath = gifFile.getPath();
        if (FileExtension.isNotGifFile(gifFilePath)) {
            throw new IllegalArgumentException("gif 확장자가 아닌 파일입니다.");
        }
        int indexOfExtensionDot = gifFilePath.lastIndexOf(FileExtension.FILENAME_EXTENSION_DOT);
        String filePathWithoutExtension = gifFilePath.substring(0, indexOfExtensionDot);
        String mp4FilePath = filePathWithoutExtension + FileExtension.MP4;

        ffmpegConverter.convertGifToMp4(gifFilePath, mp4FilePath);

        return new File(mp4FilePath);
    }
}
