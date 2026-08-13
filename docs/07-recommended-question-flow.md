# 추천 질문 흐름

추천 질문은 `MyInfoRecommendedQuestion`에 저장되고 `MyInfoAskHistory`와 1:1로 연결된다.

## 구직자 등록 방식

### 질문 이력 ID로 등록

```text
POST /api/seeker/myinfo-ask-histories/{id}/recommended-question
```

특징:

- 이미 저장된 질문 이력을 추천 질문으로 승격한다.
- body를 생략하면 질문 이력의 `question`을 그대로 사용한다.
- 같은 질문 이력이 이미 추천 질문에 연결되어 있으면 409를 반환한다.

### 추천 질문 직접 CRUD

```text
/api/seeker/recommended-questions
```

특징:

- `question`, `askHistoryId`, `enabled`, `displayOrder`를 직접 관리한다.
- `askHistoryId` 1:1 제약을 검사한다.

## 공개 조회 방식

### 목록 조회

```text
GET /api/myinfo/recommended-questions
```

특징:

- 공개 프론트엔드의 추천 질문 카드 목록에 사용한다.
- `enabled=true`인 항목만 반환한다.
- `displayOrder`, `id` 순서로 정렬한다.

### 저장 답변 조회

```text
GET /api/myinfo/recommended-questions/{id}/answer
```

특징:

- 추천 질문 ID로 연결된 질문 이력을 찾는다.
- OpenAI를 호출하지 않는다.
- 저장된 답변을 즉시 반환한다.
- `enabled=true`인 추천 질문만 공개 조회된다.

## 데이터 관계

```text
myinfo_recommended_question.ask_history_id
  -> myinfo_ask_history.id
```

`ask_history_id`는 unique 제약을 가진다.
