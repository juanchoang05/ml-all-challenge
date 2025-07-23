// Mock Service para SellerService
import { 
  getSellerById, 
  getSellerItems, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockSellerService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'SellerService';
  }

  async getSellerById(id) {
    mockLogger.log(this.serviceName, 'getSellerById', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.04);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      return {
        success: true,
        data: seller
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerById', error);
      throw error;
    }
  }

  async getSellerReputation(id) {
    mockLogger.log(this.serviceName, 'getSellerReputation', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.03);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      return {
        success: true,
        data: seller.seller_reputation || {}
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerReputation', error);
      throw error;
    }
  }

  async getSellerItems(id, options = {}) {
    mockLogger.log(this.serviceName, 'getSellerItems', { id, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.03);
      
      const results = getSellerItems(parseInt(id), options);
      
      return {
        success: true,
        data: results
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerItems', error);
      throw error;
    }
  }

  async getSellerInfo(id) {
    mockLogger.log(this.serviceName, 'getSellerInfo', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.04);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      // Información básica del vendedor
      const sellerInfo = {
        id: seller.id,
        nickname: seller.nickname,
        first_name: seller.first_name,
        last_name: seller.last_name,
        user_type: seller.user_type,
        registration_date: seller.registration_date,
        country_id: seller.country_id,
        address: seller.address,
        phone: seller.phone,
        logo: seller.logo,
        permalink: seller.permalink,
        seller_experience: seller.seller_experience,
        tags: seller.tags,
        points: seller.points
      };
      
      return {
        success: true,
        data: sellerInfo
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerInfo', error);
      throw error;
    }
  }

  async getSellerStatus(id) {
    mockLogger.log(this.serviceName, 'getSellerStatus', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      return {
        success: true,
        data: seller.status || {}
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerStatus', error);
      throw error;
    }
  }

  async getSellerMetrics(id) {
    mockLogger.log(this.serviceName, 'getSellerMetrics', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      const metrics = seller.seller_reputation?.metrics || {};
      
      return {
        success: true,
        data: metrics
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerMetrics', error);
      throw error;
    }
  }

  async getSellerContactInfo(id) {
    mockLogger.log(this.serviceName, 'getSellerContactInfo', { id });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      const seller = getSellerById(parseInt(id));
      
      if (!seller) {
        throw new Error(`Seller with id ${id} not found`);
      }
      
      const contactInfo = {
        phone: seller.phone,
        alternative_phone: seller.alternative_phone,
        email: seller.email,
        address: seller.address
      };
      
      return {
        success: true,
        data: contactInfo
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSellerContactInfo', error);
      throw error;
    }
  }
}
