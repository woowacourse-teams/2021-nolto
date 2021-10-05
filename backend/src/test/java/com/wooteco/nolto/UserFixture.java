package com.wooteco.nolto;

import com.wooteco.nolto.auth.domain.SocialType;
import com.wooteco.nolto.user.domain.User;

public class UserFixture {

    private static final String DEFAULT_USER_IMAGE = "nolto-default-thumbnail.png";

    private UserFixture() {
    }

    public static User 아마찌_생성() {
        return User.builder()
                .socialId("1")
                .socialType(SocialType.GITHUB)
                .nickName("AMAZZI")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is mazzi")
                .build();
    }

    public static User 조엘_생성() {
        return User.builder()
                .socialId("2")
                .socialType(SocialType.GITHUB)
                .nickName("JOEL")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is joel")
                .build();
    }

    public static User 찰리_생성() {
        return User.builder()
                .socialId("3")
                .socialType(SocialType.GITHUB)
                .nickName("CHARLIE")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is charlie")
                .build();
    }

    public static User 포모_생성() {
        return User.builder()
                .socialId("4")
                .socialType(SocialType.GITHUB)
                .nickName("POMO")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is pomo")
                .build();
    }

    public static User 깃헙_유저_생성() {
        return User.builder()
                .socialId("5")
                .socialType(SocialType.GITHUB)
                .nickName("엄청난 깃헙 유저")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is 엄청난 깃헙 유저")
                .build();
    }

    public static User 구글_유저_생성() {
        return User.builder()
                .socialId("6")
                .socialType(SocialType.GITHUB)
                .nickName("엄청난 구글 유저")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is 엄청난 구글 유저")
                .build();
    }

    public static User ID_있는_유저_생성(Long id) {
        return User.builder()
                .id(id)
                .socialId("7")
                .socialType(SocialType.GITHUB)
                .nickName("엄청난 유저")
                .imageUrl(DEFAULT_USER_IMAGE)
                .bio("This is 엄청난 유저")
                .build();
    }
}
