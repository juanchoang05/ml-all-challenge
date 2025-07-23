# Script completo para configurar y ejecutar el entorno local de MercadoLibre en Windows
# Incluye verificaciones de dependencias y configuración automática

param(
    [switch]$SkipBrowser,
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

# Verificar dependencias
function Check-Dependencies {
    Write-Header "Verificando dependencias del sistema"
    
    # Verificar Docker
    try {
        $dockerVersion = docker --version 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Docker está instalado: $($dockerVersion.Trim())"
        } else {
            throw "Docker no encontrado"
        }
    } catch {
        Write-Error "Docker no está instalado. Por favor descárgalo desde https://www.docker.com/products/docker-desktop"
        exit 1
    }
    
    # Verificar Docker Compose
    try {
        $composeVersion = docker-compose --version 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Docker Compose está instalado: $($composeVersion.Trim())"
        } else {
            throw "Docker Compose no encontrado"
        }
    } catch {
        Write-Error "Docker Compose no está instalado. Viene incluido con Docker Desktop"
        exit 1
    }
    
    # Verificar que Docker esté corriendo
    try {
        docker info 2>$null | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Docker está corriendo"
        } else {
            throw "Docker no está corriendo"
        }
    } catch {
        Write-Error "Docker no está corriendo. Por favor inicia Docker Desktop"
        exit 1
    }
    
    # Verificar puertos disponibles
    $ports = @(3000, 8080, 5432, 6379)
    foreach ($port in $ports) {
        try {
            $connection = Test-NetConnection -ComputerName localhost -Port $port -InformationLevel Quiet -WarningAction SilentlyContinue
            if ($connection) {
                Write-Warning "Puerto $port está en uso. Esto podría causar conflictos."
            } else {
                Write-Success "Puerto $port está disponible"
            }
        } catch {
            Write-Success "Puerto $port está disponible"
        }
    }
}

# Configurar archivos de entorno
function Setup-Environment {
    Write-Header "Configurando archivos de entorno"
    
    # Copiar .env si no existe
    if (-not (Test-Path ".env")) {
        Write-Info "Creando archivo .env desde .env.example"
        if (Test-Path ".env.example") {
            Copy-Item ".env.example" ".env"
            Write-Success "Archivo .env creado"
        } else {
            Write-Warning "Archivo .env.example no encontrado"
        }
    } else {
        Write-Info "Archivo .env ya existe"
    }
    
    # Verificar archivos de configuración del frontend
    if (-not (Test-Path "ml_item_product\.env")) {
        if (Test-Path "ml_item_product\.env.example") {
            Write-Info "Creando .env para el frontend"
            Copy-Item "ml_item_product\.env.example" "ml_item_product\.env"
            Write-Success "Archivo .env del frontend creado"
        }
    }
}

# Limpiar contenedores anteriores
function Cleanup-Previous {
    Write-Header "Limpiando contenedores anteriores"
    
    # Verificar si hay contenedores corriendo
    $runningContainers = docker-compose ps -q 2>$null
    if ($runningContainers -and $runningContainers.Trim()) {
        Write-Info "Parando contenedores existentes..."
        docker-compose down --remove-orphans
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Contenedores anteriores eliminados"
        } else {
            Write-Warning "Error al eliminar contenedores anteriores"
        }
    } else {
        Write-Info "No hay contenedores corriendo"
    }
}

# Construir e iniciar servicios
function Build-And-Start {
    Write-Header "Construyendo e iniciando servicios"
    
    Write-Info "Construyendo imágenes Docker..."
    docker-compose build
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Imágenes construidas exitosamente"
    } else {
        Write-Error "Error construyendo imágenes"
        exit 1
    }
    
    Write-Info "Iniciando servicios..."
    docker-compose up -d
    if ($LASTEXITCODE -eq 0) {
        Write-Success "Servicios iniciados"
    } else {
        Write-Error "Error iniciando servicios"
        exit 1
    }
}

# Verificar que los servicios estén funcionando
function Verify-Services {
    Write-Header "Verificando servicios"
    
    $maxAttempts = 30
    
    # Verificar base de datos
    Write-Info "Esperando que PostgreSQL esté listo..."
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
            Write-Error "PostgreSQL no responde después de $maxAttempts intentos"
            exit 1
        }
        Start-Sleep -Seconds 2
        $attempt++
    }
    
    # Verificar Redis
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
            Write-Error "Redis no responde después de $maxAttempts intentos"
            exit 1
        }
        Start-Sleep -Seconds 2
        $attempt++
    }
    
    # Verificar backend (Spring Boot)
    Write-Info "Esperando que el backend esté listo..."
    $attempt = 1
    while ($attempt -le $maxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200) {
                Write-Success "Backend está listo"
                break
            }
        } catch { }
        
        if ($attempt -eq $maxAttempts) {
            Write-Warning "Backend no responde en /actuator/health. Puede estar iniciando aún..."
            break
        }
        Start-Sleep -Seconds 3
        $attempt++
    }
    
    # Verificar frontend
    Write-Info "Verificando frontend..."
    $attempt = 1
    while ($attempt -le $maxAttempts) {
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:3000" -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200) {
                Write-Success "Frontend está listo"
                break
            }
        } catch { }
        
        if ($attempt -eq $maxAttempts) {
            Write-Warning "Frontend no responde en puerto 3000. Puede estar iniciando aún..."
            break
        }
        Start-Sleep -Seconds 2
        $attempt++
    }
}

# Mostrar información final
function Show-Info {
    Write-Header "🎉 ¡Entorno local configurado exitosamente!"
    
    Write-Host ""
    Write-Host "📍 URLs disponibles:" -ForegroundColor Green
    Write-Host "   Frontend (React):     http://localhost:3000" -ForegroundColor Blue
    Write-Host "   Backend (Spring):     http://localhost:8080" -ForegroundColor Blue
    Write-Host "   API Health Check:     http://localhost:8080/actuator/health" -ForegroundColor Blue
    Write-Host "   Swagger UI:           http://localhost:8080/swagger-ui.html" -ForegroundColor Blue
    Write-Host "   Base de datos:        localhost:5432 (mluser/mlpassword)" -ForegroundColor Blue
    Write-Host "   Redis:                localhost:6379" -ForegroundColor Blue
    Write-Host ""
    Write-Host "🔧 Comandos útiles:" -ForegroundColor Green
    Write-Host "   Ver logs:             docker-compose logs -f" -ForegroundColor Yellow
    Write-Host "   Ver logs específicos: docker-compose logs -f [ml-item-product|ml-core-platform]" -ForegroundColor Yellow
    Write-Host "   Parar aplicación:     .\stop.ps1" -ForegroundColor Yellow
    Write-Host "   Reiniciar:            docker-compose restart" -ForegroundColor Yellow
    Write-Host "   Ver servicios:        docker-compose ps" -ForegroundColor Yellow
    Write-Host "   Entrar al backend:    docker-compose exec ml-core-platform bash" -ForegroundColor Yellow
    Write-Host "   Ver BD:               docker-compose exec ml-database psql -U mluser -d mlcoreplatform" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "📁 Estructura del proyecto:" -ForegroundColor Green
    Write-Host "   ml_item_product\      - Frontend React con Vite" -ForegroundColor Blue
    Write-Host "   ml-core-platform\     - Backend Spring Boot" -ForegroundColor Blue
    Write-Host ""
}

# Función principal
function Main {
    try {
        Write-Header "🚀 Configuración del entorno local de MercadoLibre (Windows)"
        
        Check-Dependencies
        Setup-Environment
        Cleanup-Previous
        Build-And-Start
        Verify-Services
        Show-Info
        
        # Opcional: abrir el navegador
        if (-not $SkipBrowser) {
            $response = Read-Host "¿Deseas abrir el navegador automáticamente? (y/N)"
            if ($response -match '^[Yy]') {
                Write-Info "Abriendo navegador..."
                Start-Sleep -Seconds 3
                Start-Process "http://localhost:3000"
            }
        }
        
        Write-Success "¡Setup completado! Disfruta desarrollando 🎯"
        
    } catch {
        Write-Error "Error durante la configuración: $($_.Exception.Message)"
        exit 1
    }
}

# Manejo de interrupciones
$ErrorActionPreference = "Stop"
trap {
    Write-Error "Setup interrumpido por el usuario"
    exit 1
}

# Ejecutar función principal
Main
