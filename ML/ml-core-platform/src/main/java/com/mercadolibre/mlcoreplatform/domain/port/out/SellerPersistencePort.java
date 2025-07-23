package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.SellerDto;

import java.util.List;
import java.util.Optional;

public interface SellerPersistencePort {
    
    Optional<SellerDto> findById(Long sellerId);
    
    List<SellerDto> findAll();
    
    List<SellerDto> findByNickname(String nickname);
    
    List<SellerDto> findByCountryId(String countryId);
}
