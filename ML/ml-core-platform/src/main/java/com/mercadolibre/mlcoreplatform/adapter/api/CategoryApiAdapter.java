package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryApiAdapter implements CategoryApiPort {

    @Override
    public List<CategoryResponse.AttributeResponse> getCategoryAttributes(String categoryId) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public List<CategoryResponse> getCategoriesBySite(String siteId) {
        // Mock implementation for testing
        return new ArrayList<>();
    }

    @Override
    public CategoryResponse predictCategory(String siteId, String title) {
        // Mock implementation for testing
        return CategoryResponse.builder()
                .id("mock-category")
                .name("Mock Category")
                .build();
    }
}
