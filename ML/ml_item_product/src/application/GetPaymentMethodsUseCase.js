// Caso de uso para obtener métodos de pago
class GetPaymentMethodsUseCase {
  constructor(paymentAdapter) {
    this.paymentAdapter = paymentAdapter;
  }

  async execute(sellerId, amount = null) {
    try {
      const paymentMethods = await this.paymentAdapter.getPaymentMethods(sellerId);
      
      // Si se proporciona un monto, calcular cuotas disponibles
      if (amount && paymentMethods.creditCards.length > 0) {
        const installmentsPromises = paymentMethods.creditCards.map(async (card) => {
          try {
            const installments = await this.paymentAdapter.getInstallments(amount, card.id);
            return {
              ...card,
              installments: installments[0]?.installments || []
            };
          } catch (error) {
            console.warn(`Could not fetch installments for card ${card.id}:`, error);
            return card;
          }
        });
        
        paymentMethods.creditCards = await Promise.all(installmentsPromises);
      }
      
      return {
        ...paymentMethods,
        summary: this.generatePaymentSummary(paymentMethods),
        features: this.extractPaymentFeatures(paymentMethods)
      };
    } catch (error) {
      throw new Error(`Failed to get payment methods: ${error.message}`);
    }
  }

  generatePaymentSummary(paymentMethods) {
    return {
      totalMethods: this.countTotalMethods(paymentMethods),
      hasCreditCards: paymentMethods.creditCards.length > 0,
      hasDebitCards: paymentMethods.debitCards.length > 0,
      hasDigitalWallets: paymentMethods.digitalWallets.length > 0,
      hasBankTransfers: paymentMethods.bankTransfers.length > 0,
      hasCashPayments: paymentMethods.cash.length > 0,
      maxInstallments: this.getMaxInstallments(paymentMethods.creditCards),
      hasInterestFreeInstallments: this.hasInterestFreeOptions(paymentMethods.installments)
    };
  }

  countTotalMethods(paymentMethods) {
    return paymentMethods.creditCards.length +
           paymentMethods.debitCards.length +
           paymentMethods.digitalWallets.length +
           paymentMethods.bankTransfers.length +
           paymentMethods.cash.length;
  }

  getMaxInstallments(creditCards) {
    if (creditCards.length === 0) return 0;
    return Math.max(...creditCards.map(card => card.maxInstallments || 0));
  }

  hasInterestFreeOptions(installments) {
    return installments.some(installment => 
      installment.installments?.some(option => option.installmentRate === 0)
    );
  }

  extractPaymentFeatures(paymentMethods) {
    const features = [];
    
    if (paymentMethods.creditCards.length > 0) {
      features.push('Tarjetas de crédito');
    }
    
    if (paymentMethods.digitalWallets.some(wallet => wallet.name.toLowerCase().includes('mercadopago'))) {
      features.push('MercadoPago');
    }
    
    if (this.hasInterestFreeOptions(paymentMethods.installments)) {
      features.push('Cuotas sin interés');
    }
    
    if (paymentMethods.cash.length > 0) {
      features.push('Efectivo');
    }
    
    return features;
  }

  async getShippingOptions(sellerId, zipCode = null) {
    try {
      const shippingMethods = await this.paymentAdapter.getShippingMethods(sellerId, zipCode);
      
      return {
        methods: shippingMethods,
        hasFreeShipping: shippingMethods.some(method => method.freeShipping),
        fastestDelivery: this.getFastestDelivery(shippingMethods),
        cheapestOption: this.getCheapestOption(shippingMethods)
      };
    } catch (error) {
      throw new Error(`Failed to get shipping options: ${error.message}`);
    }
  }

  getFastestDelivery(shippingMethods) {
    if (shippingMethods.length === 0) return null;
    
    return shippingMethods.reduce((fastest, current) => {
      if (!fastest.estimatedDelivery || !current.estimatedDelivery) return current;
      return new Date(current.estimatedDelivery) < new Date(fastest.estimatedDelivery) ? current : fastest;
    });
  }

  getCheapestOption(shippingMethods) {
    if (shippingMethods.length === 0) return null;
    
    return shippingMethods.reduce((cheapest, current) => {
      return current.cost < cheapest.cost ? current : cheapest;
    });
  }
}

export default GetPaymentMethodsUseCase;
