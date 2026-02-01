# 1. 빌드 스테이지
FROM gradle:7.6-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# 메모리 절약을 위해 데몬 없이 빌드
RUN gradle build --no-daemon -x test -Dorg.gradle.jvmargs="-Xmx256m"

# 2. 실행 스테이지
# 에러가 났던 openjdk 대신, 안정적인 eclipse-temurin 사용
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
# 실행 시 메모리 제한 (Render 무료 티어 최적화)
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-jar", "/app.jar"]