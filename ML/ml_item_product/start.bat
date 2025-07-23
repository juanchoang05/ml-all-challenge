@echo off
rem Script para iniciar el proyecto MercadoLibre Product Page
rem Compatible con Windows

echo 🚀 Iniciando MercadoLibre Product Page...
echo.

rem Verificar si Node.js está instalado
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Node.js no está instalado. Por favor instala Node.js desde https://nodejs.org/
    pause
    exit /b 1
)

rem Verificar si npm está instalado
npm --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ npm no está instalado. Por favor instala npm.
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('node --version') do set NODE_VERSION=%%i
for /f "tokens=*" %%i in ('npm --version') do set NPM_VERSION=%%i

echo ✅ Node.js %NODE_VERSION% detectado
echo ✅ npm %NPM_VERSION% detectado
echo.

rem Verificar si package.json existe
if not exist "package.json" (
    echo ❌ No se encontró package.json. Asegúrate de estar en el directorio correcto.
    pause
    exit /b 1
)

rem Instalar dependencias
echo 📦 Instalando dependencias...
call npm install

if %errorlevel% neq 0 (
    echo ❌ Error al instalar dependencias
    pause
    exit /b 1
)

echo ✅ Dependencias instaladas correctamente
echo.

rem Configurar ambiente inicial
echo ⚙️ Configurando ambiente inicial...
call npm run env:switch development colombia

if %errorlevel% neq 0 (
    echo ⚠️  No se pudo configurar el ambiente automáticamente, continuando...
)

echo.

rem Iniciar el proyecto en modo local
echo 🔥 Iniciando proyecto en modo desarrollo local...
echo 🌐 La aplicación estará disponible en: http://localhost:5173
echo.
echo Presiona Ctrl+C para detener el servidor
echo.

call npm run dev:local

pause
