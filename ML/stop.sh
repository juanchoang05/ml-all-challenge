#!/bin/bash

# Script para parar la aplicación ML completa
# Incluye opciones para desarrollo y limpieza completa

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

echo -e "${RED}🛑 Deteniendo aplicación MercadoLibre...${NC}"

# Verificar qué servicios están corriendo
if docker-compose ps -q | grep -q .; then
    print_info "Parando servicios Docker..."
    docker-compose down --remove-orphans
    print_success "Servicios Docker detenidos"
else
    print_info "No hay servicios Docker corriendo"
fi

# Verificar procesos locales de desarrollo
print_info "Verificando procesos locales..."

# Buscar procesos de Spring Boot en puerto 8080
SPRING_PID=$(lsof -ti:8080 2>/dev/null || true)
if [ ! -z "$SPRING_PID" ]; then
    print_warning "Encontrado proceso Spring Boot en puerto 8080 (PID: $SPRING_PID)"
    read -p "¿Deseas detenerlo? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        kill $SPRING_PID 2>/dev/null || true
        print_success "Proceso Spring Boot detenido"
    fi
fi

# Buscar procesos de Vite en puerto 5173
VITE_PID=$(lsof -ti:5173 2>/dev/null || true)
if [ ! -z "$VITE_PID" ]; then
    print_warning "Encontrado proceso Vite en puerto 5173 (PID: $VITE_PID)"
    read -p "¿Deseas detenerlo? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        kill $VITE_PID 2>/dev/null || true
        print_success "Proceso Vite detenido"
    fi
fi

print_success "Aplicación detenida"

echo ""
echo -e "${YELLOW}🔧 Opciones de limpieza:${NC}"

# Opcional: limpiar volúmenes
read -p "¿Deseas eliminar los volúmenes de datos (BD, Redis)? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_info "Eliminando volúmenes..."
    docker-compose down -v 2>/dev/null || true
    docker volume prune -f 2>/dev/null || true
    print_success "Volúmenes eliminados"
fi

# Opcional: limpiar imágenes
read -p "¿Deseas eliminar las imágenes Docker construidas? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_info "Eliminando imágenes..."
    docker-compose down --rmi all 2>/dev/null || true
    print_success "Imágenes eliminadas"
fi

# Opcional: limpieza completa
read -p "¿Deseas hacer una limpieza completa de Docker? (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_warning "Realizando limpieza completa de Docker..."
    docker system prune -a -f 2>/dev/null || true
    print_success "Limpieza completa realizada"
fi

echo ""
print_success "🏁 Proceso completado"
echo -e "${BLUE}💡 Para volver a iniciar:${NC}"
echo -e "   ./setup-local.sh  (entorno completo Docker)"
echo -e "   ./setup-dev.sh    (entorno de desarrollo)"
