# 공개 MyInfo API

## POST /api/ask

컨트롤러:

- `MyInfoAiController`

요청:

```json
{
  "question": "어떤 프로젝트를 했나요?"
}
```

응답:

```json
{
  "question": "어떤 프로젝트를 했나요?",
  "selectedFiles": ["myinfo.md"],
  "tableOfContentsResponse": "...",
  "answer": "..."
}
```

처리:

1. `checklist.md`를 읽는다.
2. OpenAI에 질문과 관련된 문서 목록을 고르게 한다.
3. 선택 결과를 파싱하고 허용된 문서명만 사용한다.
4. 선택된 Markdown 문서를 읽는다.
5. OpenAI에 최종 답변을 생성하게 한다.
6. 비동기로 질문 이력을 저장하고 Telegram 알림을 보낸다.

관련 문서:

- [[06-ai-question-flow]]
- [[10-telegram]]

## POST /api/ask/progress

컨트롤러:

- `MyInfoAiController`

역할:

- 질문 처리 진행 상태를 NDJSON으로 스트리밍한다.

응답 Content-Type:

```text
application/x-ndjson
```

이벤트 예:

```json
{"type":"progress","message":"질문에 맞는 문서 목록을 선별하고 있습니다."}
{"type":"progress","message":"선별된 문서를 기반으로 답변을 생성하고 있습니다."}
{"type":"done","data":{"question":"...","selectedFiles":[],"tableOfContentsResponse":"...","answer":"..."}}
```

## GET /api/myinfo/documents/{fileName}

컨트롤러:

- `MyInfoDocumentController`

역할:

- `static/myinfo`의 단일 Markdown 문서를 text/plain으로 반환한다.
- 응답은 `Cache-Control: no-store`를 사용한다.

제약:

- `.md` 파일명만 허용한다.
- `/`, `\`, `..`가 포함된 경로 탐색 입력은 거부한다.

## GET /api/myinfo/recommended-questions

컨트롤러:

- `MyInfoRecommendedQuestionController`

역할:

- 공개 노출 가능한 추천 질문 목록을 반환한다.
- `enabled=true`인 항목만 반환한다.
- `displayOrder`, `id` 오름차순으로 정렬한다.

## GET /api/myinfo/recommended-questions/{id}/answer

컨트롤러:

- `MyInfoRecommendedQuestionController`

역할:

- 추천 질문 ID로 저장된 질문 이력의 질문/답변을 반환한다.
- OpenAI를 다시 호출하지 않는다.
- `enabled=true`인 추천 질문만 조회된다.

## POST /api/myinfo/visits

컨트롤러:

- `SiteVisitController`

역할:

- 공개 프론트엔드 방문 로그를 저장한다.

응답:

```text
204 No Content
```

## GET /api/resumes/{publicKey}

컨트롤러:

- `ResumeApiController`

역할:

- `AppUser.publicKey`로 공개 이력서 정보를 조회한다.
- 현재는 공개 키, 소유자 이름, 준비 메시지를 반환하는 초기 연결 상태다.
