package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record QuestionDto(
    Long id,
    String text,
    String status,
    LocalDateTime dateCreated,
    String itemId,
    Long sellerId,
    QuestionFromDto from,
    AnswerDto answer,
    Boolean deletedFromListing,
    Boolean hold,
    List<String> tags
) {
    @Builder
    public record QuestionFromDto(
        Long id,
        Integer answeredQuestions
    ) {}

    @Builder
    public record AnswerDto(
        String text,
        String status,
        LocalDateTime dateCreated
    ) {}
}
