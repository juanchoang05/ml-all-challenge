#!/bin/bash

# Script completo para configurar y ejecutar el entorno local de MercadoLibre
# Incluye verificaciones de dependencias y configuración automática

set -e  # Detener el script si hay algún error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Funciones de utilidad
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

# Verificar dependencias
check_dependencies() {
    print_header "Verificando dependencias del sistema"
    
    # Verificar Docker
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado. Por favor instálalo desde https://www.docker.com/"
        exit 1
    fi
    print_success "Docker está instalado"
    
    # Verificar Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose no está instalado. Por favor instálalo"
        exit 1
    fi
    print_success "Docker Compose está instalado"
    
    # Verificar que Docker esté corriendo
    if ! docker info &> /dev/null; then
        print_error "Docker no está corriendo. Por favor inicia Docker Desktop"
        exit 1
    fi
    print_success "Docker está corriendo"
    
    # Verificar Java (para desarrollo local, aunque Docker usará su propia JVM)
    if command -v java &> /dev/null; then
        local java_version=$(java -version 2>&1 | head -n 1)
        print_info "Java disponible: $java_version"
        print_info "Nota: Docker usará OpenJDK 17 independientemente de tu versión local"
    else
        print_warning "Java no está instalado localmente (no requerido para Docker)"
    fi
    
    # Verificar puertos disponibles
    local ports=(3000 8080 5432 6379)
    for port in "${ports[@]}"; do
        if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
            print_warning "Puerto $port está en uso. Esto podría causar conflictos."
        else
            print_success "Puerto $port está disponible"
        fi
    done
}

# Verificar si Maven está instalado
setup_maven(){
    print_header "Verificando dependencias del sistema MAVEN"

    if ! command -v mvn &> /dev/null; then
    print_info "⚠️ Maven no está instalado."

    # Preguntar al usuario si desea instalar Maven
    read -p "¿Deseas instalar Maven? (s/n): " respuesta

    if [[ "$respuesta" == "s" || "$respuesta" == "S" ]]; then
        print_info "🔧 Instalando Maven..."

        # Detectar el sistema operativo (para Debian/Ubuntu, puedes agregar más)
        if [ -x "$(command -v apt)" ]; then
            sudo apt update
            sudo apt install -y maven
        else
            print_info "❌ No se pudo detectar un gestor de paquetes compatible automáticamente."
            print_info "👉 Por favor, instala Maven manualmente: https://maven.apache.org/install.html"
        fi
    else
        print_info "❌ Maven es requerido. Abortando."
        exit 1
    fi
    else
        print_info "✅ Maven ya está instalado."
    fi
}

# Ejecutar el comando Maven Wrapper
verify_maven_wrapper(){
print_header "Verificando dependencias del sistema MAVEN WRAPPER"

print_info "🚀 Ejecutando: mvn -N io.takari:maven:wrapper"
mvn -N io.takari:maven:wrapper

# Verificar si el comando fue exitoso
if [ $? -eq 0 ]; then
    print_info "✅ Maven Wrapper creado correctamente."
else
    print_info "❌ Error al ejecutar el comando Maven Wrapper."
    exit 1
fi

}

# Configurar archivos de entorno
setup_environment() {
    print_header "Configurando archivos de entorno"
    
    # Obtener el directorio del script
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "$SCRIPT_DIR"
    
    # Verificar que .env.example existe
    if [ ! -f ML/.env.example ]; then
        print_error "No se encontró el archivo .env.example en $SCRIPT_DIR"
        print_info "Asegúrate de ejecutar el script desde el directorio correcto"
        exit 1
    fi
    
    # Copiar .env si no existe
    if [ ! -f ML/.env ]; then
        print_info "Creando archivo .env desde .env.example"
        cp ML/.env.example .env
        print_success "Archivo .env creado"
    else
        print_info "Archivo .env ya existe"
    fi
    
    # Verificar archivos de configuración del frontend
    if [ ! -f ml_item_product/.env ]; then
        if [ -f ml_item_product/.env.example ]; then
            print_info "Creando .env para el frontend"
            cp ml_item_product/.env.example ml_item_product/.env
            print_success "Archivo .env del frontend creado"
        else
            print_warning "No se encontró ml_item_product/.env.example"
        fi
    else
        print_info "Archivo .env del frontend ya existe"
    fi
}



# Limpiar contenedores anteriores
cleanup_previous() {
    print_header "Limpiando contenedores anteriores  $SCRIPT_DIR"
    
    # Parar y remover contenedores existentes
    if docker-compose ps -q | grep -q .; then
        print_info "Parando contenedores existentes..."
        docker-compose down --remove-orphans
        print_success "Contenedores anteriores eliminados"
    else
        print_info "No hay contenedores corriendo  $SCRIPT_DIR"
    fi
}

# Construir e iniciar servicios
build_and_start() {
    print_header "Construyendo e iniciando servicios  $SCRIPT_DIR"
    cd $SCRIPT_DIR/ML
    print_info "Construyendo imágenes Docker...  $SCRIPT_DIR"
    if docker-compose build; then
        print_success "Imágenes construidas exitosamente  $SCRIPT_DIR"
    else
        print_error "Error construyendo imágenes  $SCRIPT_DIR"
        exit 1
    fi
    
    print_info "Iniciando servicios...  $SCRIPT_DIR"
    if docker-compose up -d; then
        print_success "Servicios iniciados"
    else
        print_error "Error iniciando servicios"
        exit 1
    fi
}

# Verificar que los servicios estén funcionando
verify_services() {
    print_header "Verificando servicios"
    
    local max_attempts=30
    local attempt=1
    
    # Verificar base de datos
    print_info "Esperando que PostgreSQL esté listo..."
    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T ml-database pg_isready -U mluser -d mlcoreplatform &> /dev/null; then
            print_success "PostgreSQL está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_error "PostgreSQL no responde después de $max_attempts intentos"
            exit 1
        fi
        sleep 2
        ((attempt++))
    done
    
    # Verificar Redis
    print_info "Verificando Redis..."
    attempt=1
    while [ $attempt -le $max_attempts ]; do
        if docker-compose exec -T ml-redis redis-cli ping &> /dev/null; then
            print_success "Redis está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_error "Redis no responde después de $max_attempts intentos"
            exit 1
        fi
        sleep 2
        ((attempt++))
    done
    
    # Verificar backend (Spring Boot)
    print_info "Esperando que el backend esté listo..."
    attempt=1
    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:8080/actuator/health &> /dev/null; then
            print_success "Backend está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_warning "Backend no responde en /actuator/health. Puede estar iniciando aún..."
        fi
        sleep 3
        ((attempt++))
    done
    
    # Verificar frontend
    print_info "Verificando frontend..."
    attempt=1
    while [ $attempt -le $max_attempts ]; do
        if curl -f http://localhost:3000 &> /dev/null; then
            print_success "Frontend está listo"
            break
        fi
        if [ $attempt -eq $max_attempts ]; then
            print_warning "Frontend no responde en puerto 3000. Puede estar iniciando aún..."
        fi
        sleep 2
        ((attempt++))
    done
}

# Mostrar información final
show_info() {
    print_header "🎉 ¡Entorno local configurado exitosamente!"
    
    echo ""
    echo -e "${GREEN}📍 URLs disponibles:${NC}"
    echo -e "   ${BLUE}Frontend (React):${NC}     http://localhost:3000"
    echo -e "   ${BLUE}Backend (Spring):${NC}     http://localhost:8080"
    echo -e "   ${BLUE}API Health Check:${NC}     http://localhost:8080/actuator/health"
    echo -e "   ${BLUE}Swagger UI:${NC}           http://localhost:8080/swagger-ui.html"
    echo -e "   ${BLUE}Base de datos:${NC}        localhost:5432 (mluser/mlpassword)"
    echo -e "   ${BLUE}Redis:${NC}                localhost:6379"
    echo ""
    echo -e "${GREEN}🔧 Comandos útiles:${NC}"
    echo -e "   ${YELLOW}Ver logs:${NC}             docker-compose logs -f"
    echo -e "   ${YELLOW}Ver logs específicos:${NC} docker-compose logs -f [ml-item-product|ml-core-platform]"
    echo -e "   ${YELLOW}Parar aplicación:${NC}     ./stop.sh"
    echo -e "   ${YELLOW}Reiniciar:${NC}            docker-compose restart"
    echo -e "   ${YELLOW}Ver servicios:${NC}        docker-compose ps"
    echo -e "   ${YELLOW}Entrar al backend:${NC}    docker-compose exec ml-core-platform bash"
    echo -e "   ${YELLOW}Ver BD:${NC}               docker-compose exec ml-database psql -U mluser -d mlcoreplatform"
    echo ""
    echo -e "${GREEN}📁 Estructura del proyecto:${NC}"
    echo -e "   ${BLUE}ml_item_product/${NC}      - Frontend React con Vite"
    echo -e "   ${BLUE}ml-core-platform/${NC}     - Backend Spring Boot"
    echo ""
}

# Función principal
main() {
    print_header "🚀 Configuración del entorno local de MercadoLibre"
    
    # Asegurarse de que estamos en el directorio correcto
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "$SCRIPT_DIR"
    print_info "Ejecutando desde: $SCRIPT_DIR"
    
    check_dependencies
    setup_environment
    setup_maven
    verify_maven_wrapper
    cleanup_previous
    build_and_start
    verify_services
    show_info
    
    # Opcional: abrir el navegador
    if command -v open &> /dev/null; then
        read -p "¿Deseas abrir el navegador automáticamente? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            print_info "Abriendo navegador..."
            sleep 3
            open http://localhost:3000
        fi
    fi
    
    print_success "¡Setup completado! Disfruta desarrollando 🎯"
}

# Manejo de interrupciones
trap 'print_error "Setup interrumpido por el usuario"; exit 1' INT

# Ejecutar función principal
main "$@"
