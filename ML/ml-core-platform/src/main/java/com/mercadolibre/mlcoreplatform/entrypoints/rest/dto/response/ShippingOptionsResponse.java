package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;

@Builder
public record ShippingOptionsResponse(
        String itemId,
        List<ShippingOption> options
) {
    
    @Builder
    public record ShippingOption(
            String id,
            String name,
            String mode,
            BigDecimal cost,
            String currency,
            Integer estimatedDays,
            String speed,
            Boolean freeShipping,
            String shippingMethodId,
            String description
    ) {}
}
