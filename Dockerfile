# 빌드 단계
FROM gradle:7.6-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# 메모리 절약을 위해 데몬 없이, 최소한의 메모리로 빌드
RUN gradle build --no-daemon -x test -Dorg.gradle.jvmargs="-Xmx256m"

# 실행 단계
FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
# 실행 시에도 메모리 제한 설정
ENTRYPOINT ["java", "-Xmx384m", "-Xms256m", "-jar", "/app.jar"]