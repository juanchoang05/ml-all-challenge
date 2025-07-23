package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryPersistencePort {
    
    Optional<CategoryDto> findById(String categoryId);
    
    List<CategoryDto> findAll();
    
    List<CategoryDto> findByParentId(String parentId);
}
