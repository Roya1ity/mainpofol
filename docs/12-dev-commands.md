# 개발 명령어

## 컴파일

```powershell
.\gradlew.bat compileJava
```

## 테스트

```powershell
.\gradlew.bat test
```

코드 변경 검증은 최소 `compileJava`로 확인한다. DB나 외부 API가 필요한 동작 검증은 환경 변수와 MySQL 준비 상태를 먼저 확인한다.

## 로컬 실행

```powershell
.\gradlew.bat bootRun
```

기본 포트:

```text
8095
```

## Docker Compose 실행

```powershell
docker compose up -d --build
```

상태 확인:

```powershell
docker compose ps
```

로그 확인:

```powershell
docker compose logs -f backend
```

중지:

```powershell
docker compose down
```

## MySQL 단독 실행 예

```powershell
docker run -d `
  --name my-ai-cv-db `
  -e MYSQL_ROOT_PASSWORD=myaicv_root_password `
  -e MYSQL_DATABASE=myaicv `
  -e MYSQL_USER=myaicv `
  -e MYSQL_PASSWORD=myaicv_password `
  -p 3306:3306 `
  mysql:8.4
```

접속 확인:

```powershell
docker exec my-ai-cv-db mysqladmin ping -h localhost -u myaicv -pmyaicv_password
```
