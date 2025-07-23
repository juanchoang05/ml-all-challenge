package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.QuestionDto;
import com.mercadolibre.mlcoreplatform.domain.port.in.QuestionUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionUseCase {
    
    private final QuestionPersistencePort questionPersistencePort;
    private final QuestionApiPort questionApiPort;
    
    @Override
    public List<QuestionResponse> getAllQuestions(int offset, int limit) {
        List<QuestionDto> questions = questionPersistencePort.findAll();
        return questions.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public QuestionResponse getQuestionById(String questionId) {
        // Por ahora buscaremos por ítem ya que el port no tiene findById
        List<QuestionDto> questions = questionPersistencePort.findAll();
        return questions.stream()
                .filter(q -> q.id().toString().equals(questionId))
                .findFirst()
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
    }
    
    @Override
    public List<QuestionResponse> getQuestionsByItem(String itemId, int offset, int limit) {
        List<QuestionDto> questions = questionPersistencePort.findByItemId(itemId);
        return questions.stream()
                .skip(offset)
                .limit(limit)
                .map(this::mapToResponse)
                .toList();
    }
    
    @Override
    public QuestionResponse createQuestion(QuestionRequest request) {
        return questionApiPort.createQuestion(request);
    }
    
    @Override
    public QuestionResponse answerQuestion(String questionId, String text) {
        return questionApiPort.answerQuestion(questionId, text);
    }
    
    private QuestionResponse mapToResponse(QuestionDto questionDto) {
        return QuestionResponse.builder()
                .id(questionDto.id().toString())
                .text(questionDto.text())
                .status(questionDto.status())
                .dateCreated(questionDto.dateCreated())
                .itemId(questionDto.itemId())
                .sellerId(questionDto.sellerId() != null ? questionDto.sellerId().toString() : null)
                .answer(questionDto.answer() != null ? 
                    QuestionResponse.AnswerResponse.builder()
                            .text(questionDto.answer().text())
                            .status(questionDto.answer().status())
                            .dateCreated(questionDto.answer().dateCreated())
                            .build() : null)
                .build();
    }
}
