package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.ProductSearchRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductDescriptionResponse;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ShippingOptionsResponse;

import java.util.List;

public interface ProductApiPort {
    
    ProductDescriptionResponse getProductDescription(String productId);
    
    ShippingOptionsResponse getShippingOptions(String productId);
    
    List<ProductResponse> searchProducts(ProductSearchRequest request);
}
