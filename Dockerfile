FROM openjdk:17-jdk-slim
WORKDIR /app

COPY /target/searchbot-0.0.1-SNAPSHOT.jar application.jar
COPY /src/main/resources/application-docker.properties /app/application-docker.properties

ENTRYPOINT ["java", "-jar", "application.jar"]