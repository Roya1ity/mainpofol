# 실행 환경과 설정

## 런타임

- Java 21
- Spring Boot 4.1.0
- Gradle Wrapper
- MySQL 8.x
- Spring AI OpenAI starter

## application.yaml

주요 설정 파일은 `src/main/resources/application.yaml`이다.

현재 서버 포트:

```yaml
server:
  port: 8095
```

환경 파일 로딩:

```yaml
spring:
  config:
    import: optional:file:.env[.properties]
```

MySQL 연결:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mainpofol?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ${DB_USERNAME:sa}
    password: ${DB_PASSWORD:}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## .env 키

필요한 값:

```properties
OPENAI_API_KEY=
OPENAI_MODEL=gpt-5-mini
DB_USERNAME=mainpofol
DB_PASSWORD=1234
TELEGRAM_BOT_TOKEN=
TELEGRAM_CHAT_ID=
ADMIN_PASSWORD=
JWT_SECRET=
JWT_EXPIRATION_SECONDS=3600
```

## 환경 설정 주의

- `DB_USERNAME`, `DB_PASSWORD`만 현재 datasource에 사용된다.
- `DB_URL`이 `.env`에 있어도 현재 `application.yaml`에서는 사용하지 않는다.
- 운영에서는 `spring.jpa.hibernate.ddl-auto=update` 대신 migration 도구를 검토한다.
- 실제 토큰과 API 키는 문서에 남기지 않는다.

