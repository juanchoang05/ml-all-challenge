# 🚀 MercadoLibre - Entorno Local Completo

Este proyecto contiene la configuración completa para levantar tanto el frontend (ml_item_product) como el backend (ml-core-platform) de MercadoLibre en entorno local, con soporte para desarrollo y testing.

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Base de       │
│   (React+Vite)  │◄──►│   (Spring Boot) │◄──►│   Datos         │
│   Puerto: 3000  │    │   Puerto: 8080  │    │   (PostgreSQL)  │
│   Puerto: 5173  │    │                 │    │   Puerto: 5432  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         ▲                        ▲                        ▲
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                         ┌─────────────────┐
                         │     Redis       │
                         │   (Cache)       │
                         │  Puerto: 6379   │
                         └─────────────────┘
```

## ⚡ Inicio Súper Rápido

### 🎯 Para testing y demo (Recomendado)
```bash
./setup-local.sh
```

### 🔧 Para desarrollo con hot-reload
```bash
./setup-dev.sh
```

### 📖 Para ver todos los comandos disponibles
```bash
./help.sh
```

## 📋 Prerrequisitos

### Todos los Sistemas
- **Docker** y **Docker Compose**
- **Git**

### Para Desarrollo (Opcional)
- **Node.js** 18+ y **npm**
- **Java** 17+ y **Maven**

### Específicos por Sistema
- **macOS/Linux**: Scripts bash (.sh)
- **Windows**: Scripts PowerShell (.ps1) - Ver [README-WINDOWS.md](README-WINDOWS.md)

## 🚀 Métodos de Inicio

### 🐧 Linux/macOS

#### Método 1: Entorno Completo Docker (Recomendado para testing)

```bash
# Configuración automática con verificaciones
./setup-local.sh

# O manualmente
./start.sh
```

#### Método 2: Entorno de Desarrollo (Recomendado para coding)

```bash
# Configurar infraestructura
./setup-dev.sh

# Luego iniciar backend (terminal 1)
cd ml-core-platform
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Luego iniciar frontend (terminal 2)
cd ml_item_product
npm run dev
```

### 🪟 Windows

#### Método 1: Script Batch (Más Simple)
```batch
setup-windows.bat
```

#### Método 2: PowerShell (Recomendado)
```powershell
.\setup-local.ps1
```

#### Método 3: Para Desarrollo
```powershell
.\setup-dev.ps1
```

**📖 Para instrucciones detalladas de Windows, consulta [README-WINDOWS.md](README-WINDOWS.md)**

### Ventajas de cada método:

**Docker Completo:**
- ✅ Setup automático completo
- ✅ Verificación de dependencias
- ✅ Healthchecks automáticos
- ✅ Perfecto para testing y demos

**Desarrollo:**
- ⚡ Hot-reload automático
- 🐛 Debugging más fácil
- 🔍 Logs detallados
- ⚡ Compilación incremental más rápida

## 🌐 URLs del Sistema

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend (Producción)** | http://localhost:3000 | React + Vite (Docker) |
| **Frontend (Desarrollo)** | http://localhost:5173 | React + Vite (Hot-reload) |
| **Backend API** | http://localhost:8080 | Spring Boot REST API |
| **Health Check** | http://localhost:8080/actuator/health | Estado del backend |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación API |
| **PostgreSQL** | localhost:5432 | Base de datos |
| **Redis** | localhost:6379 | Cache en memoria |

## 🚨 Troubleshooting

### Problemas Comunes

#### 🔍 Verificar estado del sistema
```bash
./check-health.sh
```

#### 🐳 Problemas con Docker
```bash
# Verificar que Docker esté corriendo
docker --version
docker-compose --version

# Ver servicios
docker-compose ps

# Reiniciar servicios
docker-compose restart

# Logs de servicios
docker-compose logs -f [servicio]
```

#### 🔌 Puertos ocupados
```bash
# Verificar qué proceso usa un puerto
lsof -i :3000
lsof -i :8080
lsof -i :5432

# Matar proceso en puerto específico
kill -9 $(lsof -ti:8080)
```

#### 🗃️ Problemas con base de datos
```bash
# Reiniciar solo la base de datos
docker-compose restart ml-database

# Verificar logs de BD
docker-compose logs ml-database

# Conectar para verificar
docker-compose exec ml-database psql -U mluser -d mlcoreplatform
```

#### 🧹 Limpieza completa
```bash
# Parar todo y limpiar
./stop.sh

# O manualmente
docker-compose down -v
docker system prune -f

# Reconstruir todo
./setup-local.sh
```

#### ⚠️ Si nada funciona
```bash
# Limpieza nuclear (cuidado!)
docker system prune -a -f
docker volume prune -f

# Luego reconstruir
./setup-local.sh
```

### Logs Útiles

```bash
# Todos los logs
docker-compose logs -f

# Solo backend
docker-compose logs -f ml-core-platform

# Solo frontend  
docker-compose logs -f ml-item-product

# Solo base de datos
docker-compose logs -f ml-database
```

### Performance

Si el sistema va lento:

1. **Verificar recursos**:
   ```bash
   docker stats
   ```

2. **Limpiar contenedores no usados**:
   ```bash
   docker system prune
   ```

3. **Reiniciar Docker Desktop** (macOS/Windows)

4. **Aumentar memoria asignada a Docker** en configuración

## 🔧 Comandos Útiles

### Scripts disponibles
```bash
./start.sh    # Iniciar aplicación completa
./stop.sh     # Detener aplicación (con opciones de limpieza)
./dev.sh      # Modo desarrollo con logs visibles
```

### Comandos Docker Compose
```bash
# Ver estado de los servicios
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs de un servicio específico
docker-compose logs -f ml-item-product
docker-compose logs -f ml-core-platform

# Reiniciar un servicio específico
docker-compose restart ml-item-product
docker-compose restart ml-core-platform

# Parar todos los servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v

# Reconstruir imágenes
docker-compose build --no-cache
```

## ⚙️ Configuración

### Variables de Entorno

Copia `.env.example` a `.env` y modifica según tus necesidades:

```bash
cp .env.example .env
```

### Configuración del Frontend (React)
- `REACT_APP_ENV`: Entorno (development, testing, production)
- `REACT_APP_REGION`: Región (colombia, argentina, mexico, brazil, chile)
- `REACT_APP_USE_MOCK_DATA`: Usar datos mock (true/false)

### Configuración del Backend (Spring Boot)
- `SPRING_PROFILES_ACTIVE`: Perfil de Spring
- `JAVA_OPTS`: Opciones de JVM
- Base de datos y Redis se configuran automáticamente

## 🗄️ Base de Datos

### PostgreSQL
- **Host**: localhost
- **Puerto**: 5432
- **Base de datos**: mlcoreplatform
- **Usuario**: mluser
- **Contraseña**: mlpassword

### Conectar desde herramientas externas
```bash
# Usar psql
psql -h localhost -p 5432 -U mluser -d mlcoreplatform

# URL de conexión
postgresql://mluser:mlpassword@localhost:5432/mlcoreplatform
```

## 🔄 Cache Redis

### Conexión
- **Host**: localhost
- **Puerto**: 6379
- **Sin contraseña por defecto**

### Conectar con redis-cli
```bash
redis-cli -h localhost -p 6379
```

## 🌐 Nginx Proxy (Opcional)

Para usar el proxy reverso de Nginx:

```bash
# Iniciar con proxy
docker-compose --profile proxy up -d

# Acceder a través del proxy
# Frontend: http://localhost/
# Backend: http://localhost/api/
```

## 🐛 Solución de Problemas

### Verificar el estado de los servicios
```bash
docker-compose ps
docker-compose logs
```

### Problemas comunes

1. **Puerto en uso**
   ```bash
   # Verificar qué procesos usan los puertos
   lsof -i :3000
   lsof -i :8080
   lsof -i :5432
   ```

2. **Problemas de memoria**
   ```bash
   # Limpiar sistema Docker
   docker system prune -a
   ```

3. **Base de datos no conecta**
   ```bash
   # Verificar logs de PostgreSQL
   docker-compose logs ml-database
   ```

4. **Frontend no carga**
   ```bash
   # Verificar logs del frontend
   docker-compose logs ml-item-product
   ```

## 🔒 Seguridad

- Las contraseñas por defecto son para desarrollo
- En producción, cambiar todas las credenciales
- Configurar SSL/TLS para HTTPS
- Usar variables de entorno seguras

## 📝 Desarrollo

### Estructura de archivos
```
.
├── docker-compose.yml          # Configuración principal
├── nginx-proxy.conf           # Configuración de Nginx
├── .env.example              # Variables de entorno ejemplo
├── start.sh                  # Script de inicio
├── stop.sh                   # Script de parada
├── dev.sh                    # Script de desarrollo
├── ml_item_product/          # Proyecto Frontend
└── ml-core-platform/         # Proyecto Backend
```

### Flujo de desarrollo
1. Modificar código en `ml_item_product/` o `ml-core-platform/`
2. Reiniciar servicios: `docker-compose restart`
3. Ver logs: `docker-compose logs -f`

## 🤝 Contribuir

1. Hacer fork del proyecto
2. Crear una rama para tu feature
3. Realizar cambios y testing
4. Enviar pull request

## 📄 Licencia

Este proyecto es para uso interno de MercadoLibre.

---

**¿Necesitas ayuda?** Revisa los logs o contacta al equipo de desarrollo.
