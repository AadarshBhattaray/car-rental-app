# Use JDK 24 from Eclipse Temurin project
FROM eclipse-temurin:24-jdk

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Build the project using Maven (skip tests for faster build)
RUN mvn clean install -DskipTests

# Run the generated JAR file
CMD ["java", "-jar", "target/car-rental-app-1.0.0.jar"]
