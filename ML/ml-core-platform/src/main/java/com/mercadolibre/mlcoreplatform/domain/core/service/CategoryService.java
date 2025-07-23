package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.CategoryDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.CategoryUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {
    
    private final CategoryPersistencePort categoryPersistencePort;
    private final CategoryApiPort categoryApiPort;
    
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryDto> categories = categoryPersistencePort.findAll();
        return categories.stream()
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public CategoryResponse getCategoryById(String categoryId) {
        return categoryPersistencePort.findById(categoryId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
    }
    
    @Override
    public List<CategoryResponse.AttributeResponse> getCategoryAttributes(String categoryId) {
        return categoryApiPort.getCategoryAttributes(categoryId);
    }
    
    @Override
    public List<CategoryResponse> getCategoriesBySite(String siteId) {
        return categoryApiPort.getCategoriesBySite(siteId);
    }
    
    @Override
    public CategoryResponse predictCategory(String siteId, String title) {
        return categoryApiPort.predictCategory(siteId, title);
    }
    
    private CategoryResponse mapToResponse(CategoryDto categoryDto) {
        return CategoryResponse.builder()
                .id(categoryDto.id())
                .name(categoryDto.name())
                .picture(categoryDto.picture())
                .permalink(categoryDto.permalink())
                .totalItemsInThisCategory(categoryDto.totalItemsInThisCategory() != null ? 
                    categoryDto.totalItemsInThisCategory().intValue() : null)
                .pathFromRoot(mapPathFromRoot(categoryDto.pathFromRoot()))
                .childrenCategories(mapChildrenCategories(categoryDto.childrenCategories()))
                .settings(mapSettings(categoryDto.settings()))
                .build();
    }
    
    private List<CategoryResponse.PathFromRootResponse> mapPathFromRoot(List<CategoryDto.PathFromRootDto> pathFromRoot) {
        if (pathFromRoot == null) {
            return List.of();
        }
        return pathFromRoot.stream()
                .map(path -> CategoryResponse.PathFromRootResponse.builder()
                        .id(path.id())
                        .name(path.name())
                        .build())
                .toList();
    }
    
    private List<CategoryResponse> mapChildrenCategories(List<CategoryDto.ChildrenCategoryDto> childrenCategories) {
        if (childrenCategories == null) {
            return List.of();
        }
        return childrenCategories.stream()
                .map(child -> CategoryResponse.builder()
                        .id(child.id())
                        .name(child.name())
                        .totalItemsInThisCategory(child.totalItemsInThisCategory() != null ? 
                            child.totalItemsInThisCategory().intValue() : null)
                        .build())
                .toList();
    }
    
    private CategoryResponse.SettingsResponse mapSettings(CategoryDto.SettingsDto settingsDto) {
        if (settingsDto == null) {
            return null;
        }
        return CategoryResponse.SettingsResponse.builder()
                .adultContent(settingsDto.adultContent())
                .buyingAllowed(settingsDto.buyingAllowed())
                .buyingModes(settingsDto.buyingModes() != null ? 
                    String.join(",", settingsDto.buyingModes()) : null)
                .catalogDomain(settingsDto.catalogDomain())
                .coverage(settingsDto.coverageAreas())
                .currency(settingsDto.currency())
                .immediatePayment(settingsDto.immediatePayment() != null && 
                    settingsDto.immediatePayment().equals("required"))
                .build();
    }
}
