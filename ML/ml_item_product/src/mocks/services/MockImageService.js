// Mock Service para ImageService
import { 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockImageService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'ImageService';
  }

  async optimizeImage(imageUrl, options = {}) {
    mockLogger.log(this.serviceName, 'optimizeImage', { imageUrl, options });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const { width = 800, height = 600, quality = 85, format = 'webp' } = options;
      
      // Mock de optimización de imagen
      const optimizedImage = {
        original_url: imageUrl,
        optimized_url: `${imageUrl}?w=${width}&h=${height}&q=${quality}&f=${format}`,
        width,
        height,
        format,
        quality,
        size_reduction: Math.floor(Math.random() * 40) + 20, // 20-60% de reducción
        processing_time: Math.floor(Math.random() * 500) + 100 // 100-600ms
      };
      
      return {
        success: true,
        data: optimizedImage
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'optimizeImage', error);
      throw error;
    }
  }

  async resizeImage(imageUrl, width, height) {
    mockLogger.log(this.serviceName, 'resizeImage', { imageUrl, width, height });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 300);
      simulateRandomError(0.02);
      
      const resizedImage = {
        original_url: imageUrl,
        resized_url: `${imageUrl}?w=${width}&h=${height}`,
        original_dimensions: { width: 1200, height: 1200 },
        new_dimensions: { width, height },
        aspect_ratio_maintained: width === height
      };
      
      return {
        success: true,
        data: resizedImage
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'resizeImage', error);
      throw error;
    }
  }

  async generateThumbnail(imageUrl, size = 150) {
    mockLogger.log(this.serviceName, 'generateThumbnail', { imageUrl, size });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 250);
      simulateRandomError(0.01);
      
      const thumbnail = {
        original_url: imageUrl,
        thumbnail_url: `${imageUrl}?w=${size}&h=${size}&q=75&f=webp`,
        size,
        format: 'webp',
        generated_at: new Date().toISOString()
      };
      
      return {
        success: true,
        data: thumbnail
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'generateThumbnail', error);
      throw error;
    }
  }

  async getImageMetadata(imageUrl) {
    mockLogger.log(this.serviceName, 'getImageMetadata', { imageUrl });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 350);
      simulateRandomError(0.02);
      
      const metadata = {
        url: imageUrl,
        width: 1200,
        height: 1200,
        format: 'JPEG',
        file_size: Math.floor(Math.random() * 500000) + 100000, // 100KB - 600KB
        color_depth: 24,
        has_transparency: false,
        created_at: '2024-01-20T10:30:00.000Z',
        exif_data: {
          camera: 'iPhone 14 Pro',
          iso: 100,
          aperture: 'f/2.8',
          shutter_speed: '1/60'
        }
      };
      
      return {
        success: true,
        data: metadata
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getImageMetadata', error);
      throw error;
    }
  }

  async convertFormat(imageUrl, targetFormat) {
    mockLogger.log(this.serviceName, 'convertFormat', { imageUrl, targetFormat });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.03);
      
      const convertedImage = {
        original_url: imageUrl,
        converted_url: `${imageUrl}?f=${targetFormat}`,
        original_format: 'JPEG',
        target_format: targetFormat.toUpperCase(),
        size_difference: targetFormat === 'webp' ? '-35%' : '+10%',
        conversion_time: Math.floor(Math.random() * 300) + 200
      };
      
      return {
        success: true,
        data: convertedImage
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'convertFormat', error);
      throw error;
    }
  }

  async generateImageVariants(imageUrl) {
    mockLogger.log(this.serviceName, 'generateImageVariants', { imageUrl });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.03);
      
      const variants = {
        original: imageUrl,
        thumbnail: `${imageUrl}?w=150&h=150&q=75`,
        small: `${imageUrl}?w=300&h=300&q=80`,
        medium: `${imageUrl}?w=600&h=600&q=85`,
        large: `${imageUrl}?w=1200&h=1200&q=90`,
        webp_small: `${imageUrl}?w=300&h=300&q=80&f=webp`,
        webp_medium: `${imageUrl}?w=600&h=600&q=85&f=webp`,
        webp_large: `${imageUrl}?w=1200&h=1200&q=90&f=webp`
      };
      
      return {
        success: true,
        data: variants
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'generateImageVariants', error);
      throw error;
    }
  }
}
