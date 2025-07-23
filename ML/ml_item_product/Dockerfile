# Dockerfile para aplicación React + Vite (MercadoLibre Product Page)

# Stage 1: Build
FROM node:22-alpine AS builder

# Configurar directorio de trabajo
WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm ci --only=production=false

# Copiar código fuente
COPY . .

# Variables de entorno para build
ARG REACT_APP_ENV=production
ARG REACT_APP_REGION=colombia
ARG REACT_APP_USE_MOCK_DATA=false

ENV REACT_APP_ENV=$REACT_APP_ENV
ENV REACT_APP_REGION=$REACT_APP_REGION
ENV REACT_APP_USE_MOCK_DATA=$REACT_APP_USE_MOCK_DATA

# Build de la aplicación
RUN npm run build

# Stage 2: Production
FROM nginx:alpine

# Instalar supervisor para manejar múltiples procesos si es necesario
RUN apk add --no-cache supervisor

# Copiar configuración personalizada de nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar archivos build desde stage anterior
COPY --from=builder /app/dist /usr/share/nginx/html

# Copiar script de inicio y crear directorio para logs
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh && \
    mkdir -p /var/log/nginx

# Exponer puerto
EXPOSE 80

# Variables de entorno para runtime
ENV REACT_APP_ENV=production
ENV REACT_APP_REGION=colombia
ENV REACT_APP_USE_MOCK_DATA=false

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# Comando de inicio
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
