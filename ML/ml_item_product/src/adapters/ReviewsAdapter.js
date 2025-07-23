// Adaptador para opiniones y reviews del producto
class ReviewsAdapter {
  constructor(reviewsService) {
    this.reviewsService = reviewsService;
  }

  async getProductReviews(productId, limit = 10, offset = 0) {
    try {
      const rawReviews = await this.reviewsService.getProductReviews(productId, limit, offset);
      return this.transformReviews(rawReviews);
    } catch (error) {
      throw new Error(`Error fetching product reviews: ${error.message}`);
    }
  }

  transformReviews(rawReviews) {
    return {
      paging: rawReviews.paging,
      reviews: rawReviews.reviews?.map(review => this.transformReview(review)) || [],
      ratingSummary: this.transformRatingSummary(rawReviews.rating_summary),
      averages: rawReviews.rating_averages ? {
        overall: rawReviews.rating_averages.overall,
        quality: rawReviews.rating_averages.quality,
        shipping: rawReviews.rating_averages.shipping,
        service: rawReviews.rating_averages.service
      } : null
    };
  }

  transformReview(review) {
    return {
      id: review.id,
      rating: review.rate,
      title: review.title,
      content: review.content,
      dateCreated: review.date_created,
      status: review.status,
      reviewer: {
        id: review.reviewer?.id,
        nickname: review.reviewer?.nickname,
        avatar: review.reviewer?.avatar
      },
      likes: review.likes,
      dislikes: review.dislikes,
      relevance: review.relevance,
      valorization: review.valorization,
      buyingDate: review.buying_date,
      helpfulCount: review.helpful_count,
      notHelpfulCount: review.not_helpful_count,
      images: review.images?.map(img => ({
        id: img.id,
        url: img.url,
        thumbnailUrl: img.thumbnail_url
      })) || []
    };
  }

  transformRatingSummary(ratingSummary) {
    if (!ratingSummary) return null;

    return {
      total: ratingSummary.total,
      average: ratingSummary.average,
      distribution: {
        5: ratingSummary['5'] || 0,
        4: ratingSummary['4'] || 0,
        3: ratingSummary['3'] || 0,
        2: ratingSummary['2'] || 0,
        1: ratingSummary['1'] || 0
      }
    };
  }

  async getReviewsFiltered(productId, filters = {}) {
    try {
      const rawReviews = await this.reviewsService.getReviewsFiltered(productId, filters);
      return this.transformReviews(rawReviews);
    } catch (error) {
      throw new Error(`Error fetching filtered reviews: ${error.message}`);
    }
  }

  async createReview(productId, reviewData, userId) {
    try {
      const response = await this.reviewsService.createReview(productId, reviewData, userId);
      return this.transformReview(response);
    } catch (error) {
      throw new Error(`Error creating review: ${error.message}`);
    }
  }

  async markReviewHelpful(reviewId, userId, isHelpful = true) {
    try {
      const response = await this.reviewsService.markReviewHelpful(reviewId, userId, isHelpful);
      return {
        success: response.success,
        helpfulCount: response.helpful_count,
        notHelpfulCount: response.not_helpful_count
      };
    } catch (error) {
      throw new Error(`Error marking review as helpful: ${error.message}`);
    }
  }

  async getReviewAttributes(productId) {
    try {
      const attributes = await this.reviewsService.getReviewAttributes(productId);
      return attributes.map(attr => ({
        id: attr.id,
        name: attr.name,
        values: attr.values?.map(value => ({
          id: value.id,
          name: value.name,
          count: value.count
        })) || []
      }));
    } catch (error) {
      throw new Error(`Error fetching review attributes: ${error.message}`);
    }
  }
}

export default ReviewsAdapter;
