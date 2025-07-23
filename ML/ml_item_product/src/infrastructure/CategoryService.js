// Servicio para categorías y navegación
class CategoryService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getCategoryById(categoryId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/categories/${categoryId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch category ${categoryId}: ${error.message}`);
    }
  }

  async getCategoryPath(categoryId) {
    try {
      const category = await this.getCategoryById(categoryId);
      return category.path_from_root || [];
    } catch (error) {
      throw new Error(`Failed to fetch category path: ${error.message}`);
    }
  }

  async getProductWithCategory(productId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product with category: ${error.message}`);
    }
  }

  async getCategoryAttributes(categoryId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/categories/${categoryId}/attributes`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch category attributes: ${error.message}`);
    }
  }

  async getSubcategories(categoryId) {
    try {
      const category = await this.getCategoryById(categoryId);
      return category.children_categories || [];
    } catch (error) {
      throw new Error(`Failed to fetch subcategories: ${error.message}`);
    }
  }

  async getSiteCategories(siteId = 'MCO') {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/${siteId}/categories`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch site categories: ${error.message}`);
    }
  }

  async searchCategories(query) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/MCO/category_predictor/predict?q=${encodeURIComponent(query)}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to search categories: ${error.message}`);
    }
  }

  async getTrendingCategories(siteId = 'MCO') {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/${siteId}/trends`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch trending categories: ${error.message}`);
    }
  }

  async getCategoryFilters(categoryId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/MCO/search?category=${categoryId}&limit=1`);
      return response.data.available_filters || [];
    } catch (error) {
      throw new Error(`Failed to fetch category filters: ${error.message}`);
    }
  }
}

export default CategoryService;
