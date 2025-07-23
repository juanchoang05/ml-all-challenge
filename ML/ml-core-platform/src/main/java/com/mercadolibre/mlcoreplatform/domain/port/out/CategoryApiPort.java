package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryApiPort {
    
    List<CategoryResponse> getCategoriesBySite(String siteId);
    
    CategoryResponse predictCategory(String siteId, String title);
    
    List<CategoryResponse.AttributeResponse> getCategoryAttributes(String categoryId);
}
