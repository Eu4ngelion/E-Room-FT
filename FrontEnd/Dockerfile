# Stage 1: Build the application
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs maven
COPY ../pom.xml ../pom.xml
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Pproduction

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN mkdir -p /app/uploads && chmod -R 775 /app/uploads
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8080
ENV VAADIN_PRODUCTION_MODE=true
ENV SPRING_PROFILES_ACTIVE=production
ENTRYPOINT ["java", "-jar", "/app/app.jar"]