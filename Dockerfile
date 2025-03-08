 FROM maven:3.8.5-openjdk-17 AS builder
 WORKDIR /app
 COPY pom.xml .
 COPY src ./src
 RUN mvn clean package -DskipTests

 FROM openjdk:17-jdk-slim
 WORKDIR /app
 COPY --from=builder /app/target/swift-codes-api-1.0-SNAPSHOT.jar app.jar
 EXPOSE 8080
 ENTRYPOINT ["java", "-jar", "app.jar"]

