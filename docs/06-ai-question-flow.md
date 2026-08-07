# AI 질문 처리 흐름

핵심 클래스:

- `MyInfoAiService`
- `MyInfoAskPostProcessService`
- `MyInfoDocumentService`
- `MyInfoSelectionParser`
- `AdminMyInfoAskHistoryService`
- `TelegramMessageService`

## 흐름

```text
사용자 질문
  -> checklist.md 로드
  -> 허용 문서 목록 추출
  -> 진행 이벤트: 질문에 맞는 문서리스트 선별중..
  -> OpenAI 1차 호출: 관련 문서 선택
  -> 선택 결과 파싱
  -> 선택된 Markdown 문서 로드
  -> 진행 이벤트: 선별된 문서를 기반으로 답변 생성중..
  -> OpenAI 2차 호출: 최종 답변 생성
  -> 비동기 후처리 요청
  -> API 응답 반환

비동기 후처리
  -> 질문 히스토리 저장
  -> 텔레그램 발송
```

## 문서 선택

`MyInfoSelectionParser`는 OpenAI의 1차 응답에서 JSON을 추출한다.

예상 응답 구조:

```json
{
  "files": ["myinfo.md"],
  "reason": "선택 이유"
}
```

허용 문서 목록에 없는 파일은 저장하지 않는다.

## 저장

`MyInfoAiService`는 직접 Repository를 호출하지 않는다.

현재 저장 경로:

```text
MyInfoAiService
  -> MyInfoAskPostProcessService.saveAndSendTelegram (@Async)
  -> AdminMyInfoAskHistoryService.save
  -> MyInfoAskHistoryRepository.save
```

`MyInfoAiService`는 최종 답변 생성 후 DB 저장과 텔레그램 발송을 기다리지 않고 응답을 반환한다.

## 주의

- 후처리 executor는 `AsyncConfig.myInfoPostProcessExecutor`다.
- 히스토리 저장 실패와 텔레그램 발송 실패는 비동기 서비스에서 로깅하고 사용자 응답을 막지 않는다.
- 프롬프트를 수정할 때는 테스트 질문으로 문서 선택과 답변 품질을 검증한다.
