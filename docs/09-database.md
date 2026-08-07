# DB 설계

현재 DB는 MySQL을 기준으로 설정되어 있다.

## 연결

```text
jdbc:mysql://localhost:3306/mainpofol
```

사용 설정:

- `DB_USERNAME`
- `DB_PASSWORD`

## myinfo_ask_history

엔티티:

- `MyInfoAskHistory`

컬럼:

- `id`: BIGINT PK auto increment
- `question`: TEXT not null
- `answer`: TEXT not null
- `selected_documents`: TEXT not null
- `created_at`: datetime

`selectedDocuments`는 JSON 문자열로 저장된다.

## myinfo_recommended_question

엔티티:

- `MyInfoRecommendedQuestion`

컬럼:

- `id`: BIGINT PK auto increment
- `question`: TEXT not null
- `ask_history_id`: BIGINT not null unique
- `enabled`: boolean not null
- `display_order`: integer not null
- `created_at`: datetime not null

관계:

- `ask_history_id` -> `myinfo_ask_history.id`
- 1:1 연결

## site_visit

엔티티:

- `SiteVisit`

컬럼:

- `id`: BIGINT PK auto increment
- `created_at`: datetime not null

용도:

- 공개 프론트엔드 방문 수 집계에 사용한다.
- 현재는 페이지 로드마다 한 건씩 저장한다.
- 관리자 대시보드에서 총 방문자수, 최근 14일 일자별 방문자수, 오늘 시간대별 방문자수를 집계한다.

## JPA 설정

현재:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

운영 환경에서는 Flyway 또는 Liquibase 도입을 검토한다.
