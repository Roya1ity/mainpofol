# Admin API

모든 `/api/admin/**` API는 `POST /api/admin/login`을 제외하고 JWT 관리자 권한이 필요하다.

헤더:

```http
Authorization: Bearer {accessToken}
```

## POST /api/admin/login

요청:

```json
{
  "password": "관리자비밀번호"
}
```

응답:

```json
{
  "tokenType": "Bearer",
  "accessToken": "...",
  "expiresIn": 3600
}
```

## 질문 히스토리 CRUD

## GET /api/admin/dashboard

역할:

- 관리자 페이지 대시보드 값을 반환한다.

응답:

```json
{
  "visitCount": 123,
  "dailyVisits": [
    {
      "label": "2026-08-06",
      "count": 10
    }
  ],
  "hourlyVisits": [
    {
      "label": "21시",
      "count": 3
    }
  ]
}
```

관리자 프론트엔드는 이 응답으로 총 방문자수, 일자별 방문자수 그래프, 오늘 시간대별 방문자수 그래프를 표시한다.

## 질문 히스토리 CRUD

Base path:

```text
/api/admin/myinfo-ask-histories
```

엔드포인트:

- `GET /api/admin/myinfo-ask-histories`
- `GET /api/admin/myinfo-ask-histories/{id}`
- `POST /api/admin/myinfo-ask-histories`
- `PUT /api/admin/myinfo-ask-histories/{id}`
- `DELETE /api/admin/myinfo-ask-histories/{id}`

생성/수정 요청:

```json
{
  "question": "질문",
  "answer": "답변",
  "selectedDocuments": ["myinfo.md", "stack.md"]
}
```

## 질문 히스토리에서 추천 질문 등록

```text
POST /api/admin/myinfo-ask-histories/{id}/recommended-question
```

요청 body는 생략 가능하다. 생략하면 히스토리의 `question`을 추천 질문 문구로 사용한다.

```json
{
  "question": "화면에 노출할 추천 질문",
  "enabled": true,
  "displayOrder": 0
}
```

## 추천 질문 CRUD

Base path:

```text
/api/admin/recommended-questions
```

엔드포인트:

- `GET /api/admin/recommended-questions`
- `GET /api/admin/recommended-questions/{id}`
- `POST /api/admin/recommended-questions`
- `PUT /api/admin/recommended-questions/{id}`
- `DELETE /api/admin/recommended-questions/{id}`

생성/수정 요청:

```json
{
  "question": "대표 프로젝트가 뭐야?",
  "askHistoryId": 1,
  "enabled": true,
  "displayOrder": 0
}
```

제약:

- 하나의 `askHistoryId`는 추천 질문 하나에만 연결될 수 있다.
- 중복 연결 시 409 Conflict가 발생한다.

## MyInfo 문서 CRUD

Base path:

```text
/api/admin/myinfo-documents
```

대상:

- `src/main/resources/static/myinfo/*.md`

엔드포인트:

- `GET /api/admin/myinfo-documents`
- `GET /api/admin/myinfo-documents/{fileName}`
- `POST /api/admin/myinfo-documents`
- `PUT /api/admin/myinfo-documents/{fileName}`
- `DELETE /api/admin/myinfo-documents/{fileName}`

생성 요청:

```json
{
  "fileName": "example.md",
  "content": "# example"
}
```

수정 요청:

```json
{
  "content": "# updated"
}
```

제약:

- `.md` 파일명만 허용한다.
- `/`, `\\`, `..`가 포함된 경로 탐색 파일명은 거부한다.
- `checklist.md`는 수정 가능하지만 삭제는 거부한다.
