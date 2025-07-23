#!/bin/sh

# Script de entrada para el contenedor Docker
# Permite configurar variables de entorno en runtime

echo "🚀 Iniciando aplicación MercadoLibre Product Page..."
echo "📍 Región: ${REACT_APP_REGION:-colombia}"
echo "🌍 Entorno: ${REACT_APP_ENV:-production}"
echo "🔧 Mock Data: ${REACT_APP_USE_MOCK_DATA:-false}"

# Función para reemplazar variables de entorno en archivos JS
replace_env_vars() {
    echo "📝 Configurando variables de entorno en runtime..."
    
    # Buscar archivos JS en el directorio de build
    find /usr/share/nginx/html -name "*.js" -type f -exec grep -l "REACT_APP_" {} \; | while read -r file; do
        echo "🔄 Procesando: $file"
        
        # Reemplazar variables de entorno
        sed -i "s|REACT_APP_ENV_PLACEHOLDER|${REACT_APP_ENV:-production}|g" "$file"
        sed -i "s|REACT_APP_REGION_PLACEHOLDER|${REACT_APP_REGION:-colombia}|g" "$file"
        sed -i "s|REACT_APP_USE_MOCK_DATA_PLACEHOLDER|${REACT_APP_USE_MOCK_DATA:-false}|g" "$file"
    done
}

# Función para validar configuración
validate_config() {
    echo "🔍 Validando configuración..."
    
    # Validar región
    case "${REACT_APP_REGION:-colombia}" in
        colombia|argentina|mexico|brazil|chile)
            echo "✅ Región válida: ${REACT_APP_REGION}"
            ;;
        *)
            echo "⚠️  Región no reconocida: ${REACT_APP_REGION}, usando colombia por defecto"
            export REACT_APP_REGION="colombia"
            ;;
    esac
    
    # Validar entorno
    case "${REACT_APP_ENV:-production}" in
        development|testing|production)
            echo "✅ Entorno válido: ${REACT_APP_ENV}"
            ;;
        *)
            echo "⚠️  Entorno no reconocido: ${REACT_APP_ENV}, usando production por defecto"
            export REACT_APP_ENV="production"
            ;;
    esac
}

# Función para configurar nginx
configure_nginx() {
    echo "🔧 Configurando nginx..."
    
    # Verificar que los archivos existen
    if [ ! -f "/usr/share/nginx/html/index.html" ]; then
        echo "❌ Error: No se encontró index.html"
        exit 1
    fi
    
    # Verificar configuración de nginx
    nginx -t
    if [ $? -ne 0 ]; then
        echo "❌ Error en configuración de nginx"
        exit 1
    fi
    
    echo "✅ Nginx configurado correctamente"
}

# Función principal
main() {
    echo "🏗️  Iniciando configuración del contenedor..."
    
    # Validar configuración
    validate_config
    
    # Reemplazar variables de entorno si es necesario
    if [ "${RUNTIME_ENV_REPLACEMENT:-false}" = "true" ]; then
        replace_env_vars
    fi
    
    # Configurar nginx
    configure_nginx
    
    echo "✅ Configuración completada"
    echo "🌐 Aplicación disponible en puerto 80"
    echo "📊 Health check disponible en /health"
    
    # Ejecutar comando pasado como argumentos
    exec "$@"
}

# Manejar señales para graceful shutdown
trap 'echo "🛑 Recibida señal de parada, cerrando gracefully..."; nginx -s quit; exit 0' TERM

# Ejecutar función principal
main "$@"
