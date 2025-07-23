// Servicio para manejar imágenes del producto
class ImageService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getProductImages(productId) {
    try {
      const product = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return product.data.pictures || [];
    } catch (error) {
      throw new Error(`Failed to fetch product images: ${error.message}`);
    }
  }

  async getVariationImages(variationId) {
    try {
      // Las imágenes de variaciones vienen en el producto principal
      // Este método podría expandirse si hay endpoints específicos
      return [];
    } catch (error) {
      throw new Error(`Failed to fetch variation images: ${error.message}`);
    }
  }

  async getImageById(imageId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/pictures/${imageId}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch image ${imageId}: ${error.message}`);
    }
  }

  async optimizeImageUrl(originalUrl, size = 'medium') {
    // Optimizar URLs de imágenes de MercadoLibre según el tamaño necesario
    const sizeMap = {
      'small': '-I.', // 100x100
      'medium': '-O.', // 500x500
      'large': '-F.', // Original
      'thumbnail': '-V.' // 50x50
    };

    if (originalUrl && originalUrl.includes('mlstatic.com')) {
      const sizeCode = sizeMap[size] || sizeMap.medium;
      return originalUrl.replace(/-[A-Z]\./, sizeCode);
    }

    return originalUrl;
  }

  async preloadImages(imageUrls) {
    // Precargar imágenes para mejorar la experiencia del usuario
    const promises = imageUrls.map(url => {
      return new Promise((resolve, reject) => {
        const img = new Image();
        img.onload = () => resolve(url);
        img.onerror = () => reject(new Error(`Failed to preload ${url}`));
        img.src = url;
      });
    });

    try {
      return await Promise.allSettled(promises);
    } catch (error) {
      console.warn('Some images failed to preload:', error);
      return [];
    }
  }
}

export default ImageService;
