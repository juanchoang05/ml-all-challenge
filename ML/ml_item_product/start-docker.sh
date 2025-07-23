#!/bin/bash

# Script para iniciar el proyecto MercadoLibre Product Page con Docker
# Compatible con macOS y Linux

set -e

echo "🐳 Iniciando MercadoLibre Product Page con Docker..."
echo ""

# Verificar si Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker no está instalado. Por favor instala Docker desde https://www.docker.com/"
    exit 1
fi

# Verificar si Docker Compose está disponible
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ Docker Compose no está disponible. Por favor instala Docker Compose."
    exit 1
fi

echo "✅ Docker $(docker --version | cut -d' ' -f3 | sed 's/,//') detectado"
echo ""

# Verificar si docker-compose.yml existe
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ No se encontró docker-compose.yml. Asegúrate de estar en el directorio correcto."
    exit 1
fi

# Verificar si Dockerfile existe
if [ ! -f "Dockerfile" ]; then
    echo "❌ No se encontró Dockerfile. Asegúrate de estar en el directorio correcto."
    exit 1
fi

# Detener contenedores existentes si están corriendo
echo "🛑 Deteniendo contenedores existentes..."
docker-compose down 2>/dev/null || docker compose down 2>/dev/null || true

# Construir e iniciar los contenedores
echo "🔨 Construyendo e iniciando contenedores..."
echo "📦 Esto puede tomar unos minutos la primera vez..."
echo ""

# Intentar con docker-compose primero, luego con docker compose
if command -v docker-compose &> /dev/null; then
    docker-compose up --build
else
    docker compose up --build
fi

echo ""
echo "🌐 La aplicación debería estar disponible en: http://localhost:3000"
echo "📊 Logs disponibles en la consola"
echo ""
echo "Para detener los contenedores, presiona Ctrl+C y luego ejecuta:"
echo "docker-compose down  o  docker compose down"
