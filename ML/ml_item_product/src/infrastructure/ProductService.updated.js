// Servicio para interactuar con la API de productos de MercadoLibre con soporte para mocks
import { getMockContainer } from '../mocks/index.js';
import { getConfig } from '../config/index.js';

class ProductService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.config = getConfig();
    this.baseUrl = this.config.getBaseUrl();
    this.mockContainer = getMockContainer();
  }

  // Verificar si se deben usar mocks
  shouldUseMocks() {
    return this.config.isFeatureEnabled('useMockData') || 
           this.config.isFeatureEnabled('enableMockData');
  }

  async getById(productId) {
    // Usar mock service si está configurado
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getById using mock data', { productId });
        return await mockService.getProductById(productId);
      }
    }

    // Usar API real
    try {
      this.config.log('ProductService.getById using real API', { productId });
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getById error', error);
      throw new Error(`Failed to fetch product ${productId}: ${error.message}`);
    }
  }

  async getDescription(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getDescription using mock data', { productId });
        return await mockService.getProductDescription(productId);
      }
    }

    try {
      this.config.log('ProductService.getDescription using real API', { productId });
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/description`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getDescription error', error);
      throw new Error(`Failed to fetch product description ${productId}: ${error.message}`);
    }
  }

  async getByIds(productIds) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getByIds using mock data', { productIds });
        // Para múltiples productos, llamar getById para cada uno
        const results = [];
        for (const id of productIds) {
          try {
            const result = await mockService.getProductById(id);
            results.push(result.data);
          } catch (error) {
            results.push({ id, error: error.message });
          }
        }
        return {
          success: true,
          data: results
        };
      }
    }

    try {
      this.config.log('ProductService.getByIds using real API', { productIds });
      const ids = productIds.join(',');
      const response = await this.apiClient.get(`${this.baseUrl}/items?ids=${ids}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getByIds error', error);
      throw new Error(`Failed to fetch products ${productIds.join(', ')}: ${error.message}`);
    }
  }

  async search(query, filters = {}) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.search using mock data', { query, filters });
        return await mockService.searchProducts(query, filters);
      }
    }

    try {
      this.config.log('ProductService.search using real API', { query, filters });
      const params = new URLSearchParams({
        q: query,
        ...filters
      });
      
      const response = await this.apiClient.get(`${this.baseUrl}/sites/${this.config.getRegionConfig().siteId}/search?${params}`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.search error', error);
      throw new Error(`Failed to search products: ${error.message}`);
    }
  }

  async getAttributes(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getAttributes using mock data', { productId });
        return await mockService.getProductAttributes(productId);
      }
    }

    try {
      this.config.log('ProductService.getAttributes using real API', { productId });
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/attributes`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getAttributes error', error);
      throw new Error(`Failed to fetch product attributes ${productId}: ${error.message}`);
    }
  }

  async getPictures(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getPictures using mock data', { productId });
        return await mockService.getProductPictures(productId);
      }
    }

    try {
      this.config.log('ProductService.getPictures using real API', { productId });
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/pictures`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getPictures error', error);
      throw new Error(`Failed to fetch product pictures ${productId}: ${error.message}`);
    }
  }

  async getShippingOptions(productId) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getShippingOptions using mock data', { productId });
        return await mockService.getProductShipping(productId);
      }
    }

    try {
      this.config.log('ProductService.getShippingOptions using real API', { productId });
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/shipping_options`);
      return {
        success: true,
        data: response.data
      };
    } catch (error) {
      this.config.error('ProductService.getShippingOptions error', error);
      throw new Error(`Failed to fetch shipping options ${productId}: ${error.message}`);
    }
  }

  async getRelatedProducts(productId, limit = 10) {
    if (this.shouldUseMocks()) {
      const mockService = this.mockContainer.getProductService();
      if (mockService) {
        this.config.log('ProductService.getRelatedProducts using mock data', { productId, limit });
        return await mockService.getRelatedProducts(productId, limit);
      }
    }

    try {
      this.config.log('ProductService.getRelatedProducts using real API', { productId, limit });
      // En la API real, esto podría ser una búsqueda por categoría o productos similares
      const product = await this.getById(productId);
      const categoryId = product.data.category_id;
      
      const response = await this.apiClient.get(
        `${this.baseUrl}/sites/${this.config.getRegionConfig().siteId}/search?category=${categoryId}&limit=${limit}`
      );
      
      // Filtrar el producto actual
      const relatedProducts = response.data.results.filter(p => p.id !== productId);
      
      return {
        success: true,
        data: relatedProducts
      };
    } catch (error) {
      this.config.error('ProductService.getRelatedProducts error', error);
      throw new Error(`Failed to fetch related products for ${productId}: ${error.message}`);
    }
  }
}

export default ProductService;
