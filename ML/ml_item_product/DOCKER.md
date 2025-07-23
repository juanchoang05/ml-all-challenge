# 🐳 Docker Setup - MercadoLibre Product Page

## 📋 Resumen

Este proyecto incluye una configuración completa de Docker para desplegar la aplicación React de MercadoLibre Product Page con nginx como servidor web.

## 🏗️ Arquitectura Docker

```
┌─────────────────────────────────────┐
│            Docker Container         │
├─────────────────────────────────────┤
│  ┌─────────────┐  ┌───────────────┐ │
│  │    React    │  │     Nginx     │ │
│  │   (Build)   │  │  (Web Server) │ │
│  │             │  │               │ │
│  └─────────────┘  └───────────────┘ │
└─────────────────────────────────────┘
```

## 🚀 Comandos Docker

### Construcción básica
```bash
# Build de la imagen
docker build -t ml-product-page .

# Ejecutar contenedor
docker run -p 8080:80 ml-product-page
```

### Con variables de entorno
```bash
# Build con configuración específica
docker build \
  --build-arg REACT_APP_ENV=production \
  --build-arg REACT_APP_REGION=colombia \
  --build-arg REACT_APP_USE_MOCK_DATA=false \
  -t ml-product-page .

# Ejecutar con variables de entorno
docker run -p 8080:80 \
  -e REACT_APP_ENV=production \
  -e REACT_APP_REGION=colombia \
  -e REACT_APP_USE_MOCK_DATA=false \
  ml-product-page
```

## 📦 Docker Compose

### Configuración básica
```bash
# Levantar aplicación
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar aplicación
docker-compose down
```

### Con configuración específica
```bash
# Usar archivo de entorno personalizado
cp .env.docker .env

# Modificar variables según necesidades
vim .env

# Levantar con configuración personalizada
docker-compose --env-file .env up -d
```

### Diferentes entornos
```bash
# Desarrollo con mocks
REACT_APP_ENV=development REACT_APP_USE_MOCK_DATA=true docker-compose up

# Testing
REACT_APP_ENV=testing REACT_APP_REGION=colombia docker-compose up

# Producción Argentina
REACT_APP_ENV=production REACT_APP_REGION=argentina docker-compose up
```

## 🌍 Configuración por Regiones

### Colombia (por defecto)
```bash
docker run -p 8080:80 \
  -e REACT_APP_REGION=colombia \
  ml-product-page
```

### Argentina
```bash
docker run -p 8080:80 \
  -e REACT_APP_REGION=argentina \
  ml-product-page
```

### México
```bash
docker run -p 8080:80 \
  -e REACT_APP_REGION=mexico \
  ml-product-page
```

### Brasil
```bash
docker run -p 8080:80 \
  -e REACT_APP_REGION=brazil \
  ml-product-page
```

### Chile
```bash
docker run -p 8080:80 \
  -e REACT_APP_REGION=chile \
  ml-product-page
```

## 🔧 Variables de Entorno

| Variable | Descripción | Valores | Default |
|----------|-------------|---------|---------|
| `REACT_APP_ENV` | Entorno de ejecución | `development`, `testing`, `production` | `production` |
| `REACT_APP_REGION` | Región de MercadoLibre | `colombia`, `argentina`, `mexico`, `brazil`, `chile` | `colombia` |
| `REACT_APP_USE_MOCK_DATA` | Usar datos mock | `true`, `false` | `false` |

## 📊 Health Check

La aplicación incluye un endpoint de health check:

```bash
# Verificar estado de la aplicación
curl http://localhost:8080/health

# Response esperado
healthy
```

## 🔍 Debugging

### Ver logs del contenedor
```bash
docker logs ml-product-app -f
```

### Acceder al contenedor
```bash
docker exec -it ml-product-app sh
```

### Verificar configuración de nginx
```bash
docker exec ml-product-app nginx -t
```

### Verificar archivos build
```bash
docker exec ml-product-app ls -la /usr/share/nginx/html/
```

## 🚀 Despliegue en Producción

### Build optimizado
```bash
# Build multi-platform (para ARM y x86)
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  --build-arg REACT_APP_ENV=production \
  --build-arg REACT_APP_REGION=colombia \
  -t ml-product-page:latest \
  --push .
```

### Con Docker Swarm
```bash
# Inicializar swarm
docker swarm init

# Desplegar stack
docker stack deploy -c docker-compose.yml ml-stack
```

### Con Kubernetes
```bash
# Generar manifests de Kubernetes
kompose convert -f docker-compose.yml

# Aplicar manifests
kubectl apply -f ml-product-app-deployment.yaml
kubectl apply -f ml-product-app-service.yaml
```

## 🔒 Seguridad

### Headers de seguridad configurados:
- `X-Frame-Options: SAMEORIGIN`
- `X-XSS-Protection: 1; mode=block`
- `X-Content-Type-Options: nosniff`
- `Referrer-Policy: no-referrer-when-downgrade`
- `Content-Security-Policy`

### Best practices implementadas:
- Usuario no-root en contenedor
- Multi-stage build para reducir tamaño
- `.dockerignore` para excluir archivos innecesarios
- Health checks para monitoreo
- Graceful shutdown

## 🎯 URLs de Acceso

Después de ejecutar el contenedor:

- **Aplicación principal**: http://localhost:8080
- **Health check**: http://localhost:8080/health
- **Logs de nginx**: Dentro del contenedor en `/var/log/nginx/`

## 🐛 Troubleshooting

### Error: "Cannot connect to the Docker daemon"
```bash
# Verificar que Docker esté ejecutándose
docker --version
systemctl status docker  # En Linux
```

### Error: "Port already in use"
```bash
# Verificar qué proceso usa el puerto
lsof -i :8080

# Usar puerto diferente
docker run -p 8081:80 ml-product-page
```

### Build falla por memoria
```bash
# Aumentar memoria disponible para Docker
# En Docker Desktop: Settings > Resources > Memory

# O usar build con menos paralelismo
docker build --cpus=1 -t ml-product-page .
```

### Archivos estáticos no cargan
```bash
# Verificar configuración de nginx
docker exec ml-product-app cat /etc/nginx/nginx.conf

# Verificar archivos build
docker exec ml-product-app ls -la /usr/share/nginx/html/assets/
```
