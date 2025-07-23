#!/bin/bash

# Script de ayuda para el proyecto MercadoLibre
# Muestra todos los comandos disponibles y su uso

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}════════════════════════════════════════════${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}════════════════════════════════════════════${NC}"
}

print_section() {
    echo -e "${PURPLE}▶ $1${NC}"
}

print_command() {
    echo -e "   ${CYAN}$1${NC} - $2"
}

print_url() {
    echo -e "   ${GREEN}$1${NC} - $2"
}

main() {
    clear
    print_header "🚀 MercadoLibre - Guía de comandos locales"
    
    echo ""
    print_section "📋 SCRIPTS PRINCIPALES"
    print_command "./setup-local.sh" "Configuración completa con Docker (recomendado para testing)"
    print_command "./setup-dev.sh" "Configuración para desarrollo con hot-reload"
    print_command "./start.sh" "Iniciar aplicación completa (modo Docker)"
    print_command "./dev.sh" "Iniciar en modo desarrollo con logs visibles"
    print_command "./stop.sh" "Detener todos los servicios y limpiar recursos"
    print_command "./help.sh" "Mostrar esta ayuda"
    
    echo ""
    print_section "🐳 COMANDOS DOCKER"
    print_command "docker-compose up -d" "Iniciar servicios en background"
    print_command "docker-compose up --build" "Reconstruir e iniciar con logs"
    print_command "docker-compose down" "Detener servicios"
    print_command "docker-compose ps" "Ver estado de servicios"
    print_command "docker-compose logs -f [servicio]" "Ver logs de un servicio específico"
    print_command "docker-compose restart [servicio]" "Reiniciar un servicio"
    print_command "docker-compose exec [servicio] bash" "Entrar a un contenedor"
    
    echo ""
    print_section "🌐 URLs DEL SISTEMA"
    print_url "http://localhost:3000" "Frontend React (Producto MercadoLibre)"
    print_url "http://localhost:5173" "Frontend React (modo desarrollo)"
    print_url "http://localhost:8080" "Backend Spring Boot"
    print_url "http://localhost:8080/actuator/health" "Health check del backend"
    print_url "http://localhost:8080/swagger-ui.html" "Documentación API (Swagger)"
    
    echo ""
    print_section "🔧 DESARROLLO LOCAL"
    echo -e "   ${YELLOW}Frontend (React + Vite):${NC}"
    print_command "cd ml_item_product && npm run dev" "Iniciar frontend en modo desarrollo"
    print_command "cd ml_item_product && npm run build" "Construir para producción"
    print_command "cd ml_item_product && npm test" "Ejecutar tests"
    
    echo ""
    echo -e "   ${YELLOW}Backend (Spring Boot):${NC}"
    print_command "cd ml-core-platform && mvn spring-boot:run" "Iniciar backend"
    print_command "cd ml-core-platform && mvn spring-boot:run -Dspring-boot.run.profiles=local" "Iniciar con perfil local"
    print_command "cd ml-core-platform && mvn test" "Ejecutar tests"
    print_command "cd ml-core-platform && mvn clean package" "Compilar proyecto"
    
    echo ""
    print_section "🗄️ BASE DE DATOS"
    print_command "docker-compose exec ml-database psql -U mluser -d mlcoreplatform" "Conectar a PostgreSQL"
    print_command "docker-compose exec ml-redis redis-cli" "Conectar a Redis"
    
    echo ""
    print_section "📊 MONITOREO Y LOGS"
    print_command "docker-compose logs -f ml-item-product" "Logs del frontend"
    print_command "docker-compose logs -f ml-core-platform" "Logs del backend"
    print_command "docker-compose logs -f ml-database" "Logs de PostgreSQL"
    print_command "docker-compose logs -f ml-redis" "Logs de Redis"
    
    echo ""
    print_section "🔧 TROUBLESHOOTING"
    print_command "docker system prune" "Limpiar containers y imágenes no usadas"
    print_command "docker-compose down -v" "Detener y eliminar volúmenes"
    print_command "docker-compose build --no-cache" "Reconstruir sin cache"
    print_command "lsof -i :3000" "Ver qué proceso usa el puerto 3000"
    print_command "lsof -i :8080" "Ver qué proceso usa el puerto 8080"
    
    echo ""
    print_section "📁 ESTRUCTURA DEL PROYECTO"
    echo -e "   ${CYAN}ml_item_product/${NC}      - Frontend React con Vite"
    echo -e "   ${CYAN}ml-core-platform/${NC}     - Backend Spring Boot"
    echo -e "   ${CYAN}docker-compose.yml${NC}    - Configuración de servicios"
    echo -e "   ${CYAN}.env${NC}                  - Variables de entorno"
    echo -e "   ${CYAN}nginx-proxy.conf${NC}      - Configuración de Nginx (opcional)"
    
    echo ""
    print_section "⚡ FLUJOS DE TRABAJO RECOMENDADOS"
    echo ""
    echo -e "   ${GREEN}🎯 Para desarrollo diario:${NC}"
    echo -e "   1. ./setup-dev.sh (primera vez)"
    echo -e "   2. cd ml-core-platform && mvn spring-boot:run -Dspring-boot.run.profiles=local"
    echo -e "   3. En otra terminal: cd ml_item_product && npm run dev"
    echo ""
    echo -e "   ${GREEN}🚀 Para testing completo:${NC}"
    echo -e "   1. ./setup-local.sh"
    echo -e "   2. Abrir http://localhost:3000"
    echo ""
    echo -e "   ${GREEN}🛑 Para detener todo:${NC}"
    echo -e "   1. ./stop.sh"
    echo ""
    
    print_section "💡 TIPS"
    echo -e "   • Usa ${CYAN}./setup-dev.sh${NC} para desarrollo con hot-reload"
    echo -e "   • Usa ${CYAN}./setup-local.sh${NC} para testing completo"
    echo -e "   • Los archivos .env controlan la configuración"
    echo -e "   • Swagger está disponible en /swagger-ui.html"
    echo -e "   • Los logs se guardan en volúmenes Docker persistentes"
    echo ""
    
    echo -e "${GREEN}🎉 ¡Happy coding! 🎯${NC}"
    echo ""
}

main "$@"
