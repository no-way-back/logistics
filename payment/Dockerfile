# ---- Build stage ----
FROM gradle:8.9-jdk17 AS build
WORKDIR /app

# Gradle 세팅 파일들 먼저 복사
COPY settings.gradle ./
COPY build.gradle ./
COPY gradlew ./
COPY gradle ./gradle
COPY common/build.gradle common/build.gradle
COPY payment/build.gradle payment/build.gradle

# 의존성 프리-워밍
RUN chmod +x ./gradlew && ./gradlew :payment:dependencies --no-daemon || true

# 실제 소스 복사 후 빌드
COPY common/ common/
COPY payment/ payment/
RUN ./gradlew :payment:bootJar -x test --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre-jammy AS runtime

RUN apt-get update \
 && apt-get install -y --no-install-recommends curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /app/payment/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -sf http://localhost:18087/actuator/health || exit 1

EXPOSE 18087


ENTRYPOINT ["java","-Xms512m","-Xmx1024m","-jar","app.jar"]
