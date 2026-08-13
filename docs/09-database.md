# DB 설계

현재 DB는 MySQL 기준으로 설정되어 있다.

## 연결

로컬 기본 URL:

```text
jdbc:mysql://localhost:3306/myaicv
```

Docker Compose 기본 DB:

```text
MYSQL_DATABASE=myaicv
```

사용 설정:

- `DB_USERNAME`
- `DB_PASSWORD`

## app_user

엔티티:

- `AppUser`

컬럼:

- `id`: BIGINT PK auto increment
- `provider`: varchar(20), `GOOGLE` 또는 `KAKAO`
- `provider_user_id`: varchar(100)
- `email`: varchar(255)
- `name`: varchar(100)
- `role`: varchar(20), `SEEKER`, `EMPLOYER`, `ADMIN`
- `public_key`: varchar(40)
- `created_at`: datetime
- `updated_at`: datetime

제약:

- `(provider, provider_user_id)` unique
- `public_key` unique

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

- 공개 프론트엔드 방문 로그 저장
- 총 방문 수, 최근 일자별 방문 수, 오늘 시간대별 방문 수 집계

## JPA 설정

현재:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

운영 환경에서는 Flyway 또는 Liquibase 도입을 검토한다.
