FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/TutorFlow-0.0.1-SNAPSHOT.jar .

COPY tutorflow-448111-81b00dc0fefa.json /app/credentials.json
ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/TutorFlow-0.0.1-SNAPSHOT.jar"]
