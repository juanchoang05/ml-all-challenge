# 📦 MercadoLibre Local Environment - Información del Paquete

## 📊 Resumen del Paquete

- **Archivo**: `MercadoLibre-Local-Environment.zip`
- **Tamaño**: 382 KB
- **Archivos incluidos**: 327 archivos
- **Fecha de creación**: $(date)
- **Plataformas soportadas**: Linux, macOS, Windows

## 🚀 ¿Qué contiene?

### ✅ Incluido en el paquete:

#### 📋 Scripts de Configuración
- **Linux/macOS**: `setup-local.sh`, `setup-dev.sh`, `start.sh`, `stop.sh`, etc.
- **Windows**: `setup-local.ps1`, `setup-dev.ps1`, `start.ps1`, `stop.ps1`, etc.
- **Batch**: `setup-windows.bat` para usuarios de Windows

#### 🐳 Configuración Docker
- `docker-compose.yml` - Configuración completa de servicios
- `Dockerfile` para frontend y backend
- `nginx-proxy.conf` - Configuración del proxy reverso

#### 📚 Documentación
- `README.md` - Guía principal
- `README-WINDOWS.md` - Guía específica para Windows
- `QUICK-START.md` - Inicio rápido
- Documentación específica de cada proyecto

#### 💻 Código Fuente
- **Frontend**: React + Vite completo (`ml_item_product/`)
- **Backend**: Spring Boot completo (`ml-core-platform/`)
- Todas las configuraciones y dependencias definidas

#### 🔧 Herramientas Auxiliares
- `Makefile` - Comandos Make opcionales
- Scripts de ayuda y verificación de salud
- Archivos de configuración de entorno

### ❌ Excluido del paquete (se descarga automáticamente):

- `node_modules/` - Dependencias de Node.js
- `target/` - Archivos compilados de Maven
- `.git/` - Historial de Git
- Archivos de logs y temporales
- Archivos `.env` (se crean desde .env.example)
- Cache y archivos de build

## 🔧 Cómo usar el paquete

### 1. Extraer el archivo
```bash
# Linux/macOS
unzip MercadoLibre-Local-Environment.zip

# Windows (PowerShell)
Expand-Archive MercadoLibre-Local-Environment.zip
```

### 2. Entrar al directorio
```bash
cd directorio_extraido
```

### 3. Ejecutar según tu sistema

#### Linux/macOS:
```bash
./setup-local.sh
```

#### Windows:
```powershell
.\setup-local.ps1
```

#### Windows (alternativa):
```batch
setup-windows.bat
```

## 🌐 URLs disponibles después de la instalación

- **Frontend**: http://localhost:3000
- **Backend**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 📋 Prerrequisitos

### Obligatorios:
- Docker y Docker Compose
- Git (recomendado)

### Para desarrollo (opcional):
- Node.js 18+ y npm
- Java 17+ y Maven

### Windows específico:
- PowerShell 5.1+ o PowerShell Core 7+
- Política de ejecución configurada

## 🆘 Soporte

1. **Verificar sistema**: `./check-health.sh` (Linux/macOS) o `.\check-health.ps1` (Windows)
2. **Ver ayuda**: `./help.sh` (Linux/macOS) o `.\help.ps1` (Windows)
3. **Consultar documentación**: Revisar archivos README

## 🔄 Flujos de trabajo recomendados

### Para testing/demo:
```bash
# Linux/macOS
./setup-local.sh

# Windows
.\setup-local.ps1
```

### Para desarrollo:
```bash
# Linux/macOS
./setup-dev.sh

# Windows
.\setup-dev.ps1
```

---

**Nota**: Este paquete contiene todo lo necesario para ejecutar el entorno local de MercadoLibre. Las dependencias se descargarán automáticamente durante el primer setup.
