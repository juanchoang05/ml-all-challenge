#!/bin/bash

# Script para desarrollo - reinicio rápido con logs
echo "🔄 Reiniciando aplicación en modo desarrollo..."

# Parar servicios
docker-compose down

# Iniciar servicios en modo desarrollo (con logs visibles)
echo "🚀 Iniciando servicios con logs..."
docker-compose up --build

# Este script mantendrá los logs visibles
# Usa Ctrl+C para detener
