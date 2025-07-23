#!/bin/bash

# Script para cambiar fácilmente entre ambientes y regiones
# Uso: ./scripts/env-switch.sh [ambiente] [region]

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para mostrar ayuda
show_help() {
    echo -e "${BLUE}MercadoLibre Product Page - Environment Switcher${NC}"
    echo ""
    echo "Uso: $0 [ambiente] [region]"
    echo ""
    echo -e "${YELLOW}Ambientes disponibles:${NC}"
    echo "  development  - Desarrollo local con todas las features"
    echo "  production   - Configuración optimizada para producción"
    echo "  testing      - Ambiente para testing con mocks"
    echo "  local        - Desarrollo local con servidor mock"
    echo ""
    echo -e "${YELLOW}Regiones disponibles:${NC}"
    echo "  colombia     - MercadoLibre Colombia (MCO)"
    echo "  argentina    - MercadoLibre Argentina (MLA)"
    echo "  mexico       - MercadoLibre México (MLM)"
    echo "  brazil       - MercadoLibre Brasil (MLB)"
    echo "  chile        - MercadoLibre Chile (MLC)"
    echo ""
    echo -e "${YELLOW}Ejemplos:${NC}"
    echo "  $0 development colombia"
    echo "  $0 testing argentina"
    echo "  $0 production mexico"
    echo ""
}

# Función para crear archivo .env
create_env_file() {
    local environment=$1
    local region=$2
    
    echo -e "${BLUE}Creando archivo .env para ambiente: ${YELLOW}$environment${BLUE}, región: ${YELLOW}$region${NC}"
    
    # Crear archivo .env basado en el ambiente
    case $environment in
        "development")
            cat > .env << EOF
# Ambiente: Development
NODE_ENV=development
REACT_APP_REGION=$region

# API Configuration
REACT_APP_API_BASE_URL=https://api.mercadolibre.com
REACT_APP_API_TIMEOUT=10000

# Features
REACT_APP_ENABLE_LOGGING=true
REACT_APP_ENABLE_CACHE=true
REACT_APP_ENABLE_REVIEWS=true
REACT_APP_ENABLE_QUESTIONS=true
REACT_APP_ENABLE_RECOMMENDATIONS=true

# Development
REACT_APP_USE_MOCK_DATA=false
REACT_APP_MOCK_DELAY=1000
REACT_APP_CACHE_TTL=300000
EOF
            ;;
        "production")
            cat > .env << EOF
# Ambiente: Production
NODE_ENV=production
REACT_APP_REGION=$region

# API Configuration
REACT_APP_API_BASE_URL=https://api.mercadolibre.com
REACT_APP_API_TIMEOUT=15000

# Features
REACT_APP_ENABLE_LOGGING=false
REACT_APP_ENABLE_CACHE=true
REACT_APP_ENABLE_REVIEWS=true
REACT_APP_ENABLE_QUESTIONS=true
REACT_APP_ENABLE_RECOMMENDATIONS=true

# Production
REACT_APP_USE_MOCK_DATA=false
REACT_APP_MOCK_DELAY=0
REACT_APP_CACHE_TTL=600000
EOF
            ;;
        "testing")
            cat > .env << EOF
# Ambiente: Testing
NODE_ENV=testing
REACT_APP_REGION=$region

# API Configuration
REACT_APP_API_BASE_URL=https://api.mercadolibre.com
REACT_APP_API_TIMEOUT=5000

# Features
REACT_APP_ENABLE_LOGGING=true
REACT_APP_ENABLE_CACHE=false
REACT_APP_ENABLE_REVIEWS=true
REACT_APP_ENABLE_QUESTIONS=true
REACT_APP_ENABLE_RECOMMENDATIONS=false

# Testing
REACT_APP_USE_MOCK_DATA=true
REACT_APP_MOCK_DELAY=100
REACT_APP_CACHE_TTL=0
EOF
            ;;
        "local")
            cat > .env << EOF
# Ambiente: Local
NODE_ENV=development
REACT_APP_REGION=$region

# API Configuration
REACT_APP_API_BASE_URL=http://localhost:3001/api
REACT_APP_API_TIMEOUT=8000

# Features
REACT_APP_ENABLE_LOGGING=true
REACT_APP_ENABLE_CACHE=false
REACT_APP_ENABLE_REVIEWS=true
REACT_APP_ENABLE_QUESTIONS=true
REACT_APP_ENABLE_RECOMMENDATIONS=true

# Local Development
REACT_APP_USE_MOCK_DATA=true
REACT_APP_MOCK_DELAY=1000
REACT_APP_CACHE_TTL=0
EOF
            ;;
        *)
            echo -e "${RED}Error: Ambiente '$environment' no reconocido${NC}"
            show_help
            exit 1
            ;;
    esac
    
    echo -e "${GREEN}✓ Archivo .env creado exitosamente${NC}"
}

# Función para mostrar el estado actual
show_current_status() {
    if [ -f .env ]; then
        echo -e "${BLUE}Configuración actual:${NC}"
        echo -e "${YELLOW}NODE_ENV:${NC} $(grep NODE_ENV .env | cut -d '=' -f2)"
        echo -e "${YELLOW}REGION:${NC} $(grep REACT_APP_REGION .env | cut -d '=' -f2)"
        echo -e "${YELLOW}MOCK_DATA:${NC} $(grep REACT_APP_USE_MOCK_DATA .env | cut -d '=' -f2)"
        echo -e "${YELLOW}LOGGING:${NC} $(grep REACT_APP_ENABLE_LOGGING .env | cut -d '=' -f2)"
        echo ""
    else
        echo -e "${YELLOW}No existe archivo .env${NC}"
        echo ""
    fi
}

# Función principal
main() {
    # Mostrar estado actual
    show_current_status
    
    # Verificar parámetros
    if [ $# -eq 0 ]; then
        echo -e "${YELLOW}Selecciona el ambiente y región:${NC}"
        show_help
        exit 0
    fi
    
    if [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
        show_help
        exit 0
    fi
    
    local environment=${1:-development}
    local region=${2:-colombia}
    
    # Validar región
    case $region in
        "colombia"|"argentina"|"mexico"|"brazil"|"chile")
            ;;
        *)
            echo -e "${RED}Error: Región '$region' no válida${NC}"
            echo -e "${YELLOW}Regiones válidas: colombia, argentina, mexico, brazil, chile${NC}"
            exit 1
            ;;
    esac
    
    # Crear backup del .env actual si existe
    if [ -f .env ]; then
        cp .env .env.backup
        echo -e "${YELLOW}Backup creado: .env.backup${NC}"
    fi
    
    # Crear nuevo archivo .env
    create_env_file $environment $region
    
    # Mostrar siguiente paso
    echo ""
    echo -e "${GREEN}🚀 Configuración lista!${NC}"
    echo -e "${BLUE}Para iniciar el desarrollo:${NC}"
    echo "  npm run dev"
    echo ""
    echo -e "${BLUE}Para ver la configuración actual:${NC}"
    echo "  cat .env"
    echo ""
}

# Ejecutar función principal
main "$@"
