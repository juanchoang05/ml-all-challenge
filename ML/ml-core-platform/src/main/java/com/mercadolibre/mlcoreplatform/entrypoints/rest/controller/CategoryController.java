package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.CategoryUseCase;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "Endpoints para gestión de categorías")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Retorna lista de categorías")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryUseCase.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna información detallada de una categoría")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @Parameter(description = "ID de la categoría") @PathVariable String id) {
        CategoryResponse category = categoryUseCase.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/attributes")
    @Operation(summary = "Obtener atributos de categoría", description = "Retorna los atributos de una categoría específica")
    public ResponseEntity<List<CategoryResponse.AttributeResponse>> getCategoryAttributes(
            @Parameter(description = "ID de la categoría") @PathVariable String id) {
        List<CategoryResponse.AttributeResponse> attributes = categoryUseCase.getCategoryAttributes(id);
        return ResponseEntity.ok(attributes);
    }

    @GetMapping("/sites/{siteId}/categories")
    @Operation(summary = "Obtener categorías por sitio", description = "Retorna las categorías disponibles para un sitio específico")
    public ResponseEntity<List<CategoryResponse>> getCategoriesBySite(
            @Parameter(description = "ID del sitio") @PathVariable String siteId) {
        List<CategoryResponse> categories = categoryUseCase.getCategoriesBySite(siteId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/sites/{siteId}/category_predictor/predict")
    @Operation(summary = "Predecir categoría", description = "Predice la categoría más adecuada para un producto")
    public ResponseEntity<CategoryResponse> predictCategory(
            @Parameter(description = "ID del sitio") @PathVariable String siteId,
            @Parameter(description = "Título del producto") @RequestParam String title) {
        CategoryResponse category = categoryUseCase.predictCategory(siteId, title);
        return ResponseEntity.ok(category);
    }
}
