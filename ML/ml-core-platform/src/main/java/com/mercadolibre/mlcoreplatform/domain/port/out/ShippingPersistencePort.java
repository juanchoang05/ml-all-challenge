package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ShippingDto;

import java.util.List;

public interface ShippingPersistencePort {
    
    List<ShippingDto> findByItemId(String itemId);
    
    List<ShippingDto> findAll();
    
    List<ShippingDto> findByDeliveryType(String deliveryType);
    
    List<ShippingDto> findFreeShippingOptions();
}
