// Adaptador para funcionalidades de compra y carrito
class PurchaseAdapter {
  constructor(purchaseService, shippingService) {
    this.purchaseService = purchaseService;
    this.shippingService = shippingService;
  }

  async getShippingOptions(sellerId, zipCode, itemId, quantity = 1) {
    try {
      const rawShipping = await this.shippingService.getShippingOptions(sellerId, zipCode, itemId, quantity);
      return this.transformShippingOptions(rawShipping);
    } catch (error) {
      throw new Error(`Error fetching shipping options: ${error.message}`);
    }
  }

  transformShippingOptions(rawShipping) {
    return rawShipping.map(option => ({
      id: option.id,
      name: option.name,
      type: option.type,
      cost: option.cost,
      currency: option.currency_id,
      estimatedDeliveryTime: {
        date: option.estimated_delivery_time?.date,
        from: option.estimated_delivery_time?.from,
        to: option.estimated_delivery_time?.to,
        unit: option.estimated_delivery_time?.unit,
        type: option.estimated_delivery_time?.type
      },
      speed: option.speed,
      pickupPromise: option.pickup_promise,
      deliveryPromise: option.delivery_promise,
      shippingMethod: {
        id: option.shipping_method_id,
        name: option.shipping_method_name
      },
      freeShipping: option.free_shipping,
      tags: option.tags || []
    }));
  }

  async calculateTotalCost(itemPrice, quantity, shippingCost = 0, taxes = 0) {
    const subtotal = itemPrice * quantity;
    const total = subtotal + shippingCost + taxes;
    
    return {
      subtotal,
      shipping: shippingCost,
      taxes,
      total,
      currency: 'COP'
    };
  }

  async getStockInfo(itemId, variationId = null) {
    try {
      const stockInfo = await this.purchaseService.getStockInfo(itemId, variationId);
      return this.transformStockInfo(stockInfo);
    } catch (error) {
      throw new Error(`Error fetching stock info: ${error.message}`);
    }
  }

  transformStockInfo(stockInfo) {
    return {
      available: stockInfo.available_quantity,
      sold: stockInfo.sold_quantity,
      maxPurchaseQuantity: stockInfo.max_purchase_quantity || stockInfo.available_quantity,
      stockStatus: this.getStockStatus(stockInfo.available_quantity),
      lastUpdated: stockInfo.last_updated
    };
  }

  getStockStatus(availableQuantity) {
    if (availableQuantity === 0) return 'out_of_stock';
    if (availableQuantity <= 5) return 'low_stock';
    if (availableQuantity <= 20) return 'medium_stock';
    return 'in_stock';
  }

  async addToCart(itemId, quantity, variationId = null, buyerInfo = null) {
    try {
      const cartData = {
        item_id: itemId,
        quantity,
        variation_id: variationId,
        buyer_info: buyerInfo
      };
      
      const response = await this.purchaseService.addToCart(cartData);
      return this.transformCartResponse(response);
    } catch (error) {
      throw new Error(`Error adding to cart: ${error.message}`);
    }
  }

  transformCartResponse(response) {
    return {
      success: response.success,
      cartId: response.cart_id,
      itemId: response.item_id,
      quantity: response.quantity,
      price: response.price,
      totalItems: response.total_items,
      totalAmount: response.total_amount
    };
  }

  async initiateCheckout(cartId, paymentMethodId, shippingAddressId) {
    try {
      const checkoutData = {
        cart_id: cartId,
        payment_method_id: paymentMethodId,
        shipping_address_id: shippingAddressId
      };
      
      const response = await this.purchaseService.initiateCheckout(checkoutData);
      return this.transformCheckoutResponse(response);
    } catch (error) {
      throw new Error(`Error initiating checkout: ${error.message}`);
    }
  }

  transformCheckoutResponse(response) {
    return {
      checkoutId: response.checkout_id,
      redirectUrl: response.redirect_url,
      status: response.status,
      totalAmount: response.total_amount,
      paymentInfo: response.payment_info,
      shippingInfo: response.shipping_info
    };
  }

  async getBuyerProtection(itemId) {
    try {
      const protection = await this.purchaseService.getBuyerProtection(itemId);
      return this.transformBuyerProtection(protection);
    } catch (error) {
      throw new Error(`Error fetching buyer protection: ${error.message}`);
    }
  }

  transformBuyerProtection(protection) {
    return {
      hasProtection: protection.has_protection,
      guarantees: protection.guarantees?.map(guarantee => ({
        type: guarantee.type,
        description: guarantee.description,
        coverage: guarantee.coverage
      })) || [],
      returnPolicy: {
        hasReturn: protection.return_policy?.has_return,
        returnDays: protection.return_policy?.return_days,
        description: protection.return_policy?.description
      }
    };
  }
}

export default PurchaseAdapter;
