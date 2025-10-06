# Use lightweight Java 17 image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy your backend folder
COPY backend /app/backend

# Build your Spring Boot jar (try mvnw first, else mvn)
RUN cd backend && ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Run the jar
CMD ["java", "-jar", "/app/backend/target/*.jar"]