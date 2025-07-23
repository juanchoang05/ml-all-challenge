# Script para parar la aplicación ML completa en Windows
# Incluye opciones para desarrollo y limpieza completa

param(
    [switch]$Force,
    [switch]$CleanVolumes,
    [switch]$CleanImages,
    [switch]$CleanAll
)

# Colores para output
function Write-Success($message) {
    Write-Host "✅ $message" -ForegroundColor Green
}

function Write-Warning($message) {
    Write-Host "⚠️  $message" -ForegroundColor Yellow
}

function Write-Info($message) {
    Write-Host "ℹ️  $message" -ForegroundColor Blue
}

function Write-Error($message) {
    Write-Host "❌ $message" -ForegroundColor Red
}

function Main {
    Write-Host "🛑 Deteniendo aplicación MercadoLibre..." -ForegroundColor Red
    
    # Verificar qué servicios están corriendo
    $runningContainers = docker-compose ps -q 2>$null
    if ($runningContainers -and $runningContainers.Trim()) {
        Write-Info "Parando servicios Docker..."
        docker-compose down --remove-orphans
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Servicios Docker detenidos"
        } else {
            Write-Error "Error deteniendo servicios Docker"
        }
    } else {
        Write-Info "No hay servicios Docker corriendo"
    }
    
    # Verificar procesos locales de desarrollo
    Write-Info "Verificando procesos locales..."
    
    # Buscar procesos de Spring Boot en puerto 8080
    try {
        $springProcess = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($springProcess) {
            $processId = (Get-Process -Id $springProcess.OwningProcess -ErrorAction SilentlyContinue).Id
            if ($processId) {
                Write-Warning "Encontrado proceso Spring Boot en puerto 8080 (PID: $processId)"
                if ($Force) {
                    Stop-Process -Id $processId -Force
                    Write-Success "Proceso Spring Boot detenido"
                } else {
                    $response = Read-Host "¿Deseas detenerlo? (y/N)"
                    if ($response -match '^[Yy]') {
                        Stop-Process -Id $processId -Force
                        Write-Success "Proceso Spring Boot detenido"
                    }
                }
            }
        }
    } catch {
        # Silenciar errores si no hay proceso
    }
    
    # Buscar procesos de Vite en puerto 5173
    try {
        $viteProcess = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($viteProcess) {
            $processId = (Get-Process -Id $viteProcess.OwningProcess -ErrorAction SilentlyContinue).Id
            if ($processId) {
                Write-Warning "Encontrado proceso Vite en puerto 5173 (PID: $processId)"
                if ($Force) {
                    Stop-Process -Id $processId -Force
                    Write-Success "Proceso Vite detenido"
                } else {
                    $response = Read-Host "¿Deseas detenerlo? (y/N)"
                    if ($response -match '^[Yy]') {
                        Stop-Process -Id $processId -Force
                        Write-Success "Proceso Vite detenido"
                    }
                }
            }
        }
    } catch {
        # Silenciar errores si no hay proceso
    }
    
    Write-Success "Aplicación detenida"
    
    Write-Host ""
    Write-Host "🔧 Opciones de limpieza:" -ForegroundColor Yellow
    
    # Opcional: limpiar volúmenes
    if ($CleanVolumes -or $CleanAll) {
        $cleanVolumes = $true
    } else {
        $response = Read-Host "¿Deseas eliminar los volúmenes de datos (BD, Redis)? (y/N)"
        $cleanVolumes = $response -match '^[Yy]'
    }
    
    if ($cleanVolumes) {
        Write-Info "Eliminando volúmenes..."
        docker-compose down -v 2>$null
        docker volume prune -f 2>$null
        Write-Success "Volúmenes eliminados"
    }
    
    # Opcional: limpiar imágenes
    if ($CleanImages -or $CleanAll) {
        $cleanImages = $true
    } else {
        $response = Read-Host "¿Deseas eliminar las imágenes Docker construidas? (y/N)"
        $cleanImages = $response -match '^[Yy]'
    }
    
    if ($cleanImages) {
        Write-Info "Eliminando imágenes..."
        docker-compose down --rmi all 2>$null
        Write-Success "Imágenes eliminadas"
    }
    
    # Opcional: limpieza completa
    if ($CleanAll) {
        $cleanSystem = $true
    } else {
        $response = Read-Host "¿Deseas hacer una limpieza completa de Docker? (y/N)"
        $cleanSystem = $response -match '^[Yy]'
    }
    
    if ($cleanSystem) {
        Write-Warning "Realizando limpieza completa de Docker..."
        docker system prune -a -f 2>$null
        Write-Success "Limpieza completa realizada"
    }
    
    Write-Host ""
    Write-Success "🏁 Proceso completado"
    Write-Host "💡 Para volver a iniciar:" -ForegroundColor Blue
    Write-Host "   .\setup-local.ps1  (entorno completo Docker)" -ForegroundColor White
    Write-Host "   .\setup-dev.ps1    (entorno de desarrollo)" -ForegroundColor White
}

# Ejecutar función principal
try {
    Main
} catch {
    Write-Error "Error durante la ejecución: $($_.Exception.Message)"
    exit 1
}
