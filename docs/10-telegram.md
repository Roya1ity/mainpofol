# Telegram 연동

파일:

- `src/main/java/com/example/myaicv/telegram/TelegramMessageService.java`

## 설정

`.env`:

```properties
TELEGRAM_BOT_TOKEN=
TELEGRAM_CHAT_ID=
```

## 발송 시점

`POST /api/ask` 또는 `POST /api/ask/progress` 처리 중 최종 답변 생성 후 비동기로 발송한다.

발송 내용:

- 질문 내용
- 답변 내용

발송하지 않는 내용:

- 선택된 문서 목록
- 문서 선택 OpenAI 응답
- 생성 시간

## 실패 처리

- 토큰이나 chat id가 없으면 warn 로그만 남기고 건너뛴다.
- Telegram API 호출 실패는 error 로그만 남긴다.
- Telegram 실패는 사용자 질문 API 응답을 막지 않는다.
