# 🚀 MercadoLibre - Configuración para Windows

Este documento explica cómo configurar y ejecutar el entorno local de MercadoLibre en Windows usando PowerShell y Docker.

## 📋 Prerrequisitos para Windows

### Obligatorios
- **Windows 10/11** (versión 1903 o superior)
- **Docker Desktop para Windows** - [Descargar aquí](https://www.docker.com/products/docker-desktop)
- **PowerShell 5.1+** (incluido en Windows) o **PowerShell Core 7+**

### Para Desarrollo (Opcional)
- **Node.js 18+** - [Descargar aquí](https://nodejs.org/)
- **Java 17+** - [Descargar aquí](https://adoptium.net/)
- **Maven 3.6+** - [Descargar aquí](https://maven.apache.org/download.cgi)
- **Git para Windows** - [Descargar aquí](https://git-scm.com/download/win)

## 🔧 Configuración Inicial

### 1. Configurar PowerShell (Primera vez)

Abre **PowerShell como Administrador** y ejecuta:

```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### 2. Verificar Docker

Asegúrate de que Docker Desktop esté corriendo:

```powershell
docker --version
docker-compose --version
```

## ⚡ Inicio Rápido

### Opción 1: Script Batch (Más Simple)
```batch
setup-windows.bat
```

### Opción 2: PowerShell Directo (Recomendado)
```powershell
.\setup-local.ps1
```

### Opción 3: Para Desarrollo
```powershell
.\setup-dev.ps1
```

## 🛠️ Scripts Disponibles para Windows

| Script | Descripción | Uso |
|--------|-------------|-----|
| `setup-windows.bat` | Wrapper batch para facilitar ejecución | Doble clic o desde cmd |
| `.\setup-local.ps1` | Configuración completa automática | PowerShell |
| `.\setup-dev.ps1` | Configuración para desarrollo | PowerShell |
| `.\start.ps1` | Iniciar aplicación completa | PowerShell |
| `.\stop.ps1` | Detener todos los servicios | PowerShell |
| `.\help.ps1` | Guía completa de comandos | PowerShell |
| `.\check-health.ps1` | Verificar estado del sistema | PowerShell |

## 🎯 Flujos de Trabajo

### Para Testing/Demo Completo
```powershell
# Configurar e iniciar todo
.\setup-local.ps1

# Verificar que todo funcione
.\check-health.ps1

# Acceder a http://localhost:3000
```

### Para Desarrollo con Hot-reload
```powershell
# Configurar infraestructura
.\setup-dev.ps1

# Terminal 1: Backend
cd ml-core-platform
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Terminal 2: Frontend
cd ml_item_product
npm run dev
```

## 🌐 URLs Disponibles

- **Frontend (Docker)**: http://localhost:3000
- **Frontend (Dev)**: http://localhost:5173
- **Backend**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 🚨 Solución de Problemas Windows

### Error de Política de Ejecución
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```

### Puerto Ocupado
```powershell
# Ver qué proceso usa el puerto
Get-NetTCPConnection -LocalPort 8080

# Matar proceso por PID
Stop-Process -Id [PID] -Force
```

### Docker no Inicia
1. Verificar que Docker Desktop esté corriendo
2. Reiniciar Docker Desktop
3. Verificar que la virtualización esté habilitada en BIOS
4. Asegurar que Hyper-V esté habilitado

### Limpieza Completa
```powershell
.\stop.ps1 -CleanAll
```

### WSL Issues (si usas WSL2)
```bash
# En WSL2
wsl --shutdown
# Reiniciar Docker Desktop
```

## 📊 Comandos de Monitoreo

### Ver Estado de Servicios
```powershell
docker-compose ps
```

### Ver Logs
```powershell
# Todos los logs
docker-compose logs -f

# Solo backend
docker-compose logs -f ml-core-platform

# Solo frontend
docker-compose logs -f ml-item-product
```

### Verificar Salud del Sistema
```powershell
.\check-health.ps1
```

## 🔧 Comandos de Desarrollo

### Backend
```powershell
cd ml-core-platform

# Desarrollo local
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Tests
mvn test

# Compilar
mvn clean package
```

### Frontend
```powershell
cd ml_item_product

# Desarrollo
npm run dev

# Build
npm run build

# Tests
npm test
```

## 💡 Tips para Windows

1. **Usa PowerShell en lugar de CMD** para mejor experiencia
2. **Instala Windows Terminal** para múltiples pestañas
3. **Configura WSL2** si quieres usar comandos Linux
4. **Usa Docker Desktop** en lugar de Docker Engine manual
5. **Habilita Hyper-V** para mejor rendimiento de Docker

## 🆘 Obtener Ayuda

```powershell
# Ver ayuda completa
.\help.ps1

# Verificar sistema
.\check-health.ps1

# Ver logs específicos
docker-compose logs -f [servicio]
```

## 📝 Notas Específicas de Windows

- Los scripts PowerShell requieren política de ejecución configurada
- Docker Desktop debe estar corriendo antes de ejecutar scripts
- Algunos antivirus pueden interferir con Docker
- WSL2 es recomendado para mejor rendimiento
- Git Bash puede usarse como alternativa a PowerShell

---

💡 **Para soporte adicional**, consulta `.\help.ps1` o revisa los logs con `docker-compose logs -f`
