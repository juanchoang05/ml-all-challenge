// Mock data para usuarios/vendedores
export const mockSellers = {
  123456: {
    id: 123456,
    nickname: 'TECHSTORE_OFICIAL',
    registration_date: '2018-03-15T10:30:00.000Z',
    first_name: 'TechStore',
    last_name: 'Colombia',
    country_id: 'CO',
    email: 'contact@techstore.co',
    identification: {
      type: 'NIT',
      number: '900123456-1'
    },
    address: {
      city: 'Bogotá',
      state: 'Distrito Capital',
      zip_code: '110111'
    },
    phone: {
      area_code: '57',
      number: '3001234567',
      extension: '',
      verified: true
    },
    alternative_phone: {
      area_code: '',
      number: '',
      extension: ''
    },
    user_type: 'professional',
    tags: ['eshop', 'large_seller', 'mshops'],
    logo: 'https://http2.mlstatic.com/frontend-assets/sellers-logo/123456.jpg',
    points: 4850,
    site_id: 'MCO',
    permalink: 'https://perfil.mercadolibre.com.co/TECHSTORE_OFICIAL',
    seller_experience: 'ADVANCED',
    seller_reputation: {
      level_id: 'premium',
      power_seller_status: 'platinum',
      transactions: {
        canceled: 45,
        completed: 2847,
        period: 'historic',
        ratings: {
          negative: 23,
          neutral: 89,
          positive: 2735
        },
        total: 2847
      },
      metrics: {
        sales: {
          period: 'last_60_days',
          completed: 234
        },
        claims: {
          period: 'last_60_days',
          rate: 0.02,
          value: 5
        },
        delayed_handling_time: {
          period: 'last_60_days',
          rate: 0.01,
          value: 2
        },
        cancellations: {
          period: 'last_60_days',
          rate: 0.005,
          value: 1
        }
      }
    },
    status: {
      site_status: 'active',
      list: {
        allow: true,
        codes: [],
        immediate_payment: {
          reasons: [],
          required: false
        }
      },
      buy: {
        allow: true,
        codes: [],
        immediate_payment: {
          reasons: [],
          required: false
        }
      },
      sell: {
        allow: true,
        codes: [],
        immediate_payment: {
          reasons: [],
          required: false
        }
      },
      confirmed_email: true,
      user_type: 'professional',
      required_action: '',
      immediate_payment: false,
      billing_info: {
        doc_type: 'NIT',
        doc_number: '900123456-1'
      }
    }
  },
  987654: {
    id: 987654,
    nickname: 'VENDEDOR_PRO_ARG',
    registration_date: '2019-07-22T15:45:00.000Z',
    first_name: 'Juan',
    last_name: 'Pérez',
    country_id: 'AR',
    email: 'juan.perez@email.com',
    address: {
      city: 'Buenos Aires',
      state: 'Capital Federal',
      zip_code: 'C1234ABC'
    },
    phone: {
      area_code: '54',
      number: '1123456789',
      verified: true
    },
    user_type: 'normal',
    tags: ['normal'],
    points: 1250,
    site_id: 'MLA',
    permalink: 'https://perfil.mercadolibre.com.ar/VENDEDOR_PRO_ARG',
    seller_experience: 'INTERMEDIATE',
    seller_reputation: {
      level_id: 'gold',
      power_seller_status: 'gold',
      transactions: {
        canceled: 12,
        completed: 456,
        period: 'historic',
        ratings: {
          negative: 8,
          neutral: 23,
          positive: 425
        },
        total: 456
      },
      metrics: {
        sales: {
          period: 'last_60_days',
          completed: 45
        },
        claims: {
          period: 'last_60_days',
          rate: 0.04,
          value: 2
        },
        delayed_handling_time: {
          period: 'last_60_days',
          rate: 0.02,
          value: 1
        },
        cancellations: {
          period: 'last_60_days',
          rate: 0.01,
          value: 1
        }
      }
    },
    status: {
      site_status: 'active',
      confirmed_email: true,
      user_type: 'normal'
    }
  }
};

export const getSellerById = (id) => {
  return mockSellers[id] || null;
};

export const getSellerItems = (sellerId, options = {}) => {
  const { limit = 50, offset = 0 } = options;
  
  // Mock items del vendedor
  const sellerItems = [
    {
      id: 'MCO123456789',
      title: 'iPhone 14 Pro Max 256GB Morado Profundo',
      price: 4899000,
      currency_id: 'COP',
      available_quantity: 45,
      sold_quantity: 234,
      condition: 'new',
      thumbnail: 'https://http2.mlstatic.com/D_Q_NP_123456-MCO12345678-preview.jpg',
      permalink: 'https://articulo.mercadolibre.com.co/MCO-123456789-iphone-14-pro-max'
    },
    {
      id: 'MCO123456790',
      title: 'MacBook Air M2 13" 256GB Gris Espacial',
      price: 3299000,
      currency_id: 'COP',
      available_quantity: 23,
      sold_quantity: 156,
      condition: 'new',
      thumbnail: 'https://http2.mlstatic.com/D_Q_NP_234567-MCO23456789-preview.jpg',
      permalink: 'https://articulo.mercadolibre.com.co/MCO-123456790-macbook-air-m2'
    }
  ];
  
  const results = sellerItems.slice(offset, offset + limit);
  
  return {
    seller_id: sellerId,
    paging: {
      total: sellerItems.length,
      offset,
      limit
    },
    results
  };
};
