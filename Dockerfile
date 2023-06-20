FROM  eclipse-temurin:17-jdk-alpine

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV _JAVA_OPTIONS="-XX:MaxRAM=70m"

ENTRYPOINT ["java","-jar","/app.jar"]