@echo off
echo 🚀 Iniciando entorno MercadoLibre para Windows...
echo.

REM Verificar si PowerShell está disponible
powershell -Command "Get-Host" >nul 2>&1
if errorlevel 1 (
    echo ❌ PowerShell no está disponible
    echo Por favor instala PowerShell o usa los scripts .ps1 directamente
    pause
    exit /b 1
)

echo ℹ️  Ejecutando setup de PowerShell...
powershell -ExecutionPolicy Bypass -File ".\setup-local.ps1"

if errorlevel 1 (
    echo.
    echo ❌ Error durante la configuración
    echo 💡 Intenta ejecutar directamente: .\setup-local.ps1
    pause
    exit /b 1
)

echo.
echo ✅ ¡Configuración completada!
pause
