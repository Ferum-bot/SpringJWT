FROM openjdk:16
    ARG JAR_FILE=target/*.jar
    COPY