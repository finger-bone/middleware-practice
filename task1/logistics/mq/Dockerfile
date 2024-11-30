FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/mq.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8084

CMD ["java", "-jar", "app.jar"]