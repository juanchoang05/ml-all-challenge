package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ReviewDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.ReviewPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ReviewPersistenceAdapter implements ReviewPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, Map<String, Object>> reviewsData;

    public ReviewPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("reviews.json");
        this.reviewsData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, Map<String, Object>>>() {}
        );
    }

    @Override
    public Optional<ReviewDto> findByItemId(String itemId) {
        Map<String, Object> reviewData = reviewsData.get(itemId);
        if (reviewData == null) {
            return Optional.empty();
        }
        
        return Optional.of(mapToReviewDto(reviewData));
    }

    @Override
    public List<ReviewDto> findAll() {
        return reviewsData.values().stream()
                .map(this::mapToReviewDto)
                .toList();
    }

    @Override
    public List<ReviewDto> findByRatingGreaterThan(Double minRating) {
        return reviewsData.values().stream()
                .filter(data -> {
                    Double rating = data.get("rating_average") != null ? 
                        ((Number) data.get("rating_average")).doubleValue() : 0.0;
                    return rating >= minRating;
                })
                .map(this::mapToReviewDto)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private ReviewDto mapToReviewDto(Map<String, Object> data) {
        return ReviewDto.builder()
                .itemId((String) data.get("item_id"))
                .ratingAverage(data.get("rating_average") != null ? 
                    ((Number) data.get("rating_average")).doubleValue() : null)
                .totalReviews(data.get("total_reviews") != null ? 
                    ((Number) data.get("total_reviews")).intValue() : null)
                .reviews(mapReviewItems((List<Map<String, Object>>) data.get("reviews")))
                .ratingLevels(mapRatingLevels((Map<String, Object>) data.get("rating_levels")))
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<ReviewDto.ReviewItemDto> mapReviewItems(List<Map<String, Object>> reviews) {
        if (reviews == null) return List.of();
        return reviews.stream()
                .map(review -> ReviewDto.ReviewItemDto.builder()
                        .id((String) review.get("id"))
                        .rating(review.get("rating") != null ? 
                            ((Number) review.get("rating")).intValue() : null)
                        .title((String) review.get("title"))
                        .content((String) review.get("content"))
                        .dateCreated(parseDateTime((String) review.get("date_created")))
                        .reviewer(mapReviewer((Map<String, Object>) review.get("reviewer")))
                        .likes(review.get("likes") != null ? 
                            ((Number) review.get("likes")).intValue() : null)
                        .dislikes(review.get("dislikes") != null ? 
                            ((Number) review.get("dislikes")).intValue() : null)
                        .helpfulCount(review.get("helpful_count") != null ? 
                            ((Number) review.get("helpful_count")).intValue() : null)
                        .images(mapReviewImages((List<Map<String, Object>>) review.get("images")))
                        .valorization((String) review.get("valorization"))
                        .status((String) review.get("status"))
                        .build())
                .toList();
    }

    private ReviewDto.ReviewerDto mapReviewer(Map<String, Object> reviewer) {
        if (reviewer == null) return null;
        return ReviewDto.ReviewerDto.builder()
                .id((String) reviewer.get("id"))
                .nickname((String) reviewer.get("nickname"))
                .levelId((String) reviewer.get("level_id"))
                .build();
    }

    private List<ReviewDto.ReviewImageDto> mapReviewImages(List<Map<String, Object>> images) {
        if (images == null) return List.of();
        return images.stream()
                .map(image -> ReviewDto.ReviewImageDto.builder()
                        .id((String) image.get("id"))
                        .url((String) image.get("url"))
                        .size((String) image.get("size"))
                        .build())
                .toList();
    }

    private ReviewDto.RatingLevelsDto mapRatingLevels(Map<String, Object> ratingLevels) {
        if (ratingLevels == null) return null;
        return ReviewDto.RatingLevelsDto.builder()
                .fiveStar(ratingLevels.get("five_star") != null ? 
                    ((Number) ratingLevels.get("five_star")).intValue() : null)
                .fourStar(ratingLevels.get("four_star") != null ? 
                    ((Number) ratingLevels.get("four_star")).intValue() : null)
                .threeStar(ratingLevels.get("three_star") != null ? 
                    ((Number) ratingLevels.get("three_star")).intValue() : null)
                .twoStar(ratingLevels.get("two_star") != null ? 
                    ((Number) ratingLevels.get("two_star")).intValue() : null)
                .oneStar(ratingLevels.get("one_star") != null ? 
                    ((Number) ratingLevels.get("one_star")).intValue() : null)
                .build();
    }

    private LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null) return null;
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
}
