// Adaptador para características y especificaciones del producto
class SpecificationsAdapter {
  constructor(specificationsService) {
    this.specificationsService = specificationsService;
  }

  async getProductSpecifications(productId) {
    try {
      const rawSpecs = await this.specificationsService.getProductSpecs(productId);
      return this.transformSpecifications(rawSpecs);
    } catch (error) {
      throw new Error(`Error fetching product specifications: ${error.message}`);
    }
  }

  transformSpecifications(rawSpecs) {
    return {
      generalSpecs: this.transformGeneralSpecs(rawSpecs.attributes || []),
      technicalSpecs: this.transformTechnicalSpecs(rawSpecs.technical_specifications || []),
      highlights: this.transformHighlights(rawSpecs.highlights || []),
      warranty: this.transformWarranty(rawSpecs.warranty),
      dimensions: this.transformDimensions(rawSpecs.dimensions),
      weight: rawSpecs.weight
    };
  }

  transformGeneralSpecs(attributes) {
    const grouped = {};
    
    attributes.forEach(attr => {
      const groupName = attr.attribute_group_name || 'General';
      if (!grouped[groupName]) {
        grouped[groupName] = [];
      }
      
      grouped[groupName].push({
        id: attr.id,
        name: attr.name,
        value: this.formatAttributeValue(attr),
        unit: attr.value_struct?.unit || null
      });
    });

    return grouped;
  }

  formatAttributeValue(attr) {
    if (attr.value_name) return attr.value_name;
    if (attr.value_struct) {
      if (attr.value_struct.number !== undefined) {
        return `${attr.value_struct.number}${attr.value_struct.unit || ''}`;
      }
      return attr.value_struct.value || attr.value_struct;
    }
    return attr.value || 'No especificado';
  }

  transformTechnicalSpecs(techSpecs) {
    return techSpecs.map(spec => ({
      category: spec.category,
      specifications: spec.specifications?.map(s => ({
        name: s.name,
        value: s.value,
        unit: s.unit
      })) || []
    }));
  }

  transformHighlights(highlights) {
    return highlights.map(highlight => ({
      title: highlight.title,
      description: highlight.description,
      icon: highlight.icon
    }));
  }

  transformWarranty(warranty) {
    if (!warranty) return null;
    
    return {
      type: warranty.type,
      time: warranty.time,
      timeUnit: warranty.time_unit,
      description: warranty.description
    };
  }

  transformDimensions(dimensions) {
    if (!dimensions) return null;
    
    return {
      width: dimensions.width,
      height: dimensions.height,
      depth: dimensions.depth,
      unit: dimensions.unit || 'cm'
    };
  }

  async getProductDescription(productId) {
    try {
      const rawDescription = await this.specificationsService.getProductDescription(productId);
      return this.transformDescription(rawDescription);
    } catch (error) {
      throw new Error(`Error fetching product description: ${error.message}`);
    }
  }

  transformDescription(rawDescription) {
    return {
      plainText: rawDescription.plain_text,
      html: rawDescription.text,
      lastUpdated: rawDescription.last_updated,
      snapshot: rawDescription.snapshot ? {
        url: rawDescription.snapshot.url,
        width: rawDescription.snapshot.width,
        height: rawDescription.snapshot.height,
        status: rawDescription.snapshot.status
      } : null
    };
  }
}

export default SpecificationsAdapter;
