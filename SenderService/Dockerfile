FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN apk add --no-cache maven

COPY target/*.jar senderService.jar
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "senderService.jar"]