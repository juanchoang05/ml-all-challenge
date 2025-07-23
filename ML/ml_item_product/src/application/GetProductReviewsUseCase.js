// Caso de uso para obtener opiniones y reviews
class GetProductReviewsUseCase {
  constructor(reviewsAdapter) {
    this.reviewsAdapter = reviewsAdapter;
  }

  async execute(productId, filters = {}) {
    try {
      const limit = filters.limit || 10;
      const offset = filters.offset || 0;
      
      const reviews = await this.reviewsAdapter.getProductReviews(productId, limit, offset);
      const stats = await this.reviewsAdapter.getReviewStats ? 
        await this.reviewsAdapter.getReviewStats(productId) : 
        this.calculateStatsFromReviews(reviews);
      
      return {
        ...reviews,
        stats,
        hasReviews: reviews.reviews.length > 0,
        categorized: this.categorizeReviews(reviews.reviews),
        insights: this.generateReviewInsights(reviews.reviews, stats),
        summary: this.generateReviewsSummary(stats)
      };
    } catch (error) {
      throw new Error(`Failed to get product reviews: ${error.message}`);
    }
  }

  calculateStatsFromReviews(reviews) {
    if (!reviews.reviews || reviews.reviews.length === 0) {
      return {
        total: 0,
        average: 0,
        distribution: { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 }
      };
    }

    const ratings = reviews.reviews.map(r => r.rating);
    const total = ratings.length;
    const average = ratings.reduce((sum, rating) => sum + rating, 0) / total;
    
    const distribution = ratings.reduce((dist, rating) => {
      dist[rating] = (dist[rating] || 0) + 1;
      return dist;
    }, { 5: 0, 4: 0, 3: 0, 2: 0, 1: 0 });

    return { total, average: Math.round(average * 10) / 10, distribution };
  }

  categorizeReviews(reviews) {
    const categories = {
      positive: [], // 4-5 estrellas
      neutral: [],  // 3 estrellas
      negative: [], // 1-2 estrellas
      withImages: [],
      recent: []
    };
    
    const now = new Date();
    const oneMonthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000);
    
    reviews.forEach(review => {
      if (review.rating >= 4) {
        categories.positive.push(review);
      } else if (review.rating === 3) {
        categories.neutral.push(review);
      } else {
        categories.negative.push(review);
      }
      
      if (review.images && review.images.length > 0) {
        categories.withImages.push(review);
      }
      
      const reviewDate = new Date(review.dateCreated);
      if (reviewDate > oneMonthAgo) {
        categories.recent.push(review);
      }
    });
    
    return categories;
  }

  generateReviewInsights(reviews, stats) {
    return {
      satisfactionLevel: this.calculateSatisfactionLevel(stats.average),
      recommendationRate: this.calculateRecommendationRate(stats.distribution),
      commonPraisePoints: this.extractCommonPraise(reviews),
      commonConcerns: this.extractCommonConcerns(reviews),
      reviewQuality: this.assessReviewQuality(reviews),
      trendsOverTime: this.analyzeTrends(reviews)
    };
  }

  calculateSatisfactionLevel(average) {
    if (average >= 4.5) return 'very_high';
    if (average >= 4.0) return 'high';
    if (average >= 3.5) return 'moderate';
    if (average >= 3.0) return 'low';
    return 'very_low';
  }

  calculateRecommendationRate(distribution) {
    const total = Object.values(distribution).reduce((sum, count) => sum + count, 0);
    if (total === 0) return 0;
    
    const positiveReviews = (distribution[4] || 0) + (distribution[5] || 0);
    return Math.round((positiveReviews / total) * 100);
  }

  extractCommonPraise(reviews) {
    const positiveKeywords = [
      'excelente', 'bueno', 'recomendado', 'calidad',
      'rápido', 'envío', 'perfecto', 'satisfecho'
    ];
    
    return this.extractKeywordFrequency(reviews, positiveKeywords, 4);
  }

  extractCommonConcerns(reviews) {
    const negativeKeywords = [
      'problema', 'malo', 'defecto', 'tardó', 'lento',
      'roto', 'no funciona', 'decepcionado', 'demorado'
    ];
    
    return this.extractKeywordFrequency(reviews, negativeKeywords, 0, 3);
  }

  extractKeywordFrequency(reviews, keywords, minRating = 0, maxRating = 5) {
    const frequency = {};
    
    reviews
      .filter(review => review.rating >= minRating && review.rating <= maxRating)
      .forEach(review => {
        const text = `${review.title} ${review.content}`.toLowerCase();
        keywords.forEach(keyword => {
          if (text.includes(keyword)) {
            frequency[keyword] = (frequency[keyword] || 0) + 1;
          }
        });
      });
    
    return Object.entries(frequency)
      .sort(([,a], [,b]) => b - a)
      .slice(0, 3)
      .map(([keyword, count]) => ({ keyword, count }));
  }

  assessReviewQuality(reviews) {
    const detailedReviews = reviews.filter(r => 
      r.content && r.content.length > 50
    ).length;
    
    const reviewsWithImages = reviews.filter(r => 
      r.images && r.images.length > 0
    ).length;
    
    const totalReviews = reviews.length;
    
    return {
      detailedReviewsPercentage: totalReviews > 0 ? Math.round((detailedReviews / totalReviews) * 100) : 0,
      reviewsWithImagesPercentage: totalReviews > 0 ? Math.round((reviewsWithImages / totalReviews) * 100) : 0,
      averageContentLength: totalReviews > 0 ? 
        Math.round(reviews.reduce((sum, r) => sum + (r.content?.length || 0), 0) / totalReviews) : 0
    };
  }

  analyzeTrends(reviews) {
    const monthlyRatings = {};
    
    reviews.forEach(review => {
      const date = new Date(review.dateCreated);
      const monthKey = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
      
      if (!monthlyRatings[monthKey]) {
        monthlyRatings[monthKey] = { total: 0, sum: 0 };
      }
      
      monthlyRatings[monthKey].total++;
      monthlyRatings[monthKey].sum += review.rating;
    });
    
    return Object.entries(monthlyRatings)
      .map(([month, data]) => ({
        month,
        average: Math.round((data.sum / data.total) * 10) / 10,
        count: data.total
      }))
      .sort((a, b) => a.month.localeCompare(b.month));
  }

  generateReviewsSummary(stats) {
    return {
      overallRating: stats.average,
      totalReviews: stats.total,
      recommendationRate: this.calculateRecommendationRate(stats.distribution),
      satisfactionLevel: this.calculateSatisfactionLevel(stats.average),
      ratingDistribution: stats.distribution
    };
  }
}

export default GetProductReviewsUseCase;
