// Hook para gestionar el estado de la aplicación con casos de uso
import { useState, useEffect } from 'react';

// Import individual use cases to avoid circular dependencies
let GetProductInfoUseCase, GetSellerInfoUseCase, GetPaymentMethodsUseCase, 
    GetProductQuestionsUseCase, GetProductReviewsUseCase, GetNavigationUseCase;

// Import service container
let getServiceContainer;

// Dynamic imports to avoid issues
const loadDependencies = async () => {
  try {
    const applicationModule = await import('../../application/index.js');
    GetProductInfoUseCase = applicationModule.GetProductInfoUseCase;
    GetSellerInfoUseCase = applicationModule.GetSellerInfoUseCase;
    GetPaymentMethodsUseCase = applicationModule.GetPaymentMethodsUseCase;
    GetProductQuestionsUseCase = applicationModule.GetProductQuestionsUseCase;
    GetProductReviewsUseCase = applicationModule.GetProductReviewsUseCase;
    GetNavigationUseCase = applicationModule.GetNavigationUseCase;

    const containerModule = await import('../../ServiceContainer.js');
    getServiceContainer = containerModule.getServiceContainer;
    
    return true;
  } catch (error) {
    console.error('Failed to load dependencies:', error);
    return false;
  }
};

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
      navigation: true,
      dependencies: true
    },
    
    // Error states
    errors: {},
    
    // UI state
    selectedVariation: null,
    selectedImage: 0,
    quantity: 1,
    
    // Dependencies loaded
    dependenciesLoaded: false
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

  // Initialize use cases
  const initializeUseCases = () => {
    if (!state.dependenciesLoaded || !getServiceContainer) {
      console.warn('Dependencies not loaded yet');
      return null;
    }
    
    try {
      const container = getServiceContainer();
      
      return {
        getProductInfo: new GetProductInfoUseCase(
          container.productAdapter,
          container.galleryAdapter,
          container.specificationsAdapter
        ),
        getSellerInfo: new GetSellerInfoUseCase(
          container.sellerAdapter
        ),
        getPaymentMethods: new GetPaymentMethodsUseCase(
          container.paymentAdapter
        ),
        getProductQuestions: new GetProductQuestionsUseCase(
          container.questionsAdapter
        ),
        getProductReviews: new GetProductReviewsUseCase(
          container.reviewsAdapter
        ),
        getNavigation: new GetNavigationUseCase(
          container.navigationAdapter
        )
      };
    } catch (error) {
      console.error('Error initializing use cases:', error);
      return null;
    }
  };

  // Load product data
  const loadProductData = async (useCases) => {
    try {
      updateLoading('product', true);
      const productData = await useCases.getProductInfo.execute(productId);
      updateState({ product: productData });
      updateLoading('product', false);
      
      // Load seller info with seller ID from product
      if (productData.seller?.id) {
        loadSellerData(useCases, productData.seller.id);
      }
    } catch (error) {
      updateError('product', error.message);
      updateLoading('product', false);
    }
  };

  // Load seller data
  const loadSellerData = async (useCases, sellerId) => {
    try {
      updateLoading('seller', true);
      const sellerData = await useCases.getSellerInfo.execute(sellerId);
      updateState({ seller: sellerData });
      updateLoading('seller', false);
    } catch (error) {
      updateError('seller', error.message);
      updateLoading('seller', false);
    }
  };

  // Load payment methods
  const loadPaymentMethods = async (useCases) => {
    try {
      updateLoading('payment', true);
      const paymentData = await useCases.getPaymentMethods.execute();
      updateState({ paymentMethods: paymentData });
      updateLoading('payment', false);
    } catch (error) {
      updateError('payment', error.message);
      updateLoading('payment', false);
    }
  };

  // Load questions
  const loadQuestions = async (useCases) => {
    try {
      updateLoading('questions', true);
      const questionsData = await useCases.getProductQuestions.execute(productId);
      updateState({ questions: questionsData });
      updateLoading('questions', false);
    } catch (error) {
      updateError('questions', error.message);
      updateLoading('questions', false);
    }
  };

  // Load reviews
  const loadReviews = async (useCases) => {
    try {
      updateLoading('reviews', true);
      const reviewsData = await useCases.getProductReviews.execute(productId);
      updateState({ reviews: reviewsData });
      updateLoading('reviews', false);
    } catch (error) {
      updateError('reviews', error.message);
      updateLoading('reviews', false);
    }
  };

  // Load navigation
  const loadNavigation = async (useCases) => {
    try {
      updateLoading('navigation', true);
      const navigationData = await useCases.getNavigation.execute(productId);
      updateState({ navigation: navigationData });
      updateLoading('navigation', false);
    } catch (error) {
      updateError('navigation', error.message);
      updateLoading('navigation', false);
    }
  };

  // Initialize data loading
  useEffect(() => {
    const initializeHook = async () => {
      // Load dependencies first
      updateLoading('dependencies', true);
      const dependenciesLoaded = await loadDependencies();
      
      if (!dependenciesLoaded) {
        updateError('dependencies', 'Failed to load application dependencies');
        updateLoading('dependencies', false);
        return;
      }
      
      updateState({ dependenciesLoaded: true });
      updateLoading('dependencies', false);
      
      // Initialize use cases
      const useCases = initializeUseCases();
      
      if (useCases) {
        // Load all data in parallel
        Promise.allSettled([
          loadProductData(useCases),
          loadPaymentMethods(useCases),
          loadQuestions(useCases),
          loadReviews(useCases),
          loadNavigation(useCases)
        ]);
      }
    };
    
    initializeHook();
  }, [productId]);

  // Actions for UI interactions
  const actions = {
    selectVariation: (variationId) => {
      updateState({ selectedVariation: variationId });
    },
    
    selectImage: (imageIndex) => {
      updateState({ selectedImage: imageIndex });
    },
    
    updateQuantity: (newQuantity) => {
      updateState({ quantity: Math.max(1, newQuantity) });
    },
    
    addToCart: () => {
      // TODO: Implement add to cart logic
      console.log('Adding to cart:', {
        productId,
        variation: state.selectedVariation,
        quantity: state.quantity
      });
    },
    
    buyNow: () => {
      // TODO: Implement buy now logic
      console.log('Buy now:', {
        productId,
        variation: state.selectedVariation,
        quantity: state.quantity
      });
    },
    
    askQuestion: (question) => {
      // TODO: Implement ask question logic
      console.log('Asking question:', question);
    },
    
    addReview: (review) => {
      // TODO: Implement add review logic
      console.log('Adding review:', review);
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
    availableQuantity: state.selectedVariation?.availableQuantity || state.product?.availableQuantity || 0
  };
};
