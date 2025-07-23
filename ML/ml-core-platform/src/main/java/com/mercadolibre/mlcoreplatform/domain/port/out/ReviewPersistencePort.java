package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ReviewDto;

import java.util.List;
import java.util.Optional;

public interface ReviewPersistencePort {
    
    Optional<ReviewDto> findByItemId(String itemId);
    
    List<ReviewDto> findAll();
    
    List<ReviewDto> findByRatingGreaterThan(Double minRating);
}
