# ==============================================================================
# PROYECTO INTEGRADOR DEVOPS - FRONTEND (Ejercicio5Cliente)
# CRITERIO 1 & 2: DOCKER MULTI-STAGE BUILD (Reducción de tamaño y Seguridad)
# ==============================================================================

# ETAPA 1: Compilación y empaquetado (Build Stage con Maven y JDK 21)
# Utiliza la imagen oficial de Maven para descargar dependencias y compilar el código fuente.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Optimización de caché: copia pom.xml y descarga dependencias antes del código
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y empaqueta el artefacto ejecutable (.jar)
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Entorno de ejecución ligero (Runtime Stage con JRE 21)
# Reduce la imagen de ~780 MB a solo ~195 MB y elimina Maven/compiladores por seguridad.
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia exclusivamente el binario compilado desde la etapa de 'build'
COPY --from=build /app/target/Ejercicio5Cliente-0.0.1-SNAPSHOT.jar app.jar

# Punto de entrada para Render Cloud: asigna dinámicamente el puerto $PORT (default 8080)
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]
