// Caso de uso para funcionalidades de compra
class PurchaseProductUseCase {
  constructor(purchaseAdapter, paymentAdapter) {
    this.purchaseAdapter = purchaseAdapter;
    this.paymentAdapter = paymentAdapter;
  }

  async calculateTotalCost(itemId, quantity, zipCode = null, paymentMethodId = null) {
    try {
      // Obtener información del producto
      const stockInfo = await this.purchaseAdapter.getStockInfo(itemId);
      
      if (!stockInfo || stockInfo.available < quantity) {
        throw new Error('Cantidad no disponible en stock');
      }
      
      // Obtener precio base del producto (esto requeriría el ProductAdapter)
      // Por ahora asumimos que el precio viene en el stockInfo o se pasa como parámetro
      const basePrice = stockInfo.price || 0;
      
      let shippingCost = 0;
      let taxes = 0;
      
      // Calcular costo de envío si se proporciona código postal
      if (zipCode) {
        const shippingOptions = await this.purchaseAdapter.getShippingOptions(itemId, zipCode, quantity);
        const cheapestShipping = shippingOptions.reduce((min, option) => 
          option.cost < min.cost ? option : min, shippingOptions[0] || { cost: 0 });
        shippingCost = cheapestShipping.cost;
      }
      
      // Calcular total
      const totalCost = await this.purchaseAdapter.calculateTotalCost(
        basePrice, quantity, shippingCost, taxes
      );
      
      return {
        ...totalCost,
        stockInfo,
        breakdown: {
          unitPrice: basePrice,
          quantity,
          subtotal: totalCost.subtotal,
          shipping: shippingCost,
          taxes,
          total: totalCost.total
        }
      };
    } catch (error) {
      throw new Error(`Failed to calculate total cost: ${error.message}`);
    }
  }

  async getShippingOptions(itemId, zipCode, quantity = 1) {
    try {
      const shippingOptions = await this.purchaseAdapter.getShippingOptions(null, zipCode, itemId, quantity);
      
      return {
        options: shippingOptions,
        summary: {
          hasFreeShipping: shippingOptions.some(option => option.freeShipping),
          fastestOption: this.getFastestShippingOption(shippingOptions),
          cheapestOption: this.getCheapestShippingOption(shippingOptions),
          recommendedOption: this.getRecommendedShippingOption(shippingOptions)
        }
      };
    } catch (error) {
      throw new Error(`Failed to get shipping options: ${error.message}`);
    }
  }

  getFastestShippingOption(options) {
    if (options.length === 0) return null;
    
    return options.reduce((fastest, current) => {
      const fastestTime = this.parseDeliveryTime(fastest.estimatedDeliveryTime);
      const currentTime = this.parseDeliveryTime(current.estimatedDeliveryTime);
      return currentTime < fastestTime ? current : fastest;
    });
  }

  getCheapestShippingOption(options) {
    if (options.length === 0) return null;
    
    return options.reduce((cheapest, current) => 
      current.cost < cheapest.cost ? current : cheapest
    );
  }

  getRecommendedShippingOption(options) {
    if (options.length === 0) return null;
    
    // Priorizar envío gratis, luego por velocidad y costo
    const freeOptions = options.filter(option => option.freeShipping);
    if (freeOptions.length > 0) {
      return this.getFastestShippingOption(freeOptions);
    }
    
    // Si no hay envío gratis, balancear precio y tiempo
    return options.reduce((best, current) => {
      const bestScore = this.calculateShippingScore(best);
      const currentScore = this.calculateShippingScore(current);
      return currentScore > bestScore ? current : best;
    });
  }

  calculateShippingScore(option) {
    // Puntuación basada en costo y tiempo (menor es mejor)
    const costScore = 100 - Math.min(option.cost / 10000, 100); // Normalizar costo
    const timeScore = 100 - this.parseDeliveryTime(option.estimatedDeliveryTime);
    return (costScore + timeScore) / 2;
  }

  parseDeliveryTime(deliveryTime) {
    if (!deliveryTime) return 999;
    
    // Simplificado: extraer días del tiempo estimado
    if (typeof deliveryTime === 'string') {
      const match = deliveryTime.match(/(\d+)/);
      return match ? parseInt(match[1]) : 999;
    }
    
    return deliveryTime.to || deliveryTime.days || 999;
  }

  async addToCart(itemId, quantity, variationId = null, userInfo = null) {
    try {
      // Verificar stock antes de agregar al carrito
      const stockInfo = await this.purchaseAdapter.getStockInfo(itemId, variationId);
      
      if (!stockInfo || stockInfo.available < quantity) {
        throw new Error('Cantidad no disponible en stock');
      }
      
      const cartResponse = await this.purchaseAdapter.addToCart(
        itemId, quantity, variationId, userInfo
      );
      
      return {
        success: true,
        cartInfo: cartResponse,
        message: 'Producto agregado al carrito correctamente'
      };
    } catch (error) {
      throw new Error(`Failed to add to cart: ${error.message}`);
    }
  }

  async initiateCheckout(cartId, paymentMethodId, shippingAddressId) {
    try {
      const checkoutResponse = await this.purchaseAdapter.initiateCheckout(
        cartId, paymentMethodId, shippingAddressId
      );
      
      return {
        success: true,
        checkoutInfo: checkoutResponse,
        nextSteps: this.generateCheckoutNextSteps(checkoutResponse)
      };
    } catch (error) {
      throw new Error(`Failed to initiate checkout: ${error.message}`);
    }
  }

  generateCheckoutNextSteps(checkoutResponse) {
    const steps = [];
    
    if (checkoutResponse.redirectUrl) {
      steps.push({
        type: 'redirect',
        description: 'Completar pago en la plataforma de pago',
        url: checkoutResponse.redirectUrl
      });
    }
    
    if (checkoutResponse.paymentInfo?.requiresConfirmation) {
      steps.push({
        type: 'confirmation',
        description: 'Confirmar los datos de pago'
      });
    }
    
    steps.push({
      type: 'tracking',
      description: 'Hacer seguimiento del pedido'
    });
    
    return steps;
  }

  async getBuyerProtection(itemId) {
    try {
      const protection = await this.purchaseAdapter.getBuyerProtection(itemId);
      
      return {
        ...protection,
        summary: {
          hasMoneyBackGuarantee: protection.guarantees?.some(g => g.type === 'money_back'),
          hasProductGuarantee: protection.guarantees?.some(g => g.type === 'product_guarantee'),
          canReturn: protection.returnPolicy?.hasReturn,
          returnDays: protection.returnPolicy?.returnDays || 0
        }
      };
    } catch (error) {
      throw new Error(`Failed to get buyer protection: ${error.message}`);
    }
  }
}

export default PurchaseProductUseCase;
