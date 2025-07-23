package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;

public interface QuestionApiPort {
    
    QuestionResponse createQuestion(QuestionRequest request);
    
    QuestionResponse answerQuestion(String questionId, String text);
}
