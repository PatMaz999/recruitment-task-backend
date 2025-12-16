FROM openjdk:25-ea-25-jdk-slim AS build
WORKDIR /app
COPY src src

COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY settings.gradle .

RUN chmod +x ./gradlew
RUN ./gradlew clean build

FROM openjdk:25-ea-25-jdk-slim
VOLUME /tmp

COPY --from=build app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080