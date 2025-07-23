# 🚀 MercadoLibre Local - Comandos Rápidos

## ⚡ Inicio Rápido

```bash
# Configurar y ejecutar todo automáticamente
./setup-local.sh

# O para desarrollo con hot-reload
./setup-dev.sh
```

## 📋 Scripts Principales

| Comando | Descripción |
|---------|-------------|
| `./setup-local.sh` | Configuración completa automática |
| `./setup-dev.sh` | Entorno de desarrollo con hot-reload |
| `./start.sh` | Iniciar aplicación completa |
| `./stop.sh` | Detener y limpiar servicios |
| `./help.sh` | Ayuda completa |
| `./check-health.sh` | Verificar estado del sistema |

## 🌐 URLs

- **Frontend**: http://localhost:3000 (Docker) o http://localhost:5173 (dev)
- **Backend**: http://localhost:8080
- **Health**: http://localhost:8080/actuator/health
- **Swagger**: http://localhost:8080/swagger-ui.html

## 🔧 Desarrollo Local

### Backend (Terminal 1)
```bash
cd ml-core-platform
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Frontend (Terminal 2)
```bash
cd ml_item_product
npm run dev
```

## 🐳 Docker

```bash
# Ver servicios
docker-compose ps

# Logs
docker-compose logs -f

# Reiniciar
docker-compose restart

# Parar
docker-compose down
```

## 🗄️ Base de Datos

```bash
# Conectar a PostgreSQL
docker-compose exec ml-database psql -U mluser -d mlcoreplatform

# Conectar a Redis
docker-compose exec ml-redis redis-cli
```

## 🚨 Troubleshooting

```bash
# Verificar estado
./check-health.sh

# Ver qué usa un puerto
lsof -i :8080

# Limpieza completa
./stop.sh
docker system prune -f
./setup-local.sh
```

## 📦 Make (Opcional)

```bash
make setup    # Configurar
make start    # Iniciar
make stop     # Parar
make health   # Verificar
make logs     # Ver logs
make clean    # Limpiar
```

---

💡 **Tip**: Usa `./help.sh` para la guía completa de comandos
