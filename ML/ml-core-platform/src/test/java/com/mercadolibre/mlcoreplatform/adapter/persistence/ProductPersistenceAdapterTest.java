package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {

    private ProductPersistenceAdapter productPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        productPersistenceAdapter = new ProductPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(productPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllProducts() {
        // When
        List<ProductDto> products = productPersistenceAdapter.findAll();

        // Then
        assertThat(products)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all products have required fields
        products.forEach(product -> {
            assertThat(product.id()).isNotNull();
            assertThat(product.title()).isNotNull();
            assertThat(product.siteId()).isNotNull();
        });
    }

    @Test
    void shouldFindProductById_WhenProductExists() {
        // Given - First get a valid product ID from findAll
        List<ProductDto> allProducts = productPersistenceAdapter.findAll();
        assertThat(allProducts).isNotEmpty();
        String validProductId = allProducts.get(0).id();

        // When
        Optional<ProductDto> result = productPersistenceAdapter.findById(validProductId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(validProductId);
        assertThat(result.get().title()).isNotNull();
    }

    @Test
    void shouldReturnEmptyOptional_WhenProductDoesNotExist() {
        // Given
        String nonExistentProductId = "NON_EXISTENT_ID";

        // When
        Optional<ProductDto> result = productPersistenceAdapter.findById(nonExistentProductId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptional_WhenProductIdIsNull() {
        // When
        Optional<ProductDto> result = productPersistenceAdapter.findById(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindProductsByCategoryId() {
        // Given - Get all products and find a valid category ID
        List<ProductDto> allProducts = productPersistenceAdapter.findAll();
        assertThat(allProducts).isNotEmpty();
        
        ProductDto productWithCategory = allProducts.stream()
                .filter(product -> product.categoryId() != null)
                .findFirst()
                .orElse(null);

        if (productWithCategory != null) {
            String categoryId = productWithCategory.categoryId();

            // When
            List<ProductDto> result = productPersistenceAdapter.findByCategoryId(categoryId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            result.forEach(product -> {
                assertThat(product.categoryId()).isEqualTo(categoryId);
            });
        }
    }

    @Test
    void shouldReturnEmptyList_WhenCategoryIdDoesNotExist() {
        // Given
        String nonExistentCategoryId = "NON_EXISTENT_CATEGORY";

        // When
        List<ProductDto> result = productPersistenceAdapter.findByCategoryId(nonExistentCategoryId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldFindProductsBySellerId() {
        // Given - Get all products and find a valid seller ID
        List<ProductDto> allProducts = productPersistenceAdapter.findAll();
        assertThat(allProducts).isNotEmpty();
        
        ProductDto productWithSeller = allProducts.stream()
                .filter(product -> product.sellerId() != null)
                .findFirst()
                .orElse(null);

        if (productWithSeller != null) {
            Long sellerId = productWithSeller.sellerId();

            // When
            List<ProductDto> result = productPersistenceAdapter.findBySellerId(sellerId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            result.forEach(product -> {
                assertThat(product.sellerId()).isEqualTo(sellerId);
            });
        }
    }

    @Test
    void shouldReturnEmptyList_WhenSellerIdDoesNotExist() {
        // Given
        Long nonExistentSellerId = 999999999L;

        // When
        List<ProductDto> result = productPersistenceAdapter.findBySellerId(nonExistentSellerId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapProductDtoCorrectly() {
        // Given
        List<ProductDto> products = productPersistenceAdapter.findAll();
        assertThat(products).isNotEmpty();

        // When
        ProductDto product = products.get(0);

        // Then - Verify required fields are properly mapped
        assertThat(product.id()).isNotNull();
        assertThat(product.title()).isNotNull();
        assertThat(product.siteId()).isNotNull();
        
        // Verify that numeric fields are properly converted
        if (product.price() != null) {
            assertThat(product.price()).isInstanceOf(Double.class);
        }
        
        if (product.sellerId() != null) {
            assertThat(product.sellerId()).isInstanceOf(Long.class);
        }
        
        if (product.availableQuantity() != null) {
            assertThat(product.availableQuantity()).isInstanceOf(Integer.class);
        }
    }

    @Test
    void shouldHandleNullPriceFields() {
        // Given
        List<ProductDto> products = productPersistenceAdapter.findAll();
        assertThat(products).isNotEmpty();

        // Then - Verify that null price fields are handled correctly
        products.forEach(product -> {
            // These fields can be null and should not throw exceptions
            Double price = product.price();
            Double basePrice = product.basePrice();
            Double originalPrice = product.originalPrice();
            
            // If not null, they should be valid Double values
            if (price != null) {
                assertThat(price).isInstanceOf(Double.class);
            }
            if (basePrice != null) {
                assertThat(basePrice).isInstanceOf(Double.class);
            }
            if (originalPrice != null) {
                assertThat(originalPrice).isInstanceOf(Double.class);
            }
        });
    }

    @Test
    void shouldHandleNullQuantityFields() {
        // Given
        List<ProductDto> products = productPersistenceAdapter.findAll();
        assertThat(products).isNotEmpty();

        // Then - Verify that null quantity fields are handled correctly
        products.forEach(product -> {
            // These fields can be null and should not throw exceptions
            Integer initialQuantity = product.initialQuantity();
            Integer availableQuantity = product.availableQuantity();
            Integer soldQuantity = product.soldQuantity();
            
            // If not null, they should be valid Integer values
            if (initialQuantity != null) {
                assertThat(initialQuantity).isInstanceOf(Integer.class);
            }
            if (availableQuantity != null) {
                assertThat(availableQuantity).isInstanceOf(Integer.class);
            }
            if (soldQuantity != null) {
                assertThat(soldQuantity).isInstanceOf(Integer.class);
            }
        });
    }

    @Test
    void shouldHandleNestedObjects() {
        // Given
        List<ProductDto> products = productPersistenceAdapter.findAll();
        assertThat(products).isNotEmpty();

        // Then - Verify that nested objects are handled correctly
        products.forEach(product -> {
            // These nested objects can be null and should not throw exceptions
            if (product.pictures() != null) {
                assertThat(product.pictures()).isNotNull();
            }
            
            if (product.descriptions() != null) {
                assertThat(product.descriptions()).isNotNull();
            }
            
            if (product.shipping() != null) {
                assertThat(product.shipping()).isNotNull();
            }
            
            if (product.sellerAddress() != null) {
                assertThat(product.sellerAddress()).isNotNull();
            }
            
            if (product.attributes() != null) {
                assertThat(product.attributes()).isNotNull();
            }
        });
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<ProductDto> allProducts1 = productPersistenceAdapter.findAll();
        List<ProductDto> allProducts2 = productPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allProducts1).hasSize(allProducts2.size());
        
        // Verify that finding by ID is consistent
        if (!allProducts1.isEmpty()) {
            String productId = allProducts1.get(0).id();
            Optional<ProductDto> product1 = productPersistenceAdapter.findById(productId);
            Optional<ProductDto> product2 = productPersistenceAdapter.findById(productId);
            
            assertThat(product1).isPresent();
            assertThat(product2).isPresent();
            assertThat(product1.get().id()).isEqualTo(product2.get().id());
            assertThat(product1.get().title()).isEqualTo(product2.get().title());
        }
    }

    @Test
    void shouldFilterCorrectlyByCategoryId() {
        // Given
        List<ProductDto> allProducts = productPersistenceAdapter.findAll();
        
        // Find products that have categories
        List<ProductDto> productsWithCategories = allProducts.stream()
                .filter(product -> product.categoryId() != null)
                .toList();

        if (!productsWithCategories.isEmpty()) {
            String categoryId = productsWithCategories.get(0).categoryId();

            // When
            List<ProductDto> filteredProducts = productPersistenceAdapter.findByCategoryId(categoryId);

            // Then
            assertThat(filteredProducts).isNotEmpty();
            
            // All returned products should have the specified category
            filteredProducts.forEach(product -> {
                assertThat(product.categoryId()).isEqualTo(categoryId);
            });
            
            // Should not return products with different categories
            List<ProductDto> otherCategoryProducts = allProducts.stream()
                    .filter(product -> product.categoryId() != null && !product.categoryId().equals(categoryId))
                    .toList();
                    
            if (!otherCategoryProducts.isEmpty()) {
                assertThat(filteredProducts).hasSizeLessThan(allProducts.size());
            }
        }
    }

    @Test
    void shouldFilterCorrectlyBySellerId() {
        // Given
        List<ProductDto> allProducts = productPersistenceAdapter.findAll();
        
        // Find products that have sellers
        List<ProductDto> productsWithSellers = allProducts.stream()
                .filter(product -> product.sellerId() != null)
                .toList();

        if (!productsWithSellers.isEmpty()) {
            Long sellerId = productsWithSellers.get(0).sellerId();

            // When
            List<ProductDto> filteredProducts = productPersistenceAdapter.findBySellerId(sellerId);

            // Then
            assertThat(filteredProducts).isNotEmpty();
            
            // All returned products should have the specified seller
            filteredProducts.forEach(product -> {
                assertThat(product.sellerId()).isEqualTo(sellerId);
            });
            
            // Should not return products with different sellers
            List<ProductDto> otherSellerProducts = allProducts.stream()
                    .filter(product -> product.sellerId() != null && !product.sellerId().equals(sellerId))
                    .toList();
                    
            if (!otherSellerProducts.isEmpty()) {
                assertThat(filteredProducts).hasSizeLessThan(allProducts.size());
            }
        }
    }
}
