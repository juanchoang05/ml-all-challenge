# Sistema de Mocks - MercadoLibre Product Page

Este sistema proporciona servicios mock completos para el desarrollo local, permitiendo trabajar sin dependencias de APIs externas.

## 🚀 Características

- **Datos Realistas**: Mock data que simula respuestas reales de la API de MercadoLibre
- **Servicios Completos**: Todos los servicios principales tienen implementaciones mock
- **Configuración Flexible**: Control granular sobre delays, errores y comportamiento
- **Integración Transparente**: Los servicios reales pueden alternar entre mocks y APIs
- **Ambiente Específico**: Los mocks solo se activan en desarrollo

## 📁 Estructura

```
src/mocks/
├── data/                    # Datos mock estáticos
│   ├── products.js         # Productos de ejemplo
│   ├── sellers.js          # Vendedores de ejemplo
│   ├── payments.js         # Métodos de pago
│   ├── questions.js        # Preguntas y respuestas
│   ├── reviews.js          # Opiniones y calificaciones
│   ├── categories.js       # Categorías y navegación
│   ├── shipping.js         # Opciones de envío
│   ├── purchases.js        # Órdenes y compras
│   └── index.js           # Exportaciones centralizadas
├── services/               # Servicios mock
│   ├── MockProductService.js
│   ├── MockSellerService.js
│   ├── MockPaymentService.js
│   ├── MockQuestionsService.js
│   ├── MockReviewsService.js
│   ├── MockCategoryService.js
│   ├── MockShippingService.js
│   ├── MockPurchaseService.js
│   ├── MockImageService.js
│   ├── MockSpecificationsService.js
│   └── index.js           # Factory y exportaciones
└── index.js               # Punto de entrada principal
```

## ⚙️ Configuración

### Activar Mocks

Los mocks se activan automáticamente en el ambiente `local`:

```bash
# Activar ambiente local (con mocks)
npm run dev:local
```

### Variables de Entorno

```env
# Activar mocks manualmente
REACT_APP_USE_MOCK_DATA=true
REACT_APP_ENABLE_MOCK_DATA=true

# Configuración de mocks
REACT_APP_MOCK_DELAY=1000
REACT_APP_MOCK_ERROR_RATE=0.05
```

### Configuración por Ambiente

En `src/config/local.js`:

```javascript
features: {
  useMockData: true,      // Activar mocks
  enableMockData: true,   // Habilitar funcionalidad mock
  enableLogging: true     // Ver logs de mocks
},
mockData: {
  delay: 800,             // Delay de red simulado (ms)
  errorRate: 0.02,        // Tasa de errores (0.02 = 2%)
  enableLogs: true        // Logs en consola
}
```

## 🔧 Uso

### Automático en Servicios

Los servicios reales detectan automáticamente si deben usar mocks:

```javascript
// En ProductService.js
async getById(productId) {
  // Usar mock si está configurado
  if (this.shouldUseMocks()) {
    const mockService = this.mockContainer.getProductService();
    return await mockService.getProductById(productId);
  }
  
  // Usar API real
  const response = await this.apiClient.get(`/items/${productId}`);
  return response.data;
}
```

### Uso Directo

```javascript
import { getMockContainer } from '../mocks/index.js';

const mockContainer = getMockContainer();

// Obtener servicio mock específico
const productService = mockContainer.getProductService();
const product = await productService.getProductById('MCO123456789');

// Verificar si los mocks están activos
if (mockContainer.isActive()) {
  console.log('Using mock services');
}
```

## 📊 Datos Mock Disponibles

### Productos

- iPhone 14 Pro Max (MCO123456789) - Colombia
- Notebook Lenovo ThinkPad (MLA987654321) - Argentina

```javascript
import { getProductById } from '../mocks/data/products.js';

const product = getProductById('MCO123456789');
```

### Vendedores

- TechStore Oficial (123456) - Colombia
- Vendedor Pro Argentina (987654) - Argentina

```javascript
import { getSellerById } from '../mocks/data/sellers.js';

const seller = getSellerById(123456);
```

### Categorías

- Celulares y Teléfonos (MCO1055)
- Computación (MLA1652)

```javascript
import { getCategoryById } from '../mocks/data/categories.js';

const category = getCategoryById('MCO1055');
```

## 🛠️ Servicios Mock

### ProductService Mock

```javascript
const productService = mockContainer.getProductService();

// Obtener producto
const product = await productService.getProductById('MCO123456789');

// Buscar productos
const results = await productService.searchProducts('iPhone', { limit: 10 });

// Obtener descripción
const description = await productService.getProductDescription('MCO123456789');
```

### SellerService Mock

```javascript
const sellerService = mockContainer.getSellerService();

// Información del vendedor
const seller = await sellerService.getSellerById(123456);

// Reputación
const reputation = await sellerService.getSellerReputation(123456);

// Productos del vendedor
const items = await sellerService.getSellerItems(123456, { limit: 10 });
```

### PaymentService Mock

```javascript
const paymentService = mockContainer.getPaymentService();

// Métodos de pago
const methods = await paymentService.getPaymentMethods('MCO');

// Calcular cuotas
const installments = await paymentService.calculateInstallments(
  4899000, 'credit_card', 'MCO'
);

// Procesar pago (simulado)
const payment = await paymentService.processPayment({
  payment_method_id: 'credit_card',
  transaction_amount: 4899000,
  currency_id: 'COP'
});
```

### QuestionsService Mock

```javascript
const questionsService = mockContainer.getQuestionsService();

// Preguntas de un producto
const questions = await questionsService.getQuestionsByItem('MCO123456789');

// Crear pregunta
const newQuestion = await questionsService.createQuestion(
  'MCO123456789', 
  '¿Está liberado para todas las operadoras?', 
  'user_123'
);

// Responder pregunta
const answered = await questionsService.answerQuestion(
  12345001, 
  'Sí, completamente liberado.'
);
```

### ReviewsService Mock

```javascript
const reviewsService = mockContainer.getReviewsService();

// Opiniones de un producto
const reviews = await reviewsService.getReviewsByItem('MCO123456789');

// Crear opinión
const newReview = await reviewsService.createReview('MCO123456789', {
  rating: 5,
  title: 'Excelente producto',
  content: 'Muy satisfecho con la compra',
  userId: 'user_123'
});

// Dar like a opinión
const liked = await reviewsService.likeReview('rev_001');
```

## 🎛️ Configuración Avanzada

### Personalizar Delays

```javascript
// En configuración
mockData: {
  delay: 500,        // Respuestas más rápidas
  errorRate: 0,      // Sin errores simulados
  enableLogs: false  // Sin logs en consola
}
```

### Simular Errores

```javascript
// Los servicios mock simulan errores aleatorios
// Configurar tasa de error:
mockData: {
  errorRate: 0.1  // 10% de probabilidad de error
}
```

### Logging Personalizado

```javascript
import { mockLogger } from '../mocks/data/index.js';

// En servicios mock
mockLogger.log('ServiceName', 'methodName', { data });
mockLogger.error('ServiceName', 'methodName', error);
```

## 🔍 Debugging

### Ver Estado de Mocks

```javascript
import { getMockContainer } from '../mocks/index.js';

const container = getMockContainer();

console.log('Mocks activos:', container.isActive());
console.log('Configuración:', container.getConfig());
console.log('Servicios disponibles:', Object.keys(container.services));
```

### Logs en Consola

Cuando `enableLogs: true`, verás en consola:

```
[MOCK] ProductService.getProductById { id: 'MCO123456789' }
[MOCK] SellerService.getSellerById { id: 123456 }
[MOCK ERROR] PaymentService.processPayment Error: Simulated network error
```

## 🚀 Desarrollo

### Agregar Nuevos Datos Mock

1. **Crear datos en `/data/`**:
```javascript
// src/mocks/data/newEntity.js
export const mockNewEntity = {
  'id1': { /* datos */ },
  'id2': { /* datos */ }
};
```

2. **Exportar en index.js**:
```javascript
// src/mocks/data/index.js
export * from './newEntity.js';
```

### Crear Nuevo Servicio Mock

1. **Crear servicio**:
```javascript
// src/mocks/services/MockNewService.js
export class MockNewService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'NewService';
  }
  
  async someMethod() {
    mockLogger.log(this.serviceName, 'someMethod');
    await simulateNetworkDelay(this.config.mockDelay);
    return { success: true, data: {} };
  }
}
```

2. **Agregar al factory**:
```javascript
// src/mocks/services/index.js
export { MockNewService } from './MockNewService.js';

// En MockServiceFactory
createNewService() {
  return new MockNewService(this.config);
}
```

## 📋 Checklist de Desarrollo

- ✅ Datos mock realistas para todos los servicios
- ✅ Simulación de delays de red
- ✅ Simulación de errores aleatorios
- ✅ Logging configurable
- ✅ Integración transparente con servicios reales
- ✅ Configuración por ambiente
- ✅ Documentación completa

## 🤝 Contribuir

1. Agregar datos mock más variados
2. Implementar casos edge más realistas
3. Mejorar simulación de errores específicos
4. Agregar métricas de performance
5. Expandir logging y debugging
