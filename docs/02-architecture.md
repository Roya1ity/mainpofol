# 아키텍처

## 레이어

```text
Controller
  -> Service
    -> Repository
      -> Entity
```

## 공개 API 영역

패키지:

- `myinfo.controller`
- `myinfo.service`
- `myinfo.dto`

역할:

- 사용자 질문 받기
- Markdown 문서 선택
- OpenAI 답변 생성
- 추천 질문 ID로 저장된 답변 반환

관련 문서:

- [[04-api-public-myinfo]]
- [[06-ai-question-flow]]
- [[07-recommended-question-flow]]

## 관리자 API 영역

패키지:

- `admin.controller`
- `admin.service`
- `admin.dto`
- `admin.repository`

역할:

- 관리자 로그인
- 질문 히스토리 CRUD
- 추천 질문 CRUD
- 질문 히스토리 ID 기반 추천 질문 등록

관련 문서:

- [[05-api-admin]]
- [[08-security-jwt]]

## 공통 영역

패키지:

- `global.entity`
- `global.error`
- `global.security`

역할:

- JPA 엔티티
- 공통 에러 응답
- JWT 생성/검증

