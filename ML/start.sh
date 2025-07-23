#!/bin/bash

# Script para iniciar la aplicación ML completa
echo "🚀 Iniciando aplicación MercadoLibre completa..."

# Copiar archivo de configuración si no existe
if [ ! -f .env ]; then
    echo "📋 Copiando archivo de configuración..."
    cp .env.example .env
    echo "✅ Archivo .env creado. Puedes editarlo si necesitas cambiar alguna configuración."
fi

# Construir e iniciar los servicios
echo "🔨 Construyendo e iniciando servicios..."
docker-compose up --build -d

# Esperar a que los servicios estén listos
echo "⏳ Esperando a que los servicios estén listos..."
sleep 30

# Verificar el estado de los servicios
echo "🔍 Verificando estado de los servicios..."
docker-compose ps

echo ""
echo "🎉 ¡Aplicación iniciada!"
echo ""
echo "📍 URLs disponibles:"
echo "   Frontend (React):     http://localhost:3000"
echo "   Backend (Spring):     http://localhost:8080"
echo "   API Health Check:     http://localhost:8080/actuator/health"
echo "   Base de datos:        localhost:5432"
echo "   Redis:                localhost:6379"
echo ""
echo "🔧 Comandos útiles:"
echo "   Ver logs:             docker-compose logs -f"
echo "   Parar aplicación:     docker-compose down"
echo "   Reiniciar:            docker-compose restart"
echo "   Ver servicios:        docker-compose ps"
echo ""

# Opcional: abrir el navegador
if command -v open &> /dev/null; then
    echo "🌐 Abriendo navegador..."
    sleep 5
    open http://localhost:3000
fi
