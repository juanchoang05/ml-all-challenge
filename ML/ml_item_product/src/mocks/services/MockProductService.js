// Mock Service para ProductService
import { 
  getProductById, 
  searchProducts, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockProductService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'ProductService';
  }

  async getProductById(id) {
    mockLogger.log(this.serviceName, 'getProductById', { id });
    
    try {
      // Simular delay de red
      await simulateNetworkDelay(this.config.mockDelay || 800);
      
      // Simular posibles errores
      simulateRandomError(0.05);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      return {
        success: true,
        data: product
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductById', error);
      throw error;
    }
  }

  async getProductDescription(id) {
    mockLogger.log(this.serviceName, 'getProductDescription', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      const description = product.descriptions?.[0] || {
        id: `${id}-desc`,
        text: `Descripción detallada del producto ${product.title}. Este es un producto de alta calidad que cumple con todas las especificaciones técnicas requeridas.`
      };
      
      return {
        success: true,
        data: description
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductDescription', error);
      throw error;
    }
  }

  async searchProducts(query, options = {}) {
    mockLogger.log(this.serviceName, 'searchProducts', { query, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1200);
      simulateRandomError(0.02);
      
      const results = searchProducts(query, options);
      
      return {
        success: true,
        data: results
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'searchProducts', error);
      throw error;
    }
  }

  async getProductAttributes(id) {
    mockLogger.log(this.serviceName, 'getProductAttributes', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.03);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      return {
        success: true,
        data: product.attributes || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductAttributes', error);
      throw error;
    }
  }

  async getProductPictures(id) {
    mockLogger.log(this.serviceName, 'getProductPictures', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      return {
        success: true,
        data: product.pictures || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductPictures', error);
      throw error;
    }
  }

  async getProductShipping(id) {
    mockLogger.log(this.serviceName, 'getProductShipping', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.04);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      return {
        success: true,
        data: product.shipping || {}
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductShipping', error);
      throw error;
    }
  }

  async getRelatedProducts(id, limit = 10) {
    mockLogger.log(this.serviceName, 'getRelatedProducts', { id, limit });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 900);
      simulateRandomError(0.03);
      
      const product = getProductById(id);
      
      if (!product) {
        throw new Error(`Product with id ${id} not found`);
      }
      
      // Simular productos relacionados usando searchProducts
      const relatedResults = searchProducts('', { limit, offset: 0 });
      
      // Filtrar el producto actual de los resultados
      const related = relatedResults.results
        .filter(p => p.id !== id)
        .slice(0, limit);
      
      return {
        success: true,
        data: related
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getRelatedProducts', error);
      throw error;
    }
  }
}
