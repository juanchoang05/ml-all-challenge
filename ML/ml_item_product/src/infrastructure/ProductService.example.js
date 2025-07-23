// Ejemplo de cómo actualizar otros servicios para usar la configuración
import { getConfig } from '../config/index.js';

class ProductService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.config = getConfig();
  }

  async getById(productId) {
    try {
      const url = this.config.buildUrl('itemById', { id: productId });
      const response = await this.apiClient.get(url);
      
      this.config.log('Product fetched successfully', { productId });
      return response.data;
    } catch (error) {
      this.config.warn('Failed to fetch product', { productId, error: error.message });
      throw new Error(`Failed to fetch product ${productId}: ${error.message}`);
    }
  }

  async getDescription(productId) {
    try {
      const url = this.config.buildUrl('itemDescription', { id: productId });
      const response = await this.apiClient.get(url);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product description ${productId}: ${error.message}`);
    }
  }

  async getByIds(productIds) {
    try {
      const ids = productIds.join(',');
      const baseUrl = this.config.getBaseUrl();
      const endpoint = this.config.getEndpoint('items');
      const url = `${baseUrl}${endpoint}?ids=${ids}`;
      
      const response = await this.apiClient.get(url);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch products: ${error.message}`);
    }
  }

  async search(query, filters = {}) {
    try {
      const siteId = filters.siteId || this.config.getDefault('siteId');
      const limit = filters.limit || this.config.getDefault('limit');
      const offset = filters.offset || this.config.getDefault('offset');
      
      const url = this.config.buildUrl('search', { siteId });
      const params = new URLSearchParams({
        q: query,
        limit,
        offset,
        ...filters
      });
      
      const response = await this.apiClient.get(`${url}?${params}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to search products: ${error.message}`);
    }
  }

  async getAvailableFilters(categoryId) {
    try {
      const siteId = this.config.getDefault('siteId');
      const url = this.config.buildUrl('search', { siteId });
      const response = await this.apiClient.get(`${url}?category=${categoryId}&limit=1`);
      return response.data.available_filters || [];
    } catch (error) {
      throw new Error(`Failed to fetch available filters: ${error.message}`);
    }
  }

  async getRelatedProducts(productId, limit = 10) {
    try {
      // Simular productos relacionados basados en categoría
      const product = await this.getById(productId);
      const searchResponse = await this.search('', {
        category: product.category_id,
        limit,
        offset: 0
      });
      
      return searchResponse.results.filter(item => item.id !== productId);
    } catch (error) {
      throw new Error(`Failed to fetch related products: ${error.message}`);
    }
  }
}

export default ProductService;
