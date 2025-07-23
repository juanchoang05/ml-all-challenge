// Configuración para ambiente local de desarrollo
export const localConfig = {
  api: {
    baseUrl: 'http://localhost:3001/api', // Servidor local de desarrollo
    timeout: 8000,
    retryAttempts: 2,
    retryDelay: 1000
  },
  endpoints: {
    // Productos
    items: '/items',
    itemById: '/items/{id}',
    itemDescription: '/items/{id}/description',
    itemQuestions: '/questions/search?item={id}',
    itemReviews: '/reviews/item/{id}',
    itemShippingOptions: '/items/{id}/shipping_options',
    
    // Usuarios y vendedores
    users: '/users',
    userById: '/users/{id}',
    userItems: '/users/{id}/items/search',
    userPaymentMethods: '/users/{id}/accepted_payment_methods',
    userShippingPreferences: '/users/{id}/shipping_preferences',
    userAddresses: '/users/{id}/addresses',
    
    // Categorías
    categories: '/categories',
    categoryById: '/categories/{id}',
    categoryAttributes: '/categories/{id}/attributes',
    siteCategories: '/sites/{siteId}/categories',
    categoryPredictor: '/sites/{siteId}/category_predictor/predict',
    
    // Búsqueda
    search: '/sites/{siteId}/search',
    
    // Preguntas
    questions: '/questions',
    questionById: '/questions/{id}',
    questionsSearch: '/questions/search',
    answers: '/answers',
    
    // Reviews
    reviews: '/reviews',
    reviewById: '/reviews/{id}',
    reviewsItem: '/reviews/item/{id}',
    reviewsSeller: '/reviews/seller/{id}',
    reviewAttributes: '/reviews/item/{id}/attributes',
    
    // Pagos
    paymentMethods: '/sites/{siteId}/payment_methods',
    installments: '/sites/{siteId}/payment_methods/installments',
    payments: '/payments',
    
    // Envíos
    shippingServices: '/shipping_services',
    dropOffPoints: '/shipping_services/{serviceId}/drop_off_points',
    shippingPromise: '/items/{id}/shipping_promise',
    
    // Compras
    carts: '/carts',
    checkout: '/checkout',
    orders: '/orders',
    orderById: '/orders/{id}',
    shipments: '/shipments',
    shipmentById: '/shipments/{id}',
    
    // Otros
    countries: '/countries',
    zipCodes: '/countries/{countryId}/zip_codes/{zipCode}',
    trends: '/sites/{siteId}/trends',
    pictures: '/pictures/{id}'
  },
  features: {
    enableCaching: false, // Cache deshabilitado en desarrollo local
    enableRetry: true,
    enableLogging: true,
    enableMockData: true,  // Datos mock para desarrollo
    useMockData: true      // Usar servicios mock
  },
  mockData: {
    delay: 800,      // Delay más rápido para desarrollo local
    errorRate: 0.02, // Menos errores en desarrollo local
    enableLogs: true
  },
  defaults: {
    siteId: 'MCO',
    currency: 'COP',
    limit: 20,
    offset: 0
  }
};

export default localConfig;
