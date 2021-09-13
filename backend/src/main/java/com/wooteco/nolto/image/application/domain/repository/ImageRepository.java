package com.wooteco.nolto.image.application.domain.repository;

import com.wooteco.nolto.image.application.domain.ProcessedImage;

public interface ImageRepository {

    String save(ProcessedImage processedImage);
}
