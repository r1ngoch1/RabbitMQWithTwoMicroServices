FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY target/*.jar senderService.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "senderService.jar"]