package com.capstonebe.capstonebe.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    IS_NOT_ADMIN(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트를 찾을 수 없습니다."),

    // FAQ
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 FAQ를 찾을 수 없습니다."),

    // Inquiry
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문의를 찾을 수 없습니다."),

    // item
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "해당 아이템이 존재하지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // place
    NOT_FOUND_PLACE(HttpStatus.NOT_FOUND, "해당 장소가 존재하지 않습니다."),

    // category
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),

    // image
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지 찾을 수 없습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다."),
    IMAGE_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환에 실패했습니다."),
    IMAGE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 삭제 중 오류가 발생했습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제하려는 파일이 존재하지 않습니다."),
    INVALID_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 형식입니다."),

    // keyword
    DUPLICATED_KEYWORD(HttpStatus.BAD_REQUEST, "이미 등록된 키워드입니다."),
    NOT_FOUND_KEYWORD(HttpStatus.NOT_FOUND, "해당 키워드를 찾을 수 없습니다."),

    // Global
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "유효성 검증 실패"),
    NPE(HttpStatus.BAD_REQUEST, "Null Pointer Exception"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류");

    private final HttpStatus status;
    private final String message;
}
