// Adaptador para la galería de imágenes del producto
class GalleryAdapter {
  constructor(imageService) {
    this.imageService = imageService;
  }

  async getProductImages(productId) {
    try {
      const rawImages = await this.imageService.getProductImages(productId);
      return this.transformImages(rawImages);
    } catch (error) {
      throw new Error(`Error fetching product images: ${error.message}`);
    }
  }

  transformImages(rawImages) {
    return rawImages.map((image, index) => ({
      id: image.id || `img_${index}`,
      url: image.secure_url || image.url,
      alt: image.alt_text || `Imagen del producto ${index + 1}`,
      thumbnail: this.generateThumbnailUrl(image.secure_url || image.url),
      isMain: index === 0
    }));
  }

  generateThumbnailUrl(originalUrl) {
    // Convierte la URL original a una versión thumbnail
    // Esto es específico para MercadoLibre que usa diferentes tamaños
    if (originalUrl.includes('mlstatic.com')) {
      return originalUrl.replace('-O.', '-I.');
    }
    return originalUrl;
  }

  async getVariationImages(variationId) {
    try {
      const rawImages = await this.imageService.getVariationImages(variationId);
      return this.transformImages(rawImages);
    } catch (error) {
      throw new Error(`Error fetching variation images: ${error.message}`);
    }
  }
}

export default GalleryAdapter;
