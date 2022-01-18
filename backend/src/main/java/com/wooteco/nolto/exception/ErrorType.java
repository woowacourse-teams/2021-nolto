package com.wooteco.nolto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    DATA_BINDING_ERROR("common-001", "요청값이 잘못되었습니다."),
    LOGIC_ERROR("common-002", "서버 내부의 에러입니다."),
    NOT_FOUND("common-003", "해당 url을 찾을 수 없습니다."),

    INVALID_TOKEN("auth-001", "유효하지 않은 토큰입니다."),
    TOKEN_NEEDED("auth-002", "토큰이 필요합니다."),
    INVALID_OAUTH_CODE("auth-003", "oauth code가 잘못되었습니다."),
    USER_NOT_FOUND("auth-004", "존재하지 않는 유저입니다."),
    NOT_SUPPORTED_SOCIAL_LOGIN("auth-005", "지원하지 않는 소셜 로그인입니다."),
    SOCIAL_LOGIN_CONNECTION_FAIL("auth-006", "소셜 로그인 연동에 실패했습니다."),
    INVALID_CLIENT("auth-007", "권한이 없는 클라이언트의 요청입니다."),
    ADDRESS_NOT_FOUND("auth-008", "클라이언트 IP가 존재하지 않습니다."),
    ADMIN_ONLY("auth-009", "어드민 권한이 필요합니다."),

    FEED_NOT_FOUND("feed-001", "존재하지 않는 피드입니다."),
    MISSING_DEPLOY_URL("feed-002", "전시 중 피드는 deployUrl이 필수입니다."),
    UNAUTHORIZED_UPDATE_FEED("feed-003", "피드는 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_DELETE_FEED("feed-004", "피드는 작성자만 삭제할 수 있습니다."),
    NOT_SUPPORTED_FILTERING("feed-005", "지원하지 않는 피드의 필터링 값입니다."),
    NOT_SUPPORTED_STEP("feed-006", "지원하지 않는 피드의 Step입니다."),
    MULTIPART_CONVERT_FAIL("feed-007", "MultipartFile 변환에 실패하였습니다."),
    IMAGE_RESIZING_FAIL("feed-008", "이미지 리사이징에 실패하였습니다."),
    GIF_MP4_CONVERT_FAIL("feed-009", "gif파일을 mp4파일로 변환에 실패하였습니다."),

    NOT_SUPPORTED_IMAGE("image-001", "지원하지 않는 이미지 입니다."),
    IMAGE_NOT_FOUND("image-002", "존재하지 않는 이미지 입니다."),
    IMAGE_FORMAT_NOT_SUPPORTED("image-003", "지원하지 않는 이미지 양식입니다."),

    ALREADY_LIKED("like-001", "이미 좋아요 누른 글 입니다."),
    NOT_LIKED("like-002", "좋아요를 누르지 않았습니다."),

    COMMENT_NOT_FOUND("comment-001", "존재하지 않는 댓글입니다."),
    UNAUTHORIZED_UPDATE_COMMENT("comment-002", "댓글은 작성자만 수정할 수 있습니다."),
    UNAUTHORIZED_DELETE_COMMENT("comment-003", "댓글은 작성자만 삭제할 수 있습니다."),
    ALREADY_LIKED_COMMENT("comment-004", "이미 좋아요 누른 댓글 입니다."),
    NOT_LIKED_COMMENT("comment-005", "좋아요를 누르지 않은 댓글입니다."),
    REPLY_NOT_SUPPORTED_HELPER("comment-006", "대댓글은 도와줄게요로 설정할 수 없습니다."),

    ALREADY_EXIST_NICKNAME("member-001", "이미 존재하는 닉네임입니다."),
    NOTIFICATION_NOT_FOUND("member-002", "존재하지 않는 알림입니다."),
    UNAUTHORIZED_DELETE_NOTIFICATION("member-003", "알림은 본인만 삭제할 수 있습니다.");

    private final String errorCode;
    private final String message;
}
