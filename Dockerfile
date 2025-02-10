FROM openjdk:17-jdk-slim as build
WORKDIR /app
RUN apt-get update && apt-get install -y postgresql-client
COPY target/contas-pagar-api-0.0.1-SNAPSHOT.jar /app/contas-pagar-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "contas-pagar-api.jar"]
