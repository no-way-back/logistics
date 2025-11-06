# Build stage
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY common/ common/
COPY gateway/ gateway/

RUN gradle clean :gateway:bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl

WORKDIR /app
COPY --from=build /app/gateway/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]

