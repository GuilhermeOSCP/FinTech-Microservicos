FROM maven:3.8.5-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
ARG RABBITMQ_SERVER=fintech-microservicos-rabbitmq
ARG EUREKA_SERVER=fintech-microservicos-eureka
EXPOSE 8761
ENTRYPOINT java -jar app.jar