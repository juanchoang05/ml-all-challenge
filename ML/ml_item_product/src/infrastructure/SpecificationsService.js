// Servicio para especificaciones y características de productos
class SpecificationsService {
  constructor(apiClient) {
    this.apiClient = apiClient;
    this.baseUrl = 'https://api.mercadolibre.com';
  }

  async getProductSpecs(productId) {
    try {
      const product = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return {
        attributes: product.data.attributes,
        technical_specifications: product.data.technical_specifications,
        highlights: product.data.highlights,
        warranty: product.data.warranty,
        dimensions: product.data.dimensions,
        weight: product.data.weight
      };
    } catch (error) {
      throw new Error(`Failed to fetch product specifications: ${error.message}`);
    }
  }

  async getProductDescription(productId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/items/${productId}/description`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch product description: ${error.message}`);
    }
  }

  async getCategoryAttributes(categoryId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/categories/${categoryId}/attributes`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch category attributes: ${error.message}`);
    }
  }

  async getAttributeValues(attributeId) {
    try {
      const response = await this.apiClient.get(`${this.baseUrl}/attributes/${attributeId}/values`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch attribute values: ${error.message}`);
    }
  }

  async getProductWarranty(productId) {
    try {
      const product = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      return product.data.warranty || null;
    } catch (error) {
      throw new Error(`Failed to fetch product warranty: ${error.message}`);
    }
  }

  async getProductDimensions(productId) {
    try {
      const product = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      
      // Buscar dimensiones en atributos
      const attributes = product.data.attributes || [];
      const dimensions = {};
      
      attributes.forEach(attr => {
        switch(attr.id) {
          case 'WIDTH':
            dimensions.width = attr.value_struct?.number || attr.value_name;
            dimensions.widthUnit = attr.value_struct?.unit;
            break;
          case 'HEIGHT':
            dimensions.height = attr.value_struct?.number || attr.value_name;
            dimensions.heightUnit = attr.value_struct?.unit;
            break;
          case 'DEPTH':
            dimensions.depth = attr.value_struct?.number || attr.value_name;
            dimensions.depthUnit = attr.value_struct?.unit;
            break;
          case 'WEIGHT':
            dimensions.weight = attr.value_struct?.number || attr.value_name;
            dimensions.weightUnit = attr.value_struct?.unit;
            break;
        }
      });
      
      return Object.keys(dimensions).length > 0 ? dimensions : null;
    } catch (error) {
      throw new Error(`Failed to fetch product dimensions: ${error.message}`);
    }
  }

  async getProductHighlights(productId) {
    try {
      const product = await this.apiClient.get(`${this.baseUrl}/items/${productId}`);
      
      // Extraer características destacadas de los atributos
      const attributes = product.data.attributes || [];
      const highlights = attributes
        .filter(attr => attr.attribute_group_name === 'Destacados' || attr.relevance > 0.8)
        .map(attr => ({
          title: attr.name,
          value: attr.value_name || attr.value_struct?.number + ' ' + attr.value_struct?.unit,
          id: attr.id
        }));
      
      return highlights;
    } catch (error) {
      throw new Error(`Failed to fetch product highlights: ${error.message}`);
    }
  }
}

export default SpecificationsService;
