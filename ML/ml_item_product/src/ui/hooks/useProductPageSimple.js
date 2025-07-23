// Hook simplificado para gestionar datos de la página de producto
// Usa datos mock directos para evitar problemas de importación
import { useState, useEffect } from 'react';

export const useProductPage = (productId = 'MCO123456789') => {
  const [state, setState] = useState({
    // Product data
    product: null,
    seller: null,
    paymentMethods: null,
    questions: null,
    reviews: null,
    navigation: null,
    
    // Loading states
    loading: {
      product: true,
      seller: true,
      payment: true,
      questions: true,
      reviews: true,
      navigation: true
    },
    
    // Error states
    errors: {},
    
    // UI state
    selectedVariation: null,
    selectedImage: 0,
    quantity: 1
  });

  const updateState = (updates) => {
    setState(prev => ({
      ...prev,
      ...updates
    }));
  };

  const updateLoading = (key, value) => {
    setState(prev => ({
      ...prev,
      loading: {
        ...prev.loading,
        [key]: value
      }
    }));
  };

  const updateError = (key, error) => {
    setState(prev => ({
      ...prev,
      errors: {
        ...prev.errors,
        [key]: error
      }
    }));
  };

  // Mock data
  const mockProduct = {
    id: productId,
    title: 'iPhone 14 Pro Max 128GB - Morado Intenso',
    price: {
      amount: 4899000,
      currency_id: 'COP'
    },
    condition: 'new',
    sold_quantity: 47,
    available_quantity: 3,
    shipping: {
      free_shipping: true
    },
    images: [
      {
        url: 'https://http2.mlstatic.com/D_Q_NP_2X_952892-MLA82578193512_032025-R.webp',
        alt: 'iPhone 14 Pro Max - Vista frontal'
      },
      {
        url: 'https://http2.mlstatic.com/D_NQ_NP_2X_952892-MLA82578193512_032025-F.webp',
        alt: 'iPhone 14 Pro Max - Vista trasera'
      },
      {
        url: 'https://http2.mlstatic.com/D_Q_NP_2X_947859-MLU86339427239_062025-R.webp',
        alt: 'iPhone 14 Pro Max - Vista lateral'
      },
      {
        url: 'https://http2.mlstatic.com/D_Q_NP_2X_960729-MLA82578174454_032025-R.webp',
        alt: 'iPhone 14 Pro Max - Accesorios'
      }
    ],
    variations: [
      {
        id: 'var1',
        name: 'Morado Intenso',
        price: { amount: 4899000, currency_id: 'COP' },
        availableQuantity: 3,
        picture_ids: ['https://http2.mlstatic.com/D_Q_NP_2X_952892-MLA82578193512_032025-R.webp']
      },
      {
        id: 'var2',
        name: 'Negro Espacial',
        price: { amount: 4899000, currency_id: 'COP' },
        availableQuantity: 2,
        picture_ids: ['https://http2.mlstatic.com/D_NQ_NP_2X_952892-MLA82578193512_032025-F.webp']
      }
    ],
    reviews: {
      rating_average: 4.8,
      total_reviews: 54
    },
    highlights: [
      {
        title: 'Características destacadas',
        description: 'Pantalla Super Retina XDR de 6.7 pulgadas'
      },
      {
        title: 'Cámara profesional',
        description: 'Sistema de cámara Pro de 48MP'
      },
      {
        title: 'Chip A16 Bionic',
        description: 'El chip más rápido en un smartphone'
      }
    ],
    installments: {
      quantity: 12,
      amount: 408250,
      rate: 0
    },
    warranty: '12 meses de garantía oficial Apple',
    specifications: {
      generalSpecs: [
        { name: 'Pantalla', value: '6.7" Super Retina XDR' },
        { name: 'Almacenamiento', value: '128GB' },
        { name: 'Cámara', value: '48MP + 12MP + 12MP' },
        { name: 'Procesador', value: 'A16 Bionic' }
      ]
    }
  };

  const mockSeller = {
    id: 123456,
    nickname: 'TechStore Oficial',
    reputation: {
      level_id: 'platinum',
      power_seller_status: 'platinum',
      transactions: {
        total: 15432,
        completed: 15301
      }
    }
  };

  const mockPaymentMethods = [
    {
      id: 'credit_card',
      name: 'Tarjeta de crédito',
      thumbnail: 'https://http2.mlstatic.com/ui/navigation/3.12.0/mercadopago.png'
    },
    {
      id: 'debit_card',
      name: 'Tarjeta débito',
      thumbnail: 'https://http2.mlstatic.com/ui/navigation/3.12.0/mercadopago.png'
    }
  ];

  const mockQuestions = {
    questions: [
      {
        id: 12345001,
        text: '¿Está liberado para todas las operadoras?',
        answer: {
          text: 'Sí, completamente liberado para usar con cualquier operadora.',
          date_created: '2024-01-20T10:30:00.000Z'
        },
        date_created: '2024-01-19T15:45:00.000Z'
      }
    ],
    total: 1
  };

  const mockReviews = {
    reviews: [
      {
        id: 'rev_001',
        rating: 5,
        title: 'Excelente producto',
        content: 'Muy satisfecho con la compra, llegó rápido y en perfecto estado.',
        reviewer: 'Usuario verificado',
        date_created: '2024-01-18T14:20:00.000Z'
      }
    ],
    total_reviews: 54,
    rating_average: 4.8
  };

  const mockNavigation = {
    breadcrumbs: [
      { name: 'Inicio', url: '/' },
      { name: 'Celulares y Teléfonos', url: '/celulares' },
      { name: 'Celulares y Smartphones', url: '/celulares/smartphones' },
      { name: 'iPhone', url: '/celulares/iphone' }
    ]
  };

  // Simulate API calls with delays
  const simulateApiCall = (data, delay = 800) => {
    return new Promise(resolve => {
      setTimeout(() => resolve(data), delay);
    });
  };

  // Load functions
  const loadProductData = async () => {
    try {
      updateLoading('product', true);
      const productData = await simulateApiCall(mockProduct, 600);
      updateState({ product: productData });
      updateLoading('product', false);
    } catch (error) {
      updateError('product', error.message);
      updateLoading('product', false);
    }
  };

  const loadSellerData = async () => {
    try {
      updateLoading('seller', true);
      const sellerData = await simulateApiCall(mockSeller, 400);
      updateState({ seller: sellerData });
      updateLoading('seller', false);
    } catch (error) {
      updateError('seller', error.message);
      updateLoading('seller', false);
    }
  };

  const loadPaymentMethods = async () => {
    try {
      updateLoading('payment', true);
      const paymentData = await simulateApiCall(mockPaymentMethods, 500);
      updateState({ paymentMethods: paymentData });
      updateLoading('payment', false);
    } catch (error) {
      updateError('payment', error.message);
      updateLoading('payment', false);
    }
  };

  const loadQuestions = async () => {
    try {
      updateLoading('questions', true);
      const questionsData = await simulateApiCall(mockQuestions, 700);
      updateState({ questions: questionsData });
      updateLoading('questions', false);
    } catch (error) {
      updateError('questions', error.message);
      updateLoading('questions', false);
    }
  };

  const loadReviews = async () => {
    try {
      updateLoading('reviews', true);
      const reviewsData = await simulateApiCall(mockReviews, 600);
      updateState({ reviews: reviewsData });
      updateLoading('reviews', false);
    } catch (error) {
      updateError('reviews', error.message);
      updateLoading('reviews', false);
    }
  };

  const loadNavigation = async () => {
    try {
      updateLoading('navigation', true);
      const navigationData = await simulateApiCall(mockNavigation, 300);
      updateState({ navigation: navigationData });
      updateLoading('navigation', false);
    } catch (error) {
      updateError('navigation', error.message);
      updateLoading('navigation', false);
    }
  };

  // Initialize data loading
  useEffect(() => {
    // Load all data in parallel
    Promise.allSettled([
      loadProductData(),
      loadSellerData(),
      loadPaymentMethods(),
      loadQuestions(),
      loadReviews(),
      loadNavigation()
    ]);
  }, [productId]);

  // Actions for UI interactions
  const actions = {
    selectVariation: (variationId) => {
      const variation = state.product?.variations?.find(v => v.id === variationId);
      updateState({ selectedVariation: variation });
    },
    
    selectImage: (imageIndex) => {
      updateState({ selectedImage: imageIndex });
    },
    
    updateQuantity: (newQuantity) => {
      const maxQuantity = state.selectedVariation?.availableQuantity || state.product?.available_quantity || 0;
      updateState({ quantity: Math.max(1, Math.min(newQuantity, maxQuantity)) });
    },
    
    addToCart: () => {
      console.log('Adding to cart:', {
        productId,
        variation: state.selectedVariation,
        quantity: state.quantity
      });
      // Future: Implement actual cart logic
    },
    
    buyNow: () => {
      console.log('Buy now:', {
        productId,
        variation: state.selectedVariation,
        quantity: state.quantity
      });
      // Future: Implement actual purchase logic
    },
    
    askQuestion: (question) => {
      console.log('Asking question:', question);
      // Future: Implement question submission
    },
    
    addReview: (review) => {
      console.log('Adding review:', review);
      // Future: Implement review submission
    }
  };

  return {
    // Data
    product: state.product,
    seller: state.seller,
    paymentMethods: state.paymentMethods,
    questions: state.questions,
    reviews: state.reviews,
    navigation: state.navigation,
    
    // UI State
    selectedVariation: state.selectedVariation,
    selectedImage: state.selectedImage,
    quantity: state.quantity,
    
    // Loading and error states
    loading: state.loading,
    errors: state.errors,
    
    // Actions
    actions,
    
    // Computed values
    isLoading: Object.values(state.loading).some(Boolean),
    hasErrors: Object.keys(state.errors).length > 0,
    selectedImageUrl: state.product?.images?.[state.selectedImage]?.url,
    currentPrice: state.selectedVariation?.price || state.product?.price,
    availableQuantity: state.selectedVariation?.availableQuantity || state.product?.available_quantity || 0
  };
};
