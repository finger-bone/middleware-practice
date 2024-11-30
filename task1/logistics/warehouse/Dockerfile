FROM openjdk:21-jdk-slim

ARG JAR_FILE=build/libs/warehouse.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]