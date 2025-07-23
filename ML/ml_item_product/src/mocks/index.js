// Índice principal del sistema de mocks
export * from './data/index.js';
export * from './services/index.js';

// MockServiceContainer - Contenedor de servicios mock
import { MockServiceFactory, shouldUseMocks, defaultMockConfig } from './services/index.js';
import { getConfig } from '../config/index.js';

export class MockServiceContainer {
  constructor() {
    this.config = getConfig();
    this.mockConfig = {
      ...defaultMockConfig,
      mockDelay: this.config.config.mockData?.delay || 1000,
      errorRate: this.config.config.mockData?.errorRate || 0.05,
      enableLogs: this.config.isFeatureEnabled('enableLogging')
    };
    
    this.factory = new MockServiceFactory(this.mockConfig);
    this.services = {};
    this.initialized = false;
  }

  // Inicializar todos los servicios mock
  initialize() {
    if (this.initialized) return;
    
    if (shouldUseMocks() || this.config.isFeatureEnabled('useMockData')) {
      this.services = this.factory.createAllServices();
      this.initialized = true;
      
      if (this.mockConfig.enableLogs) {
        console.log('[MOCK CONTAINER] Mock services initialized', {
          config: this.mockConfig,
          services: Object.keys(this.services)
        });
      }
    }
  }

  // Obtener servicio específico
  getService(serviceName) {
    if (!this.initialized) {
      this.initialize();
    }
    
    return this.services[serviceName] || null;
  }

  // Métodos para obtener servicios específicos
  getProductService() {
    return this.getService('productService');
  }

  getSellerService() {
    return this.getService('sellerService');
  }

  getPaymentService() {
    return this.getService('paymentService');
  }

  getQuestionsService() {
    return this.getService('questionsService');
  }

  getReviewsService() {
    return this.getService('reviewsService');
  }

  getCategoryService() {
    return this.getService('categoryService');
  }

  getShippingService() {
    return this.getService('shippingService');
  }

  getPurchaseService() {
    return this.getService('purchaseService');
  }

  getImageService() {
    return this.getService('imageService');
  }

  getSpecificationsService() {
    return this.getService('specificationsService');
  }

  // Verificar si los mocks están activos
  isActive() {
    return this.initialized && Object.keys(this.services).length > 0;
  }

  // Obtener configuración de mocks
  getConfig() {
    return this.mockConfig;
  }

  // Actualizar configuración de mocks
  updateConfig(newConfig) {
    this.mockConfig = { ...this.mockConfig, ...newConfig };
    
    // Reinicializar servicios con nueva configuración
    this.factory = new MockServiceFactory(this.mockConfig);
    this.services = {};
    this.initialized = false;
    this.initialize();
  }
}

// Instancia singleton del contenedor de mocks
let mockContainer = null;

export const getMockContainer = () => {
  if (!mockContainer) {
    mockContainer = new MockServiceContainer();
  }
  return mockContainer;
};

// Función helper para obtener servicios mock
export const getMockService = (serviceName) => {
  const container = getMockContainer();
  return container.getService(serviceName);
};

// Función para verificar si se deben usar mocks
export const useMockServices = () => {
  const container = getMockContainer();
  return container.isActive();
};
