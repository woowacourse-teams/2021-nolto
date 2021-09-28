package com.wooteco.nolto.feed.ui.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    @NotBlank
    private String content;
    private boolean helper;

    public CommentRequest(String content, boolean helper) {
        this.content = content;
        this.helper = helper;
    }
}
