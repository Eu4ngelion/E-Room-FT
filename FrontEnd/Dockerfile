# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Install Node.js and Maven
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs maven

COPY pom.xml .
COPY src ./src
RUN mvn clean package -Pproduction

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Create uploads directory
RUN mkdir -p /app/uploads && chmod -R 775 /app/uploads

# Copy the JAR from the builder stage
COPY --from=builder /app/target/*.jar /app/app.jar

# Expose port
EXPOSE 8080

# Set production mode
ENV VAADIN_PRODUCTION_MODE=true
ENV SPRING_PROFILES_ACTIVE=production

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]