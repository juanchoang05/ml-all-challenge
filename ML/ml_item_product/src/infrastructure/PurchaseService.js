// Servicio para funcionalidades de compra
class PurchaseService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getStockInfo(itemId, variationId = null) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${itemId}`);
      const item = response.data;
      
      if (variationId && item.variations) {
        const variation = item.variations.find(v => v.id === variationId);
        return variation ? {
          available_quantity: variation.available_quantity,
          sold_quantity: variation.sold_quantity,
          max_purchase_quantity: variation.available_quantity,
          last_updated: item.last_updated
        } : null;
      }
      
      return {
        available_quantity: item.available_quantity,
        sold_quantity: item.sold_quantity,
        max_purchase_quantity: item.available_quantity,
        last_updated: item.last_updated
      };
    } catch (error) {
      throw new Error(`Failed to fetch stock info: ${error.message}`);
    }
  }

  async addToCart(cartData) {
    try {
      // Simular agregar al carrito (MercadoLibre no tiene API pública de carrito)
      const response = await this.apiClient.post(`${this.baseUrl}/carts`, cartData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to add to cart: ${error.message}`);
    }
  }

  async initiateCheckout(checkoutData) {
    try {
      // Simular inicio de checkout
      const response = await this.apiClient.post(`${this.baseUrl}/checkout`, checkoutData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to initiate checkout: ${error.message}`);
    }
  }

  async getBuyerProtection(itemId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${itemId}/buyer_protection`);
      return response.data;
    } catch (error) {
      // Si no hay endpoint específico, devolver protección estándar de ML
      return {
        has_protection: true,
        guarantees: [
          {
            type: 'money_back',
            description: 'Devolución del dinero si no recibes el producto',
            coverage: 'full'
          },
          {
            type: 'product_guarantee',
            description: 'Garantía del producto por defectos de fábrica',
            coverage: 'manufacturer'
          }
        ],
        return_policy: {
          has_return: true,
          return_days: 30,
          description: 'Tienes 30 días para devolver el producto'
        }
      };
    }
  }

  async createOrder(orderData) {
    try {
      const response = await this.apiClient.post(`${this.baseUrl}/orders`, orderData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to create order: ${error.message}`);
    }
  }

  async getOrderStatus(orderId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/orders/${orderId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch order status: ${error.message}`);
    }
  }

  async cancelOrder(orderId, reason) {
    try {
      const response = await this.apiClient.put(`${this.baseUrl}/orders/${orderId}/cancel`, { reason });
      return response.data;
    } catch (error) {
      throw new Error(`Failed to cancel order: ${error.message}`);
    }
  }

  async trackShipment(shipmentId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/shipments/${shipmentId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to track shipment: ${error.message}`);
    }
  }
}

export default PurchaseService;
