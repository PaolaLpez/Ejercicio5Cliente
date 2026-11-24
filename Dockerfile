# Multi-stage build para Frontend Ejercicio5Cliente
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime ligero
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
LABEL maintainer="Paola Jaqueline López Mata <pjlm@utng.edu.mx>"
LABEL description="Frontend Web Thymeleaf - Administrador de Proyectos"
COPY --from=build /app/target/Ejercicio5Cliente-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_PORT=8080
ENV GESTOR_BACKEND_URL=http://backend:8081
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
