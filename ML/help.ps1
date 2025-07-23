# Script de ayuda para el proyecto MercadoLibre en Windows
# Muestra todos los comandos disponibles y su uso

# Colores para output
function Write-Header($message) {
    Write-Host "════════════════════════════════════════════" -ForegroundColor Blue
    Write-Host $message -ForegroundColor Blue
    Write-Host "════════════════════════════════════════════" -ForegroundColor Blue
}

function Write-Section($message) {
    Write-Host "▶ $message" -ForegroundColor Magenta
}

function Write-Command($command, $description) {
    Write-Host "   $command" -ForegroundColor Cyan -NoNewline
    Write-Host " - $description" -ForegroundColor White
}

function Write-URL($url, $description) {
    Write-Host "   $url" -ForegroundColor Green -NoNewline
    Write-Host " - $description" -ForegroundColor White
}

function Main {
    Clear-Host
    Write-Header "🚀 MercadoLibre - Guía de comandos locales (Windows)"
    
    Write-Host ""
    Write-Section "📋 SCRIPTS PRINCIPALES"
    Write-Command ".\setup-local.ps1" "Configuración completa con Docker (recomendado para testing)"
    Write-Command ".\setup-dev.ps1" "Configuración para desarrollo con hot-reload"
    Write-Command ".\start.ps1" "Iniciar aplicación completa (modo Docker)"
    Write-Command ".\stop.ps1" "Detener todos los servicios y limpiar recursos"
    Write-Command ".\check-health.ps1" "Verificar estado del sistema"
    Write-Command ".\help.ps1" "Mostrar esta ayuda"
    
    Write-Host ""
    Write-Section "🐳 COMANDOS DOCKER"
    Write-Command "docker-compose up -d" "Iniciar servicios en background"
    Write-Command "docker-compose up --build" "Reconstruir e iniciar con logs"
    Write-Command "docker-compose down" "Detener servicios"
    Write-Command "docker-compose ps" "Ver estado de servicios"
    Write-Command "docker-compose logs -f [servicio]" "Ver logs de un servicio específico"
    Write-Command "docker-compose restart [servicio]" "Reiniciar un servicio"
    Write-Command "docker-compose exec [servicio] bash" "Entrar a un contenedor"
    
    Write-Host ""
    Write-Section "🌐 URLs DEL SISTEMA"
    Write-URL "http://localhost:3000" "Frontend React (Producto MercadoLibre)"
    Write-URL "http://localhost:5173" "Frontend React (modo desarrollo)"
    Write-URL "http://localhost:8080" "Backend Spring Boot"
    Write-URL "http://localhost:8080/actuator/health" "Health check del backend"
    Write-URL "http://localhost:8080/swagger-ui.html" "Documentación API (Swagger)"
    
    Write-Host ""
    Write-Section "🔧 DESARROLLO LOCAL"
    Write-Host "   Frontend (React + Vite):" -ForegroundColor Yellow
    Write-Command "cd ml_item_product; npm run dev" "Iniciar frontend en modo desarrollo"
    Write-Command "cd ml_item_product; npm run build" "Construir para producción"
    Write-Command "cd ml_item_product; npm test" "Ejecutar tests"
    
    Write-Host ""
    Write-Host "   Backend (Spring Boot):" -ForegroundColor Yellow
    Write-Command "cd ml-core-platform; mvn spring-boot:run" "Iniciar backend"
    Write-Command "cd ml-core-platform; mvn spring-boot:run -Dspring-boot.run.profiles=local" "Iniciar con perfil local"
    Write-Command "cd ml-core-platform; mvn test" "Ejecutar tests"
    Write-Command "cd ml-core-platform; mvn clean package" "Compilar proyecto"
    
    Write-Host ""
    Write-Section "🗄️ BASE DE DATOS"
    Write-Command "docker-compose exec ml-database psql -U mluser -d mlcoreplatform" "Conectar a PostgreSQL"
    Write-Command "docker-compose exec ml-redis redis-cli" "Conectar a Redis"
    
    Write-Host ""
    Write-Section "📊 MONITOREO Y LOGS"
    Write-Command "docker-compose logs -f ml-item-product" "Logs del frontend"
    Write-Command "docker-compose logs -f ml-core-platform" "Logs del backend"
    Write-Command "docker-compose logs -f ml-database" "Logs de PostgreSQL"
    Write-Command "docker-compose logs -f ml-redis" "Logs de Redis"
    
    Write-Host ""
    Write-Section "🔧 TROUBLESHOOTING"
    Write-Command "docker system prune" "Limpiar containers y imágenes no usadas"
    Write-Command "docker-compose down -v" "Detener y eliminar volúmenes"
    Write-Command "docker-compose build --no-cache" "Reconstruir sin cache"
    Write-Command "Get-NetTCPConnection -LocalPort 3000" "Ver qué proceso usa el puerto 3000"
    Write-Command "Get-NetTCPConnection -LocalPort 8080" "Ver qué proceso usa el puerto 8080"
    Write-Command "Stop-Process -Id [PID] -Force" "Matar proceso por PID"
    
    Write-Host ""
    Write-Section "📁 ESTRUCTURA DEL PROYECTO"
    Write-Host "   ml_item_product\      - Frontend React con Vite" -ForegroundColor Cyan
    Write-Host "   ml-core-platform\     - Backend Spring Boot" -ForegroundColor Cyan
    Write-Host "   docker-compose.yml    - Configuración de servicios" -ForegroundColor Cyan
    Write-Host "   .env                  - Variables de entorno" -ForegroundColor Cyan
    Write-Host "   nginx-proxy.conf      - Configuración de Nginx (opcional)" -ForegroundColor Cyan
    
    Write-Host ""
    Write-Section "⚡ FLUJOS DE TRABAJO RECOMENDADOS"
    Write-Host ""
    Write-Host "   🎯 Para desarrollo diario:" -ForegroundColor Green
    Write-Host "   1. .\setup-dev.ps1 (primera vez)"
    Write-Host "   2. cd ml-core-platform; mvn spring-boot:run -Dspring-boot.run.profiles=local"
    Write-Host "   3. En otra terminal: cd ml_item_product; npm run dev"
    Write-Host ""
    Write-Host "   🚀 Para testing completo:" -ForegroundColor Green
    Write-Host "   1. .\setup-local.ps1"
    Write-Host "   2. Abrir http://localhost:3000"
    Write-Host ""
    Write-Host "   🛑 Para detener todo:" -ForegroundColor Green
    Write-Host "   1. .\stop.ps1"
    Write-Host ""
    
    Write-Section "💡 TIPS PARA WINDOWS"
    Write-Host "   • Ejecuta PowerShell como Administrador para la primera configuración"
    Write-Host "   • Configura la política de ejecución: Set-ExecutionPolicy RemoteSigned"
    Write-Host "   • Usa .\setup-dev.ps1 para desarrollo con hot-reload"
    Write-Host "   • Usa .\setup-local.ps1 para testing completo"
    Write-Host "   • Los archivos .env controlan la configuración"
    Write-Host "   • Swagger está disponible en /swagger-ui.html"
    Write-Host "   • Para problemas de puertos usa: Get-NetTCPConnection -LocalPort [puerto]"
    Write-Host ""
    
    Write-Section "🚨 SOLUCIÓN DE PROBLEMAS COMUNES"
    Write-Host ""
    Write-Host "   🔒 Error de política de ejecución:" -ForegroundColor Yellow
    Write-Host "   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser"
    Write-Host ""
    Write-Host "   🐳 Docker no inicia:" -ForegroundColor Yellow
    Write-Host "   - Verificar que Docker Desktop esté corriendo"
    Write-Host "   - Reiniciar Docker Desktop"
    Write-Host "   - Verificar virtualización en BIOS (Hyper-V)"
    Write-Host ""
    Write-Host "   🔌 Puerto ocupado:" -ForegroundColor Yellow
    Write-Host "   Get-NetTCPConnection -LocalPort 8080 | Select OwningProcess"
    Write-Host "   Stop-Process -Id [PID] -Force"
    Write-Host ""
    Write-Host "   🧹 Limpieza completa:" -ForegroundColor Yellow
    Write-Host "   .\stop.ps1 -CleanAll"
    Write-Host ""
    
    Write-Host "🎉 ¡Happy coding en Windows! 🎯" -ForegroundColor Green
    Write-Host ""
}

# Ejecutar función principal
Main
