// Caso de uso para obtener breadcrumbs y navegación
class GetNavigationUseCase {
  constructor(navigationAdapter) {
    this.navigationAdapter = navigationAdapter;
  }

  async execute(productId) {
    try {
      const breadcrumbs = await this.navigationAdapter.getProductBreadcrumbs(productId);
      
      return {
        breadcrumbs,
        currentPage: breadcrumbs.find(item => item.isCurrentPage),
        categoryPath: breadcrumbs.filter(item => !item.isCurrentPage && item.url !== '/'),
        structured: this.generateStructuredData(breadcrumbs)
      };
    } catch (error) {
      throw new Error(`Failed to get navigation: ${error.message}`);
    }
  }

  generateStructuredData(breadcrumbs) {
    // Generar datos estructurados para SEO (JSON-LD)
    return {
      "@context": "https://schema.org",
      "@type": "BreadcrumbList",
      "itemListElement": breadcrumbs.map((item, index) => ({
        "@type": "ListItem",
        "position": index + 1,
        "name": item.name,
        "item": item.url ? `${window.location.origin}${item.url}` : undefined
      }))
    };
  }

  async getCategoryFilters(categoryId) {
    try {
      const attributes = await this.navigationAdapter.getCategoryAttributes(categoryId);
      
      return {
        attributes,
        filterGroups: this.groupFiltersByType(attributes),
        availableFilters: this.extractAvailableFilters(attributes)
      };
    } catch (error) {
      throw new Error(`Failed to get category filters: ${error.message}`);
    }
  }

  groupFiltersByType(attributes) {
    const groups = {
      primary: [],    // Filtros principales (color, marca, etc.)
      technical: [],  // Especificaciones técnicas
      price: [],      // Filtros de precio
      other: []       // Otros filtros
    };
    
    const primaryFilters = ['COLOR', 'BRAND', 'MODEL', 'SIZE'];
    const technicalFilters = ['PROCESSOR', 'MEMORY', 'STORAGE', 'SCREEN_SIZE'];
    
    attributes.forEach(attr => {
      if (primaryFilters.includes(attr.id)) {
        groups.primary.push(attr);
      } else if (technicalFilters.includes(attr.id)) {
        groups.technical.push(attr);
      } else if (attr.id === 'PRICE') {
        groups.price.push(attr);
      } else {
        groups.other.push(attr);
      }
    });
    
    return groups;
  }

  extractAvailableFilters(attributes) {
    return attributes.reduce((filters, attr) => {
      if (attr.values && attr.values.length > 0) {
        filters[attr.id] = {
          name: attr.name,
          type: attr.type,
          options: attr.values.map(value => ({
            id: value.id,
            name: value.name,
            count: value.results || 0
          }))
        };
      }
      return filters;
    }, {});
  }
}

export default GetNavigationUseCase;
