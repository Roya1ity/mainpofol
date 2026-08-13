# AI 질문 처리 흐름

관련 클래스:

- `MyInfoAiService`
- `MyInfoAskPostProcessService`
- `MyInfoDocumentService`
- `MyInfoSelectionParser`
- `SeekerMyInfoAskHistoryService`
- `TelegramMessageService`

## 흐름

```text
사용자 질문
  -> checklist.md 로드
  -> 허용 문서 파일명 추출
  -> progress: 질문에 맞는 문서 목록을 선별하고 있습니다.
  -> OpenAI 1차 호출: 관련 문서 선택
  -> 선택 결과 JSON 파싱
  -> 선택된 Markdown 문서 로드
  -> progress: 선별된 문서를 기반으로 답변을 생성하고 있습니다.
  -> OpenAI 2차 호출: 최종 답변 생성
  -> 비동기 후처리 요청
  -> API 응답 반환

비동기 후처리
  -> 질문 이력 저장
  -> Telegram 알림 발송
```

## 문서 선택

`MyInfoDocumentService.extractDocumentFileNames`는 `checklist.md`에서 `](./file.md)` 형식의 링크를 추출한다.

`MyInfoSelectionParser`는 OpenAI의 1차 응답에서 JSON 객체를 추출한다.

예상 응답:

```json
{
  "files": ["myinfo.md"],
  "reason": "선택 이유"
}
```

허용 문서 목록에 없는 파일은 무시한다. 선택 결과가 비어 있으면 허용 문서 중 앞의 2개를 fallback으로 사용한다.

## 답변 생성

최종 답변 프롬프트의 원칙:

- 제공된 Markdown 문서 내용만 사용한다.
- 문서에서 확인할 수 없으면 확인할 수 없다고 답한다.
- 한국어로 답변한다.
- 사용자에게 추가 문서를 요구하지 않는다.

## 저장과 알림

`MyInfoAiService`는 직접 Repository를 호출하지 않는다.

현재 저장 경로:

```text
MyInfoAiService
  -> MyInfoAskPostProcessService.saveAndSendTelegram (@Async)
  -> SeekerMyInfoAskHistoryService.save
  -> MyInfoAskHistoryRepository.save
```

질문 이력 저장 실패나 Telegram 전송 실패는 비동기 서비스에서 로깅하고 사용자 응답을 막지 않는다.

## 비동기 설정

- AI 호출 executor: `AsyncConfig.myInfoAiExecutor`
- 후처리 executor: `AsyncConfig.myInfoPostProcessExecutor`
- Spring MVC async request timeout: `180000ms`
