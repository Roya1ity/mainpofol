# 에러 처리

## 공통 에러 구조

파일:

- `CustomException`
- `ErrorCode`
- `ErrorResponse`
- `GlobalExceptionHandler`

응답 구조:

```json
{
  "timestamp": "2026-08-13T21:00:00",
  "status": 400,
  "code": "INVALID_REQUEST",
  "message": "Invalid request.",
  "details": []
}
```

## 처리 방식

`CustomException`은 `ErrorCode`의 HTTP status와 code를 사용한다.

주요 `ErrorCode`:

- `INVALID_REQUEST`
- `REQUIRED_FIELD_MISSING`
- `INVALID_JSON_REQUEST`
- `MYINFO_DOCUMENT_NOT_FOUND`
- `MYINFO_DOCUMENT_READ_FAILED`
- `OPENAI_REQUEST_FAILED`
- `INTERNAL_SERVER_ERROR`

validation 오류:

- `MethodArgumentNotValidException`
- `REQUIRED_FIELD_MISSING`
- field별 detail 포함

JSON 파싱 오류:

- `HttpMessageNotReadableException`
- `INVALID_JSON_REQUEST`

`ResponseStatusException`:

- HTTP status는 예외의 status를 따른다.
- code는 현재 `INVALID_REQUEST`로 감싼다.

기타 예외:

- error 로그 기록
- `INTERNAL_SERVER_ERROR`

## 현재 추가 API의 직접 예외

일부 Auth/MyInfo/Seeker/Resume API는 `ResponseStatusException`을 직접 사용한다.

예:

- 로그인 필요: 401
- 추천 질문 없음: 404
- 질문 이력 없음: 404
- 공개 이력서 키 없음: 404
- 이미 연결된 추천 질문: 409
- 지원하지 않는 OAuth provider: 400
