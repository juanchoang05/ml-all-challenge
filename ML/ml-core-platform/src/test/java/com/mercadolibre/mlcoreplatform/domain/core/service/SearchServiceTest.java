package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.domain.port.out.SearchApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchApiPort searchApiPort;
    
    private SearchService searchService;
    
    @BeforeEach
    void setUp() {
        searchService = new SearchService(searchApiPort);
    }
    
    @Test
    void searchProducts_ShouldReturnSearchResults() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("iphone")
            .siteId("MLA")
            .categoryId("MLA1055")
            .minPrice(1000.0)
            .maxPrice(2000.0)
            .condition("new")
            .offset(0)
            .limit(10)
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA123456")
                .title("iPhone 15 Pro")
                .price(BigDecimal.valueOf(1500))
                .categoryId("MLA1055")
                .condition("new")
                .build(),
            ProductResponse.builder()
                .id("MLA789012")
                .title("iPhone 14")
                .price(BigDecimal.valueOf(1200))
                .categoryId("MLA1055")
                .condition("new")
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA123456", result.get(0).id());
        assertEquals("iPhone 15 Pro", result.get(0).title());
        assertEquals(BigDecimal.valueOf(1500), result.get(0).price());
        assertEquals("MLA789012", result.get(1).id());
        assertEquals("iPhone 14", result.get(1).title());
        assertEquals(BigDecimal.valueOf(1200), result.get(1).price());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WhenNoResults_ShouldReturnEmptyList() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("nonexistent product")
            .siteId("MLA")
            .build();
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(Collections.emptyList());
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithMinimalCriteria_ShouldWork() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("test")
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA111111")
                .title("Test Product")
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MLA111111", result.get(0).id());
        assertEquals("Test Product", result.get(0).title());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithAllCriteria_ShouldWork() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("smartphone")
            .siteId("MLA")
            .categoryId("MLA1055")
            .minPrice(500.0)
            .maxPrice(3000.0)
            .condition("new")
            .shipping("free")
            .sort("price_asc")
            .offset(0)
            .limit(20)
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA555555")
                .title("Smartphone Premium")
                .price(BigDecimal.valueOf(2500))
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MLA555555", result.get(0).id());
        assertEquals("Smartphone Premium", result.get(0).title());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithNullCriteria_ShouldHandleGracefully() {
        // Given
        SearchCriteria criteria = null;
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(Collections.emptyList());
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithPriceRangeFilter_ShouldReturnFilteredResults() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("laptop")
            .minPrice(800.0)
            .maxPrice(1500.0)
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA666666")
                .title("Laptop Gaming")
                .price(BigDecimal.valueOf(1200))
                .build(),
            ProductResponse.builder()
                .id("MLA777777")
                .title("Laptop Office")
                .price(BigDecimal.valueOf(900))
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verify both products are within price range
        result.forEach(product -> {
            assertTrue(product.price().compareTo(BigDecimal.valueOf(800.0)) >= 0);
            assertTrue(product.price().compareTo(BigDecimal.valueOf(1500.0)) <= 0);
        });
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithCategoryFilter_ShouldReturnCategorySpecificResults() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("celular")
            .categoryId("MLA1055")
            .siteId("MLA")
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA888888")
                .title("Celular Samsung")
                .categoryId("MLA1055")
                .build(),
            ProductResponse.builder()
                .id("MLA999999")
                .title("Celular Xiaomi")
                .categoryId("MLA1055")
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verify all products belong to the specified category
        result.forEach(product -> 
            assertEquals("MLA1055", product.categoryId())
        );
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithPaginationCriteria_ShouldWork() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("tablet")
            .offset(10)
            .limit(5)
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA101010")
                .title("Tablet 1")
                .build(),
            ProductResponse.builder()
                .id("MLA101011")
                .title("Tablet 2")
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA101010", result.get(0).id());
        assertEquals("MLA101011", result.get(1).id());
        verify(searchApiPort).searchProducts(criteria);
    }
    
    @Test
    void searchProducts_WithSortingCriteria_ShouldWork() {
        // Given
        SearchCriteria criteria = SearchCriteria.builder()
            .query("mouse")
            .sort("price_desc")
            .build();
        
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA202020")
                .title("Mouse Gaming Premium")
                .price(BigDecimal.valueOf(150))
                .build(),
            ProductResponse.builder()
                .id("MLA202021")
                .title("Mouse Gaming Standard")
                .price(BigDecimal.valueOf(80))
                .build()
        );
        
        when(searchApiPort.searchProducts(criteria)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = searchService.searchProducts(criteria);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Results should be sorted by price descending
        assertTrue(result.get(0).price().compareTo(result.get(1).price()) > 0);
        verify(searchApiPort).searchProducts(criteria);
    }
}
