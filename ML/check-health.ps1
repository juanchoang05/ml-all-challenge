# Script para verificar que el entorno local esté funcionando correctamente en Windows
# Realiza checks de salud de todos los servicios

param(
    [switch]$Verbose,
    [switch]$Quick
)

# Colores para output
function Write-Header($message) {
    Write-Host "`n════════════════════════════════════════════" -ForegroundColor Blue
    Write-Host $message -ForegroundColor Blue
    Write-Host "════════════════════════════════════════════" -ForegroundColor Blue
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

# Variables para tracking
$script:IssuesFound = 0

# Función para reportar problemas
function Report-Issue($message) {
    Write-Error $message
    $script:IssuesFound++
}

# Verificar Docker
function Check-Docker {
    Write-Info "Verificando Docker..."
    
    try {
        $dockerVersion = docker --version 2>$null
        if ($LASTEXITCODE -ne 0) {
            Report-Issue "Docker no está instalado"
            return $false
        }
    } catch {
        Report-Issue "Docker no está instalado"
        return $false
    }
    
    try {
        docker info 2>$null | Out-Null
        if ($LASTEXITCODE -ne 0) {
            Report-Issue "Docker no está corriendo"
            return $false
        }
    } catch {
        Report-Issue "Docker no está corriendo"
        return $false
    }
    
    Write-Success "Docker está funcionando"
    return $true
}

# Verificar servicios Docker
function Check-Docker-Services {
    Write-Info "Verificando servicios Docker..."
    
    $services = @("ml-item-product-app", "ml-core-platform-app", "ml-core-platform-db", "ml-core-platform-redis")
    
    foreach ($service in $services) {
        $containerStatus = docker ps --format "table {{.Names}}\t{{.Status}}" | Select-String $service
        if ($containerStatus) {
            Write-Success "Servicio $service está corriendo"
        } else {
            Write-Warning "Servicio $service no está corriendo"
        }
    }
}

# Verificar conectividad de puertos
function Check-Ports {
    Write-Info "Verificando puertos..."
    
    $ports = @(3000, 8080, 5432, 6379)
    $services = @("Frontend", "Backend", "PostgreSQL", "Redis")
    
    for ($i = 0; $i -lt $ports.Length; $i++) {
        $port = $ports[$i]
        $service = $services[$i]
        
        try {
            $connection = Test-NetConnection -ComputerName localhost -Port $port -InformationLevel Quiet -WarningAction SilentlyContinue
            if ($connection.TcpTestSucceeded) {
                Write-Success "$service responde en puerto $port"
            } else {
                Report-Issue "$service no responde en puerto $port"
            }
        } catch {
            Report-Issue "$service no responde en puerto $port"
        }
    }
}

# Verificar health endpoints
function Check-Health-Endpoints {
    Write-Info "Verificando endpoints de salud..."
    
    # Backend health check
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 10 -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            try {
                $health = $response.Content | ConvertFrom-Json
                if ($health.status -eq "UP") {
                    Write-Success "Backend health check: UP"
                } else {
                    Write-Warning "Backend health check: $($health.status)"
                }
            } catch {
                Write-Success "Backend health endpoint responde"
            }
        } else {
            Report-Issue "Backend health endpoint no responde"
        }
    } catch {
        Report-Issue "Backend health endpoint no responde"
    }
    
    # Frontend basic check
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:3000" -UseBasicParsing -TimeoutSec 10 -ErrorAction SilentlyContinue
        if ($response.StatusCode -eq 200) {
            Write-Success "Frontend responde correctamente"
        } else {
            Report-Issue "Frontend no responde"
        }
    } catch {
        Report-Issue "Frontend no responde"
    }
}

# Verificar base de datos
function Check-Database {
    Write-Info "Verificando base de datos..."
    
    try {
        docker-compose exec -T ml-database pg_isready -U mluser -d mlcoreplatform 2>$null | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "PostgreSQL está funcionando"
            
            # Verificar conexión desde el backend
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
                if ($response.StatusCode -eq 200) {
                    try {
                        $health = $response.Content | ConvertFrom-Json
                        if ($health.components.db.status -eq "UP") {
                            Write-Success "Conexión backend-database: OK"
                        } else {
                            Write-Warning "Conexión backend-database: $($health.components.db.status)"
                        }
                    } catch {
                        Write-Info "No se pudo verificar conexión backend-database desde health endpoint"
                    }
                }
            } catch {
                Write-Warning "No se pudo verificar conexión backend-database"
            }
        } else {
            Report-Issue "PostgreSQL no está funcionando"
        }
    } catch {
        Report-Issue "PostgreSQL no está funcionando"
    }
}

# Verificar Redis
function Check-Redis {
    Write-Info "Verificando Redis..."
    
    try {
        docker-compose exec -T ml-redis redis-cli ping 2>$null | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Redis está funcionando"
            
            # Verificar conexión desde el backend
            try {
                $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
                if ($response.StatusCode -eq 200) {
                    try {
                        $health = $response.Content | ConvertFrom-Json
                        if ($health.components.redis.status -eq "UP") {
                            Write-Success "Conexión backend-redis: OK"
                        } else {
                            Write-Warning "Conexión backend-redis: $($health.components.redis.status)"
                        }
                    } catch {
                        Write-Info "No se pudo verificar conexión backend-redis desde health endpoint"
                    }
                }
            } catch {
                Write-Warning "No se pudo verificar conexión backend-redis"
            }
        } else {
            Report-Issue "Redis no está funcionando"
        }
    } catch {
        Report-Issue "Redis no está funcionando"
    }
}

# Verificar logs para errores
function Check-Logs {
    if ($Quick) { return }
    
    Write-Info "Verificando logs para errores críticos..."
    
    try {
        # Verificar logs del backend para errores
        $backendLogs = docker-compose logs ml-core-platform 2>$null
        if ($backendLogs) {
            $backendErrors = ($backendLogs | Select-String -Pattern "error|exception|failed" -CaseSensitive:$false).Count
            if ($backendErrors -gt 0) {
                Write-Warning "Se encontraron $backendErrors errores en logs del backend"
            } else {
                Write-Success "No se encontraron errores críticos en backend"
            }
        }
        
        # Verificar logs del frontend para errores
        $frontendLogs = docker-compose logs ml-item-product 2>$null
        if ($frontendLogs) {
            $frontendErrors = ($frontendLogs | Select-String -Pattern "error|failed" -CaseSensitive:$false).Count
            if ($frontendErrors -gt 0) {
                Write-Warning "Se encontraron $frontendErrors errores en logs del frontend"
            } else {
                Write-Success "No se encontraron errores críticos en frontend"
            }
        }
    } catch {
        Write-Warning "No se pudieron verificar logs"
    }
}

# Verificar recursos del sistema
function Check-System-Resources {
    if ($Quick) { return }
    
    Write-Info "Verificando recursos del sistema..."
    
    try {
        # Verificar uso de memoria Docker
        $dockerStats = docker stats --no-stream --format "table {{.MemUsage}}" 2>$null
        if ($dockerStats) {
            Write-Info "Docker está consumiendo memoria"
        }
        
        # Verificar espacio en disco
        $diskInfo = Get-WmiObject -Class Win32_LogicalDisk -Filter "DeviceID='C:'"
        $freeSpaceGB = [math]::Round($diskInfo.FreeSpace / 1GB, 2)
        $totalSpaceGB = [math]::Round($diskInfo.Size / 1GB, 2)
        $usedPercentage = [math]::Round((($totalSpaceGB - $freeSpaceGB) / $totalSpaceGB) * 100, 1)
        
        if ($usedPercentage -gt 90) {
            Write-Warning "Uso de disco alto: ${usedPercentage}%"
        } else {
            Write-Success "Espacio en disco: ${usedPercentage}% usado (${freeSpaceGB}GB libres)"
        }
    } catch {
        Write-Warning "No se pudieron verificar recursos del sistema"
    }
}

# Mostrar resumen de APIs disponibles
function Show-API-Summary {
    Write-Info "Verificando APIs disponibles..."
    
    Write-Host "📋 Resumen de APIs:" -ForegroundColor Blue
    Write-Host ""
    
    # Lista de endpoints importantes
    $endpoints = @(
        @{ Method = "GET"; Url = "http://localhost:8080/actuator/health"; Description = "Health Check" },
        @{ Method = "GET"; Url = "http://localhost:8080/actuator/info"; Description = "Application Info" },
        @{ Method = "GET"; Url = "http://localhost:8080/swagger-ui.html"; Description = "API Documentation" }
    )
    
    foreach ($endpoint in $endpoints) {
        try {
            $response = Invoke-WebRequest -Uri $endpoint.Url -UseBasicParsing -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200) {
                Write-Host "   ✅ $($endpoint.Method) $($endpoint.Url)" -ForegroundColor Green -NoNewline
                Write-Host " - $($endpoint.Description)" -ForegroundColor White
            } else {
                Write-Host "   ❌ $($endpoint.Method) $($endpoint.Url)" -ForegroundColor Red -NoNewline
                Write-Host " - $($endpoint.Description)" -ForegroundColor White
            }
        } catch {
            Write-Host "   ❌ $($endpoint.Method) $($endpoint.Url)" -ForegroundColor Red -NoNewline
            Write-Host " - $($endpoint.Description)" -ForegroundColor White
        }
    }
}

# Función principal
function Main {
    Write-Header "🔍 Verificación del Entorno Local MercadoLibre (Windows)"
    
    Write-Host ""
    Check-Docker
    Write-Host ""
    Check-Docker-Services
    Write-Host ""
    Check-Ports
    Write-Host ""
    Check-Health-Endpoints
    Write-Host ""
    Check-Database
    Write-Host ""
    Check-Redis
    Write-Host ""
    Check-Logs
    Write-Host ""
    Check-System-Resources
    Write-Host ""
    Show-API-Summary
    
    Write-Host ""
    Write-Header "📊 Resumen de Verificación"
    
    if ($script:IssuesFound -eq 0) {
        Write-Success "🎉 ¡Todo está funcionando perfectamente!"
        Write-Host ""
        Write-Host "🌐 URLs disponibles:" -ForegroundColor Green
        Write-Host "   Frontend: http://localhost:3000" -ForegroundColor Blue
        Write-Host "   Backend:  http://localhost:8080" -ForegroundColor Blue
        Write-Host "   Swagger:  http://localhost:8080/swagger-ui.html" -ForegroundColor Blue
        Write-Host "   Health:   http://localhost:8080/actuator/health" -ForegroundColor Blue
    } else {
        Write-Warning "Se encontraron $($script:IssuesFound) problemas"
        Write-Host ""
        Write-Host "💡 Sugerencias:" -ForegroundColor Yellow
        Write-Host "   • Ejecutar: .\setup-local.ps1" -ForegroundColor White
        Write-Host "   • Verificar Docker: docker-compose ps" -ForegroundColor White
        Write-Host "   • Ver logs: docker-compose logs -f" -ForegroundColor White
        Write-Host "   • Reiniciar: docker-compose restart" -ForegroundColor White
    }
    
    Write-Host ""
}

# Ejecutar función principal
try {
    Main
} catch {
    Write-Error "Error durante la verificación: $($_.Exception.Message)"
    exit 1
}
