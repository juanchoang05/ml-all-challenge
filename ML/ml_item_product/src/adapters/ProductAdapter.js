// Adaptador para información básica del producto
class ProductAdapter {
  constructor(productService) {
    this.productService = productService;
  }

  async getProductInfo(productId) {
    try {
      const rawProduct = await this.productService.getById(productId);
      return this.transformProductInfo(rawProduct);
    } catch (error) {
      throw new Error(`Error fetching product info: ${error.message}`);
    }
  }

  transformProductInfo(rawProduct) {
    return {
      id: rawProduct.id,
      title: rawProduct.title,
      subtitle: rawProduct.subtitle || `${rawProduct.condition} | +${rawProduct.soldQuantity || 0} vendidos`,
      price: {
        amount: rawProduct.price,
        currency: rawProduct.currency_id || 'COP',
        installments: rawProduct.installments ? {
          quantity: rawProduct.installments.quantity,
          amount: rawProduct.installments.amount,
          rate: rawProduct.installments.rate
        } : null
      },
      condition: rawProduct.condition,
      availableQuantity: rawProduct.available_quantity,
      soldQuantity: rawProduct.sold_quantity,
      rating: {
        average: rawProduct.reviews?.rating_average || 0,
        total: rawProduct.reviews?.total || 0
      },
      attributes: rawProduct.attributes?.map(attr => ({
        id: attr.id,
        name: attr.name,
        value: attr.value_name || attr.value_struct?.number || attr.value_struct?.unit
      })) || [],
      variations: rawProduct.variations?.map(variation => ({
        id: variation.id,
        attributeCombinations: variation.attribute_combinations,
        price: variation.price,
        availableQuantity: variation.available_quantity,
        picture: variation.picture_ids?.[0]
      })) || []
    };
  }

  async getProductVariations(productId) {
    try {
      const product = await this.productService.getById(productId);
      return product.variations || [];
    } catch (error) {
      throw new Error(`Error fetching product variations: ${error.message}`);
    }
  }
}

export default ProductAdapter;
