package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request;

import lombok.Builder;

@Builder
public record QuestionRequest(
        String itemId,
        String sellerId,
        String text,
        String fromUserId
) {
}
