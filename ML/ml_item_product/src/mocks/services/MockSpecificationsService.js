// Mock Service para SpecificationsService
import { 
  getProductById, 
  simulateNetworkDelay, 
  simulateRandomError, 
  mockLogger 
} from '../data/index.js';

export class MockSpecificationsService {
  constructor(config = {}) {
    this.config = config;
    this.serviceName = 'SpecificationsService';
  }

  async getProductSpecifications(productId) {
    mockLogger.log(this.serviceName, 'getProductSpecifications', { productId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 500);
      simulateRandomError(0.02);
      
      const product = getProductById(productId);
      
      if (!product) {
        throw new Error(`Product with id ${productId} not found`);
      }
      
      // Generar especificaciones mock basadas en el producto
      const specifications = this.generateMockSpecifications(product);
      
      return {
        success: true,
        data: specifications
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getProductSpecifications', error);
      throw error;
    }
  }

  async getSpecificationsByCategory(categoryId) {
    mockLogger.log(this.serviceName, 'getSpecificationsByCategory', { categoryId });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 600);
      simulateRandomError(0.03);
      
      const categorySpecs = this.generateCategorySpecifications(categoryId);
      
      return {
        success: true,
        data: categorySpecs
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'getSpecificationsByCategory', error);
      throw error;
    }
  }

  async compareProducts(productIds) {
    mockLogger.log(this.serviceName, 'compareProducts', { productIds });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 800);
      simulateRandomError(0.04);
      
      const comparison = {
        products: [],
        common_attributes: [],
        differences: []
      };
      
      for (const productId of productIds) {
        const product = getProductById(productId);
        if (product) {
          const specs = this.generateMockSpecifications(product);
          comparison.products.push({
            id: productId,
            title: product.title,
            specifications: specs
          });
        }
      }
      
      // Mock de atributos comunes y diferencias
      comparison.common_attributes = [
        { name: 'Marca', attribute_id: 'BRAND' },
        { name: 'Condición', attribute_id: 'CONDITION' }
      ];
      
      comparison.differences = [
        { name: 'Modelo', attribute_id: 'MODEL' },
        { name: 'Capacidad de almacenamiento', attribute_id: 'STORAGE_CAPACITY' },
        { name: 'Precio', attribute_id: 'PRICE' }
      ];
      
      return {
        success: true,
        data: comparison
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'compareProducts', error);
      throw error;
    }
  }

  async validateSpecifications(productId, specifications) {
    mockLogger.log(this.serviceName, 'validateSpecifications', { productId, specifications });
    
    try {
      await simulateNetworkDelay(this.config.mockDelay || 400);
      simulateRandomError(0.02);
      
      const validation = {
        product_id: productId,
        is_valid: true,
        errors: [],
        warnings: [],
        suggestions: []
      };
      
      // Mock de validación
      for (const spec of specifications) {
        if (!spec.value || spec.value.trim() === '') {
          validation.errors.push({
            attribute_id: spec.attribute_id,
            message: `${spec.name} es requerido`
          });
          validation.is_valid = false;
        }
        
        if (spec.attribute_id === 'STORAGE_CAPACITY' && !spec.value.includes('GB')) {
          validation.warnings.push({
            attribute_id: spec.attribute_id,
            message: 'La capacidad de almacenamiento debería especificar la unidad (GB/TB)'
          });
        }
      }
      
      return {
        success: true,
        data: validation
      };
    } catch (error) {
      mockLogger.error(this.serviceName, 'validateSpecifications', error);
      throw error;
    }
  }

  generateMockSpecifications(product) {
    const baseSpecs = {
      general: [
        {
          group: 'Información General',
          attributes: [
            { name: 'Marca', value: this.getAttributeValue(product, 'BRAND') || 'N/A' },
            { name: 'Modelo', value: this.getAttributeValue(product, 'MODEL') || 'N/A' },
            { name: 'Condición', value: product.condition === 'new' ? 'Nuevo' : 'Usado' },
            { name: 'Garantía', value: product.warranty || 'Sin garantía especificada' }
          ]
        }
      ]
    };
    
    // Especificaciones específicas según la categoría
    if (product.category_id?.includes('1055')) { // Celulares
      baseSpecs.technical = [
        {
          group: 'Especificaciones Técnicas',
          attributes: [
            { name: 'Capacidad de almacenamiento', value: this.getAttributeValue(product, 'STORAGE_CAPACITY') || '256 GB' },
            { name: 'Sistema operativo', value: 'iOS 17' },
            { name: 'Tamaño de pantalla', value: '6.7 pulgadas' },
            { name: 'Resolución de pantalla', value: '2796 x 1290 píxeles' },
            { name: 'Cámara principal', value: '48 MP' },
            { name: 'Cámara frontal', value: '12 MP' },
            { name: 'Batería', value: '4422 mAh' },
            { name: 'Procesador', value: 'A16 Bionic' },
            { name: 'RAM', value: '6 GB' }
          ]
        }
      ];
      
      baseSpecs.connectivity = [
        {
          group: 'Conectividad',
          attributes: [
            { name: 'Red móvil', value: '5G, 4G LTE' },
            { name: 'WiFi', value: '802.11ax (Wi-Fi 6)' },
            { name: 'Bluetooth', value: '5.3' },
            { name: 'NFC', value: 'Sí' },
            { name: 'Conector', value: 'Lightning' }
          ]
        }
      ];
    } else if (product.category_id?.includes('1652')) { // Computación
      baseSpecs.technical = [
        {
          group: 'Especificaciones Técnicas',
          attributes: [
            { name: 'Procesador', value: 'Intel Core i5-1135G7' },
            { name: 'Memoria RAM', value: this.getAttributeValue(product, 'RAM_MEMORY') || '8 GB' },
            { name: 'Almacenamiento', value: '256 GB SSD' },
            { name: 'Tarjeta gráfica', value: 'Intel Iris Xe' },
            { name: 'Tamaño de pantalla', value: '15.6 pulgadas' },
            { name: 'Resolución', value: '1920 x 1080 Full HD' },
            { name: 'Sistema operativo', value: 'Windows 11 Home' },
            { name: 'Batería', value: 'Hasta 8 horas' }
          ]
        }
      ];
      
      baseSpecs.connectivity = [
        {
          group: 'Conectividad',
          attributes: [
            { name: 'WiFi', value: '802.11ac' },
            { name: 'Bluetooth', value: '5.0' },
            { name: 'USB', value: '2x USB 3.0, 1x USB-C' },
            { name: 'HDMI', value: 'Sí' },
            { name: 'Ethernet', value: 'Gigabit Ethernet' }
          ]
        }
      ];
    }
    
    baseSpecs.physical = [
      {
        group: 'Características Físicas',
        attributes: [
          { name: 'Peso', value: this.generateRandomWeight(product.category_id) },
          { name: 'Dimensiones', value: this.generateRandomDimensions(product.category_id) },
          { name: 'Color', value: this.extractColorFromTitle(product.title) },
          { name: 'Material', value: this.generateRandomMaterial(product.category_id) }
        ]
      }
    ];
    
    return baseSpecs;
  }

  generateCategorySpecifications(categoryId) {
    // Mock de especificaciones por categoría
    const categorySpecs = {
      'MCO1055': { // Celulares
        required: ['BRAND', 'MODEL', 'STORAGE_CAPACITY'],
        optional: ['COLOR', 'OPERATING_SYSTEM', 'SCREEN_SIZE'],
        groups: ['Información General', 'Especificaciones Técnicas', 'Conectividad', 'Características Físicas']
      },
      'MLA1652': { // Computación
        required: ['BRAND', 'MODEL', 'RAM_MEMORY'],
        optional: ['STORAGE_TYPE', 'SCREEN_SIZE', 'PROCESSOR'],
        groups: ['Información General', 'Especificaciones Técnicas', 'Conectividad', 'Características Físicas']
      }
    };
    
    return categorySpecs[categoryId] || {
      required: ['BRAND', 'MODEL'],
      optional: ['COLOR', 'CONDITION'],
      groups: ['Información General', 'Características Físicas']
    };
  }

  getAttributeValue(product, attributeId) {
    const attribute = product.attributes?.find(attr => attr.id === attributeId);
    return attribute?.value_name || attribute?.values?.[0]?.name;
  }

  generateRandomWeight(categoryId) {
    if (categoryId?.includes('1055')) return `${Math.floor(Math.random() * 50) + 150}g`;
    if (categoryId?.includes('1652')) return `${Math.floor(Math.random() * 1000) + 1500}g`;
    return `${Math.floor(Math.random() * 500) + 100}g`;
  }

  generateRandomDimensions(categoryId) {
    if (categoryId?.includes('1055')) return '160.7 x 77.6 x 7.85 mm';
    if (categoryId?.includes('1652')) return '358 x 236 x 19.9 mm';
    return '100 x 50 x 10 mm';
  }

  extractColorFromTitle(title) {
    const colors = ['Negro', 'Blanco', 'Azul', 'Rojo', 'Verde', 'Rosa', 'Morado', 'Dorado', 'Plateado'];
    const foundColor = colors.find(color => title.toLowerCase().includes(color.toLowerCase()));
    return foundColor || 'Color no especificado';
  }

  generateRandomMaterial(categoryId) {
    if (categoryId?.includes('1055')) return 'Aluminio y vidrio';
    if (categoryId?.includes('1652')) return 'Plástico ABS';
    return 'Material mixto';
  }
}
