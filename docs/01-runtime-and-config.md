# 실행 환경과 설정

## 기술 스택

- Java 21
- Spring Boot 4.1.0
- Gradle Wrapper
- MySQL 8.x
- Spring AI OpenAI starter
- Spring Security OAuth2 Client
- Lombok

## application.yaml

주요 설정 파일은 `src/main/resources/application.yaml`이다.

서버 포트:

```yaml
server:
  port: 8095
```

로컬 환경 파일 로딩:

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
```

MySQL 기본 연결:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myaicv?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USERNAME:myaicv}
    password: ${DB_PASSWORD:}
```

OpenAI:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        options:
          model: ${OPENAI_MODEL:gpt-5-mini}
```

## .env 주요 키

```properties
OPENAI_API_KEY=
OPENAI_MODEL=gpt-5-mini
DB_USERNAME=myaicv
DB_PASSWORD=
TELEGRAM_BOT_TOKEN=
TELEGRAM_CHAT_ID=
ADMIN_EMAILS=
JWT_SECRET=
JWT_EXPIRATION_SECONDS=3600
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
KAKAO_CLIENT_ID=
KAKAO_CLIENT_SECRET=
MYINFO_DOCUMENT_DIRECTORY=src/main/resources/static/myinfo
```

## Docker Compose

`docker-compose.yml`은 다음 서비스를 구성한다.

- `frontend`: `Dockerfile.frontend`와 nginx, 외부 포트 `${APP_PORT:-80}`
- `backend`: Spring Boot 앱, 내부 포트 `8095`
- `db`: MySQL 8.4, 기본 DB `${MYSQL_DATABASE:-myaicv}`

컨테이너 환경에서는 `SPRING_DATASOURCE_URL`이 `db:3306`을 바라보도록 override된다.

## 운영 주의

- `spring.jpa.hibernate.ddl-auto=update`는 개발 편의 설정이다. 운영에서는 migration 도구 도입을 검토한다.
- `JWT_SECRET`, OAuth client secret, OpenAI API key는 절대 문서에 실제 값을 적지 않는다.
- 쿠키의 `secure(false)`는 로컬 개발 기준이다. HTTPS 운영 환경에서는 보안 설정을 재검토한다.
