package com.wooteco.nolto;

import com.wooteco.nolto.tech.domain.Tech;

public class TechFixture {

    private TechFixture() {
    }

    public static Tech 자바_생성() {
        return new Tech("Java");
    }

    public static Tech 자바스크립트_생성() {
        return new Tech("JavaScript");
    }

    public static Tech 리액트_생성() {
        return new Tech("React");
    }

    public static Tech 리액트_네이티브_생성() {
        return new Tech("React Native");
    }

    public static Tech 스프링_생성() {
        return new Tech("Spring");
    }
}
