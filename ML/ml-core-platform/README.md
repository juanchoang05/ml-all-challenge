# ML Core Platform

Proyecto base de Spring Boot para MercadoLibre Core Platform.

## Tecnologías incluidas

- **Spring Boot 3.2.1**: Framework principal
- **Lombok**: Para reducir código boilerplate
- **Swagger/OpenAPI**: Documentación de API

## Requisitos del Sistema

- Java 17+
- Maven 3.6+ (o usar el Maven wrapper incluido)

## Instalación y Configuración

### Opción 1: Usar Maven Wrapper (Recomendado)

Este proyecto incluye Maven wrapper, por lo que no necesitas instalar Maven manualmente:

```bash
# En Unix/Linux/macOS
./mvnw clean install

# En Windows
mvnw.cmd clean install
```

### Opción 2: Instalación Manual

#### Para macOS:

1. **Instalar Java 17**
   ```bash
   # Usando Homebrew
   brew install openjdk@17
   
   # Configurar JAVA_HOME
   echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
   echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
   source ~/.zshrc
   ```

2. **Instalar Maven**
   ```bash
   # Usando Homebrew
   brew install maven
   
   # Verificar instalación
   mvn --version
   ```

3. **Instalar dependencias del proyecto**
   ```bash
   mvn clean install
   ```

#### Para Windows:

1. **Instalar Java 17**
   - Descargar desde [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) o [OpenJDK 17](https://adoptium.net/)
   - Ejecutar el instalador
   - Configurar variables de entorno:
     - `JAVA_HOME`: Ruta de instalación de Java (ej: `C:\Program Files\Java\jdk-17`)
     - Agregar `%JAVA_HOME%\bin` al `PATH`

2. **Instalar Maven**
   - Descargar desde [Apache Maven](https://maven.apache.org/download.cgi)
   - Extraer en una carpeta (ej: `C:\Program Files\Apache\maven`)
   - Configurar variables de entorno:
     - `MAVEN_HOME`: Ruta de Maven
     - Agregar `%MAVEN_HOME%\bin` al `PATH`

3. **Instalar dependencias del proyecto**
   ```cmd
   mvn clean install
   ```

## Ejecución del Proyecto

### Con Maven Wrapper (Recomendado)

```bash
# En Unix/Linux/macOS
./mvnw spring-boot:run

# En Windows  
mvnw.cmd spring-boot:run
```

### Con Maven instalado

```bash
mvn spring-boot:run
```

## Scripts de Automatización

Para facilitar la instalación, puedes usar los scripts automatizados:

#### Instalación Tradicional (Java + Maven)

**Para macOS/Linux:**
```bash
chmod +x install-dependencies-mac.sh
./install-dependencies-mac.sh
```

**Para Windows:**
```cmd
install-dependencies-windows.bat
```

#### Instalación con Docker (Recomendado) 🐳

**Para macOS/Linux:**
```bash
chmod +x install-docker-mac.sh
./install-docker-mac.sh
```

**Para Windows:**
```cmd
install-docker-windows.bat
```

## Ejecución

### Ejecución Tradicional

```bash
mvn spring-boot:run
```

### Ejecución con Docker

```bash
# Inicio rápido (recomendado)
./quick-start-docker.sh        # macOS/Linux
quick-start-docker.bat         # Windows

# O manualmente:
docker-compose up -d           # Construir y ejecutar
docker-compose logs -f ml-core-platform  # Ver logs
docker-compose down            # Detener servicios
```

**Servicios incluidos en Docker:**
- 🚀 **Aplicación Spring Boot** (Puerto 8080)
- 🗄️ **PostgreSQL Database** (Puerto 5432) 
- 🔄 **Redis Cache** (Puerto 6379)
- 🌐 **Nginx Reverse Proxy** (Puerto 80)

## Verificación de la Instalación

Para verificar que todo esté correctamente instalado:

```bash
# Verificar versión de Java
java -version

# Verificar versión de Maven (con wrapper)
./mvnw --version              # Unix/Linux/macOS
mvnw.cmd --version           # Windows

# O con Maven instalado
mvn --version

# Verificar que las dependencias se instalaron correctamente
./mvnw dependency:tree       # Con wrapper
mvn dependency:tree          # Con Maven instalado

# Compilar y ejecutar tests
./mvnw clean test           # Con wrapper
mvn clean test              # Con Maven instalado
```

## Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con reporte de cobertura
mvn clean test jacoco:report

# Ver reporte de cobertura
open target/site/jacoco/index.html  # macOS
start target/site/jacoco/index.html # Windows
```

## Documentación de API

Una vez que la aplicación esté ejecutándose, puedes acceder a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## Estructura del Proyecto

```
src/
├── main/
│   └── java/
│       └── com/mercadolibre/mlcoreplatform/
└── test/
    └── java/
        └── com/mercadolibre/mlcoreplatform/
```

## Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

## Solución de Problemas

### Error: JAVA_HOME no configurado
```bash
# macOS/Linux
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-17
```

### Error: Maven no encontrado
Verifica que Maven esté en tu PATH:
```bash
echo $PATH | grep maven  # macOS/Linux
echo %PATH% | findstr maven  # Windows
```

### Puerto 8080 ocupado
Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```
