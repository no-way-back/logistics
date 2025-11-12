# Build stage
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Gradle 의존성 캐싱
COPY build.gradle settings.gradle ./
COPY common/ common/

# 소스 코드 복사
COPY company/ company/

# 빌드
RUN gradle clean :company:bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine

RUN apk update && \
    apk add --no-cache curl

WORKDIR /app

COPY --from=build /app/company/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 18082

ENTRYPOINT ["java", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-jar", \
    "app.jar"]