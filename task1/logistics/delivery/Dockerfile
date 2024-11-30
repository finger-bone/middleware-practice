FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/delivery.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8082

CMD ["java", "-jar", "app.jar"]