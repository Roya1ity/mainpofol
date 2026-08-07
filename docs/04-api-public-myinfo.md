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
2. OpenAI에게 질문과 관련된 문서 목록을 고르게 한다.
3. 선택된 Markdown 문서를 읽는다.
4. OpenAI에게 최종 답변을 생성하게 한다.
5. `AdminMyInfoAskHistoryService.save`를 통해 질문 히스토리에 저장한다.
6. 질문과 답변만 텔레그램으로 발송한다.

관련 문서:

- [[06-ai-question-flow]]
- [[10-telegram]]

## POST /api/ask/progress

컨트롤러:

- `MyInfoAiController`

역할:

- 질문 처리 진행 상태를 NDJSON으로 스트리밍한다.
- 프론트 로딩 말풍선의 상태 문구에 사용한다.

응답 Content-Type:

```text
application/x-ndjson
```

이벤트 예:

```json
{"type":"progress","message":"질문에 맞는 문서리스트 선별중.."}
{"type":"progress","message":"선별된 문서를 기반으로 답변 생성중.."}
{"type":"done","data":{"question":"...","selectedFiles":[],"tableOfContentsResponse":"...","answer":"..."}}
```

## GET /api/myinfo/recommended-questions

컨트롤러:

- `MyInfoRecommendedQuestionController`

역할:

- 공개 노출 가능한 추천 질문 목록을 반환한다.
- `enabled=true`인 항목만 반환한다.
- `displayOrder` 오름차순, `id` 오름차순으로 정렬한다.

응답:

```json
[
  {
    "id": 1,
    "question": "대표 프로젝트가 뭐야?",
    "displayOrder": 0
  }
]
```

## GET /api/myinfo/recommended-questions/{id}/answer

컨트롤러:

- `MyInfoRecommendedQuestionController`

역할:

- 추천 질문 ID로 추천 질문을 조회한다.
- 연결된 `MyInfoAskHistory`의 질문/답변을 반환한다.
- OpenAI를 다시 호출하지 않는다.

응답:

```json
{
  "recommendedQuestionId": 1,
  "askHistoryId": 10,
  "question": "대표 프로젝트가 뭐야?",
  "answer": "저장된 답변",
  "selectedDocuments": ["myinfo.md"],
  "createdAt": "2026-08-06T12:00:00"
}
```

조건:

- `enabled=true`인 추천 질문만 조회된다.
- 없거나 비활성화된 추천 질문은 404로 처리된다.

## POST /api/myinfo/visits

컨트롤러:

- `SiteVisitController`

역할:

- 공개 프론트엔드 최초 로드 시 방문 로그를 저장한다.
- 응답 본문은 없다.

응답:

```text
204 No Content
```
