package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.PaymentMethodDto;

import java.util.List;

public interface PaymentMethodPersistencePort {
    
    List<PaymentMethodDto> findBySiteId(String siteId);
    
    List<PaymentMethodDto> findAll();
    
    List<PaymentMethodDto> findByPaymentType(String siteId, String paymentType);
}
