@echo off
setlocal enabledelayedexpansion

REM Script de inicio rápido para ML Core Platform con Docker - Windows
REM Uso: quick-start-docker.bat

echo.
echo =================================================================
echo  🐳 ML Core Platform - Inicio Rápido con Docker
echo =================================================================
echo.

REM Verificar que Docker esté instalado
docker --version >nul 2>&1
if %errorLevel% neq 0 (
    echo [ERROR] Docker no está instalado
    echo [INFO] Ejecute: install-docker-windows.bat
    pause
    exit /b 1
)

REM Verificar que Docker esté ejecutándose
docker system info >nul 2>&1
if %errorLevel% neq 0 (
    echo [ERROR] Docker no está ejecutándose
    echo [INFO] Inicie Docker Desktop y ejecute este script nuevamente
    pause
    exit /b 1
)

echo [SUCCESS] Docker está instalado y ejecutándose

REM Verificar Docker Compose
docker compose version >nul 2>&1
if %errorLevel% equ 0 (
    set "COMPOSE_CMD=docker compose"
    echo [SUCCESS] Docker Compose encontrado: docker compose
) else (
    docker-compose --version >nul 2>&1
    if %errorLevel% equ 0 (
        set "COMPOSE_CMD=docker-compose"
        echo [SUCCESS] Docker Compose encontrado: docker-compose
    ) else (
        echo [ERROR] Docker Compose no está disponible
        pause
        exit /b 1
    )
)

REM Limpiar contenedores previos
echo [INFO] Limpiando contenedores previos...
%COMPOSE_CMD% down -v --remove-orphans >nul 2>&1

REM Construir y ejecutar servicios
echo [INFO] Construyendo y ejecutando servicios...
%COMPOSE_CMD% up -d --build

if %errorLevel% neq 0 (
    echo [ERROR] Error al construir o ejecutar los servicios
    echo [INFO] Verificando logs...
    %COMPOSE_CMD% logs --tail=10
    pause
    exit /b 1
)

REM Esperar a que los servicios estén listos
echo [INFO] Esperando a que los servicios inicien...
timeout /t 10 /nobreak >nul

REM Verificar base de datos
echo [INFO] Verificando base de datos PostgreSQL...
%COMPOSE_CMD% exec -T ml-database pg_isready -U mluser -d mlcoreplatform >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Base de datos PostgreSQL lista
) else (
    echo [WARNING] ⚠ Base de datos PostgreSQL no responde
)

REM Verificar Redis
echo [INFO] Verificando Redis...
%COMPOSE_CMD% exec -T ml-redis redis-cli ping >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Redis listo
) else (
    echo [WARNING] ⚠ Redis no responde
)

REM Verificar aplicación (esperando más tiempo)
echo [INFO] Verificando aplicación Spring Boot...
set /a attempts=0
:check_app
curl -s -f "http://localhost:8080/actuator/health" >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Aplicación Spring Boot lista
    goto app_ready
)

set /a attempts+=1
if %attempts% lss 15 (
    echo Esperando aplicación...
    timeout /t 4 /nobreak >nul
    goto check_app
)

echo [WARNING] ⚠ Aplicación Spring Boot no responde
echo [INFO] Verificando logs...
%COMPOSE_CMD% logs --tail=10 ml-core-platform

:app_ready

REM Mostrar estado de servicios
echo.
echo [INFO] Estado de los servicios:
%COMPOSE_CMD% ps

REM Mostrar URLs de acceso
echo.
echo [SUCCESS] 🎉 ¡ML Core Platform está ejecutándose!
echo.
echo 📋 URLs de acceso:
echo    🌐 Aplicación:     http://localhost:8080
echo    📚 Swagger UI:     http://localhost:8080/swagger-ui.html
echo    ❤️ Health Check:  http://localhost:8080/actuator/health
echo    🗄️ PostgreSQL:    localhost:5432 (mluser/mlpassword)
echo    🔄 Redis:          localhost:6379
echo.
echo 📊 Comandos útiles:
echo    Ver logs:          %COMPOSE_CMD% logs -f ml-core-platform
echo    Detener servicios: %COMPOSE_CMD% down
echo    Reiniciar:         %COMPOSE_CMD% restart ml-core-platform
echo.

REM Abrir navegador automáticamente
echo [INFO] Abriendo Swagger UI en el navegador...
timeout /t 3 /nobreak >nul
start http://localhost:8080/swagger-ui.html

echo Presione cualquier tecla para continuar...
pause >nul
