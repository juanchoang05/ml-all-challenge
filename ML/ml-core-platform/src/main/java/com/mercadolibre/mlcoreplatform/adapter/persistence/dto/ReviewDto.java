package com.mercadolibre.mlcoreplatform.adapter.persistence.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReviewDto(
    String itemId,
    Double ratingAverage,
    Integer totalReviews,
    List<ReviewItemDto> reviews,
    RatingLevelsDto ratingLevels
) {
    @Builder
    public record ReviewItemDto(
        String id,
        Integer rating,
        String title,
        String content,
        LocalDateTime dateCreated,
        ReviewerDto reviewer,
        Integer likes,
        Integer dislikes,
        Integer helpfulCount,
        List<ReviewImageDto> images,
        String valorization,
        String status
    ) {}

    @Builder
    public record ReviewerDto(
        String id,
        String nickname,
        String levelId
    ) {}

    @Builder
    public record ReviewImageDto(
        String id,
        String url,
        String size
    ) {}

    @Builder
    public record RatingLevelsDto(
        Integer fiveStar,
        Integer fourStar,
        Integer threeStar,
        Integer twoStar,
        Integer oneStar
    ) {}
}
