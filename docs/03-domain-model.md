# 도메인 모델

## AppUser

파일:

- `src/main/java/com/example/myaicv/auth/domain/AppUser.java`

역할:

- OAuth로 로그인한 사용자를 저장한다.
- provider와 provider user id 조합을 unique로 관리한다.
- 공개 이력서 조회용 `publicKey`를 가진다.

필드:

- `id`: PK
- `provider`: `GOOGLE`, `KAKAO`
- `providerUserId`: OAuth 제공자 사용자 ID
- `email`: 이메일
- `name`: 표시 이름
- `role`: `SEEKER`, `EMPLOYER`, `ADMIN`
- `publicKey`: 공개 이력서 키
- `createdAt`, `updatedAt`: 생성/수정 시간

## MyInfoAskHistory

파일:

- `src/main/java/com/example/myaicv/global/entity/MyInfoAskHistory.java`

역할:

- 사용자 질문과 AI 답변을 저장한다.
- 선택된 Markdown 문서 목록을 JSON 문자열로 저장한다.

필드:

- `id`: PK
- `question`: 질문 내용
- `answer`: 답변 내용
- `selectedDocuments`: 선택 문서명 목록
- `createdAt`: 생성 시간

관계:

- `MyInfoRecommendedQuestion`과 1:1로 연결될 수 있다.

## MyInfoRecommendedQuestion

파일:

- `src/main/java/com/example/myaicv/global/entity/MyInfoRecommendedQuestion.java`

역할:

- 공개 화면에 노출할 추천 질문을 저장한다.
- 추천 질문은 저장된 질문 이력 하나와 연결된다.

필드:

- `id`: PK
- `question`: 추천 질문 문구
- `askHistory`: 연결된 질문 이력
- `enabled`: 공개 노출 여부
- `displayOrder`: 노출 정렬값
- `createdAt`: 생성 시간

## SiteVisit

파일:

- `src/main/java/com/example/myaicv/global/entity/SiteVisit.java`

역할:

- 공개 프론트엔드 방문 기록을 저장한다.
- 대시보드의 총 방문 수, 일자별 방문 수, 시간대별 방문 수 집계에 사용한다.

## StringListJsonConverter

파일:

- `src/main/java/com/example/myaicv/myinfo/persistence/StringListJsonConverter.java`

역할:

- `List<String>`을 DB `TEXT` 컬럼에 JSON 문자열로 저장한다.
- DB 값을 다시 `List<String>`으로 역직렬화한다.
