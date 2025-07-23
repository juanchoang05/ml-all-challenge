// Configuraciones específicas por región/país
export const regionConfigs = {
  colombia: {
    siteId: 'MCO',
    currency: 'COP',
    language: 'es',
    timezone: 'America/Bogota',
    paymentMethods: ['visa', 'mastercard', 'pse', 'efecty', 'mercadopago'],
    shippingMethods: ['standard', 'express', 'pickup'],
    features: {
      mercadoCredits: true,
      mercadoPoints: true,
      mercadoPago: true
    }
  },
  argentina: {
    siteId: 'MLA',
    currency: 'ARS',
    language: 'es',
    timezone: 'America/Argentina/Buenos_Aires',
    paymentMethods: ['visa', 'mastercard', 'rapipago', 'pagofacil', 'mercadopago'],
    shippingMethods: ['standard', 'express', 'pickup', 'correo_argentino'],
    features: {
      mercadoCredits: true,
      mercadoPoints: true,
      mercadoPago: true
    }
  },
  mexico: {
    siteId: 'MLM',
    currency: 'MXN',
    language: 'es',
    timezone: 'America/Mexico_City',
    paymentMethods: ['visa', 'mastercard', 'oxxo', 'mercadopago'],
    shippingMethods: ['standard', 'express', 'pickup'],
    features: {
      mercadoCredits: true,
      mercadoPoints: true,
      mercadoPago: true
    }
  },
  brazil: {
    siteId: 'MLB',
    currency: 'BRL',
    language: 'pt',
    timezone: 'America/Sao_Paulo',
    paymentMethods: ['visa', 'mastercard', 'boleto', 'pix', 'mercadopago'],
    shippingMethods: ['standard', 'express', 'pickup', 'correios'],
    features: {
      mercadoCredits: true,
      mercadoPoints: true,
      mercadoPago: true
    }
  },
  chile: {
    siteId: 'MLC',
    currency: 'CLP',
    language: 'es',
    timezone: 'America/Santiago',
    paymentMethods: ['visa', 'mastercard', 'mercadopago'],
    shippingMethods: ['standard', 'express', 'pickup'],
    features: {
      mercadoCredits: true,
      mercadoPoints: true,
      mercadoPago: true
    }
  }
};

export const getRegionConfig = (country = 'colombia') => {
  return regionConfigs[country.toLowerCase()] || regionConfigs.colombia;
};

export const detectRegionFromUrl = () => {
  if (typeof window === 'undefined') return 'colombia';
  
  const hostname = window.location.hostname;
  
  if (hostname.includes('.com.ar')) return 'argentina';
  if (hostname.includes('.com.mx')) return 'mexico';
  if (hostname.includes('.com.br')) return 'brazil';
  if (hostname.includes('.cl')) return 'chile';
  if (hostname.includes('.com.co')) return 'colombia';
  
  return 'colombia'; // default
};

export default regionConfigs;
