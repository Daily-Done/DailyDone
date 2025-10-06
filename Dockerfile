# Use lightweight Java 17 image
FROM openjdk:17-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy backend project into container
COPY backend /app/backend

# Build your Spring Boot JAR
RUN cd backend && mvn clean package -DskipTests

# Run the generated JAR
CMD ["java", "-jar", "/app/backend/target/*.jar"]