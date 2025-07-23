// Adaptador para información del vendedor
class SellerAdapter {
  constructor(sellerService) {
    this.sellerService = sellerService;
  }

  async getSellerInfo(sellerId) {
    try {
      const rawSeller = await this.sellerService.getById(sellerId);
      return this.transformSellerInfo(rawSeller);
    } catch (error) {
      throw new Error(`Error fetching seller info: ${error.message}`);
    }
  }

  transformSellerInfo(rawSeller) {
    return {
      id: rawSeller.id,
      nickname: rawSeller.nickname,
      name: rawSeller.name || rawSeller.nickname,
      logo: rawSeller.logo,
      permalink: rawSeller.permalink,
      registrationDate: rawSeller.registration_date,
      countryId: rawSeller.country_id,
      address: {
        city: rawSeller.address?.city,
        state: rawSeller.address?.state
      },
      sellerReputation: this.transformSellerReputation(rawSeller.seller_reputation),
      eshop: rawSeller.eshop ? {
        nick_name: rawSeller.eshop.nick_name,
        eshop_rubro: rawSeller.eshop.eshop_rubro,
        eshop_id: rawSeller.eshop.eshop_id,
        eshop_logo_url: rawSeller.eshop.eshop_logo_url
      } : null,
      tags: rawSeller.tags || [],
      status: {
        site_status: rawSeller.status?.site_status,
        list: {
          allow: rawSeller.status?.list?.allow,
          codes: rawSeller.status?.list?.codes || []
        }
      }
    };
  }

  transformSellerReputation(reputation) {
    if (!reputation) return null;

    return {
      levelId: reputation.level_id,
      powerSellerStatus: reputation.power_seller_status,
      transactions: {
        period: reputation.transactions?.period,
        total: reputation.transactions?.total,
        completed: reputation.transactions?.completed,
        canceled: reputation.transactions?.canceled,
        ratings: {
          negative: reputation.transactions?.ratings?.negative,
          neutral: reputation.transactions?.ratings?.neutral,
          positive: reputation.transactions?.ratings?.positive
        }
      },
      metrics: {
        sales: {
          period: reputation.metrics?.sales?.period,
          completed: reputation.metrics?.sales?.completed
        },
        claims: {
          period: reputation.metrics?.claims?.period,
          rate: reputation.metrics?.claims?.rate,
          value: reputation.metrics?.claims?.value
        },
        delayedHandlingTime: {
          period: reputation.metrics?.delayed_handling_time?.period,
          rate: reputation.metrics?.delayed_handling_time?.rate,
          value: reputation.metrics?.delayed_handling_time?.value
        },
        cancellations: {
          period: reputation.metrics?.cancellations?.period,
          rate: reputation.metrics?.cancellations?.rate,
          value: reputation.metrics?.cancellations?.value
        }
      }
    };
  }

  async getSellerItems(sellerId, limit = 50, offset = 0) {
    try {
      const rawItems = await this.sellerService.getSellerItems(sellerId, limit, offset);
      return this.transformSellerItems(rawItems);
    } catch (error) {
      throw new Error(`Error fetching seller items: ${error.message}`);
    }
  }

  transformSellerItems(rawItems) {
    return {
      seller_id: rawItems.seller_id,
      query: rawItems.query,
      paging: rawItems.paging,
      results: rawItems.results?.map(item => ({
        id: item.id,
        title: item.title,
        price: item.price,
        currency_id: item.currency_id,
        available_quantity: item.available_quantity,
        sold_quantity: item.sold_quantity,
        condition: item.condition,
        thumbnail: item.thumbnail,
        permalink: item.permalink
      })) || []
    };
  }
}

export default SellerAdapter;
