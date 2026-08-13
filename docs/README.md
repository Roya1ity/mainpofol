# My-Ai-Cv LLM Wiki

이 문서는 Obsidian에서 열어 탐색할 수 있는 프로젝트용 LLM Wiki의 시작점이다.

## 빠른 진입

- [[00-project-map|프로젝트 지도]]
- [[01-runtime-and-config|실행 환경과 설정]]
- [[02-architecture|아키텍처]]
- [[03-domain-model|도메인 모델]]
- [[04-api-public-myinfo|공개 MyInfo API]]
- [[05-api-seeker-admin|Seeker/Admin API]]
- [[06-ai-question-flow|AI 질문 처리 흐름]]
- [[07-recommended-question-flow|추천 질문 흐름]]
- [[08-security-jwt-oauth|OAuth/JWT 보안]]
- [[09-database|DB 설계]]
- [[10-telegram|Telegram 연동]]
- [[11-error-handling|에러 처리]]
- [[12-dev-commands|개발 명령어]]
- [[13-file-index|파일 인덱스]]
- [[14-llm-maintenance-guide|LLM 유지보수 가이드]]

## 현재 프로젝트 성격

`My-Ai-Cv`는 자기소개/포트폴리오 Markdown 문서를 기반으로 사용자 질문에 답변하는 Spring Boot API 서버다. 공개 화면은 정적 리소스로 제공하고, OpenAI 응답 생성, 질문 이력 저장, 추천 질문 관리, OAuth 로그인, JWT 인증, 방문 통계를 포함한다.

핵심 기능:

- `static/myinfo` Markdown 문서를 기반으로 OpenAI 답변 생성
- 질문, 답변, 선택 문서 목록, 생성 시간 저장
- 질문/답변 Telegram 알림
- Google/Kakao OAuth 로그인
- JWT를 Authorization 헤더 또는 `ACCESS_TOKEN` 쿠키에서 읽어 인증
- `SEEKER`, `EMPLOYER`, `ADMIN` 역할 기반 API 보호
- 구직자용 질문 이력, 추천 질문, MyInfo 문서 CRUD
- 공개 추천 질문과 저장된 답변 조회
- 공개 이력서 키 기반 Resume API
- 방문 수 저장 및 구직자 대시보드 통계

## LLM 사용 규칙

작업을 시작하는 LLM은 먼저 [[00-project-map]]과 [[14-llm-maintenance-guide]]를 읽는다. 문서와 코드가 다르면 코드를 기준으로 판단하고 문서를 갱신한다.
