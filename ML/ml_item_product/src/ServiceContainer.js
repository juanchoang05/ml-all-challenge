// Configuración y factory para instanciar servicios, adaptadores y casos de uso
import {
  ApiClient,
  ProductService,
  ImageService,
  PaymentService,
  SellerService,
  SpecificationsService,
  QuestionsService,
  ReviewsService,
  CategoryService,
  PurchaseService,
  ShippingService
} from './infrastructure/index.js';

import {
  ProductAdapter,
  GalleryAdapter,
  PaymentAdapter,
  SellerAdapter,
  SpecificationsAdapter,
  QuestionsAdapter,
  ReviewsAdapter,
  NavigationAdapter,
  PurchaseAdapter
} from './adapters/index.js';

import {
  GetProductInfoUseCase,
  GetSellerInfoUseCase,
  GetPaymentMethodsUseCase,
  GetProductQuestionsUseCase,
  GetProductReviewsUseCase,
  PurchaseProductUseCase,
  GetNavigationUseCase
} from './application/index.js';

class ServiceContainer {
  constructor(config = {}) {
    this.config = {
      apiBaseUrl: 'https://api.mercadolibre.com',
      timeout: 10000,
      ...config
    };
    
    this._initializeServices();
    this._initializeAdapters();
    this._initializeUseCases();
  }

  _initializeServices() {
    // Cliente HTTP
    this.apiClient = new ApiClient({
      baseURL: this.config.apiBaseUrl,
      timeout: this.config.timeout
    });

    // Servicios de infraestructura
    this.productService = new ProductService(this.apiClient);
    this.imageService = new ImageService(this.apiClient);
    this.paymentService = new PaymentService(this.apiClient);
    this.sellerService = new SellerService(this.apiClient);
    this.specificationsService = new SpecificationsService(this.apiClient);
    this.questionsService = new QuestionsService(this.apiClient);
    this.reviewsService = new ReviewsService(this.apiClient);
    this.categoryService = new CategoryService(this.apiClient);
    this.purchaseService = new PurchaseService(this.apiClient);
    this.shippingService = new ShippingService(this.apiClient);
  }

  _initializeAdapters() {
    // Adaptadores
    this.productAdapter = new ProductAdapter(this.productService);
    this.galleryAdapter = new GalleryAdapter(this.imageService);
    this.paymentAdapter = new PaymentAdapter(this.paymentService);
    this.sellerAdapter = new SellerAdapter(this.sellerService);
    this.specificationsAdapter = new SpecificationsAdapter(this.specificationsService);
    this.questionsAdapter = new QuestionsAdapter(this.questionsService);
    this.reviewsAdapter = new ReviewsAdapter(this.reviewsService);
    this.navigationAdapter = new NavigationAdapter(this.categoryService);
    this.purchaseAdapter = new PurchaseAdapter(this.purchaseService, this.shippingService);
  }

  _initializeUseCases() {
    // Casos de uso
    this.getProductInfo = new GetProductInfoUseCase(
      this.productAdapter,
      this.galleryAdapter,
      this.specificationsAdapter
    );

    this.getSellerInfo = new GetSellerInfoUseCase(this.sellerAdapter);

    this.getPaymentMethods = new GetPaymentMethodsUseCase(this.paymentAdapter);

    this.getProductQuestions = new GetProductQuestionsUseCase(this.questionsAdapter);

    this.getProductReviews = new GetProductReviewsUseCase(this.reviewsAdapter);

    this.purchaseProduct = new PurchaseProductUseCase(
      this.purchaseAdapter,
      this.paymentAdapter
    );

    this.getNavigation = new GetNavigationUseCase(this.navigationAdapter);
  }

  // Método para obtener todos los casos de uso necesarios para la página de producto
  getProductPageUseCases() {
    return {
      getProductInfo: this.getProductInfo,
      getSellerInfo: this.getSellerInfo,
      getPaymentMethods: this.getPaymentMethods,
      getProductQuestions: this.getProductQuestions,
      getProductReviews: this.getProductReviews,
      purchaseProduct: this.purchaseProduct,
      getNavigation: this.getNavigation
    };
  }

  // Método para configurar autenticación si es necesario
  setAuthToken(token) {
    this.apiClient.setAuthToken(token);
  }

  // Método para limpiar autenticación
  clearAuth() {
    this.apiClient.removeAuthToken();
  }
}

// Instancia singleton del contenedor de servicios
let serviceContainer = null;

export const getServiceContainer = (config) => {
  if (!serviceContainer) {
    serviceContainer = new ServiceContainer(config);
  }
  return serviceContainer;
};

export const initializeServices = (config) => {
  serviceContainer = new ServiceContainer(config);
  return serviceContainer;
};

export default ServiceContainer;
