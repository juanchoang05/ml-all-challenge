// Mock Service para ReviewsService
import { 
  getReviewsByItemId, 
  getReviewById, 
  createReview, 
  likeReview, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockReviewsService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'ReviewsService';
  }

  async getReviewsByItem(itemId, options = {}) {
    mockLogger.log(this.serviceName, 'getReviewsByItem', { itemId, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 700);
      simulateRandomError(0.03);
      
      const result = getReviewsByItemId(itemId, options);
      
      return {
        success: true,
        data: result
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getReviewsByItem', error);
      throw error;
    }
  }

  async getReviewById(reviewId) {
    mockLogger.log(this.serviceName, 'getReviewById', { reviewId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const review = getReviewById(reviewId);
      
      if (!review) {
        throw new Error(`Review with id ${reviewId} not found`);
      }
      
      return {
        success: true,
        data: review
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getReviewById', error);
      throw error;
    }
  }

  async createReview(itemId, reviewData) {
    mockLogger.log(this.serviceName, 'createReview', { itemId, reviewData });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 1000);
      simulateRandomError(0.04);
      
      // Validar datos requeridos
      if (!reviewData.rating || reviewData.rating < 1 || reviewData.rating > 5) {
        throw new Error('Rating must be between 1 and 5');
      }
      
      if (!reviewData.title || reviewData.title.trim().length === 0) {
        throw new Error('Title is required');
      }
      
      const newReview = createReview(itemId, reviewData);
      
      return {
        success: true,
        data: newReview
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'createReview', error);
      throw error;
    }
  }

  async likeReview(reviewId) {
    mockLogger.log(this.serviceName, 'likeReview', { reviewId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      const likedReview = likeReview(reviewId);
      
      if (!likedReview) {
        throw new Error(`Review with id ${reviewId} not found`);
      }
      
      return {
        success: true,
        data: likedReview
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'likeReview', error);
      throw error;
    }
  }

  async dislikeReview(reviewId) {
    mockLogger.log(this.serviceName, 'dislikeReview', { reviewId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      const review = getReviewById(reviewId);
      
      if (!review) {
        throw new Error(`Review with id ${reviewId} not found`);
      }
      
      // Simular dislike
      review.dislikes += 1;
      
      return {
        success: true,
        data: review
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'dislikeReview', error);
      throw error;
    }
  }

  async getReviewsByRating(itemId, rating, options = {}) {
    mockLogger.log(this.serviceName, 'getReviewsByRating', { itemId, rating, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const result = getReviewsByItemId(itemId, { 
        ...options, 
        rating: parseInt(rating) 
      });
      
      return {
        success: true,
        data: result
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getReviewsByRating', error);
      throw error;
    }
  }

  async getReviewStatistics(itemId) {
    mockLogger.log(this.serviceName, 'getReviewStatistics', { itemId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const reviewData = getReviewsByItemId(itemId, { limit: 1000, offset: 0 });
      
      if (reviewData.total_reviews === 0) {
        return {
          success: true,
          data: {
            item_id: itemId,
            total_reviews: 0,
            rating_average: 0,
            rating_distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 },
            attributes_rating: {}
          }
        };
      }
      
      return {
        success: true,
        data: {
          item_id: itemId,
          total_reviews: reviewData.total_reviews,
          rating_average: reviewData.rating_average,
          rating_distribution: reviewData.rating_distribution,
          attributes_rating: reviewData.attributes_rating,
          recent_reviews_count: reviewData.reviews.filter(review => {
            const reviewDate = new Date(review.date_created);
            const thirtyDaysAgo = new Date();
            thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
            return reviewDate >= thirtyDaysAgo;
          }).length
        }
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getReviewStatistics', error);
      throw error;
    }
  }

  async searchReviews(itemId, searchText, options = {}) {
    mockLogger.log(this.serviceName, 'searchReviews', { itemId, searchText, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const allReviews = getReviewsByItemId(itemId, { limit: 1000, offset: 0 });
      
      // Filtrar reviews por texto de búsqueda
      const filteredReviews = allReviews.reviews.filter(review => 
        review.title.toLowerCase().includes(searchText.toLowerCase()) ||
        review.content.toLowerCase().includes(searchText.toLowerCase())
      );
      
      const { limit = 10, offset = 0 } = options;
      const results = filteredReviews.slice(offset, offset + limit);
      
      return {
        success: true,
        data: {
          ...allReviews,
          reviews: results,
          total_filtered: filteredReviews.length,
          search_term: searchText
        }
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'searchReviews', error);
      throw error;
    }
  }

  async reportReview(reviewId, reason) {
    mockLogger.log(this.serviceName, 'reportReview', { reviewId, reason });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const review = getReviewById(reviewId);
      
      if (!review) {
        throw new Error(`Review with id ${reviewId} not found`);
      }
      
      // Simular reporte de review
      const report = {
        id: `report_${Date.now()}`,
        review_id: reviewId,
        reason,
        status: 'pending',
        date_created: new Date().toISOString()
      };
      
      return {
        success: true,
        data: report
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'reportReview', error);
      throw error;
    }
  }

  async getReviewImages(reviewId) {
    mockLogger.log(this.serviceName, 'getReviewImages', { reviewId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      const review = getReviewById(reviewId);
      
      if (!review) {
        throw new Error(`Review with id ${reviewId} not found`);
      }
      
      return {
        success: true,
        data: review.images || []
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getReviewImages', error);
      throw error;
    }
  }
}
