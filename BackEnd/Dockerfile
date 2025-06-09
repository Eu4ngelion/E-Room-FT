FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/ERoomAPI-0.0.1-SNAPSHOT.jar app.jar

# Set default environment variables
ENV MYSQL_HOST=localhost
ENV MYSQL_PORT=3306
ENV MYSQL_DATABASE=eRoomDB
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=
ENV PORT=8080

EXPOSE $PORT
CMD ["java", "-jar", "app.jar"]