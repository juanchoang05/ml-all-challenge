// Mock Service para PaymentService
import { 
  getPaymentMethodsBySite, 
  getPaymentMethodById, 
  calculateInstallments, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockPaymentService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'PaymentService';
  }

  async getPaymentMethods(siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getPaymentMethods', { siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const paymentMethods = getPaymentMethodsBySite(siteId);
      
      return {
        success: true,
        data: paymentMethods
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getPaymentMethods', error);
      throw error;
    }
  }

  async getPaymentMethodById(paymentMethodId, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getPaymentMethodById', { paymentMethodId, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const paymentMethod = getPaymentMethodById(paymentMethodId, siteId);
      
      if (!paymentMethod) {
        throw new Error(`Payment method ${paymentMethodId} not found for site ${siteId}`);
      }
      
      return {
        success: true,
        data: paymentMethod
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getPaymentMethodById', error);
      throw error;
    }
  }

  async calculateInstallments(amount, paymentMethodId, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'calculateInstallments', { amount, paymentMethodId, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.03);
      
      const installments = calculateInstallments(amount, paymentMethodId, siteId);
      
      return {
        success: true,
        data: installments
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'calculateInstallments', error);
      throw error;
    }
  }

  async getCardIssuers(paymentMethodId, siteId = 'MCO') {
    mockLogger.log(this.serviceName, 'getCardIssuers', { paymentMethodId, siteId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      // Mock de emisores de tarjetas según el país
      const issuers = {
        'MCO': [
          { id: 'bancolombia', name: 'Bancolombia', secure_thumbnail: 'https://http2.mlstatic.com/issuers/bancolombia.png' },
          { id: 'banco_bogota', name: 'Banco de Bogotá', secure_thumbnail: 'https://http2.mlstatic.com/issuers/banco_bogota.png' },
          { id: 'davivienda', name: 'Davivienda', secure_thumbnail: 'https://http2.mlstatic.com/issuers/davivienda.png' },
          { id: 'colpatria', name: 'Colpatria', secure_thumbnail: 'https://http2.mlstatic.com/issuers/colpatria.png' }
        ],
        'MLA': [
          { id: 'banco_nacion', name: 'Banco Nación', secure_thumbnail: 'https://http2.mlstatic.com/issuers/banco_nacion.png' },
          { id: 'santander', name: 'Santander', secure_thumbnail: 'https://http2.mlstatic.com/issuers/santander.png' },
          { id: 'bbva', name: 'BBVA', secure_thumbnail: 'https://http2.mlstatic.com/issuers/bbva.png' },
          { id: 'galicia', name: 'Banco Galicia', secure_thumbnail: 'https://http2.mlstatic.com/issuers/galicia.png' }
        ]
      };
      
      return {
        success: true,
        data: issuers[siteId] || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getCardIssuers', error);
      throw error;
    }
  }

  async processPayment(paymentData) {
    mockLogger.log(this.serviceName, 'processPayment', { 
      payment_method_id: paymentData.payment_method_id,
      transaction_amount: paymentData.transaction_amount 
    });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 2000);
      
      // Simular 10% de probabilidad de rechazo
      const isRejected = Math.random() < 0.1;
      
      if (isRejected) {
        return {
          success: false,
          data: {
            id: `payment_${Date.now()}`,
            status: 'rejected',
            status_detail: 'cc_rejected_insufficient_amount',
            date_created: new Date().toISOString(),
            transaction_amount: paymentData.transaction_amount,
            currency_id: paymentData.currency_id || 'COP'
          }
        };
      }
      
      // Simular pago exitoso
      const payment = {
        id: `payment_${Date.now()}`,
        status: 'approved',
        status_detail: 'accredited',
        date_created: new Date().toISOString(),
        date_approved: new Date().toISOString(),
        transaction_amount: paymentData.transaction_amount,
        currency_id: paymentData.currency_id || 'COP',
        payment_method_id: paymentData.payment_method_id,
        payment_type_id: paymentData.payment_type_id || 'credit_card',
        installments: paymentData.installments || 1,
        installment_amount: paymentData.transaction_amount / (paymentData.installments || 1),
        transaction_details: {
          net_received_amount: paymentData.transaction_amount * 0.94, // 6% de comisión
          total_paid_amount: paymentData.transaction_amount,
          installment_amount: paymentData.transaction_amount / (paymentData.installments || 1),
          financial_institution: paymentData.issuer_id || 'visa'
        },
        fee_details: [
          {
            type: 'mercadopago_fee',
            amount: paymentData.transaction_amount * 0.06,
            fee_payer: 'collector'
          }
        ]
      };
      
      return {
        success: true,
        data: payment
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'processPayment', error);
      throw error;
    }
  }

  async getPaymentStatus(paymentId) {
    mockLogger.log(this.serviceName, 'getPaymentStatus', { paymentId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      // Mock de estado del pago
      const paymentStatus = {
        id: paymentId,
        status: 'approved',
        status_detail: 'accredited',
        date_created: '2024-01-24T14:30:00.000Z',
        date_approved: '2024-01-24T14:32:00.000Z',
        transaction_amount: 4899000,
        currency_id: 'COP'
      };
      
      return {
        success: true,
        data: paymentStatus
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getPaymentStatus', error);
      throw error;
    }
  }

  async refundPayment(paymentId, amount = null) {
    mockLogger.log(this.serviceName, 'refundPayment', { paymentId, amount });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1500);
      simulateRandomError(0.05);
      
      const refund = {
        id: `refund_${Date.now()}`,
        payment_id: paymentId,
        amount: amount || 4899000,
        currency_id: 'COP',
        status: 'approved',
        date_created: new Date().toISOString(),
        reason: 'Cliente solicitó devolución'
      };
      
      return {
        success: true,
        data: refund
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'refundPayment', error);
      throw error;
    }
  }
}
