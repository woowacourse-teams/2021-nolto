package com.wooteco.nolto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {
    LOGIC_ERROR("common-002", "서버 내부의 에러입니다."),

    INVALID_TOKEN("auth-001", "유효하지 않은 토큰입니다."),
    TOKEN_NEEDED("auth-002", "토큰이 필요합니다."),
    INVALID_OAUTH_CODE("auth-003", "oauth code가 잘못되었습니다."),
    USER_NOT_FOUND("auth-004", "존재하지 않는 유저입니다."),
    NOT_SUPPORTED_SOCIAL_LOGIN("auth-005", "지원하지 않는 소셜 로그인입니다."),
    SOCIAL_LOGIN_CONNECTION_FAIL("auth-006", "소셜 로그인 연동에 실패했습니다."),

    FEED_NOT_FOUND("feed-001", "존재하지 않는 피드입니다."),
    MISSING_DEPLOY_URL("feed-002", "전시 중 피드는 deployUrl이 필수입니다."),
    UNAUTHORIZED_UPDATE_FEED("feed-003", "피드는 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_DELETE_FEED("feed-004", "피드는 작성자만 삭제할 수 있습니다."),
    NOT_SUPPORTED_FILTERING("feed-005", "지원하지 않는 피드의 필터링 값입니다."),
    NOT_SUPPORTED_STEP("feed-006", "지원하지 않는 피드의 Step입니다."),
    MULTIPART_CONVERT_FAIL("feed-007", "MultipartFile 변환에 실패하였습니다."),

    ALREADY_LIKED("like-001", "이미 좋아요 누른 글 입니다."),
    NOT_LIKED("like-002", "좋아요를 누르지 않았습니다."),

    COMMENT_NOT_FOUND("comment-001", "존재하지 않는 댓글입니다."),
    UNAUTHORIZED_UPDATE_COMMENT("comment-002", "댓글은 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_DELETE_COMMENT("comment-003", "댓글은 작성자만 삭제할 수 있습니다."),
    ALREADY_LIKED_COMMENT("comment-004", "이미 좋아요 누른 댓글 입니다."),
    NOT_LIKED_COMMENT("comment-005", "좋아요를 누르지 않은 댓글입니다."),

    ALREADY_EXIST_NICKNAME("member-001", "이미 존재하는 닉네임입니다.");

    private String errorCode;
    private String message;
}
