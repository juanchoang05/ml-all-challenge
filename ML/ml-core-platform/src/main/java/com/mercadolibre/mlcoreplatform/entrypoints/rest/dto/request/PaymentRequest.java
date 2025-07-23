package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request;

import lombok.Builder;

@Builder
public record PaymentRequest(
        String paymentMethodId,
        String transactionAmount,
        String description,
        String payerId,
        String token,
        Integer installments,
        String issuerId
) {
}
