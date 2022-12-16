FROM openjdk:17-slim-buster
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT [ "java", "-jar", "./app.jar" ]  








