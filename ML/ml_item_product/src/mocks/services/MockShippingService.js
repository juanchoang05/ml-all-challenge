// Mock Service para ShippingService
import { 
  getShippingOptionsByItemId, 
  calculateShipping, 
  getPickupPoints, 
  trackShipment, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockShippingService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'ShippingService';
  }

  async getShippingOptions(itemId) {
    mockLogger.log(this.serviceName, 'getShippingOptions', { itemId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const options = getShippingOptionsByItemId(itemId);
      
      return {
        success: true,
        data: options
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getShippingOptions', error);
      throw error;
    }
  }

  async calculateShipping(itemId, zipCode, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'calculateShipping', { itemId, zipCode, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.04);
      
      const calculation = calculateShipping(itemId, zipCode, siteId);
      
      return {
        success: true,
        data: calculation
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'calculateShipping', error);
      throw error;
    }
  }

  async getPickupPoints(zipCode, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getPickupPoints', { zipCode, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.03);
      
      const pickupPoints = getPickupPoints(zipCode, siteId);
      
      return {
        success: true,
        data: pickupPoints
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getPickupPoints', error);
      throw error;
    }
  }

  async trackShipment(shipmentId) {
    mockLogger.log(this.serviceName, 'trackShipment', { shipmentId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const tracking = trackShipment(shipmentId);
      
      return {
        success: true,
        data: tracking
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'trackShipment', error);
      throw error;
    }
  }

  async createShipment(orderData) {
    mockLogger.log(this.serviceName, 'createShipment', { orderData });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1000);
      simulateRandomError(0.05);
      
      const shipment = {
        id: `shipment_${Date.now()}`,
        order_id: orderData.order_id,
        status: 'ready_to_ship',
        mode: orderData.shipping_mode || 'me2',
        date_created: new Date().toISOString(),
        sender_address: {
          id: 'sender_address_001',
          address_line: 'Carrera 7 # 32-16',
          city: 'Bogotá',
          state: 'Distrito Capital',
          country: 'Colombia',
          zip_code: '110311'
        },
        receiver_address: orderData.receiver_address,
        items: orderData.items || [],
        cost: orderData.shipping_cost || 0,
        currency_id: orderData.currency_id || 'COP',
        estimated_delivery: {
          date: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000).toISOString(), // +2 días
          unit: 'business_days'
        },
        tracking_number: `ME${Date.now()}CO`,
        tracking_method: 'mercadoenvios'
      };
      
      return {
        success: true,
        data: shipment
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'createShipment', error);
      throw error;
    }
  }

  async updateShipmentStatus(shipmentId, status) {
    mockLogger.log(this.serviceName, 'updateShipmentStatus', { shipmentId, status });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const updatedShipment = {
        id: shipmentId,
        status,
        last_updated: new Date().toISOString(),
        status_history: [
          {
            status: 'ready_to_ship',
            date: '2024-01-24T10:00:00.000Z',
            description: 'Listo para envío'
          },
          {
            status,
            date: new Date().toISOString(),
            description: this.getStatusDescription(status)
          }
        ]
      };
      
      return {
        success: true,
        data: updatedShipment
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'updateShipmentStatus', error);
      throw error;
    }
  }

  async getShippingCoverage(zipCode, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getShippingCoverage', { zipCode, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      // Mock de cobertura de envío
      const coverage = {
        zip_code: zipCode,
        site_id: siteId,
        covered: true,
        city: siteId === 'MCO' ? 'Bogotá' : 'Buenos Aires',
        state: siteId === 'MCO' ? 'Distrito Capital' : 'Capital Federal',
        available_methods: [
          {
            id: 'mercadoenvios',
            name: 'Mercado Envíos',
            delivery_time: '2-3 días hábiles',
            cost_estimate: siteId === 'MCO' ? 8900 : 2500
          },
          {
            id: 'flex',
            name: 'Mercado Envíos Flex',
            delivery_time: '1-2 días hábiles',
            cost_estimate: 0
          }
        ],
        restrictions: []
      };
      
      return {
        success: true,
        data: coverage
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getShippingCoverage', error);
      throw error;
    }
  }

  async getShippingLabels(shipmentId) {
    mockLogger.log(this.serviceName, 'getShippingLabels', { shipmentId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const labels = {
        shipment_id: shipmentId,
        labels: [
          {
            id: `label_${shipmentId}`,
            format: 'PDF',
            url: `https://shipping.mercadolibre.com/labels/${shipmentId}.pdf`,
            created_at: new Date().toISOString()
          }
        ]
      };
      
      return {
        success: true,
        data: labels
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getShippingLabels', error);
      throw error;
    }
  }

  getStatusDescription(status) {
    const descriptions = {
      'ready_to_ship': 'Listo para envío',
      'shipped': 'En camino',
      'in_transit': 'En tránsito',
      'out_for_delivery': 'En reparto',
      'delivered': 'Entregado',
      'delivery_failed': 'Fallo en la entrega',
      'returned': 'Devuelto'
    };
    
    return descriptions[status] || 'Estado desconocido';
  }
}
