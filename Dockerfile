# ==========================================
# Production Dockerfile for Employee Management API
# Base Image: Eclipse Temurin JDK 17 Alpine
# ==========================================
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Add metadata
LABEL maintainer="devops@company.com"
LABEL description="Employee Management System REST API"

# Copy Maven packaged JAR artifact to container
ARG JAR_FILE=target/employee-management-1.0.0.jar
COPY ${JAR_FILE} app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Configure container execution command
ENTRYPOINT ["java", "-jar", "app.jar"]
