package com.mercadolibre.mlcoreplatform.domain.port.in;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;

import java.util.List;

public interface QuestionUseCase {
    
    List<QuestionResponse> getAllQuestions(int offset, int limit);
    
    QuestionResponse getQuestionById(String questionId);
    
    List<QuestionResponse> getQuestionsByItem(String itemId, int offset, int limit);
    
    QuestionResponse createQuestion(QuestionRequest request);
    
    QuestionResponse answerQuestion(String questionId, String text);
}
