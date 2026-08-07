# 프로젝트 지도

## 루트

- `build.gradle`: Spring Boot, Spring AI, JPA, Security, MySQL 드라이버 의존성
- `settings.gradle`: Gradle 프로젝트명
- `.env`: 로컬 비밀값과 환경 설정
- `src/main/resources/application.yaml`: Spring 설정
- `src/main/resources/static/myinfo`: AI 답변 근거 Markdown 문서

## 주요 패키지

- `com.example.mainpofol`: 애플리케이션 진입점
- `com.example.mainpofol.config`: Spring Security 설정
- `com.example.mainpofol.admin`: 관리자 인증과 관리자 비즈니스 API
- `com.example.mainpofol.myinfo`: 공개 질문/추천 질문 API와 AI 서비스
- `com.example.mainpofol.global`: 공통 엔티티, 에러, JWT 보안
- `com.example.mainpofol.telegram`: 텔레그램 Bot API 연동

## 핵심 흐름

- 일반 질문: [[06-ai-question-flow]]
- 질문 히스토리 관리: [[05-api-admin]]
- 추천 질문 등록/응답: [[07-recommended-question-flow]]
- 관리자 인증: [[08-security-jwt]]
- DB 구조: [[09-database]]

## 주의할 점

- `.env`에는 실제 키가 들어갈 수 있으므로 문서나 커밋에 값을 복사하지 않는다.
- `application.yaml`의 datasource는 현재 MySQL URL을 직접 사용한다.
- `DB_URL` 환경변수는 현재 datasource URL에 연결되어 있지 않다.
- `MyInfoAiService`의 프롬프트 문자열 일부는 인코딩이 깨진 상태다. 의미를 복구하지 않은 상태에서 임의 수정하지 않는다.

