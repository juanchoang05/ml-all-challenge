#!/bin/bash

# Script de instalación de Docker para macOS
# ML Core Platform - MercadoLibre

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
print_status "=== ML Core Platform - Instalador de Docker para macOS ==="
echo ""

# Verificar si Docker está instalado
check_docker() {
    if command -v docker &> /dev/null; then
        print_success "Docker ya está instalado"
        docker --version
        return 0
    else
        return 1
    fi
}

# Verificar si Docker Compose está instalado
check_docker_compose() {
    if command -v docker-compose &> /dev/null || docker compose version &> /dev/null; then
        print_success "Docker Compose ya está instalado"
        if command -v docker-compose &> /dev/null; then
            docker-compose --version
        else
            docker compose version
        fi
        return 0
    else
        return 1
    fi
}

# Instalar Docker Desktop
install_docker() {
    print_status "Instalando Docker Desktop para macOS..."
    
    # Verificar si Homebrew está disponible
    if command -v brew &> /dev/null; then
        print_status "Instalando Docker Desktop usando Homebrew..."
        brew install --cask docker
        print_success "Docker Desktop instalado"
    else
        print_warning "Homebrew no encontrado. Instalación manual requerida."
        print_status "Por favor:"
        print_status "1. Visite: https://www.docker.com/products/docker-desktop/"
        print_status "2. Descargue Docker Desktop para Mac"
        print_status "3. Instale y ejecute Docker Desktop"
        print_status "4. Ejecute este script nuevamente"
        exit 1
    fi
}

# Iniciar Docker Desktop
start_docker() {
    print_status "Iniciando Docker Desktop..."
    open /Applications/Docker.app
    
    print_status "Esperando a que Docker Desktop inicie..."
    local timeout=60
    local count=0
    
    while ! docker system info &> /dev/null; do
        if [ $count -eq $timeout ]; then
            print_error "Timeout esperando a Docker Desktop"
            print_warning "Por favor, inicie Docker Desktop manualmente y ejecute este script nuevamente"
            exit 1
        fi
        
        echo -n "."
        sleep 2
        ((count++))
    done
    
    echo ""
    print_success "Docker Desktop está ejecutándose"
}

# Verificar la instalación
verify_docker() {
    print_status "Verificando instalación de Docker..."
    
    # Verificar Docker
    if docker --version &> /dev/null; then
        print_success "✅ Docker: $(docker --version)"
    else
        print_error "❌ Docker no está funcionando correctamente"
        return 1
    fi
    
    # Verificar Docker Compose
    if docker compose version &> /dev/null; then
        print_success "✅ Docker Compose: $(docker compose version --short)"
    elif docker-compose --version &> /dev/null; then
        print_success "✅ Docker Compose: $(docker-compose --version)"
    else
        print_error "❌ Docker Compose no está funcionando correctamente"
        return 1
    fi
    
    # Test básico
    print_status "Ejecutando test básico de Docker..."
    if docker run --rm hello-world &> /dev/null; then
        print_success "✅ Test de Docker exitoso"
    else
        print_error "❌ Test de Docker falló"
        return 1
    fi
}

# Función principal
main() {
    if check_docker && check_docker_compose; then
        print_success "Docker y Docker Compose ya están instalados y funcionando"
        verify_docker
    else
        if ! check_docker; then
            install_docker
        fi
        
        start_docker
        verify_docker
    fi
    
    echo ""
    print_success "🎉 ¡Docker instalado y configurado correctamente!"
    echo ""
    print_status "Para usar Docker con ML Core Platform:"
    print_status "1. cd al directorio del proyecto"
    print_status "2. docker-compose up -d"
    print_status "3. Visita: http://localhost:8080/swagger-ui.html"
    echo ""
}

main
