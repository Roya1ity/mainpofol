# LLM 유지보수 가이드

이 프로젝트를 수정하는 LLM은 다음 순서를 따른다.

## 시작 전 확인

1. [[00-project-map]]을 읽는다.
2. 관련 기능 문서를 읽는다.
3. 실제 Java 파일을 확인한다.
4. 문서와 코드가 다르면 코드를 기준으로 판단하고 문서를 갱신한다.

## 코드 수정 원칙

- 기존 패키지 구조를 따른다.
- 공개 사용자 기능은 `myinfo` 패키지에 둔다.
- 관리자 기능은 `admin` 패키지에 둔다.
- 공통 엔티티와 보안은 `global` 패키지에 둔다.
- Repository를 직접 여러 서비스에 퍼뜨리기보다 현재 서비스 경계를 우선 재사용한다.
- `.env` 실제 값을 문서나 로그에 복사하지 않는다.

## API 추가 시 문서 갱신

API를 추가하거나 변경하면 다음 문서를 갱신한다.

- 공개 API: [[04-api-public-myinfo]]
- 관리자 API: [[05-api-admin]]
- 추천 질문 흐름: [[07-recommended-question-flow]]
- DB 변경: [[09-database]]

## DB 변경 시 문서 갱신

엔티티 필드나 관계를 바꾸면 다음 문서를 갱신한다.

- [[03-domain-model]]
- [[09-database]]

## 검증

기본 검증:

```powershell
.\gradlew.bat compileJava
```

가능하면 API 동작까지 확인한다.

