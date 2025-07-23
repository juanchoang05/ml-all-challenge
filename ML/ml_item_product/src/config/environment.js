// Variables de entorno y configuración sensible
export const envConfig = {
  development: {
    API_BASE_URL: process.env.REACT_APP_API_BASE_URL || 'https://api.mercadolibre.com',
    API_TIMEOUT: parseInt(process.env.REACT_APP_API_TIMEOUT) || 10000,
    ENABLE_LOGGING: process.env.REACT_APP_ENABLE_LOGGING === 'true',
    ENABLE_CACHE: process.env.REACT_APP_ENABLE_CACHE !== 'false',
    CACHE_TTL: parseInt(process.env.REACT_APP_CACHE_TTL) || 300000, // 5 minutes
    
    // Analytics
    GOOGLE_ANALYTICS_ID: process.env.REACT_APP_GA_ID,
    HOTJAR_ID: process.env.REACT_APP_HOTJAR_ID,
    
    // Error tracking
    SENTRY_DSN: process.env.REACT_APP_SENTRY_DSN,
    
    // Feature flags
    ENABLE_REVIEWS: process.env.REACT_APP_ENABLE_REVIEWS !== 'false',
    ENABLE_QUESTIONS: process.env.REACT_APP_ENABLE_QUESTIONS !== 'false',
    ENABLE_RECOMMENDATIONS: process.env.REACT_APP_ENABLE_RECOMMENDATIONS !== 'false',
    
    // Mock data
    USE_MOCK_DATA: process.env.REACT_APP_USE_MOCK_DATA === 'true',
    MOCK_DELAY: parseInt(process.env.REACT_APP_MOCK_DELAY) || 1000,
  },
  
  production: {
    API_BASE_URL: process.env.REACT_APP_API_BASE_URL || 'https://api.mercadolibre.com',
    API_TIMEOUT: parseInt(process.env.REACT_APP_API_TIMEOUT) || 15000,
    ENABLE_LOGGING: false, // Siempre false en producción
    ENABLE_CACHE: true,
    CACHE_TTL: parseInt(process.env.REACT_APP_CACHE_TTL) || 600000, // 10 minutes
    
    // Analytics
    GOOGLE_ANALYTICS_ID: process.env.REACT_APP_GA_ID,
    HOTJAR_ID: process.env.REACT_APP_HOTJAR_ID,
    
    // Error tracking
    SENTRY_DSN: process.env.REACT_APP_SENTRY_DSN,
    
    // Feature flags
    ENABLE_REVIEWS: true,
    ENABLE_QUESTIONS: true,
    ENABLE_RECOMMENDATIONS: true,
    
    // Mock data
    USE_MOCK_DATA: false,
    MOCK_DELAY: 0,
  },
  
  testing: {
    API_BASE_URL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:3001/api',
    API_TIMEOUT: parseInt(process.env.REACT_APP_API_TIMEOUT) || 5000,
    ENABLE_LOGGING: true,
    ENABLE_CACHE: false,
    CACHE_TTL: 0,
    
    // Analytics deshabilitado en testing
    GOOGLE_ANALYTICS_ID: null,
    HOTJAR_ID: null,
    
    // Error tracking
    SENTRY_DSN: null,
    
    // Feature flags
    ENABLE_REVIEWS: true,
    ENABLE_QUESTIONS: true,
    ENABLE_RECOMMENDATIONS: false, // Deshabilitar en testing
    
    // Mock data
    USE_MOCK_DATA: true,
    MOCK_DELAY: 100,
  }
};

export const getEnvConfig = (environment = 'development') => {
  return envConfig[environment] || envConfig.development;
};

// Helper para verificar si una feature está habilitada
export const isFeatureEnabled = (featureName, environment = 'development') => {
  const config = getEnvConfig(environment);
  return config[featureName] === true;
};

// Helper para obtener URLs de API con fallbacks
export const getApiUrl = (endpoint, environment = 'development') => {
  const config = getEnvConfig(environment);
  return `${config.API_BASE_URL}${endpoint}`;
};

export default envConfig;
