package com.mercadolibre.mlcoreplatform.adapter.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.QuestionDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionPersistencePort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class QuestionPersistenceAdapter implements QuestionPersistencePort {

    private final ObjectMapper objectMapper;
    private final Map<String, List<Map<String, Object>>> questionsData;

    public QuestionPersistenceAdapter() throws IOException {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        
        ClassPathResource resource = new ClassPathResource("questions.json");
        this.questionsData = objectMapper.readValue(
            resource.getInputStream(), 
            new TypeReference<Map<String, List<Map<String, Object>>>>() {}
        );
    }

    @Override
    public List<QuestionDto> findByItemId(String itemId) {
        List<Map<String, Object>> itemQuestions = questionsData.get(itemId);
        if (itemQuestions == null) {
            return List.of();
        }
        
        return itemQuestions.stream()
                .map(this::mapToQuestionDto)
                .toList();
    }

    @Override
    public List<QuestionDto> findAll() {
        return questionsData.values().stream()
                .flatMap(List::stream)
                .map(this::mapToQuestionDto)
                .toList();
    }

    @Override
    public List<QuestionDto> findBySellerId(Long sellerId) {
        return questionsData.values().stream()
                .flatMap(List::stream)
                .filter(question -> sellerId.equals(((Number) question.get("seller_id")).longValue()))
                .map(this::mapToQuestionDto)
                .toList();
    }

    @Override
    public List<QuestionDto> findByStatus(String status) {
        return questionsData.values().stream()
                .flatMap(List::stream)
                .filter(question -> status.equals(question.get("status")))
                .map(this::mapToQuestionDto)
                .toList();
    }

    @Override
    public List<QuestionDto> findUnansweredQuestions() {
        return findByStatus("UNANSWERED");
    }

    @SuppressWarnings("unchecked")
    private QuestionDto mapToQuestionDto(Map<String, Object> data) {
        return QuestionDto.builder()
                .id(data.get("id") != null ? ((Number) data.get("id")).longValue() : null)
                .text((String) data.get("text"))
                .status((String) data.get("status"))
                .dateCreated(parseDateTime((String) data.get("date_created")))
                .itemId((String) data.get("item_id"))
                .sellerId(data.get("seller_id") != null ? ((Number) data.get("seller_id")).longValue() : null)
                .from(mapQuestionFrom((Map<String, Object>) data.get("from")))
                .answer(mapAnswer((Map<String, Object>) data.get("answer")))
                .deletedFromListing((Boolean) data.get("deleted_from_listing"))
                .hold((Boolean) data.get("hold"))
                .tags((List<String>) data.get("tags"))
                .build();
    }

    private QuestionDto.QuestionFromDto mapQuestionFrom(Map<String, Object> from) {
        if (from == null) return null;
        return QuestionDto.QuestionFromDto.builder()
                .id(from.get("id") != null ? ((Number) from.get("id")).longValue() : null)
                .answeredQuestions(from.get("answered_questions") != null ? 
                    ((Number) from.get("answered_questions")).intValue() : null)
                .build();
    }

    private QuestionDto.AnswerDto mapAnswer(Map<String, Object> answer) {
        if (answer == null) return null;
        return QuestionDto.AnswerDto.builder()
                .text((String) answer.get("text"))
                .status((String) answer.get("status"))
                .dateCreated(parseDateTime((String) answer.get("date_created")))
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
