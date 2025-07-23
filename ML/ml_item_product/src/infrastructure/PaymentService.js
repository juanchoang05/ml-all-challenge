// Servicio para métodos de pago y financiamiento
class PaymentService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getSellerPaymentMethods(sellerId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/users/${sellerId}/accepted_payment_methods`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch payment methods for seller ${sellerId}: ${error.message}`);
    }
  }

  async getPaymentMethods(siteId = 'MCO') {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/sites/${siteId}/payment_methods`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch payment methods: ${error.message}`);
    }
  }

  async getInstallments(amount, paymentMethodId) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/sites/MCO/payment_methods/installments?amount=${amount}&payment_method_id=${paymentMethodId}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch installments: ${error.message}`);
    }
  }

  async getShippingMethods(sellerId, zipCode = null) {
    try {
      let url = `${this.baseUrl}/users/${sellerId}/shipping_preferences`;
      if (zipCode) {
        url += `?zip_code=${zipCode}`;
      }
      
      const response = await this.apiClient.get(url);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch shipping methods: ${error.message}`);
    }
  }

  async calculateShipping(itemId, quantity, zipCode) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/items/${itemId}/shipping_options?zip_code=${zipCode}&quantity=${quantity}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to calculate shipping: ${error.message}`);
    }
  }

  async getPaymentMethodById(paymentMethodId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/payment_methods/${paymentMethodId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch payment method ${paymentMethodId}: ${error.message}`);
    }
  }

  async validatePaymentData(paymentData) {
    try {
      // Validar datos de pago antes del procesamiento
      const response = await this.apiClient.post(`${this.baseUrl}/payments/validate`, paymentData);
      return response.data;
    } catch (error) {
      throw new Error(`Payment validation failed: ${error.message}`);
    }
  }
}

export default PaymentService;
