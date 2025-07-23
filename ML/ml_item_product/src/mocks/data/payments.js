// Mock data para métodos de pago
export const mockPaymentMethods = {
  'MCO': [
    {
      id: 'credit_card',
      name: 'Tarjetas de crédito',
      payment_type_id: 'credit_card',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/credit-card.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/credit-card.png',
      deferred_capture: 'supported',
      settings: [
        {
          bin: {
            pattern: '^4',
            installments_pattern: '^4',
            exclusion_pattern: null
          },
          card_number: {
            validation: 'standard',
            length: 16
          },
          security_code: {
            length: 3,
            card_location: 'back',
            mode: 'mandatory'
          }
        }
      ],
      additional_info_needed: ['cardholder_name', 'cardholder_identification_number'],
      min_installments: 1,
      max_installments: 36,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: ['recommended']
        },
        {
          installments: 3,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 6,
          installment_rate: 15.99,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 12,
          installment_rate: 22.50,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 24,
          installment_rate: 28.75,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 36,
          installment_rate: 35.20,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ],
      issuer: {
        id: 'master',
        name: 'Mastercard',
        secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/mastercard.png',
        thumbnail: 'https://http2.mlstatic.com/payment-methods/mastercard.png'
      }
    },
    {
      id: 'debit_card',
      name: 'Tarjetas de débito',
      payment_type_id: 'debit_card',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/debit-card.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/debit-card.png',
      deferred_capture: 'unsupported',
      additional_info_needed: ['cardholder_name'],
      min_installments: 1,
      max_installments: 1,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 5,
          reimbursement_rate: null,
          labels: ['recommended']
        }
      ]
    },
    {
      id: 'pse',
      name: 'PSE',
      payment_type_id: 'bank_transfer',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/pse.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/pse.png',
      deferred_capture: 'unsupported',
      additional_info_needed: ['entity_type'],
      min_installments: 1,
      max_installments: 1,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ]
    },
    {
      id: 'efecty',
      name: 'Efecty',
      payment_type_id: 'ticket',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/efecty.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/efecty.png',
      deferred_capture: 'unsupported',
      additional_info_needed: [],
      min_installments: 1,
      max_installments: 1,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ]
    }
  ],
  'MLA': [
    {
      id: 'credit_card',
      name: 'Tarjetas de crédito',
      payment_type_id: 'credit_card',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/credit-card.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/credit-card.png',
      deferred_capture: 'supported',
      additional_info_needed: ['cardholder_name', 'cardholder_identification_number'],
      min_installments: 1,
      max_installments: 12,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: ['recommended']
        },
        {
          installments: 3,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 6,
          installment_rate: 28.50,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        },
        {
          installments: 12,
          installment_rate: 45.75,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ]
    },
    {
      id: 'rapipago',
      name: 'Rapipago',
      payment_type_id: 'ticket',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/rapipago.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/rapipago.png',
      deferred_capture: 'unsupported',
      additional_info_needed: [],
      min_installments: 1,
      max_installments: 1,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ]
    },
    {
      id: 'pagofacil',
      name: 'Pago Fácil',
      payment_type_id: 'ticket',
      status: 'active',
      secure_thumbnail: 'https://http2.mlstatic.com/payment-methods/pagofacil.png',
      thumbnail: 'https://http2.mlstatic.com/payment-methods/pagofacil.png',
      deferred_capture: 'unsupported',
      additional_info_needed: [],
      min_installments: 1,
      max_installments: 1,
      installments: [
        {
          installments: 1,
          installment_rate: 0,
          discount_rate: 0,
          reimbursement_rate: null,
          labels: []
        }
      ]
    }
  ]
};

export const getPaymentMethodsBySite = (siteId) => {
  return mockPaymentMethods[siteId] || [];
};

export const getPaymentMethodById = (paymentMethodId, siteId = 'MCO') => {
  const methods = mockPaymentMethods[siteId] || [];
  return methods.find(method => method.id === paymentMethodId) || null;
};

export const calculateInstallments = (amount, paymentMethodId, siteId = 'MCO') => {
  const paymentMethod = getPaymentMethodById(paymentMethodId, siteId);
  
  if (!paymentMethod?.installments) {
    return [];
  }
  
  const getCurrencyId = (site) => {
    if (site === 'MCO') return 'COP';
    if (site === 'MLA') return 'ARS';
    return 'USD';
  };
  
  return paymentMethod.installments.map(installment => ({
    quantity: installment.installments,
    installment_rate: installment.installment_rate,
    installment_amount: amount * (1 + installment.installment_rate / 100) / installment.installments,
    total_amount: amount * (1 + installment.installment_rate / 100),
    currency_id: getCurrencyId(siteId),
    labels: installment.labels || []
  }));
};
