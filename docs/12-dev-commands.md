# 개발 명령어

## 컴파일

```powershell
.\gradlew.bat compileJava
```

## 테스트

```powershell
.\gradlew.bat test
```

현재 환경에서는 Gradle test worker 관련 문제가 발생할 수 있다. 코드 변경 검증은 우선 `compileJava`로 확인했다.

## 앱 실행

```powershell
.\gradlew.bat bootRun
```

## MySQL 컨테이너

현재 설정에 맞는 컨테이너 예:

```powershell
docker run -d `
  --name mainpofol-mysql `
  -e MYSQL_ROOT_PASSWORD=1234 `
  -e MYSQL_DATABASE=mainpofol `
  -e MYSQL_USER=mainpofol `
  -e MYSQL_PASSWORD=1234 `
  -p 3306:3306 `
  mysql:8.0
```

상태 확인:

```powershell
docker ps --filter "name=mainpofol-mysql"
```

접속 확인:

```powershell
docker exec mainpofol-mysql mysqladmin ping -h localhost -u mainpofol -p1234
```

