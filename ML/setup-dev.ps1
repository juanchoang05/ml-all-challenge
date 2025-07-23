# Script para desarrollo local con hot-reload en Windows
# Este script levanta los servicios de base de datos en Docker
# pero permite ejecutar el frontend y backend en modo desarrollo

param(
    [switch]$Verbose
)

# Configurar política de ejecución si es necesaria
if ((Get-ExecutionPolicy) -eq 'Restricted') {
    Write-Warning "La política de ejecución está restringida. Ejecute como administrador:"
    Write-Host "Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser" -ForegroundColor Yellow
    exit 1
}

# Colores para output
function Write-Header($message) {
    Write-Host "`n============================================" -ForegroundColor Blue
    Write-Host $message -ForegroundColor Blue
    Write-Host "============================================" -ForegroundColor Blue
}

function Write-Success($message) {
    Write-Host "✅ $message" -ForegroundColor Green
}

function Write-Warning($message) {
    Write-Host "⚠️  $message" -ForegroundColor Yellow
}

function Write-Error($message) {
    Write-Host "❌ $message" -ForegroundColor Red
}

function Write-Info($message) {
    Write-Host "ℹ️  $message" -ForegroundColor Blue
}

# Verificar dependencias para desarrollo
function Check-Dev-Dependencies {
    Write-Header "Verificando dependencias de desarrollo"
    
    # Verificar Node.js
    try {
        $nodeVersion = node --version 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Node.js está instalado: $($nodeVersion.Trim())"
        } else {
            throw "Node.js no encontrado"
        }
    } catch {
        Write-Error "Node.js no está instalado. Por favor descárgalo desde https://nodejs.org/"
        exit 1
    }
    
    # Verificar npm
    try {
        $npmVersion = npm --version 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "npm está instalado: $($npmVersion.Trim())"
        } else {
            throw "npm no encontrado"
        }
    } catch {
        Write-Error "npm no está instalado"
        exit 1
    }
    
    # Verificar Java
    try {
        $javaVersion = java --version 2>$null | Select-Object -First 1
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Java está instalado: $($javaVersion.Trim())"
        } else {
            throw "Java no encontrado"
        }
    } catch {
        Write-Error "Java no está instalado. Necesitas Java 17 o superior"
        Write-Info "Descarga desde: https://adoptium.net/"
        exit 1
    }
    
    # Verificar Maven
    try {
        $mvnVersion = mvn --version 2>$null | Select-Object -First 1
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Maven está instalado: $($mvnVersion.Trim())"
        } else {
            throw "Maven no encontrado"
        }
    } catch {
        Write-Error "Maven no está instalado"
        Write-Info "Descarga desde: https://maven.apache.org/download.cgi"
        exit 1
    }
}

# Iniciar solo servicios de infraestructura (BD, Redis)
function Start-Infrastructure {
    Write-Header "Iniciando servicios de infraestructura"
    
    # Crear archivo de configuración si no existe
    if (-not (Test-Path ".env")) {
        if (Test-Path ".env.example") {
            Copy-Item ".env.example" ".env"
            Write-Success "Archivo .env creado"
        }
    }
    
    # Iniciar solo base de datos y Redis
    Write-Info "Iniciando PostgreSQL y Redis..."
    docker-compose up -d ml-database ml-redis
    
    # Esperar a que estén listos
    Write-Info "Esperando que PostgreSQL esté listo..."
    $maxAttempts = 30
    $attempt = 1
    while ($attempt -le $maxAttempts) {
        try {
            docker-compose exec -T ml-database pg_isready -U mluser -d mlcoreplatform 2>$null | Out-Null
            if ($LASTEXITCODE -eq 0) {
                Write-Success "PostgreSQL está listo"
                break
            }
        } catch { }
        
        if ($attempt -eq $maxAttempts) {
            Write-Error "PostgreSQL no responde"
            exit 1
        }
        Start-Sleep -Seconds 2
        $attempt++
    }
    
    Write-Info "Verificando Redis..."
    $attempt = 1
    while ($attempt -le $maxAttempts) {
        try {
            docker-compose exec -T ml-redis redis-cli ping 2>$null | Out-Null
            if ($LASTEXITCODE -eq 0) {
                Write-Success "Redis está listo"
                break
            }
        } catch { }
        
        if ($attempt -eq $maxAttempts) {
            Write-Error "Redis no responde"
            exit 1
        }
        Start-Sleep -Seconds 2
        $attempt++
    }
}

# Instalar dependencias del frontend
function Setup-Frontend {
    Write-Header "Configurando frontend (React + Vite)"
    
    if (Test-Path "ml_item_product") {
        Set-Location "ml_item_product"
        
        # Instalar dependencias si no existen
        if (-not (Test-Path "node_modules")) {
            Write-Info "Instalando dependencias del frontend..."
            npm install
            if ($LASTEXITCODE -eq 0) {
                Write-Success "Dependencias del frontend instaladas"
            } else {
                Write-Error "Error instalando dependencias del frontend"
                Set-Location ".."
                exit 1
            }
        } else {
            Write-Info "Dependencias del frontend ya están instaladas"
        }
        
        # Crear .env.local para desarrollo
        if (-not (Test-Path ".env.local")) {
            $envContent = @"
VITE_API_URL=http://localhost:8080
VITE_ENV=development
VITE_REGION=colombia
VITE_USE_MOCK_DATA=false
"@
            $envContent | Out-File -FilePath ".env.local" -Encoding UTF8
            Write-Success "Archivo .env.local creado para desarrollo"
        }
        
        Set-Location ".."
    }
}

# Configurar backend para desarrollo local
function Setup-Backend {
    Write-Header "Configurando backend (Spring Boot)"
    
    if (Test-Path "ml-core-platform") {
        Set-Location "ml-core-platform"
        
        # Crear application-local.properties si no existe
        $appPropsPath = "src\main\resources\application-local.properties"
        if (-not (Test-Path $appPropsPath)) {
            $appPropsDir = Split-Path $appPropsPath -Parent
            if (-not (Test-Path $appPropsDir)) {
                New-Item -ItemType Directory -Path $appPropsDir -Force | Out-Null
            }
            
            $propsContent = @"
# Configuración para desarrollo local
server.port=8080

# Base de datos PostgreSQL (Docker)
spring.datasource.url=jdbc:postgresql://localhost:5432/mlcoreplatform
spring.datasource.username=mluser
spring.datasource.password=mlpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Redis (Docker)
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000ms

# Logging para desarrollo
logging.level.com.mercadolibre=DEBUG
logging.level.org.springframework.web=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always

# CORS para desarrollo
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=*
spring.web.cors.allowed-headers=*
"@
            $propsContent | Out-File -FilePath $appPropsPath -Encoding UTF8
            Write-Success "Archivo application-local.properties creado"
        }
        
        Set-Location ".."
    }
}

# Mostrar instrucciones para iniciar los servicios
function Show-Dev-Instructions {
    Write-Header "🎯 Entorno de desarrollo configurado"
    
    Write-Host ""
    Write-Host "📍 Servicios de infraestructura corriendo:" -ForegroundColor Green
    Write-Host "   PostgreSQL: localhost:5432 (mluser/mlpassword)" -ForegroundColor Blue
    Write-Host "   Redis:      localhost:6379" -ForegroundColor Blue
    Write-Host ""
    Write-Host "🚀 Para iniciar el desarrollo:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1. Backend (Spring Boot):" -ForegroundColor Blue
    Write-Host "   cd ml-core-platform" -ForegroundColor White
    Write-Host "   mvn spring-boot:run -Dspring-boot.run.profiles=local" -ForegroundColor White
    Write-Host "   ➜ Disponible en: http://localhost:8080" -ForegroundColor Green
    Write-Host ""
    Write-Host "2. Frontend (React + Vite) - EN OTRA TERMINAL:" -ForegroundColor Blue
    Write-Host "   cd ml_item_product" -ForegroundColor White
    Write-Host "   npm run dev" -ForegroundColor White
    Write-Host "   ➜ Disponible en: http://localhost:5173" -ForegroundColor Green
    Write-Host ""
    Write-Host "🔧 Comandos útiles:" -ForegroundColor Green
    Write-Host "   Ver logs de BD:       docker-compose logs -f ml-database" -ForegroundColor Yellow
    Write-Host "   Ver logs de Redis:    docker-compose logs -f ml-redis" -ForegroundColor Yellow
    Write-Host "   Parar infraestructura: docker-compose stop ml-database ml-redis" -ForegroundColor Yellow
    Write-Host "   Reiniciar BD:         docker-compose restart ml-database" -ForegroundColor Yellow
    Write-Host "   Acceder a BD:         docker-compose exec ml-database psql -U mluser -d mlcoreplatform" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "💡 Ventajas del modo desarrollo:" -ForegroundColor Green
    Write-Host "   ✨ Hot-reload automático en frontend y backend" -ForegroundColor White
    Write-Host "   🐛 Debugging más fácil" -ForegroundColor White
    Write-Host "   🔍 Logs detallados" -ForegroundColor White
    Write-Host "   ⚡ Compilación más rápida" -ForegroundColor White
    Write-Host ""
}

# Función principal
function Main {
    try {
        Write-Header "🔧 Configuración del entorno de desarrollo (Windows)"
        
        Check-Dev-Dependencies
        Start-Infrastructure
        Setup-Frontend
        Setup-Backend
        Show-Dev-Instructions
        
        Write-Success "¡Entorno de desarrollo listo! 🎉"
        
    } catch {
        Write-Error "Error durante la configuración: $($_.Exception.Message)"
        exit 1
    }
}

# Manejo de interrupciones
$ErrorActionPreference = "Stop"
trap {
    Write-Error "Setup interrumpido"
    exit 1
}

# Ejecutar función principal
Main
