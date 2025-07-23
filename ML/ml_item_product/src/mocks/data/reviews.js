// Mock data para reviews/opiniones
export const mockReviews = {
  'MCO123456789': {
    item_id: 'MCO123456789',
    rating_average: 4.7,
    total_reviews: 47,
    reviews: [
      {
        id: 'rev_001',
        rating: 5,
        title: 'Excelente producto, llegó muy rápido',
        content: 'El iPhone funciona perfecto, la batería dura todo el día y la cámara es increíble. El vendedor muy responsable y el envío súper rápido. Totalmente recomendado.',
        date_created: '2024-01-20T10:30:00.000Z',
        reviewer: {
          id: 'user_001',
          nickname: 'CarlosM_2023',
          level_id: 'gold'
        },
        likes: 12,
        dislikes: 0,
        helpful_count: 8,
        images: [
          {
            id: 'img_001',
            url: 'https://http2.mlstatic.com/reviews/iphone-review-001.jpg',
            size: '800x600'
          }
        ],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_002',
        rating: 5,
        title: 'Tal como se describe',
        content: 'Producto original, en perfectas condiciones. El color morado es hermoso y la calidad de construcción es premium como siempre en Apple.',
        date_created: '2024-01-18T15:45:00.000Z',
        reviewer: {
          id: 'user_002',
          nickname: 'TechLover_2024',
          level_id: 'platinum'
        },
        likes: 8,
        dislikes: 1,
        helpful_count: 6,
        images: [],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_003',
        rating: 4,
        title: 'Buen producto pero demoró el envío',
        content: 'El iPhone está perfecto, funciona muy bien y sin problemas. Solo que el envío se demoró un poco más de lo esperado, pero al final llegó bien empacado.',
        date_created: '2024-01-15T09:20:00.000Z',
        reviewer: {
          id: 'user_003',
          nickname: 'Maria_Bogota',
          level_id: 'silver'
        },
        likes: 5,
        dislikes: 2,
        helpful_count: 3,
        images: [],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_004',
        rating: 5,
        title: '¡Increíble!',
        content: 'Superó mis expectativas. La pantalla se ve espectacular, el rendimiento es excelente y la duración de la batería muy buena. Vendedor 100% confiable.',
        date_created: '2024-01-12T14:30:00.000Z',
        reviewer: {
          id: 'user_004',
          nickname: 'AndroidUser_Convert',
          level_id: 'gold'
        },
        likes: 15,
        dislikes: 0,
        helpful_count: 11,
        images: [
          {
            id: 'img_002',
            url: 'https://http2.mlstatic.com/reviews/iphone-review-002.jpg',
            size: '800x600'
          },
          {
            id: 'img_003',
            url: 'https://http2.mlstatic.com/reviews/iphone-review-003.jpg',
            size: '800x600'
          }
        ],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_005',
        rating: 3,
        title: 'Producto correcto pero precio alto',
        content: 'El teléfono funciona bien y está en buenas condiciones, pero considero que el precio está un poco elevado comparado con otros vendedores. Aún así, es un buen producto.',
        date_created: '2024-01-10T11:15:00.000Z',
        reviewer: {
          id: 'user_005',
          nickname: 'BargainHunter',
          level_id: 'bronze'
        },
        likes: 3,
        dislikes: 8,
        helpful_count: 1,
        images: [],
        valorization: 'NEUTRAL',
        status: 'ACTIVE'
      }
    ],
    rating_distribution: {
      5: 34,
      4: 8,
      3: 3,
      2: 1,
      1: 1
    },
    attributes_rating: {
      'quality': 4.8,
      'price': 4.2,
      'shipping': 4.6,
      'service': 4.9
    }
  },
  'MLA987654321': {
    item_id: 'MLA987654321',
    rating_average: 4.3,
    total_reviews: 23,
    reviews: [
      {
        id: 'rev_arg_001',
        rating: 5,
        title: 'Excelente notebook para trabajo',
        content: 'Muy buena calidad, rápida para trabajar con múltiples programas. La batería dura bastante y la pantalla se ve muy bien.',
        date_created: '2024-01-19T13:20:00.000Z',
        reviewer: {
          id: 'user_arg_001',
          nickname: 'WorkFromHome_BA',
          level_id: 'gold'
        },
        likes: 7,
        dislikes: 0,
        helpful_count: 5,
        images: [],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_arg_002',
        rating: 4,
        title: 'Buena relación precio-calidad',
        content: 'Cumple con lo que promete. Para uso básico y de oficina está perfecto. Llegó en tiempo y forma.',
        date_created: '2024-01-16T16:45:00.000Z',
        reviewer: {
          id: 'user_arg_002',
          nickname: 'OfficeUser_2024',
          level_id: 'silver'
        },
        likes: 4,
        dislikes: 1,
        helpful_count: 3,
        images: [],
        valorization: 'POSITIVE',
        status: 'ACTIVE'
      },
      {
        id: 'rev_arg_003',
        rating: 3,
        title: 'Cumple pero nada extraordinario',
        content: 'La notebook está bien para tareas básicas, pero para gaming o programas pesados se queda un poco corta. Construcción sólida.',
        date_created: '2024-01-14T10:30:00.000Z',
        reviewer: {
          id: 'user_arg_003',
          nickname: 'GamerBoy_ARG',
          level_id: 'bronze'
        },
        likes: 2,
        dislikes: 3,
        helpful_count: 1,
        images: [],
        valorization: 'NEUTRAL',
        status: 'ACTIVE'
      }
    ],
    rating_distribution: {
      5: 12,
      4: 7,
      3: 3,
      2: 1,
      1: 0
    },
    attributes_rating: {
      'quality': 4.4,
      'price': 4.1,
      'shipping': 4.5,
      'service': 4.6
    }
  }
};

export const getReviewsByItemId = (itemId, options = {}) => {
  const { limit = 10, offset = 0, rating = null } = options;
  const itemReviews = mockReviews[itemId];
  
  if (!itemReviews) {
    return {
      item_id: itemId,
      rating_average: 0,
      total_reviews: 0,
      reviews: [],
      rating_distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 },
      attributes_rating: {}
    };
  }
  
  let filteredReviews = itemReviews.reviews;
  
  if (rating) {
    filteredReviews = itemReviews.reviews.filter(review => review.rating === rating);
  }
  
  const results = filteredReviews.slice(offset, offset + limit);
  
  return {
    ...itemReviews,
    reviews: results,
    total_filtered: filteredReviews.length,
    offset,
    limit
  };
};

export const getReviewById = (reviewId) => {
  const allReviews = Object.values(mockReviews).flatMap(item => item.reviews);
  return allReviews.find(review => review.id === reviewId) || null;
};

export const createReview = (itemId, reviewData) => {
  const getValorization = (rating) => {
    if (rating >= 4) return 'POSITIVE';
    if (rating === 3) return 'NEUTRAL';
    return 'NEGATIVE';
  };

  const newReview = {
    id: `rev_${Date.now()}`,
    rating: reviewData.rating,
    title: reviewData.title,
    content: reviewData.content,
    date_created: new Date().toISOString(),
    reviewer: {
      id: reviewData.userId,
      nickname: reviewData.userNickname || 'Usuario_Anónimo',
      level_id: 'bronze'
    },
    likes: 0,
    dislikes: 0,
    helpful_count: 0,
    images: reviewData.images || [],
    valorization: getValorization(reviewData.rating),
    status: 'ACTIVE'
  };
  
  if (!mockReviews[itemId]) {
    mockReviews[itemId] = {
      item_id: itemId,
      rating_average: 0,
      total_reviews: 0,
      reviews: [],
      rating_distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 },
      attributes_rating: {}
    };
  }
  
  mockReviews[itemId].reviews.unshift(newReview);
  mockReviews[itemId].total_reviews += 1;
  mockReviews[itemId].rating_distribution[reviewData.rating] += 1;
  
  // Recalcular promedio
  const allRatings = mockReviews[itemId].reviews.map(r => r.rating);
  const average = allRatings.reduce((sum, rating) => sum + rating, 0) / allRatings.length;
  mockReviews[itemId].rating_average = Math.round(average * 10) / 10;
  
  return newReview;
};

export const likeReview = (reviewId) => {
  const allReviews = Object.values(mockReviews).flatMap(item => item.reviews);
  const review = allReviews.find(r => r.id === reviewId);
  
  if (review) {
    review.likes += 1;
    review.helpful_count += 1;
    return review;
  }
  
  return null;
};
