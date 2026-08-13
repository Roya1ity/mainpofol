# 프로젝트 지도

## 루트

- `build.gradle`: Spring Boot 4.1.0, Spring AI, JPA, Security, OAuth2 Client, MySQL 의존성
- `settings.gradle`: Gradle 프로젝트명 `My-Ai-Cv`
- `.env`: 로컬 비밀값과 환경 설정
- `.env.example`: 필요한 환경 변수 예시
- `src/main/resources/application.yaml`: Spring 런타임 설정
- `src/main/resources/static`: 정적 프론트엔드 리소스
- `src/main/resources/static/myinfo`: AI 답변 근거 Markdown 문서
- `docker-compose.yml`: nginx 프론트엔드, Spring Boot 백엔드, MySQL 구성
- `Dockerfile`, `Dockerfile.frontend`, `nginx.conf`: 배포 컨테이너 구성

## 주요 패키지

- `com.example.myaicv`: 애플리케이션 진입점
- `com.example.myaicv.config`: 비동기 실행기와 Spring Security 설정
- `com.example.myaicv.auth`: OAuth 로그인, 사용자 저장, JWT 쿠키 처리
- `com.example.myaicv.myinfo`: 공개 질문 API, 문서 조회, 추천 질문 조회, 방문 기록
- `com.example.myaicv.seeker`: 구직자 인증 API, MyInfo 문서/이력/추천 질문 CRUD, 대시보드
- `com.example.myaicv.resume`: 공개 이력서 페이지/API
- `com.example.myaicv.admin`: 관리자 전용 메뉴 API. 세부 기능은 아직 확장 예정
- `com.example.myaicv.global`: 공통 엔티티, 에러 응답, JWT 보안
- `com.example.myaicv.telegram`: Telegram Bot API 연동

## 핵심 흐름

- 공개 질문: [[06-ai-question-flow]]
- 추천 질문: [[07-recommended-question-flow]]
- 인증/권한: [[08-security-jwt-oauth]]
- DB 구조: [[09-database]]
- 실행/배포 설정: [[01-runtime-and-config]]

## 주의 사항

- `.env`에는 실제 키가 들어갈 수 있으므로 문서나 답변에 값 자체를 복사하지 않는다.
- 현재 datasource 기본 DB명은 `myaicv`다.
- MyInfo 문서는 UTF-8 Markdown으로 읽고 쓴다.
- 과거 문서에는 `mainpofol` 패키지와 `/api/admin/**` CRUD가 남아 있을 수 있으나, 현재 코드는 `com.example.myaicv`와 `/api/seeker/**` 중심이다.
