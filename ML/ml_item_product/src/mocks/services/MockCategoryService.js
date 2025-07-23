// Mock Service para CategoryService
import { 
  getCategoryById, 
  getCategoriesBySite, 
  searchCategories, 
  getCategoryBreadcrumb, 
  getTopCategories, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockCategoryService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'CategoryService';
  }

  async getCategoryById(categoryId) {
    mockLogger.log(this.serviceName, 'getCategoryById', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const category = getCategoryById(categoryId);
      
      if (!category) {
        throw new Error(`Category with id ${categoryId} not found`);
      }
      
      return {
        success: true,
        data: category
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoryById', error);
      throw error;
    }
  }

  async getCategoriesBySite(siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getCategoriesBySite', { siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const categories = getCategoriesBySite(siteId);
      
      return {
        success: true,
        data: categories
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoriesBySite', error);
      throw error;
    }
  }

  async searchCategories(query, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'searchCategories', { query, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.03);
      
      const results = searchCategories(query, siteId);
      
      return {
        success: true,
        data: results
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'searchCategories', error);
      throw error;
    }
  }

  async getCategoryBreadcrumb(categoryId) {
    mockLogger.log(this.serviceName, 'getCategoryBreadcrumb', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const breadcrumb = getCategoryBreadcrumb(categoryId);
      
      return {
        success: true,
        data: breadcrumb
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoryBreadcrumb', error);
      throw error;
    }
  }

  async getTopCategories(siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getTopCategories', { siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const topCategories = getTopCategories(siteId);
      
      return {
        success: true,
        data: topCategories
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getTopCategories', error);
      throw error;
    }
  }

  async getCategoryAttributes(categoryId) {
    mockLogger.log(this.serviceName, 'getCategoryAttributes', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const category = getCategoryById(categoryId);
      
      if (!category) {
        throw new Error(`Category with id ${categoryId} not found`);
      }
      
      return {
        success: true,
        data: category.attributes || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoryAttributes', error);
      throw error;
    }
  }

  async getCategoryChildren(categoryId) {
    mockLogger.log(this.serviceName, 'getCategoryChildren', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const category = getCategoryById(categoryId);
      
      if (!category) {
        throw new Error(`Category with id ${categoryId} not found`);
      }
      
      return {
        success: true,
        data: category.children_categories || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoryChildren', error);
      throw error;
    }
  }

  async getCategoryStats(categoryId) {
    mockLogger.log(this.serviceName, 'getCategoryStats', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const category = getCategoryById(categoryId);
      
      if (!category) {
        throw new Error(`Category with id ${categoryId} not found`);
      }
      
      const stats = {
        category_id: categoryId,
        total_items: category.total_items_in_this_category || 0,
        active_items: Math.floor((category.total_items_in_this_category || 0) * 0.85),
        new_items_last_week: Math.floor(Math.random() * 100),
        avg_price: Math.floor(Math.random() * 1000000) + 50000,
        top_brands: ['Apple', 'Samsung', 'Xiaomi', 'Huawei', 'Motorola'].slice(0, 3),
        price_range: {
          min: 50000,
          max: 10000000,
          currency_id: 'COP'
        }
      };
      
      return {
        success: true,
        data: stats
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCategoryStats', error);
      throw error;
    }
  }
}
