FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

ENV TZ=Asia/Seoul

RUN useradd --system --create-home --uid 1001 spring

COPY --from=build /workspace/build/libs/*.jar app.jar

USER spring

EXPOSE 8095

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
