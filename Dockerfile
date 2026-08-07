FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace

COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx512m -Dorg.gradle.daemon=false -Dorg.gradle.workers.max=1"

RUN chmod +x ./gradlew && ./gradlew bootJar -x test --no-daemon --max-workers=1

FROM eclipse-temurin:21-jre

WORKDIR /app

ENV TZ=Asia/Seoul
ENV MYINFO_DOCUMENT_DIRECTORY=/app/myinfo

RUN useradd --system --create-home --uid 1001 spring

COPY --from=build /workspace/build/libs/*.jar app.jar
COPY --chown=1001:1001 src/main/resources/static/myinfo /app/myinfo

USER spring

EXPOSE 8095

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
