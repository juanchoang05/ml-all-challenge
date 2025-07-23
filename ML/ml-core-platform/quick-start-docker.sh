#!/bin/bash

# Script de inicio rápido para ML Core Platform con Docker
# Uso: ./quick-start-docker.sh

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

echo ""
print_status "🐳 ML Core Platform - Inicio Rápido con Docker"
echo ""

# Verificar que Docker esté instalado y ejecutándose
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado"
    print_status "Ejecute: ./install-docker-mac.sh (macOS) o install-docker-windows.bat (Windows)"
    exit 1
fi

if ! docker system info &> /dev/null; then
    print_error "Docker no está ejecutándose"
    print_status "Inicie Docker Desktop y ejecute este script nuevamente"
    exit 1
fi

print_success "Docker está instalado y ejecutándose"

# Verificar que docker-compose esté disponible
if command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
elif docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
else
    print_error "Docker Compose no está disponible"
    exit 1
fi

print_success "Docker Compose encontrado: $COMPOSE_CMD"

# Limpiar contenedores previos si existen
print_status "Limpiando contenedores previos..."
$COMPOSE_CMD down -v --remove-orphans 2>/dev/null || true

# Construir y ejecutar servicios
print_status "Construyendo y ejecutando servicios..."
$COMPOSE_CMD up -d --build

# Esperar a que los servicios estén listos
print_status "Esperando a que los servicios inicien..."

# Función para verificar si un servicio está listo
check_service() {
    local service=$1
    local url=$2
    local max_attempts=30
    local attempt=1

    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            return 0
        fi
        echo -n "."
        sleep 2
        ((attempt++))
    done
    return 1
}

# Verificar base de datos
print_status "Verificando base de datos PostgreSQL..."
if $COMPOSE_CMD exec -T ml-database pg_isready -U mluser -d mlcoreplatform &> /dev/null; then
    print_success "✅ Base de datos PostgreSQL lista"
else
    print_warning "⚠️  Base de datos PostgreSQL no responde"
fi

# Verificar Redis
print_status "Verificando Redis..."
if $COMPOSE_CMD exec -T ml-redis redis-cli ping &> /dev/null; then
    print_success "✅ Redis listo"
else
    print_warning "⚠️  Redis no responde"
fi

# Verificar aplicación
print_status "Verificando aplicación Spring Boot..."
if check_service "ml-core-platform" "http://localhost:8080/actuator/health"; then
    print_success "✅ Aplicación Spring Boot lista"
else
    print_warning "⚠️  Aplicación Spring Boot no responde"
    print_status "Verificando logs..."
    $COMPOSE_CMD logs --tail=10 ml-core-platform
fi

# Mostrar estado de todos los servicios
echo ""
print_status "Estado de los servicios:"
$COMPOSE_CMD ps

# Mostrar URLs de acceso
echo ""
print_success "🎉 ¡ML Core Platform está ejecutándose!"
echo ""
echo "📋 URLs de acceso:"
echo "   🌐 Aplicación:     http://localhost:8080"
echo "   📚 Swagger UI:     http://localhost:8080/swagger-ui.html"
echo "   ❤️  Health Check:  http://localhost:8080/actuator/health"
echo "   🗄️  PostgreSQL:    localhost:5432 (mluser/mlpassword)"
echo "   🔄 Redis:          localhost:6379"
echo ""
echo "📊 Comandos útiles:"
echo "   Ver logs:          $COMPOSE_CMD logs -f ml-core-platform"
echo "   Detener servicios: $COMPOSE_CMD down"
echo "   Reiniciar:         $COMPOSE_CMD restart ml-core-platform"
echo ""

# Abrir navegador automáticamente (macOS)
if [[ "$OSTYPE" == "darwin"* ]]; then
    print_status "Abriendo Swagger UI en el navegador..."
    sleep 3
    open "http://localhost:8080/swagger-ui.html" 2>/dev/null || true
fi
