@echo off
setlocal enabledelayedexpansion

REM Script de instalación de Docker para Windows
REM ML Core Platform - MercadoLibre

echo.
echo =================================================================
echo  ML Core Platform - Instalador de Docker para Windows
echo =================================================================
echo.

REM Variables
set "DOCKER_DOWNLOAD_URL=https://desktop.docker.com/win/main/amd64/Docker%%20Desktop%%20Installer.exe"

REM Verificar Docker
:check_docker
echo [INFO] Verificando instalación de Docker...
docker --version >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] Docker ya está instalado
    docker --version
    goto check_docker_compose
) else (
    echo [INFO] Docker no encontrado. Procediendo con la instalación...
    goto install_docker
)

:check_docker_compose
echo [INFO] Verificando Docker Compose...
docker compose version >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] Docker Compose ya está instalado
    docker compose version
    goto verify_docker
) else (
    docker-compose --version >nul 2>&1
    if %errorLevel% equ 0 (
        echo [SUCCESS] Docker Compose (standalone) ya está instalado
        docker-compose --version
        goto verify_docker
    ) else (
        echo [INFO] Docker Compose no encontrado
        goto install_docker
    )
)

:install_docker
echo [INFO] Instalando Docker Desktop para Windows...

REM Verificar requisitos del sistema
echo [INFO] Verificando requisitos del sistema...

REM Verificar versión de Windows
for /f "tokens=3" %%i in ('reg query "HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion" /v CurrentVersion ^| findstr CurrentVersion') do set WINVER=%%i
echo [INFO] Versión de Windows: %WINVER%

REM Verificar si WSL2 está disponible (Windows 10 version 2004+)
ver | findstr "10\.0\.19041\|10\.0\.2" >nul
if %errorLevel% equ 0 (
    echo [SUCCESS] WSL2 es compatible con esta versión de Windows
) else (
    echo [WARNING] Esta versión de Windows podría no ser compatible con WSL2
    echo [INFO] Docker Desktop puede requerir Hyper-V en su lugar
)

REM Crear directorio temporal
if not exist "%TEMP%\ml-docker-setup" mkdir "%TEMP%\ml-docker-setup"

echo [INFO] Descargando Docker Desktop...
powershell -Command ^
    "$ProgressPreference = 'SilentlyContinue'; " ^
    "try { " ^
        "Invoke-WebRequest -Uri '%DOCKER_DOWNLOAD_URL%' -OutFile '%TEMP%\ml-docker-setup\DockerDesktopInstaller.exe' -UseBasicParsing; " ^
        "Write-Host '[SUCCESS] Docker Desktop descargado correctamente'; " ^
    "} catch { " ^
        "Write-Host '[ERROR] Error al descargar Docker Desktop. Verifique su conexión a internet.'; " ^
        "exit 1; " ^
    "}"

if %errorLevel% neq 0 (
    echo [ERROR] Error al descargar Docker Desktop
    goto manual_docker_install
)

echo [INFO] Ejecutando instalador de Docker Desktop...
echo [WARNING] El instalador puede requerir reinicio del sistema
echo [INFO] Siga las instrucciones del instalador...

start /wait "%TEMP%\ml-docker-setup\DockerDesktopInstaller.exe" install --quiet

if %errorLevel% equ 0 (
    echo [SUCCESS] Docker Desktop instalado correctamente
) else (
    echo [WARNING] El instalador terminó con código: %errorLevel%
    echo [INFO] Esto puede ser normal si se requiere reinicio
)

echo.
echo [INFO] Por favor:
echo [INFO] 1. Reinicie su computadora si se le solicita
echo [INFO] 2. Inicie Docker Desktop desde el menú de inicio
echo [INFO] 3. Complete la configuración inicial de Docker Desktop
echo [INFO] 4. Ejecute este script nuevamente para verificar la instalación
echo.
pause
goto end

:manual_docker_install
echo.
echo [WARNING] No se pudo descargar Docker Desktop automáticamente.
echo [INFO] Por favor, instale Docker Desktop manualmente:
echo [INFO] 1. Visite: https://www.docker.com/products/docker-desktop/
echo [INFO] 2. Descargue Docker Desktop para Windows
echo [INFO] 3. Ejecute el instalador como administrador
echo [INFO] 4. Reinicie su computadora si se le solicita
echo [INFO] 5. Ejecute este script nuevamente
echo.
pause
exit /b 1

:verify_docker
echo [INFO] Verificando instalación de Docker...

REM Verificar que Docker esté ejecutándose
docker system info >nul 2>&1
if %errorLevel% neq 0 (
    echo [WARNING] Docker no está ejecutándose
    echo [INFO] Iniciando Docker Desktop...
    
    REM Intentar iniciar Docker Desktop
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    
    echo [INFO] Esperando a que Docker Desktop inicie...
    timeout /t 30 /nobreak >nul
    
    REM Verificar nuevamente
    docker system info >nul 2>&1
    if %errorLevel% neq 0 (
        echo [ERROR] Docker Desktop no pudo iniciarse automáticamente
        echo [INFO] Por favor, inicie Docker Desktop manualmente y ejecute este script nuevamente
        pause
        exit /b 1
    )
)

echo.
echo ============================
echo  Verificación de instalación
echo ============================

REM Verificar Docker
docker --version >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Docker instalado correctamente
    docker --version
) else (
    echo [ERROR] ✗ Docker no está funcionando correctamente
    goto end
)

REM Verificar Docker Compose
docker compose version >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Docker Compose instalado correctamente
    docker compose version
) else (
    docker-compose --version >nul 2>&1
    if %errorLevel% equ 0 (
        echo [SUCCESS] ✓ Docker Compose (standalone) instalado correctamente
        docker-compose --version
    ) else (
        echo [ERROR] ✗ Docker Compose no está funcionando correctamente
    )
)

REM Test básico
echo [INFO] Ejecutando test básico de Docker...
docker run --rm hello-world >nul 2>&1
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Test de Docker exitoso
) else (
    echo [ERROR] ✗ Test de Docker falló
)

echo.
echo [SUCCESS] ¡Docker instalado y configurado correctamente!
echo.
echo Para usar Docker con ML Core Platform:
echo 1. Abra una nueva ventana de comandos
echo 2. Navegue al directorio del proyecto
echo 3. Ejecute: docker-compose up -d
echo 4. Visite: http://localhost:8080/swagger-ui.html
echo.

:end
REM Limpiar archivos temporales
if exist "%TEMP%\ml-docker-setup" (
    rmdir /s /q "%TEMP%\ml-docker-setup"
)

echo Presione cualquier tecla para continuar...
pause >nul
