// Mock Service para PurchaseService
import { 
  getOrderById, 
  getOrdersByUser, 
  createOrder, 
  updateOrderStatus, 
  addPaymentToOrder, 
  mockCarts, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockPurchaseService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'PurchaseService';
  }

  async createCart(userId, itemId, quantity = 1) {
    mockLogger.log(this.serviceName, 'createCart', { userId, itemId, quantity });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const cartId = `cart_${Date.now()}`;
      const cart = {
        id: cartId,
        site_id: 'MCO',
        buyer_id: userId,
        date_created: new Date().toISOString(),
        date_updated: new Date().toISOString(),
        expiration_date: new Date(Date.now() + 24 * 60 * 60 * 1000).toISOString(), // +24 horas
        items: [
          {
            id: itemId,
            quantity,
            unit_price: 4899000, // Precio mock
            currency_id: 'COP',
            available_quantity: 45,
            condition: 'new',
            warranty: '12 meses',
            shipping: {
              free_shipping: true,
              mode: 'me2'
            }
          }
        ],
        coupons: [],
        summary: {
          total_amount: 4899000 * quantity,
          shipping_cost: 0,
          total_amount_with_shipping: 4899000 * quantity,
          currency_id: 'COP',
          total_items: quantity
        },
        shipping_options: [
          {
            id: 'mercadoenvios_flex',
            name: 'Mercado Envíos Flex',
            cost: 0,
            estimated_delivery: '2024-01-26'
          }
        ]
      };
      
      mockCarts[cartId] = cart;
      
      return {
        success: true,
        data: cart
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'createCart', error);
      throw error;
    }
  }

  async getCart(cartId) {
    mockLogger.log(this.serviceName, 'getCart', { cartId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const cart = mockCarts[cartId];
      
      if (!cart) {
        throw new Error(`Cart with id ${cartId} not found`);
      }
      
      return {
        success: true,
        data: cart
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCart', error);
      throw error;
    }
  }

  async updateCartItem(cartId, itemId, quantity) {
    mockLogger.log(this.serviceName, 'updateCartItem', { cartId, itemId, quantity });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.03);
      
      const cart = mockCarts[cartId];
      
      if (!cart) {
        throw new Error(`Cart with id ${cartId} not found`);
      }
      
      const itemIndex = cart.items.findIndex(item => item.id === itemId);
      
      if (itemIndex === -1) {
        throw new Error(`Item ${itemId} not found in cart`);
      }
      
      cart.items[itemIndex].quantity = quantity;
      cart.summary.total_amount = cart.items.reduce((total, item) => 
        total + (item.unit_price * item.quantity), 0);
      cart.summary.total_amount_with_shipping = cart.summary.total_amount + cart.summary.shipping_cost;
      cart.summary.total_items = cart.items.reduce((total, item) => total + item.quantity, 0);
      cart.date_updated = new Date().toISOString();
      
      return {
        success: true,
        data: cart
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'updateCartItem', error);
      throw error;
    }
  }

  async removeCartItem(cartId, itemId) {
    mockLogger.log(this.serviceName, 'removeCartItem', { cartId, itemId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const cart = mockCarts[cartId];
      
      if (!cart) {
        throw new Error(`Cart with id ${cartId} not found`);
      }
      
      cart.items = cart.items.filter(item => item.id !== itemId);
      cart.summary.total_amount = cart.items.reduce((total, item) => 
        total + (item.unit_price * item.quantity), 0);
      cart.summary.total_amount_with_shipping = cart.summary.total_amount + cart.summary.shipping_cost;
      cart.summary.total_items = cart.items.reduce((total, item) => total + item.quantity, 0);
      cart.date_updated = new Date().toISOString();
      
      return {
        success: true,
        data: cart
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'removeCartItem', error);
      throw error;
    }
  }

  async createOrder(cartId, paymentData, shippingData) {
    mockLogger.log(this.serviceName, 'createOrder', { cartId, paymentData, shippingData });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1200);
      simulateRandomError(0.05);
      
      const order = createOrder(cartId, paymentData, shippingData);
      
      return {
        success: true,
        data: order
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'createOrder', error);
      throw error;
    }
  }

  async getOrder(orderId) {
    mockLogger.log(this.serviceName, 'getOrder', { orderId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const order = getOrderById(orderId);
      
      if (!order) {
        throw new Error(`Order with id ${orderId} not found`);
      }
      
      return {
        success: true,
        data: order
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getOrder', error);
      throw error;
    }
  }

  async getOrdersByUser(userId, options = {}) {
    mockLogger.log(this.serviceName, 'getOrdersByUser', { userId, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.03);
      
      const orders = getOrdersByUser(userId, options);
      
      return {
        success: true,
        data: orders
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getOrdersByUser', error);
      throw error;
    }
  }

  async updateOrderStatus(orderId, status, statusDetail) {
    mockLogger.log(this.serviceName, 'updateOrderStatus', { orderId, status, statusDetail });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const order = updateOrderStatus(orderId, status, statusDetail);
      
      if (!order) {
        throw new Error(`Order with id ${orderId} not found`);
      }
      
      return {
        success: true,
        data: order
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'updateOrderStatus', error);
      throw error;
    }
  }

  async processPayment(orderId, paymentData) {
    mockLogger.log(this.serviceName, 'processPayment', { orderId, paymentData });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1500);
      simulateRandomError(0.08);
      
      const order = addPaymentToOrder(orderId, paymentData);
      
      if (!order) {
        throw new Error(`Order with id ${orderId} not found`);
      }
      
      return {
        success: true,
        data: order
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'processPayment', error);
      throw error;
    }
  }

  async cancelOrder(orderId, reason) {
    mockLogger.log(this.serviceName, 'cancelOrder', { orderId, reason });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.04);
      
      const order = updateOrderStatus(orderId, 'cancelled', {
        code: 'user_cancelled',
        description: reason || 'Cancelado por el usuario'
      });
      
      if (!order) {
        throw new Error(`Order with id ${orderId} not found`);
      }
      
      return {
        success: true,
        data: order
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'cancelOrder', error);
      throw error;
    }
  }

  async applyCoupon(cartId, couponCode) {
    mockLogger.log(this.serviceName, 'applyCoupon', { cartId, couponCode });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const cart = mockCarts[cartId];
      
      if (!cart) {
        throw new Error(`Cart with id ${cartId} not found`);
      }
      
      // Mock de cupón
      const mockCoupons = {
        'WELCOME10': { discount: 0.10, type: 'percentage', description: '10% de descuento' },
        'SAVE50000': { discount: 50000, type: 'fixed', description: 'Descuento de $50,000' },
        'FREESHIP': { discount: 0, type: 'free_shipping', description: 'Envío gratis' }
      };
      
      const coupon = mockCoupons[couponCode];
      
      if (!coupon) {
        throw new Error(`Coupon ${couponCode} is not valid`);
      }
      
      // Aplicar cupón
      cart.coupons = [
        {
          id: couponCode,
          ...coupon,
          amount: coupon.type === 'percentage' ? 
            cart.summary.total_amount * coupon.discount : 
            coupon.discount
        }
      ];
      
      // Recalcular totales
      const discountAmount = cart.coupons[0].amount;
      cart.summary.total_amount_with_shipping = cart.summary.total_amount - discountAmount;
      
      if (coupon.type === 'free_shipping') {
        cart.summary.shipping_cost = 0;
      }
      
      cart.date_updated = new Date().toISOString();
      
      return {
        success: true,
        data: cart
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'applyCoupon', error);
      throw error;
    }
  }
}
