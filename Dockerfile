# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
# Default Maven profile (local): embeds Tomcat; do not use -Pk3s (bakes localhost config into the JAR).
RUN mvn -B -DskipTests clean package && \
    cp /app/target/mesapi-*.jar /app/app.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from=build /app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
