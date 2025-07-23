package com.mercadolibre.mlcoreplatform.entrypoints.rest.controller;

import com.mercadolibre.mlcoreplatform.domain.port.in.SearchUseCase;
import com.mercadolibre.mlcoreplatform.domain.port.in.dto.SearchCriteria;
import com.mercadolibre.mlcoreplatform.entrypoints.rest.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites/{siteId}/search")
@Tag(name = "Search", description = "Endpoints para búsqueda de productos")
@RequiredArgsConstructor
public class SearchController {

    private final SearchUseCase searchUseCase;

    @GetMapping
    @Operation(summary = "Buscar productos", description = "Busca productos en un sitio específico según criterios de búsqueda")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @Parameter(description = "ID del sitio") @PathVariable String siteId,
            @Parameter(description = "Término de búsqueda") @RequestParam(required = false) String q,
            @Parameter(description = "ID de categoría") @RequestParam(required = false) String categoryId,
            @Parameter(description = "Condición del producto") @RequestParam(required = false) String condition,
            @Parameter(description = "Precio mínimo") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Precio máximo") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Tipo de envío") @RequestParam(required = false) String shipping,
            @Parameter(description = "Tipo de ordenamiento") @RequestParam(required = false) String sort,
            @Parameter(description = "Número de página") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "50") int limit) {
        
        SearchCriteria criteria = SearchCriteria.builder()
                .siteId(siteId)
                .query(q)
                .categoryId(categoryId)
                .condition(condition)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .shipping(shipping)
                .sort(sort)
                .offset(offset)
                .limit(limit)
                .build();
        
        List<ProductResponse> products = searchUseCase.searchProducts(criteria);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/trends")
    @Operation(summary = "Obtener tendencias", description = "Retorna las tendencias de búsqueda para un sitio")
    public ResponseEntity<List<String>> getTrends(
            @Parameter(description = "ID del sitio") @PathVariable String siteId) {
        // TODO: Implementar lógica de negocio
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Obtener sugerencias", description = "Retorna sugerencias de búsqueda para un término")
    public ResponseEntity<List<String>> getSearchSuggestions(
            @Parameter(description = "ID del sitio") @PathVariable String siteId,
            @Parameter(description = "Término parcial") @RequestParam String q) {
        // TODO: Implementar lógica de negocio
        return ResponseEntity.ok(List.of());
    }
}
