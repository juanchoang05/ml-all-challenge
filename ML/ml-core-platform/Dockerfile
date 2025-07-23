# Dockerfile para ML Core Platform
FROM openjdk:17-jdk-slim

# Metadata
LABEL maintainer="MercadoLibre Core Platform Team"
LABEL description="ML Core Platform - Spring Boot Application"
LABEL version="1.0.0"

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=production
ENV SERVER_PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Crear usuario no-root para seguridad
RUN groupadd -r mlapp && useradd -r -g mlapp mlapp

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven para cache de dependencias
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x ./mvnw

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN ./mvnw clean package -DskipTests -B

# Cambiar propietario de archivos al usuario mlapp
RUN chown -R mlapp:mlapp /app

# Cambiar al usuario no-root
USER mlapp

# Exponer puerto
EXPOSE ${SERVER_PORT}

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]
