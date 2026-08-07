package com.example.mainpofol.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "요청 값이 올바르지 않습니다."),
    REQUIRED_FIELD_MISSING(HttpStatus.BAD_REQUEST, "REQUIRED_FIELD_MISSING", "필수 요청 값이 누락되었습니다."),
    INVALID_JSON_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_JSON_REQUEST", "요청 JSON 형식이 올바르지 않습니다."),
    MYINFO_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "MYINFO_DOCUMENT_NOT_FOUND", "요청한 자기소개 문서를 찾을 수 없습니다."),
    MYINFO_DOCUMENT_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MYINFO_DOCUMENT_READ_FAILED", "자기소개 문서를 읽는 중 오류가 발생했습니다."),
    OPENAI_REQUEST_FAILED(HttpStatus.BAD_GATEWAY, "OPENAI_REQUEST_FAILED", "OpenAI API 요청 중 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
