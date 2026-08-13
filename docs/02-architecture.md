# 아키텍처

## 기본 레이어

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
- `resume.controller`
- `auth.controller`

역할:

- 공개 질문을 받아 MyInfo Markdown 문서 기반 답변 생성
- 질문 처리 진행 상황을 NDJSON으로 스트리밍
- 추천 질문 목록과 저장된 답변 제공
- 개별 MyInfo 문서 텍스트 조회
- 공개 이력서 키 기반 Resume 조회
- OAuth 로그인 진입점과 현재 사용자 조회

관련 문서:

- [[04-api-public-myinfo]]
- [[06-ai-question-flow]]
- [[07-recommended-question-flow]]

## 인증 사용자 영역

패키지:

- `seeker.controller`
- `seeker.service`
- `seeker.dto`
- `seeker.repository`

역할:

- 구직자 대시보드 방문 통계 조회
- 질문 이력 CRUD
- 추천 질문 CRUD
- 질문 이력에서 추천 질문 등록
- MyInfo Markdown 문서 CRUD

관련 문서:

- [[05-api-seeker-admin]]
- [[08-security-jwt-oauth]]

## 인증/보안 영역

패키지:

- `auth.domain`
- `auth.service`
- `global.security`
- `config.SecurityConfig`

역할:

- Google/Kakao OAuth 프로필을 `AppUser`로 저장 또는 갱신
- `ADMIN_EMAILS`에 포함된 이메일은 `ADMIN` 권한 부여
- JWT 생성/검증
- `Authorization: Bearer` 또는 `ACCESS_TOKEN` 쿠키 인증

## 공통 영역

패키지:

- `global.entity`
- `global.error`

역할:

- JPA 엔티티
- 공통 에러 응답
- validation, JSON parsing, 커스텀 예외 처리
