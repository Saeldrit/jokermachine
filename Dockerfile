FROM maven:3.8.6-openjdk-17 AS build

WORKDIR /app

COPY src /app/src
COPY pom.xml /app/pom.xml

RUN mvn clean compile package

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/jokermachine.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]