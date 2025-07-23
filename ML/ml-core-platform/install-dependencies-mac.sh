#!/bin/bash

# Script de instalación de dependencias para macOS
# ML Core Platform - MercadoLibre

set -e

echo "🚀 Iniciando instalación de dependencias para ML Core Platform en macOS..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
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

# Verificar si Homebrew está instalado
check_homebrew() {
    if ! command -v brew &> /dev/null; then
        print_warning "Homebrew no está instalado. Instalando Homebrew..."
        /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
        
        # Configurar Homebrew en el PATH para Apple Silicon Macs
        if [[ $(uname -m) == "arm64" ]]; then
            echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zshrc
            eval "$(/opt/homebrew/bin/brew shellenv)"
        else
            echo 'eval "$(/usr/local/bin/brew shellenv)"' >> ~/.zshrc
            eval "$(/usr/local/bin/brew shellenv)"
        fi
        
        print_success "Homebrew instalado correctamente"
    else
        print_success "Homebrew ya está instalado"
        # Actualizar Homebrew
        print_status "Actualizando Homebrew..."
        brew update
    fi
}

# Verificar e instalar Java 17
install_java() {
    print_status "Verificando instalación de Java 17..."
    
    if java -version 2>&1 | grep -q "17\." ; then
        print_success "Java 17 ya está instalado"
    else
        print_status "Instalando Java 17..."
        brew install openjdk@17
        
        # Crear symlink para que el sistema encuentre Java
        sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
        
        # Configurar JAVA_HOME
        JAVA_HOME_LINE='export JAVA_HOME=$(/usr/libexec/java_home -v 17)'
        PATH_LINE='export PATH=$JAVA_HOME/bin:$PATH'
        
        # Detectar shell y configurar archivo correspondiente
        if [[ $SHELL == *"zsh"* ]]; then
            SHELL_RC="$HOME/.zshrc"
        elif [[ $SHELL == *"bash"* ]]; then
            SHELL_RC="$HOME/.bash_profile"
        else
            SHELL_RC="$HOME/.profile"
        fi
        
        # Agregar configuración si no existe
        if ! grep -q "JAVA_HOME.*java_home.*17" "$SHELL_RC" 2>/dev/null; then
            echo "" >> "$SHELL_RC"
            echo "# Java 17 Configuration" >> "$SHELL_RC"
            echo "$JAVA_HOME_LINE" >> "$SHELL_RC"
            echo "$PATH_LINE" >> "$SHELL_RC"
        fi
        
        # Aplicar cambios en la sesión actual
        export JAVA_HOME=$(/usr/libexec/java_home -v 17)
        export PATH=$JAVA_HOME/bin:$PATH
        
        print_success "Java 17 instalado y configurado correctamente"
    fi
}

# Verificar e instalar Maven
install_maven() {
    print_status "Verificando instalación de Maven..."
    
    if command -v mvn &> /dev/null; then
        MAVEN_VERSION=$(mvn --version | head -n 1 | awk '{print $3}')
        print_success "Maven ya está instalado (versión: $MAVEN_VERSION)"
    else
        print_status "Instalando Maven..."
        brew install maven
        print_success "Maven instalado correctamente"
    fi
}

# Instalar dependencias del proyecto
install_project_dependencies() {
    print_status "Instalando dependencias del proyecto..."
    
    if [ -f "pom.xml" ]; then
        mvn clean install
        print_success "Dependencias del proyecto instaladas correctamente"
    else
        print_error "No se encontró el archivo pom.xml en el directorio actual"
        print_warning "Asegúrate de ejecutar este script desde el directorio raíz del proyecto"
        exit 1
    fi
}

# Verificar instalación
verify_installation() {
    print_status "Verificando instalación..."
    
    echo ""
    echo "📋 Resumen de la instalación:"
    echo "=========================="
    
    # Verificar Java
    if java -version 2>&1 | grep -q "17\." ; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
        print_success "✅ Java: $JAVA_VERSION"
    else
        print_error "❌ Java 17 no está correctamente instalado"
    fi
    
    # Verificar Maven
    if command -v mvn &> /dev/null; then
        MAVEN_VERSION=$(mvn --version | head -n 1 | awk '{print $3}')
        print_success "✅ Maven: $MAVEN_VERSION"
    else
        print_error "❌ Maven no está correctamente instalado"
    fi
    
    # Verificar JAVA_HOME
    if [ -n "$JAVA_HOME" ]; then
        print_success "✅ JAVA_HOME: $JAVA_HOME"
    else
        print_error "❌ JAVA_HOME no está configurado"
    fi
    
    echo ""
}

# Función principal
main() {
    echo ""
    print_status "=== ML Core Platform - Instalador de Dependencias para macOS ==="
    echo ""
    
    check_homebrew
    install_java
    install_maven
    install_project_dependencies
    verify_installation
    
    echo ""
    print_success "🎉 ¡Instalación completada!"
    echo ""
    print_status "Para comenzar a desarrollar:"
    print_status "1. Reinicia tu terminal o ejecuta: source ~/.zshrc (o ~/.bash_profile)"
    print_status "2. Ejecuta: mvn spring-boot:run"
    print_status "3. Visita: http://localhost:8080/swagger-ui.html"
    echo ""
}

# Ejecutar función principal
main
