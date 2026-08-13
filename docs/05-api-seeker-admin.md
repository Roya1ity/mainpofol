# Seeker/Admin API

현재 MyInfo 관리 CRUD는 `/api/seeker/**` 아래에 있으며 `ROLE_SEEKER` 또는 `ROLE_ADMIN` 권한이 필요하다. `/api/admin/**`은 `ROLE_ADMIN` 전용이고 현재는 메뉴 연결 API만 있다.

인증 토큰 전달 방식:

```http
Authorization: Bearer {accessToken}
```

또는 OAuth 성공 후 설정되는 `ACCESS_TOKEN` HttpOnly 쿠키를 사용한다.

## GET /api/seeker/dashboard

컨트롤러:

- `SeekerDashboardController`

역할:

- 총 방문 수, 일자별 방문 수, 오늘 시간대별 방문 수를 반환한다.

## 질문 이력 CRUD

Base path:

```text
/api/seeker/myinfo-ask-histories
```

엔드포인트:

- `GET /api/seeker/myinfo-ask-histories`
- `GET /api/seeker/myinfo-ask-histories/{id}`
- `POST /api/seeker/myinfo-ask-histories`
- `PUT /api/seeker/myinfo-ask-histories/{id}`
- `DELETE /api/seeker/myinfo-ask-histories/{id}`

생성/수정 요청:

```json
{
  "question": "질문",
  "answer": "답변",
  "selectedDocuments": ["myinfo.md", "stack.md"]
}
```

## 질문 이력에서 추천 질문 등록

```text
POST /api/seeker/myinfo-ask-histories/{id}/recommended-question
```

요청 body는 생략 가능하다. 생략하면 질문 이력의 `question`을 추천 질문 문구로 사용한다.

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
/api/seeker/recommended-questions
```

엔드포인트:

- `GET /api/seeker/recommended-questions`
- `GET /api/seeker/recommended-questions/{id}`
- `POST /api/seeker/recommended-questions`
- `PUT /api/seeker/recommended-questions/{id}`
- `DELETE /api/seeker/recommended-questions/{id}`

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

- 하나의 `askHistoryId`에는 추천 질문 하나만 연결할 수 있다.
- 중복 연결 시 409 Conflict가 발생한다.

## MyInfo 문서 CRUD

Base path:

```text
/api/seeker/myinfo-documents
```

대상:

- `src/main/resources/static/myinfo/*.md`

엔드포인트:

- `GET /api/seeker/myinfo-documents`
- `GET /api/seeker/myinfo-documents/{fileName}`
- `POST /api/seeker/myinfo-documents`
- `PUT /api/seeker/myinfo-documents/{fileName}`
- `DELETE /api/seeker/myinfo-documents/{fileName}`

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
- `/`, `\`, `..`가 포함된 경로 탐색 파일명은 거부한다.
- `checklist.md`는 수정 가능하지만 삭제는 거부된다.

## GET /api/admin/menu

컨트롤러:

- `AdminMenuController`

역할:

- 관리자 메뉴 연결 상태를 반환한다.
- 세부 관리자 기능은 아직 확장 예정 상태다.
