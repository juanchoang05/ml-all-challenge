#!/bin/bash

# Script para verificar que el entorno local esté funcionando correctamente
# Realiza checks de salud de todos los servicios

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}════════════════════════════════════════════${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}════════════════════════════════════════════${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Variables para tracking
ISSUES_FOUND=0

# Función para reportar problemas
report_issue() {
    print_error "$1"
    ((ISSUES_FOUND++))
}

# Verificar Docker
check_docker() {
    print_info "Verificando Docker..."
    
    if ! command -v docker &> /dev/null; then
        report_issue "Docker no está instalado"
        return 1
    fi
    
    if ! docker info &> /dev/null; then
        report_issue "Docker no está corriendo"
        return 1
    fi
    
    print_success "Docker está funcionando"
}

# Verificar servicios Docker
check_docker_services() {
    print_info "Verificando servicios Docker..."
    
    local services=("ml-item-product-app" "ml-core-platform-app" "ml-core-platform-db" "ml-core-platform-redis")
    
    for service in "${services[@]}"; do
        if docker ps --format "table {{.Names}}" | grep -q "$service"; then
            print_success "Servicio $service está corriendo"
        else
            print_warning "Servicio $service no está corriendo"
        fi
    done
}

# Verificar conectividad de puertos
check_ports() {
    print_info "Verificando puertos..."
    
    local ports=(3000 8080 5432 6379)
    local services=("Frontend" "Backend" "PostgreSQL" "Redis")
    
    for i in "${!ports[@]}"; do
        local port=${ports[$i]}
        local service=${services[$i]}
        
        if nc -z localhost $port 2>/dev/null; then
            print_success "$service responde en puerto $port"
        else
            report_issue "$service no responde en puerto $port"
        fi
    done
}

# Verificar health endpoints
check_health_endpoints() {
    print_info "Verificando endpoints de salud..."
    
    # Backend health check
    if curl -s -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        local health_status=$(curl -s http://localhost:8080/actuator/health | jq -r '.status' 2>/dev/null || echo "unknown")
        if [ "$health_status" = "UP" ]; then
            print_success "Backend health check: UP"
        else
            print_warning "Backend health check: $health_status"
        fi
    else
        report_issue "Backend health endpoint no responde"
    fi
    
    # Frontend basic check
    if curl -s -f http://localhost:3000 > /dev/null 2>&1; then
        print_success "Frontend responde correctamente"
    else
        report_issue "Frontend no responde"
    fi
}

# Verificar base de datos
check_database() {
    print_info "Verificando base de datos..."
    
    if docker-compose exec -T ml-database pg_isready -U mluser -d mlcoreplatform > /dev/null 2>&1; then
        print_success "PostgreSQL está funcionando"
        
        # Verificar conexión desde el backend
        local db_test=$(curl -s http://localhost:8080/actuator/health 2>/dev/null | jq -r '.components.db.status' 2>/dev/null || echo "unknown")
        if [ "$db_test" = "UP" ]; then
            print_success "Conexión backend-database: OK"
        else
            print_warning "Conexión backend-database: $db_test"
        fi
    else
        report_issue "PostgreSQL no está funcionando"
    fi
}

# Verificar Redis
check_redis() {
    print_info "Verificando Redis..."
    
    if docker-compose exec -T ml-redis redis-cli ping > /dev/null 2>&1; then
        print_success "Redis está funcionando"
        
        # Verificar conexión desde el backend
        local redis_test=$(curl -s http://localhost:8080/actuator/health 2>/dev/null | jq -r '.components.redis.status' 2>/dev/null || echo "unknown")
        if [ "$redis_test" = "UP" ]; then
            print_success "Conexión backend-redis: OK"
        else
            print_warning "Conexión backend-redis: $redis_test"
        fi
    else
        report_issue "Redis no está funcionando"
    fi
}

# Verificar logs para errores
check_logs() {
    print_info "Verificando logs para errores críticos..."
    
    # Verificar logs del backend para errores
    local backend_errors=$(docker-compose logs ml-core-platform 2>/dev/null | grep -i "error\|exception\|failed" | wc -l)
    if [ "$backend_errors" -gt 0 ]; then
        print_warning "Se encontraron $backend_errors errores en logs del backend"
    else
        print_success "No se encontraron errores críticos en backend"
    fi
    
    # Verificar logs del frontend para errores
    local frontend_errors=$(docker-compose logs ml-item-product 2>/dev/null | grep -i "error\|failed" | wc -l)
    if [ "$frontend_errors" -gt 0 ]; then
        print_warning "Se encontraron $frontend_errors errores en logs del frontend"
    else
        print_success "No se encontraron errores críticos en frontend"
    fi
}

# Verificar recursos del sistema
check_system_resources() {
    print_info "Verificando recursos del sistema..."
    
    # Verificar uso de memoria Docker
    local docker_memory=$(docker stats --no-stream --format "table {{.MemUsage}}" | tail -n +2 | head -1 | cut -d'/' -f1 | tr -d ' ')
    print_info "Uso de memoria Docker: $docker_memory"
    
    # Verificar espacio en disco
    local disk_usage=$(df -h . | tail -1 | awk '{print $5}' | sed 's/%//')
    if [ "$disk_usage" -gt 90 ]; then
        print_warning "Uso de disco alto: ${disk_usage}%"
    else
        print_success "Espacio en disco: ${disk_usage}% usado"
    fi
}

# Mostrar resumen de APIs disponibles
show_api_summary() {
    print_info "Verificando APIs disponibles..."
    
    echo -e "${BLUE}📋 Resumen de APIs:${NC}"
    echo ""
    
    # Lista de endpoints importantes
    local endpoints=(
        "GET http://localhost:8080/actuator/health|Health Check"
        "GET http://localhost:8080/actuator/info|Application Info"
        "GET http://localhost:8080/swagger-ui.html|API Documentation"
    )
    
    for endpoint in "${endpoints[@]}"; do
        local url=$(echo $endpoint | cut -d'|' -f1)
        local desc=$(echo $endpoint | cut -d'|' -f2)
        local method=$(echo $url | cut -d' ' -f1)
        local path=$(echo $url | cut -d' ' -f2)
        
        if curl -s -f "$path" > /dev/null 2>&1; then
            echo -e "   ${GREEN}✅ $method $path${NC} - $desc"
        else
            echo -e "   ${RED}❌ $method $path${NC} - $desc"
        fi
    done
}

# Función principal
main() {
    print_header "🔍 Verificación del Entorno Local MercadoLibre"
    
    echo ""
    check_docker
    echo ""
    check_docker_services
    echo ""
    check_ports
    echo ""
    check_health_endpoints
    echo ""
    check_database
    echo ""
    check_redis
    echo ""
    check_logs
    echo ""
    check_system_resources
    echo ""
    show_api_summary
    
    echo ""
    print_header "📊 Resumen de Verificación"
    
    if [ $ISSUES_FOUND -eq 0 ]; then
        print_success "🎉 ¡Todo está funcionando perfectamente!"
        echo ""
        echo -e "${GREEN}🌐 URLs disponibles:${NC}"
        echo -e "   Frontend: http://localhost:3000"
        echo -e "   Backend:  http://localhost:8080"
        echo -e "   Swagger:  http://localhost:8080/swagger-ui.html"
        echo -e "   Health:   http://localhost:8080/actuator/health"
    else
        print_warning "Se encontraron $ISSUES_FOUND problemas"
        echo ""
        echo -e "${YELLOW}💡 Sugerencias:${NC}"
        echo -e "   • Ejecutar: ./setup-local.sh"
        echo -e "   • Verificar Docker: docker-compose ps"
        echo -e "   • Ver logs: docker-compose logs -f"
        echo -e "   • Reiniciar: docker-compose restart"
    fi
    
    echo ""
}

# Ejecutar función principal
main "$@"
