# LLM 유지보수 가이드

이 프로젝트를 수정하는 LLM은 다음 순서를 따른다.

## 시작 시 확인

1. [[00-project-map]]을 읽는다.
2. 변경하려는 기능과 관련된 문서를 읽는다.
3. 실제 Java, YAML, static 파일을 확인한다.
4. 문서와 코드가 다르면 코드를 기준으로 판단하고 문서를 갱신한다.

## 코드 수정 원칙

- 기존 패키지 구조를 따른다.
- 공개 질문/문서 조회 기능은 `myinfo` 패키지에 둔다.
- OAuth와 사용자 인증 기능은 `auth` 패키지에 둔다.
- 구직자 관리 기능은 `seeker` 패키지에 둔다.
- 관리자 전용 기능은 `admin` 패키지에 둔다.
- 공통 엔티티, 에러, JWT 보안은 `global` 패키지에 둔다.
- Repository를 여러 서비스에 무리하게 흩뿌리기보다 현재 서비스 경계를 우선 재사용한다.
- `.env` 실제 값을 문서나 로그에 복사하지 않는다.

## API 변경 시 문서 갱신

API를 추가하거나 변경하면 관련 문서를 갱신한다.

- 공개 API: [[04-api-public-myinfo]]
- Seeker/Admin API: [[05-api-seeker-admin]]
- 인증/권한: [[08-security-jwt-oauth]]
- 추천 질문 흐름: [[07-recommended-question-flow]]
- DB 변경: [[09-database]]
- 파일 위치 변경: [[13-file-index]]

## DB 변경 시 문서 갱신

엔티티 필드나 관계를 바꾸면 다음 문서를 갱신한다.

- [[03-domain-model]]
- [[09-database]]

## 검증

기본 검증:

```powershell
.\gradlew.bat compileJava
```

가능하면 관련 API 동작도 확인한다. 외부 연동 검증이 필요하면 OpenAI, OAuth, Telegram, MySQL 환경 변수가 준비되어 있는지 먼저 확인한다.

## 문서 인코딩

- Markdown 문서는 UTF-8로 저장한다.
- 한글이 `???`, `ê`, `ë`, `�`처럼 깨지면 인코딩이 손상된 것으로 보고 원문을 기준으로 복구한다.
- 이미 `?`로 대체된 문자는 원본 없이는 완전 복구할 수 없으므로 현재 코드 기준으로 재작성한다.
