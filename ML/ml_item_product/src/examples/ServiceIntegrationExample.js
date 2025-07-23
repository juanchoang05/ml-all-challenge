// Ejemplo de integración de mocks en servicio existente
// Este archivo muestra cómo actualizar un servicio de infraestructura
// para usar automáticamente mocks en desarrollo

import { getMockContainer } from '../mocks/index.js';
import { getConfig } from '../config/index.js';

export class ProductService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.config = getConfig();
    this.mockContainer = getMockContainer();
  }

  /**
   * Determinar si debo usar mocks basado en configuración
   */
  shouldUseMocks() {
    return this.config.features?.useMockData || 
           this.config.features?.enableMockData ||
           process.env.REACT_APP_USE_MOCK_DATA === 'true';
  }

  /**
   * Obtener producto por ID
   * Usa mock automáticamente si está configurado
   */
  async getById(productId) {
    try {
      // Usar mock si está configurado
      if (this.shouldUseMocks()) {
        const mockService = this.mockContainer.getProductService();
        return await mockService.getProductById(productId);
      }

      // Usar API real
      const response = await this.apiClient.get(`/items/${productId}`);
      return response.data;
    } catch (error) {
      console.error('Error getting product:', error);
      
      // Fallback a mock en caso de error (opcional)
      if (this.config.features?.fallbackToMock) {
        console.log('Falling back to mock data...');
        const mockService = this.mockContainer.getProductService();
        return await mockService.getProductById(productId);
      }
      
      throw error;
    }
  }

  /**
   * Obtener descripción del producto
   */
  async getDescription(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      return await mockService.getProductDescription(productId);
    }

    const response = await this.apiClient.get(`/items/${productId}/description`);
    return response.data;
  }

  /**
   * Buscar productos
   */
  async search(query, options = {}) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      return await mockService.searchProducts(query, options);
    }

    const params = new URLSearchParams({
      q: query,
      limit: options.limit || 50,
      offset: options.offset || 0,
      ...options
    });

    const response = await this.apiClient.get(`/sites/${this.config.region.siteId}/search?${params}`);
    return response.data;
  }

  /**
   * Obtener productos relacionados
   */
  async getRelated(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      return await mockService.getRelatedProducts(productId);
    }

    const response = await this.apiClient.get(`/items/${productId}/related`);
    return response.data;
  }
}

// Ejemplo de uso en un caso de uso
export class GetProductUseCase {
  constructor(productService, sellerService, reviewsService) {
    this.productService = productService;
    this.sellerService = sellerService;
    this.reviewsService = reviewsService;
  }

  async execute(productId) {
    try {
      // Los servicios automáticamente usan mocks si están configurados
      const [product, seller, reviews] = await Promise.all([
        this.productService.getById(productId),
        this.sellerService.getById(product.seller_id),
        this.reviewsService.getByItemId(productId)
      ]);

      return {
        product,
        seller,
        reviews,
        // Metadatos útiles para debugging
        usingMocks: this.productService.shouldUseMocks()
      };
    } catch (error) {
      console.error('Error in GetProductUseCase:', error);
      throw error;
    }
  }
}

// Ejemplo de configuración del contenedor de servicios
export function createServiceContainer() {
  const config = getConfig();
  const apiClient = createApiClient(config);

  // Los servicios se configuran una vez y automáticamente
  // detectan si deben usar mocks
  const productService = new ProductService(apiClient);
  const sellerService = new SellerService(apiClient);
  const reviewsService = new ReviewsService(apiClient);
  
  return {
    productService,
    sellerService,
    reviewsService,
    
    // Casos de uso
    getProductUseCase: new GetProductUseCase(
      productService,
      sellerService,
      reviewsService
    )
  };
}
