# Configuración por Ambientes

Este directorio contiene la configuración para diferentes ambientes y regiones del proyecto.

## Estructura de Archivos

```
src/config/
├── index.js          # Gestor principal de configuración
├── development.js    # Configuración para desarrollo
├── production.js     # Configuración para producción
├── testing.js        # Configuración para testing
├── local.js          # Configuración para desarrollo local
├── regions.js        # Configuraciones específicas por país/región
├── environment.js    # Variables de entorno
└── README.md         # Este archivo
```

## Uso Básico

```javascript
import { getConfig } from './config/index.js';

// Obtener la configuración actual
const config = getConfig();

// Construir URLs de endpoints
const url = config.buildUrl('itemById', { id: 'MLU123456789' });

// Obtener configuración de API
const apiConfig = config.getApiConfig();

// Verificar si una feature está habilitada
if (config.isFeatureEnabled('enableReviews')) {
  // Mostrar reviews
}
```

## Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
# API Configuration
REACT_APP_API_BASE_URL=https://api.mercadolibre.com
REACT_APP_API_TIMEOUT=10000

# Features
REACT_APP_ENABLE_LOGGING=true
REACT_APP_ENABLE_CACHE=true
REACT_APP_ENABLE_REVIEWS=true
REACT_APP_ENABLE_QUESTIONS=true
REACT_APP_ENABLE_RECOMMENDATIONS=false

# Analytics
REACT_APP_GA_ID=GA-XXXXXXXXX
REACT_APP_HOTJAR_ID=XXXXXXX
REACT_APP_SENTRY_DSN=https://xxx@sentry.io/xxx

# Development
REACT_APP_USE_MOCK_DATA=false
REACT_APP_MOCK_DELAY=1000
REACT_APP_REGION=colombia
```

## Configuración por Región

El sistema detecta automáticamente la región basada en la URL:

- `.com.co` → Colombia (MCO)
- `.com.ar` → Argentina (MLA)
- `.com.mx` → México (MLM)
- `.com.br` → Brasil (MLB)
- `.cl` → Chile (MLC)

Cada región tiene configuraciones específicas:
- Site ID de MercadoLibre
- Moneda
- Métodos de pago disponibles
- Métodos de envío
- Features habilitadas

## Endpoints Disponibles

Los endpoints están definidos con placeholders que se reemplazan dinámicamente:

```javascript
// Productos
itemById: '/items/{id}'
itemDescription: '/items/{id}/description'
itemQuestions: '/questions/search?item={id}'

// Usuarios
userById: '/users/{id}'
userItems: '/users/{id}/items/search'

// Categorías
categoryById: '/categories/{id}'
categoryAttributes: '/categories/{id}/attributes'

// Y muchos más...
```

## Ejemplos de Uso en Servicios

```javascript
import { getConfig } from '../config/index.js';

class ProductService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.config = getConfig();
  }

  async getById(productId) {
    const url = this.config.buildUrl('itemById', { id: productId });
    const response = await this.apiClient.get(url);
    
    this.config.log('Product fetched', { productId });
    return response.data;
  }
}
```

## Features y Configuración

### Logging
```javascript
if (config.isFeatureEnabled('enableLogging')) {
  config.log('Message', data);
  config.warn('Warning', data);
}
```

### Cache
```javascript
if (config.isFeatureEnabled('enableCaching')) {
  // Implementar lógica de cache
}
```

### Mock Data
```javascript
if (config.isFeatureEnabled('useMockData')) {
  // Usar datos simulados en lugar de API real
}
```

## Ambientes

### Development
- Logging habilitado
- Cache habilitado
- Timeouts normales
- Datos mock opcionales

### Production
- Logging deshabilitado
- Cache habilitado
- Timeouts más largos
- Sin datos mock

### Testing
- Logging habilitado
- Cache deshabilitado
- Timeouts cortos
- Datos mock habilitados
- Sin analytics

### Local
- Servidor local
- Logging habilitado
- Cache deshabilitado
- Datos mock opcionales

## Personalización

Para override de configuración:

```javascript
import { initConfig } from './config/index.js';

const customConfig = {
  api: {
    baseUrl: 'http://mi-api-personalizada.com'
  },
  features: {
    enableCustomFeature: true
  }
};

const config = initConfig(customConfig);
```

## Mejores Prácticas

1. **Nunca hardcodear URLs** - Usar siempre `config.buildUrl()`
2. **Verificar features** - Usar `isFeatureEnabled()` antes de usar funcionalidades opcionales
3. **Logging consistente** - Usar `config.log()` y `config.warn()`
4. **Variables de entorno** - Usar variables de entorno para configuraciones sensibles
5. **Configuración por región** - Aprovechar la detección automática de región
