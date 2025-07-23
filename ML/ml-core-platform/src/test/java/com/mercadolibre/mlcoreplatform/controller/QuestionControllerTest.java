package com.mercadolibre.mlcoreplatform.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.QuestionUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.controller.QuestionController;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionUseCase questionUseCase;

    private QuestionController questionController;

    @BeforeEach
    void setUp() {
        questionController = new QuestionController(questionUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
    }

    @Test
    void testGetAllQuestions() throws Exception {
        // Arrange
        List<QuestionResponse> mockResponse = Arrays.asList(
                QuestionResponse.builder()
                        .id("QUESTION123")
                        .text("¿Incluye cargador?")
                        .status("ANSWERED")
                        .build(),
                QuestionResponse.builder()
                        .id("QUESTION456")
                        .text("¿Cuál es la garantía?")
                        .status("UNANSWERED")
                        .build()
        );

        when(questionUseCase.getAllQuestions(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/questions")
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("QUESTION123"))
                .andExpect(jsonPath("$[0].text").value("¿Incluye cargador?"))
                .andExpect(jsonPath("$[0].status").value("ANSWERED"))
                .andExpect(jsonPath("$[1].id").value("QUESTION456"))
                .andExpect(jsonPath("$[1].status").value("UNANSWERED"));
    }

    @Test
    void testGetAllQuestionsWithDefaultParams() throws Exception {
        // Arrange
        List<QuestionResponse> mockResponse = Arrays.asList(
                QuestionResponse.builder()
                        .id("QUESTION123")
                        .text("¿Incluye cargador?")
                        .build()
        );

        when(questionUseCase.getAllQuestions(0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetQuestionById() throws Exception {
        // Arrange
        String questionId = "QUESTION123";
        QuestionResponse mockResponse = QuestionResponse.builder()
                .id(questionId)
                .text("¿Incluye cargador?")
                .status("ANSWERED")
                .answer(QuestionResponse.AnswerResponse.builder()
                        .text("Sí, incluye cargador original")
                        .status("ACTIVE")
                        .build())
                .build();

        when(questionUseCase.getQuestionById(questionId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/questions/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(questionId))
                .andExpect(jsonPath("$.text").value("¿Incluye cargador?"))
                .andExpect(jsonPath("$.status").value("ANSWERED"))
                .andExpect(jsonPath("$.answer.text").value("Sí, incluye cargador original"));
    }

    @Test
    void testGetQuestionsByItem() throws Exception {
        // Arrange
        String itemId = "ITEM123";
        List<QuestionResponse> mockResponse = Arrays.asList(
                QuestionResponse.builder()
                        .id("QUESTION123")
                        .text("¿Incluye cargador?")
                        .itemId(itemId)
                        .build(),
                QuestionResponse.builder()
                        .id("QUESTION456")
                        .text("¿Cuál es la garantía?")
                        .itemId(itemId)
                        .build()
        );

        when(questionUseCase.getQuestionsByItem(itemId, 0, 50)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/questions/search")
                        .param("item", itemId)
                        .param("offset", "0")
                        .param("limit", "50"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("QUESTION123"))
                .andExpect(jsonPath("$[0].itemId").value(itemId))
                .andExpect(jsonPath("$[1].id").value("QUESTION456"))
                .andExpect(jsonPath("$[1].itemId").value(itemId));
    }

    @Test
    void testCreateQuestion() throws Exception {
        // Arrange
        QuestionResponse mockResponse = QuestionResponse.builder()
                .id("QUESTION123")
                .text("¿Incluye cargador?")
                .status("UNANSWERED")
                .itemId("ITEM123")
                .build();

        when(questionUseCase.createQuestion(any(QuestionRequest.class))).thenReturn(mockResponse);

        String requestBody = """
                {
                    "text": "¿Incluye cargador?",
                    "itemId": "ITEM123",
                    "fromId": "USER123"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("QUESTION123"))
                .andExpect(jsonPath("$.text").value("¿Incluye cargador?"))
                .andExpect(jsonPath("$.status").value("UNANSWERED"))
                .andExpect(jsonPath("$.itemId").value("ITEM123"));
    }

    @Test
    void testAnswerQuestion() throws Exception {
        // Arrange
        String questionId = "QUESTION123";
        String answerText = "Sí, incluye cargador original";
        QuestionResponse mockResponse = QuestionResponse.builder()
                .id(questionId)
                .text("¿Incluye cargador?")
                .status("ANSWERED")
                .answer(QuestionResponse.AnswerResponse.builder()
                        .text(answerText)
                        .status("ACTIVE")
                        .build())
                .build();

        when(questionUseCase.answerQuestion(questionId, answerText)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/questions/{id}/answers", questionId)
                        .param("text", answerText))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(questionId))
                .andExpect(jsonPath("$.status").value("ANSWERED"))
                .andExpect(jsonPath("$.answer.text").value(answerText));
    }
}
