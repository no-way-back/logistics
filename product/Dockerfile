### Build Stage ###
FROM gradle:8.9-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY common/ common/

COPY product/ product/

RUN gradle clean :product:bootJar -x test --no-daemon

### Runtime Stage ###
FROM eclipse-temurin:17-jdk-alpine AS runtime

RUN apk update && apk add --no-cache curl

WORKDIR /app

COPY --from=build /app/product/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:18086/actuator/health || exit 1

EXPOSE 18086

ENTRYPOINT ["java", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-jar", \
    "app.jar"]