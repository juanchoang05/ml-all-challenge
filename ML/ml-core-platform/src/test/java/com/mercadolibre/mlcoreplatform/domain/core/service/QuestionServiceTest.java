package com.mercadolibre.mlcoreplatform.domain.core.service;

import com.mercadolibre.mlcoreplatform.adapter.persistence.dto.QuestionDto;
import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionApiPort;
import com.mercadolibre.mlcoreplatform.domain.port.out.QuestionPersistencePort;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionPersistencePort questionPersistencePort;
    
    @Mock
    private QuestionApiPort questionApiPort;
    
    private QuestionService questionService;
    
    @BeforeEach
    void setUp() {
        questionService = new QuestionService(questionPersistencePort, questionApiPort);
    }
    
    @Test
    void getAllQuestions_ShouldReturnPaginatedQuestions() {
        // Given
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDto(1L, "¿Tiene garantía?"),
            createMockQuestionDto(2L, "¿Cuándo llega?"),
            createMockQuestionDto(3L, "¿Es original?")
        );
        when(questionPersistencePort.findAll()).thenReturn(mockQuestions);
        
        // When
        List<QuestionResponse> result = questionService.getAllQuestions(0, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("¿Tiene garantía?", result.get(0).text());
        assertEquals("2", result.get(1).id());
        assertEquals("¿Cuándo llega?", result.get(1).text());
        verify(questionPersistencePort).findAll();
    }
    
    @Test
    void getAllQuestions_WithOffset_ShouldSkipCorrectNumber() {
        // Given
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDto(1L, "¿Tiene garantía?"),
            createMockQuestionDto(2L, "¿Cuándo llega?"),
            createMockQuestionDto(3L, "¿Es original?")
        );
        when(questionPersistencePort.findAll()).thenReturn(mockQuestions);
        
        // When
        List<QuestionResponse> result = questionService.getAllQuestions(1, 2);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("2", result.get(0).id());
        assertEquals("3", result.get(1).id());
        verify(questionPersistencePort).findAll();
    }
    
    @Test
    void getAllQuestions_WhenNoQuestions_ShouldReturnEmptyList() {
        // Given
        when(questionPersistencePort.findAll()).thenReturn(Collections.emptyList());
        
        // When
        List<QuestionResponse> result = questionService.getAllQuestions(0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(questionPersistencePort).findAll();
    }
    
    @Test
    void getQuestionById_WhenQuestionExists_ShouldReturnQuestion() {
        // Given
        String questionId = "1";
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDto(1L, "¿Tiene garantía?"),
            createMockQuestionDto(2L, "¿Cuándo llega?")
        );
        when(questionPersistencePort.findAll()).thenReturn(mockQuestions);
        
        // When
        QuestionResponse result = questionService.getQuestionById(questionId);
        
        // Then
        assertNotNull(result);
        assertEquals(questionId, result.id());
        assertEquals("¿Tiene garantía?", result.text());
        verify(questionPersistencePort).findAll();
    }
    
    @Test
    void getQuestionById_WhenQuestionNotFound_ShouldThrowException() {
        // Given
        String questionId = "999";
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDto(1L, "¿Tiene garantía?")
        );
        when(questionPersistencePort.findAll()).thenReturn(mockQuestions);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> questionService.getQuestionById(questionId));
        assertEquals("Question not found with id: 999", exception.getMessage());
        verify(questionPersistencePort).findAll();
    }
    
    @Test
    void getQuestionsByItem_ShouldReturnItemQuestions() {
        // Given
        String itemId = "MLA123456";
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDtoWithItem(1L, "¿Tiene garantía?", itemId),
            createMockQuestionDtoWithItem(2L, "¿Cuándo llega?", itemId)
        );
        when(questionPersistencePort.findByItemId(itemId)).thenReturn(mockQuestions);
        
        // When
        List<QuestionResponse> result = questionService.getQuestionsByItem(itemId, 0, 10);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals(itemId, result.get(0).itemId());
        assertEquals("2", result.get(1).id());
        assertEquals(itemId, result.get(1).itemId());
        verify(questionPersistencePort).findByItemId(itemId);
    }
    
    @Test
    void getQuestionsByItem_WithPagination_ShouldReturnLimitedResults() {
        // Given
        String itemId = "MLA123456";
        List<QuestionDto> mockQuestions = Arrays.asList(
            createMockQuestionDtoWithItem(1L, "¿Tiene garantía?", itemId),
            createMockQuestionDtoWithItem(2L, "¿Cuándo llega?", itemId),
            createMockQuestionDtoWithItem(3L, "¿Es original?", itemId)
        );
        when(questionPersistencePort.findByItemId(itemId)).thenReturn(mockQuestions);
        
        // When
        List<QuestionResponse> result = questionService.getQuestionsByItem(itemId, 1, 1);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("2", result.get(0).id());
        verify(questionPersistencePort).findByItemId(itemId);
    }
    
    @Test
    void getQuestionsByItem_WhenNoQuestions_ShouldReturnEmptyList() {
        // Given
        String itemId = "MLA123456";
        when(questionPersistencePort.findByItemId(itemId)).thenReturn(Collections.emptyList());
        
        // When
        List<QuestionResponse> result = questionService.getQuestionsByItem(itemId, 0, 10);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(questionPersistencePort).findByItemId(itemId);
    }
    
    @Test
    void createQuestion_ShouldCallApiAndReturnResponse() {
        // Given
        QuestionRequest request = QuestionRequest.builder()
            .text("¿Tiene garantía?")
            .itemId("MLA123456")
            .build();
        QuestionResponse expectedResponse = QuestionResponse.builder()
            .id("10")
            .text("¿Tiene garantía?")
            .status("UNANSWERED")
            .itemId("MLA123456")
            .build();
        when(questionApiPort.createQuestion(request)).thenReturn(expectedResponse);
        
        // When
        QuestionResponse result = questionService.createQuestion(request);
        
        // Then
        assertNotNull(result);
        assertEquals("10", result.id());
        assertEquals("¿Tiene garantía?", result.text());
        assertEquals("UNANSWERED", result.status());
        verify(questionApiPort).createQuestion(request);
    }
    
    @Test
    void answerQuestion_ShouldCallApiAndReturnResponse() {
        // Given
        String questionId = "1";
        String answerText = "Sí, tiene 1 año de garantía";
        QuestionResponse expectedResponse = QuestionResponse.builder()
            .id(questionId)
            .text("¿Tiene garantía?")
            .status("ANSWERED")
            .answer(QuestionResponse.AnswerResponse.builder()
                .text(answerText)
                .status("ACTIVE")
                .build())
            .build();
        when(questionApiPort.answerQuestion(questionId, answerText)).thenReturn(expectedResponse);
        
        // When
        QuestionResponse result = questionService.answerQuestion(questionId, answerText);
        
        // Then
        assertNotNull(result);
        assertEquals(questionId, result.id());
        assertEquals("ANSWERED", result.status());
        assertNotNull(result.answer());
        assertEquals(answerText, result.answer().text());
        assertEquals("ACTIVE", result.answer().status());
        verify(questionApiPort).answerQuestion(questionId, answerText);
    }
    
    @Test
    void mapToResponse_ShouldMapAllFields() {
        // Given
        QuestionDto questionDto = createCompleteQuestionDto();
        when(questionPersistencePort.findAll()).thenReturn(Arrays.asList(questionDto));
        
        // When
        QuestionResponse result = questionService.getQuestionById("1");
        
        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("¿Tiene garantía?", result.text());
        assertEquals("ANSWERED", result.status());
        assertNotNull(result.dateCreated());
        assertEquals("MLA123456", result.itemId());
        assertEquals("12345", result.sellerId());
        assertNotNull(result.answer());
        assertEquals("Sí, tiene garantía", result.answer().text());
        assertEquals("ACTIVE", result.answer().status());
        assertNotNull(result.answer().dateCreated());
    }
    
    @Test
    void mapToResponse_WithNullAnswer_ShouldHandleNull() {
        // Given
        QuestionDto questionDto = QuestionDto.builder()
            .id(1L)
            .text("¿Tiene garantía?")
            .status("UNANSWERED")
            .dateCreated(LocalDateTime.now())
            .itemId("MLA123456")
            .sellerId(12345L)
            .answer(null)
            .build();
        when(questionPersistencePort.findAll()).thenReturn(Arrays.asList(questionDto));
        
        // When
        QuestionResponse result = questionService.getQuestionById("1");
        
        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("UNANSWERED", result.status());
        assertNull(result.answer());
    }
    
    @Test
    void mapToResponse_WithNullSellerId_ShouldHandleNull() {
        // Given
        QuestionDto questionDto = QuestionDto.builder()
            .id(1L)
            .text("¿Tiene garantía?")
            .status("UNANSWERED")
            .dateCreated(LocalDateTime.now())
            .itemId("MLA123456")
            .sellerId(null)
            .answer(null)
            .build();
        when(questionPersistencePort.findAll()).thenReturn(Arrays.asList(questionDto));
        
        // When
        QuestionResponse result = questionService.getQuestionById("1");
        
        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertNull(result.sellerId());
    }
    
    private QuestionDto createMockQuestionDto(Long id, String text) {
        return QuestionDto.builder()
            .id(id)
            .text(text)
            .status("UNANSWERED")
            .dateCreated(LocalDateTime.now())
            .itemId("MLA123456")
            .sellerId(12345L)
            .answer(null)
            .build();
    }
    
    private QuestionDto createMockQuestionDtoWithItem(Long id, String text, String itemId) {
        return QuestionDto.builder()
            .id(id)
            .text(text)
            .status("UNANSWERED")
            .dateCreated(LocalDateTime.now())
            .itemId(itemId)
            .sellerId(12345L)
            .answer(null)
            .build();
    }
    
    private QuestionDto createCompleteQuestionDto() {
        QuestionDto.AnswerDto answer = QuestionDto.AnswerDto.builder()
            .text("Sí, tiene garantía")
            .status("ACTIVE")
            .dateCreated(LocalDateTime.of(2024, 1, 15, 14, 30))
            .build();
            
        return QuestionDto.builder()
            .id(1L)
            .text("¿Tiene garantía?")
            .status("ANSWERED")
            .dateCreated(LocalDateTime.of(2024, 1, 15, 10, 30))
            .itemId("MLA123456")
            .sellerId(12345L)
            .answer(answer)
            .build();
    }
}
