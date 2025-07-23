// Servicio para información de vendedores
class SellerService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getById(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller ${sellerId}: ${error.message}`);
    }
  }

  async getSellerItems(sellerId, limit = 50, offset = 0) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/users/${sellerId}/items/search?limit=${limit}&offset=${offset}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller items: ${error.message}`);
    }
  }

  async getSellerReputation(sellerId) {
    try {
      const seller = await this.getById(sellerId);
      return seller.seller_reputation;
    } catch (error) {
      throw new Error(`Failed to fetch seller reputation: ${error.message}`);
    }
  }

  async getSellerAddresses(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}/addresses`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller addresses: ${error.message}`);
    }
  }

  async getSellerShippingPreferences(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}/shipping_preferences`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch shipping preferences: ${error.message}`);
    }
  }

  async getSellerBrands(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}/brands`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller brands: ${error.message}`);
    }
  }

  async isOfficialStore(sellerId) {
    try {
      const seller = await this.getById(sellerId);
      return seller.tags?.includes('eshop') || false;
    } catch (error) {
      console.warn(`Could not determine if seller is official store: ${error.message}`);
      return false;
    }
  }

  async getSellerStats(sellerId) {
    try {
      const seller = await this.getById(sellerId);
      const reputation = seller.seller_reputation;
      
      return {
        totalSales: reputation?.transactions?.total || 0,
        positiveRating: reputation?.transactions?.ratings?.positive || 0,
        neutralRating: reputation?.transactions?.ratings?.neutral || 0,
        negativeRating: reputation?.transactions?.ratings?.negative || 0,
        powerSellerStatus: reputation?.power_seller_status || null,
        levelId: reputation?.level_id || null,
        registrationDate: seller.registration_date
      };
    } catch (error) {
      throw new Error(`Failed to fetch seller stats: ${error.message}`);
    }
  }
}

export default SellerService;
