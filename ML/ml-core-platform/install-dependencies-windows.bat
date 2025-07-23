@echo off
setlocal enabledelayedexpansion

REM Script de instalación de dependencias para Windows
REM ML Core Platform - MercadoLibre

echo.
echo =================================================================
echo  ML Core Platform - Instalador de Dependencias para Windows
echo =================================================================
echo.

REM Variables de configuración
set "JAVA_DOWNLOAD_URL=https://download.oracle.com/java/17/latest/jdk-17_windows-x64_bin.exe"
set "MAVEN_VERSION=3.9.6"
set "MAVEN_DOWNLOAD_URL=https://archive.apache.org/dist/maven/maven-3/!MAVEN_VERSION!/binaries/apache-maven-!MAVEN_VERSION!-bin.zip"

REM Verificar si se está ejecutando como administrador
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo [WARNING] Este script requiere permisos de administrador para algunas operaciones.
    echo [INFO] Intentando ejecutar con permisos elevados...
    powershell -Command "Start-Process '%~f0' -Verb RunAs"
    exit /b
)

echo [INFO] Ejecutando con permisos de administrador...
echo.

REM Función para verificar si Java 17 está instalado
:check_java
echo [INFO] Verificando instalación de Java 17...
java -version 2>nul | findstr "17\." >nul
if %errorLevel% equ 0 (
    echo [SUCCESS] Java 17 ya está instalado
    goto check_maven
) else (
    echo [INFO] Java 17 no encontrado. Procediendo con la instalación...
    goto install_java
)

:install_java
echo [INFO] Instalando Java 17...
echo [INFO] Descargando Java 17 desde Oracle...

REM Crear directorio temporal
if not exist "%TEMP%\ml-setup" mkdir "%TEMP%\ml-setup"

REM Descargar Java usando PowerShell
powershell -Command ^
    "$ProgressPreference = 'SilentlyContinue'; " ^
    "try { " ^
        "Invoke-WebRequest -Uri '%JAVA_DOWNLOAD_URL%' -OutFile '%TEMP%\ml-setup\jdk-17-installer.exe' -UseBasicParsing; " ^
        "Write-Host '[SUCCESS] Java 17 descargado correctamente'; " ^
    "} catch { " ^
        "Write-Host '[ERROR] Error al descargar Java 17. Verifique su conexión a internet.'; " ^
        "exit 1; " ^
    "}"

if %errorLevel% neq 0 (
    echo [ERROR] Error al descargar Java 17
    goto manual_java_install
)

echo [INFO] Ejecutando instalador de Java 17...
echo [INFO] Siga las instrucciones del instalador...
start /wait "%TEMP%\ml-setup\jdk-17-installer.exe"

echo [INFO] Configurando variables de entorno para Java...

REM Buscar la instalación de Java 17
for /d %%i in ("C:\Program Files\Java\jdk-17*") do set "JAVA_HOME=%%i"
if not defined JAVA_HOME (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set "JAVA_HOME=%%i"
)
if not defined JAVA_HOME (
    for /d %%i in ("C:\Program Files\Eclipse Foundation\jdk-17*") do set "JAVA_HOME=%%i"
)

if defined JAVA_HOME (
    echo [INFO] Java encontrado en: !JAVA_HOME!
    setx JAVA_HOME "!JAVA_HOME!" /M
    setx PATH "%PATH%;!JAVA_HOME!\bin" /M
    echo [SUCCESS] Variables de entorno configuradas
) else (
    goto manual_java_config
)

goto check_maven

:manual_java_install
echo.
echo [WARNING] No se pudo descargar Java automáticamente.
echo [INFO] Por favor, instale Java 17 manualmente:
echo [INFO] 1. Visite: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
echo [INFO] 2. Descargue e instale JDK 17 para Windows x64
echo [INFO] 3. Ejecute este script nuevamente
echo.
pause
exit /b 1

:manual_java_config
echo.
echo [WARNING] No se pudo configurar JAVA_HOME automáticamente.
echo [INFO] Por favor, configure las variables de entorno manualmente:
echo [INFO] 1. JAVA_HOME = Ruta de instalación de Java (ej: C:\Program Files\Java\jdk-17)
echo [INFO] 2. Agregue %%JAVA_HOME%%\bin al PATH
echo [INFO] 3. Ejecute este script nuevamente
echo.
pause
exit /b 1

:check_maven
echo [INFO] Verificando instalación de Maven...
mvn -version 2>nul | findstr "Apache Maven" >nul
if %errorLevel% equ 0 (
    echo [SUCCESS] Maven ya está instalado
    goto install_dependencies
) else (
    echo [INFO] Maven no encontrado. Procediendo con la instalación...
    goto install_maven
)

:install_maven
echo [INFO] Instalando Apache Maven %MAVEN_VERSION%...

REM Crear directorio para Maven
if not exist "C:\Program Files\Apache" mkdir "C:\Program Files\Apache"

echo [INFO] Descargando Maven...
powershell -Command ^
    "$ProgressPreference = 'SilentlyContinue'; " ^
    "try { " ^
        "Invoke-WebRequest -Uri '%MAVEN_DOWNLOAD_URL%' -OutFile '%TEMP%\ml-setup\maven.zip' -UseBasicParsing; " ^
        "Write-Host '[SUCCESS] Maven descargado correctamente'; " ^
    "} catch { " ^
        "Write-Host '[ERROR] Error al descargar Maven. Verifique su conexión a internet.'; " ^
        "exit 1; " ^
    "}"

if %errorLevel% neq 0 (
    echo [ERROR] Error al descargar Maven
    goto manual_maven_install
)

echo [INFO] Extrayendo Maven...
powershell -Command ^
    "try { " ^
        "Expand-Archive -Path '%TEMP%\ml-setup\maven.zip' -DestinationPath 'C:\Program Files\Apache\' -Force; " ^
        "Write-Host '[SUCCESS] Maven extraído correctamente'; " ^
    "} catch { " ^
        "Write-Host '[ERROR] Error al extraer Maven'; " ^
        "exit 1; " ^
    "}"

if %errorLevel% neq 0 (
    echo [ERROR] Error al extraer Maven
    goto manual_maven_install
)

echo [INFO] Configurando variables de entorno para Maven...
set "MAVEN_HOME=C:\Program Files\Apache\apache-maven-%MAVEN_VERSION%"
setx MAVEN_HOME "%MAVEN_HOME%" /M
setx PATH "%PATH%;%MAVEN_HOME%\bin" /M

echo [SUCCESS] Maven instalado y configurado correctamente
goto install_dependencies

:manual_maven_install
echo.
echo [WARNING] No se pudo instalar Maven automáticamente.
echo [INFO] Por favor, instale Maven manualmente:
echo [INFO] 1. Visite: https://maven.apache.org/download.cgi
echo [INFO] 2. Descargue el archivo Binary zip archive
echo [INFO] 3. Extraiga en C:\Program Files\Apache\
echo [INFO] 4. Configure MAVEN_HOME y agregue al PATH
echo [INFO] 5. Ejecute este script nuevamente
echo.
pause
exit /b 1

:install_dependencies
echo [INFO] Instalando dependencias del proyecto...

if not exist "pom.xml" (
    echo [ERROR] No se encontró el archivo pom.xml en el directorio actual
    echo [WARNING] Asegúrese de ejecutar este script desde el directorio raíz del proyecto
    pause
    exit /b 1
)

REM Refrescar variables de entorno para la sesión actual
call refreshenv >nul 2>&1

mvn clean install
if %errorLevel% equ 0 (
    echo [SUCCESS] Dependencias del proyecto instaladas correctamente
) else (
    echo [ERROR] Error al instalar las dependencias del proyecto
    echo [INFO] Verifique que Java y Maven estén correctamente configurados
    goto verify_installation
)

:verify_installation
echo.
echo [INFO] Verificando instalación...
echo.
echo ============================
echo  Resumen de la instalación
echo ============================

REM Verificar Java
java -version 2>nul | findstr "17\." >nul
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Java 17 instalado correctamente
    java -version 2>&1 | findstr "version"
) else (
    echo [ERROR] ✗ Java 17 no está correctamente instalado
)

REM Verificar Maven
mvn -version 2>nul | findstr "Apache Maven" >nul
if %errorLevel% equ 0 (
    echo [SUCCESS] ✓ Maven instalado correctamente
    mvn -version 2>&1 | findstr "Apache Maven"
) else (
    echo [ERROR] ✗ Maven no está correctamente instalado
)

REM Verificar JAVA_HOME
if defined JAVA_HOME (
    echo [SUCCESS] ✓ JAVA_HOME configurado: %JAVA_HOME%
) else (
    echo [ERROR] ✗ JAVA_HOME no está configurado
)

echo.
echo [SUCCESS] ¡Instalación completada!
echo.
echo Para comenzar a desarrollar:
echo 1. Cierre y abra una nueva ventana de comandos
echo 2. Navegue al directorio del proyecto
echo 3. Ejecute: mvn spring-boot:run
echo 4. Visite: http://localhost:8080/swagger-ui.html
echo.

REM Limpiar archivos temporales
if exist "%TEMP%\ml-setup" (
    rmdir /s /q "%TEMP%\ml-setup"
)

echo Presione cualquier tecla para continuar...
pause >nul
