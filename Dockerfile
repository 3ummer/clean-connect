# Use official OpenJDK runtime as parent image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy gradle wrapper and build files
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build --no-daemon

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "build/libs/cleaning2-1.0-SNAPSHOT.jar"]