package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Component
public class FFmpegConverter {

    private static final String FRAME_RATE_OPTION = "-r";
    private static final String DELETE_SOUND_OPTION = "-an";

    private final FFmpeg ffmpeg;

    public void convertGifToMp4(String gifFilePath, String mp4FilePath) {
        log.info("convert gif to mp4 {} -> {}", gifFilePath, mp4FilePath);
        try {
            checkExistsGifFile(gifFilePath);
            ImageSize resizedGifImage = ImageSize.ofGif(gifFilePath).resize();

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(gifFilePath)
                    .overrideOutputFiles(true)
                    .addExtraArgs(FRAME_RATE_OPTION, "20")
                    .addOutput(mp4FilePath)
                    .addExtraArgs(DELETE_SOUND_OPTION)
                    .setVideoMovFlags("faststart")
                    .setVideoCodec("h264")
                    .setVideoPixelFormat("yuv420p")
                    .setVideoFilter("scale=trunc(iw/2)*2:trunc(ih/2)*2")
                    .setVideoWidth(resizedGifImage.getWidthOnesRounded())
                    .setVideoHeight(resizedGifImage.getHeightOnesRounded())
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
            FFmpegJob job = executor.createJob(builder);
            job.run();
            checkExistsMp4File(mp4FilePath);
        } catch (IOException e) {
            log.error("gif파일을 mp4로 변환하는데 실패했습니다.", e);
            throw new InternalServerErrorException(ErrorType.GIF_MP4_CONVERT_FAIL);
        }
    }

    private void checkExistsGifFile(String gifFilePath) {
        if (Files.notExists(Paths.get(gifFilePath))) {
            log.error("gif파일이 경로에 존재하지 않습니다.");
            throw new InternalServerErrorException(ErrorType.GIF_MP4_CONVERT_FAIL);
        }
    }

    private void checkExistsMp4File(String mp4FilePath) {
        if (Files.notExists(Paths.get(mp4FilePath))) {
            log.error("gif파일 변환 후 mp4파일이 존재하지 않습니다.");
            throw new InternalServerErrorException(ErrorType.GIF_MP4_CONVERT_FAIL);
        }
    }
}
