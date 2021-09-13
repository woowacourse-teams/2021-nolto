package com.wooteco.nolto.image.infrastructure;

import com.wooteco.nolto.exception.ErrorType;
import com.wooteco.nolto.exception.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FfmpegConverter {

    private static final String FRAME_RATE_OPTION = "-r";
    private static final String DELETE_SOUND_OPTION = "-an";

    private final FFmpeg fFmpeg;
    private final FFprobe fFprobe;

    public void convertGifToMp4(String gifFilePath, String mp4FilePath) {
        log.info("convert gif to mp4 {} -> {}", gifFilePath, mp4FilePath);
        try {
            ImageSize gifImageSizeWithResize = ImageSize.resizeOf(gifFilePath);

            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(gifFilePath)
                    .overrideOutputFiles(true)
                    .addExtraArgs(FRAME_RATE_OPTION, "10")
                    .addOutput(mp4FilePath)
                    .addExtraArgs(DELETE_SOUND_OPTION)
                    .setVideoMovFlags("faststart")
                    .setVideoPixelFormat("yuv420p")
                    .setVideoFilter("scale=trunc(iw/2)*2:trunc(ih/2)*2")
                    .setVideoWidth(gifImageSizeWithResize.getWidthOnesRounded())
                    .setVideoHeight(gifImageSizeWithResize.getHeightOnesRounded())
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(fFmpeg, fFprobe);
            FFmpegJob job = executor.createJob(builder);
            job.run();
        } catch (RuntimeException e) {
            throw new InternalServerErrorException(ErrorType.GIF_MP4_CONVERT_FAIL);
        }
    }
}
