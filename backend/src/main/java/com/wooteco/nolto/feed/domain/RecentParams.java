package com.wooteco.nolto.feed.domain;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RecentParams {

    private String step;
    @Pattern(regexp = "^true$|^false$", message = "Boolean 타입이 아닙니다.")
    private String help = "false";
    @Pattern(regexp = "^[1-9][0-9]*$", message = "자연수만 가능합니다.")
    private String nextFeedId = "10000000";
    @Pattern(regexp = "^[1-9][0-9]*$", message = "자연수만 가능합니다.")
    private String countPerPage = "15";

    public String getStep() {
        return step;
    }

    public boolean getHelp() {
        return Boolean.parseBoolean(help);
    }

    public long getNextFeedId() {
        return Long.parseLong(nextFeedId);
    }

    public int getCountPerPage() {
        return Integer.parseInt(countPerPage);
    }
}
