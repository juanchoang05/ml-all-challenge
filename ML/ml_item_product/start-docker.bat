@echo off
rem Script para iniciar el proyecto MercadoLibre Product Page con Docker
rem Compatible con Windows

echo 🐳 Iniciando MercadoLibre Product Page con Docker...
echo.

rem Verificar si Docker está instalado
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Docker no está instalado. Por favor instala Docker desde https://www.docker.com/
    pause
    exit /b 1
)

rem Verificar si Docker Compose está disponible
docker compose version >nul 2>&1
if %errorlevel% neq 0 (
    docker-compose --version >nul 2>&1
    if %errorlevel% neq 0 (
        echo ❌ Docker Compose no está disponible. Por favor instala Docker Compose.
        pause
        exit /b 1
    )
)

for /f "tokens=*" %%i in ('docker --version') do set DOCKER_VERSION=%%i
echo ✅ %DOCKER_VERSION% detectado
echo.

rem Verificar si docker-compose.yml existe
if not exist "docker-compose.yml" (
    echo ❌ No se encontró docker-compose.yml. Asegúrate de estar en el directorio correcto.
    pause
    exit /b 1
)

rem Verificar si Dockerfile existe
if not exist "Dockerfile" (
    echo ❌ No se encontró Dockerfile. Asegúrate de estar en el directorio correcto.
    pause
    exit /b 1
)

rem Detener contenedores existentes si están corriendo
echo 🛑 Deteniendo contenedores existentes...
docker compose down >nul 2>&1 || docker-compose down >nul 2>&1 || echo Ningún contenedor corriendo

rem Construir e iniciar los contenedores
echo 🔨 Construyendo e iniciando contenedores...
echo 📦 Esto puede tomar unos minutos la primera vez...
echo.

rem Intentar con docker compose primero, luego con docker-compose
docker compose up --build 2>nul
if %errorlevel% neq 0 (
    docker-compose up --build
)

echo.
echo 🌐 La aplicación debería estar disponible en: http://localhost:3000
echo 📊 Logs disponibles en la consola
echo.
echo Para detener los contenedores, presiona Ctrl+C y luego ejecuta:
echo docker compose down  o  docker-compose down

pause
