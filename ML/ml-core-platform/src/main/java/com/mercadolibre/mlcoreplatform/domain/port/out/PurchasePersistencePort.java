package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PurchaseDto;

import java.util.List;
import java.util.Optional;

public interface PurchasePersistencePort {
    
    Optional<PurchaseDto> findById(String orderId);
    
    List<PurchaseDto> findAll();
    
    List<PurchaseDto> findByStatus(String status);
    
    List<PurchaseDto> findByBuyerId(Long buyerId);
    
    List<PurchaseDto> findBySellerId(Long sellerId);
}
