package com.mercadolibre.mlcoreplatform.domain.port.out;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.QuestionDto;

import java.util.List;

public interface QuestionPersistencePort {
    
    List<QuestionDto> findByItemId(String itemId);
    
    List<QuestionDto> findAll();
    
    List<QuestionDto> findBySellerId(Long sellerId);
    
    List<QuestionDto> findByStatus(String status);
    
    List<QuestionDto> findUnansweredQuestions();
}
