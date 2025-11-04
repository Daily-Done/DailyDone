# ----------- Base Image -----------
FROM eclipse-temurin:21-jdk

# ----------- Install Maven -----------
RUN apt-get update && apt-get install -y maven

# ----------- Set Working Directory -----------
WORKDIR /app

# ----------- Copy Backend Code -----------
COPY backend /app/backend

# ----------- Build Spring Boot JAR -----------
RUN cd backend && mvn clean package -DskipTests

# ----------- Run the Generated JAR -----------
CMD ["sh", "-c", "java -jar $(find /app/backend/target -type f -name '*.jar' | head -n 1)"]