package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductPersistencePort {
    
    Optional<ProductDto> findById(String productId);
    
    List<ProductDto> findAll();
    
    List<ProductDto> findByCategoryId(String categoryId);
    
    List<ProductDto> findBySellerId(Long sellerId);
}
