// Servicio para interactuar con la API de productos de MercadoLibre
class ProductService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getById(productId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product ${productId}: ${error.message}`);
    }
  }

  async getDescription(productId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/description`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product description ${productId}: ${error.message}`);
    }
  }

  async getByIds(productIds) {
    try {
      const ids = productIds.join(',');
      const response = await this.apiClient.get(`${this.baseUrl}/items?ids=${ids}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch products: ${error.message}`);
    }
  }

  async search(query, filters = {}) {
    try {
      const params = new URLSearchParams({
        q: query,
        ...filters
      });
      
      const response = await this.apiClient.get(`${this.baseUrl}/sites/MCO/search?${params}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to search products: ${error.message}`);
    }
  }

  async getAvailableFilters(categoryId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/MCO/search?category=${categoryId}&limit=1`);
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
