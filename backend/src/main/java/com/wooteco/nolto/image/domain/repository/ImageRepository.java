package com.wooteco.nolto.image.domain.repository;

import com.wooteco.nolto.image.domain.ProcessedImage;

public interface ImageRepository {

    String save(ProcessedImage processedImage);
}
