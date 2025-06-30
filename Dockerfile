# Use Maven + JDK 21 image for building
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the Spring Boot project
RUN mvn clean install -DskipTests

# Use a lightweight JDK 21 image to run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY --from=build /app/target/car-rental-app-1.0.0.jar app.jar

CMD ["java", "-jar", "app.jar"]
