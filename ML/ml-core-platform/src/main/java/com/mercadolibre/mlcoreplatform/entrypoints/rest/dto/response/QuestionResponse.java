package com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record QuestionResponse(
        String id,
        String text,
        String status,
        LocalDateTime dateCreated,
        String itemId,
        String sellerId,
        String fromUserId,
        AnswerResponse answer
) {
    
    @Builder
    public record AnswerResponse(
            String text,
            String status,
            LocalDateTime dateCreated
    ) {}
}
