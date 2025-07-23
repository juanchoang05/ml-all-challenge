// Índice centralizado de todos los servicios mock
export { MockProductService } from './MockProductService.js';
export { MockSellerService } from './MockSellerService.js';
export { MockPaymentService } from './MockPaymentService.js';
export { MockQuestionsService } from './MockQuestionsService.js';
export { MockReviewsService } from './MockReviewsService.js';
export { MockCategoryService } from './MockCategoryService.js';
export { MockShippingService } from './MockShippingService.js';
export { MockPurchaseService } from './MockPurchaseService.js';
export { MockImageService } from './MockImageService.js';
export { MockSpecificationsService } from './MockSpecificationsService.js';

// Factory para crear servicios mock
export class MockServiceFactory {
  constructor(config = {}) {
    this.config = config;
  }

  createProductService() {
    return new MockProductService(this.config);
  }

  createSellerService() {
    return new MockSellerService(this.config);
  }

  createPaymentService() {
    return new MockPaymentService(this.config);
  }

  createQuestionsService() {
    return new MockQuestionsService(this.config);
  }

  createReviewsService() {
    return new MockReviewsService(this.config);
  }

  createCategoryService() {
    return new MockCategoryService(this.config);
  }

  createShippingService() {
    return new MockShippingService(this.config);
  }

  createPurchaseService() {
    return new MockPurchaseService(this.config);
  }

  createImageService() {
    return new MockImageService(this.config);
  }

  createSpecificationsService() {
    return new MockSpecificationsService(this.config);
  }

  // Método para crear todos los servicios de una vez
  createAllServices() {
    return {
      productService: this.createProductService(),
      sellerService: this.createSellerService(),
      paymentService: this.createPaymentService(),
      questionsService: this.createQuestionsService(),
      reviewsService: this.createReviewsService(),
      categoryService: this.createCategoryService(),
      shippingService: this.createShippingService(),
      purchaseService: this.createPurchaseService(),
      imageService: this.createImageService(),
      specificationsService: this.createSpecificationsService()
    };
  }
}

// Función utilitaria para verificar si usar mocks
export const shouldUseMocks = () => {
  return process.env.NODE_ENV === 'development' && 
         (process.env.REACT_APP_USE_MOCK_DATA === 'true' || 
          process.env.REACT_APP_ENABLE_MOCK_DATA === 'true');
};

// Configuración de mock por defecto
export const defaultMockConfig = {
  mockDelay: 1000,
  errorRate: 0.05,
  enableLogs: true
};
