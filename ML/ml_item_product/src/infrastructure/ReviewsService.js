// Servicio para reviews y opiniones
class ReviewsService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getProductReviews(productId, limit = 10, offset = 0) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/reviews/item/${productId}?limit=${limit}&offset=${offset}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product reviews: ${error.message}`);
    }
  }

  async getReviewsFiltered(productId, filters = {}) {
    try {
      const params = new URLSearchParams({
        limit: filters.limit || 10,
        offset: filters.offset || 0,
        ...filters
      });
      
      const response = await this.apiClient.get(
        `${this.baseUrl}/reviews/item/${productId}?${params}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch filtered reviews: ${error.message}`);
    }
  }

  async getReviewById(reviewId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/reviews/${reviewId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch review ${reviewId}: ${error.message}`);
    }
  }

  async createReview(productId, reviewData, userId) {
    try {
      const review = {
        item_id: productId,
        reviewer_id: userId,
        rate: reviewData.rating,
        title: reviewData.title,
        content: reviewData.content,
        images: reviewData.images || []
      };
      
      const response = await this.apiClient.post(`${this.baseUrl}/reviews`, review);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to create review: ${error.message}`);
    }
  }

  async markReviewHelpful(reviewId, userId, isHelpful = true) {
    try {
      const action = isHelpful ? 'helpful' : 'not_helpful';
      const response = await this.apiClient.post(
        `${this.baseUrl}/reviews/${reviewId}/${action}`,
        { user_id: userId }
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to mark review as ${isHelpful ? 'helpful' : 'not helpful'}: ${error.message}`);
    }
  }

  async getReviewAttributes(productId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/reviews/item/${productId}/attributes`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch review attributes: ${error.message}`);
    }
  }

  async getReviewStats(productId) {
    try {
      const reviews = await this.getProductReviews(productId, 1000, 0);
      const totalReviews = reviews.paging?.total || 0;
      
      if (totalReviews === 0) {
        return {
          total: 0,
          average: 0,
          distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 }
        };
      }
      
      const ratings = reviews.reviews?.map(r => r.rate) || [];
      const average = ratings.reduce((sum, rating) => sum + rating, 0) / ratings.length;
      
      const distribution = ratings.reduce((dist, rating) => {
        dist[rating] = (dist[rating] || 0) + 1;
        return dist;
      }, { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 });
      
      return {
        total: totalReviews,
        average: Math.round(average * 10) / 10,
        distribution
      };
    } catch (error) {
      throw new Error(`Failed to fetch review stats: ${error.message}`);
    }
  }

  async reportReview(reviewId, reason, userId) {
    try {
      const reportData = {
        review_id: reviewId,
        reason,
        reporter_id: userId
      };
      
      const response = await this.apiClient.post(`${this.baseUrl}/reviews/${reviewId}/report`, reportData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to report review: ${error.message}`);
    }
  }

  async getSellerReviews(sellerId, limit = 50, offset = 0) {
    try {
      const response = await this.apiClient.get(
        `${this.baseUrl}/reviews/seller/${sellerId}?limit=${limit}&offset=${offset}`
      );
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch seller reviews: ${error.message}`);
    }
  }
}

export default ReviewsService;
