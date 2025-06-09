FROM eclipse-temurin:17-jre

# Create uploads directory
RUN mkdir -p /app/uploads && chmod -R 775 /app/uploads

# Copy the JAR file
COPY target/*.jar /app/app.jar

# Expose port
EXPOSE 8080

# Set production mode
ENV VAADIN_PRODUCTION_MODE=true
ENV SPRING_PROFILES_ACTIVE=production

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]