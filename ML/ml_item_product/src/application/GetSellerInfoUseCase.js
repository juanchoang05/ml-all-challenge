// Caso de uso para obtener información del vendedor
class GetSellerInfoUseCase {
  constructor(sellerAdapter) {
    this.sellerAdapter = sellerAdapter;
  }

  async execute(sellerId) {
    try {
      const sellerInfo = await this.sellerAdapter.getSellerInfo(sellerId);
      const sellerItems = await this.sellerAdapter.getSellerItems(sellerId, 10, 0);
      
      return {
        ...sellerInfo,
        recentItems: sellerItems.results,
        totalItems: sellerItems.paging?.total || 0,
        isOfficialStore: this.isOfficialStore(sellerInfo),
        trustLevel: this.calculateTrustLevel(sellerInfo.sellerReputation),
        responseQuality: this.calculateResponseQuality(sellerInfo.sellerReputation)
      };
    } catch (error) {
      throw new Error(`Failed to get seller info: ${error.message}`);
    }
  }

  isOfficialStore(sellerInfo) {
    return sellerInfo.tags?.includes('eshop') || 
           sellerInfo.tags?.includes('official_store') ||
           sellerInfo.eshop !== null;
  }

  calculateTrustLevel(reputation) {
    if (!reputation) return 'unknown';
    
    const transactions = reputation.transactions;
    if (!transactions || transactions.total < 10) return 'new';
    
    const positiveRate = transactions.ratings.positive / transactions.total;
    
    if (positiveRate >= 0.98) return 'excellent';
    if (positiveRate >= 0.95) return 'very_good';
    if (positiveRate >= 0.90) return 'good';
    if (positiveRate >= 0.80) return 'average';
    return 'poor';
  }

  calculateResponseQuality(reputation) {
    if (!reputation?.metrics) return null;
    
    const claims = reputation.metrics.claims;
    const cancellations = reputation.metrics.cancellations;
    const delayedHandling = reputation.metrics.delayedHandlingTime;
    
    const scores = [];
    
    if (claims && claims.rate !== undefined) {
      scores.push(Math.max(0, 100 - claims.rate * 100));
    }
    
    if (cancellations && cancellations.rate !== undefined) {
      scores.push(Math.max(0, 100 - cancellations.rate * 100));
    }
    
    if (delayedHandling && delayedHandling.rate !== undefined) {
      scores.push(Math.max(0, 100 - delayedHandling.rate * 100));
    }
    
    if (scores.length === 0) return null;
    
    const averageScore = scores.reduce((sum, score) => sum + score, 0) / scores.length;
    
    if (averageScore >= 95) return 'excellent';
    if (averageScore >= 85) return 'very_good';
    if (averageScore >= 75) return 'good';
    if (averageScore >= 60) return 'average';
    return 'poor';
  }
}

export default GetSellerInfoUseCase;
