package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.ReviewDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ReviewPersistenceAdapterTest {

    private ReviewPersistenceAdapter reviewPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        reviewPersistenceAdapter = new ReviewPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(reviewPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindAllReviews() {
        // When
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();

        // Then
        assertThat(reviews)
                .isNotNull()
                .isNotEmpty();
        
        // Verify that all reviews have required fields
        reviews.forEach(review -> {
            assertThat(review.itemId()).isNotNull();
            assertThat(review.totalReviews()).isNotNull();
        });
    }

    @Test
    void shouldFindReviewByItemId_WhenItemExists() {
        // Given - First get a valid item ID from findAll
        List<ReviewDto> allReviews = reviewPersistenceAdapter.findAll();
        assertThat(allReviews).isNotEmpty();
        String validItemId = allReviews.get(0).itemId();

        // When
        Optional<ReviewDto> result = reviewPersistenceAdapter.findByItemId(validItemId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().itemId()).isEqualTo(validItemId);
        assertThat(result.get().totalReviews()).isNotNull();
    }

    @Test
    void shouldReturnEmptyOptional_WhenItemIdDoesNotExist() {
        // Given
        String nonExistentItemId = "NON_EXISTENT_ITEM";

        // When
        Optional<ReviewDto> result = reviewPersistenceAdapter.findByItemId(nonExistentItemId);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyOptional_WhenItemIdIsNull() {
        // When
        Optional<ReviewDto> result = reviewPersistenceAdapter.findByItemId(null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldMapReviewDtoCorrectly() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        assertThat(reviews).isNotEmpty();

        // When
        ReviewDto review = reviews.get(0);

        // Then - Verify required fields are properly mapped
        assertThat(review.itemId()).isNotNull();
        assertThat(review.totalReviews()).isNotNull();
        
        // Verify numeric fields
        if (review.ratingAverage() != null) {
            assertThat(review.ratingAverage()).isInstanceOf(Double.class);
            assertThat(review.ratingAverage()).isBetween(1.0, 5.0);
        }
        
        if (review.totalReviews() != null) {
            assertThat(review.totalReviews()).isInstanceOf(Integer.class);
            assertThat(review.totalReviews()).isGreaterThanOrEqualTo(0);
        }
    }

    @Test
    void shouldHandleReviewsList() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        assertThat(reviews).isNotEmpty();

        // Then - Verify that review items are handled correctly
        reviews.forEach(review -> {
            if (review.reviews() != null) {
                assertThat(review.reviews()).isNotNull();
                review.reviews().forEach(reviewItem -> {
                    assertThat(reviewItem.id()).isNotNull();
                    assertThat(reviewItem.rating()).isNotNull();
                    assertThat(reviewItem.rating()).isBetween(1, 5);
                });
            }
        });
    }

    @Test
    void shouldHandleRatingLevels() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        assertThat(reviews).isNotEmpty();

        // Then - Verify that rating levels are handled correctly
        reviews.forEach(review -> {
            if (review.ratingLevels() != null) {
                assertThat(review.ratingLevels()).isNotNull();
                
                // Rating levels should be non-negative if present
                if (review.ratingLevels().fiveStar() != null) {
                    assertThat(review.ratingLevels().fiveStar()).isGreaterThanOrEqualTo(0);
                }
                if (review.ratingLevels().fourStar() != null) {
                    assertThat(review.ratingLevels().fourStar()).isGreaterThanOrEqualTo(0);
                }
                if (review.ratingLevels().threeStar() != null) {
                    assertThat(review.ratingLevels().threeStar()).isGreaterThanOrEqualTo(0);
                }
                if (review.ratingLevels().twoStar() != null) {
                    assertThat(review.ratingLevels().twoStar()).isGreaterThanOrEqualTo(0);
                }
                if (review.ratingLevels().oneStar() != null) {
                    assertThat(review.ratingLevels().oneStar()).isGreaterThanOrEqualTo(0);
                }
            }
        });
    }

    @Test
    void shouldHandleReviewItemDetails() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        
        // Find a review with individual review items
        Optional<ReviewDto> reviewWithItems = reviews.stream()
                .filter(review -> review.reviews() != null && !review.reviews().isEmpty())
                .findFirst();

        if (reviewWithItems.isPresent()) {
            ReviewDto review = reviewWithItems.get();
            
            // Then - Individual reviews should be properly mapped
            assertThat(review.reviews()).isNotEmpty();
            review.reviews().forEach(reviewItem -> {
                assertThat(reviewItem.id()).isNotNull();
                assertThat(reviewItem.rating()).isNotNull();
                assertThat(reviewItem.rating()).isBetween(1, 5);
                
                // Date fields can be null but should be valid if present
                if (reviewItem.dateCreated() != null) {
                    assertThat(reviewItem.dateCreated()).isNotNull();
                }
                
                // Reviewer can be null but should be valid if present
                if (reviewItem.reviewer() != null) {
                    assertThat(reviewItem.reviewer()).isNotNull();
                    if (reviewItem.reviewer().id() != null) {
                        assertThat(reviewItem.reviewer().id()).isNotNull();
                    }
                }
            });
        }
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        List<ReviewDto> allReviews1 = reviewPersistenceAdapter.findAll();
        List<ReviewDto> allReviews2 = reviewPersistenceAdapter.findAll();

        // Then - Multiple calls should return the same data
        assertThat(allReviews1).hasSize(allReviews2.size());
        
        // Verify that finding by item ID is consistent
        if (!allReviews1.isEmpty()) {
            String itemId = allReviews1.get(0).itemId();
            Optional<ReviewDto> review1 = reviewPersistenceAdapter.findByItemId(itemId);
            Optional<ReviewDto> review2 = reviewPersistenceAdapter.findByItemId(itemId);
            
            assertThat(review1).isPresent();
            assertThat(review2).isPresent();
            assertThat(review1.get().itemId()).isEqualTo(review2.get().itemId());
            assertThat(review1.get().totalReviews()).isEqualTo(review2.get().totalReviews());
        }
    }

    @Test
    void shouldHaveValidRatingAverages() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        assertThat(reviews).isNotEmpty();

        // Then - All rating averages should be within valid range
        reviews.forEach(review -> {
            if (review.ratingAverage() != null) {
                assertThat(review.ratingAverage()).isBetween(1.0, 5.0);
            }
        });
    }

    @Test
    void shouldHaveValidTotalReviews() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        assertThat(reviews).isNotEmpty();

        // Then - All total reviews should be non-negative
        reviews.forEach(review -> {
            if (review.totalReviews() != null) {
                assertThat(review.totalReviews()).isGreaterThanOrEqualTo(0);
            }
        });
    }

    @Test
    void shouldHaveConsistentReviewCounts() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        
        // Find reviews with both totalReviews and individual reviews
        List<ReviewDto> reviewsWithBoth = reviews.stream()
                .filter(review -> review.totalReviews() != null && 
                                 review.reviews() != null)
                .toList();

        // Then - Total count should match individual reviews count
        reviewsWithBoth.forEach(review -> {
            int actualCount = review.reviews().size();
            Integer totalCount = review.totalReviews();
            
            // The total count should be consistent with actual reviews
            // (allowing for cases where not all reviews are returned)
            assertThat(actualCount).isLessThanOrEqualTo(totalCount);
        });
    }

    @Test
    void shouldFilterCorrectlyByItemId() {
        // Given
        List<ReviewDto> allReviews = reviewPersistenceAdapter.findAll();
        assertThat(allReviews).isNotEmpty();

        for (ReviewDto review : allReviews) {
            String itemId = review.itemId();

            // When
            Optional<ReviewDto> foundReview = reviewPersistenceAdapter.findByItemId(itemId);

            // Then
            assertThat(foundReview).isPresent();
            assertThat(foundReview.get().itemId()).isEqualTo(itemId);
        }
    }

    @Test
    void shouldHandleReviewStatuses() {
        // Given
        List<ReviewDto> reviews = reviewPersistenceAdapter.findAll();
        
        // Find reviews with individual review items
        List<ReviewDto.ReviewItemDto> allIndividualReviews = reviews.stream()
                .filter(review -> review.reviews() != null)
                .flatMap(review -> review.reviews().stream())
                .toList();

        // Then - All review statuses should be valid
        allIndividualReviews.forEach(reviewItem -> {
            if (reviewItem.status() != null) {
                List<String> validStatuses = List.of(
                    "ACTIVE", "INACTIVE", "PENDING", "APPROVED", "REJECTED"
                );
                assertThat(validStatuses).contains(reviewItem.status());
            }
        });
    }
}
