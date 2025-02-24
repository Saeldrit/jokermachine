FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY src /app/src
COPY pom.xml /app/pom.xml

ARG db_url
ARG db_user
ARG db_password
ARG db_driver

RUN mvn clean compile package -Ddb_url=${db_url} -Ddb_user=${db_user} -Ddb_password=${db_password} -Ddb_driver=${db_driver}

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]