# OAuth/JWT 보안

## Spring Security 설정

파일:

- `src/main/java/com/example/myaicv/config/SecurityConfig.java`

공개 경로:

- `/`, `/index.html`, `/app.css`, `/app.js`, `/favicon.*`
- `/api/auth/oauth2/**`, `/oauth2/**`, `/login/oauth2/**`
- `/api/auth/logout`
- `/api/ask/**`
- `/api/myinfo/**`
- `/api/resumes/**`, `/resume`, `/resume/**`

권한 경로:

- `/api/seeker/**`: `ROLE_SEEKER` 또는 `ROLE_ADMIN`
- `/api/employer/**`: `ROLE_EMPLOYER` 또는 `ROLE_ADMIN`
- `/api/admin/**`: `ROLE_ADMIN`

세션 정책:

- `STATELESS`
- CSRF disabled
- HTTP Basic disabled
- OAuth2 Login success handler 사용

## OAuth 로그인

시작 API:

```text
GET /api/auth/oauth2/{provider}?role=SEEKER
```

지원 provider:

- `google`
- `kakao`

역할:

- 요청 role 기본값은 `SEEKER`
- `ADMIN` role은 직접 요청할 수 없다.
- 로그인 이메일이 `ADMIN_EMAILS`에 포함되면 `ADMIN`으로 승격된다.

OAuth 성공 처리:

```text
OAuthLoginSuccessHandler
  -> OAuthUserProvisionService.saveOrUpdate
  -> JwtTokenProvider.createToken
  -> AuthCookieService.addAccessToken
  -> redirect /?login=success
```

## Auth API

```text
GET /api/auth/me
POST /api/auth/logout
```

- `/me`는 현재 JWT principal로 `AppUser`를 조회한다.
- `/logout`은 `ACCESS_TOKEN`, `LOGIN_ROLE` 쿠키를 만료시킨다.

## JWT

파일:

- `JwtTokenProvider`
- `JwtAuthenticationFilter`

토큰:

- 알고리즘: HMAC-SHA256
- subject: `AppUser.id`
- claims: `email`, `name`, `role`, `publicKey`, `iat`, `exp`
- 만료: `JWT_EXPIRATION_SECONDS`

토큰 위치:

- `Authorization: Bearer {token}`
- `ACCESS_TOKEN` 쿠키

## 운영 주의

- `JWT_SECRET`은 충분히 긴 랜덤 문자열을 사용한다.
- 현재 쿠키 `secure(false)`는 로컬 개발 기준이다.
- `ADMIN_EMAILS`는 쉼표로 구분하며 소문자 비교로 처리된다.
