#!/bin/bash

# Script para desarrollo local con hot-reload
# Este script levanta los servicios de base de datos en Docker
# pero permite ejecutar el frontend y backend en modo desarrollo

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}"
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

# Verificar dependencias para desarrollo
check_dev_dependencies() {
    print_header "Verificando dependencias de desarrollo"
    
    # Verificar Node.js
    if ! command -v node &> /dev/null; then
        print_error "Node.js no está instalado. Por favor instálalo desde https://nodejs.org/"
        exit 1
    fi
    print_success "Node.js está instalado ($(node --version))"
    
    # Verificar npm
    if ! command -v npm &> /dev/null; then
        print_error "npm no está instalado"
        exit 1
    fi
    print_success "npm está instalado ($(npm --version))"
    
    # Verificar Java
    if ! command -v java &> /dev/null; then
        print_error "Java no está instalado. Necesitas Java 17 o superior"
        exit 1
    fi
    print_success "Java está instalado ($(java --version | head -n 1))"
    
    # Verificar Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven no está instalado"
        exit 1
    fi
    print_success "Maven está instalado ($(mvn --version | head -n 1))"
}

# Iniciar solo servicios de infraestructura (BD, Redis)
start_infrastructure() {
    print_header "Iniciando servicios de infraestructura"
    
    # Crear archivo de configuración si no existe
    if [ ! -f .env ]; then
        cp .env.example .env
        print_success "Archivo .env creado"
    fi
    
    # Iniciar solo base de datos y Redis
    print_info "Iniciando PostgreSQL y Redis..."
    docker-compose up -d ml-database ml-redis
    
    # Esperar a que estén listos
    print_info "Esperando que PostgreSQL esté listo..."
    local max_attempts=30
    local attempt=1
    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T ml-database pg_isready -U mluser -d mlcoreplatform &> /dev/null; then
            print_success "PostgreSQL está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_error "PostgreSQL no responde"
            exit 1
        fi
        sleep 2
        ((attempt++))
    done
    
    print_info "Verificando Redis..."
    attempt=1
    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T ml-redis redis-cli ping &> /dev/null; then
            print_success "Redis está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_error "Redis no responde"
            exit 1
        fi
        sleep 2
        ((attempt++))
    done
}

# Instalar dependencias del frontend
setup_frontend() {
    print_header "Configurando frontend (React + Vite)"
    
    cd ml_item_product
    
    # Instalar dependencias si no existen
    if [ ! -d "node_modules" ]; then
        print_info "Instalando dependencias del frontend..."
        npm install
        print_success "Dependencias del frontend instaladas"
    else
        print_info "Dependencias del frontend ya están instaladas"
    fi
    
    # Crear .env local para desarrollo
    if [ ! -f .env.local ]; then
        cat > .env.local << EOF
VITE_API_URL=http://localhost:8080
VITE_ENV=development
VITE_REGION=colombia
VITE_USE_MOCK_DATA=false
EOF
        print_success "Archivo .env.local creado para desarrollo"
    fi
    
    cd ..
}

# Configurar backend para desarrollo local
setup_backend() {
    print_header "Configurando backend (Spring Boot)"
    
    cd ml-core-platform
    
    # Crear application-local.properties si no existe
    local app_props="src/main/resources/application-local.properties"
    if [ ! -f "$app_props" ]; then
        mkdir -p "$(dirname "$app_props")"
        cat > "$app_props" << EOF
# Configuración para desarrollo local
server.port=8080

# Base de datos PostgreSQL (Docker)
spring.datasource.url=jdbc:postgresql://localhost:5432/mlcoreplatform
spring.datasource.username=mluser
spring.datasource.password=mlpassword
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Redis (Docker)
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000ms

# Logging para desarrollo
logging.level.com.mercadolibre=DEBUG
logging.level.org.springframework.web=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Actuator endpoints
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always

# CORS para desarrollo
spring.web.cors.allowed-origins=http://localhost:3000
spring.web.cors.allowed-methods=*
spring.web.cors.allowed-headers=*
EOF
        print_success "Archivo application-local.properties creado"
    fi
    
    cd ..
}

# Mostrar instrucciones para iniciar los servicios
show_dev_instructions() {
    print_header "🎯 Entorno de desarrollo configurado"
    
    echo ""
    echo -e "${GREEN}📍 Servicios de infraestructura corriendo:${NC}"
    echo -e "   ${BLUE}PostgreSQL:${NC} localhost:5432 (mluser/mlpassword)"
    echo -e "   ${BLUE}Redis:${NC}      localhost:6379"
    echo ""
    echo -e "${YELLOW}🚀 Para iniciar el desarrollo:${NC}"
    echo ""
    echo -e "${BLUE}1. Backend (Spring Boot):${NC}"
    echo -e "   cd ml-core-platform"
    echo -e "   mvn spring-boot:run -Dspring-boot.run.profiles=local"
    echo -e "   ${GREEN}➜ Disponible en: http://localhost:8080${NC}"
    echo ""
    echo -e "${BLUE}2. Frontend (React + Vite) - EN OTRA TERMINAL:${NC}"
    echo -e "   cd ml_item_product"
    echo -e "   npm run dev"
    echo -e "   ${GREEN}➜ Disponible en: http://localhost:5173${NC}"
    echo ""
    echo -e "${GREEN}🔧 Comandos útiles:${NC}"
    echo -e "   ${YELLOW}Ver logs de BD:${NC}       docker-compose logs -f ml-database"
    echo -e "   ${YELLOW}Ver logs de Redis:${NC}    docker-compose logs -f ml-redis"
    echo -e "   ${YELLOW}Parar infraestructura:${NC} docker-compose stop ml-database ml-redis"
    echo -e "   ${YELLOW}Reiniciar BD:${NC}         docker-compose restart ml-database"
    echo -e "   ${YELLOW}Acceder a BD:${NC}         docker-compose exec ml-database psql -U mluser -d mlcoreplatform"
    echo ""
    echo -e "${GREEN}💡 Ventajas del modo desarrollo:${NC}"
    echo -e "   ✨ Hot-reload automático en frontend y backend"
    echo -e "   🐛 Debugging más fácil"
    echo -e "   🔍 Logs detallados"
    echo -e "   ⚡ Compilación más rápida"
    echo ""
}

# Función principal
main() {
    print_header "🔧 Configuración del entorno de desarrollo"
    
    check_dev_dependencies
    start_infrastructure
    setup_frontend
    setup_backend
    show_dev_instructions
    
    print_success "¡Entorno de desarrollo listo! 🎉"
}

# Manejo de interrupciones
trap 'print_error "Setup interrumpido"; exit 1' INT

# Ejecutar función principal
main "$@"
