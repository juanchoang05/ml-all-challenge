package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.QuestionUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.request.QuestionRequest;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Endpoints para gestión de preguntas y respuestas")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionUseCase questionUseCase;

    @GetMapping
    @Operation(summary = "Obtener todas las preguntas", description = "Retorna lista de preguntas con paginación")
    public ResponseEntity<List<QuestionResponse>> getAllQuestions(
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<QuestionResponse> questions = questionUseCase.getAllQuestions(offset, limit);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pregunta por ID", description = "Retorna información detallada de una pregunta")
    public ResponseEntity<QuestionResponse> getQuestionById(
            @Parameter(description = "ID de la pregunta") @PathVariable String id) {
        QuestionResponse question = questionUseCase.getQuestionById(id);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar preguntas por producto", description = "Retorna las preguntas de un producto específico")
    public ResponseEntity<List<QuestionResponse>> getQuestionsByItem(
            @Parameter(description = "ID del producto") @RequestParam String item,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        List<QuestionResponse> questions = questionUseCase.getQuestionsByItem(item, offset, limit);
        return ResponseEntity.ok(questions);
    }

    @PostMapping
    @Operation(summary = "Crear nueva pregunta", description = "Crea una nueva pregunta para un producto")
    public ResponseEntity<QuestionResponse> createQuestion(
            @RequestBody QuestionRequest request) {
        QuestionResponse question = questionUseCase.createQuestion(request);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/{id}/answers")
    @Operation(summary = "Responder pregunta", description = "Responde a una pregunta específica")
    public ResponseEntity<QuestionResponse> answerQuestion(
            @Parameter(description = "ID de la pregunta") @PathVariable String id,
            @Parameter(description = "Texto de la respuesta") @RequestParam String text) {
        QuestionResponse question = questionUseCase.answerQuestion(id, text);
        return ResponseEntity.ok(question);
    }
}
