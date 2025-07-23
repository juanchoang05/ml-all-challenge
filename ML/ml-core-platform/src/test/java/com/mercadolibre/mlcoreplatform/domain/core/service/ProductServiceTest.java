package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ProductDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.ProductApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.ProductPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductPersistencePort productPersistencePort;
    
    @Mock
    private ProductApiPort productApiPort;
    
    private ProductService productService;
    
    @BeforeEach
    void setUp() {
        productService = new ProductService(productPersistencePort, productApiPort);
    }
    
    @Test
    void getAllProducts_ShouldReturnPaginatedProducts() {
        // Given
        List<ProductDto> mockProducts = Arrays.asList(
            createMockProductDto("MLA123456", "iPhone 15 Pro"),
            createMockProductDto("MLA789012", "Samsung Galaxy S24"),
            createMockProductDto("MLA345678", "Google Pixel 8")
        );
        when(productPersistencePort.findAll()).thenReturn(mockProducts);
        
        // When
        List<ProductResponse> result = productService.getAllProducts(0, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA123456", result.get(0).id());
        assertEquals("iPhone 15 Pro", result.get(0).title());
        assertEquals("MLA789012", result.get(1).id());
        assertEquals("Samsung Galaxy S24", result.get(1).title());
        verify(productPersistencePort).findAll();
    }
    
    @Test
    void getAllProducts_WithOffset_ShouldSkipCorrectNumber() {
        // Given
        List<ProductDto> mockProducts = Arrays.asList(
            createMockProductDto("MLA123456", "iPhone 15 Pro"),
            createMockProductDto("MLA789012", "Samsung Galaxy S24"),
            createMockProductDto("MLA345678", "Google Pixel 8")
        );
        when(productPersistencePort.findAll()).thenReturn(mockProducts);
        
        // When
        List<ProductResponse> result = productService.getAllProducts(1, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA789012", result.get(0).id());
        assertEquals("MLA345678", result.get(1).id());
        verify(productPersistencePort).findAll();
    }
    
    @Test
    void getAllProducts_WhenNoProducts_ShouldReturnEmptyList() {
        // Given
        when(productPersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<ProductResponse> result = productService.getAllProducts(0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productPersistencePort).findAll();
    }
    
    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        String productId = "MLA123456";
        ProductDto mockProduct = createMockProductDto(productId, "iPhone 15 Pro");
        when(productPersistencePort.findById(productId)).thenReturn(Optional.of(mockProduct));
        
        // When
        ProductResponse result = productService.getProductById(productId);
        
        // Then
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("iPhone 15 Pro", result.title());
        assertEquals("MLA1055", result.categoryId());
        assertEquals(BigDecimal.valueOf(999999.99), result.price());
        verify(productPersistencePort).findById(productId);
    }
    
    @Test
    void getProductById_WhenProductNotFound_ShouldThrowException() {
        // Given
        String productId = "NON_EXISTENT";
        when(productPersistencePort.findById(productId)).thenReturn(Optional.empty());
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> productService.getProductById(productId));
        assertEquals("Product not found with id: NON_EXISTENT", exception.getMessage());
        verify(productPersistencePort).findById(productId);
    }
    
    @Test
    void getProductDescription_ShouldReturnDescription() {
        // Given
        String productId = "MLA123456";
        ProductDescriptionResponse mockDescription = ProductDescriptionResponse.builder()
            .plainText("Descripción del producto")
            .build();
        when(productApiPort.getProductDescription(productId)).thenReturn(mockDescription);
        
        // When
        ProductDescriptionResponse result = productService.getProductDescription(productId);
        
        // Then
        assertNotNull(result);
        assertEquals("Descripción del producto", result.plainText());
        verify(productApiPort).getProductDescription(productId);
    }
    
    @Test
    void getShippingOptions_ShouldReturnShippingOptions() {
        // Given
        String productId = "MLA123456";
        ShippingOptionsResponse mockShipping = ShippingOptionsResponse.builder()
            .itemId(productId)
            .options(Arrays.asList(
                ShippingOptionsResponse.ShippingOption.builder()
                    .id("shipping1")
                    .name("Envío gratis")
                    .mode("me2")
                    .freeShipping(true)
                    .build()
            ))
            .build();
        when(productApiPort.getShippingOptions(productId)).thenReturn(mockShipping);
        
        // When
        ShippingOptionsResponse result = productService.getShippingOptions(productId);
        
        // Then
        assertNotNull(result);
        assertEquals(productId, result.itemId());
        assertNotNull(result.options());
        assertFalse(result.options().isEmpty());
        assertTrue(result.options().get(0).freeShipping());
        assertEquals("me2", result.options().get(0).mode());
        verify(productApiPort).getShippingOptions(productId);
    }
    
    @Test
    void searchProducts_ShouldReturnSearchResults() {
        // Given
        ProductSearchRequest request = ProductSearchRequest.builder()
            .query("iphone")
            .categoryId("MLA1055")
            .offset(0)
            .limit(10)
            .build();
        List<ProductResponse> mockResults = Arrays.asList(
            ProductResponse.builder()
                .id("MLA123456")
                .title("iPhone 15 Pro")
                .build(),
            ProductResponse.builder()
                .id("MLA789012")
                .title("iPhone 14")
                .build()
        );
        when(productApiPort.searchProducts(request)).thenReturn(mockResults);
        
        // When
        List<ProductResponse> result = productService.searchProducts(request);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("MLA123456", result.get(0).id());
        assertEquals("iPhone 15 Pro", result.get(0).title());
        verify(productApiPort).searchProducts(request);
    }
    
    @Test
    void searchProducts_WhenNoResults_ShouldReturnEmptyList() {
        // Given
        ProductSearchRequest request = ProductSearchRequest.builder()
            .query("nonexistent")
            .build();
        when(productApiPort.searchProducts(request)).thenReturn(Collections.emptyList());
        
        // When
        List<ProductResponse> result = productService.searchProducts(request);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productApiPort).searchProducts(request);
    }
    
    @Test
    void mapToResponse_ShouldMapAllFields() {
        // Given
        String productId = "MLA123456";
        ProductDto productDto = createCompleteProductDto(productId);
        when(productPersistencePort.findById(productId)).thenReturn(Optional.of(productDto));
        
        // When
        ProductResponse result = productService.getProductById(productId);
        
        // Then
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("Complete Product", result.title());
        assertEquals("MLA1055", result.categoryId());
        assertEquals(BigDecimal.valueOf(1500.0), result.price());
        assertEquals("USD", result.currencyId());
        assertEquals(10, result.availableQuantity());
        assertEquals("new", result.condition());
        assertEquals("https://example.com/product", result.permalink());
        assertEquals("thumbnail123", result.thumbnailId());
        assertNotNull(result.dateCreated());
        assertNotNull(result.lastUpdated());
        assertEquals("12345", result.sellerId());
        assertEquals("6 meses", result.warranty());
    }
    
    @Test
    void mapToResponse_WithNullOptionalFields_ShouldHandleNulls() {
        // Given
        String productId = "MLA123456";
        ProductDto productDto = ProductDto.builder()
            .id(productId)
            .title("Product with Nulls")
            .categoryId("MLA1055")
            .price(null)
            .currencyId("USD")
            .availableQuantity(5)
            .condition("new")
            .permalink("https://example.com/product")
            .thumbnail("thumbnail123")
            .dateCreated(LocalDateTime.now())
            .lastUpdated(LocalDateTime.now())
            .sellerId(null)
            .warranty(null)
            .build();
        when(productPersistencePort.findById(productId)).thenReturn(Optional.of(productDto));
        
        // When
        ProductResponse result = productService.getProductById(productId);
        
        // Then
        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("Product with Nulls", result.title());
        assertNull(result.price());
        assertNull(result.sellerId());
        assertNull(result.warranty());
    }
    
    @Test
    void getAllProducts_WithZeroLimit_ShouldReturnEmptyList() {
        // Given
        List<ProductDto> mockProducts = Arrays.asList(
            createMockProductDto("MLA123456", "iPhone 15 Pro")
        );
        when(productPersistencePort.findAll()).thenReturn(mockProducts);
        
        // When
        List<ProductResponse> result = productService.getAllProducts(0, 0);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productPersistencePort).findAll();
    }
    
    @Test
    void getAllProducts_WithLargeOffset_ShouldReturnEmptyList() {
        // Given
        List<ProductDto> mockProducts = Arrays.asList(
            createMockProductDto("MLA123456", "iPhone 15 Pro")
        );
        when(productPersistencePort.findAll()).thenReturn(mockProducts);
        
        // When
        List<ProductResponse> result = productService.getAllProducts(10, 5);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productPersistencePort).findAll();
    }
    
    private ProductDto createMockProductDto(String id, String title) {
        return ProductDto.builder()
            .id(id)
            .title(title)
            .categoryId("MLA1055")
            .price(999999.99)
            .currencyId("ARS")
            .availableQuantity(1)
            .condition("new")
            .permalink("https://example.com/product/" + id)
            .thumbnail("thumbnail" + id.substring(3))
            .dateCreated(LocalDateTime.now())
            .lastUpdated(LocalDateTime.now())
            .sellerId(12345L)
            .warranty("1 año")
            .build();
    }
    
    private ProductDto createCompleteProductDto(String id) {
        return ProductDto.builder()
            .id(id)
            .title("Complete Product")
            .categoryId("MLA1055")
            .price(1500.0)
            .currencyId("USD")
            .availableQuantity(10)
            .condition("new")
            .permalink("https://example.com/product")
            .thumbnail("thumbnail123")
            .dateCreated(LocalDateTime.of(2024, 1, 15, 10, 30))
            .lastUpdated(LocalDateTime.of(2024, 1, 15, 12, 0))
            .sellerId(12345L)
            .warranty("6 meses")
            .build();
    }
}
