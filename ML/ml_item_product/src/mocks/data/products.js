// Mock data para productos
export const mockProducts = {
  'MCO123456789': {
    id: 'MCO123456789',
    site_id: 'MCO',
    title: 'iPhone 14 Pro Max 256GB Morado Profundo',
    subtitle: null,
    seller_id: 123456,
    category_id: 'MCO1055',
    official_store_id: null,
    price: 4899000,
    base_price: 5299000,
    original_price: null,
    currency_id: 'COP',
    initial_quantity: 100,
    available_quantity: 45,
    sold_quantity: 234,
    condition: 'new',
    permalink: 'https://articulo.mercadolibre.com.co/MCO-123456789-iphone-14-pro-max-256gb-morado-profundo',
    thumbnail: 'https://http2.mlstatic.com/D_Q_NP_123456-MCO12345678-preview.jpg',
    secure_thumbnail: 'https://http2.mlstatic.com/D_Q_NP_123456-MCO12345678-preview.jpg',
    pictures: [
      {
        id: '123456',
        url: 'https://http2.mlstatic.com/D_Q_NP_123456-MCO12345678.jpg',
        secure_url: 'https://http2.mlstatic.com/D_Q_NP_123456-MCO12345678.jpg',
        size: '500x500',
        max_size: '1200x1200'
      },
      {
        id: '123457',
        url: 'https://http2.mlstatic.com/D_Q_NP_123457-MCO12345678.jpg',
        secure_url: 'https://http2.mlstatic.com/D_Q_NP_123457-MCO12345678.jpg',
        size: '500x500',
        max_size: '1200x1200'
      }
    ],
    video_id: null,
    descriptions: [
      {
        id: 'MCO123456789-desc',
        text: 'iPhone 14 Pro Max con la tecnología más avanzada de Apple. Pantalla Super Retina XDR de 6.7 pulgadas, chip A16 Bionic, sistema de cámaras Pro con teleobjetivo 3x y funciones cinematográficas.'
      }
    ],
    accepts_mercadopago: true,
    non_mercado_pago_payment_methods: [],
    shipping: {
      mode: 'me2',
      methods: [
        {
          id: 100009,
          name: 'Estándar',
          free_shipping: {
            flag: true,
            rule: {
              free_mode: 'minimum_price',
              value: 70000
            }
          }
        }
      ],
      tags: ['self_service_in', 'mandatory_free_shipping'],
      dimensions: null,
      local_pick_up: true,
      free_shipping: true,
      logistic_type: 'fulfillment',
      store_pick_up: false
    },
    seller_address: {
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
      search_location: {
        neighborhood: {
          id: 'TUxBQ0NBUzQyMDFa',
          name: 'Zona Rosa'
        },
        city: {
          id: 'CO-DC-B',
          name: 'Bogotá'
        },
        state: {
          id: 'CO-DC',
          name: 'Distrito Capital'
        }
      },
      id: 45678901
    },
    attributes: [
      {
        id: 'BRAND',
        name: 'Marca',
        value_id: 'APPLE',
        value_name: 'Apple',
        value_struct: null,
        values: [
          {
            id: 'APPLE',
            name: 'Apple',
            struct: null
          }
        ],
        attribute_group_id: 'OTHERS',
        attribute_group_name: 'Otros'
      },
      {
        id: 'MODEL',
        name: 'Modelo',
        value_id: null,
        value_name: 'iPhone 14 Pro Max',
        value_struct: null,
        values: [
          {
            id: null,
            name: 'iPhone 14 Pro Max',
            struct: null
          }
        ],
        attribute_group_id: 'OTHERS',
        attribute_group_name: 'Otros'
      },
      {
        id: 'STORAGE_CAPACITY',
        name: 'Capacidad de almacenamiento',
        value_id: '256GB',
        value_name: '256 GB',
        value_struct: null,
        values: [
          {
            id: '256GB',
            name: '256 GB',
            struct: null
          }
        ],
        attribute_group_id: 'OTHERS',
        attribute_group_name: 'Otros'
      }
    ],
    warnings: [],
    listing_source: '',
    variations: [],
    status: 'active',
    sub_status: [],
    tags: ['good_quality_thumbnail', 'loyalty_discount_eligible', 'brand_verified'],
    warranty: '12 meses de garantía de fábrica',
    catalog_product_id: 'MCO9876543',
    domain_id: 'MCO-CELLPHONES',
    parent_item_id: null,
    differential_pricing: null,
    deal_ids: [],
    automatic_relist: false,
    date_created: '2024-01-15T10:30:00.000Z',
    last_updated: '2024-01-20T14:45:00.000Z',
    health: null,
    catalog_listing: true
  },
  'MLA987654321': {
    id: 'MLA987654321',
    site_id: 'MLA',
    title: 'Notebook Lenovo ThinkPad E15 Intel Core i5 8GB RAM 256GB SSD',
    subtitle: 'Ideal para trabajo y estudio',
    seller_id: 987654,
    category_id: 'MLA1652',
    price: 350000,
    base_price: 380000,
    currency_id: 'ARS',
    available_quantity: 25,
    sold_quantity: 156,
    condition: 'new',
    permalink: 'https://articulo.mercadolibre.com.ar/MLA-987654321-notebook-lenovo-thinkpad',
    thumbnail: 'https://http2.mlstatic.com/D_Q_NP_987654-MLA98765432-preview.jpg',
    pictures: [
      {
        id: '987654',
        url: 'https://http2.mlstatic.com/D_Q_NP_987654-MLA98765432.jpg',
        secure_url: 'https://http2.mlstatic.com/D_Q_NP_987654-MLA98765432.jpg',
        size: '500x500',
        max_size: '1200x1200'
      }
    ],
    shipping: {
      free_shipping: false,
      mode: 'me2',
      methods: [
        {
          id: 100008,
          name: 'Estándar',
          cost: 2500,
          currency_id: 'ARS'
        }
      ]
    },
    attributes: [
      {
        id: 'BRAND',
        name: 'Marca',
        value_name: 'Lenovo'
      },
      {
        id: 'RAM_MEMORY',
        name: 'Memoria RAM',
        value_name: '8 GB'
      }
    ]
  }
};

export const getProductById = (id) => {
  return mockProducts[id] || null;
};

export const searchProducts = (query, options = {}) => {
  const { limit = 50, offset = 0 } = options;
  const allProducts = Object.values(mockProducts);
  
  let filteredProducts = allProducts;
  
  if (query) {
    filteredProducts = allProducts.filter(product => 
      product.title.toLowerCase().includes(query.toLowerCase())
    );
  }
  
  const results = filteredProducts.slice(offset, offset + limit);
  
  return {
    site_id: 'MCO',
    query,
    paging: {
      total: filteredProducts.length,
      offset,
      limit,
      primary_results: filteredProducts.length
    },
    results
  };
};
