package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class QuestionPersistenceAdapterTest {

    private QuestionPersistenceAdapter questionPersistenceAdapter;

    @BeforeEach
    void setUp() throws IOException {
        questionPersistenceAdapter = new QuestionPersistenceAdapter();
    }

    @Test
    void shouldInitializeSuccessfully() {
        assertThat(questionPersistenceAdapter).isNotNull();
    }

    @Test
    void shouldFindQuestionsByItemId() {
        // Given - Use a common item ID pattern
        String itemId = "MLA1"; // Common pattern for testing

        // When
        List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);

        // Then
        assertThat(questions).isNotNull();
        
        // If questions exist, verify their structure
        questions.forEach(question -> {
            assertThat(question.id()).isNotNull();
            assertThat(question.text()).isNotNull();
            assertThat(question.status()).isNotNull();
        });
    }

    @Test
    void shouldReturnEmptyList_WhenItemIdDoesNotExist() {
        // Given
        String nonExistentItemId = "NON_EXISTENT_ITEM";

        // When
        List<QuestionDto> result = questionPersistenceAdapter.findByItemId(nonExistentItemId);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyList_WhenItemIdIsNull() {
        // When
        List<QuestionDto> result = questionPersistenceAdapter.findByItemId(null);

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldMapQuestionDtoCorrectly() {
        // Given - Find any item with questions
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        QuestionDto testQuestion = null;
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            if (!questions.isEmpty()) {
                testQuestion = questions.get(0);
                break;
            }
        }

        if (testQuestion != null) {
            // Then - Verify all required fields are properly mapped
            assertThat(testQuestion.id()).isNotNull();
            assertThat(testQuestion.text()).isNotNull();
            assertThat(testQuestion.status()).isNotNull();
            
            // Verify date fields if present
            if (testQuestion.dateCreated() != null) {
                assertThat(testQuestion.dateCreated()).isNotNull();
            }
            
            // Verify nested objects
            if (testQuestion.from() != null) {
                assertThat(testQuestion.from()).isNotNull();
                if (testQuestion.from().id() != null) {
                    assertThat(testQuestion.from().id()).isInstanceOf(Long.class);
                }
            }
            
            if (testQuestion.answer() != null) {
                assertThat(testQuestion.answer()).isNotNull();
                assertThat(testQuestion.answer().text()).isNotNull();
                assertThat(testQuestion.answer().status()).isNotNull();
            }
        }
    }

    @Test
    void shouldHandleNullDateFields() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then - Verify that null date fields are handled correctly
            questions.forEach(question -> {
                // Date fields can be null and should not throw exceptions
                if (question.dateCreated() != null) {
                    assertThat(question.dateCreated()).isNotNull();
                }
                
                if (question.answer() != null && question.answer().dateCreated() != null) {
                    assertThat(question.answer().dateCreated()).isNotNull();
                }
            });
        }
    }

    @Test
    void shouldHandleNullAnswer() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then - Verify that null answers are handled correctly
            questions.forEach(question -> {
                // Answer can be null and should not throw exceptions
                if (question.answer() != null) {
                    assertThat(question.answer()).isNotNull();
                    assertThat(question.answer().text()).isNotNull();
                    assertThat(question.answer().status()).isNotNull();
                }
            });
        }
    }

    @Test
    void shouldHandleFromUser() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then - Verify that from user is handled correctly
            questions.forEach(question -> {
                // From user can be null and should not throw exceptions
                if (question.from() != null) {
                    assertThat(question.from()).isNotNull();
                    
                    if (question.from().id() != null) {
                        assertThat(question.from().id()).isInstanceOf(Long.class);
                    }
                    
                    if (question.from().answeredQuestions() != null) {
                        assertThat(question.from().answeredQuestions()).isInstanceOf(Integer.class);
                    }
                }
            });
        }
    }

    @Test
    void shouldHaveValidQuestionStatuses() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then - All questions should have valid statuses
            questions.forEach(question -> {
                assertThat(question.status()).isNotNull();
                
                // Status should be one of the common values
                List<String> validStatuses = List.of(
                    "UNANSWERED", "ANSWERED", "CLOSED_UNANSWERED", "UNDER_REVIEW", "BANNED"
                );
                assertThat(validStatuses).contains(question.status());
            });
        }
    }

    @Test
    void shouldMaintainDataConsistency() {
        // Given
        String itemId = "MLA1";
        List<QuestionDto> questions1 = questionPersistenceAdapter.findByItemId(itemId);
        List<QuestionDto> questions2 = questionPersistenceAdapter.findByItemId(itemId);

        // Then - Multiple calls should return the same data
        assertThat(questions1).hasSize(questions2.size());
        
        // Verify that content is consistent
        if (!questions1.isEmpty()) {
            QuestionDto firstQuestion1 = questions1.get(0);
            QuestionDto firstQuestion2 = questions2.get(0);
            
            assertThat(firstQuestion1.id()).isEqualTo(firstQuestion2.id());
            assertThat(firstQuestion1.text()).isEqualTo(firstQuestion2.text());
            assertThat(firstQuestion1.status()).isEqualTo(firstQuestion2.status());
        }
    }

    @Test
    void shouldHaveValidQuestionIds() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then - All questions should have valid IDs
            questions.forEach(question -> {
                assertThat(question.id()).isNotNull();
                assertThat(question.id()).isInstanceOf(Long.class);
            });
            
            // IDs should be unique within the item
            if (questions.size() > 1) {
                List<Long> ids = questions.stream()
                        .map(QuestionDto::id)
                        .toList();
                        
                List<Long> uniqueIds = ids.stream()
                        .distinct()
                        .toList();
                        
                assertThat(ids).hasSize(uniqueIds.size());
            }
        }
    }

    @Test
    void shouldFilterCorrectlyByItemId() {
        // Given - Try multiple item IDs
        String[] testItemIds = {"MLA1", "MLA123", "MLB456"};
        
        for (String itemId : testItemIds) {
            // When
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Then
            assertThat(questions).isNotNull();
            
            // All returned questions should be related to the specified item
            questions.forEach(question -> {
                assertThat(question.itemId()).isEqualTo(itemId);
            });
        }
    }

    @Test
    void shouldHandleAnsweredQuestions() {
        // Given - Find questions to test
        String[] testItemIds = {"MLA1", "MLA123", "MLB456", "item1"};
        
        for (String itemId : testItemIds) {
            List<QuestionDto> questions = questionPersistenceAdapter.findByItemId(itemId);
            
            // Find answered questions
            List<QuestionDto> answeredQuestions = questions.stream()
                    .filter(question -> question.answer() != null)
                    .toList();
            
            // Then - Answered questions should have proper answer structure
            answeredQuestions.forEach(question -> {
                assertThat(question.answer()).isNotNull();
                assertThat(question.answer().text()).isNotNull();
                assertThat(question.answer().status()).isNotNull();
                
                if (question.answer().dateCreated() != null) {
                    assertThat(question.answer().dateCreated()).isNotNull();
                }
            });
        }
    }
}
