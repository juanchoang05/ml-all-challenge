package com.mercadolibre.mlcoreplatform.adapter.api;

import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionApiPort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class QuestionApiAdapter implements QuestionApiPort {

    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {
        // Mock implementation for testing
        return QuestionResponse.builder()
                .id("QUESTION-" + System.currentTimeMillis())
                .text(request.text())
                .status("UNANSWERED")
                .dateCreated(LocalDateTime.now())
                .build();
    }

    @Override
    public QuestionResponse answerQuestion(String questionId, String text) {
        // Mock implementation for testing
        return QuestionResponse.builder()
                .id(questionId)
                .text("Original question text")
                .answer(QuestionResponse.AnswerResponse.builder()
                        .text(text)
                        .status("ANSWERED")
                        .dateCreated(LocalDateTime.now())
                        .build())
                .status("ANSWERED")
                .dateCreated(LocalDateTime.now().minusHours(1))
                .build();
    }
}
