package com.wooteco.nolto.feed.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyRequest {

    @NotNull(message = "대댓글이 입력되지 않았습니다.")
    private String content;

}
