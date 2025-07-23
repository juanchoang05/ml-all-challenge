# MercadoLibre Product Page - React + Vite

Una aplicación React que replica la página de producto de MercadoLibre, construida con arquitectura limpia y configuración multi-ambiente.

## 🚀 Instalación

### Opción 1: Inicio Rápido con Scripts
```bash
# macOS/Linux
./start.sh

# Windows
start.bat

# Con Docker (macOS/Linux)
./start-docker.sh

# Con Docker (Windows)
start-docker.bat
```

### Opción 2: Instalación Manual
```bash
# 1. Clonar repositorio
git clone <repository-url>
cd ml_item_product

# 2. Instalar dependencias
npm install

# 3. Configurar ambiente inicial
npm run env:switch development colombia

# 4. Iniciar desarrollo
npm run dev
```

## 🏗️ Estructura del Proyecto

```
ml_item_product/
├── src/
│   ├── ui/                    # Componentes de interfaz de usuario
│   ├── application/           # Casos de uso y lógica de negocio
│   ├── adapters/             # Adaptadores para transformación de datos
│   ├── infrastructure/       # Servicios para APIs externas
│   ├── domain/              # Entidades y lógica de dominio
│   ├── config/              # Configuraciones por ambiente
│   └── ServiceContainer.js  # Inyección de dependencias
├── scripts/                 # Scripts de automatización
├── public/                  # Archivos estáticos
├── start.sh                 # Script de inicio (macOS/Linux)
├── start.bat                # Script de inicio (Windows)
├── start-docker.sh          # Script Docker (macOS/Linux)
├── start-docker.bat         # Script Docker (Windows)
├── docker-compose.yml       # Configuración Docker
├── Dockerfile              # Imagen Docker
├── package.json
└── README.md
```

## ⚡ Comandos Principales

### Desarrollo
```bash
# Desarrollo por región
npm run dev:colombia      # Colombia
npm run dev:argentina     # Argentina  
npm run dev:mexico        # México

# Desarrollo local con mocks
npm run dev:local         # Desarrollo con datos simulados

# Testing
npm run test:colombia
```

### Producción
```bash
# Build para producción
npm run build:production

# Build por región
npm run build:colombia
npm run build:argentina
npm run build:mexico
```

### Gestión de Ambientes
```bash
# Ver configuración actual
npm run env:status

# Cambiar ambiente manualmente
npm run env:switch [ambiente] [región]
npm run env:switch development colombia
npm run env:switch testing argentina
```

## 🚀 Scripts de Inicio Rápido

El proyecto incluye scripts automatizados para facilitar el inicio:

### Scripts Locales
```bash
# macOS/Linux - Desarrollo local
./start.sh                   # Instala dependencias + dev:local

# Windows - Desarrollo local  
start.bat                    # Instala dependencias + dev:local
```

### Scripts Docker
```bash
# macOS/Linux - Con Docker
./start-docker.sh            # Build + run con Docker

# Windows - Con Docker
start-docker.bat             # Build + run con Docker
```

### Características de los Scripts
- ✅ **Verificación automática** de dependencias (Node.js, npm, Docker)
- ✅ **Instalación automática** de paquetes npm
- ✅ **Configuración de ambiente** inicial
- ✅ **Inicio automático** del servidor de desarrollo
- ✅ **Mensajes informativos** del progreso
- ✅ **Manejo de errores** y validaciones

##  Configuración por Región

| País | Site ID | Moneda | Variable de Entorno |
|------|---------|--------|-------------------|
| Colombia | MCO | COP | `colombia` |
| Argentina | MLA | ARS | `argentina` |
| México | MLM | MXN | `mexico` |
| Brasil | MLB | BRL | `brazil` |
| Chile | MLC | CLP | `chile` |

## ⚙️ Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto (opcional, se crea automáticamente):

```env
# Ambiente y región
NODE_ENV=development
REACT_APP_REGION=colombia

# API Configuration
REACT_APP_API_BASE_URL=https://api.mercadolibre.com
REACT_APP_API_TIMEOUT=10000

# Features
REACT_APP_ENABLE_LOGGING=true
REACT_APP_ENABLE_CACHE=true
REACT_APP_USE_MOCK_DATA=false
```

## � URLs de Desarrollo

- **Local**: http://localhost:5173
- **Colombia**: http://localhost:5173?region=colombia
- **Argentina**: http://localhost:5173?region=argentina
- **México**: http://localhost:5173?region=mexico

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios
4. Commit tus cambios (`git commit -am 'Agregar nueva funcionalidad'`)
5. Push a la rama (`git push origin feature/nueva-funcionalidad`)
6. Abre un Pull Request
