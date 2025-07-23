// Caso de uso para obtener información completa del producto
class GetProductInfoUseCase {
  constructor(productAdapter, galleryAdapter, specificationsAdapter) {
    this.productAdapter = productAdapter;
    this.galleryAdapter = galleryAdapter;
    this.specificationsAdapter = specificationsAdapter;
  }

  async execute(productId) {
    try {
      // Obtener información básica del producto
      const productInfo = await this.productAdapter.getProductInfo(productId);
      
      // Obtener imágenes del producto
      const images = await this.galleryAdapter.getProductImages(productId);
      
      // Obtener especificaciones básicas
      const specifications = await this.specificationsAdapter.getProductSpecifications(productId);
      
      return {
        ...productInfo,
        images,
        specifications: specifications.generalSpecs,
        highlights: specifications.highlights,
        warranty: specifications.warranty
      };
    } catch (error) {
      throw new Error(`Failed to get product info: ${error.message}`);
    }
  }

  async executeWithVariation(productId, variationId) {
    try {
      const productInfo = await this.execute(productId);
      
      // Si hay variación seleccionada, obtener información específica
      if (variationId) {
        const variation = productInfo.variations.find(v => v.id === variationId);
        if (variation) {
          const variationImages = await this.galleryAdapter.getVariationImages(variationId);
          return {
            ...productInfo,
            selectedVariation: variation,
            images: variationImages.length > 0 ? variationImages : productInfo.images,
            price: variation.price ? { ...productInfo.price, amount: variation.price } : productInfo.price,
            availableQuantity: variation.availableQuantity || productInfo.availableQuantity
          };
        }
      }
      
      return productInfo;
    } catch (error) {
      throw new Error(`Failed to get product info with variation: ${error.message}`);
    }
  }
}

export default GetProductInfoUseCase;
