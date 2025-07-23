# Guía de Instalación y Troubleshooting - ML Core Platform

## Scripts de Instalación

### Instalación Tradicional (Java + Maven)

#### macOS/Linux

El script `install-dependencies-mac.sh` automatiza la instalación de:
- Homebrew (si no está instalado)
- Java 17 (OpenJDK)
- Apache Maven
- Dependencias del proyecto

**Uso:**
```bash
chmod +x install-dependencies-mac.sh
./install-dependencies-mac.sh
```

#### Windows

El script `install-dependencies-windows.bat` automatiza la instalación de:
- Java 17 (Oracle JDK)
- Apache Maven
- Dependencias del proyecto

**Uso:**
```cmd
# Ejecutar como administrador
install-dependencies-windows.bat
```

### Instalación con Docker (Recomendado)

#### macOS/Linux

El script `install-docker-mac.sh` automatiza la instalación de:
- Docker Desktop
- Docker Compose
- Configuración completa del entorno

**Uso:**
```bash
chmod +x install-docker-mac.sh
./install-docker-mac.sh
```

#### Windows

El script `install-docker-windows.bat` automatiza la instalación de:
- Docker Desktop
- Docker Compose
- Configuración completa del entorno

**Uso:**
```cmd
# Ejecutar como administrador
install-docker-windows.bat
```

### Ejecución con Docker

Una vez instalado Docker, puedes ejecutar la aplicación con:

```bash
# Construir y ejecutar todos los servicios
docker-compose up -d

# Ver logs
docker-compose logs -f ml-core-platform

# Detener servicios
docker-compose down

# Reconstruir imagen después de cambios
docker-compose up --build -d
```

**Servicios incluidos:**
- **ml-core-platform**: Aplicación Spring Boot (Puerto 8080)
- **ml-database**: PostgreSQL 15 (Puerto 5432)
- **ml-redis**: Redis para cache (Puerto 6379)
- **ml-nginx**: Reverse proxy (Puerto 80/443)

### Inicio Rápido con Docker 🚀

Para una experiencia de inicio más fluida, use los scripts de inicio rápido:

#### macOS/Linux
```bash
chmod +x quick-start-docker.sh
./quick-start-docker.sh
```

#### Windows
```cmd
quick-start-docker.bat
```

Estos scripts automáticamente:
- ✅ Verifican que Docker esté instalado y ejecutándose
- 🧹 Limpian contenedores previos
- 🔨 Construyen y ejecutan todos los servicios
- ⏱️ Esperan a que todos los servicios estén listos
- 🌐 Abren automáticamente Swagger UI en el navegador
- 📊 Muestran comandos útiles para el desarrollo

## Problemas Comunes y Soluciones

### Instalación Tradicional (Java + Maven)

#### 1. Java no encontrado después de la instalación

**Síntoma:** `java: command not found` o `'java' is not recognized`

**Solución macOS:**
```bash
# Verificar instalaciones de Java
/usr/libexec/java_home -V

# Configurar JAVA_HOME manualmente
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```

**Solución Windows:**
1. Buscar Java en `C:\Program Files\Java\jdk-17*`
2. Configurar variables de entorno:
   - `JAVA_HOME` = ruta completa al JDK
   - Agregar `%JAVA_HOME%\bin` al `PATH`

#### 2. Maven no encontrado

**Síntoma:** `mvn: command not found` o `'mvn' is not recognized`

**Solución macOS:**
```bash
# Reinstalar con Homebrew
brew install maven

# Verificar instalación
which mvn
mvn --version
```

**Solución Windows:**
1. Verificar que Maven esté en `C:\Program Files\Apache\apache-maven-*`
2. Configurar variables:
   - `MAVEN_HOME` = ruta a Maven
   - Agregar `%MAVEN_HOME%\bin` al `PATH`

#### 3. Permisos en macOS

**Síntoma:** `Permission denied` al ejecutar scripts

**Solución:**
```bash
# Dar permisos de ejecución
chmod +x install-dependencies-mac.sh

# Si persiste el problema, verificar quarantine
xattr -d com.apple.quarantine install-dependencies-mac.sh
```

#### 4. Error de certificados SSL

**Síntoma:** Error al descargar dependencias con Maven

**Solución:**
```bash
# Actualizar certificados (macOS)
brew upgrade ca-certificates

# Limpiar cache de Maven
mvn dependency:purge-local-repository
```

#### 5. Versión incorrecta de Java

**Síntoma:** El proyecto requiere Java 17 pero está usando otra versión

**Solución:**
```bash
# Ver todas las versiones instaladas (macOS)
/usr/libexec/java_home -V

# Cambiar a Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Windows: cambiar JAVA_HOME en variables de entorno del sistema
```

#### 6. Puerto 8080 ocupado

**Síntoma:** `Port 8080 was already in use`

**Solución:**
```bash
# Encontrar proceso usando el puerto
lsof -ti:8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Matar el proceso
kill -9 <PID>  # macOS/Linux
taskkill /PID <PID> /F  # Windows

# O cambiar puerto en application.properties
echo "server.port=8081" >> src/main/resources/application.properties
```

### Instalación con Docker

#### 7. Docker Desktop no inicia

**Síntoma:** `Cannot connect to the Docker daemon`

**Solución macOS:**
```bash
# Verificar si Docker Desktop está ejecutándose
open /Applications/Docker.app

# Reiniciar Docker Desktop
killall Docker && open /Applications/Docker.app
```

**Solución Windows:**
```cmd
# Reiniciar Docker Desktop
taskkill /f /im "Docker Desktop.exe"
start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
```

#### 8. Error de permisos con Docker

**Síntoma:** `permission denied while trying to connect to the Docker daemon socket`

**Solución Linux:**
```bash
# Agregar usuario al grupo docker
sudo usermod -aG docker $USER

# Aplicar cambios (requiere logout/login)
newgrp docker
```

#### 9. Falta de memoria para contenedores

**Síntoma:** Contenedores se reinician constantemente o fallan

**Solución:**
```bash
# Verificar uso de memoria
docker stats

# Ajustar memoria en docker-compose.yml
services:
  ml-core-platform:
    deploy:
      resources:
        limits:
          memory: 1G
        reservations:
          memory: 512M
```

#### 10. Problemas con WSL2 en Windows

**Síntoma:** Docker Desktop requiere WSL2 pero no está disponible

**Solución:**
```cmd
# Habilitar WSL2
dism.exe /online /enable-feature /featurename:Microsoft-Windows-Subsystem-Linux /all /norestart
dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart

# Reiniciar Windows
# Descargar e instalar el kernel de WSL2 desde Microsoft
```

#### 11. Problemas de red entre contenedores

**Síntoma:** Aplicación no puede conectarse a la base de datos

**Solución:**
```bash
# Verificar red de Docker
docker network ls
docker network inspect ml-core-platform_ml-network

# Reiniciar servicios con red limpia
docker-compose down
docker system prune -f
docker-compose up -d
```

#### 12. Volúmenes persistentes corruptos

**Síntoma:** Base de datos no inicia o datos se pierden

**Solución:**
```bash
# Limpiar volúmenes
docker-compose down -v
docker volume prune -f

# Recrear con datos limpios
docker-compose up -d
```

## Verificación Post-Instalación

### Instalación Tradicional (Java + Maven)

Ejecute estos comandos para verificar que todo esté funcionando:

```bash
# 1. Verificar Java
java -version
# Debe mostrar: openjdk version "17.x.x"

# 2. Verificar Maven
mvn --version
# Debe mostrar: Apache Maven 3.x.x

# 3. Verificar JAVA_HOME
echo $JAVA_HOME  # macOS/Linux
echo %JAVA_HOME%  # Windows

# 4. Compilar proyecto
mvn clean compile

# 5. Ejecutar tests
mvn test

# 6. Iniciar aplicación
mvn spring-boot:run
```

### Instalación con Docker

Ejecute estos comandos para verificar Docker:

```bash
# 1. Verificar Docker
docker --version
# Debe mostrar: Docker version 20.x.x

# 2. Verificar Docker Compose
docker compose version
# Debe mostrar: Docker Compose version v2.x.x

# 3. Verificar que Docker esté ejecutándose
docker system info

# 4. Test básico de Docker
docker run --rm hello-world

# 5. Construir y ejecutar aplicación
docker-compose up -d

# 6. Verificar servicios ejecutándose
docker-compose ps

# 7. Ver logs de la aplicación
docker-compose logs -f ml-core-platform

# 8. Verificar salud de los servicios
docker-compose exec ml-core-platform curl -f http://localhost:8080/actuator/health
```

### Acceso a la Aplicación

Una vez que la aplicación esté ejecutándose:

**Instalación Tradicional:**
- Aplicación: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health

**Instalación con Docker:**
- Aplicación: http://localhost:8080 (o http://localhost si usa nginx)
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health Check: http://localhost:8080/actuator/health
- Base de datos PostgreSQL: localhost:5432
- Redis: localhost:6379

## Desinstalación

### Instalación Tradicional

#### macOS
```bash
# Desinstalar con Homebrew
brew uninstall openjdk@17
brew uninstall maven

# Limpiar configuración del shell
# Editar ~/.zshrc y remover líneas de JAVA_HOME
```

#### Windows
1. Desinstalar Java desde "Programas y características"
2. Eliminar carpeta de Maven
3. Limpiar variables de entorno `JAVA_HOME` y `MAVEN_HOME`
4. Remover rutas del `PATH`

### Instalación con Docker

#### Limpiar completamente Docker

```bash
# Detener todos los contenedores
docker-compose down -v

# Remover imágenes del proyecto
docker rmi ml-core-platform_ml-core-platform

# Limpiar sistema completo (CUIDADO: borra todo)
docker system prune -a --volumes

# Desinstalar Docker Desktop
# macOS: Drag Docker.app to Trash
# Windows: Uninstall from Programs and Features
```

#### Limpiar solo el proyecto

```bash
# Detener servicios
docker-compose down -v

# Remover volúmenes específicos
docker volume rm ml-core-platform_postgres-data
docker volume rm ml-core-platform_redis-data
docker volume rm ml-core-platform_app-logs

# Remover red personalizada
docker network rm ml-core-platform_ml-network
```

## Comandos Útiles de Docker

### Desarrollo diario

```bash
# Iniciar servicios en background
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f

# Reiniciar un servicio específico
docker-compose restart ml-core-platform

# Acceder al contenedor de la aplicación
docker-compose exec ml-core-platform bash

# Acceder a la base de datos
docker-compose exec ml-database psql -U mluser -d mlcoreplatform

# Monitorear recursos
docker stats

# Ver servicios ejecutándose
docker-compose ps
```

### Debugging y mantenimiento

```bash
# Reconstruir imagen después de cambios
docker-compose build --no-cache ml-core-platform

# Ejecutar solo un servicio específico
docker-compose up ml-core-platform

# Ver configuración de un servicio
docker-compose config

# Backup de base de datos
docker-compose exec ml-database pg_dump -U mluser mlcoreplatform > backup.sql

# Restaurar base de datos
docker-compose exec -T ml-database psql -U mluser -d mlcoreplatform < backup.sql
```

## Contacto y Soporte

Si continúan los problemas después de seguir esta guía:

1. Verificar los logs del script de instalación
2. Revisar la documentación oficial de Spring Boot
3. Consultar con el equipo de desarrollo

---

**Nota:** Esta guía asume sistemas operativos actualizados. Para versiones muy antiguas de macOS o Windows, pueden requerirse pasos adicionales.
