package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ProductDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.ProductUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.ProductApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.ProductPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {
    
    private final ProductPersistencePort productPersistencePort;
    private final ProductApiPort productApiPort;
    
    @Override
    public List<ProductResponse> getAllProducts(int offset, int limit) {
        List<ProductDto> products = productPersistencePort.findAll();
        return products.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public ProductResponse getProductById(String productId) {
        return productPersistencePort.findById(productId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }
    
    @Override
    public ProductDescriptionResponse getProductDescription(String productId) {
        return productApiPort.getProductDescription(productId);
    }
    
    @Override
    public ShippingOptionsResponse getShippingOptions(String productId) {
        return productApiPort.getShippingOptions(productId);
    }
    
    @Override
    public List<ProductResponse> searchProducts(ProductSearchRequest request) {
        return productApiPort.searchProducts(request);
    }
    
    private ProductResponse mapToResponse(ProductDto productDto) {
        return ProductResponse.builder()
                .id(productDto.id())
                .title(productDto.title())
                .categoryId(productDto.categoryId())
                .price(productDto.price() != null ? BigDecimal.valueOf(productDto.price()) : null)
                .currencyId(productDto.currencyId())
                .availableQuantity(productDto.availableQuantity())
                .condition(productDto.condition())
                .permalink(productDto.permalink())
                .thumbnailId(productDto.thumbnail())
                .dateCreated(productDto.dateCreated())
                .lastUpdated(productDto.lastUpdated())
                .sellerId(productDto.sellerId() != null ? productDto.sellerId().toString() : null)
                .warranty(productDto.warranty())
                .build();
    }
}
