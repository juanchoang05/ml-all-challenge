package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;

import java.util.List;

public interface ProductUseCase {
    
    List<ProductResponse> getAllProducts(int offset, int limit);
    
    ProductResponse getProductById(String productId);
    
    ProductDescriptionResponse getProductDescription(String productId);
    
    ShippingOptionsResponse getShippingOptions(String productId);
    
    List<ProductResponse> searchProducts(ProductSearchRequest request);
}
