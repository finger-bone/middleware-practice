FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/center.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]