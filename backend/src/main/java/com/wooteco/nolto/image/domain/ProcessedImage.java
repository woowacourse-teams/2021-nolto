package com.wooteco.nolto.image.domain;

import java.io.File;

public class ProcessedImage {

    private final File file;
    private final String imageName;

    public ProcessedImage(File file, String imageName) {
        this.file = file;
        this.imageName = imageName;
    }

    public File getFile() {
        return file;
    }

    public String getImageName() {
        return imageName;
    }
}
