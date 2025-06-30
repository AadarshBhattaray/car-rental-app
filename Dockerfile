# Use Maven + JDK 24 image from official Maven repo
FROM maven:3.9.6-eclipse-temurin-24 AS build

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the project
RUN mvn clean install -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:24-jdk

WORKDIR /app
COPY --from=build /app/target/car-rental-app-1.0.0.jar app.jar

# Start the app
CMD ["java", "-jar", "app.jar"]
