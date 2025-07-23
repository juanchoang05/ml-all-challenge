package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryUseCase {
    
    List<CategoryResponse> getAllCategories();
    
    CategoryResponse getCategoryById(String categoryId);
    
    List<CategoryResponse.AttributeResponse> getCategoryAttributes(String categoryId);
    
    List<CategoryResponse> getCategoriesBySite(String siteId);
    
    CategoryResponse predictCategory(String siteId, String title);
}
