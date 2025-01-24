# Build Stage
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run Stage
FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar
COPY .env .env  # Optional: Include .env file in the image
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
