# Mainpofol LLM Wiki

이 문서는 Obsidian에서 열어 탐색할 수 있는 프로젝트용 LLM Wiki의 시작점이다.

## 빠른 진입

- [[00-project-map|프로젝트 지도]]
- [[01-runtime-and-config|실행 환경과 설정]]
- [[02-architecture|아키텍처]]
- [[03-domain-model|도메인 모델]]
- [[04-api-public-myinfo|공개 MyInfo API]]
- [[05-api-admin|Admin API]]
- [[06-ai-question-flow|AI 질문 처리 흐름]]
- [[07-recommended-question-flow|추천 질문 흐름]]
- [[08-security-jwt|JWT 관리자 인증]]
- [[09-database|DB 설계]]
- [[10-telegram|텔레그램 연동]]
- [[11-error-handling|에러 처리]]
- [[12-dev-commands|개발 명령어]]
- [[13-file-index|파일 인덱스]]
- [[14-llm-maintenance-guide|LLM 유지보수 가이드]]

## 현재 프로젝트 성격

`mainpofol`은 자기소개/포트폴리오 문서를 기반으로 사용자 질문에 답변하는 Spring Boot API 서버다.

핵심 기능은 다음과 같다.

- `static/myinfo` Markdown 문서를 기반으로 OpenAI 답변 생성
- 질문, 답변, 선택된 문서 목록, 생성 시간 저장
- 질문/답변 텔레그램 발송
- 관리자 JWT 로그인
- 관리자 질문 히스토리 CRUD
- 관리자 추천 질문 CRUD
- 공개 추천 질문 ID 기반 저장 답변 조회

## LLM 사용 규칙

새 작업을 시작하는 LLM은 먼저 [[00-project-map]]과 [[14-llm-maintenance-guide]]를 읽는다.

