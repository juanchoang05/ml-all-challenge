package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.CategoryDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.CategoryPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryPersistencePort categoryPersistencePort;
    
    @Mock
    private CategoryApiPort categoryApiPort;
    
    private CategoryService categoryService;
    
    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryPersistencePort, categoryApiPort);
    }
    
    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Given
        List<CategoryDto> mockCategories = Arrays.asList(
            createMockCategoryDto("MLA1055", "Celulares y Smartphones"),
            createMockCategoryDto("MLA1648", "Computación")
        );
        when(categoryPersistencePort.findAll()).thenReturn(mockCategories);
        
        // When
        List<CategoryResponse> result = categoryService.getAllCategories();
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA1055", result.get(0).id());
        assertEquals("Celulares y Smartphones", result.get(0).name());
        assertEquals("MLA1648", result.get(1).id());
        assertEquals("Computación", result.get(1).name());
        verify(categoryPersistencePort).findAll();
    }
    
    @Test
    void getAllCategories_WhenNoCategories_ShouldReturnEmptyList() {
        // Given
        when(categoryPersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<CategoryResponse> result = categoryService.getAllCategories();
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryPersistencePort).findAll();
    }
    
    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        // Given
        String categoryId = "MLA1055";
        CategoryDto mockCategory = createMockCategoryDto(categoryId, "Celulares y Smartphones");
        when(categoryPersistencePort.findById(categoryId)).thenReturn(Optional.of(mockCategory));
        
        // When
        CategoryResponse result = categoryService.getCategoryById(categoryId);
        
        // Then
        assertNotNull(result);
        assertEquals(categoryId, result.id());
        assertEquals("Celulares y Smartphones", result.name());
        verify(categoryPersistencePort).findById(categoryId);
    }
    
    @Test
    void getCategoryById_WhenCategoryNotFound_ShouldThrowException() {
        // Given
        String categoryId = "NON_EXISTENT";
        when(categoryPersistencePort.findById(categoryId)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> categoryService.getCategoryById(categoryId));
        assertEquals("Category not found with id: NON_EXISTENT", exception.getMessage());
        verify(categoryPersistencePort).findById(categoryId);
    }
    
    @Test
    void getCategoryAttributes_ShouldReturnAttributesList() {
        // Given
        String categoryId = "MLA1055";
        List<CategoryResponse.AttributeResponse> mockAttributes = Arrays.asList(
            CategoryResponse.AttributeResponse.builder()
                .id("COLOR")
                .name("Color")
                .build(),
            CategoryResponse.AttributeResponse.builder()
                .id("BRAND")
                .name("Marca")
                .build()
        );
        when(categoryApiPort.getCategoryAttributes(categoryId)).thenReturn(mockAttributes);
        
        // When
        List<CategoryResponse.AttributeResponse> result = categoryService.getCategoryAttributes(categoryId);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("COLOR", result.get(0).id());
        assertEquals("Color", result.get(0).name());
        verify(categoryApiPort).getCategoryAttributes(categoryId);
    }
    
    @Test
    void getCategoryAttributes_WhenNoAttributes_ShouldReturnEmptyList() {
        // Given
        String categoryId = "MLA1055";
        when(categoryApiPort.getCategoryAttributes(categoryId)).thenReturn(Collections.emptyList());
        
        // When
        List<CategoryResponse.AttributeResponse> result = categoryService.getCategoryAttributes(categoryId);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryApiPort).getCategoryAttributes(categoryId);
    }
    
    @Test
    void getCategoriesBySite_ShouldReturnCategoriesForSite() {
        // Given
        String siteId = "MLA";
        List<CategoryResponse> mockCategories = Arrays.asList(
            CategoryResponse.builder()
                .id("MLA1055")
                .name("Celulares y Smartphones")
                .build()
        );
        when(categoryApiPort.getCategoriesBySite(siteId)).thenReturn(mockCategories);
        
        // When
        List<CategoryResponse> result = categoryService.getCategoriesBySite(siteId);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MLA1055", result.get(0).id());
        verify(categoryApiPort).getCategoriesBySite(siteId);
    }
    
    @Test
    void predictCategory_ShouldReturnPredictedCategory() {
        // Given
        String siteId = "MLA";
        String title = "iPhone 15 Pro Max";
        CategoryResponse mockPrediction = CategoryResponse.builder()
            .id("MLA1055")
            .name("Celulares y Smartphones")
            .build();
        when(categoryApiPort.predictCategory(siteId, title)).thenReturn(mockPrediction);
        
        // When
        CategoryResponse result = categoryService.predictCategory(siteId, title);
        
        // Then
        assertNotNull(result);
        assertEquals("MLA1055", result.id());
        assertEquals("Celulares y Smartphones", result.name());
        verify(categoryApiPort).predictCategory(siteId, title);
    }
    
    @Test
    void mapToResponse_WithCompleteData_ShouldMapCorrectly() {
        // Given
        CategoryDto categoryDto = createComplexMockCategoryDto();
        when(categoryPersistencePort.findById("MLA1055")).thenReturn(Optional.of(categoryDto));
        
        // When
        CategoryResponse result = categoryService.getCategoryById("MLA1055");
        
        // Then
        assertNotNull(result);
        assertEquals("MLA1055", result.id());
        assertEquals("Celulares y Smartphones", result.name());
        assertEquals("https://example.com/picture.jpg", result.picture());
        assertEquals("https://example.com/permalink", result.permalink());
        assertEquals(1000, result.totalItemsInThisCategory());
        assertNotNull(result.pathFromRoot());
        assertEquals(1, result.pathFromRoot().size());
        assertNotNull(result.childrenCategories());
        assertEquals(1, result.childrenCategories().size());
        assertNotNull(result.settings());
    }
    
    @Test
    void mapToResponse_WithNullOptionalFields_ShouldHandleNulls() {
        // Given
        CategoryDto categoryDto = CategoryDto.builder()
            .id("MLA1055")
            .name("Celulares y Smartphones")
            .picture(null)
            .permalink(null)
            .totalItemsInThisCategory(null)
            .pathFromRoot(null)
            .childrenCategories(null)
            .settings(null)
            .build();
        when(categoryPersistencePort.findById("MLA1055")).thenReturn(Optional.of(categoryDto));
        
        // When
        CategoryResponse result = categoryService.getCategoryById("MLA1055");
        
        // Then
        assertNotNull(result);
        assertEquals("MLA1055", result.id());
        assertEquals("Celulares y Smartphones", result.name());
        assertNull(result.picture());
        assertNull(result.permalink());
        assertNull(result.totalItemsInThisCategory());
        assertNotNull(result.pathFromRoot());
        assertTrue(result.pathFromRoot().isEmpty());
        assertNotNull(result.childrenCategories());
        assertTrue(result.childrenCategories().isEmpty());
        assertNull(result.settings());
    }
    
    private CategoryDto createMockCategoryDto(String id, String name) {
        return CategoryDto.builder()
            .id(id)
            .name(name)
            .picture("https://example.com/picture.jpg")
            .permalink("https://example.com/permalink")
            .totalItemsInThisCategory(1000L)
            .pathFromRoot(Collections.emptyList())
            .childrenCategories(Collections.emptyList())
            .settings(null)
            .build();
    }
    
    private CategoryDto createComplexMockCategoryDto() {
        CategoryDto.PathFromRootDto pathFromRoot = CategoryDto.PathFromRootDto.builder()
            .id("MLA")
            .name("Mercado Libre Argentina")
            .build();
            
        CategoryDto.ChildrenCategoryDto childCategory = CategoryDto.ChildrenCategoryDto.builder()
            .id("MLA1056")
            .name("Accesorios para Celulares")
            .totalItemsInThisCategory(500L)
            .build();
            
        CategoryDto.SettingsDto settings = CategoryDto.SettingsDto.builder()
            .adultContent(false)
            .buyingAllowed(true)
            .buyingModes(Arrays.asList("buy_it_now", "auction"))
            .catalogDomain("MLA-CELLPHONES")
            .coverageAreas("national")
            .currency("ARS")
            .immediatePayment("required")
            .build();
        
        return CategoryDto.builder()
            .id("MLA1055")
            .name("Celulares y Smartphones")
            .picture("https://example.com/picture.jpg")
            .permalink("https://example.com/permalink")
            .totalItemsInThisCategory(1000L)
            .pathFromRoot(Arrays.asList(pathFromRoot))
            .childrenCategories(Arrays.asList(childCategory))
            .settings(settings)
            .build();
    }
}
