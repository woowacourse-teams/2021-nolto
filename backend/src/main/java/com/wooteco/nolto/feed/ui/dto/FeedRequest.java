package com.wooteco.nolto.feed.ui.dto;

import com.wooteco.nolto.feed.domain.Feed;
import com.wooteco.nolto.feed.domain.Step;
import com.wooteco.nolto.tech.domain.TechValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {
    @NotBlank(message = "제목은 빈 값일 수 없습니다.")
    private String title;

    @TechValid
    private List<Long> techs = new ArrayList<>();

    @NotBlank(message = "내용은 빈 값일 수 없습니다.")
    private String content;

    @NotBlank(message = "스텝은 빈 값일 수 없습니다.")
    private String step;

    @NotNull(message = "SOS는 빈 값일 수 없습니다.")
    private boolean sos;

    private String storageUrl;
    private String deployedUrl;
    private MultipartFile thumbnailImage;

    public Feed toEntityWithThumbnailUrl(String thumbnailUrl) {
        return new Feed(
                this.title,
                this.content,
                Step.of(step),
                this.sos,
                this.storageUrl,
                this.deployedUrl,
                thumbnailUrl
        );
    }
}
