# Use Eclipse Temurin JDK 17 as base image
FROM --platform=linux/amd64 eclipse-temurin:17-jdk-alpine

# Create a directory for the H2 database files
RUN mkdir -p ~/data/h2

# Set working directory
WORKDIR /app

# Copy the application JAR file
COPY target/employee-0.0.1-SNAPSHOT.jar employee.jar

# Expose port for Spring Boot application and H2 server access
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "employee.jar"]
