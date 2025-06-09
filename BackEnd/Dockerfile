FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY ../pom.xml /app/pom.xml
COPY BackEnd/pom.xml /app/BackEnd/pom.xml
COPY BackEnd/src /app/BackEnd/src
RUN cd BackEnd && mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/BackEnd/target/ERoomAPI-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]