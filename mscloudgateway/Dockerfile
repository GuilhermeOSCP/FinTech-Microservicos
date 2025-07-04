FROM maven:3.8.5-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
ARG EUREKA_SERVER=fintech-microservicos-eureka
ARG KEYCLOAK_SERVER=fintech-microservicos-keycloak
ARG KEYCLOAK_PORT=8080
EXPOSE 8761
ENTRYPOINT java -jar app.jar