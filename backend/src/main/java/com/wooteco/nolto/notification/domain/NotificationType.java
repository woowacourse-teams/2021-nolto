package com.wooteco.nolto.notification.domain;

public enum NotificationType {
    COMMENT_SOS, COMMENT, LIKE, REPLY;

    public static NotificationType findCommentBy(boolean isHelper) {
        if (isHelper) {
            return COMMENT_SOS;
        }
        return COMMENT;
    }
}
