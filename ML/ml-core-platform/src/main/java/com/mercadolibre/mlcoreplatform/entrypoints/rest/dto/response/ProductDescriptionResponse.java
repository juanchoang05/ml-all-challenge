package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record ProductDescriptionResponse(
        String id,
        String text,
        String plainText,
        LocalDateTime lastUpdated,
        LocalDateTime dateCreated,
        String snapshot
) {
}
