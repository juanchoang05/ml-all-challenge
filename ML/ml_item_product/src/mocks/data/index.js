// Índice centralizado de todos los mock data
export * from './products.js';
export * from './sellers.js';
export * from './payments.js';
export * from './questions.js';
export * from './reviews.js';
export * from './categories.js';
export * from './shipping.js';
export * from './purchases.js';

// Función utilitaria para simular delay de red
export const simulateNetworkDelay = async (ms = 1000) => {
  return new Promise(resolve => setTimeout(resolve, ms));
};

// Función utilitaria para simular errores aleatorios
export const simulateRandomError = (errorRate = 0.1) => {
  if (Math.random() < errorRate) {
    throw new Error('Simulated network error');
  }
};

// Función para verificar si estamos en modo mock
export const isMockMode = () => {
  return process.env.NODE_ENV === 'development' && 
         (process.env.REACT_APP_USE_MOCK_DATA === 'true' || 
          process.env.REACT_APP_ENABLE_MOCK_DATA === 'true');
};

// Configuración de mock por defecto
export const mockConfig = {
  defaultDelay: 1000,
  errorRate: 0.05, // 5% de probabilidad de error
  enableLogs: true
};

// Logger para mocks
export const mockLogger = {
  log: (service, method, data) => {
    if (mockConfig.enableLogs && process.env.NODE_ENV === 'development') {
      console.log(`[MOCK] ${service}.${method}`, data);
    }
  },
  error: (service, method, error) => {
    if (mockConfig.enableLogs) {
      console.error(`[MOCK ERROR] ${service}.${method}`, error);
    }
  }
};
