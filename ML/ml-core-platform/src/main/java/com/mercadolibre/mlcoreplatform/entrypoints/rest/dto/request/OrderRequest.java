package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request;

import lombok.Builder;

@Builder
public record OrderRequest(
        String buyerId,
        String sellerId,
        String itemId,
        Integer quantity,
        String paymentMethodId,
        String shippingMode,
        String shippingAddress
) {
}
