# 도메인 모델

## MyInfoAskHistory

파일:

- `src/main/java/com/example/mainpofol/global/entity/MyInfoAskHistory.java`

역할:

- 사용자 질문과 AI 답변을 저장한다.
- 선택된 Markdown 문서 목록을 JSON 문자열로 저장한다.

필드:

- `id`: PK
- `question`: 질문 내용
- `answer`: 답변 내용
- `selectedDocuments`: 선택된 문서명 목록
- `createdAt`: 생성 시간

연관:

- `MyInfoRecommendedQuestion`과 1:1로 연결될 수 있다.

## MyInfoRecommendedQuestion

파일:

- `src/main/java/com/example/mainpofol/global/entity/MyInfoRecommendedQuestion.java`

역할:

- 공개 화면에서 노출할 추천 질문을 저장한다.
- 추천 질문은 하나의 질문 히스토리와 1:1로 연결된다.
- 추천 질문 클릭 시 연결된 히스토리의 저장 답변을 반환한다.

필드:

- `id`: PK
- `question`: 추천 질문 문구
- `askHistory`: 연결된 질문 히스토리
- `enabled`: 공개 조회 가능 여부
- `displayOrder`: 노출 정렬값
- `createdAt`: 생성 시간

## StringListJsonConverter

파일:

- `src/main/java/com/example/mainpofol/myinfo/persistence/StringListJsonConverter.java`

역할:

- `List<String>`을 DB의 `TEXT` 컬럼에 JSON 문자열로 저장한다.
- DB 값을 다시 `List<String>`으로 역직렬화한다.

