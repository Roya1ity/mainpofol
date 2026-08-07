# JWT 관리자 인증

## 보안 설정

파일:

- `src/main/java/com/example/mainpofol/config/SecurityConfig.java`

규칙:

- `/api/admin/login`: 공개
- `/api/admin/**`: `ROLE_ADMIN` 필요
- `/api/ask`: 공개
- `/api/myinfo/**`: 공개
- 기타: 공개

세션 정책:

- `STATELESS`
- HTTP Basic 비활성화
- Form Login 비활성화

## 로그인

파일:

- `AdminAuthController`
- `AdminAuthService`

관리자 비밀번호는 `.env`의 `ADMIN_PASSWORD`와 비교한다.

## 토큰

파일:

- `JwtTokenProvider`
- `JwtAuthenticationFilter`

토큰 정보:

- 알고리즘: HMAC-SHA256
- subject: `admin`
- role: `ADMIN`
- expiration: `JWT_EXPIRATION_SECONDS`

## 운영 주의

- `JWT_SECRET`은 충분히 긴 랜덤 문자열을 사용한다.
- `ADMIN_PASSWORD`는 단순 문자열 비교 방식이다.
- 운영 보안 수준을 올리려면 비밀번호 해시, 로그인 실패 제한, 감사 로그를 추가한다.

