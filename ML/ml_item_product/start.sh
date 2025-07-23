#!/bin/bash

# Script para iniciar el proyecto MercadoLibre Product Page
# Compatible con macOS y Linux

set -e

echo "🚀 Iniciando MercadoLibre Product Page..."
echo ""

# Verificar si Node.js está instalado
if ! command -v node &> /dev/null; then
    echo "❌ Node.js no está instalado. Por favor instala Node.js desde https://nodejs.org/"
    exit 1
fi

# Verificar si npm está instalado
if ! command -v npm &> /dev/null; then
    echo "❌ npm no está instalado. Por favor instala npm."
    exit 1
fi

echo "✅ Node.js $(node --version) detectado"
echo "✅ npm $(npm --version) detectado"
echo ""

# Verificar si package.json existe
if [ ! -f "package.json" ]; then
    echo "❌ No se encontró package.json. Asegúrate de estar en el directorio correcto."
    exit 1
fi

# Instalar dependencias
echo "📦 Instalando dependencias..."
npm install

if [ $? -ne 0 ]; then
    echo "❌ Error al instalar dependencias"
    exit 1
fi

echo "✅ Dependencias instaladas correctamente"
echo ""

# Configurar ambiente inicial
echo "⚙️ Configurando ambiente inicial..."
npm run env:switch development colombia

if [ $? -ne 0 ]; then
    echo "⚠️  No se pudo configurar el ambiente automáticamente, continuando..."
fi

echo ""

# Iniciar el proyecto en modo local
echo "🔥 Iniciando proyecto en modo desarrollo local..."
echo "🌐 La aplicación estará disponible en: http://localhost:5173"
echo ""
echo "Presiona Ctrl+C para detener el servidor"
echo ""

npm run dev:local
