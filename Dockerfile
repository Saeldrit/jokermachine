FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY src /app/src
COPY pom.xml /app/pom.xml

ARG db_url
ARG db_username
ARG db_password
ARG db_driver

ENV db_url=${db_url}
ENV db_username=${db_username}
ENV db_password=${db_password}
ENV dv_driver=${dv_driver}

RUN mvn clean compile package
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]