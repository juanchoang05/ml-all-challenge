package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.ProductApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductApiAdapter implements ProductApiPort {

    @Override
    public ProductDescriptionResponse getProductDescription(String productId) {
        // Mock implementation for testing
        return ProductDescriptionResponse.builder()
                .id(productId)
                .plainText("Mock product description for " + productId)
                .build();
    }

    @Override
    public ShippingOptionsResponse getShippingOptions(String productId) {
        // Mock implementation for testing
        return ShippingOptionsResponse.builder()
                .itemId(productId)
                .options(new ArrayList<>())
                .build();
    }

    @Override
    public List<ProductResponse> searchProducts(ProductSearchRequest request) {
        // Mock implementation for testing
        return new ArrayList<>();
    }
}
