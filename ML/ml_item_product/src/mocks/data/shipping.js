// Mock data para opciones de envío
export const mockShippingOptions = {
  'MCO123456789': [
    {
      id: 'mercadoenvios_flex',
      name: 'Mercado Envíos Flex',
      display_name: 'Llega mañana',
      currency_id: 'COP',
      list_cost: 0,
      cost: 0,
      free_shipping: {
        flag: true,
        rule: {
          free_mode: 'minimum_price',
          value: 70000
        }
      },
      tracks_shipments_status: 'ACTIVE',
      estimated_schedule_limit: {
        date: '2024-01-25T18:00:00.000Z'
      },
      estimated_delivery: {
        date: '2024-01-26T18:00:00.000Z',
        unit: 'business_days',
        offset: {
          date: 1,
          shipping: 1
        }
      },
      speed_ranking: 1,
      delivery_type: 'standard',
      shipping_method_type: 'custom',
      shipping_option_type: 'mercadoenvios',
      estimated_handling_limit: {
        date: '2024-01-24T18:00:00.000Z'
      },
      receivers: [
        {
          type: 'pickup_point',
          details: {
            pickup_points: [
              {
                id: 'pp_001',
                name: 'Punto de Retiro Centro Comercial Andino',
                address: 'Carrera 11 # 82-71, Local 2-19',
                city: 'Bogotá',
                phone: '601-234-5678',
                business_hours: [
                  {
                    day: 'monday',
                    time: '09:00-18:00'
                  },
                  {
                    day: 'tuesday',
                    time: '09:00-18:00'
                  },
                  {
                    day: 'wednesday',
                    time: '09:00-18:00'
                  },
                  {
                    day: 'thursday',
                    time: '09:00-18:00'
                  },
                  {
                    day: 'friday',
                    time: '09:00-18:00'
                  },
                  {
                    day: 'saturday',
                    time: '09:00-17:00'
                  }
                ]
              },
              {
                id: 'pp_002',
                name: 'Punto de Retiro Zona Rosa',
                address: 'Calle 93A # 11-27',
                city: 'Bogotá',
                phone: '601-345-6789',
                business_hours: [
                  {
                    day: 'monday',
                    time: '10:00-19:00'
                  },
                  {
                    day: 'tuesday',
                    time: '10:00-19:00'
                  },
                  {
                    day: 'wednesday',
                    time: '10:00-19:00'
                  },
                  {
                    day: 'thursday',
                    time: '10:00-19:00'
                  },
                  {
                    day: 'friday',
                    time: '10:00-19:00'
                  },
                  {
                    day: 'saturday',
                    time: '10:00-18:00'
                  }
                ]
              }
            ]
          }
        },
        {
          type: 'home_delivery',
          details: {
            description: 'Envío a domicilio',
            estimated_delivery: {
              date: '2024-01-26T18:00:00.000Z'
            }
          }
        }
      ]
    },
    {
      id: 'mercadoenvios_normal',
      name: 'Mercado Envíos',
      display_name: 'Llega en 2 a 3 días',
      currency_id: 'COP',
      list_cost: 8900,
      cost: 0,
      free_shipping: {
        flag: true,
        rule: {
          free_mode: 'minimum_price',
          value: 70000
        }
      },
      tracks_shipments_status: 'ACTIVE',
      estimated_delivery: {
        date: '2024-01-28T18:00:00.000Z',
        unit: 'business_days',
        offset: {
          date: 3,
          shipping: 2
        }
      },
      speed_ranking: 2,
      delivery_type: 'standard',
      shipping_method_type: 'standard',
      shipping_option_type: 'mercadoenvios',
      receivers: [
        {
          type: 'home_delivery',
          details: {
            description: 'Envío a domicilio',
            estimated_delivery: {
              date: '2024-01-28T18:00:00.000Z'
            }
          }
        }
      ]
    }
  ],
  'MLA987654321': [
    {
      id: 'mercadoenvios_flex_arg',
      name: 'Mercado Envíos Flex',
      display_name: 'Llega mañana',
      currency_id: 'ARS',
      list_cost: 0,
      cost: 0,
      free_shipping: {
        flag: false,
        rule: {
          free_mode: 'minimum_price',
          value: 50000
        }
      },
      tracks_shipments_status: 'ACTIVE',
      estimated_delivery: {
        date: '2024-01-26T18:00:00.000Z',
        unit: 'business_days',
        offset: {
          date: 1,
          shipping: 1
        }
      },
      speed_ranking: 1,
      delivery_type: 'standard',
      receivers: [
        {
          type: 'pickup_point',
          details: {
            pickup_points: [
              {
                id: 'pp_arg_001',
                name: 'Punto de Retiro Palermo',
                address: 'Av. Santa Fe 3253',
                city: 'Buenos Aires',
                phone: '11-4567-8901'
              }
            ]
          }
        }
      ]
    },
    {
      id: 'correo_argentino',
      name: 'Correo Argentino',
      display_name: 'Llega en 5 a 7 días',
      currency_id: 'ARS',
      list_cost: 2500,
      cost: 2500,
      free_shipping: {
        flag: false
      },
      tracks_shipments_status: 'ACTIVE',
      estimated_delivery: {
        date: '2024-02-02T18:00:00.000Z',
        unit: 'business_days',
        offset: {
          date: 7,
          shipping: 5
        }
      },
      speed_ranking: 3,
      delivery_type: 'standard',
      receivers: [
        {
          type: 'home_delivery',
          details: {
            description: 'Envío a domicilio por Correo Argentino'
          }
        }
      ]
    }
  ]
};

export const mockShippingCalculations = {
  'MCO': {
    zipCode: '110111',
    city: 'Bogotá',
    state: 'Distrito Capital',
    options: [
      {
        id: 'flex',
        name: 'Mercado Envíos Flex',
        cost: 0,
        currency_id: 'COP',
        delivery_time: '1-2 días hábiles',
        free_shipping: true
      },
      {
        id: 'standard',
        name: 'Mercado Envíos',
        cost: 8900,
        currency_id: 'COP',
        delivery_time: '2-3 días hábiles',
        free_shipping: true
      }
    ]
  },
  'MLA': {
    zipCode: 'C1234ABC',
    city: 'Buenos Aires',
    state: 'Capital Federal',
    options: [
      {
        id: 'flex',
        name: 'Mercado Envíos Flex',
        cost: 1200,
        currency_id: 'ARS',
        delivery_time: '1-2 días hábiles',
        free_shipping: false
      },
      {
        id: 'correo',
        name: 'Correo Argentino',
        cost: 2500,
        currency_id: 'ARS',
        delivery_time: '5-7 días hábiles',
        free_shipping: false
      }
    ]
  }
};

export const getShippingOptionsByItemId = (itemId) => {
  return mockShippingOptions[itemId] || [];
};

export const calculateShipping = (itemId, zipCode, siteId = 'MCO') => {
  const baseCalculation = mockShippingCalculations[siteId];
  
  if (!baseCalculation) {
    return {
      zipCode,
      error: 'Zona de envío no disponible'
    };
  }
  
  return {
    ...baseCalculation,
    zipCode,
    calculated_at: new Date().toISOString()
  };
};

export const getPickupPoints = (zipCode, siteId = 'MCO') => {
  const shippingOptions = Object.values(mockShippingOptions).flat();
  const pickupOptions = shippingOptions.filter(option => 
    option.receivers?.some(receiver => receiver.type === 'pickup_point')
  );
  
  const allPickupPoints = pickupOptions.flatMap(option => 
    option.receivers
      .filter(receiver => receiver.type === 'pickup_point')
      .flatMap(receiver => receiver.details.pickup_points || [])
  );
  
  return {
    zipCode,
    siteId,
    pickup_points: allPickupPoints.slice(0, 10) // Limitar a 10 puntos
  };
};

export const trackShipment = (shipmentId) => {
  return {
    id: shipmentId,
    status: 'shipped',
    status_history: [
      {
        status: 'handling',
        date_created: '2024-01-24T10:00:00.000Z',
        description: 'El vendedor está preparando tu pedido'
      },
      {
        status: 'shipped',
        date_created: '2024-01-24T16:30:00.000Z',
        description: 'Tu pedido está en camino'
      }
    ],
    estimated_delivery: {
      date: '2024-01-26T18:00:00.000Z'
    },
    tracking_number: `ME${shipmentId}CO`,
    carrier: {
      name: 'Mercado Envíos',
      phone: '018000-123456'
    }
  };
};
