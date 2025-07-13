FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=builder /app/target/searchbot-0.0.1-SNAPSHOT.jar application.jar
COPY src/main/resources/application-docker.properties /app/application-docker.properties

ENTRYPOINT ["java", "-Dspring.config.location=file:/app/application-docker.properties", "-jar", "application.jar"]