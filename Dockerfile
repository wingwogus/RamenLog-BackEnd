FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/ramenlog-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yml src/main/resources/application.yml

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]