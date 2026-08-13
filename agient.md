# Agent Instructions

이 프로젝트에서 작업하는 LLM/Agent는 먼저 `docs/README.md`를 열고 LLM Wiki를 확인한다.

## 필수 참조

- `docs/README.md`
- `docs/00-project-map.md`
- `docs/14-llm-maintenance-guide.md`

## 작업 규칙

- 코드 수정 전 관련 Wiki 문서를 확인한다.
- 코드 변경 후 관련 Wiki 문서를 함께 갱신한다.
- `.env`의 실제 키, 토큰, 비밀번호 값은 답변이나 문서에 노출하지 않는다.
- 공개 사용 API는 `myinfo`, `resume`, `auth` 패키지를 우선 확인한다.
- 구직자 관리 API는 `seeker` 패키지를 우선 확인한다.
- 관리자 전용 API는 `admin` 패키지를 우선 확인한다.
- 공통 엔티티, 에러, 보안 코드는 `global` 패키지를 우선 확인한다.
- 검증은 최소 `.\gradlew.bat compileJava`로 수행한다.
- `*.md` 문서는 UTF-8로 저장하고 깨짐 여부를 확인한다.
- 코드 작성시 에러로그,프롬프트 등 문자열 작성시에는 한글로 작성한다.

## Wiki 연결

프로젝트 구조와 규칙은 Obsidian 호환 LLM Wiki인 `docs/README.md`에서 시작한다. 작업 시작 시 `[[README]]`, `[[00-project-map]]`, `[[14-llm-maintenance-guide]]` 흐름으로 문맥을 확인한다.
