package com.wooteco.nolto.image.config;

import net.bramp.ffmpeg.FFmpeg;
import org.bytedeco.javacpp.Loader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FFmpegConfig {

    @Bean
    public FFmpeg ffmpeg() throws IOException {
        String ffmpegPath = Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
        return new FFmpeg(ffmpegPath);
    }
}
