FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /home/gradle/project

COPY build.gradle.kts settings.gradle.kts gradle.properties* ./
COPY gradle gradle
RUN gradle --no-daemon -q clean build -x test || true

COPY src src
RUN gradle --no-daemon -q clean bootJar -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN useradd --system --create-home --uid 10001 appuser
USER appuser

COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
