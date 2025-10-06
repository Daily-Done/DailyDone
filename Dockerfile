# Use lightweight Java 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy your backend folder into the container
COPY backend /app/backend

# Build your Spring Boot JAR (tries ./mvnw first, else mvn)
RUN cd backend && ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Run the generated JAR file
CMD ["java", "-jar", "/app/backend/target/*.jar"]