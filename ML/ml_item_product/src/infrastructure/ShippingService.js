// Servicio para cálculo de envíos
class ShippingService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getShippingOptions(sellerId, zipCode, itemId, quantity = 1) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/items/${itemId}/shipping_options?zip_code=${zipCode}&quantity=${quantity}`
      );
      return response.data.options || [];
    } catch (error) {
      throw new Error(`Failed to fetch shipping options: ${error.message}`);
    }
  }

  async calculateShippingCost(itemId, zipCode, quantity = 1) {
    try {
      const options = await this.getShippingOptions(null, zipCode, itemId, quantity);
      return options.map(option => ({
        id: option.id,
        name: option.name,
        cost: option.cost,
        currency: option.currency_id,
        estimatedDeliveryTime: option.estimated_delivery_time
      }));
    } catch (error) {
      throw new Error(`Failed to calculate shipping cost: ${error.message}`);
    }
  }

  async getAvailableShippingMethods(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}/shipping_preferences`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch shipping methods: ${error.message}`);
    }
  }

  async validateZipCode(zipCode, countryId = 'CO') {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/countries/${countryId}/zip_codes/${zipCode}`);
      return response.data;
    } catch (error) {
      throw new Error(`Invalid zip code: ${error.message}`);
    }
  }

  async getShippingPromise(itemId, zipCode) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/items/${itemId}/shipping_promise?zip_code=${zipCode}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch shipping promise: ${error.message}`);
    }
  }

  async getDropOffPoints(zipCode, serviceId) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/shipping_services/${serviceId}/drop_off_points?zip_code=${zipCode}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch drop off points: ${error.message}`);
    }
  }

  async getShippingModes(itemId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${itemId}`);
      const item = response.data;
      
      return {
        freeShipping: item.shipping?.free_shipping || false,
        mode: item.shipping?.mode || 'not_specified',
        localPickUp: item.shipping?.local_pick_up || false,
        tags: item.shipping?.tags || [],
        logisticType: item.shipping?.logistic_type || null
      };
    } catch (error) {
      throw new Error(`Failed to fetch shipping modes: ${error.message}`);
    }
  }
}

export default ShippingService;
