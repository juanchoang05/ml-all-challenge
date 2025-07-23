// Adaptador para navegación y breadcrumbs
class NavigationAdapter {
  constructor(categoryService) {
    this.categoryService = categoryService;
  }

  async getCategoryPath(categoryId) {
    try {
      const rawPath = await this.categoryService.getCategoryPath(categoryId);
      return this.transformCategoryPath(rawPath);
    } catch (error) {
      throw new Error(`Error fetching category path: ${error.message}`);
    }
  }

  transformCategoryPath(rawPath) {
    return rawPath.map(category => ({
      id: category.id,
      name: category.name,
      permalink: category.permalink || null,
      url: `/categoria/${category.id}`,
      level: category.level || 0
    }));
  }

  async getProductBreadcrumbs(productId) {
    try {
      const product = await this.categoryService.getProductWithCategory(productId);
      const categoryPath = await this.getCategoryPath(product.category_id);
      
      return [
        { name: 'Inicio', url: '/' },
        ...categoryPath,
        { name: product.title, url: `/producto/${productId}`, isCurrentPage: true }
      ];
    } catch (error) {
      throw new Error(`Error fetching product breadcrumbs: ${error.message}`);
    }
  }

  async getCategoryAttributes(categoryId) {
    try {
      const rawAttributes = await this.categoryService.getCategoryAttributes(categoryId);
      return this.transformCategoryAttributes(rawAttributes);
    } catch (error) {
      throw new Error(`Error fetching category attributes: ${error.message}`);
    }
  }

  transformCategoryAttributes(rawAttributes) {
    return rawAttributes.map(attr => ({
      id: attr.id,
      name: attr.name,
      type: attr.type,
      hierarchy: attr.hierarchy,
      relevance: attr.relevance,
      tags: attr.tags || [],
      values: attr.values?.map(value => ({
        id: value.id,
        name: value.name,
        results: value.results
      })) || []
    }));
  }
}

export default NavigationAdapter;
