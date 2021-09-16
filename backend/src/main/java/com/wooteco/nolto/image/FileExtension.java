package com.wooteco.nolto.image;

public class FileExtension {

    public static final String FILENAME_EXTENSION_DOT = ".";
    public static final String GIF = ".gif";
    public static final String MP4 = ".mp4";
    public static final String PNG = ".png";
    public static final String JPG = ".jpg";

    private FileExtension() {
    }

    public static boolean isGifFile(String fileName) {
        return fileName.endsWith(GIF);
    }

    public static boolean isNotGifFile(String fileName) {
        return !fileName.endsWith(GIF);
    }
}
