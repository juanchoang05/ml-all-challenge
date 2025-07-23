// Mock data para compras y órdenes
export const mockOrders = {
  'order_123456': {
    id: 'order_123456',
    date_created: '2024-01-24T14:30:00.000Z',
    date_closed: null,
    last_updated: '2024-01-24T14:30:00.000Z',
    status: 'confirmed',
    status_detail: {
      code: 'payment_confirmed',
      description: 'Pago confirmado, procesando envío'
    },
    currency_id: 'COP',
    order_items: [
      {
        item: {
          id: 'MCO123456789',
          title: 'iPhone 14 Pro Max 256GB Morado Profundo',
          category_id: 'MCO1055',
          variation_id: null,
          seller_custom_field: null,
          variation_attributes: [],
          warranty: '12 meses de garantía de fábrica',
          condition: 'new',
          seller_sku: null,
          global_price: null,
          net_weight: null
        },
        quantity: 1,
        unit_price: 4899000,
        full_unit_price: 4899000,
        currency_id: 'COP',
        manufacturing_days: null,
        sale_fee: 294540,
        listing_type_id: 'gold_special'
      }
    ],
    total_amount: 4899000,
    total_amount_with_shipping: 4899000,
    paid_amount: 4899000,
    coupon: {
      id: null,
      amount: 0
    },
    shipping: {
      id: 'shipping_123456',
      shipment_type: 'shipping',
      status: 'pending',
      cost: 0,
      currency_id: 'COP',
      receiver_address: {
        id: 'addr_123456',
        address_line: 'Carrera 15 # 93-47 Apto 501',
        street_name: 'Carrera 15',
        street_number: '93-47',
        comment: 'Apto 501, Portería 24h',
        zip_code: '110221',
        city: {
          id: 'CO-DC-B',
          name: 'Bogotá'
        },
        state: {
          id: 'CO-DC',
          name: 'Distrito Capital'
        },
        country: {
          id: 'CO',
          name: 'Colombia'
        },
        neighborhood: {
          id: 'neighborhood_001',
          name: 'Zona Rosa'
        },
        municipality: {
          id: 'municipality_001',
          name: 'Chapinero'
        },
        agency: null,
        types: ['billing', 'shipping'],
        latitude: 4.676016,
        longitude: -74.048019
      },
      estimated_delivery: {
        date: '2024-01-26T18:00:00.000Z',
        time_from: null,
        time_to: null,
        unit: 'business_days'
      }
    },
    payments: [
      {
        id: 'payment_123456',
        order_id: 'order_123456',
        payer_id: 'buyer_123',
        collector: {
          id: 'seller_123456'
        },
        currency_id: 'COP',
        status: 'approved',
        status_detail: 'accredited',
        date_created: '2024-01-24T14:30:00.000Z',
        date_last_updated: '2024-01-24T14:32:00.000Z',
        card_id: null,
        reason: 'iPhone 14 Pro Max 256GB Morado Profundo',
        payment_method_id: 'credit_card',
        payment_type: 'credit_card',
        installments: 1,
        installment_amount: 4899000,
        pos_id: null,
        external_reference: null,
        transaction_amount: 4899000,
        net_received_amount: 4604460,
        total_paid_amount: 4899000,
        overpaid_amount: 0,
        transaction_details: {
          payment_method_reference_id: '123456789',
          net_received_amount: 4604460,
          total_paid_amount: 4899000,
          installment_amount: 4899000,
          financial_institution: 'visa',
          payable_deferral_period: null,
          acquirer_reference: null
        }
      }
    ],
    feedback: {
      buyer: null,
      seller: null
    },
    context: {
      channel: 'marketplace',
      site_id: 'MCO',
      flows: [],
      application: {
        id: 'web'
      }
    },
    buyer: {
      id: 'buyer_123',
      nickname: 'COMPRADOR_2024',
      email: 'comprador@email.com',
      phone: {
        area_code: '57',
        number: '3001234567',
        extension: '',
        verified: false
      },
      alternative_phone: {
        area_code: '',
        number: '',
        extension: ''
      },
      first_name: 'Juan',
      last_name: 'Pérez',
      billing_info: {
        doc_type: 'CC',
        doc_number: '12345678'
      }
    },
    seller: {
      id: 123456,
      nickname: 'TECHSTORE_OFICIAL',
      email: 'contact@techstore.co',
      phone: {
        area_code: '57',
        number: '3009876543',
        extension: '',
        verified: true
      },
      first_name: 'TechStore',
      last_name: 'Colombia'
    },
    taxes: {
      amount: null,
      currency_id: null,
      id: null
    }
  },
  'order_789012': {
    id: 'order_789012',
    date_created: '2024-01-20T09:15:00.000Z',
    date_closed: '2024-01-22T16:45:00.000Z',
    last_updated: '2024-01-22T16:45:00.000Z',
    status: 'paid',
    status_detail: {
      code: 'delivered',
      description: 'Entregado'
    },
    currency_id: 'ARS',
    order_items: [
      {
        item: {
          id: 'MLA987654321',
          title: 'Notebook Lenovo ThinkPad E15 Intel Core i5 8GB RAM 256GB SSD',
          category_id: 'MLA1652',
          condition: 'new'
        },
        quantity: 1,
        unit_price: 350000,
        full_unit_price: 350000,
        currency_id: 'ARS'
      }
    ],
    total_amount: 352500,
    total_amount_with_shipping: 352500,
    paid_amount: 352500,
    shipping: {
      id: 'shipping_789012',
      status: 'delivered',
      cost: 2500,
      currency_id: 'ARS',
      estimated_delivery: {
        date: '2024-01-22T18:00:00.000Z'
      }
    },
    payments: [
      {
        id: 'payment_789012',
        status: 'approved',
        status_detail: 'accredited',
        currency_id: 'ARS',
        payment_method_id: 'credit_card',
        installments: 3,
        transaction_amount: 352500
      }
    ],
    buyer: {
      id: 'buyer_789',
      nickname: 'COMPRADOR_ARG',
      first_name: 'María',
      last_name: 'González'
    },
    seller: {
      id: 987654,
      nickname: 'VENDEDOR_PRO_ARG'
    }
  }
};

export const mockCarts = {
  'cart_123': {
    id: 'cart_123',
    site_id: 'MCO',
    buyer_id: 'buyer_123',
    date_created: '2024-01-24T14:00:00.000Z',
    date_updated: '2024-01-24T14:25:00.000Z',
    expiration_date: '2024-01-25T14:00:00.000Z',
    items: [
      {
        id: 'MCO123456789',
        quantity: 1,
        unit_price: 4899000,
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
      total_amount: 4899000,
      shipping_cost: 0,
      total_amount_with_shipping: 4899000,
      currency_id: 'COP',
      total_items: 1
    },
    shipping_options: [
      {
        id: 'mercadoenvios_flex',
        name: 'Mercado Envíos Flex',
        cost: 0,
        estimated_delivery: '2024-01-26'
      }
    ]
  }
};

export const getOrderById = (orderId) => {
  return mockOrders[orderId] || null;
};

export const getOrdersByUser = (userId, options = {}) => {
  const { limit = 10, offset = 0, status = null } = options;
  const userOrders = Object.values(mockOrders).filter(order => 
    order.buyer.id === userId || order.seller.id === userId
  );
  
  let filteredOrders = userOrders;
  
  if (status) {
    filteredOrders = userOrders.filter(order => order.status === status);
  }
  
  const results = filteredOrders.slice(offset, offset + limit);
  
  return {
    results,
    paging: {
      total: filteredOrders.length,
      offset,
      limit
    }
  };
};

export const createOrder = (cartId, paymentData, shippingData) => {
  const cart = mockCarts[cartId];
  
  if (!cart) {
    throw new Error('Cart not found');
  }
  
  const orderId = `order_${Date.now()}`;
  const newOrder = {
    id: orderId,
    date_created: new Date().toISOString(),
    date_closed: null,
    last_updated: new Date().toISOString(),
    status: 'confirmed',
    status_detail: {
      code: 'payment_pending',
      description: 'Esperando confirmación de pago'
    },
    currency_id: cart.summary.currency_id,
    order_items: cart.items.map(item => ({
      item: {
        id: item.id,
        title: `Product ${item.id}`,
        condition: item.condition,
        warranty: item.warranty
      },
      quantity: item.quantity,
      unit_price: item.unit_price,
      full_unit_price: item.unit_price,
      currency_id: item.currency_id
    })),
    total_amount: cart.summary.total_amount,
    total_amount_with_shipping: cart.summary.total_amount_with_shipping,
    paid_amount: 0,
    shipping: {
      id: `shipping_${Date.now()}`,
      status: 'pending',
      cost: cart.summary.shipping_cost,
      currency_id: cart.summary.currency_id,
      receiver_address: shippingData.address,
      estimated_delivery: shippingData.estimated_delivery
    },
    payments: [],
    buyer: {
      id: cart.buyer_id,
      nickname: 'BUYER_USER',
      email: paymentData.buyer_email || 'buyer@email.com'
    },
    context: {
      channel: 'marketplace',
      site_id: cart.site_id
    }
  };
  
  mockOrders[orderId] = newOrder;
  return newOrder;
};

export const updateOrderStatus = (orderId, status, statusDetail) => {
  const order = mockOrders[orderId];
  
  if (order) {
    order.status = status;
    order.status_detail = statusDetail;
    order.last_updated = new Date().toISOString();
    
    if (status === 'paid' || status === 'cancelled') {
      order.date_closed = new Date().toISOString();
    }
    
    return order;
  }
  
  return null;
};

export const addPaymentToOrder = (orderId, paymentData) => {
  const order = mockOrders[orderId];
  
  if (order) {
    const payment = {
      id: `payment_${Date.now()}`,
      order_id: orderId,
      status: paymentData.status || 'pending',
      status_detail: paymentData.status_detail || 'pending_payment',
      currency_id: order.currency_id,
      payment_method_id: paymentData.payment_method_id,
      payment_type: paymentData.payment_type,
      installments: paymentData.installments || 1,
      transaction_amount: paymentData.amount,
      date_created: new Date().toISOString()
    };
    
    order.payments.push(payment);
    
    if (payment.status === 'approved') {
      order.paid_amount = payment.transaction_amount;
      order.status = 'paid';
      order.status_detail = {
        code: 'payment_confirmed',
        description: 'Pago confirmado'
      };
    }
    
    return order;
  }
  
  return null;
};
