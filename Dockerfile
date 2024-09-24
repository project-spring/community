# Docker file for building the image

# jdk 21
FROM openjdk:21

# Copy the jar file to the container
ARG JAR_FILE=build/libs/*.jar
# jar file Copy
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Dspring.profiles.active=docker", "-jar","app.jar"]