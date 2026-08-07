# 에러 처리

## 공통 에러 구조

파일:

- `CustomException`
- `ErrorCode`
- `ErrorResponse`
- `GlobalExceptionHandler`

## 처리 방식

`CustomException`은 `ErrorCode`의 HTTP status와 code를 사용한다.

validation 오류:

- `MethodArgumentNotValidException`
- `REQUIRED_FIELD_MISSING`

JSON 파싱 오류:

- `HttpMessageNotReadableException`
- `INVALID_JSON_REQUEST`

기타 예외:

- `INTERNAL_SERVER_ERROR`

## 현재 추가 API의 예외

일부 Admin/MyInfo API는 `ResponseStatusException`을 직접 사용한다.

예:

- 추천 질문 없음: 404
- 질문 히스토리 없음: 404
- 이미 연결된 추천 질문: 409

향후 일관성을 높이려면 `ErrorCode`에 다음 코드를 추가할 수 있다.

- `ASK_HISTORY_NOT_FOUND`
- `RECOMMENDED_QUESTION_NOT_FOUND`
- `RECOMMENDED_QUESTION_ALREADY_LINKED`
- `ADMIN_PASSWORD_NOT_CONFIGURED`

