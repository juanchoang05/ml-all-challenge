# Script para iniciar la aplicación ML completa en Windows

param(
    [switch]$Build,
    [switch]$SkipBrowser
)

# Colores para output
function Write-Success($message) {
    Write-Host "✅ $message" -ForegroundColor Green
}

function Write-Info($message) {
    Write-Host "ℹ️  $message" -ForegroundColor Blue
}

function Write-Error($message) {
    Write-Host "❌ $message" -ForegroundColor Red
}

Write-Host "🚀 Iniciando aplicación MercadoLibre completa..." -ForegroundColor Blue

# Copiar archivo de configuración si no existe
if (-not (Test-Path ".env")) {
    Write-Info "Copiando archivo de configuración..."
    if (Test-Path ".env.example") {
        Copy-Item ".env.example" ".env"
        Write-Success "Archivo .env creado. Puedes editarlo si necesitas cambiar alguna configuración."
    }
}

# Construir e iniciar los servicios
Write-Info "Construyendo e iniciando servicios..."
if ($Build) {
    docker-compose up --build -d
} else {
    docker-compose up -d
}

if ($LASTEXITCODE -ne 0) {
    Write-Error "Error iniciando servicios"
    exit 1
}

# Esperar a que los servicios estén listos
Write-Info "Esperando a que los servicios estén listos..."
Start-Sleep -Seconds 30

# Verificar el estado de los servicios
Write-Info "Verificando estado de los servicios..."
docker-compose ps

Write-Host ""
Write-Host "🎉 ¡Aplicación iniciada!" -ForegroundColor Green
Write-Host ""
Write-Host "📍 URLs disponibles:" -ForegroundColor Green
Write-Host "   Frontend (React):     http://localhost:3000" -ForegroundColor Blue
Write-Host "   Backend (Spring):     http://localhost:8080" -ForegroundColor Blue
Write-Host "   API Health Check:     http://localhost:8080/actuator/health" -ForegroundColor Blue
Write-Host "   Base de datos:        localhost:5432" -ForegroundColor Blue
Write-Host "   Redis:                localhost:6379" -ForegroundColor Blue
Write-Host ""
Write-Host "🔧 Comandos útiles:" -ForegroundColor Green
Write-Host "   Ver logs:             docker-compose logs -f" -ForegroundColor Yellow
Write-Host "   Parar aplicación:     .\stop.ps1" -ForegroundColor Yellow
Write-Host "   Reiniciar:            docker-compose restart" -ForegroundColor Yellow
Write-Host "   Ver servicios:        docker-compose ps" -ForegroundColor Yellow
Write-Host ""

# Opcional: abrir el navegador
if (-not $SkipBrowser) {
    $response = Read-Host "¿Deseas abrir el navegador automáticamente? (y/N)"
    if ($response -match '^[Yy]') {
        Write-Info "Abriendo navegador..."
        Start-Sleep -Seconds 5
        Start-Process "http://localhost:3000"
    }
}
