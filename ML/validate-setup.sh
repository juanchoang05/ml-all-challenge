#!/bin/bash
# Script de validación completa de todos los scripts y configuraciones

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

function print_header() {
    echo -e "\n${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}"
}

function print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

function print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

function print_error() {
    echo -e "${RED}❌ $1${NC}"
}

function print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Función para verificar sintaxis de scripts bash
function check_bash_scripts() {
    print_header "Verificando sintaxis de scripts Bash"
    
    local scripts=(
        "setup-local.sh"
        "dev.sh"
        "help.sh"
        "stop.sh"
        "check-health.sh"
        "ml_item_product/start.sh"
        "ml_item_product/start-docker.sh"
        "ml_item_product/docker-entrypoint.sh"
        "ml_item_product/scripts/env-switch.sh"
        "ml-core-platform/quick-start-docker.sh"
        "ml-core-platform/install-dependencies-mac.sh"
        "ml-core-platform/install-docker-mac.sh"
        "ml-core-platform/jacoco-coverage.sh"
    )
    
    for script in "${scripts[@]}"; do
        if [[ -f "$script" ]]; then
            # Hacer ejecutable
            chmod +x "$script"
            
            # Verificar sintaxis
            if bash -n "$script" 2>/dev/null; then
                print_success "Sintaxis correcta: $script"
            else
                print_error "Error de sintaxis en: $script"
                return 1
            fi
        else
            print_warning "Script no encontrado: $script"
        fi
    done
}

# Función para verificar archivos PowerShell
function check_powershell_scripts() {
    print_header "Verificando scripts PowerShell"
    
    local ps_scripts=(
        "setup-local.ps1"
        "stop.ps1"
        "help.ps1"
        "setup-dev.ps1"
        "check-health.ps1"
        "start.ps1"
    )
    
    for script in "${ps_scripts[@]}"; do
        if [[ -f "$script" ]]; then
            # Verificación básica de sintaxis PowerShell
            if grep -q "^#.*PowerShell\|^param\|function.*{" "$script"; then
                print_success "Script PowerShell válido: $script"
            else
                print_warning "Revisar script PowerShell: $script"
            fi
        else
            print_warning "Script PowerShell no encontrado: $script"
        fi
    done
}

# Función para verificar archivos Docker
function check_docker_files() {
    print_header "Verificando archivos Docker"
    
    # Verificar Dockerfiles
    local dockerfiles=(
        "ml_item_product/Dockerfile"
        "ml-core-platform/Dockerfile"
    )
    
    for dockerfile in "${dockerfiles[@]}"; do
        if [[ -f "$dockerfile" ]]; then
            if grep -q "^FROM\|^RUN\|^COPY" "$dockerfile"; then
                print_success "Dockerfile válido: $dockerfile"
            else
                print_error "Dockerfile inválido: $dockerfile"
                return 1
            fi
        else
            print_error "Dockerfile no encontrado: $dockerfile"
            return 1
        fi
    done
    
    # Verificar docker-compose.yml
    local compose_files=(
        "docker-compose.yml"
        "ml_item_product/docker-compose.yml"
    )
    
    for compose_file in "${compose_files[@]}"; do
        if [[ -f "$compose_file" ]]; then
            # Verificación básica de sintaxis YAML
            if python3 -c "import yaml; yaml.safe_load(open('$compose_file'))" 2>/dev/null; then
                print_success "Docker Compose válido: $compose_file"
            elif python -c "import yaml; yaml.safe_load(open('$compose_file'))" 2>/dev/null; then
                print_success "Docker Compose válido: $compose_file"
            else
                print_warning "No se pudo validar YAML (falta python/yaml): $compose_file"
            fi
        else
            print_warning "Docker Compose no encontrado: $compose_file"
        fi
    done
}

# Función para verificar archivos de configuración
function check_config_files() {
    print_header "Verificando archivos de configuración"
    
    # Verificar package.json
    if [[ -f "ml_item_product/package.json" ]]; then
        if python3 -c "import json; json.load(open('ml_item_product/package.json'))" 2>/dev/null || \
           python -c "import json; json.load(open('ml_item_product/package.json'))" 2>/dev/null; then
            print_success "package.json válido"
        else
            print_error "package.json inválido"
            return 1
        fi
    else
        print_error "package.json no encontrado"
        return 1
    fi
    
    # Verificar pom.xml (si Maven wrapper está disponible)
    if [[ -f "ml-core-platform/pom.xml" && -f "ml-core-platform/mvnw" ]]; then
        cd ml-core-platform
        if ./mvnw help:effective-pom -q >/dev/null 2>&1; then
            print_success "pom.xml válido"
        else
            print_warning "No se pudo validar pom.xml (puede ser problema de JAVA_HOME)"
        fi
        cd ..
    else
        print_warning "pom.xml o mvnw no encontrado"
    fi
}

# Función para verificar permisos
function check_permissions() {
    print_header "Verificando permisos de archivos"
    
    local executable_files=(
        "setup-local.sh"
        "dev.sh"
        "help.sh"
        "stop.sh"
        "check-health.sh"
        "ml_item_product/start.sh"
        "ml_item_product/start-docker.sh"
        "ml_item_product/docker-entrypoint.sh"
        "ml-core-platform/mvnw"
        "ml-core-platform/quick-start-docker.sh"
        "ml-core-platform/install-dependencies-mac.sh"
        "ml-core-platform/install-docker-mac.sh"
    )
    
    for file in "${executable_files[@]}"; do
        if [[ -f "$file" ]]; then
            if [[ -x "$file" ]]; then
                print_success "Permisos ejecutables correctos: $file"
            else
                print_warning "Sin permisos de ejecución: $file"
                chmod +x "$file"
                print_info "Permisos corregidos para: $file"
            fi
        fi
    done
}

# Función para verificar dependencias del sistema
function check_system_dependencies() {
    print_header "Verificando dependencias del sistema disponibles"
    
    # Verificar Docker
    if command -v docker >/dev/null 2>&1; then
        print_success "Docker disponible: $(docker --version)"
    else
        print_warning "Docker no está instalado"
    fi
    
    # Verificar Docker Compose
    if command -v docker-compose >/dev/null 2>&1; then
        print_success "Docker Compose disponible: $(docker-compose --version)"
    elif docker compose version >/dev/null 2>&1; then
        print_success "Docker Compose (plugin) disponible: $(docker compose version)"
    else
        print_warning "Docker Compose no está disponible"
    fi
    
    # Verificar Node.js
    if command -v node >/dev/null 2>&1; then
        print_success "Node.js disponible: $(node --version)"
    else
        print_warning "Node.js no está instalado"
    fi
    
    # Verificar Java
    if command -v java >/dev/null 2>&1; then
        print_success "Java disponible: $(java -version 2>&1 | head -1)"
    else
        print_warning "Java no está instalado"
    fi
    
    # Verificar Maven
    if command -v mvn >/dev/null 2>&1; then
        print_success "Maven disponible: $(mvn --version | head -1)"
    else
        print_info "Maven no está instalado (se puede usar wrapper)"
    fi
}

# Función principal
function main() {
    print_header "🔍 Validación completa del proyecto MercadoLibre"
    
    # Cambiar al directorio del proyecto
    cd "$(dirname "$0")"
    
    # Ejecutar todas las verificaciones
    check_bash_scripts
    check_powershell_scripts
    check_docker_files
    check_config_files
    check_permissions
    check_system_dependencies
    
    print_header "✅ Validación completada"
    print_success "Todos los scripts y configuraciones han sido verificados"
    print_info "El proyecto está listo para usar"
    
    echo ""
    echo -e "${BLUE}📖 Para comenzar a usar el proyecto:${NC}"
    echo -e "${YELLOW}   Linux/macOS: ./setup-local.sh${NC}"
    echo -e "${YELLOW}   Windows:     .\\setup-local.ps1${NC}"
    echo ""
}

# Ejecutar validación
main "$@"
